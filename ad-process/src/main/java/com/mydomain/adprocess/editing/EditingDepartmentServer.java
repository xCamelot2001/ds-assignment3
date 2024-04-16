package com.mydomain.adprocess.editing;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import com.mydomain.adprocess.messaging.MessageConsumer;

public class EditingDepartmentServer {
    private static final int THREAD_POOL_SIZE = 10; // Number of threads in the pool
    public static final int EDITING_PORT = 12345; // The port for the editing department server
    public static final int ACCOUNTING_PORT = 12346; // The port for the accounting server

    // Reconnection delay constants
    private static final int INITIAL_RECONNECT_DELAY = 1000; // 1 second
    private static final int MAX_RECONNECT_ATTEMPTS = 5; // Maximum number of reconnection attempts

    private static MessageConsumer consumer;
    private static Thread messageConsumerThread;
    
    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
        
        // Start the message consumer in its own thread
        consumer = new MessageConsumer();
        messageConsumerThread = new Thread(() -> {
            try {
                consumer.startConsuming();
            } catch (Exception e) {
                System.err.println("Failed to start message consumer: " + e.getMessage());
                e.printStackTrace();
            }
        });

        messageConsumerThread.start(); // Start the consumer thread
        
        boolean running = true;
        int port = EDITING_PORT;

        while (running) {
            try (ServerSocket serverSocket = new ServerSocket(port)) {
                System.out.println("Editing Department Server is running on port " + port);

                while (true) {
                    Socket clientSocket = serverSocket.accept();
                    executor.execute(new ClientHandler(clientSocket));
                }
            } catch (IOException e) {
                running = handleServerSocketFailure(e, port);
            }
        }

        // Shutdown sequence
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Shutting down...");
            try {
                consumer.stopConsuming(); // Stop the message consumer
                messageConsumerThread.join(); // Ensure the consumer thread stops before proceeding
            } catch (Exception e) {
                System.err.println("Error during shutdown: " + e.getMessage());
            }
            executor.shutdownNow(); // Stop all currently executing tasks
        }));

        executor.shutdown(); // If while loop is exited, shutdown executor
    }

    private static boolean handleServerSocketFailure(IOException exception, int port) {
        System.err.println("Error: " + exception.getMessage());
        System.err.println("Attempting to reconnect...");
        int attempt = 0;
        while (attempt < MAX_RECONNECT_ATTEMPTS) {
            try {
                Thread.sleep(INITIAL_RECONNECT_DELAY * (int) Math.pow(2, attempt));
                new ServerSocket(port).close(); // Check if we can open the port again
                System.out.println("Reconnection successful.");
                return true; // Return true to continue running the server
            } catch (InterruptedException | IOException e) {
                attempt++;
                System.err.println("Reconnection attempt " + attempt + " failed: " + e.getMessage());
            }
        }
        System.err.println("Could not reconnect after " + MAX_RECONNECT_ATTEMPTS + " attempts. Shutting down.");
        return false; // Return false to stop the server
    }
    
    // You may need to add a method to stop consuming messages in your MessageConsumer class
}
