package com.redhat.kafka.bridge.commands.consumers;

import com.redhat.kafka.bridge.commands.consumers.positions.ConsumerPositionsCommand;
import com.redhat.kafka.bridge.commands.consumers.subscriptions.ConsumerSubscriptionsCommand;

import picocli.CommandLine.Command;
import picocli.CommandLine.Spec;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.ParameterException;


@Command(name="consumers", mixinStandardHelpOptions = true, 
         subcommands = {ConsumerCreateCommand.class,
                        ConsumerDeleteCommand.class,
                        ConsumerRecordsGetCommand.class,
                        ConsumerAssignmentsCreateCommand.class,
                        ConsumerOffsetsCreateCommand.class,
                        ConsumerPositionsCommand.class,
                        ConsumerSubscriptionsCommand.class
                        }, 
        synopsisSubcommandLabel = "COMMAND")
public class ConsumerCommand implements Runnable {
    @Spec CommandSpec spec;

    @Override
    public void run() {
        throw new ParameterException(spec.commandLine(), "Missing required subcommand");
    }
}
