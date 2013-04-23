package com.amnesty.panicbutton.trigger;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.amnesty.panicbutton.MessageAlerter;

public class HardwareTriggerReceiver extends BroadcastReceiver {
    private MultiClickEvent multiClickEvent;

    public HardwareTriggerReceiver() {
        resetEvent();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        multiClickEvent.registerClick();
        if (multiClickEvent.isActivated()) {
            activateAlert(new MessageAlerter(context));
            resetEvent();
        }
    }

    void activateAlert(MessageAlerter messageAlerter) {
        messageAlerter.start();
    }

    private void resetEvent() {
        multiClickEvent = new MultiClickEvent();
    }
}
