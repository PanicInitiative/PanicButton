package com.amnesty.panicbutton.twitter;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;
import com.google.gson.Gson;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TwitterSeedData {
    private Map<String, Map<String, String>> countryMap = new HashMap<String, Map<String, String>>();

    private static final String FILE_NAME = "twitter_short_codes.json";

    public TwitterSeedData(Context context) {
        try {
            AssetManager assetManager = context.getApplicationContext().getAssets();
            InputStreamReader inputStreamReader = new InputStreamReader(assetManager.open(FILE_NAME));
            countryMap = new Gson().fromJson(inputStreamReader, Map.class);
        } catch (Exception e) {
            Log.e(getClass().getSimpleName(), "Error reading shortCodes : " + e.getMessage());
        }
    }

    public List<String> getCountries() {
        return new ArrayList<String>(countryMap.keySet());
    }

    public List<String> getServiceProviders(String country) {
        return new ArrayList<String>(countryMap.get(country).keySet());
    }

    public String getShortCode(String country, String serviceProvider) {
        return countryMap.get(country).get(serviceProvider);
    }
}