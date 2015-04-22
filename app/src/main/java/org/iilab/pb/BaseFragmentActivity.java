package org.iilab.pb;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

public class BaseFragmentActivity extends FragmentActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(">>>>>", "Registering finish activity in oncreate -> BaseFragmentActivity");
        registerFinishActivityReceiver();

    }

    public void registerFinishActivityReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("org.iilab.pb.ACTION_LOGOUT");
        registerReceiver(activityFinishReceiver, intentFilter);
    }

    public void callFinishActivityReceiver() {
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("org.iilab.pb.ACTION_LOGOUT");
        sendBroadcast(broadcastIntent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            unregisterReceiver(activityFinishReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    BroadcastReceiver activityFinishReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("org.iilab.pb.ACTION_LOGOUT")) {
                finish();
            }
        }
    };

}
