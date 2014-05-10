package com.apb.beacon.alert;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.SystemClock;
import android.os.Vibrator;
import android.util.Log;

import com.apb.beacon.common.AppConstants;
import com.apb.beacon.common.ApplicationSettings;
import com.apb.beacon.common.AppUtil;
import com.apb.beacon.location.CurrentLocationProvider;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static android.location.LocationManager.GPS_PROVIDER;
import static android.location.LocationManager.NETWORK_PROVIDER;
import static com.apb.beacon.common.Intents.alarmPendingIntent;
import static com.apb.beacon.common.Intents.locationPendingIntent;

public class PanicAlert {
    private static final String TAG = PanicAlert.class.getName();
    private LocationManager locationManager;
    private Context context;
    private AlarmManager alarmManager;

    public PanicAlert(Context context) {
        this.context = context;
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    }

    public void activate() {
        AppUtil.close(context);
        vibrate();
        if (isActive()
//                || ApplicationSettings.isRestartedSetup(context)
                ) {
            return;
        }
        ApplicationSettings.setAlertActive(context, true);
        getExecutorService().execute(
                new Runnable() {
                    @Override
                    public void run() {
                        activateAlert();
                    }
                }
        );
    }

    private void activateAlert() {
        ApplicationSettings.setAlertActive(context, true);
        sendFirstAlert();
        registerLocationUpdate();
        scheduleFutureAlert();
    }

    public void deActivate() {
        ApplicationSettings.setAlertActive(context, false);
        locationManager.removeUpdates(locationPendingIntent(context));
        alarmManager.cancel(alarmPendingIntent(context));
    }

    private void sendFirstAlert() {
        CurrentLocationProvider currentLocationProvider = getCurrentLocationProvider();
        Location loc = getLocation(currentLocationProvider);
        createPanicMessage().send(loc);
    }

    PanicMessage createPanicMessage() {
        return new PanicMessage(context);
    }

    CurrentLocationProvider getCurrentLocationProvider() {
        return new CurrentLocationProvider(context);
    }

    private void scheduleFutureAlert() {
        PendingIntent alarmPendingIntent = alarmPendingIntent(context);
        long timeFromNow = SystemClock.elapsedRealtime() + AppConstants.ONE_MINUTE * ApplicationSettings.getAlertDelay(context);
        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, timeFromNow, AppConstants.ONE_MINUTE * ApplicationSettings.getAlertDelay(context), alarmPendingIntent);
    }

    private void registerLocationUpdate() {
    	if (locationManager.getAllProviders().contains(LocationManager.GPS_PROVIDER)) 
    		locationManager.requestLocationUpdates(GPS_PROVIDER, AppConstants.GPS_MIN_TIME, AppConstants.GPS_MIN_DISTANCE, locationPendingIntent(context));
    	if (locationManager.getAllProviders().contains(LocationManager.NETWORK_PROVIDER))
    		locationManager.requestLocationUpdates(NETWORK_PROVIDER, AppConstants.NETWORK_MIN_TIME, AppConstants.NETWORK_MIN_DISTANCE, locationPendingIntent(context));
    }

    public boolean isActive() {
        return ApplicationSettings.isAlertActive(context);
    }

    private void vibrate() {
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(AppConstants.HAPTIC_FEEDBACK_DURATION);
    }

    private Location getLocation(CurrentLocationProvider currentLocationProvider) {
        Location location = null;
        int retryCount = 0;

        while (retryCount < MAX_RETRIES && location == null) {
            location = currentLocationProvider.getLocation();
            if (location == null) {
                try {
                    retryCount++;
                    Thread.sleep(LOCATION_WAIT_TIME);
                } catch (InterruptedException e) {
                    Log.e(TAG, "Location wait InterruptedException", e);
                }
            }
        }
        return location;
    }

//    public AlertStatus getAlertStatus() {
//        if (isActive()) {
//            return AlertStatus.ACTIVE;
//        }
//        return AlertStatus.STANDBY;
//    }

    ExecutorService getExecutorService() {
        return Executors.newSingleThreadExecutor();
    }

    public static final int MAX_RETRIES = 10;
    public static final int LOCATION_WAIT_TIME = 1000;
}