package com.redhat.kafka.bridge.commands.topics;

import com.redhat.kafka.bridge.commands.topics.partitions.PartitionsCommand;

import picocli.CommandLine.Command;
import picocli.CommandLine.Spec;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.ParameterException;


@Command(name="topics", mixinStandardHelpOptions = true, 
         subcommands = {TopicsListCommand.class,
                        TopicGetCommand.class,
                        TopicCreateCommand.class,
                        PartitionsCommand.class
                        }, 
        synopsisSubcommandLabel = "COMMAND")
public class TopicsCommand implements Runnable {
    @Spec CommandSpec spec;

    @Override
    public void run() {
        throw new ParameterException(spec.commandLine(), "Missing required subcommand");
    }
}
