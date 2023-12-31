package com.redhat.kafka.bridge.commands.consumers.positions;

import picocli.CommandLine.Command;
import picocli.CommandLine.Spec;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.ParameterException;


@Command(name="positions", mixinStandardHelpOptions = true, 
         subcommands = {ConsumerPositionsCreateCommand.class,
                        ConsumerPositionsBeginningCommand.class,
                        ConsumerPositionsEndCommand.class
                        }, 
        synopsisSubcommandLabel = "COMMAND")
public class ConsumerPositionsCommand implements Runnable {
    @Spec CommandSpec spec;

    @Override
    public void run() {
        throw new ParameterException(spec.commandLine(), "Missing required subcommand");
    }
}
