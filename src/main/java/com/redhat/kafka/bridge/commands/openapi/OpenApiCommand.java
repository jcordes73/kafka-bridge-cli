package com.redhat.kafka.bridge.commands.openapi;

import org.jboss.logging.Logger;

import com.redhat.kafka.bridge.rest.client.service.KafkaBridgeServiceFactory;

import jakarta.inject.Inject;
import picocli.CommandLine.Command;
import picocli.CommandLine.Spec;
import picocli.CommandLine.Model.CommandSpec;

@Command(name="openapi", mixinStandardHelpOptions = true, synopsisSubcommandLabel = "COMMAND")
public class OpenApiCommand implements Runnable {

    @Spec CommandSpec spec;

    private static final Logger LOG = Logger.getLogger(OpenApiCommand.class);
    
    @Inject
    KafkaBridgeServiceFactory kafkaBridgeServiceFactory;

    @Override
    public void run() {
        try {
            String response = kafkaBridgeServiceFactory.getKafkaBridgeService().getOpenApi();

            spec.commandLine().getOut().println(response);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
    }
}