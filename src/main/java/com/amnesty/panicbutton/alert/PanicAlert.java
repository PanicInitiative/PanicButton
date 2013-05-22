package com.amnesty.panicbutton.alert;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.SystemClock;
import android.os.Vibrator;
import android.util.Log;
import com.amnesty.panicbutton.ApplicationSettings;
import com.amnesty.panicbutton.location.CurrentLocationProvider;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static android.location.LocationManager.GPS_PROVIDER;
import static android.location.LocationManager.NETWORK_PROVIDER;
import static com.amnesty.panicbutton.common.Intents.alarmPendingIntent;
import static com.amnesty.panicbutton.common.Intents.locationPendingIntent;

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
        vibrate();
        if(isActive()) {
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
        createPanicMessage().send(getLocation(currentLocationProvider));
    }

    PanicMessage createPanicMessage() {
        return new PanicMessage(context);
    }

    CurrentLocationProvider getCurrentLocationProvider() {
        return new CurrentLocationProvider(context);
    }

    private void scheduleFutureAlert() {
        PendingIntent alarmPendingIntent = alarmPendingIntent(context);
        long timeFromNow = SystemClock.elapsedRealtime() + ALERT_FREQUENCY_IN_SECS;
        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, timeFromNow, ALERT_FREQUENCY_IN_SECS, alarmPendingIntent);
    }

    private void registerLocationUpdate() {
        locationManager.requestLocationUpdates(NETWORK_PROVIDER, NETWORK_MIN_TIME, NETWORK_MIN_DISTANCE, locationPendingIntent(context));
        locationManager.requestLocationUpdates(GPS_PROVIDER, GPS_MIN_TIME, GPS_MIN_DISTANCE, locationPendingIntent(context));
    }

    public boolean isActive() {
        return ApplicationSettings.isAlertActive(context);
    }

    private void vibrate() {
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(HAPTIC_FEEDBACK_DURATION);
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

    public AlertStatus getAlertStatus() {
        if(isActive()) {
            return AlertStatus.ACTIVE;
        }
        return AlertStatus.STANDBY;
    }

    ExecutorService getExecutorService() {
        return Executors.newSingleThreadExecutor();
    }

    public static final int MAX_RETRIES = 10;
    public static final int LOCATION_WAIT_TIME = 1000;
    public static final int HAPTIC_FEEDBACK_DURATION = 3000;

    private static final float GPS_MIN_DISTANCE = 0;
    private static final long GPS_MIN_TIME = 1000 * 60 * 2;

    private static final float NETWORK_MIN_DISTANCE = 0;
    private static final long NETWORK_MIN_TIME = 1000 * 60 * 2;

    private static final long ALERT_FREQUENCY_IN_SECS = 1000 * 60 * 5;
}