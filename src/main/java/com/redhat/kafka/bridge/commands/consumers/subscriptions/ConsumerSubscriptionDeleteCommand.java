package com.redhat.kafka.bridge.commands.consumers.subscriptions;

import org.jboss.logging.Logger;

import com.redhat.kafka.bridge.rest.client.service.KafkaBridgeServiceFactory;

import jakarta.inject.Inject;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.Spec;

@Command(name="delete", mixinStandardHelpOptions = true)
public class ConsumerSubscriptionDeleteCommand implements Runnable {

    private static final Logger LOG = Logger.getLogger(ConsumerSubscriptionDeleteCommand.class);

    @Spec
    CommandSpec spec;
        
    @Inject
    KafkaBridgeServiceFactory kafkaBridgeServiceFactory;

    @Parameters(index="0", description="The group ID.")
    private String groupId;

    @Parameters(index="1", description="Consumer Name")
    private String consumerName;

    @Override
    public void run() {
        try {
            kafkaBridgeServiceFactory.getKafkaBridgeService().deleteConsumerSubscription(groupId, consumerName);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
    }
}
