package com.amnesty.panicbutton.trigger;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.amnesty.panicbutton.MessageAlerter;

public class HardwareTrigger extends BroadcastReceiver {
    private Triggers triggers;
    private MessageAlerter messageAlerter;

    public HardwareTrigger(MessageAlerter messageAlerter, Triggers triggers) {
        this.messageAlerter = messageAlerter;
        this.triggers = triggers;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        triggers.add(System.currentTimeMillis());
        if (triggers.isActive()) {
            messageAlerter.start();
        }
    }
}
