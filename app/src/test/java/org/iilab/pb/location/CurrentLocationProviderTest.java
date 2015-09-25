package org.iilab.pb.location;

import android.location.Location;

import org.iilab.pb.BuildConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class CurrentLocationProviderTest {
    @Mock
    private Location mockLocation;

    @Before
    public void setUp() {
        initMocks(this);
    }

    @Test
    public void shouldReturnTheCurrentLocation() {
        CurrentLocationProvider currentLocationProvider = new CurrentLocationProvider(RuntimeEnvironment.application);
        currentLocationProvider.onLocationChanged(mockLocation);
        assertEquals(mockLocation, currentLocationProvider.getLocation());
    }
}
