package com.mydomain.adprocess.marketing;

import com.mydomain.adprocess.messaging.MessageProducer;
import com.mydomain.adprocess.messaging.AdDetails;

import java.util.Date;

public class MarketingDepartmentClient {

    public static void main(String[] args) {
        // Create advertisement instances
        Advertisement ad1 = new Advertisement("Advertiser A", "Buy the best product!", "Back cover", 2, 1001, 500.00);
        Advertisement ad2 = new Advertisement("Advertiser B", "Sale - Everything must go!", "Page 2", 1, 1002, 300.00);

        // Use the MessageProducer to send advertisements asynchronously
        MessageProducer producer = new MessageProducer();
        try {
            // Convert Advertisement to AdDetails
            AdDetails details1 = convertToAdDetails(ad1);
            AdDetails details2 = convertToAdDetails(ad2);

            producer.sendAdDetails(details1);
            producer.sendAdDetails(details2);
            System.out.println("Advertisements sent asynchronously via RabbitMQ");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Failed to send advertisements: " + e.getMessage());
        }
    }

    private static AdDetails convertToAdDetails(Advertisement ad) {
        // Assuming there's an appropriate constructor or method to create AdDetails from Advertisement
        return new AdDetails(
            // You'll need to generate or assign a unique ID for each ad
            // For example purposes, let's just use the advertiser's name and issue number
            ad.getAdvertiserName() + "-" + ad.getIssueNumber(),
            ad.getAdvertiserName(),
            new Date(), // Assume the scheduled date is now for example purposes
            ad.getContent(),
            ad.getSize(),
            ad.getPlacement(),
            false, // Payment not complete by default
            "", // URL can be empty or set as needed
            "Pending" // Initial status
        );
    }
}
