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
import static com.amnesty.panicbutton.R.string.select_phone_service_hint;

public class TwitterShortCodeFragment extends RoboFragment {
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
        countrySpinner.setSelection(countrySpinnerAdapter.getCount());
        countrySpinner.setOnItemSelectedListener(countryOnSelectListener);
    }

    private AdapterView.OnItemSelectedListener countryOnSelectListener = new OnItemSelectedListenerAdapter() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (isHintTextSelected(position, countrySpinner)) {
                return;
            }
            String currentCountry = (String) parent.getItemAtPosition(position);
            shortCodeSettings = new ShortCodeSettings(currentCountry);

            List<String> serviceProviders = twitterSeedData.getServiceProviders(currentCountry);
            serviceProviders.add(getString(R.string.other_phone_service));
            HintSpinnerAdapter serviceProviderAdapter = new HintSpinnerAdapter(getString(select_phone_service_hint),
                    serviceProviders, getActivity());
            serviceProviderSpinner.setAdapter(serviceProviderAdapter);
            serviceProviderSpinner.setSelection(serviceProviderAdapter.getCount());
            serviceProviderSpinner.setOnItemSelectedListener(serviceProviderOnSelectListener);
            shortCodeLayout.setVisibility(INVISIBLE);
        }
    };

    private AdapterView.OnItemSelectedListener serviceProviderOnSelectListener = new OnItemSelectedListenerAdapter() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (isHintTextSelected(position, serviceProviderSpinner)) {
                sendBroadCast(false);
                return;
            }
            String selectedServiceProvider = (String) parent.getItemAtPosition(position);
            String shortCode = twitterSeedData.getShortCode(shortCodeSettings.getCountry(), selectedServiceProvider);
            shortCodeSettings.setServiceProvider(selectedServiceProvider);
            shortCodeSettings.setShortCode(shortCode);

            processShortCodeChange(selectedServiceProvider);
            shortCodeTextView.setText(shortCode);
            shortCodeLayout.setVisibility(VISIBLE);
        }
    };

    public void displaySettings(ShortCodeSettings shortCodeSettings) {
        if(shortCodeSettings.getCountry() == null) {
            reset();
            return;
        }
        setSelection(countrySpinner, shortCodeSettings.getCountry());
        setSelection(serviceProviderSpinner, shortCodeSettings.getServiceProvider());
        this.shortCodeSettings = shortCodeSettings;
    }

    public void reset() {
        countrySpinner.setSelection(countrySpinner.getAdapter().getCount(), true);
        serviceProviderSpinner.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item));
        shortCodeHelpText.setText("");
        shortCodeTextView.setText("");
        shortCodeLayout.setVisibility(INVISIBLE);
    }

    private void setSelection(Spinner spinner, String value) {
        ArrayAdapter<String> adapter = (ArrayAdapter<String>) spinner.getAdapter();
        if (adapter != null) {
            spinner.setSelection(adapter.getPosition(value), true);
        }
    }

    private void processShortCodeChange(String selectedServiceProvider) {
        if (selectedServiceProvider.equals(getString(R.string.other_phone_service))) {
            shortCodeHelpText.setText(getString(R.string.twitter_provider_not_supported_text));
            sendBroadCast(false);

        } else {
            shortCodeHelpText.setText(getString(R.string.twitter_help_text));
            sendBroadCast(true);
        }
    }

    void sendBroadCast(boolean isShortCodeValid) {
        if (isShortCodeValid) {
            getActivity().sendBroadcast(new Intent(TwitterIntentAction.VALID_SHORT_CODE.getAction()));
        } else {
            getActivity().sendBroadcast(new Intent(TwitterIntentAction.INVALID_SHORT_CODE.getAction()));
        }
    }

    private boolean isHintTextSelected(int currentPosition, Spinner spinner) {
        return currentPosition == spinner.getAdapter().getCount();
    }

    public ShortCodeSettings getShortCodeSettings() {
        return shortCodeSettings;
    }

    private TwitterSeedData twitterSeedData;
    private ShortCodeSettings shortCodeSettings;

    private Spinner countrySpinner;
    private Spinner serviceProviderSpinner;

    @InjectView(R.id.twitter_short_code)
    private TextView shortCodeTextView;

    @InjectView(R.id.twitter_help_text)
    private TextView shortCodeHelpText;

    @InjectView(R.id.twitter_short_code_layout)
    private ViewGroup shortCodeLayout;
}