package com.amnesty.panicbutton.twitter;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.amnesty.panicbutton.R;
import com.google.gson.Gson;
import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectView;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.R.layout.simple_spinner_item;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

public class TwitterShortCodeFragment extends RoboFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.twitter_short_code_fragment, container, false);
        countrySpinner = (Spinner) view.findViewById(R.id.country_spinner);
        serviceProviderSpinner = (Spinner) view.findViewById(R.id.service_provider_spinner);

        initAllCountriesShortCodeMap();
        initCountryList();
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

    private void initCountryList() {
        SpinnerAdapter countrySpinnerAdapter = new ArrayAdapter<String>(getActivity(), simple_spinner_item,
                new ArrayList<String>(allCountriesShortCodeMap.keySet()));
        countrySpinner.setAdapter(countrySpinnerAdapter);
        countrySpinner.setOnItemSelectedListener(countryOnSelectListener);
    }

    private AdapterView.OnItemSelectedListener countryOnSelectListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
            String currentCountry = (String) parent.getItemAtPosition(i);
            selectedShortCodeMap = new TwitterShortCodeMap(currentCountry, allCountriesShortCodeMap.get(currentCountry));

            SpinnerAdapter serviceProviderAdapter = new ArrayAdapter<String>(view.getContext(), simple_spinner_item,
                    selectedShortCodeMap.getServiceProviders());
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
            String shortCode = selectedShortCodeMap.getShortCode((String) parent.getItemAtPosition(i));
            shortCodeTextView.setText(shortCode);
            shortCodeLayout.setVisibility(VISIBLE);
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {
        }
    };

    private TwitterShortCodeMap selectedShortCodeMap;

    private Map<String, Map<String, String>> allCountriesShortCodeMap = new HashMap<String, Map<String, String>>();

    private Spinner countrySpinner;

    private Spinner serviceProviderSpinner;

    @InjectView(R.id.twitter_short_code)
    private TextView shortCodeTextView;

    @InjectView(R.id.twitter_short_code_layout)
    private ViewGroup shortCodeLayout;

    private static final String FILE_NAME = "twitter_short_codes.json";
}