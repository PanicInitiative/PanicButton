package org.iilab.pb.location;

import android.location.Location;

import org.iilab.pb.location.CurrentLocationProvider;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(RobolectricTestRunner.class)
public class CurrentLocationProviderTest {
    @Mock
    private Location mockLocation;

    @Before
    public void setUp() {
        initMocks(this);
    }

    @Test
    public void shouldReturnTheCurrentLocation() {
        CurrentLocationProvider currentLocationProvider = new CurrentLocationProvider(Robolectric.application);
        currentLocationProvider.onLocationChanged(mockLocation);
        assertEquals(mockLocation, currentLocationProvider.getLocation());
    }
}
