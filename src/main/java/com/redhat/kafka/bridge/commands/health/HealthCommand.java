package com.redhat.kafka.bridge.commands.health;

import org.jboss.logging.Logger;

import com.redhat.kafka.bridge.rest.client.service.KafkaBridgeServiceFactory;

import jakarta.inject.Inject;
import picocli.CommandLine.Command;
import picocli.CommandLine.Spec;
import picocli.CommandLine.Model.CommandSpec;

@Command(name="health", mixinStandardHelpOptions = true, synopsisSubcommandLabel = "COMMAND")
public class HealthCommand implements Runnable {

    @Spec CommandSpec spec;

    private static final Logger LOG = Logger.getLogger(HealthCommand.class);
    
    @Inject
    KafkaBridgeServiceFactory kafkaBridgeServiceFactory;

    @Override
    public void run() {
        try {
            kafkaBridgeServiceFactory.getKafkaBridgeService().getHealth();

            spec.commandLine().getOut().println("OK");
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
    }
}