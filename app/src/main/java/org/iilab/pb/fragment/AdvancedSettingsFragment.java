package org.iilab.pb.fragment;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceCategory;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.util.Log;

import org.iilab.pb.MainActivity;
import org.iilab.pb.R;
import org.iilab.pb.WizardActivity;
import org.iilab.pb.trigger.HardwareTriggerService;

import static org.iilab.pb.common.AppConstants.ALARM_SENDING_CONFIRMATION_PATTERN_LONG;
import static org.iilab.pb.common.AppConstants.ALARM_SENDING_CONFIRMATION_PATTERN_NONE;
import static org.iilab.pb.common.AppConstants.PAGE_ADVANCED_SETTINGS;
import static org.iilab.pb.common.AppConstants.PAGE_HOME_READY;
import static org.iilab.pb.common.AppConstants.PAGE_ID;
import static org.iilab.pb.common.AppConstants.PAGE_SETTINGS;
import static org.iilab.pb.common.AppConstants.PAGE_SETUP_ALARM_RETRAINING;
import static org.iilab.pb.common.AppConstants.PARENT_ACTIVITY;
import static org.iilab.pb.common.ApplicationSettings.getInitialClicksForAlertTrigger;
import static org.iilab.pb.common.ApplicationSettings.getTriggerSettings;
import static org.iilab.pb.common.ApplicationSettings.isConfirmationFeedback;
import static org.iilab.pb.common.ApplicationSettings.setConfirmationFeedback;
import static org.iilab.pb.common.ApplicationSettings.setConfirmationFeedbackVibrationPattern;
import static org.iilab.pb.common.ApplicationSettings.setInitialClicksForAlertTrigger;

public class AdvancedSettingsFragment extends PreferenceFragmentCompat {


    private static final String TAG = AdvancedSettingsFragment.class.getName();

