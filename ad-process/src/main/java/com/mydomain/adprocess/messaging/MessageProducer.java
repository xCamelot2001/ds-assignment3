// Produces messages to the queue
package com.mydomain.adprocess.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class MessageProducer {
    private final static String QUEUE_NAME = "advertisement_queue";

    public void sendAdDetails(AdDetails adDetails) throws Exception {
        String message = serializeAdDetails(adDetails);

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        // Set other connection properties, if necessary

        try (Connection connection = factory.newConnection();
                Channel channel = connection.createChannel()) {
            channel.queueDeclare(QUEUE_NAME, true, false, false, null);
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
            System.out.println(" [x] Sent ad details: " + message);
        }
    }

    private String serializeAdDetails(AdDetails adDetails) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(adDetails);
    }
}
