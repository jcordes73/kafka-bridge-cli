package com.redhat.kafka.bridge.commands.topics.partitions;

import java.nio.file.Files;
import java.nio.file.Paths;

import org.jboss.logging.Logger;

import com.redhat.kafka.bridge.rest.client.service.KafkaBridgeServiceFactory;

import jakarta.inject.Inject;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.Spec;

@Command(name="records", mixinStandardHelpOptions = true)
public class PartitionRecordsCommand implements Runnable {

    private static final Logger LOG = Logger.getLogger(PartitionRecordsCommand.class);

    @Spec
    CommandSpec spec;
        
    @Inject
    KafkaBridgeServiceFactory kafkaBridgeServiceFactory;

    @Parameters(index="0", description="The topic name.")
    private String topicName;

    @Parameters(index="1", description="The partition ID.")
    private String partitionId;

    @Parameters(index="2", description="Records definition file")
    private String recordsDefinitionFile;

    @Option(names={"async"}, description="Whether to return immediately upon sending records, instead of waiting for metadata. No offsets will be returned if specified. Defaults to false.")
    private Boolean async;

    
    @Override
    public void run() {
        try {
            String body = Files.readString(Paths.get(recordsDefinitionFile));
            kafkaBridgeServiceFactory.getKafkaBridgeService().createPartitionRecords(topicName, partitionId, async, body);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
    }
}
