package com.redhat.kafka.bridge.commands.consumers.subscriptions;

import picocli.CommandLine.Command;
import picocli.CommandLine.Spec;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.ParameterException;


@Command(name="subscriptions", mixinStandardHelpOptions = true, 
         subcommands = {ConsumerSubscriptionCreateCommand.class,
                        ConsumerSubscriptionDeleteCommand.class,
                        ConsumerSubscriptionTopicsGetCommand.class
        }, 
        synopsisSubcommandLabel = "COMMAND")
public class ConsumerSubscriptionsCommand implements Runnable {
    @Spec CommandSpec spec;

    @Override
    public void run() {
        throw new ParameterException(spec.commandLine(), "Missing required subcommand");
    }
}
