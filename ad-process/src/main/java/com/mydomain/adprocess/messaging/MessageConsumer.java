// Consumes messages from the queue
package com.mydomain.adprocess.messaging;

import java.io.IOException;
import java.util.concurrent.TimeoutException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.*;

public class MessageConsumer {
    private final static String QUEUE_NAME = "advertisement_queue";
    private Connection connection;
    private Channel channel;
    private volatile boolean shouldConsume = true; // Flag to control the message consuming loop

    public void startConsuming() throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        // Set other connection properties, if necessary

        connection = factory.newConnection();
        channel = connection.createChannel();

        channel.queueDeclare(QUEUE_NAME, true, false, false, null);
        System.out.println(" [*] Waiting for ad details");

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            if (shouldConsume) { // Check if we should still be consuming messages
                String message = new String(delivery.getBody(), "UTF-8");
                AdDetails adDetails = deserializeAdDetails(message);
                // Process the ad details
                processAdDetails(adDetails);
                System.out.println(" [x] Received ad details: " + adDetails);
            }
        };

        channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> {});
    }

    // Method to stop consuming messages and clean up resources
    public void stopConsuming() throws IOException, TimeoutException {
        shouldConsume = false; // Signal to stop consuming messages
        if (channel != null && channel.isOpen()) {
            channel.close();
        }
        if (connection != null && connection.isOpen()) {
            connection.close();
        }
    }

    private AdDetails deserializeAdDetails(String message) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(message, AdDetails.class);
    }

    // Process the ad details
    private void processAdDetails(AdDetails adDetails) {
        if (validateAdDetails(adDetails)) {
            storeAdDetailsInDatabase(adDetails);
            updateWorkflowStatus(adDetails);
        } else {
            System.out.println("Received invalid ad details: " + adDetails);
            // Handle invalid ad details appropriately
        }
    }

    private boolean validateAdDetails(AdDetails adDetails) {
        // Validation logic here
        // Return true if valid, false otherwise
        return adDetails.getAdContent() != null && !adDetails.getAdContent().trim().isEmpty(); // Simple example
    }

    private void storeAdDetailsInDatabase(AdDetails adDetails) {
        // Code to store the ad details in the database
    }

    private void updateWorkflowStatus(AdDetails adDetails) {
        // Code to update the status in the workflow, such as marking as "Ready for Review"
    }
}
