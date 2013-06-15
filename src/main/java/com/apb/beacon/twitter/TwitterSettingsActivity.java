package com.apb.beacon.twitter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.apb.beacon.CalculatorActivity;
import com.apb.beacon.HomeActivity;
import com.apb.beacon.R;
import com.apb.beacon.SettingsActivity;
import com.apb.beacon.SoftKeyboard;
import com.apb.beacon.wizard.ActionButtonStateListener;
import com.apb.beacon.wizard.WizardActivity;

import roboguice.activity.RoboFragmentActivity;
import roboguice.inject.InjectFragment;
import roboguice.inject.InjectView;

public class TwitterSettingsActivity extends RoboFragmentActivity implements ActionButtonStateListener {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.twitter_settings_layout);
        boolean isEnabled = TwitterSettings.isEnabled(this);
        optTwitterCheckbox.setChecked(isEnabled);
        twitterSettingsFragment.setVisibility(isEnabled);
    }

    public void toggleTwitterSettings(View view) {
        twitterSettingsFragment.setVisibility(optTwitterCheckbox.isChecked());
    }


    public void onSave(View view) {
        saveSettings();
        SoftKeyboard.hide(this, this.findViewById(android.R.id.content));
        Toast.makeText(this, R.string.twitter_save_successful, Toast.LENGTH_SHORT).show();
    }

    private void saveSettings() {
        if (!optTwitterCheckbox.isChecked()) {
            TwitterSettings.disable(this);
            return;
        }
        TwitterSettings.enable(this);
        twitterSettingsFragment.save();
    }

    public void goBack(View view) {
        this.finish();
        startActivity(new Intent(this, SettingsActivity.class));
    }

    @Override
    public void enableActionButton(boolean isEnabled) {
        saveButton.setEnabled(isEnabled);
    }

    @InjectView(R.id.twitter_save_button)
    private Button saveButton;

    @InjectView(R.id.opt_twitter_checkbox)
    private CheckBox optTwitterCheckbox;

    @InjectFragment(R.id.twitter_settings_fragment)
    private TwitterSettingsFragment twitterSettingsFragment;
}