package com.mydomain.adprocess.editing;

import com.mydomain.adprocess.marketing.Advertisement;
import com.mydomain.adprocess.messaging.MessageProducer;
import com.mydomain.adprocess.messaging.AdDetails;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Date;

public class ClientHandler implements Runnable {
    private final Socket clientSocket;
    private final MessageProducer messageProducer;

    public ClientHandler(Socket socket, MessageProducer producer) {
        this.clientSocket = socket;
        this.messageProducer = producer;
    }

    @Override
    public void run() {
        try (ObjectInputStream input = new ObjectInputStream(clientSocket.getInputStream());
             ObjectOutputStream output = new ObjectOutputStream(clientSocket.getOutputStream())) {

            Advertisement advertisement = (Advertisement) input.readObject();

            // Process the advertisement
            Editor editor = new Editor();
            Advertisement processedAd = editor.processAdvertisement(advertisement);

            // Send processed advertisement to AccountingServer
            sendToAccountingServer(processedAd);

            // After sending to the Accounting Server, now we send it to the message queue
            sendToMessageQueue(processedAd);

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

    private void sendToMessageQueue(Advertisement processedAd) {
        // Create AdDetails from the processed advertisement
        AdDetails adDetails = new AdDetails(
            processedAd.getAdvertiserName() + "-" + processedAd.getIssueNumber(),
            processedAd.getAdvertiserName(),
            new Date(), // Use the current date for simplicity
            processedAd.getContent(),
            processedAd.getPlacement(),
            false, // Payment status, false for not completed
            "", // Graphics URL (should be set appropriately)
            "Pending" // Status of the advertisement
        );

        try {
            // Send AdDetails to the message queue
            messageProducer.sendAdDetails(adDetails);
            System.out.println("Processed ad details sent to Message Queue.");
        } catch (Exception e) {
            System.err.println("Error sending ad details to message queue: " + e.getMessage());
        }
    }
}
