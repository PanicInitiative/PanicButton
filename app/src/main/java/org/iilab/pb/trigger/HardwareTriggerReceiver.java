package org.iilab.pb.trigger;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.util.Log;
import org.iilab.pb.alert.PanicAlert;
import org.iilab.pb.common.AppUtil;

import static android.content.Intent.ACTION_SCREEN_OFF;
import static android.content.Intent.ACTION_SCREEN_ON;

public class HardwareTriggerReceiver extends BroadcastReceiver {
    private static final String TAG = HardwareTriggerReceiver.class.getName();
    protected MultiClickEvent multiClickEvent;
    private Context mContext;

    public HardwareTriggerReceiver(Context context) {
        Log.d(TAG, "in constructor of HWReceiver" +context);
        mContext=context;
        resetEvent();
    }


    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "in onReceive of HWReceiver" +multiClickEvent.getContext() +" " +mContext);
        multiClickEvent.setContext(context);
        String action = intent.getAction();
        if (!isCallActive(context) && (action.equals(ACTION_SCREEN_OFF) || action.equals(ACTION_SCREEN_ON))) {
            multiClickEvent.registerClick(System.currentTimeMillis());

            if(multiClickEvent.skipCurrentClick()){
                Log.d(TAG, "skipped click");
                multiClickEvent.resetSkipCurrentClickFlag();
            }

            else if(multiClickEvent.canStartVibration()){
                Log.d(TAG, "vibration started");
//                PanicAlert panicAlert = getPanicAlert(context);
                AppUtil.vibrateForHapticFeedback(context);
            }

            else if (multiClickEvent.isActivated()) {
                Log.d(TAG, "alerts activated");
                onActivation(context);
                resetEvent();
            }
        }
    }

    protected void onActivation(Context context) {
        Log.d(TAG, "in onActivation of HWReceiver");
        activateAlert(getPanicAlert(context));
    }

    void activateAlert(PanicAlert panicAlert) {
//        panicAlert.start();
        panicAlert.activate();
    }

    protected void resetEvent() {
        Log.d(TAG, "in resetevent of HWReceiver" +mContext);
        multiClickEvent = new MultiClickEvent(mContext);
    }

    protected PanicAlert getPanicAlert(Context context) {
        return new PanicAlert(context);
    }

    private boolean isCallActive(Context context) {
        AudioManager manager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        return manager.getMode() == AudioManager.MODE_IN_CALL;
    }
}
