package com.redhat.kafka.bridge.commands.topics;

import org.jboss.logging.Logger;

import com.redhat.kafka.bridge.rest.client.service.KafkaBridgeServiceFactory;

import jakarta.inject.Inject;
import picocli.CommandLine.Command;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.Spec;

@Command(name="list", mixinStandardHelpOptions = true)
public class TopicsListCommand implements Runnable {

    private static final Logger LOG = Logger.getLogger(TopicsListCommand.class);

    @Spec
    CommandSpec spec;
        
    @Inject
    KafkaBridgeServiceFactory kafkaBridgeServiceFactory;

    @Override
    public void run() {
        try {
            String response = kafkaBridgeServiceFactory.getKafkaBridgeService().getTopics();

            spec.commandLine().getOut().println(response);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
    }
}
