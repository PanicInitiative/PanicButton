package com.apb.beacon.location;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;

import static android.location.LocationManager.NETWORK_PROVIDER;

public class CurrentLocationProvider extends LocationListenerAdapter {
    private Location currentLocation;

    public CurrentLocationProvider(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestSingleUpdate(NETWORK_PROVIDER, this, context.getMainLooper());
    }

    @Override
    public void onLocationChanged(Location location) {
        currentLocation = location;
    }

    public Location getLocation() {
        return currentLocation;
    }
}
