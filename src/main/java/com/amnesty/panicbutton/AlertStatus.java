package com.amnesty.panicbutton;

public enum AlertStatus {
    STANDBY(R.string.alert_status_standby, true),
    NOT_CONFIGURED(R.string.alert_status_not_configured, false);

    private int description;
    private boolean status;

    private AlertStatus(int description, boolean status) {
        this.description = description;
        this.status = status;
    }

    public int getDescription() {
        return description;
    }

    public boolean canAlert() {
        return this.status;
    }
}