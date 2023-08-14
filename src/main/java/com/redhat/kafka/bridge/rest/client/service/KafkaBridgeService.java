package com.redhat.kafka.bridge.rest.client.service;

import java.util.List;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import io.quarkus.rest.client.reactive.ClientExceptionMapper;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/")
@RegisterRestClient(configKey = "kafkabridge")
public interface KafkaBridgeService {


    @ClientExceptionMapper
    static RuntimeException toException(Response response) {

        if (response.getStatus() == 400){
            return new RuntimeException("Bad Request");
        } else if (response.getStatus() == 401) {
            return new RuntimeException("Unauthorized");
        } else if (response.getStatus() == 403) {
            return new RuntimeException("Access denied");
        } else if (response.getStatus() == 404) {
            return new RuntimeException("Entry not found");
        } else if (response.getStatus() == 409) {
            return new RuntimeException("Conflict");  
        } else if (response.getStatus() == 422) {
            String responseText = null;

            if (response.hasEntity()){
                responseText = response.readEntity(String.class);
            }
            return new RuntimeException("Malformed request " + responseText);
        } else if (response.getStatus() == 500) {
            return new RuntimeException("The remote service responded with HTTP 500");
        }

        return null;
    }


    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public String getInfo(
    );

    @POST
    @Path("/consumers/{groupId}")
    @Consumes("application/vnd.kafka.json.v2+json")
    @Produces("application/vnd.kafka.v2+json")
    public void createConsumer(
        @PathParam("groupId") String groupId,
        String body
    );

    @POST
    @Path("/consumers/{groupId}/instances/{consumerName}/assignments")
    @Consumes("application/vnd.kafka.v2+json")
    @Produces("application/vnd.kafka.v2+json")
    public void createConsumerAssignments(
        @PathParam("groupId") String groupId,
        @PathParam("consumerName") String consumerName,
        String body
    );

    @POST
    @Path("/consumers/{groupId}/instances/{consumerName}/offsets")
    @Consumes("application/vnd.kafka.v2+json")
    @Produces("application/vnd.kafka.v2+json")
    public void createConsumerOffsets(
        @PathParam("groupId") String groupId,
        @PathParam("consumerName") String consumerName,
        String body
    );

    @POST
    @Path("/consumers/{groupId}/instances/{consumerName}/positions")
    @Consumes("application/vnd.kafka.v2+json")
    @Produces("application/vnd.kafka.v2+json")
    public void createConsumerPositions(
        @PathParam("groupId") String groupId,
        @PathParam("consumerName") String consumerName,
        String body
    );

    @POST
    @Path("/consumers/{groupId}/instances/{consumerName}/positions/beginning")
    @Consumes("application/vnd.kafka.v2+json")
    @Produces("application/vnd.kafka.v2+json")
    public void createConsumerPositionsBeginning(
        @PathParam("groupId") String groupId,
        @PathParam("consumerName") String consumerName,
        String body
    );

    @POST
    @Path("/consumers/{groupId}/instances/{consumerName}/positions/end")
    @Consumes("application/vnd.kafka.v2+json")
    @Produces("application/vnd.kafka.v2+json")
    public void createConsumerPositionsEnd(
        @PathParam("groupId") String groupId,
        @PathParam("consumerName") String consumerName,
        String body
    );

    @DELETE
    @Path("/consumers/{groupId}/instances/{consumerName}")
    @Consumes("application/vnd.kafka.v2+json")
    @Produces("application/vnd.kafka.v2+json")
    public void deleteConsumer(
        @PathParam("groupId") String groupId,
        @PathParam("consumerName") String consumerName
    );

    @GET
    @Path("/consumers/{groupId}/instances/{consumerName}/records")
    @Produces("application/vnd.kafka.json.v2+json")
    public String getConsumerRecords(
        @PathParam("groupId") String groupId,
        @PathParam("consumerName") String consumerName,
        @QueryParam("max_bytes") Integer maxBytes,
        @QueryParam("timeout")Integer timeout
    );

    @POST
    @Path("/consumers/{groupId}/instances/{consumerName}/subscription")
    @Consumes("application/vnd.kafka.v2+json")
    @Produces("application/vnd.kafka.v2+json")
    public void createConsumerSubscription(
        @PathParam("groupId") String groupId,
        @PathParam("consumerName") String consumerName,
        String body
    );

    @GET
    @Path("/consumers/{groupId}/instances/{consumerName}/subscription")
    @Produces("application/vnd.kafka.v2+json")
    public String getConsumerSubscriptionTopics(
        @PathParam("groupId") String groupId,
        @PathParam("consumerName") String consumerName
    );

    @DELETE
    @Path("/consumers/{groupId}/instances/{consumerName}/subscription")
    public void deleteConsumerSubscription(
        @PathParam("groupId") String groupId,
        @PathParam("consumerName") String consumerName
    );

    @GET
    @Path("/topics")
    @Produces("application/vnd.kafka.v2+json")
    public String getTopics();

    @POST
    @Path("/topics/{topicName}")
    @Consumes("application/vnd.kafka.json.v2+json")
    @Produces("application/vnd.kafka.v2+json")
    public void createTopic(
        @PathParam("topicName") String topicName,
        @QueryParam("async") Boolean async,
        String body
    );

    @GET
    @Path("/topics/{topicName}")
    @Produces("application/vnd.kafka.v2+json")
    public String getTopic(
        @PathParam("topicName") String topicName
    );

    @GET
    @Path("/topics/{topicName}/partitions")
    @Produces("application/vnd.kafka.v2+json")
    public String getPartitions(
        @PathParam("topicName") String topicName
    );

    @GET
    @Path("/topics/{topicName}/partitions/{partitionId}")
    @Produces("application/vnd.kafka.v2+json")
    public String getPartition(
        @PathParam("topicName") String topicName,
        @PathParam("partitionId") String partitionId
    );

    @POST
    @Path("/topics/{topicName}/partitions/{partitionId}")
    @Consumes("application/vnd.kafka.json.v2+json")
    @Produces("application/vnd.kafka.v2+json")
    public void createPartitionRecords(
        @PathParam("topicName") String topicName,
        @PathParam("partitionId") String partitionId,
        @QueryParam("async") Boolean async,
        String body
    );

    @GET
    @Path("/topics/{topicName}/partitions/{partitionId}/offsets")
    @Produces("application/vnd.kafka.v2+json")
    public String getPartitionOffset(
        @PathParam("topicName") String topicName,
        @PathParam("partitionId") String partitionId
    );

    @POST
    @Path("/topics/{topicName}/partitions/{partitionId}/offsets")
    @Consumes("application/vnd.kafka.v2+json")
    @Produces("application/vnd.kafka.v2+json")
    public void createPartitionOffset(
        @PathParam("topicName") String topicName,
        @PathParam("partitionId") String partitionId,
        String body
    );

    @GET
    @Path("/healthy")
    public String getHealth();

    @GET
    @Path("/openapi")
    @Produces("application/json")
    public String getOpenApi();

    @GET
    @Path("/ready")
    public String getReady();

    @GET
    @Path("/metrics")
    public String getMetrics();
}