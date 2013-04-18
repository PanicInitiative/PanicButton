package com.amnesty.panicbutton.location;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

public class LocationProvider {
    private Context context;
    private Location lastKnowLocation;

    public LocationProvider(Context context) {
        this.context = context;
        initLocationListener();
    }

    private void initLocationListener() {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        LocationListener locationListener = new SimpleLocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                updateLocation(location);
            }
        };

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
    }

    private void updateLocation(Location location) {
        lastKnowLocation = location;
    }

    public Location getLastKnowLocation() throws InterruptedException {
        return lastKnowLocation;
    }

}
