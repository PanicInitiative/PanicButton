package com.amnesty.panicbutton.location;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowLocationManager;

import static android.location.LocationManager.GPS_PROVIDER;
import static android.location.LocationManager.NETWORK_PROVIDER;
import static com.amnesty.panicbutton.location.LocationTestUtil.location;
import static java.lang.System.currentTimeMillis;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.robolectric.Robolectric.shadowOf;

@RunWith(RobolectricTestRunner.class)
public class LocationProviderTest {
    public static final float LESS_ACCURATE = 20.0f;
    public static final float ALMOST_SAME_AS_LESS_ACCURATE = 20.1f;

    public static final float VERY_LESS_ACCURATE = 50.0f;
    private static final float SIGNIFICANTLY_LESS_ACCURATE = 260f;

    private static final float MORE_ACCURATE = 0.60f;

    private ShadowLocationManager shadowLocationManager;
    private Context robolectricContext = Robolectric.application;
    private LocationProvider locationProvider;

    @Before
    public void setUp() {
        LocationManager locationManager = (LocationManager) robolectricContext.getSystemService(Context.LOCATION_SERVICE);
        shadowLocationManager = shadowOf(locationManager);
        locationProvider = new LocationProvider(robolectricContext);
        locationProvider.start();
    }

    @Test
    public void shouldNotProvideLocationWhenNoUpdatesAreReceived() {
        assertNull(locationProvider.currentBestLocation());
    }

    @Test
    public void shouldProvideTheLatestLocationFromAvailableLocationProviders() {
        Location expectedLocation = location(NETWORK_PROVIDER, 10.0, 20.0, currentTimeMillis(), LESS_ACCURATE);
        shadowLocationManager.simulateLocation(expectedLocation);

        Location actualLocation = locationProvider.currentBestLocation();
        assertEquals(expectedLocation, actualLocation);
    }

    @Test
    public void shouldProvideSignificantlyLatestLocation() {
        Location oldLocation1 = location(NETWORK_PROVIDER, 12.0, 20.0, offsetCurrentTimeBy(-90), LESS_ACCURATE);
        Location oldLocation2 = location(NETWORK_PROVIDER, 10.0, 20.0, offsetCurrentTimeBy(-80), LESS_ACCURATE);
        Location newLocation = location(NETWORK_PROVIDER, 11.0, 20.0, currentTimeMillis(), LESS_ACCURATE);

        shadowLocationManager.simulateLocation(oldLocation1);
        shadowLocationManager.simulateLocation(newLocation);
        shadowLocationManager.simulateLocation(oldLocation1);
        shadowLocationManager.simulateLocation(oldLocation2);

        Location actualLocation = locationProvider.currentBestLocation();

        assertEquals(newLocation, actualLocation);
    }

    @Test
    public void shouldProvideMoreAccurateLocation() {
        Location oldLocation = location(NETWORK_PROVIDER, 12.0, 20.0, currentTimeMillis(), LESS_ACCURATE);
        Location newLocation = location(NETWORK_PROVIDER, 11.5, 20.0, offsetCurrentTimeBy(40), MORE_ACCURATE);
        Location newLocation1 = location(NETWORK_PROVIDER, 11.0, 20.0, offsetCurrentTimeBy(-20), LESS_ACCURATE);

        shadowLocationManager.simulateLocation(oldLocation);
        shadowLocationManager.simulateLocation(newLocation);
        shadowLocationManager.simulateLocation(newLocation1);

        Location actualLocation = locationProvider.currentBestLocation();
        assertEquals(newLocation, actualLocation);
    }

    @Test
    public void shouldProvideLocationWhenOfAlmostSameAccuracyAndRelativelyNew() {
        Location oldLocation = location(NETWORK_PROVIDER, 12.0, 20.0, currentTimeMillis(), LESS_ACCURATE);
        long time = offsetCurrentTimeBy(10);
        Location newLocation = location(NETWORK_PROVIDER, 12.0, 20.0, time, ALMOST_SAME_AS_LESS_ACCURATE);
        Location newLocationClone = location(NETWORK_PROVIDER, 12.0, 20.0, time, ALMOST_SAME_AS_LESS_ACCURATE);

        shadowLocationManager.simulateLocation(oldLocation);
        shadowLocationManager.simulateLocation(newLocation);
        shadowLocationManager.simulateLocation(newLocationClone);

        Location actualLocation = locationProvider.currentBestLocation();
        assertEquals(newLocation, actualLocation);
    }

    @Test
    public void shouldProvideLocationWhenFromSameProviderAndNotSignificantlyAccurateButRelativelyNew() {
        Location oldLocation = location(NETWORK_PROVIDER, 12.0, 20.0, currentTimeMillis(), LESS_ACCURATE);
        Location lessAccurateButNewLocation = location(NETWORK_PROVIDER, 12.0, 20.0, offsetCurrentTimeBy(10), VERY_LESS_ACCURATE);
        Location diffProviderLocation = location(GPS_PROVIDER, 12.0, 20.0, offsetCurrentTimeBy(20), VERY_LESS_ACCURATE + 10.0f);
        Location significantlyLessAccurateLocation = location(NETWORK_PROVIDER, 13.0, 20.0, offsetCurrentTimeBy(10), SIGNIFICANTLY_LESS_ACCURATE);

        shadowLocationManager.simulateLocation(oldLocation);
        shadowLocationManager.simulateLocation(lessAccurateButNewLocation);
        shadowLocationManager.simulateLocation(diffProviderLocation);
        shadowLocationManager.simulateLocation(significantlyLessAccurateLocation);

        Location actualLocation = locationProvider.currentBestLocation();
        assertEquals(lessAccurateButNewLocation, actualLocation);
    }


    private long offsetCurrentTimeBy(int seconds) {
        return currentTimeMillis() + (1000 * seconds);
    }
}
