package org.iilab.pb.trigger;

import android.util.EventLog;
import android.util.Log;
import com.google.gson.Gson;
import org.iilab.pb.common.AppConstants;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MultiClickEvent {
    private static final int TIME_INTERVAL = 8000;
    private static final int TIME_INTERVAL_FOR_CONFIRMATION = 3000;
    private static final int TOTAL_CLICKS = 4;
    private final int IS_STATE_CHANGE_USER_TRIGERRED = 2;
    private final String EVENT_LOG_TAG_POWER_SCREEN_STATE = "power_screen_state";

    private Long firstEventTime;
    private int clickCount = 0;
    private int IS_POWER_STATE_ASLEEP = 0;
    private boolean waitForConfirmation = false;
    private Long vibrationStartTime;
    private boolean isActivated;
    private Map<String, String> eventLog = new HashMap<String, String>();

    public void reset() {
    	firstEventTime = null;
    	clickCount = 0;
        eventLog = new HashMap<String, String>();
    }

    public void registerClick(Long eventTime) {
        if(waitForConfirmation){
            long confirmationDuration = eventTime - vibrationStartTime;
            int vibrationDuration = AppConstants.HAPTIC_FEEDBACK_DURATION;

            boolean isVibrationEnded = confirmationDuration >= vibrationDuration;
            boolean isConfirmationClickWithinTimeLimit = (vibrationDuration + TIME_INTERVAL_FOR_CONFIRMATION) >= confirmationDuration;

            if(isVibrationEnded && isConfirmationClickWithinTimeLimit){
                isActivated = true;
                waitForConfirmation = false;
                return;
            }
            if(isVibrationEnded) {
                resetClickCount(eventTime);
            }
            return;
        }

        if (isFirstClick() || notWithinLimit(eventTime) || !isPowerClickBecauseOfUser()) {
            resetClickCount(eventTime);
            return;
        }
        else {
            clickCount++;
            eventLog.put(Integer.toString(clickCount) + " click", new Date(eventTime).toString());
            if (clickCount >= TOTAL_CLICKS) {
                waitForConfirmation = true;
                eventLog.put("Waiting for confirmation", new Date(eventTime).toString());
                vibrationStartTime = eventTime;
                return;
            }
        }
    }

    private void resetClickCount(Long eventTime) {
        firstEventTime = eventTime;
        clickCount = 1;
        waitForConfirmation=false;
        Log.e(">>>>>>", "MultiClickEvent clickCount = " + clickCount);
        eventLog = new HashMap<String, String>();
        eventLog.put(Integer.toString(clickCount) + " click", new Date(eventTime).toString());
    }

    //TODO: move this to a class like PowerStateEventLogReader
    private boolean isPowerClickBecauseOfUser() {
        ArrayList<EventLog.Event> events = new ArrayList<EventLog.Event>();
        try {
            int powerScreenStateTagCode = EventLog.getTagCode(EVENT_LOG_TAG_POWER_SCREEN_STATE);
            EventLog.readEvents(new int[]{powerScreenStateTagCode}, events);
            if(!events.isEmpty()){
                EventLog.Event event = events.get(events.size() - 1);
                try {
                    Object[] powerEventLogData = (Object[]) event.getData();
                    if(powerEventLogData.length > 2 && (Integer)powerEventLogData[0] == IS_POWER_STATE_ASLEEP){
                        boolean isPowerChangeUserTriggered = (Integer) powerEventLogData[1] == IS_STATE_CHANGE_USER_TRIGERRED;
                        Log.e(">>>>>> is Power change user triggered? ", "" + isPowerChangeUserTriggered);
                        return isPowerChangeUserTriggered;
                    }
                }catch (ClassCastException ce){
                    Object data = event.getData();
                    Log.e(">>>>>>", "could not read event data" + new Gson().toJson(data));
                }
            }
        } catch (IOException e) {
            Log.e(">>>>>>", "could not read event logs");
        }
        return true;
    }

    private boolean notWithinLimit(long current) {
        return (current - firstEventTime) > TIME_INTERVAL;
    }

    private boolean isFirstClick() {
        return firstEventTime == null;
    }

    public boolean isActivated() {
        return isActivated;
    }

    public boolean canStartVibration() {
        return waitForConfirmation;
    }

    public Map<String, String> getEventLog(){
        return eventLog;
    }

}
