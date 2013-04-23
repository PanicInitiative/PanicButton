package com.amnesty.panicbutton.trigger;

public class MultiClickEvent {
    public static final int TIME_INTERVAL = 5000;
    private static final int TOTAL_CLICKS = 5;

    private Long startTimestamp;
    private int clickCount=0;

    public void registerClick() {
        long currentTime = currentTime();
        if (isFirstClick() || notWithinLimit(currentTime)) {
            startTimestamp = currentTime;
            clickCount=1;
            return;
        }
        clickCount++;
    }

    long currentTime() {
        return System.currentTimeMillis();
    }

    private boolean notWithinLimit(long current) {
        return (current - startTimestamp) > TIME_INTERVAL;
    }

    private boolean isFirstClick() {
        return startTimestamp == null;
    }

    public boolean isActivated() {
        return clickCount >= TOTAL_CLICKS;
    }
}
