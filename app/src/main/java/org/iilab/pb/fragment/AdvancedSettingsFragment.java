package org.iilab.pb.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.util.Log;

import org.iilab.pb.R;
import org.iilab.pb.WizardActivity;
import org.iilab.pb.trigger.HardwareTriggerService;

import static org.iilab.pb.common.AppConstants.PAGE_ID;
import static org.iilab.pb.common.AppConstants.PAGE_SETUP_ALARM_RETRAINING;
import static org.iilab.pb.common.AppConstants.PARENT_ACTIVITY;

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
        Preference button = (Preference) findPreference(getString(R.string.redoTrainingKey));
        button.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Log.d(TAG, "Testing redo training button");
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
                Log.d(TAG, "Inside on preference change of power button trigger setting");

                if (selectedValue.equals(getString(R.string.activate_power_button_trigger))) {
                    getActivity().startService(new Intent(getActivity(), HardwareTriggerService.class));
                    Log.d(TAG, "Power button alarm trigger is enabled");
                } else {
                    getActivity().stopService(new Intent(getActivity(), HardwareTriggerService.class));
                    Log.d(TAG, "Power button alarm trigger is disabled");
                }
                return true;
            }
        });
    }

}
