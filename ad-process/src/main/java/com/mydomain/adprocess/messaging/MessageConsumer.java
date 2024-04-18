package com.mydomain.adprocess.messaging;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.*;

public class MessageConsumer {
    private ConnectionFactory factory;
    private Connection connection; // Connection to the RabbitMQ server
    private Channel channel; // Channel for communication
    private volatile boolean shouldConsume = true; // Flag to control the message consuming loop
    public static final String QUEUE_NAME = "advertisement_queue";

    // No-argument constructor for default use
    public MessageConsumer() {
        this.factory = new ConnectionFactory();
        this.factory.setHost("localhost");
        initConnectionAndChannel(); // Initialize the connection and channel
    }

    // Constructor that accepts a ConnectionFactory for flexibility and testing
    public MessageConsumer(ConnectionFactory factory) {
        this.factory = factory;
        initConnectionAndChannel(); // Initialize the connection and channel
    }

    private void initConnectionAndChannel() {
        try {
            if (this.connection == null || !this.connection.isOpen()) {
                this.connection = factory.newConnection();
            }
            if (this.channel == null || !this.channel.isOpen()) {
                this.channel = connection.createChannel();
                channel.queueDeclare(QUEUE_NAME, true, false, false, null);
            }
        } catch (Exception e) {
            System.err.println("Failed to initialize the connection or channel: " + e.getMessage());
        }
    }

    public void startConsuming() throws Exception {
        if (this.channel == null || !this.channel.isOpen()) {
            initConnectionAndChannel();  // Ensure the channel is open
        }
        System.out.println(" [*] Waiting for ad details");

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            if (shouldConsume) {
                String message = new String(delivery.getBody(), "UTF-8");
                System.out.println(" [x] Received '" + message + "'");
            }
        };
        channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> {});
    }

    public void stopConsuming() throws IOException, TimeoutException {
        shouldConsume = false;
        if (channel != null && channel.isOpen()) {
            channel.close();
        }
        if (connection != null && connection.isOpen()) {
            connection.close();
        }
    }
}
