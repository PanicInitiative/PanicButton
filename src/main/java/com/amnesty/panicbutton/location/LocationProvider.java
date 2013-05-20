package com.amnesty.panicbutton.location;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import static android.location.LocationManager.GPS_PROVIDER;
import static android.location.LocationManager.NETWORK_PROVIDER;

public class LocationProvider extends LocationListenerAdapter {
    private static final String TAG = LocationProvider.class.getSimpleName();

    private static final float GPS_MIN_DISTANCE = 0;
    private static final long GPS_MIN_TIME = 1000 * 30;

    private static final float NETWORK_MIN_DISTANCE = 0;
    private static final long NETWORK_MIN_TIME = 1000 * 1;

    private static long MIN_UPDATE_INTERVAL = 1000 * 60;
    public static final int ACCURACY_THRESHOLD = 200;

    private Handler handler;
    private Context context;
    private Location currentBestLocation;

    public LocationProvider(Context context) {
        this.context = context;
    }

    @Override
    public void run() {
        Looper.prepare();
        handler = new Handler();
        initLocationListener();
        Looper.loop();
    }

    private void initLocationListener() {
        LocationManager locationManager = locationManager();
        locationManager.requestLocationUpdates(NETWORK_PROVIDER, NETWORK_MIN_TIME, NETWORK_MIN_DISTANCE, this);
        locationManager.requestLocationUpdates(GPS_PROVIDER, GPS_MIN_TIME, GPS_MIN_DISTANCE, this);
        Log.i(TAG, "registered for location updates");
    }

    @Override
    public void onLocationChanged(Location location) {
        final boolean isBetterLocation = isBetterLocation(location, currentBestLocation);
        Log.i(TAG, "Location received : " + location.getProvider() +
                   ", Accuracy : " + location.getAccuracy());
        Log.i(TAG, "isBetter : " + isBetterLocation);

        if (isBetterLocation) {
            currentBestLocation = location;
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
        boolean isNewer = timeDelta > 0;

        if (timeDelta > MIN_UPDATE_INTERVAL) {
            return true;
        } else if (timeDelta < -MIN_UPDATE_INTERVAL) {
            return false;
        }

        int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
        boolean isMoreAccurate = accuracyDelta < 0;
        boolean isAlmostSame = accuracyDelta == 0;
        boolean isSignificantlyLessAccurate = accuracyDelta > ACCURACY_THRESHOLD;
        boolean isFromSameProvider = isSameProvider(location.getProvider(), currentBestLocation.getProvider());

        if (isMoreAccurate) {
            return true;
        } else if (isAlmostSame && isNewer) {
            return true;
        } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
            return true;
        }

        return false;
    }

    private boolean isSameProvider(String provider1, String provider2) {
        return provider1.equals(provider2);
    }

    public void terminate() {
        locationManager().removeUpdates(this);
        handler.getLooper().quit();
    }

    private LocationManager locationManager() {
        return (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    }
}
