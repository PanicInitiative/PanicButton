package org.iilab.pb.location;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

public class LocationListenerAdapter extends Thread implements LocationListener {
    @Override
    public void onLocationChanged(Location location) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }
}
