package com.redhat.kafka.bridge.commands.ready;

import org.jboss.logging.Logger;

import com.redhat.kafka.bridge.rest.client.service.KafkaBridgeServiceFactory;

import jakarta.inject.Inject;
import picocli.CommandLine.Command;
import picocli.CommandLine.Spec;
import picocli.CommandLine.Model.CommandSpec;

@Command(name="ready", mixinStandardHelpOptions = true, synopsisSubcommandLabel = "COMMAND")
public class ReadyCommand implements Runnable {

    @Spec CommandSpec spec;

    private static final Logger LOG = Logger.getLogger(ReadyCommand.class);
    
    @Inject
    KafkaBridgeServiceFactory kafkaBridgeServiceFactory;

    @Override
    public void run() {
        try {
            kafkaBridgeServiceFactory.getKafkaBridgeService().getReady();

            spec.commandLine().getOut().println("OK");
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
    }
}