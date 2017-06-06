package org.iilab.pb.trigger;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

public class HardwareTriggerService extends Service {
	private HardwareTriggerReceiver hardwareTriggerReceiver;
    private static final String TAG = HardwareTriggerService.class.getName();
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "HardwareTriggerService CREATED.");
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        hardwareTriggerReceiver = new HardwareTriggerReceiver(this);
        registerReceiver(hardwareTriggerReceiver, filter);
    }
    
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "HardwareTriggerService DESTROYED.");
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);

        unregisterReceiver(hardwareTriggerReceiver);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
