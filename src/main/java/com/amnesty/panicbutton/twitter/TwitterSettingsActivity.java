package com.amnesty.panicbutton.twitter;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Spinner;
import com.amnesty.panicbutton.R;
import roboguice.activity.RoboFragmentActivity;
import roboguice.inject.InjectView;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

public class TwitterSettingsActivity extends RoboFragmentActivity {
    @InjectView(R.id.country_spinner)
    private Spinner countrySpinner;

    @InjectView(R.id.phone_service_spinner)
    private Spinner phoneServiceSpinner;

    @InjectView(R.id.opt_twitter_checkbox)
    private CheckBox optTwitterCheckbox;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.twitter_settings_layout);
    }

    public void toggleCountryAndService(View view) {
        countrySpinner.setVisibility(optTwitterCheckbox.isChecked() ? VISIBLE : INVISIBLE);
        phoneServiceSpinner.setVisibility(optTwitterCheckbox.isChecked() ? VISIBLE : INVISIBLE);
    }
}