package com.amnesty.panicbutton.alert;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import com.amnesty.panicbutton.ApplicationSettings;
import com.amnesty.panicbutton.common.Intents;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(Intents.SEND_ALERT_ACTION)) {
            Location currentBestLocation = ApplicationSettings.getCurrentBestLocation(context);
            getPanicMessage(context).send(currentBestLocation);
        }
    }

    PanicMessage getPanicMessage(Context context) {
        return new PanicMessage(context);
    }
}
