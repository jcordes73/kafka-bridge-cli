package com.redhat.kafka.bridge.commands.consumers.subscriptions;

import java.nio.file.Files;
import java.nio.file.Paths;

import org.jboss.logging.Logger;

import com.redhat.kafka.bridge.rest.client.service.KafkaBridgeServiceFactory;

import jakarta.inject.Inject;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.Spec;

@Command(name="create", mixinStandardHelpOptions = true)
public class ConsumerSubscriptionCreateCommand implements Runnable {

    private static final Logger LOG = Logger.getLogger(ConsumerSubscriptionCreateCommand.class);

    @Spec
    CommandSpec spec;
        
    @Inject
    KafkaBridgeServiceFactory kafkaBridgeServiceFactory;

    @Parameters(index="0", description="The group ID.")
    private String groupId;

    @Parameters(index="1", description="Consumer Name")
    private String consumerName;

    @Parameters(index="2", description="Subscription definition file")
    private String subscriptionDefinitionFile;

    @Override
    public void run() {
        try {
            String body = Files.readString(Paths.get(subscriptionDefinitionFile));
            kafkaBridgeServiceFactory.getKafkaBridgeService().createConsumerSubscription(groupId, consumerName, body);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
    }
}
