package com.redhat.kafka.bridge.commands.consumers;

import org.jboss.logging.Logger;

import com.redhat.kafka.bridge.rest.client.service.KafkaBridgeServiceFactory;

import jakarta.inject.Inject;
import picocli.CommandLine.Command;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.Parameters;
import picocli.CommandLine.Spec;

@Command(name="delete", mixinStandardHelpOptions = true)
public class ConsumerDeleteCommand implements Runnable {

    private static final Logger LOG = Logger.getLogger(ConsumerDeleteCommand.class);

    @Spec
    CommandSpec spec;
        
    @Inject
    KafkaBridgeServiceFactory kafkaBridgeServiceFactory;

    @Parameters(index="0", description="The group ID.")
    private String groupId;

    @Parameters(index="1", description="The consumer name.")
    private String consumerId;


    @Override
    public void run() {
        try {
            kafkaBridgeServiceFactory.getKafkaBridgeService().deleteConsumer(groupId, consumerId);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
    }
}
