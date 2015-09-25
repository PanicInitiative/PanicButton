package org.iilab.pb.location;

import android.content.Context;
import android.location.Location;

import org.iilab.pb.BuildConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import static android.location.LocationManager.NETWORK_PROVIDER;
import static java.lang.System.currentTimeMillis;
import static org.junit.Assert.assertEquals;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk=21)
public class LocationFormatterTest {
    Context context = RuntimeEnvironment.application;

    @Test
    public void shouldReturnGoogleMapUrlForTheGivenLocation() {
        double latitude = -183.123456;
        double longitude = 78.654321;
        Location location = LocationTestUtil.location(NETWORK_PROVIDER, latitude, longitude, currentTimeMillis(), 10.0f);
        String expectedMessage = "I\'m at https://maps.google.com/maps?q=" + latitude + "," + longitude +" via network";
        LocationFormatter locationFormatter = new LocationFormatter(location);

        assertEquals(expectedMessage, locationFormatter.format(context));
    }

    @Test
    public void shouldReturnEmptyStringIfTheGivenLocationIsNull() {
        LocationFormatter locationFormatter = new LocationFormatter(null);
        assertEquals("", locationFormatter.format(context));
    }
}
