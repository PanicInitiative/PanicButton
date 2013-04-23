package com.amnesty.panicbutton.trigger;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.amnesty.panicbutton.alert.PanicAlert;

import static android.content.Intent.ACTION_SCREEN_OFF;
import static android.content.Intent.ACTION_SCREEN_ON;

public class HardwareTriggerReceiver extends BroadcastReceiver {
    private MultiClickEvent multiClickEvent;

    public HardwareTriggerReceiver() {
        resetEvent();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if(action.equals(ACTION_SCREEN_OFF) || action.equals(ACTION_SCREEN_ON)) {
            process(context);
        }
    }

    private void process(Context context) {
        multiClickEvent.registerClick();
        if (multiClickEvent.isActivated()) {
            activateAlert(new PanicAlert(context));
            resetEvent();
        }
    }

    void activateAlert(PanicAlert panicAlert) {
        panicAlert.start();
    }

    private void resetEvent() {
        multiClickEvent = new MultiClickEvent();
    }
}
