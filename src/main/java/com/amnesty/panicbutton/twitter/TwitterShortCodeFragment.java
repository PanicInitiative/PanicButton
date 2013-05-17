package com.amnesty.panicbutton.twitter;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.amnesty.panicbutton.R;
import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectView;

import java.util.List;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

public class TwitterShortCodeFragment extends RoboFragment {
    private String selectedCountry;
    private String selectedServiceProvider;
    private String selectedShortCode;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.twitter_short_code_fragment, container, false);
        countrySpinner = (Spinner) view.findViewById(R.id.country_spinner);
        serviceProviderSpinner = (Spinner) view.findViewById(R.id.service_provider_spinner);
        twitterSeedData = new TwitterSeedData(this.getActivity());
        initCountrySpinner();
        return view;
    }

    public void initCountrySpinner() {
        List<String> countries = twitterSeedData.getCountries();
        SpinnerAdapter countrySpinnerAdapter = new HintSpinnerAdapter(getString(R.string.select_country_hint), countries, getActivity());

        countrySpinner.setAdapter(countrySpinnerAdapter);
        countrySpinner.setSelection(getSelectedCountry(), true);
        countrySpinner.setOnItemSelectedListener(countryOnSelectListener);
    }

    private int getSelectedCountry() {
        if (this.selectedCountry == null)
            return countrySpinner.getCount();
        ArrayAdapter<String> adapter = (ArrayAdapter<String>) countrySpinner.getAdapter();
        return adapter.getPosition(selectedCountry);
    }

    private HintSpinnerAdapter getServiceProviderSpinnerAdapter() {
        List<String> serviceProviders = twitterSeedData.getServiceProviders(selectedCountry);
        serviceProviders.add(getString(R.string.other_phone_service));
        return new HintSpinnerAdapter(getString(R.string.select_phone_service_hint), serviceProviders, getActivity());
    }

    private int getSelectedServiceProvider() {
        if (selectedServiceProvider == null) {
            shortCodeLayout.setVisibility(INVISIBLE);
            return serviceProviderSpinner.getCount();
        }
        ArrayAdapter<String> adapter = (ArrayAdapter<String>) serviceProviderSpinner.getAdapter();
        int position = adapter.getPosition(selectedServiceProvider);
        if (position == -1) {
            shortCodeLayout.setVisibility(INVISIBLE);
            this.selectedServiceProvider = null;
            this.selectedShortCode = null;
            return serviceProviderSpinner.getCount();
        }
        return position;
    }

    private AdapterView.OnItemSelectedListener countryOnSelectListener = new OnItemSelectedListenerAdapter() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (isHintTextSelected(position, countrySpinner)) {
                return;
            }
            selectedCountry = (String) parent.getItemAtPosition(position);
            serviceProviderSpinner.setVisibility(VISIBLE);
            serviceProviderSpinner.setAdapter(getServiceProviderSpinnerAdapter());
            serviceProviderSpinner.setSelection(getSelectedServiceProvider(), true);
            serviceProviderSpinner.setOnItemSelectedListener(serviceProviderOnSelectListener);
        }
    };

    private AdapterView.OnItemSelectedListener serviceProviderOnSelectListener = new OnItemSelectedListenerAdapter() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (isHintTextSelected(position, serviceProviderSpinner)) {
                sendBroadCast();
                return;
            }
            selectedServiceProvider = (String) parent.getItemAtPosition(position);
            processShortCodeChange();
            shortCodeTextView.setText(selectedShortCode);
            shortCodeLayout.setVisibility(VISIBLE);
        }
    };

    private void processShortCodeChange() {
        if (this.selectedServiceProvider.equals(getString(R.string.other_phone_service))) {
            this.selectedShortCode = null;
            this.shortCodeHelpText.setText(getString(R.string.twitter_provider_not_supported_text));
        } else {
            this.selectedShortCode = twitterSeedData.getShortCode(selectedCountry, selectedServiceProvider);
            this.shortCodeHelpText.setText(getString(R.string.twitter_help_text));
            this.savedShortCodeSettings = new ShortCodeSettings(selectedCountry, selectedServiceProvider, selectedShortCode);
        }
        this.shortCodeTextView.setText(selectedShortCode);
        this.shortCodeLayout.setVisibility(VISIBLE);
        sendBroadCast();
    }

    public void displaySettings(ShortCodeSettings shortCodeSettings) {
        if (shortCodeSettings.getCountry() == null) {
            reset();
            return;
        }
        this.selectedCountry = shortCodeSettings.getCountry();
        this.selectedServiceProvider = shortCodeSettings.getServiceProvider();
        this.selectedShortCode = shortCodeSettings.getShortCode();

        this.serviceProviderSpinner.setAdapter(getServiceProviderSpinnerAdapter());

        this.countrySpinner.setSelection(getSelectedCountry(), true);
        this.serviceProviderSpinner.setSelection(getSelectedServiceProvider(), true);

        processShortCodeChange();
    }

    public void reset() {
        this.selectedCountry = null;
        this.selectedServiceProvider = null;
        this.selectedShortCode = null;
        this.savedShortCodeSettings = null;
        countrySpinner.setSelection(getSelectedCountry(), true);
        shortCodeHelpText.setText("");
        shortCodeTextView.setText("");
        shortCodeLayout.setVisibility(INVISIBLE);
    }

    void sendBroadCast() {
        if (this.selectedShortCode != null) {
            getActivity().sendBroadcast(new Intent(TwitterIntentAction.VALID_SHORT_CODE.getAction()));
        } else {
            getActivity().sendBroadcast(new Intent(TwitterIntentAction.INVALID_SHORT_CODE.getAction()));
        }
    }

    private boolean isHintTextSelected(int currentPosition, Spinner spinner) {
        return currentPosition == spinner.getAdapter().getCount();
    }

    public ShortCodeSettings getShortCodeSettings() {
        return this.savedShortCodeSettings;
    }

    private TwitterSeedData twitterSeedData;

    private ShortCodeSettings savedShortCodeSettings;

    private Spinner countrySpinner;
    private Spinner serviceProviderSpinner;

    @InjectView(R.id.twitter_short_code)
    private TextView shortCodeTextView;

    @InjectView(R.id.twitter_help_text)
    private TextView shortCodeHelpText;

    @InjectView(R.id.twitter_short_code_layout)
    private ViewGroup shortCodeLayout;
}