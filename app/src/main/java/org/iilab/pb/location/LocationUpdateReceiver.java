package org.iilab.pb.location;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

import org.iilab.pb.alert.PanicMessage;
import org.iilab.pb.common.ApplicationSettings;
import org.iilab.pb.common.Intents;

public class LocationUpdateReceiver extends BroadcastReceiver {
    private static final String TAG = LocationUpdateReceiver.class.getName();
    private static long MIN_UPDATE_INTERVAL = 1000 * 60 * 1;
    private static final int ACCURACY_THRESHOLD = 200;
    private Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e(">>>>>>>>", "onReceive - LocationUpdateReceiver");
        if(intent.getAction().equals(Intents.LOCATION_UPDATE_ACTION)) {
            Log.e(">>>>>>>>", "location update received in LocationUpdateReceiver at current time in millis = " + System.currentTimeMillis() % 100000);
            this.context = context;
            processNewLocation(intent);

            if (!ApplicationSettings.isFirstMsgWithLocationTriggered(context)) {
                Location location =  getCurrentBestLocation();
                if(location != null) {
                    new PanicMessage(context).sendAlertMessage(getCurrentBestLocation());
                    ApplicationSettings.setFirstMsgWithLocationTriggered(context, true);
                }
            }

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

        // Check whether the new location fix is newer or older
        long timeDelta = location.getTime() - currentBestLocation.getTime();
        boolean isSignificantlyNewer = timeDelta > MIN_UPDATE_INTERVAL;
        boolean isSignificantlyOlder = timeDelta < -MIN_UPDATE_INTERVAL;
        boolean isNewer = timeDelta > 0;

        // If it's been more than two minutes since the current location, use the new location
        // because the user has likely moved
        if (isSignificantlyNewer) {
            return true;
            // If the new location is more than two minutes older, it must be worse
        } else if (isSignificantlyOlder) {
            return false;
        }

        // Check whether the new location fix is more or less accurate
        int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
        boolean isLessAccurate = accuracyDelta > 0;
        boolean isMoreAccurate = accuracyDelta < 0;
        boolean isSignificantlyLessAccurate = accuracyDelta > ACCURACY_THRESHOLD;

        // Check if the old and new location are from the same provider
        boolean isFromSameProvider = isSameProvider(location.getProvider(), currentBestLocation.getProvider());

        // Determine location quality using a combination of timeliness and accuracy
        if (isMoreAccurate) {
            return true;
        } else if (isNewer && !isLessAccurate) {
            return true;
        } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
            return true;
        }
        return false;
    }

    /** Checks whether two providers are the same */
    private boolean isSameProvider(String provider1, String provider2) {
        if (provider1 == null) {
            return provider2 == null;
        }
        return provider1.equals(provider2);
    }


    Location getCurrentBestLocation() {
        return ApplicationSettings.getCurrentBestLocation(context);
    }

    void setCurrentBestLocation(Location location) {
        ApplicationSettings.setCurrentBestLocation(context, location);
    }
}
