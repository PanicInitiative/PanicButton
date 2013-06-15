package com.apb.beacon;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.apb.beacon.alert.AlertStatus;
import com.apb.beacon.alert.PanicAlert;
import roboguice.activity.RoboActivity;

public abstract class PanicButtonActivity extends RoboActivity {
    public static final int ADD_TO_TOP = 0;

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        View alertStatusStrip = LayoutInflater.from(this).inflate(R.layout.alert_status_strip, getRootLayout(), false);
        getRootLayout().addView(alertStatusStrip, ADD_TO_TOP);
    }

    private int alertStatusStripColor() {
        return AlertStatus.ACTIVE.equals(getPanicAlert().getAlertStatus()) ?
                getResources().getColor(R.color.active_color) : getResources().getColor(R.color.standby_color);
    }

    private ViewGroup getRootLayout() {
        return (ViewGroup) ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateAlertStatusStrip();
    }

    protected void updateAlertStatusStrip() {
        findViewById(R.id.alert_status_strip).setBackgroundColor(alertStatusStripColor());
    }

    PanicAlert getPanicAlert() {
        return new PanicAlert(this);
    }
}