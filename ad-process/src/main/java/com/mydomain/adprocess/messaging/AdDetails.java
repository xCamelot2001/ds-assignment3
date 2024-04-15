// Details of the ad for messaging
package com.mydomain.adprocess.messaging;

import java.util.Date;

public class AdDetails {
    private String adId; // Unique identifier for the advertisement
    private String advertiserName; // Name of the advertiser
    private Date scheduledDate; // When the ad is scheduled to appear
    private String adContent; // Content of the advertisement
    private int adSize; // Size of the ad, could be an enum or int representing size categories
    private String placementPreference; // Preferred placement in the magazine
    private boolean isPaymentComplete; // Status of payment for the ad
    private String graphicsUrl; // URL or path to the associated graphics for the ad
    private String status; // Current status of the ad in the workflow

    // Constructors
    public AdDetails(String adId, String advertiserName, Date scheduledDate, String adContent,
            int adSize, String placementPreference, boolean isPaymentComplete,
            String graphicsUrl, String status) {
        this.adId = adId;
        this.advertiserName = advertiserName;
        this.scheduledDate = scheduledDate;
        this.adContent = adContent;
        this.adSize = adSize;
        this.placementPreference = placementPreference;
        this.isPaymentComplete = isPaymentComplete;
        this.graphicsUrl = graphicsUrl;
        this.status = status;
    }

    // Getters and Setters
    public String getAdId() {
        return adId;
    }

    public void setAdId(String adId) {
        this.adId = adId;
    }

    public String getAdvertiserName() {
        return advertiserName;
    }

    public void setAdvertiserName(String advertiserName) {
        this.advertiserName = advertiserName;
    }

    public Date getScheduledDate() {
        return scheduledDate;
    }

    public void setScheduledDate(Date scheduledDate) {
        this.scheduledDate = scheduledDate;
    }

    public String getAdContent() {
        return adContent;
    }

    public void setAdContent(String adContent) {
        this.adContent = adContent;
    }

    public int getAdSize() {
        return adSize;
    }

    public void setAdSize(int adSize) {
        this.adSize = adSize;
    }

    public String getPlacementPreference() {
        return placementPreference;
    }

    public void setPlacementPreference(String placementPreference) {
        this.placementPreference = placementPreference;
    }

    public boolean isPaymentComplete() {
        return isPaymentComplete;
    }

    public void setPaymentComplete(boolean paymentComplete) {
        isPaymentComplete = paymentComplete;
    }

    public String getGraphicsUrl() {
        return graphicsUrl;
    }

    public void setGraphicsUrl(String graphicsUrl) {
        this.graphicsUrl = graphicsUrl;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
