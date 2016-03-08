package org.iilab.pb.alert;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.SystemClock;
import android.util.Log;

import org.iilab.pb.common.AppConstants;
import org.iilab.pb.common.AppUtil;
import org.iilab.pb.common.ApplicationSettings;
import org.iilab.pb.common.Intents;
import org.iilab.pb.location.CurrentLocationProvider;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static android.location.LocationManager.GPS_PROVIDER;
import static android.location.LocationManager.NETWORK_PROVIDER;
import static org.iilab.pb.common.AppConstants.ONE_MINUTE;
import static org.iilab.pb.common.Intents.locationPendingIntent;

public class PanicAlert {

    private static final String TAG = PanicAlert.class.getName();
    private LocationManager locationManager;
    private Context context;
    private AlarmManager alarmManager1, alarmManager2;


    public PanicAlert(Context context) {
        this.context = context;
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        alarmManager1 = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager2 = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

    }

    public void activate() {
        AppUtil.close(context);

        AppUtil.vibrateForConfirmationOfAlertTriggered(context);

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
        Log.d(TAG, "Deactivating the triggered alert");
        ApplicationSettings.setAlertActive(context, false);
        locationManager.removeUpdates(locationPendingIntent(context));
        alarmManager2.cancel(Intents.alarmPendingIntent(context));
        ApplicationSettings.setFirstMsgWithLocationTriggered(context, false);
        ApplicationSettings.setFirstMsgSent(context, false);
    }

    private void sendFirstAlert() {
        Log.d(TAG, "inside of sendFirstAlert");
        CurrentLocationProvider currentLocationProvider = getCurrentLocationProvider();
        Location loc = getLocation(currentLocationProvider);
        Log.d(TAG, "Identified location is "+loc);
        if (loc != null) {
            ApplicationSettings.setFirstMsgWithLocationTriggered(context, true);
        } else {
//            ApplicationSettings.setFirstMsgWithLocationTriggered(context, false);
            scheduleFirstLocationAlert();
        }
        createPanicMessage().sendAlertMessage(loc);
    }

    PanicMessage createPanicMessage() {
        return new PanicMessage(context);
    }

    CurrentLocationProvider getCurrentLocationProvider() {
        return new CurrentLocationProvider(context);
    }

    private void scheduleFirstLocationAlert() {
        PendingIntent alarmPendingIntent = Intents.singleAlarmPendingIntent(context);
        long firstTimeTriggerAt = SystemClock.elapsedRealtime() + ONE_MINUTE * 1;             // we schedule this alarm after 1 minute
        alarmManager1.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, firstTimeTriggerAt, alarmPendingIntent);
//        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, firstTimeTriggerAt, interval, alarmPendingIntent);
    }

    private void scheduleFutureAlert() {
        PendingIntent alarmPendingIntent = Intents.alarmPendingIntent(context);
        long firstTimeTriggerAt = SystemClock.elapsedRealtime() + ONE_MINUTE * ApplicationSettings.getAlertDelay(context);
        long interval = ONE_MINUTE * ApplicationSettings.getAlertDelay(context);
        alarmManager2.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, firstTimeTriggerAt, interval, alarmPendingIntent);
    }

    private void registerLocationUpdate() {

//        if (locationManager.getAllProviders().contains(LocationManager.GPS_PROVIDER))
//            locationManager.requestLocationUpdates(GPS_PROVIDER, AppConstants.GPS_MIN_TIME_IN_FIRST_ONE_MINUTE, AppConstants.GPS_MIN_DISTANCE, locationPendingIntent(context));
//        if (locationManager.getAllProviders().contains(LocationManager.NETWORK_PROVIDER))
//            locationManager.requestLocationUpdates(NETWORK_PROVIDER, AppConstants.NETWORK_MIN_TIME_IN_FIRST_ONE_MINUTE, AppConstants.NETWORK_MIN_DISTANCE, locationPendingIntent(context));
//
        int threadRunCount = 0;
        while (!ApplicationSettings.isFirstMsgWithLocationTriggered(context) && threadRunCount < 4) {
            try {
                Thread.sleep(20000);
                threadRunCount++;

                if (locationManager != null && locationPendingIntent(context) != null) {
                    locationManager.removeUpdates(locationPendingIntent(context));
                }
                if (locationManager.getAllProviders().contains(LocationManager.GPS_PROVIDER))
                    locationManager.requestLocationUpdates(GPS_PROVIDER, AppConstants.GPS_MIN_TIME_IN_FIRST_ONE_MINUTE, AppConstants.GPS_MIN_DISTANCE, locationPendingIntent(context));
                if (locationManager.getAllProviders().contains(LocationManager.NETWORK_PROVIDER))
                    locationManager.requestLocationUpdates(NETWORK_PROVIDER, AppConstants.NETWORK_MIN_TIME_IN_FIRST_ONE_MINUTE, AppConstants.NETWORK_MIN_DISTANCE, locationPendingIntent(context));
                Log.d(TAG, "threadRunCount = " + threadRunCount);
            } catch (SecurityException e) {
                Log.e(TAG, "SecurityException exception " + e.getMessage());
                e.printStackTrace();
            } catch (InterruptedException e) {
                Log.e(TAG, "SecurityException exception " + e.getMessage());
                e.printStackTrace();
            }
        }

        if (locationManager != null && locationPendingIntent(context) != null) {
            locationManager.removeUpdates(locationPendingIntent(context));
        }
        try {
            if (locationManager.getAllProviders().contains(LocationManager.GPS_PROVIDER))
                locationManager.requestLocationUpdates(GPS_PROVIDER, AppConstants.GPS_MIN_TIME, AppConstants.GPS_MIN_DISTANCE, locationPendingIntent(context));
            if (locationManager.getAllProviders().contains(LocationManager.NETWORK_PROVIDER))
                locationManager.requestLocationUpdates(NETWORK_PROVIDER, AppConstants.NETWORK_MIN_TIME, AppConstants.NETWORK_MIN_DISTANCE, locationPendingIntent(context));
        } catch (SecurityException e) {
            Log.e(TAG, "SecurityException exception " + e.getMessage());
            e.printStackTrace();
        }
//        HomeActivity.runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                Handler locationRefreshIntervalHandler = new Handler();
//                locationRefreshIntervalHandler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        if (locationManager != null && locationPendingIntent(context) != null) {
//                            locationManager.removeUpdates(locationPendingIntent(context));
//                        }
//                        if (locationManager.getAllProviders().contains(LocationManager.GPS_PROVIDER))
//                            locationManager.requestLocationUpdates(GPS_PROVIDER, AppConstants.GPS_MIN_TIME, AppConstants.GPS_MIN_DISTANCE, locationPendingIntent(context));
//                        if (locationManager.getAllProviders().contains(LocationManager.NETWORK_PROVIDER))
//                            locationManager.requestLocationUpdates(NETWORK_PROVIDER, AppConstants.NETWORK_MIN_TIME, AppConstants.NETWORK_MIN_DISTANCE, locationPendingIntent(context));
//                    }
//                }, 60 * 1000);          // after 1 minute
//            }
//        });

    }

    public boolean isActive() {
        return ApplicationSettings.isAlertActive(context);
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