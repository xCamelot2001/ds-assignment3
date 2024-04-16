package com.mydomain.adprocess.editing;

import com.mydomain.adprocess.marketing.Advertisement;

import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable {
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
            Editor editor = new Editor();
            Advertisement processedAd = editor.processAdvertisement(advertisement);
            
            output.writeObject(processedAd);
            System.out.println("Processed advertisement for: " + processedAd.getAdvertiserName());

            // Send processed advertisement to AccountingServer
            sendToAccountingServer(processedAd);

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
        try (Socket accountingSocket = new Socket("localhost", EditingDepartmentServer.ACCOUNTING_PORT);
             ObjectOutputStream outToAccounting = new ObjectOutputStream(accountingSocket.getOutputStream());
             ObjectInputStream inFromAccounting = new ObjectInputStream(accountingSocket.getInputStream())) {

            outToAccounting.writeObject(advertisement);
            System.out.println("Advertisement sent to Accounting Server for: " + advertisement.getAdvertiserName());

            // Get acknowledgment from the accounting server
            String response = (String) inFromAccounting.readObject();
            System.out.println("Acknowledgment from Accounting Server: " + response);

        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error communicating with Accounting Server: " + e.getMessage());
            // Here you could implement a retry mechanism or queue the message for later processing.
        }
    }
}
