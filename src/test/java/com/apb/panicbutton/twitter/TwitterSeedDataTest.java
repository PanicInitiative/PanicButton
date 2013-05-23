package com.apb.panicbutton.twitter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
public class TwitterSeedDataTest {
    private TwitterSeedData twitterSeedData;

    @Before
    public void setUp() {
        twitterSeedData = new TwitterSeedData(Robolectric.application);
    }

    @Test
    public void shouldReturnAllTheCountries() {
        assertEquals(99, twitterSeedData.getCountries().size());
    }

    @Test
    public void shouldReturnServiceProvidersForAGivenCountry() {
        List<String> serviceProviders = twitterSeedData.getServiceProviders("India");
        assertEquals(3, serviceProviders.size());
        assertTrue(serviceProviders.contains("Bharti Airtel"));
        assertTrue(serviceProviders.contains("Reliance"));
        assertTrue(serviceProviders.contains("TATA DOCOMO"));
    }

    @Test
    public void shouldReturnShortCodeForAGivenCountryAndServiceProvider() {
        assertEquals("89887", twitterSeedData.getShortCode("Indonesia", "AXIS"));
    }

    @Test
    public void shouldReturnEmptyListOfCountriesOnException(){
        assertEquals(0,new TwitterSeedData(null).getCountries().size());
    }
}
