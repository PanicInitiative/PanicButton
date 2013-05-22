package com.amnesty.panicbutton.location;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;
import com.amnesty.panicbutton.ApplicationSettings;
import com.amnesty.panicbutton.common.Intents;

public class LocationUpdateReceiver extends BroadcastReceiver {
    private static final String TAG = LocationUpdateReceiver.class.getName();
    private static long MIN_UPDATE_INTERVAL = 1000 * 60 * 1;
    private static final int ACCURACY_THRESHOLD = 200;
    private Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(Intents.LOCATION_UPDATE_ACTION)) {
            this.context = context;
            processNewLocation(intent);
        }
    }

    private void processNewLocation(Intent intent) {
        Location newLocation = (Location)intent.getExtras().get(LocationManager.KEY_LOCATION_CHANGED);
        if(newLocation == null) { return; }
        Log.i(TAG, "Received location : " + newLocation.getProvider() + ", Accuracy : " + newLocation.getAccuracy());
        if(isBetterLocation(newLocation)) {
            setCurrentBestLocation(newLocation);
        }
    }

    private boolean isBetterLocation(Location location) {
        Location currentBestLocation = getCurrentBestLocation();
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

    Location getCurrentBestLocation() {
        return ApplicationSettings.getCurrentBestLocation(context);
    }

    void setCurrentBestLocation(Location location) {
        ApplicationSettings.setCurrentBestLocation(context, location);
    }
}
