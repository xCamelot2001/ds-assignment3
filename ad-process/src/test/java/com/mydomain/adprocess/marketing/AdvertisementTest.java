package com.mydomain.adprocess.marketing;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class AdvertisementTest {
    private Advertisement advertisement;

    @Before
    public void setUp() {
        advertisement = new Advertisement("Acme Corp", "Buy Acme products!", "Page 3", 1, 100.0);
    }

    @Test
    public void testGetters() {
        assertEquals("Acme Corp", advertisement.getAdvertiserName());
        assertEquals("Buy Acme products!", advertisement.getContent());
        assertEquals("Page 3", advertisement.getPlacement());
        assertEquals(1, advertisement.getIssueNumber());
        assertEquals(100.0, advertisement.getPrice(), 0.001);
    }

    @Test
    public void testSetters() {
        advertisement.setAdvertiserName("Acme Inc");
        advertisement.setContent("New product available!");
        advertisement.setPlacement("Back cover");
        advertisement.setIssueNumber(2);
        advertisement.setPrice(150.0);

        assertEquals("Acme Inc", advertisement.getAdvertiserName());
        assertEquals("New product available!", advertisement.getContent());
        assertEquals("Back cover", advertisement.getPlacement());
        assertEquals(2, advertisement.getIssueNumber());
        assertEquals(150.0, advertisement.getPrice(), 0.001);
    }
}
