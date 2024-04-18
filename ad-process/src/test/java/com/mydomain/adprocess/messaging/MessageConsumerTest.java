package com.mydomain.adprocess.messaging;

import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import com.rabbitmq.client.*;

public class MessageConsumerTest {

    @Mock private ConnectionFactory factory;
    @Mock private Connection connection;
    @Mock private Channel channel;
    private MessageConsumer consumer;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        when(factory.newConnection()).thenReturn(connection);
        when(connection.createChannel()).thenReturn(channel);
        consumer = new MessageConsumer(factory); // pass the factory to the constructor
    }

    @Test
    public void testStartConsuming() throws Exception {
        // Set up the deliver callback to simulate receiving a message
        doAnswer(invocation -> {
            DeliverCallback callback = invocation.getArgument(2);
            String fakeMessage = "{\"adContent\":\"Example Ad Content\"}"; // JSON representation of an AdDetails object
            AMQP.BasicProperties props = new AMQP.BasicProperties();
            Envelope envelope = new Envelope(1L, false, "exchange", "routingKey");
            Delivery delivery = new Delivery(envelope, props, fakeMessage.getBytes());

            callback.handle("consumerTag", delivery);
            return null;
        }).when(channel).basicConsume(eq(MessageConsumer.QUEUE_NAME), eq(true), any(DeliverCallback.class), any(CancelCallback.class));

        consumer.startConsuming();

        // Verify that the channel sets up to consume messages
        verify(channel, times(1)).basicConsume(eq(MessageConsumer.QUEUE_NAME),
        eq(true), any(DeliverCallback.class), any(CancelCallback.class));
    }
}
