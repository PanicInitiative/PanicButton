package com.apb.beacon.trigger;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.apb.beacon.CalculatorActivity;
import com.apb.beacon.HomeActivity;

import org.apache.commons.logging.impl.LogKitLogger;

/**
 * Created by sohjsolwin on 8/10/13.
 */
public class DialBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)) {
            String number = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);

            if (number.equals("1234") || number == "1234") {

                setResultData(null);

                Intent home = new Intent(context, HomeActivity.class);
                home.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(home);

            }
        }
    }
}
