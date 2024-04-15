//Represents an entity that creates advertisements.
package com.mydomain.adprocess.marketing;

public class Advertiser {
    private String name;
    private String contactDetails; // Added for more details about the advertiser

    public Advertiser(String name, String contactDetails) {
        this.name = name;
        this.contactDetails = contactDetails;
    }

    public Advertisement createAdvertisement(String content, String placement, int size, int issueNumber,
            double price) {
        // The advertisement is created with all necessary details, including those
        // specific to the issue of the magazine and pricing.
        return new Advertisement(this.name, content, placement, size, issueNumber, price);
    }

    // Getters and setters
    public String getName() {
        return name;
    }

    public String getContactDetails() {
        return contactDetails;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setContactDetails(String contactDetails) {
        this.contactDetails = contactDetails;
    }

    // You may add additional behaviors and properties that are relevant to an
    // advertiser here.
    // For instance, you could add methods for managing advertisements, tracking
    // payments, etc.
}
