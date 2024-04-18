package com.mydomain.adprocess.editing;

import com.mydomain.adprocess.marketing.Advertisement;
import com.mydomain.adprocess.messaging.MessageProducer;
import com.mydomain.adprocess.messaging.AdDetails;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ClientHandler implements Runnable {
    private final Socket clientSocket;
    private final MessageProducer messageProducer;
    private ExecutorService executorService;

    public ClientHandler(Socket socket, MessageProducer producer) {
        this.clientSocket = socket; // Client socket
        this.messageProducer = producer; // Message queue producer
        this.executorService = Executors.newFixedThreadPool(10); // Process up to 10 advertisements concurrently
    }

    @Override
    public void run() {
        // Process the incoming advertisements
        try (ObjectInputStream input = new ObjectInputStream(clientSocket.getInputStream())) {
            @SuppressWarnings("unchecked")
            // Read the list of advertisements from the client
            List<Advertisement> advertisements = (List<Advertisement>) input.readObject();
            // Process each advertisement in a separate thread
            for (Advertisement advertisement : advertisements) {
                executorService.submit(() -> processAdvertisement(advertisement));
            }
        // Handle exceptions
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error handling client #" + this.hashCode() + ": " + e.getMessage());
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                System.err.println("Error closing connection for client #" + this.hashCode() + ": " + e.getMessage());
            }
            executorService.shutdown();
        }
    }
    // Process the advertisement
    private void processAdvertisement(Advertisement advertisement) {
        Editor editor = new Editor(null);
        Advertisement processedAd = editor.processAdvertisement(advertisement);

        sendToAccountingServer(processedAd);
        sendToMessageQueue(processedAd);
    }
    // Send the processed advertisement to the accounting server
    private void sendToAccountingServer(Advertisement advertisement) {
        try (Socket accountingSocket = new Socket("localhost", EditingDepartmentServer.ACCOUNTING_PORT);
             ObjectOutputStream outToAccounting = new ObjectOutputStream(accountingSocket.getOutputStream());
             ObjectInputStream inFromAccounting = new ObjectInputStream(accountingSocket.getInputStream())) {
                
            outToAccounting.writeObject(advertisement);
            System.out.println("Advertisement sent to Accounting Server for: " + advertisement.getAdvertiserName());

            String response = (String) inFromAccounting.readObject();
            System.out.println("Acknowledgment from Accounting Server: " + response);

        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error communicating with Accounting Server: " + e.getMessage());
            // Implement retry logic or queuing for later processing if necessary.
        }
    }
    // Send the processed advertisement to the message queue
    private void sendToMessageQueue(Advertisement processedAd) {
        AdDetails adDetails = new AdDetails(
            processedAd.getAdvertiserName() + "-" + processedAd.getIssueNumber(),
            processedAd.getAdvertiserName(),
            new Date(),
            processedAd.getContent(),
            processedAd.getPlacement(),
            false,
            "", 
            "Pending"
        );

        try {
            messageProducer.sendAdDetails(adDetails);
            System.out.println("Processed ad details sent to Message Queue.");
        } catch (Exception e) {
            System.err.println("Error sending ad details to message queue: " + e.getMessage());
        }
    }
}
