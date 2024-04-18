package com.mydomain.adprocess.payment;

import com.mydomain.adprocess.marketing.Advertisement;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

public class AccountingDepartment {

    private static final int THREAD_POOL_SIZE = 10; // Number of threads in the pool
    private static ExecutorService executor;
    private static AtomicBoolean running = new AtomicBoolean(true);
    private static ServerSocket serverSocket;

    public static void main(String[] args) throws IOException {
        executor = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
        int port = 12346; // The port for the accounting server‘s socket

        Runtime.getRuntime().addShutdownHook(new Thread(AccountingDepartment::shutdown));

        // Create a server socket to listen for incoming connections
        serverSocket = new ServerSocket(port);
        System.out.println("Accounting Socket Server is running on port " + port);

        while (running.get()) {
            try {
                Socket clientSocket = serverSocket.accept();
                executor.execute(new AccountingClientHandler(clientSocket));
            } catch (IOException e) {
                if (!running.get()) {
                    System.out.println("Server is stopped.");
                } else {
                    System.err.println("Error accepting client connection: " + e.getMessage());
                }
            }
        }
    }

    public static void shutdown() {
        running.set(false);
        if (executor != null) {
            executor.shutdown(); // Disable new tasks from being submitted
        }
        try {
            if (serverSocket != null) {
                serverSocket.close(); // This will cause serverSocket.accept() to throw a SocketException
            }
        } catch (IOException e) {
            System.err.println("Error while closing server socket: " + e.getMessage());
        }
    }
}

class AccountingClientHandler implements Runnable {
    private final Socket clientSocket;

    public AccountingClientHandler(Socket socket) {
        this.clientSocket = socket;
    }

    @Override
    public void run() {
        try (ObjectInputStream input = new ObjectInputStream(clientSocket.getInputStream());
             ObjectOutputStream output = new ObjectOutputStream(clientSocket.getOutputStream())) {
            // Read the advertisement object from the client
            Advertisement advertisement = (Advertisement) input.readObject();

            // Process payment for the advertisement
            processPayment(advertisement);

            // Send acknowledgment back to EditingServer
            output.writeObject("Payment processed for " + advertisement.getAdvertiserName());
            System.out.println("Payment processed for: " + advertisement.getAdvertiserName());

        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error handling client #" + this.hashCode() + ": " + e.getMessage());
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                System.err.println("Error closing connection for client #" + this.hashCode() + ": " + e.getMessage());
            }
        }
    }

    private void processPayment(Advertisement advertisement) {
        // Processing payment logic...
        System.out.println("Processing payment for advertisement: " + advertisement.getContent());
    }
}
