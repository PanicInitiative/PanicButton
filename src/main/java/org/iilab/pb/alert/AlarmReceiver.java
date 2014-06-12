package org.iilab.pb.alert;

import org.iilab.pb.common.ApplicationSettings;
import org.iilab.pb.common.Intents;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;


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
