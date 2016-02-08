package org.iilab.pb.receiver;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import org.iilab.pb.CalculatorActivity;
import org.iilab.pb.R;

import static org.iilab.pb.common.AppConstants.APP_RELEASE_VERSION_1_5;
import static org.iilab.pb.common.ApplicationSettings.getLastUpdatedVersion;

public class UpdateReceiver extends BroadcastReceiver
{
    private static final String TAG = UpdateReceiver.class.getName();
    public static class LegacyUpdateReceiver extends BroadcastReceiver
    {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null && intent.getData() != null && context.getPackageName().equals(intent.getData().getSchemeSpecificPart())) {
                onUpdate(context);
            }
        }
    }

    @Override
    public void onReceive(Context context, Intent intent)
    {
        onUpdate(context);
    }

    public static void onUpdate(Context context)
    {
        Log.d(TAG, "PB app updated");
        if (getLastUpdatedVersion(context) == APP_RELEASE_VERSION_1_5)
            displayNotification(context);
    }

    public static void displayNotification(Context context) {
        // Invoking the default notification service
        Log.d(TAG, "inside displayNotification for update of PB triggerred for 1.5 release");
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
        int notifyID = 1;
        mBuilder.setContentTitle("Calculate!");
        mBuilder.setContentText("Calculate! has been updated!");
        mBuilder.setSmallIcon(R.drawable.warning);
        mBuilder.setAutoCancel(true);
        Intent calculatorActivityIntent = new Intent(context.getApplicationContext(), CalculatorActivity.class);
        calculatorActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(context, 0,
                calculatorActivityIntent, 0);
        // start the activity when the user clicks the notification text
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManagerCompat myNotificationManager = NotificationManagerCompat.from(context);
        // pass the Notification object to the system
        myNotificationManager.notify(notifyID, mBuilder.build());
    }

}
