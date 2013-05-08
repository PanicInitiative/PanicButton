package com.amnesty.panicbutton.twitter;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import com.amnesty.panicbutton.R;
import roboguice.activity.RoboFragmentActivity;
import roboguice.inject.InjectView;

import java.util.List;

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
        initCountryList();
    }

    private void initCountryList() {
        SpinnerAdapter countrySpinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, countries());
        countrySpinner.setAdapter(countrySpinnerAdapter);
    }

    List<String> countries() {
        return TwitterShortCodeReader.getInstance(getApplicationContext()).countries();
    }

    public void toggleCountryAndService(View view) {
        countrySpinner.setVisibility(optTwitterCheckbox.isChecked() ? VISIBLE : INVISIBLE);
        phoneServiceSpinner.setVisibility(optTwitterCheckbox.isChecked() ? VISIBLE : INVISIBLE);
    }
}