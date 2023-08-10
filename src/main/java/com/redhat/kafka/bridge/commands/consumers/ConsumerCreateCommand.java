package com.redhat.kafka.bridge.commands.consumers;

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
public class ConsumerCreateCommand implements Runnable {

    private static final Logger LOG = Logger.getLogger(ConsumerCreateCommand.class);

    @Spec
    CommandSpec spec;
        
    @Inject
    KafkaBridgeServiceFactory kafkaBridgeServiceFactory;

    @Parameters(index="0", description="The group ID.")
    private String groupId;

    @Parameters(index="1", description="Group definition file")
    private String groupDefinitionFile;

    @Override
    public void run() {
        try {
            String body = Files.readString(Paths.get(groupDefinitionFile));
            kafkaBridgeServiceFactory.getKafkaBridgeService().createConsumer(groupId, body);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
    }
}
