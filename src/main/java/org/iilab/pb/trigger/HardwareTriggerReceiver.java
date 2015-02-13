package org.iilab.pb.trigger;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Debug;
import android.util.Log;
import org.iilab.pb.alert.PanicAlert;

import static android.content.Intent.ACTION_SCREEN_OFF;
import static android.content.Intent.ACTION_SCREEN_ON;

public class HardwareTriggerReceiver extends BroadcastReceiver {
    private static final String TAG = HardwareTriggerReceiver.class.getName();
    //    private MultiClickEvent multiClickEvent;
    protected MultiClickEvent multiClickEvent;

    public HardwareTriggerReceiver() {
        resetEvent();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e(">>>>>>>", "in onReceive of HWReceiver");
        String action = intent.getAction();
        if (!isCallActive(context) && (action.equals(ACTION_SCREEN_OFF) || action.equals(ACTION_SCREEN_ON))) {
            multiClickEvent.registerClick(System.currentTimeMillis());

            if(multiClickEvent.canStartVibration()){
                PanicAlert panicAlert = getPanicAlert(context);
                panicAlert.vibrate();
            }

            else if (multiClickEvent.isActivated()) {
                onActivation(context);
                resetEvent();
            }
        }
    }

    protected void onActivation(Context context) {
        Log.e(">>>>>>>", "in onActivation of HWReceiver");
        activateAlert(getPanicAlert(context));
    }

    void activateAlert(PanicAlert panicAlert) {
//        panicAlert.start();
        panicAlert.activate();
    }

    protected void resetEvent() {
        multiClickEvent = new MultiClickEvent();
    }

    protected PanicAlert getPanicAlert(Context context) {
        return new PanicAlert(context);
    }

    private boolean isCallActive(Context context) {
        AudioManager manager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        return manager.getMode() == AudioManager.MODE_IN_CALL;
    }
}
