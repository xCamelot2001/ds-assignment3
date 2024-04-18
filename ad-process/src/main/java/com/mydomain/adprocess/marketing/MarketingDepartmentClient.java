package com.mydomain.adprocess.marketing;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

public class MarketingDepartmentClient {

    public static void main(String[] args) {
        String editingDepartmentHost = "localhost"; // Editing Department server host
        int editingDepartmentPort = 12345; // Editing Department server port
        // Connect to the Editing Department server
        try (Socket socket = new Socket(editingDepartmentHost, editingDepartmentPort);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             Scanner scanner = new Scanner(System.in)) {
            // Read the advertisements from the user
            List<Advertisement> ads = new ArrayList<>();
            System.out.println("Enter the number of advertisements:");
            int count = scanner.nextInt();
            scanner.nextLine(); // consume newline
            // Read the advertisements in a for loop
            for (int i = 0; i < count; i++) {
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
                scanner.nextLine(); // consume newline
                // Create the advertisement object
                Advertisement ad = new Advertisement(advertiserName, message, position, adId, cost);
                ads.add(ad);
            }
            // Send the advertisements to the Editing Department
            out.writeObject(ads);
            System.out.println("Advertisements batch sent to Editing Department.");
        } catch (IOException e) {
            System.err.println("I/O Error: " + e.getMessage());
        }
    }
}
