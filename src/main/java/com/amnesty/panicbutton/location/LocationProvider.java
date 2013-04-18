package com.amnesty.panicbutton.location;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import java.util.List;

public class LocationProvider {
    private static final float MIN_DISTANCE = 0;
    private static final long MIN_TIME = 0;
    private static long MIN_UPDATE_INTERVAL = 1000 * 60;

    private Context context;
    private long minUpdateInterval;

    private Location currentBestLocation;
    private LocationListener locationListener = new LocationListenerAdapter() {
        @Override
        public void onLocationChanged(Location location) {
            if (isBetterLocation(location, currentBestLocation)) {
                currentBestLocation = location;
            }
        }
    };

    public LocationProvider(Context context) {
        this(context, MIN_UPDATE_INTERVAL);
    }

    public LocationProvider(Context context, long minUpdateInterval) {
        this.context = context;
        this.minUpdateInterval = minUpdateInterval;
        initLocationListener();
    }


    private void initLocationListener() {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        List<String> allProviders = locationManager.getAllProviders();
        for (String provider : allProviders) {
            locationManager.requestLocationUpdates(provider, MIN_TIME, MIN_DISTANCE, locationListener);
        }
    }

    public Location currentBestLocation() {
        return currentBestLocation;
    }

    protected boolean isBetterLocation(Location location, Location currentBestLocation) {
        if (currentBestLocation == null) {
            return true;
        }
        long timeDelta = location.getTime() - currentBestLocation.getTime();

        if (timeDelta > minUpdateInterval) {
            return true;
        } else if (timeDelta < -minUpdateInterval) {
            return false;
        }

        int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
        boolean isMoreAccurate = accuracyDelta < 0;

        if (isMoreAccurate) {
            return true;
        }

        return false;
    }
}
