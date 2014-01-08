package com.apb.beacon.trigger;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

public class HardwareTriggerService extends Service {
    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(">>>>>>", "HardwareTriggerService CREATED.");
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);

        registerReceiver(new HardwareTriggerReceiver(), filter);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
