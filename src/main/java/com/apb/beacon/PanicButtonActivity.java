package com.apb.beacon;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.apb.beacon.alert.AlertStatus;
import com.apb.beacon.alert.PanicAlert;
import roboguice.activity.RoboActivity;

public abstract class PanicButtonActivity extends RoboActivity {
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        View alertStatusStrip = getLayoutInflater().inflate(R.layout.alert_status_strip, null);
        ViewGroup rootLayout = (ViewGroup) getRootLayout();
        rootLayout.addView(alertStatusStrip, 0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, 5));
    }

    private int alertStatusStripColor() {
        return getPanicAlert().getAlertStatus().equals(AlertStatus.ACTIVE) ?
                getResources().getColor(R.color.active_color) : getResources().getColor(R.color.standby_color);
    }


    protected View getRootLayout() {
        return ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        findViewById(R.id.alert_status_strip).setBackgroundColor(alertStatusStripColor());
    }

    PanicAlert getPanicAlert() {
        return new PanicAlert(this);
    }
}