package com.redhat.kafka.bridge.commands.topics.partitions;

import picocli.CommandLine.Command;
import picocli.CommandLine.Spec;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.ParameterException;


@Command(name="partitions", mixinStandardHelpOptions = true, 
         subcommands = {PartitionsListCommand.class,
                        PartitionGetCommand.class,
                        PartitionRecordsCommand.class,
                        PartitionOffsetsGetCommand.class
                        }, 
        synopsisSubcommandLabel = "COMMAND")
public class PartitionsCommand implements Runnable {
    @Spec CommandSpec spec;

    @Override
    public void run() {
        throw new ParameterException(spec.commandLine(), "Missing required subcommand");
    }
}
