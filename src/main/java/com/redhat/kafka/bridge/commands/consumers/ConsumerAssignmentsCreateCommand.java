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

@Command(name="assignments", mixinStandardHelpOptions = true)
public class ConsumerAssignmentsCreateCommand implements Runnable {

    private static final Logger LOG = Logger.getLogger(ConsumerAssignmentsCreateCommand.class);

    @Spec
    CommandSpec spec;
        
    @Inject
    KafkaBridgeServiceFactory kafkaBridgeServiceFactory;

    @Parameters(index="0", description="The group ID.")
    private String groupId;

    @Parameters(index="1", description="Consumer Name")
    private String consumerName;

    @Parameters(index="2", description="Assignment definition file")
    private String assignmentDefinitionFile;

    @Override
    public void run() {
        try {

            String body = Files.readString(Paths.get(assignmentDefinitionFile));

            kafkaBridgeServiceFactory.getKafkaBridgeService().createConsumerAssignments(groupId, consumerName, body);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
    }
}
