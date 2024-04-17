//The class that models an advertisement.
package com.mydomain.adprocess.marketing;

import java.io.Serializable;

public class Advertisement implements Serializable {
    private String content;
    private String placement;
    private String advertiserName;
    private int issueNumber;
    private double price;

    // Constructors
    public Advertisement(String advertiserName, String content, String placement, int issueNumber,
            double price) {
        this.advertiserName = advertiserName;
        this.content = content;
        this.placement = placement;
        this.issueNumber = issueNumber;
        this.price = price;
    }

    // Getters
    public String getAdvertiserName() {
        return advertiserName;
    }

    public String getContent() {
        return content;
    }

    public String getPlacement() {
        return placement;
    }

    public int getIssueNumber() {
        return issueNumber;
    }

    public double getPrice() {
        return price;
    }

    // Setters
    public void setAdvertiserName(String advertiserName) {
        this.advertiserName = advertiserName;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setPlacement(String placement) {
        this.placement = placement;
    }

    public void setIssueNumber(int issueNumber) {
        this.issueNumber = issueNumber;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    // toString method for printing advertisement details
    @Override
    public String toString() {
        return "Advertisement{" +
                "advertiserName='" + advertiserName + '\'' +
                ", content='" + content + '\'' +
                ", placement='" + placement + '\'' +
                ", issueNumber=" + issueNumber +
                ", price=" + price +
                '}';
    }

    // Additional methods can be added here as needed.
}
