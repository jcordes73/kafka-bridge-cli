package com.redhat.kafka.bridge.commands.info;

import org.jboss.logging.Logger;

import com.redhat.kafka.bridge.rest.client.service.KafkaBridgeServiceFactory;

import jakarta.inject.Inject;
import picocli.CommandLine.Command;
import picocli.CommandLine.Spec;
import picocli.CommandLine.Model.CommandSpec;

@Command(name="info", mixinStandardHelpOptions = true, synopsisSubcommandLabel = "COMMAND")
public class InfoCommand implements Runnable {

    @Spec CommandSpec spec;

    private static final Logger LOG = Logger.getLogger(InfoCommand.class);
    
    @Inject
    KafkaBridgeServiceFactory kafkaBridgeServiceFactory;

    @Override
    public void run() {
        try {
            String response = kafkaBridgeServiceFactory.getKafkaBridgeService().getInfo();

            spec.commandLine().getOut().println(response);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
    }
}