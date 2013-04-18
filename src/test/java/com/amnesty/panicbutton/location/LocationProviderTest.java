package com.amnesty.panicbutton.location;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowLocationManager;

import static android.location.LocationManager.NETWORK_PROVIDER;
import static java.lang.System.currentTimeMillis;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.robolectric.Robolectric.shadowOf;

@RunWith(RobolectricTestRunner.class)
public class LocationProviderTest {
    public static final float FIFTY_PERCENT_ACCURATE = 0.50f;
    private ShadowLocationManager shadowLocationManager;
    private Context robolectricContext = Robolectric.application;
    private LocationProvider locationProvider;

    @Before
    public void setUp() {
        LocationManager locationManager = (LocationManager) robolectricContext.getSystemService(Context.LOCATION_SERVICE);
        shadowLocationManager = shadowOf(locationManager);
        locationProvider = new LocationProvider(robolectricContext);
    }

    @Test
    public void shouldNotProvideLocationWhenNoUpdatesAreReceived() {
        assertNull(locationProvider.currentBestLocation());
    }

    @Test
    public void shouldProvideTheLatestLocationFromAvailableLocationProviders() {
        Location expectedLocation = location(NETWORK_PROVIDER, 10.0, 20.0, currentTimeMillis(), FIFTY_PERCENT_ACCURATE);
        shadowLocationManager.simulateLocation(expectedLocation);

        Location actualLocation = locationProvider.currentBestLocation();
        assertEquals(expectedLocation, actualLocation);
    }

    @Test
    public void shouldProvideLocationWhenSignificantlyLatest() {
        Location oldLocation1 = location(NETWORK_PROVIDER, 12.0, 20.0, offsetCurrentTimeBy(-90), FIFTY_PERCENT_ACCURATE);
        Location oldLocation2 = location(NETWORK_PROVIDER, 10.0, 20.0, offsetCurrentTimeBy(-80), FIFTY_PERCENT_ACCURATE);
        Location newLocation = location(NETWORK_PROVIDER, 11.0, 20.0, currentTimeMillis(), FIFTY_PERCENT_ACCURATE);
        Location newLocation2 = location(NETWORK_PROVIDER, 13.0, 20.0, offsetCurrentTimeBy(30), FIFTY_PERCENT_ACCURATE);

        shadowLocationManager.simulateLocation(oldLocation1);
        shadowLocationManager.simulateLocation(newLocation);
        shadowLocationManager.simulateLocation(oldLocation1);
        shadowLocationManager.simulateLocation(oldLocation2);
        shadowLocationManager.simulateLocation(newLocation2);

        Location actualLocation = locationProvider.currentBestLocation();

        assertEquals(newLocation, actualLocation);
    }

    @Test
    @Ignore
    public void shouldProvideLocationWhenMoreAccurate() {
    }

    @Test
    @Ignore
    public void shouldProvideLocationWhenLessAccurateAndRelativelyNew() {
    }

    @Test
    @Ignore
    public void shouldProvideLocationWhenFromSameProviderAndRelativelyAccurateAndRelativelyNew() {
    }

    private Location location(String provider, double latitude, double longitude, long time, float accuracy) {
        Location location = new Location(provider);
        location.setLatitude(latitude);
        location.setLongitude(longitude);
        location.setTime(time);
        location.setAccuracy(accuracy);
        return location;
    }

    private long offsetCurrentTimeBy(int seconds) {
        return currentTimeMillis() + (1000 * seconds);
    }
}
