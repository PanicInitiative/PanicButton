package com.amnesty.panicbutton.twitter;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import com.amnesty.panicbutton.R;
import com.google.gson.Gson;
import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectView;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

public class TwitterShortCodeFragment extends RoboFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.twitter_short_code_fragment, container, false);
        countrySpinner = (Spinner) view.findViewById(R.id.country_spinner);
        serviceProviderSpinner = (Spinner) view.findViewById(R.id.service_provider_spinner);

        initAllCountriesShortCodeMap();
        initCountrySpinner();
        return view;
    }

    private void initAllCountriesShortCodeMap() {
        try {
            AssetManager assetManager = this.getActivity().getApplicationContext().getAssets();
            InputStreamReader inputStreamReader = new InputStreamReader(assetManager.open(FILE_NAME));
            allCountriesShortCodeMap = new Gson().fromJson(inputStreamReader, Map.class);
        } catch (Exception e) {
            Log.e(getClass().getSimpleName(), "Error reading shortCodes : " + e.getMessage());
        }
    }

    private void initCountrySpinner() {
        List<String> countries = new ArrayList<String>(allCountriesShortCodeMap.keySet());
        SpinnerAdapter countrySpinnerAdapter = new HintSpinnerAdapter(getString(R.string.select_country_hint), countries, getActivity());

        countrySpinner.setAdapter(countrySpinnerAdapter);
        countrySpinner.setSelection(countrySpinnerAdapter.getCount());
        countrySpinner.setOnItemSelectedListener(countryOnSelectListener);
    }

    private AdapterView.OnItemSelectedListener countryOnSelectListener = new OnItemSelectedListenerAdapter() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if(isHintTextSelected(position, countrySpinner)) {
                return;
            }
            String currentCountry = (String) parent.getItemAtPosition(position);
            selectedCountryMap = new CountryServiceProviderMap(currentCountry, allCountriesShortCodeMap.get(currentCountry));

            List<String> serviceProviders = selectedCountryMap.getServiceProviders();
            serviceProviders.add(getString(R.string.other_phone_service));
            HintSpinnerAdapter serviceProviderAdapter = new HintSpinnerAdapter(getString(R.string.select_phone_service_hint), serviceProviders, getActivity());
            serviceProviderSpinner.setAdapter(serviceProviderAdapter);
            serviceProviderSpinner.setSelection(serviceProviderAdapter.getCount());
            serviceProviderSpinner.setOnItemSelectedListener(serviceProviderOnSelectListener);
            shortCodeLayout.setVisibility(INVISIBLE);
        }
    };

    private AdapterView.OnItemSelectedListener serviceProviderOnSelectListener = new OnItemSelectedListenerAdapter() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if(isHintTextSelected(position, serviceProviderSpinner)) {
                return;
            }
            String selectedServiceProvider = (String) parent.getItemAtPosition(position);
            updateShortCodeHelpText(selectedServiceProvider);
            String shortCode = selectedCountryMap.getShortCode(selectedServiceProvider);
            shortCodeTextView.setText(shortCode);
            shortCodeLayout.setVisibility(VISIBLE);
        }
    };

    private void updateShortCodeHelpText(String selectedServiceProvider) {
        if(selectedServiceProvider.equals(getString(R.string.other_phone_service))) {
            shortCodeHelpText.setText(getString(R.string.twitter_provider_not_supported_text));
        } else {
            shortCodeHelpText.setText(getString(R.string.twitter_help_text));
        }
    }

    private boolean isHintTextSelected(int currentPosition, Spinner spinner) {
        return currentPosition == spinner.getAdapter().getCount();
    }

    private CountryServiceProviderMap selectedCountryMap;
    private Map<String, Map<String, String>> allCountriesShortCodeMap = new HashMap<String, Map<String, String>>();

    private Spinner countrySpinner;
    private Spinner serviceProviderSpinner;

    @InjectView(R.id.twitter_short_code)
    private TextView shortCodeTextView;
    @InjectView(R.id.twitter_help_text)
    private TextView shortCodeHelpText;
    @InjectView(R.id.twitter_short_code_layout)
    private ViewGroup shortCodeLayout;

    private static final String FILE_NAME = "twitter_short_codes.json";
}