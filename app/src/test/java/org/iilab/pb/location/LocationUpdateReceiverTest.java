package org.iilab.pb.location;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import static android.location.LocationManager.GPS_PROVIDER;
import static android.location.LocationManager.NETWORK_PROVIDER;
import static java.lang.System.currentTimeMillis;
import static org.iilab.pb.common.Intents.LOCATION_UPDATE_ACTION;
import static org.iilab.pb.location.LocationTestUtil.location;
import static org.junit.Assert.assertEquals;

@RunWith(RobolectricTestRunner.class)
public class LocationUpdateReceiverTest {
    public static final float LESS_ACCURATE = 20.0f;
    public static final float ALMOST_SAME_AS_LESS_ACCURATE = 20.1f;
    public static final float VERY_LESS_ACCURATE = 50.0f;
    private static final float SIGNIFICANTLY_LESS_ACCURATE = 260f;
    private static final float MORE_ACCURATE = 0.60f;

    private LocationUpdateReceiver locationUpdateReceiver;
    private Context context;

    @Before
    public void setUp() throws InterruptedException {
        context = Robolectric.application;
        locationUpdateReceiver = new LocationUpdateReceiver() {
            public Location location;

            Location getCurrentBestLocation() {
                return location;
            }

            void setCurrentBestLocation(Location location) {
                this.location = location;
            }
        };
    }

    @Test
    public void shouldReturnBestKnowLocation() {
        Location expectedLocation = location(NETWORK_PROVIDER, 10.0, 20.0, currentTimeMillis(), LESS_ACCURATE);
        locationUpdateReceiver.onReceive(context, getIntent(expectedLocation));

        assertLocation(expectedLocation);
    }

    @Test
    public void shouldReturnSignificantlyLatestLocation() {
        Location oldLocation1 = location(NETWORK_PROVIDER, 12.0, 20.0, offsetCurrentTimeBy(-90), LESS_ACCURATE);
        Location oldLocation2 = location(NETWORK_PROVIDER, 10.0, 20.0, offsetCurrentTimeBy(-80), LESS_ACCURATE);
        Location newLocation = location(NETWORK_PROVIDER, 11.0, 20.0, currentTimeMillis(), LESS_ACCURATE);

        locationUpdateReceiver.onReceive(context, getIntent(oldLocation1));
        locationUpdateReceiver.onReceive(context, getIntent(newLocation));
        locationUpdateReceiver.onReceive(context, getIntent(oldLocation1));
        locationUpdateReceiver.onReceive(context, getIntent(oldLocation2));

        assertLocation(newLocation);
    }

    @Test
    public void shouldReturnMoreAccurateLocation() {
        Location oldLocation = location(NETWORK_PROVIDER, 12.0, 20.0, currentTimeMillis(), LESS_ACCURATE);
        Location newLocation = location(NETWORK_PROVIDER, 11.5, 20.0, offsetCurrentTimeBy(40), MORE_ACCURATE);
        Location newLocation1 = location(NETWORK_PROVIDER, 11.0, 20.0, offsetCurrentTimeBy(-20), LESS_ACCURATE);

        locationUpdateReceiver.onReceive(context, getIntent(oldLocation));
        locationUpdateReceiver.onReceive(context, getIntent(newLocation));
        locationUpdateReceiver.onReceive(context, getIntent(newLocation1));

        assertLocation(newLocation);
    }

    @Test
    public void shouldReturnLocationWhichHasAlmostSameAccuracyAndRelativelyNew() {
        Location oldLocation = location(NETWORK_PROVIDER, 12.0, 20.0, currentTimeMillis(), LESS_ACCURATE);
        long time = offsetCurrentTimeBy(10);
        Location newLocation = location(NETWORK_PROVIDER, 12.0, 20.0, time, ALMOST_SAME_AS_LESS_ACCURATE);
        Location newLocationClone = location(NETWORK_PROVIDER, 12.0, 20.0, time, ALMOST_SAME_AS_LESS_ACCURATE);

        locationUpdateReceiver.onReceive(context, getIntent(oldLocation));
        locationUpdateReceiver.onReceive(context, getIntent(newLocation));
        locationUpdateReceiver.onReceive(context, getIntent(newLocationClone));

        assertLocation(newLocation);
    }

    @Test
    public void shouldReturnLocationFromSameProviderAndNotSignificantlyAccurateButRelativelyNew() {
        Location oldLocation = location(NETWORK_PROVIDER, 12.0, 20.0, currentTimeMillis(), LESS_ACCURATE);
        Location lessAccurateButNewLocation = location(NETWORK_PROVIDER, 12.0, 20.0, offsetCurrentTimeBy(10), VERY_LESS_ACCURATE);
        Location diffMoreAccurateProviderLocation = location(GPS_PROVIDER, 12.0, 20.0, offsetCurrentTimeBy(20), VERY_LESS_ACCURATE - 10.0f);
        Location significantlyLessAccurateLocation = location(NETWORK_PROVIDER, 13.0, 20.0, offsetCurrentTimeBy(10), SIGNIFICANTLY_LESS_ACCURATE);

        locationUpdateReceiver.onReceive(context, getIntent(oldLocation));
        locationUpdateReceiver.onReceive(context, getIntent(lessAccurateButNewLocation));
        locationUpdateReceiver.onReceive(context, getIntent(diffMoreAccurateProviderLocation));
        locationUpdateReceiver.onReceive(context, getIntent(significantlyLessAccurateLocation));

        assertLocation(diffMoreAccurateProviderLocation);
    }

    private Intent getIntent(Location location) {
        Intent intent = new Intent(LOCATION_UPDATE_ACTION);
        intent.putExtra(LocationManager.KEY_LOCATION_CHANGED, location);
        return intent;
    }

    private long offsetCurrentTimeBy(int seconds) {
        return currentTimeMillis() + (1000 * seconds);
    }

    private void assertLocation(Location expectedLocation) {
        Location actualLocation = locationUpdateReceiver.getCurrentBestLocation();
        assertEquals(expectedLocation, actualLocation);
    }
}
