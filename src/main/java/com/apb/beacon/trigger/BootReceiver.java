package com.apb.beacon.trigger;

import com.apb.beacon.ApplicationSettings;
import com.apb.beacon.alert.PanicAlert;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import static android.content.Intent.ACTION_BOOT_COMPLETED;

public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(ACTION_BOOT_COMPLETED)) {
//        	new PanicAlert(context).deActivate();
        	ApplicationSettings.setAlertActive(context, false);
        	new PanicAlert(context).activate();
            context.startService(new Intent(context, HardwareTriggerService.class));
        }
    }
}
