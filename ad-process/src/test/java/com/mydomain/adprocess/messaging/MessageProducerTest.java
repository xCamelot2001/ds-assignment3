package com.mydomain.adprocess.messaging;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import static org.mockito.Mockito.*;

import java.util.Date; // Import for the Date class

public class MessageProducerTest {

    @Mock private ConnectionFactory factory;
    @Mock private Connection connection;
    @Mock private Channel channel;
    private MessageProducer producer;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        when(factory.newConnection()).thenReturn(connection);
        when(connection.createChannel()).thenReturn(channel);
        producer = new MessageProducer(factory); // Initialize with the mock factory
    }

    @Test
    public void testSendAdDetails() throws Exception {
        // Create a sample AdDetails object with required fields
        AdDetails adDetails = new AdDetails("Ad1", "Advertiser1", new Date(), 
        "Content", "Placement", false, "", "Pending");
        producer.sendAdDetails(adDetails);
        // Verify that basicPublish is called with the correct parameters
        verify(channel, times(1)).basicPublish("", MessageProducer.QUEUE_NAME, 
        null, any(byte[].class));
    }
}
