package com.apb.beacon.alert;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.widget.Toast;

import com.apb.beacon.ApplicationSettings;
import com.apb.beacon.HomeActivity;
import com.apb.beacon.SettingsActivity;
import com.apb.beacon.common.Intents;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(Intents.SEND_ALERT_ACTION)) {
            Location currentBestLocation = ApplicationSettings.getCurrentBestLocation(context);
            getPanicMessage(context).send(currentBestLocation);


        } else if (intent.getAction().equals(Intents.ACTIVATE_ALERT_ACTION)){

            ApplicationSettings.setIsGuardianActive(context, false);
//            Intent home = new Intent(context, SettingsActivity.class);
//            home.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            home.putExtra("ACTIVATE_ONLY", true);
//            context.startActivity(home);

            new PanicAlert(context).activate();
        }
    }

    PanicMessage getPanicMessage(Context context) {
        return new PanicMessage(context);
    }
}
