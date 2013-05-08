package com.amnesty.panicbutton.twitter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(RobolectricTestRunner.class)
public class TwitterShortCodesTest {
    @Test
    public void shouldReturnNullInstanceOnError() {
        TwitterShortCodes instance = TwitterShortCodes.getInstance(null);
        assertNull(instance);
    }

    @Test
    public void shouldReturnTheSingletonInstance() {
        TwitterShortCodes instance = TwitterShortCodes.getInstance(Robolectric.application);
        assertNotNull(instance);
    }

    @Test
    public void shouldReturnCountries() {
        TwitterShortCodes instance = TwitterShortCodes.getInstance(Robolectric.application);
        assertEquals(99, instance.countries().size());
    }

    @Test
    public void shouldReturnThePhoneServicesForTheGivenCountry() {
        TwitterShortCodes twitterShortCodes = TwitterShortCodes.getInstance(Robolectric.application);

        List<String> serviceProviders = twitterShortCodes.serviceProviders("India");
        assertEquals(3, serviceProviders.size());
        assertEquals("Bharti Airtel", serviceProviders.get(0));
        assertEquals("Reliance", serviceProviders.get(1));
        assertEquals("TATA DOCOMO", serviceProviders.get(2));
    }

    @Test
    public void shouldReturnShortCodeForCountryAndServiceProvider(){
        TwitterShortCodes twitterShortCodes = TwitterShortCodes.getInstance(Robolectric.application);

        assertEquals("53000", twitterShortCodes.shortCode("India","Bharti Airtel"));

    }
}