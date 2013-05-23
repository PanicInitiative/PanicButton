package com.amnesty.panicbutton.trigger;

import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.amnesty.panicbutton.alert.PanicAlert;

import static android.content.Intent.ACTION_SCREEN_OFF;
import static android.content.Intent.ACTION_SCREEN_ON;

public class HardwareTriggerReceiver extends BroadcastReceiver {
    private static final String TAG = HardwareTriggerReceiver.class.getName();
    private MultiClickEvent multiClickEvent;

    public HardwareTriggerReceiver() {
        resetEvent();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if(isScreenLocked(context) && (action.equals(ACTION_SCREEN_OFF) || action.equals(ACTION_SCREEN_ON))) {
            process(context);
        }
    }

    private boolean isScreenLocked(Context context) {
        KeyguardManager keyguardManager = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
        return keyguardManager.inKeyguardRestrictedInputMode();
    }

    private void process(Context context) {
        multiClickEvent.registerClick(System.currentTimeMillis());
        if (multiClickEvent.isActivated()) {
            getPanicAlert(context).activate();
            resetEvent();
        }
    }

    PanicAlert getPanicAlert(Context context) {
        return new PanicAlert(context);
    }

    private void resetEvent() {
        multiClickEvent = new MultiClickEvent();
    }
}
