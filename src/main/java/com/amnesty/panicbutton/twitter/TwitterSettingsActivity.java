package com.amnesty.panicbutton.twitter;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.amnesty.panicbutton.R;
import roboguice.activity.RoboFragmentActivity;
import roboguice.inject.InjectView;

import java.util.List;

import static android.R.layout.simple_spinner_item;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

public class TwitterSettingsActivity extends RoboFragmentActivity {
    private String currentCountry;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.twitter_settings_layout);
        initCountryList();
    }

    private void initCountryList() {
        List<String> countries = twitterShortCodes().countries();
        SpinnerAdapter countrySpinnerAdapter = new ArrayAdapter<String>(this, simple_spinner_item, countries);
        countrySpinner.setAdapter(countrySpinnerAdapter);
        countrySpinner.setOnItemSelectedListener(countryOnSelectListener);
        shortCodeLayout.setVisibility(INVISIBLE);
    }

    public void toggleCountryAndService(View view) {
        countrySpinner.setVisibility(optTwitterCheckbox.isChecked() ? VISIBLE : INVISIBLE);
        serviceProviderSpinner.setVisibility(optTwitterCheckbox.isChecked() ? VISIBLE : INVISIBLE);
    }

    private AdapterView.OnItemSelectedListener countryOnSelectListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
            currentCountry = (String) parent.getItemAtPosition(i);
            List<String> serviceProviders = twitterShortCodes().serviceProviders(currentCountry);
            SpinnerAdapter serviceProviderAdapter = new ArrayAdapter<String>(view.getContext(), simple_spinner_item, serviceProviders);
            serviceProviderSpinner.setAdapter(serviceProviderAdapter);
            serviceProviderSpinner.setOnItemSelectedListener(serviceProviderOnSelectListener);
            shortCodeLayout.setVisibility(INVISIBLE);
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {
        }
    };

    private AdapterView.OnItemSelectedListener serviceProviderOnSelectListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
            String selectedServiceProvider = (String) parent.getItemAtPosition(i);
            String shortCode = twitterShortCodes().shortCode(currentCountry, selectedServiceProvider);

            shortCodeTextView.setText(shortCode);
            shortCodeLayout.setVisibility(VISIBLE);
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {
        }
    };

    private TwitterShortCodes twitterShortCodes() {
        return TwitterShortCodes.getInstance(getApplicationContext());
    }

    @InjectView(R.id.country_spinner)
    private Spinner countrySpinner;

    @InjectView(R.id.service_provider_spinner)
    private Spinner serviceProviderSpinner;

    @InjectView(R.id.opt_twitter_checkbox)
    private CheckBox optTwitterCheckbox;

    @InjectView(R.id.twitter_short_code)
    private TextView shortCodeTextView;

    @InjectView(R.id.twitter_short_code_layout)
    private ViewGroup shortCodeLayout;
}