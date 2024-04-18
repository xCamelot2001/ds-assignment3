package com.mydomain.adprocess.messaging;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Date;

public class AdDetailsTest {
    private AdDetails adDetails;

    @Before
    public void setUp() {
        adDetails = new AdDetails("ad123", "Acme Corp", new Date(), "Buy Acme products!",
                                  "Page 3", false, "http://example.com/image.jpg", "Pending");
    }

    @Test
    public void testGetters() {
        assertEquals("ad123", adDetails.getAdId());
        assertEquals("Acme Corp", adDetails.getAdvertiserName());
        assertEquals("Buy Acme products!", adDetails.getAdContent());
        assertEquals("Page 3", adDetails.getPlacementPreference());
        assertFalse(adDetails.isPaymentComplete());
        assertEquals("http://example.com/image.jpg", adDetails.getGraphicsUrl());
        assertEquals("Pending", adDetails.getStatus());
    }

    @Test
    public void testSetters() {
        adDetails.setAdId("ad124");
        adDetails.setAdvertiserName("Acme Inc");
        adDetails.setAdContent("New content");
        adDetails.setPlacementPreference("Page 4");
        adDetails.setPaymentComplete(true);
        adDetails.setGraphicsUrl("http://example.com/newimage.jpg");
        adDetails.setStatus("Completed");

        assertEquals("ad124", adDetails.getAdId());
        assertEquals("Acme Inc", adDetails.getAdvertiserName());
        assertEquals("New content", adDetails.getAdContent());
        assertEquals("Page 4", adDetails.getPlacementPreference());
        assertTrue(adDetails.isPaymentComplete());
        assertEquals("http://example.com/newimage.jpg", adDetails.getGraphicsUrl());
        assertEquals("Completed", adDetails.getStatus());
    }
}
