package com.amnesty.panicbutton.alert;

import com.amnesty.panicbutton.R;

public enum AlertStatus {
    STANDBY(R.string.send_emergency_alert, R.string.alert_status_standby, R.drawable.send_alert_button),
    ACTIVE(R.string.stop_alert, R.string.alert_status_active, R.drawable.stop_alert_button);

    private int action;
    private int description;
    private int backgroundStyle;

    private AlertStatus(int action, int description, int backgroundStyle) {
        this.action = action;
        this.description = description;
        this.backgroundStyle = backgroundStyle;
    }

    public int getDescription() {
        return description;
    }

    public int getAction() {
        return action;
    }

    public int getStyle() {
        return backgroundStyle;
    }
}