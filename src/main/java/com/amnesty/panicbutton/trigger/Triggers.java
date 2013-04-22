package com.amnesty.panicbutton.trigger;

import java.util.ArrayList;
import java.util.List;

public class Triggers {
    public static final int TIME_UPPER_LIMIT = 5000;
    private static final int TRIGGER_THRESHOLD = 5;
    private List<Long> timestamps = new ArrayList<Long>();

    public void add(Long timestamp) {
        if (!hasTimestamps() || !isWithinLimit(timestamp)) {
            timestamps = new ArrayList<Long>();
            timestamps.add(timestamp);
            return;
        }

        if (!isActive()) {
            timestamps.add(timestamp);
        }
    }

    private boolean isWithinLimit(long current) {
        return (current - timestamps.get(0)) < TIME_UPPER_LIMIT;
    }

    private boolean hasTimestamps() {
        return timestamps.size() > 0;
    }

    public boolean isActive() {
        return timestamps.size() == TRIGGER_THRESHOLD;
    }

    int count() {
        return this.timestamps.size();
    }
}
