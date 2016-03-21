package org.iilab.pb.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.CheckBoxPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.util.Log;

import org.iilab.pb.R;

import static org.iilab.pb.common.AppConstants.ALARM_SENDING_CONFIRMATION_PATTERN_LONG;
import static org.iilab.pb.common.AppConstants.ALARM_SENDING_CONFIRMATION_PATTERN_NONE;
import static org.iilab.pb.common.AppConstants.PAGE_ID;
import static org.iilab.pb.common.AppConstants.PARENT_ACTIVITY;
import static org.iilab.pb.common.ApplicationSettings.isAlarmConfirmationRequired;
import static org.iilab.pb.common.ApplicationSettings.setAlarmConfirmationRequired;
import static org.iilab.pb.common.ApplicationSettings.setConfirmationFeedbackVibrationPattern;

public class AdvancedSettingsSubScreenFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener{
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Log.d(TAG,"called for advanced settings sub screen "+ key+"  "+R.string.initialPressesKey);
        if (key.equals(getString(R.string.initialPressesKey))) {
            Preference initialClicks = findPreference(key);
            // Set summary to be the user-description for the selected value
            //// x repeated press with confirmation
            CheckBoxPreference customPreference = (CheckBoxPreference) findPreference(getString(R.string.customKey));
            Log.d(TAG, "inside initial clicks " +customPreference);
//            customPreference.setSummary(sharedPreferences.getString(key, "")+"repeated press");
        }
    }

    private static final String TAG = AdvancedSettingsSubScreenFragment.class.getName();

    public static AdvancedSettingsSubScreenFragment newInstance(String pageId, int parentActivity) {
        AdvancedSettingsSubScreenFragment f = new AdvancedSettingsSubScreenFragment();
        Bundle args = new Bundle();
        args.putString(PAGE_ID, pageId);
        args.putInt(PARENT_ACTIVITY, parentActivity);
        f.setArguments(args);
        return (f);
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        // Load the preferences from an XML resource
        setPreferencesFromResource(R.xml.preferences, rootKey);
        Log.d(TAG, "onCreatePreferences of the sub screen "+rootKey);
        Preference alertConfirmationSettings = (Preference) findPreference(getString(R.string.confirmationSequenceKey));
        if (isAlarmConfirmationRequired(getActivity())) {
            enableConfirmationPatterns(true);
        } else {
            enableConfirmationPatterns(false);
        }
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
    }

    private void enableConfirmationPatterns(boolean flag) {
        Preference confirmationWaitTime = (Preference) findPreference(getString(R.string.confirmationWaitTimeKey));
        confirmationWaitTime.setEnabled(flag);
        Preference confirmationWaitVibration = (Preference) findPreference(getString(R.string.hapticFeedbackVibrationPatternKey));
        confirmationWaitVibration.setEnabled(flag);
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }
}

