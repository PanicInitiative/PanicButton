package org.iilab.pb.fragment;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.preference.CheckBoxPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.util.Log;

import org.iilab.pb.MainActivity;
import org.iilab.pb.R;
import org.iilab.pb.WizardActivity;
import org.iilab.pb.trigger.HardwareTriggerService;

import static org.iilab.pb.common.AppConstants.ALARM_5_REPEATED_CLICKS;
import static org.iilab.pb.common.AppConstants.ALARM_7_REPEATED_CLICKS;
import static org.iilab.pb.common.AppConstants.ALARM_SENDING_CONFIRMATION_PATTERN_LONG;
import static org.iilab.pb.common.AppConstants.ALARM_SENDING_CONFIRMATION_PATTERN_NONE;
import static org.iilab.pb.common.AppConstants.PAGE_ADVANCED_SETTINGS;
import static org.iilab.pb.common.AppConstants.PAGE_HOME_READY;
import static org.iilab.pb.common.AppConstants.PAGE_ID;
import static org.iilab.pb.common.AppConstants.PAGE_SETTINGS;
import static org.iilab.pb.common.AppConstants.PAGE_SETUP_ALARM_RETRAINING;
import static org.iilab.pb.common.AppConstants.PARENT_ACTIVITY;
import static org.iilab.pb.common.ApplicationSettings.getCustomSettings;
import static org.iilab.pb.common.ApplicationSettings.isAlarmConfirmationRequired;
import static org.iilab.pb.common.ApplicationSettings.setAlarmConfirmationRequired;
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
        final CheckBoxPreference default7RepeatedPress = (CheckBoxPreference) findPreference(getString(R.string.default7RepeatedPressKey));
        final CheckBoxPreference extraConfirmationClick = (CheckBoxPreference) findPreference(getString(R.string.extraConfirmationPressKey));
        final CheckBoxPreference customPreference = (CheckBoxPreference) findPreference(getString(R.string.customKey));
        final Preference customSettings = (Preference) findPreference(getString(R.string.customSettingsKey));
        if (getCustomSettings(getActivity())) {
            default7RepeatedPress.setChecked(false);
            extraConfirmationClick.setChecked(false);
            customPreference.setChecked(true);
            customSettings.setEnabled(true);
        } else {
            customPreference.setChecked(false);
            customSettings.setEnabled(false);
        }
        if (isAlarmConfirmationRequired(getActivity())) {
            enableConfirmationPatterns(true);
        } else {
            enableConfirmationPatterns(false);
        }

        Preference redoTrainingButton = (Preference) findPreference(getString(R.string.redoTrainingKey));
        redoTrainingButton.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Log.d(TAG, "Testing redo training redoTrainingButton");
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
                Log.d(TAG, "Inside on preference change of power redoTrainingButton trigger setting");

                if (selectedValue.equals(getString(R.string.activate_power_button_trigger))) {
                    getActivity().startService(new Intent(getActivity(), HardwareTriggerService.class));
                    Log.d(TAG, "Power redoTraining Button alarm trigger is enabled");
                } else {
                    getActivity().stopService(new Intent(getActivity(), HardwareTriggerService.class));
                    displayNotification();
                    Log.d(TAG, "Power redoTrainingButton alarm trigger is disabled");
                }
                return true;
            }
        });

        Preference alertConfirmationSettings = (Preference) findPreference(getString(R.string.confirmationSequenceKey));

        alertConfirmationSettings.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object selectedValue) {
                Log.d(TAG, "Inside of Alarm confirmation settings");

                if (selectedValue.equals(getString(R.string.confirmationSequenceDefault))) {
                    // disable Confirmation Wait Time/ Confirmation Wait Vibration
                    enableConfirmationPatterns(false);
                    setAlarmConfirmationRequired(getActivity(), false);
                    setConfirmationFeedbackVibrationPattern(getActivity(), ALARM_SENDING_CONFIRMATION_PATTERN_NONE);
                    Log.d(TAG, "default confirmation press deactivated");
                } else {
                    // enable Confirmation Wait Time/ Confirmation Wait Vibration
                    enableConfirmationPatterns(true);
                    setAlarmConfirmationRequired(getActivity(), true);
                    setConfirmationFeedbackVibrationPattern(getActivity(), ALARM_SENDING_CONFIRMATION_PATTERN_LONG);
                    Log.d(TAG, "Confirmation press enabled");
                }
                return true;
            }
        });


        default7RepeatedPress.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object selectedValue) {
                Log.d(TAG, "Inside on preference change of default 7 repeated press checkbox " + selectedValue.getClass());

                if ((Boolean) selectedValue) {
                    extraConfirmationClick.setChecked(false);
                    customPreference.setChecked(false);
                    customSettings.setEnabled(false);
                    setInitialClicksForAlertTrigger(getActivity(), ALARM_7_REPEATED_CLICKS);
                    setConfirmationFeedbackVibrationPattern(getActivity(), ALARM_SENDING_CONFIRMATION_PATTERN_NONE);
                    Log.d(TAG, "Default 7 presses to trigger alarm without confirmation click");
                }
                return true;
            }
        });
        extraConfirmationClick.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object selectedValue) {
                Log.d(TAG, "Inside on preference change of 5 presses and a confirmation click " + selectedValue.getClass());

                if ((Boolean) selectedValue) {
                    default7RepeatedPress.setChecked(false);
                    customPreference.setChecked(false);
                    customSettings.setEnabled(false);
                    // make the press 5 plus confirm
                    setAlarmConfirmationRequired(getActivity(), true);
                    setInitialClicksForAlertTrigger(getActivity(), ALARM_5_REPEATED_CLICKS);
                    setConfirmationFeedbackVibrationPattern(getActivity(), ALARM_SENDING_CONFIRMATION_PATTERN_LONG);
                    Log.d(TAG, "Default 5 presses to trigger alarm with confirmation click");
                }
                return true;
            }
        });

        customPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object selectedValue) {
                Log.d(TAG, "Inside on preference change of custom checkbox selection " + selectedValue.getClass());

                if ((Boolean) selectedValue) {
                    default7RepeatedPress.setChecked(false);
                    extraConfirmationClick.setChecked(false);
                    customSettings.setEnabled(true);
                    setConfirmationFeedbackVibrationPattern(getActivity(), ALARM_SENDING_CONFIRMATION_PATTERN_NONE);
                    Log.d(TAG, "Custom setting are enabled and confirmation click defaults to false");
                }
                return true;
            }
        });
    }

    private void enableConfirmationPatterns(boolean flag) {
        Preference confirmationWaitTime = (Preference) findPreference(getString(R.string.confirmationWaitTimeKey));
        confirmationWaitTime.setEnabled(flag);
        Preference confirmationWaitVibration = (Preference) findPreference(getString(R.string.hapticFeedbackVibrationPatternKey));
        confirmationWaitVibration.setEnabled(flag);
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

