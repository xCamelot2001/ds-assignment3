// Server that processes payment info
package com.mydomain.adprocess.payment;

import com.mydomain.adprocess.marketing.Advertisement;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AccountingSocketServer {

    private static final int THREAD_POOL_SIZE = 10;

    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
        int port = 12346; // The port for the accounting server

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Accounting Socket Server is running on port " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                executor.execute(new AccountingClientHandler(clientSocket));
            }
        } catch (IOException e) {
            System.err.println("Could not listen on port " + port);
            e.printStackTrace();
        } finally {
            executor.shutdown();
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
        // Here you would implement the logic to process the payment
        // This is just a placeholder to simulate payment processing
        System.out.println("Processing payment for advertisement: " + advertisement.getContent());
    }
}
