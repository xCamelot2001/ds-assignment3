package com.mydomain.adprocess.marketing;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class AdvertiserTest {
    private Advertiser advertiser;

    @Before
    public void setUp() {
        advertiser = new Advertiser("Acme Corp", "contact@acmecorp.com");
    }

    @Test
    public void testCreateAdvertisement() {
        Advertisement ad = advertiser.createAdvertisement("Buy Acme products!", "Page 3", 1, 100.0);

        assertNotNull(ad);
        assertEquals("Acme Corp", ad.getAdvertiserName());
        assertEquals("Buy Acme products!", ad.getContent());
        assertEquals("Page 3", ad.getPlacement());
        assertEquals(1, ad.getIssueNumber());
        assertEquals(100.0, ad.getPrice(), 0.001);
    }

    @Test
    public void testGettersSetters() {
        advertiser.setName("Acme Inc");
        advertiser.setContactDetails("newcontact@acmeinc.com");

        assertEquals("Acme Inc", advertiser.getName());
        assertEquals("newcontact@acmeinc.com", advertiser.getContactDetails());
    }
}
