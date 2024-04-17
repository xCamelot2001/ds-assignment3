package com.mydomain.adprocess.marketing;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class MarketingDepartmentClient {

    public static void main(String[] args) {
        // Assuming you have the host and port of the EditingDepartmentServer
        String editingDepartmentHost = "localhost";
        int editingDepartmentPort = 12345;

        try (Socket socket = new Socket(editingDepartmentHost, editingDepartmentPort);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream())) {
        
            // Prompt the advertiser to create an advertisement
            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter advertiser name:");
            String advertiserName = scanner.nextLine();
            System.out.println("Enter advertisement message:");
            String message = scanner.nextLine();
            System.out.println("Enter advertisement position:");
            String position = scanner.nextLine();
            System.out.println("Enter advertisement ID:");
            int adId = scanner.nextInt();
            System.out.println("Enter advertisement cost:");
            double cost = scanner.nextDouble();
            
            // Create the advertisement instance
            Advertisement ad = new Advertisement(advertiserName, message, position, adId, cost);
            
            // Send the advertisement to Editing Department Server
            out.writeObject(ad);
            System.out.println("Advertisement sent to Editing Department: " + ad);
            
            // close the scanner
            scanner.close();

        } catch (UnknownHostException e) {
            System.err.println("Host unknown: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("I/O Error: " + e.getMessage());
        }
    }

    // prompt the advertiser to create an advertisement
    
}
