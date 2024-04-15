// Consumes messages from the queue
package com.mydomain.adprocess.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.*;

public class MessageConsumer {
    private final static String QUEUE_NAME = "advertisement_queue";

    public void startConsuming() throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        // Set other connection properties, if necessary

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare(QUEUE_NAME, true, false, false, null);
        System.out.println(" [*] Waiting for ad details. To exit press CTRL+C");

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            AdDetails adDetails = deserializeAdDetails(message);
            // Process the ad details
            processAdDetails(adDetails);
            System.out.println(" [x] Received ad details: " + message);
        };

        channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> {
        });
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
        // Code to update the status in the workflow, such as marking as "Ready for
        // Review"
    }
}
