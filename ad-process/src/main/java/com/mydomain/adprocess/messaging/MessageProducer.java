package com.mydomain.adprocess.messaging;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;

public class MessageProducer {
    private ConnectionFactory factory;
    private Channel channel;
    private Connection connection;
    public static final String QUEUE_NAME = "advertisement_queue";

    // Default constructor initializes the factory and configures it to use "localhost"
    public MessageProducer() {
        this.factory = new ConnectionFactory();
        this.factory.setHost("localhost");
        initChannel(); // Initialize the channel during the construction of the producer
    }

    // Constructor that accepts a ConnectionFactory for flexibility and testing
    public MessageProducer(ConnectionFactory factory) {
        this.factory = factory;
        initChannel(); // Initialize the channel during the construction of the producer
    }

    private void initChannel() {
        try {
            if (this.connection == null || !this.connection.isOpen()) {
                this.connection = factory.newConnection();
            }
            if (this.channel == null || !this.channel.isOpen()) {
                this.channel = connection.createChannel();
                channel.queueDeclare(QUEUE_NAME, true, false, false, null);
            }
        } catch (Exception e) {
            System.err.println("Failed to initialize the channel: " + e.getMessage());
        }
    }

    public void sendAdDetails(AdDetails adDetails) throws Exception {
        if (this.channel == null || !this.channel.isOpen()) {
            initChannel();  // Ensure the channel is open
        }
        String message = serializeAdDetails(adDetails);
        this.channel.basicPublish("", QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
        System.out.println(" [x] Sent ad details: " + adDetails);
    }

    private String serializeAdDetails(AdDetails adDetails) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(adDetails);
    }
}
