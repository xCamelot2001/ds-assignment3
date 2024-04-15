// Socket server for the editing department
package com.mydomain.adprocess.editing;

import com.mydomain.adprocess.marketing.Advertisement;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EditingSocketServer {

    private static final int THREAD_POOL_SIZE = 10;
    public static final int ACCOUNTING_PORT = 12346; // The port for the accounting server

    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
        int port = 12345; // The port for the editing server

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Editing Socket Server is running on port " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                executor.execute(new ClientHandler(clientSocket));
            }
        } catch (IOException e) {
            System.err.println("Could not listen on port " + port);
            e.printStackTrace();
        } finally {
            executor.shutdown();
        }
    }
}

class ClientHandler implements Runnable {
    private final Socket clientSocket;

    public ClientHandler(Socket socket) {
        this.clientSocket = socket;
    }

    @Override
    public void run() {
        try (ObjectInputStream input = new ObjectInputStream(clientSocket.getInputStream());
                ObjectOutputStream output = new ObjectOutputStream(clientSocket.getOutputStream())) {

            Advertisement advertisement = (Advertisement) input.readObject();

            // Process the advertisement
            String processedContent = "Processed: " + advertisement.getContent();
            advertisement.setContent(processedContent);

            output.writeObject(advertisement);
            System.out.println("Processed advertisement for: " + advertisement.getAdvertiserName());

            // Send processed advertisement to AccountingSocketServer
            sendToAccountingServer(advertisement);

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

    private void sendToAccountingServer(Advertisement advertisement) {
        try (Socket accountingSocket = new Socket("localhost", EditingSocketServer.ACCOUNTING_PORT);
                ObjectOutputStream outToAccounting = new ObjectOutputStream(accountingSocket.getOutputStream());
                ObjectInputStream inFromAccounting = new ObjectInputStream(accountingSocket.getInputStream())) {

            outToAccounting.writeObject(advertisement);
            System.out.println("Advertisement sent to Accounting Server for: " + advertisement.getAdvertiserName());

            // Get acknowledgment from the accounting server
            String response = (String) inFromAccounting.readObject();
            System.out.println("Acknowledgment from Accounting Server: " + response);

        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error communicating with Accounting Server: " + e.getMessage());
        }
    }
}
