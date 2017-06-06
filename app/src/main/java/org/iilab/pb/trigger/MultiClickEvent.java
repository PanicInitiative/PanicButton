package org.iilab.pb.trigger;

import android.content.Context;
import android.os.Vibrator;
import android.util.EventLog;
import android.util.Log;

import com.google.gson.Gson;

import org.iilab.pb.common.AppUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.iilab.pb.common.AppConstants.ALARM_NOT_CONFIRMED_THREE_FAST;
import static org.iilab.pb.common.AppConstants.ONE_SECOND;
import static org.iilab.pb.common.ApplicationSettings.getAlarmNotConfirmedPattern;
import static org.iilab.pb.common.ApplicationSettings.getConfirmationWaitVibrationDuration;
import static org.iilab.pb.common.ApplicationSettings.getInitialClicksForAlertTrigger;
import static org.iilab.pb.common.ApplicationSettings.getInitialClicksMaxTimeLimit;
import static org.iilab.pb.common.ApplicationSettings.isAlarmConfirmationRequired;

public class MultiClickEvent {
    //This is the guard time
    private static final int MAX_TIME_INTERVAL_FOR_CONFIRMATION = 3000;
    //    private static final int INITIAL_CLICKS = 5;
    private final int IS_STATE_CHANGE_USER_TRIGERRED = 2;
    private final String EVENT_LOG_TAG_POWER_SCREEN_STATE = "power_screen_state";

    private Long firstEventTime;
    private int clickCount = 0;
    private int IS_POWER_STATE_ASLEEP = 0;
    private boolean waitForConfirmation = false;
    private Long hapticFeedbackVibrationStartTime;
    private boolean isActivated;
    private Map<String, String> eventLog = new HashMap<String, String>();
    private boolean skipClick = false;
    private static final String TAG = MultiClickEvent.class.getName();
    protected Context mContext;

    public MultiClickEvent(Context context) {
        mContext = context;
    }

    public void reset() {
        firstEventTime = null;
        clickCount = 0;
        eventLog = new HashMap<String, String>();
    }

    public void setContext(Context context) {
        mContext = context;
    }

    public Context getContext() {
        return mContext;
    }

    public void registerClick(Long timeWhenButtonClicked) {
        if (waitForConfirmation) {
            long confirmationDuration = timeWhenButtonClicked - hapticFeedbackVibrationStartTime;
            int hapticFeedbackVibrationDuration = ONE_SECOND * Integer.parseInt(getConfirmationWaitVibrationDuration(mContext));
            boolean isConfirmationClickedBeforeVibrationEnded = confirmationDuration <= hapticFeedbackVibrationDuration;
            boolean isConfirmationClickedWithinTimeLimit = (hapticFeedbackVibrationDuration + MAX_TIME_INTERVAL_FOR_CONFIRMATION) >= confirmationDuration;

            if (isConfirmationClickedBeforeVibrationEnded) {
                Log.d(TAG, "isConfirmationClickedBeforeVibrationEnded");
                skipClick = true;
                return;
            }
            if (!isConfirmationClickedBeforeVibrationEnded && isConfirmationClickedWithinTimeLimit) {
                Log.d(TAG, "!isConfirmationClickedBeforeVibrationEnded && isConfirmationClickedWithinTimeLimit");
                isActivated = true;
                waitForConfirmation = false;
                return;
            }
            if (!isConfirmationClickedWithinTimeLimit) {
                Log.d(TAG, "!isConfirmationClickedWithinTimeLimit");
                resetClickCount(timeWhenButtonClicked);

                alarmNotConfirmedOnTime();
            }
            return;
        }
        if (isFirstClick() || notWithinLimit(timeWhenButtonClicked) || !isPowerClickBecauseOfUser()) {
            Log.d(TAG, "isFirstClick() || notWithinLimit(eventTime) || !isPowerClickBecauseOfUser()");
            resetClickCount(timeWhenButtonClicked);
            return;
        } else {
            clickCount++;
            Log.d(TAG, "MultiClickEvent clickCount = " + clickCount + " " + mContext);
            eventLog.put(Integer.toString(clickCount) + " click", new Date(timeWhenButtonClicked).toString());
            if (clickCount >= Integer.parseInt(getInitialClicksForAlertTrigger(mContext))) {

                //if no confirmation click required, activate the alarm
//                if(ALARM_SENDING_CONFIRMATION_PATTERN_NONE.equals(getConfirmationFeedbackVibrationPattern(mContext))) {
                    if(!isAlarmConfirmationRequired(mContext)){
                    waitForConfirmation=false;
                    isActivated = true;
                    AppUtil.vibrateForHapticFeedback(mContext);
                }else {
                    waitForConfirmation = true;
                }
                eventLog.put("Waiting for confirmation", new Date(timeWhenButtonClicked).toString());
                hapticFeedbackVibrationStartTime = timeWhenButtonClicked;
                return;
            }
        }
    }

