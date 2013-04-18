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

import static org.junit.Assert.assertEquals;
import static org.robolectric.Robolectric.shadowOf;

@RunWith(RobolectricTestRunner.class)
public class LocationProviderTest {
    private ShadowLocationManager shadowLocationManager;
    private Context context = Robolectric.application;
    private LocationProvider locationProvider;

    @Before
    public void setUp() {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        shadowLocationManager = shadowOf(locationManager);
        locationProvider = new LocationProvider(context);
    }

    @Test
    public void shouldReturnTheCurrentLocation() throws InterruptedException {
        Location expectedLocation = new Location(LocationManager.GPS_PROVIDER);
        expectedLocation.setLatitude(10.0);
        expectedLocation.setLongitude(20.0);
        shadowLocationManager.simulateLocation(expectedLocation);

        Location actualLocation = locationProvider.getLastKnowLocation();

        assertEquals(expectedLocation, actualLocation);
    }
}