    public static AdvancedSettingsFragment newInstance(String pageId, int parentActivity) {
        AdvancedSettingsFragment f = new AdvancedSettingsFragment();
        Bundle args = new Bundle();
        args.putString(PAGE_ID, pageId);
        args.putInt(PARENT_ACTIVITY, parentActivity);
        f.setArguments(args);
        return (f);
    }


    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);
        if(getString(R.string.custom_modeValue).equals(getTriggerSettings(getActivity()))){
            enableAdvancedSettings(true);
        }else{
            enableAdvancedSettings(false);
        }
        if(isConfirmationFeedback(getActivity())){
            enableConfirmationFeedback(true);
        }else{
            enableConfirmationFeedback(false);
        }

        Preference redoTrainingButton = (Preference) findPreference(getString(R.string.redoTrainingKey));
        redoTrainingButton.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Log.d(TAG, "Testing redo training redoTrainingBtton");
                // During redo exercise of alarm trigger, stop the send alert hardware service.
                getActivity().stopService(new Intent(getActivity(), HardwareTriggerService.class));
                Intent i = new Intent(getActivity(), WizardActivity.class);
                i.putExtra(PAGE_ID, PAGE_SETUP_ALARM_RETRAINING);
                startActivity(i);
                return true;
            }
        });

        Preference powerButtonAlarmTrigger = (Preference) findPreference(getString(R.string.configurePowerButtonTriggerKey));

        powerButtonAlarmTrigger.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object selectedValue) {
                Log.d(TAG, "Inside on preference change of power redoTrainingBtton trigger setting");

                if (selectedValue.equals(getString(R.string.activate_power_button_trigger))) {
                    getActivity().startService(new Intent(getActivity(), HardwareTriggerService.class));
                    Log.d(TAG, "Power redoTrainingBtton alarm trigger is enabled");
                } else {
                    getActivity().stopService(new Intent(getActivity(), HardwareTriggerService.class));
                    displayNotification();
                    Log.d(TAG, "Power redoTrainingBtton alarm trigger is disabled");
                }
                return true;
            }
        });

        Preference alertConfirmationSettings = (Preference) findPreference(getString(R.string.confirmationSettingsKey));

        alertConfirmationSettings.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object selectedValue) {
                Log.d(TAG, "Inside of feedback for alarm activation settings");

                if (selectedValue.equals(getString(R.string.confirmationSettingsDefault))) {
                    // disable Confirmation Wait Time/ Confirmation Wait Vibration
                    enableConfirmationFeedback(false);
                    setConfirmationFeedback(getActivity(),false);
                    Log.d(TAG, "default confirmation press deactivated");
                } else {
                    // enable Confirmation Wait Time/ Confirmation Wait Vibration
                    enableConfirmationFeedback(true);
                    setConfirmationFeedback(getActivity(), true);
                    Log.d(TAG, "Confirmation press enabled");
                }
                return true;
            }
        });
        Preference triggerSettingsPef = (Preference) findPreference(getString(R.string.triggerSettingsKey));

        triggerSettingsPef.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object selectedValue) {
                Log.d(TAG, "Inside on preference change of main trigger setting");

                if (selectedValue.equals(getString(R.string.default7RepeatedPressValue))) {
                    setConfirmationFeedbackVibrationPattern(getActivity(), ALARM_SENDING_CONFIRMATION_PATTERN_NONE);
                    enableAdvancedSettings(false);
                    enableRedoTraining(true);
                    Log.d(TAG, "Extra confirmation click required to trigger alarm");
                } else if (selectedValue.equals(getString(R.string.extraConfirmationPressValue))) {
                    //make the press 5
                    setInitialClicksForAlertTrigger(getActivity(),"5");
                    setConfirmationFeedbackVibrationPattern(getActivity(), ALARM_SENDING_CONFIRMATION_PATTERN_LONG);
                    enableAdvancedSettings(false);
                    enableRedoTraining(true);
                    Log.d(TAG, "Extra confirmation click required to trigger alarm "+getInitialClicksForAlertTrigger(getActivity()));
                } else if (selectedValue.equals(getString(R.string.custom_modeValue))) {
                    enableAdvancedSettings(true);
                    // enable all the advanced settings
                    Log.d(TAG, "Power redoTrainingButton alarm trigger is disabled");
                }
                return true;
            }
        });
    }
    private void enableAdvancedSettings(boolean flag){
        PreferenceCategory prefCatTriggerPatternSettings = (PreferenceCategory) findPreference(getString(R.string.triggerPatternSettingsKey));
        PreferenceCategory prefCatTriggerVibrationSettings = (PreferenceCategory) findPreference(getString(R.string.feedbackAlarmActivationKey));
        PreferenceCategory prefCatRedoTraining = (PreferenceCategory) findPreference(getString(R.string.redoTrainingPrefCatKey));
        PreferenceCategory prefCatPowerButtonTriggerSettings = (PreferenceCategory) findPreference(getString(R.string.configurePowerButtonPrefCatKey));
        prefCatTriggerPatternSettings.setEnabled(flag);
        prefCatTriggerVibrationSettings.setEnabled(flag);
        prefCatRedoTraining.setEnabled(flag);
        prefCatPowerButtonTriggerSettings.setEnabled(flag);
    }
    private void enableRedoTraining(boolean flag){
        PreferenceCategory prefCatRedoTraining = (PreferenceCategory) findPreference(getString(R.string.redoTrainingPrefCatKey));
        prefCatRedoTraining.setEnabled(flag);
    }
    private void enableConfirmationFeedback(boolean flag){
        Preference confirmationWaitTime = (Preference) findPreference(getString(R.string.confirmationWaitTimeKey));
        confirmationWaitTime.setEnabled(flag);
        Preference confirmationWaitVibration = (Preference) findPreference(getString(R.string.hapticFeedbackVibrationPatternKey));
        confirmationWaitVibration.setEnabled(flag);
        Preference alertSendingConfirmationPattern = (Preference) findPreference(getString(R.string.alertSendingConfirmationVibrationKey));
        alertSendingConfirmationPattern.setEnabled(flag);

    }

    private void displayNotification() {
        // Invoking the default notification service
        Log.d(TAG, "inside displayNotification for power button alarm trigger");
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getActivity());
        int notifyID = 1;
        mBuilder.setContentTitle("Power Button Trigger");
        mBuilder.setContentText("The power button trigger is deactivated");
        mBuilder.setSmallIcon(R.drawable.warning);
        mBuilder.setAutoCancel(true);
        // Creates an explicit intent for an Activity in your app
        Context mContext = getActivity();
        Intent advancedSettingsIntent = new Intent(mContext, MainActivity.class);
        advancedSettingsIntent.putExtra(PAGE_ID, PAGE_ADVANCED_SETTINGS);
        mContext.startActivity(advancedSettingsIntent);
        ((MainActivity) mContext).callFinishActivityReceiver();

        Intent settingsIntent = new Intent(mContext, MainActivity.class);
        settingsIntent.putExtra(PAGE_ID, PAGE_SETTINGS);
        Intent homePageIntent = new Intent(mContext, MainActivity.class);
        homePageIntent.putExtra(PAGE_ID, PAGE_HOME_READY);
        //This ensures that navigating backward from the Activity leads out of the app to Home page
        TaskStackBuilder stackBuilder = TaskStackBuilder.create((MainActivity) mContext);
       // Adds the back stack for the Intent
        stackBuilder.addNextIntent(homePageIntent).addNextIntent(settingsIntent).addNextIntent(advancedSettingsIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(0,
                        PendingIntent.FLAG_UPDATE_CURRENT //can only be used once
                );
        // start the activity when the user clicks the notification text
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManagerCompat myNotificationManager = NotificationManagerCompat.from(mContext);
        // pass the Notification object to the system
        myNotificationManager.notify(notifyID, mBuilder.build());
    }
}

