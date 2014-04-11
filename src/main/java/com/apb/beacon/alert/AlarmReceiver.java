package com.apb.beacon.alert;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;

import com.apb.beacon.common.ApplicationSettings;
import com.apb.beacon.common.Intents;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
    	Context _context = context.getApplicationContext();
        if(intent.getAction().equals(Intents.SEND_ALERT_ACTION)) {
            Location currentBestLocation = ApplicationSettings.getCurrentBestLocation(_context);
            getPanicMessage(_context).send(currentBestLocation);
        }
    }

    PanicMessage getPanicMessage(Context context) {
    	Context _context = context.getApplicationContext();
        return new PanicMessage(_context);
    }
}