    private void alarmNotConfirmedOnTime() {
        String alarmNotConfirmedPattern = getAlarmNotConfirmedPattern(mContext);
        Log.d(TAG, "Alarm not confirmed on time pattern 1-None, 2- three short " + alarmNotConfirmedPattern);
        if (ALARM_NOT_CONFIRMED_THREE_FAST.equals(alarmNotConfirmedPattern)) {
            //vibrate three times fast
            Vibrator vibrator = (Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE);
            long[] pattern = {0, 600, 400, 600, 400, 600, 400};
            vibrator.vibrate(pattern, -1);
        }
    }

    private void resetClickCount(Long eventTime) {
        firstEventTime = eventTime;
        clickCount = 1;
        waitForConfirmation = false;
        Log.d(TAG, " Reset clickCount = " + clickCount);
        eventLog = new HashMap<String, String>();
        eventLog.put(Integer.toString(clickCount) + " click", new Date(eventTime).toString());
    }

    //TODO: move this to a class like PowerStateEventLogReader
    // - event logs can't be read post Android 4.1
    private boolean isPowerClickBecauseOfUser() {
        ArrayList<EventLog.Event> events = new ArrayList<EventLog.Event>();
        try {
            int powerScreenStateTagCode = EventLog.getTagCode(EVENT_LOG_TAG_POWER_SCREEN_STATE);
            EventLog.readEvents(new int[]{powerScreenStateTagCode}, events);
            if (!events.isEmpty()) {
                EventLog.Event event = events.get(events.size() - 1);
                try {
                    Object[] powerEventLogData = (Object[]) event.getData();
                    if (powerEventLogData.length > 2 && (Integer) powerEventLogData[0] == IS_POWER_STATE_ASLEEP) {
                        boolean isPowerChangeUserTriggered = (Integer) powerEventLogData[1] == IS_STATE_CHANGE_USER_TRIGERRED;
                        Log.d(TAG, " is Power change user triggered? " + isPowerChangeUserTriggered);
                        return isPowerChangeUserTriggered;
                    }
                } catch (ClassCastException ce) {
                    Object data = event.getData();
                    Log.d(TAG, "could not read event data" + new Gson().toJson(data));
                }
            }
        } catch (IOException e) {
            Log.e(TAG, "could not read event logs");
        }
        return true;
    }

    private boolean notWithinLimit(long current) {
        int initialClicksMaxTimeLimit = ONE_SECOND * Integer.parseInt(getInitialClicksMaxTimeLimit(mContext));
        boolean isInitialClicksWithInLimit = (current - firstEventTime) > initialClicksMaxTimeLimit;
        Log.d(TAG, "Initial clicks max time limit is " + initialClicksMaxTimeLimit);
        Log.d(TAG, "Initial clicks done with in assigned time limit? " + isInitialClicksWithInLimit);
        return isInitialClicksWithInLimit;
    }

    private boolean isFirstClick() {
        return firstEventTime == null;
    }

    public boolean isActivated() {
        return isActivated;
    }

    /**
     * Since initial click counts are done, we can start the haptic feedback vibration.
     *
     * @return
     */
    public boolean canStartVibration() {
        return waitForConfirmation;
    }

    public Map<String, String> getEventLog() {
        return eventLog;
    }

    public boolean skipCurrentClick() {
        return skipClick;
    }

    public void resetSkipCurrentClickFlag() {
        skipClick = false;
    }
}
