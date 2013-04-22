package com.amnesty.panicbutton.trigger;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.amnesty.panicbutton.MessageAlerter;

import static android.content.Intent.ACTION_SCREEN_OFF;
import static android.content.Intent.ACTION_SCREEN_ON;

public class HardwareTriggerReceiver extends BroadcastReceiver {
    private Triggers triggers;
    private MessageAlerter messageAlerter;

    public HardwareTriggerReceiver(MessageAlerter messageAlerter, Triggers triggers) {
        this.messageAlerter = messageAlerter;
        this.triggers = triggers;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if(action.equals(ACTION_SCREEN_OFF) || action.equals(ACTION_SCREEN_ON)) {
            triggers.add(System.currentTimeMillis());
            if (triggers.isActive()) {
                messageAlerter.start();
            }
        }
    }
}
