// Client side of editing socket communication
package com.mydomain.adprocess.marketing;

import java.net.Socket;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class EditingSocketClient {

    public static void main(String[] args) {
        String serverName = "localhost";
        int port = 12345; // The same port where the server is listening
        try (Socket socket = new Socket(serverName, port);
                ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream input = new ObjectInputStream(socket.getInputStream())) {

            // Create advertisement instances
            Advertisement ad1 = new Advertisement("Advertiser A", "Buy the best product!", "Back cover", 2, 1001,
                    500.00);
            Advertisement ad2 = new Advertisement("Advertiser B", "Sale - Everything must go!", "Page 2", 1, 1002,
                    300.00);

            // Send advertisements
            output.writeObject(ad1);
            // Wait for response and process it
            Advertisement responseAd1 = (Advertisement) input.readObject();
            System.out.println("Response for Ad1: " + responseAd1.getContent());

            output.writeObject(ad2);
            // Wait for response and process it
            Advertisement responseAd2 = (Advertisement) input.readObject();
            System.out.println("Response for Ad2: " + responseAd2.getContent());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
