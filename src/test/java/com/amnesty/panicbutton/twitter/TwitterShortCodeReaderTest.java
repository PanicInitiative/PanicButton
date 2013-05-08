package com.amnesty.panicbutton.twitter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.*;

@RunWith(RobolectricTestRunner.class)
public class TwitterShortCodeReaderTest {
    @Test
    public void shouldReturnNullInstanceOnError() {
        TwitterShortCodeReader instance = TwitterShortCodeReader.getInstance(null);
        assertNull(instance);
    }

    @Test
    public void shouldReturnTheSingletonInstance() {
        TwitterShortCodeReader instance = TwitterShortCodeReader.getInstance(Robolectric.application);
        assertNotNull(instance);
    }

    @Test
    public void shouldReturnCountries() {
        TwitterShortCodeReader instance = TwitterShortCodeReader.getInstance(Robolectric.application);
        assertEquals(99, instance.countries().size());
    }
}