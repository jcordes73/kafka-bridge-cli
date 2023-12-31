package com.redhat.kafka.bridge;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.microprofile.config.Config;
import org.jline.reader.EndOfFileException;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.MaskingCallback;
import org.jline.reader.Parser;
import org.jline.reader.UserInterruptException;
import org.jline.reader.impl.DefaultParser;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.console.SystemRegistry;
import org.jline.console.impl.SystemRegistryImpl;

import com.redhat.kafka.bridge.commands.consumers.ConsumerCommand;
import com.redhat.kafka.bridge.commands.health.HealthCommand;
import com.redhat.kafka.bridge.commands.info.InfoCommand;
import com.redhat.kafka.bridge.commands.metrics.MetricsCommand;
import com.redhat.kafka.bridge.commands.openapi.OpenApiCommand;
import com.redhat.kafka.bridge.commands.ready.ReadyCommand;
import com.redhat.kafka.bridge.commands.topics.TopicsCommand;
import com.redhat.kafka.bridge.helpers.JsonPathExecution;
import com.redhat.kafka.bridge.helpers.JsonToXmlConverter;
import com.redhat.kafka.bridge.helpers.XPathExecution;

import io.quarkus.picocli.runtime.annotations.TopCommand;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;
import jakarta.inject.Inject;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.shell.jline3.PicocliCommands;

@QuarkusMain
@TopCommand
@Command(name="",mixinStandardHelpOptions = true,
         subcommands = {InfoCommand.class,
                        HealthCommand.class,
                        OpenApiCommand.class,
                        ReadyCommand.class,
                        MetricsCommand.class,
                        ConsumerCommand.class,
                        TopicsCommand.class
                        },
                        synopsisSubcommandLabel = "COMMAND")
public class KafkaBridge implements Runnable, QuarkusApplication {

    @Inject
    Config config;
    
    @Inject
    CommandLine.IFactory factory;

    @Inject
    XPathExecution xPathExecution;

    @Inject
    JsonToXmlConverter jsonToXmlConverter;

    @Inject
    JsonPathExecution jsonpathExecution;

    private HashMap<String,String> variables = new HashMap<String,String>();
    
    @Override
    public void run() {
        CommandLine commandLine = new CommandLine(this, factory);
        commandLine.usage(System.out);
    }

    @Override
    public int run(String... args) throws Exception {
        CommandLine commandLine = new CommandLine(this, factory);

        int exitCode = -1;

        if (args.length > 0 && "-f".equals(args[0])) {
            commandLine.setTrimQuotes(false);
            exitCode = runBatch(commandLine, args);
        } else if (args.length > 0 && "-i".equals(args[0])) {
            runInteractive(commandLine);
        } else {
            exitCode = executeLine(new PrintWriter(System.out), commandLine, String.join(" ", args));
        }

        return exitCode;
    }

    private int runBatch(CommandLine commandLine, String... args) throws Exception {
        int exitCode = -1;

        BufferedReader reader = new BufferedReader(new FileReader(args[1]));

        String line = reader.readLine();

        PrintWriter pw = new PrintWriter(System.out);

        while (line != null) {
            if (!line.startsWith("#") && !line.isBlank()){
                if (line.startsWith("echo")){
                    System.out.println(replaceVariables(line.substring(5)));
                } else {
                    exitCode = executeLine(pw, commandLine, line);
                }
            }
            line = reader.readLine();
        }

        reader.close();

        return exitCode;
    }

