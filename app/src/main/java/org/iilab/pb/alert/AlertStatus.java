package org.iilab.pb.alert;

import org.iilab.pb.R;

public enum AlertStatus {
    STANDBY(R.string.send_emergency_alert, R.string.alert_status_standby, R.drawable.send_alert_button, true),
    ACTIVE(R.string.stop_alert, R.string.alert_status_active, R.drawable.stop_alert_button, false);

    private int action;
    private int description;
    private int backgroundStyle;
    private boolean settingsEnabled;

    private AlertStatus(int action, int description, int backgroundStyle, boolean settingsEnabled) {
        this.action = action;
        this.description = description;
        this.backgroundStyle = backgroundStyle;
        this.settingsEnabled = settingsEnabled;
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

    public boolean isSettingsEnabled() {
        return settingsEnabled;
    }
}