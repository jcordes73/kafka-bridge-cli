package com.redhat.kafka.bridge.commands.topics.partitions;

import org.jboss.logging.Logger;

import com.redhat.kafka.bridge.rest.client.service.KafkaBridgeServiceFactory;

import jakarta.inject.Inject;
import picocli.CommandLine.Command;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.Parameters;
import picocli.CommandLine.Spec;

@Command(name="get", mixinStandardHelpOptions = true)
public class PartitionGetCommand implements Runnable {

    private static final Logger LOG = Logger.getLogger(PartitionGetCommand.class);

    @Spec
    CommandSpec spec;
        
    @Inject
    KafkaBridgeServiceFactory kafkaBridgeServiceFactory;

    @Parameters(index="0", description="The topic name.")
    private String topicName;

    @Parameters(index="1", description="The partition ID.")
    private String partitionId;


    @Override
    public void run() {
        try {
            String response = kafkaBridgeServiceFactory.getKafkaBridgeService().getPartition(topicName, partitionId);

            spec.commandLine().getOut().println(response);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
    }
}