    private int runInteractive(CommandLine commandLine) throws Exception {
        int exitCode = -1;

        Supplier<Path> workDir = () -> Paths.get(System.getProperty("user.dir"));

        Parser parser = new DefaultParser();

        PicocliCommands picocliCommands = new PicocliCommands(commandLine);
        Terminal terminal = TerminalBuilder.builder().build();

        SystemRegistry systemRegistry = new SystemRegistryImpl(parser, terminal, workDir, null);
		systemRegistry.setCommandRegistries(picocliCommands);
		systemRegistry.register("help", picocliCommands);

        LineReader lineReader = LineReaderBuilder.builder().terminal(terminal).completer(systemRegistry.completer()).parser(parser).variable(LineReader.LIST_MAX, 50).build();
        
        String prompt = "kafka-bridge>";

        boolean recording = false;
        FileWriter fw = null;

        while (true){
            String line = null;

            try {
                line = lineReader.readLine(prompt);

                if ("exit".equals(line)){
                    terminal.close();
                    break;
                } else if (line.startsWith("echo")){
                    terminal.writer().print(replaceVariables(line.substring(5)));
                } else if (!line.startsWith("recording")){
                    exitCode = executeLine(terminal.writer(), commandLine, line);

                    if (exitCode != 0){
                        continue;
                    }
                }

                if (!line.startsWith("recording") && recording){
                    String result = lineReader.readLine("kafka-bridge>Save line (y/n)? ",null,new MaskingCallback() {
                        @Override
                        public String display(String line) {
                            return line;
                        }

                        @Override
                        public String history(String line) {
                            return null;
                        }
                    }, "y");
                    
                    if ("y".equals(result)){
                        if (fw != null){
                            fw.write(line);
                            fw.write("\n");
                        }
                    }
                }

                if (line.startsWith("recording start")){
                    recording = true;

                    if (fw == null){
                        String[] split = line.split(" ");

                        if (split.length >= 3){
                            fw = new FileWriter(split[2]);
                        } else {
                            terminal.writer().println("Please specify a file name for the recording");    
                        }
                    } else {
                        terminal.writer().println("Already recording, stop recording first");
                    }
                } else if (line.startsWith("recording stop")){
                    recording = false;

                    if (fw != null){
                        fw.flush();
                        fw.close();

                        fw = null;
                    } else {
                        terminal.writer().println("Not recording, start recording first");
                    }
                }
            } catch (UserInterruptException e){
            } catch (EndOfFileException e) {
                return 0;
            } catch (Exception e){
                terminal.writer().println(e.getMessage());
            }
        }

        return exitCode;
    }

    private int executeLine(PrintWriter pw, CommandLine commandLine, String line) throws Exception {
        String bridgeCommand = replaceVariables(line);

        String variableName = null;
        String filterCommand = null;

        if (bridgeCommand.startsWith("assign variable")){
            int commandPos = bridgeCommand.indexOf("=");

            variableName = bridgeCommand.substring(16, commandPos);
            bridgeCommand = bridgeCommand.substring(commandPos+1);
        }

        int filterIndex = bridgeCommand.indexOf("|");

        if (filterIndex > 0) {
            filterCommand = bridgeCommand.substring(filterIndex+1);

            bridgeCommand = bridgeCommand.substring(0, filterIndex);
        }


        ArrayList<String> parameters = new ArrayList<String>();
        
        String regex = "\"([^\"]*)\"|(\\S+)";

        Matcher m = Pattern.compile(regex).matcher(bridgeCommand);
        while (m.find()) {
            if (m.group(1) != null) {
                parameters.add(m.group(1));
            } else {
                parameters.add(m.group(2));
            }
        }

        StringWriter sw = new StringWriter();                
        commandLine.setOut(new PrintWriter(sw));
        int exitCode = commandLine.execute(parameters.toArray(new String[parameters.size()]));
        commandLine.setOut(pw);

        String result = sw.toString();

        if (filterCommand != null){

            String[] filters = null;
            
            if (filterCommand.contains("|"))
                filters = filterCommand.split("\\|");
            else
                filters = new String[]{filterCommand};

            for (int i=0; i < filters.length; i++){
                String filter = filters[i];

                if (filter.startsWith("xpath")){
                    String xpathQuery = filter.substring(6);

                    result = xPathExecution.execute(result, xpathQuery);
                } else if (filter.startsWith("jsonpath")){
                    String jsonpathQuery = filter.substring(9);

                    result = jsonpathExecution.execute(result, jsonpathQuery);
                } else if (filter.startsWith("prettyprint")){
                    if (result.startsWith("<"))
                        result = xPathExecution.prettyPrint(result);
                    else if (result.startsWith("{"))
                        result = jsonpathExecution.prettyPrint(result);
                } else if (filter.startsWith("json2xml")){
                    result = jsonToXmlConverter.convert(result);
                }
          }
        }

        if (variableName != null){
            variables.put(variableName, result);
        } else {
            System.out.print(result);
        }

        return exitCode;
    }

    private String replaceVariables(String text){
        Pattern pattern = Pattern.compile("\\$\\{(.+?)\\}");
        Matcher matcher = pattern.matcher(text);
        
        StringBuilder builder = new StringBuilder();
        int i = 0;
        while (matcher.find()) {
            String variableName = matcher.group(1);
            String replacement = getVariable(variableName);

            builder.append(text.substring(i, matcher.start()));
            if (replacement == null)
                builder.append(matcher.group(0));
            else
                builder.append(replacement);
            i = matcher.end();
        }
        builder.append(text.substring(i, text.length()));
        return builder.toString();
    }

    private String getVariable(String variableName){
        String variableValue = null;

        if (variables.containsKey(variableName)){
            variableValue = variables.get(variableName);
        } else {
            variableValue = System.getenv(variableName);
        }

        return variableValue;
    }
}