package com.amnesty.panicbutton.twitter;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;
import com.google.gson.Gson;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class TwitterShortCodes {
    private static final String FILE_NAME = "twitter_short_codes.json";
    private static final String TAG = TwitterShortCodes.class.getSimpleName();
    private static TwitterShortCodes twitterShortCodes;

    private Map<String, Map<String, String>> shortCodeMap = new Hashtable<String, Map<String, String>>();

    private TwitterShortCodes() {
    }

    public static TwitterShortCodes getInstance(Context context) {
        if (twitterShortCodes == null) {
            try {
                AssetManager assetManager = context.getAssets();
                InputStreamReader inputStreamReader = new InputStreamReader(assetManager.open(FILE_NAME));
                Map<String, Map<String, String>> shortCodeMap = new Gson().fromJson(inputStreamReader, Map.class);
                twitterShortCodes = new TwitterShortCodes();
                twitterShortCodes.shortCodeMap = shortCodeMap;
            } catch (Exception e) {
                Log.e(TAG, "Error reading shortCodes : " + e.getMessage());
            }
        }
        return twitterShortCodes;
    }

    public List<String> countries() {
        return new ArrayList<String>(shortCodeMap.keySet());
    }

    public List<String> serviceProviders(String country) {
        return new ArrayList<String>(shortCodeMap.get(country).keySet());
    }

    public String shortCode(String country, String serviceProvider) {
        return shortCodeMap.get(country).get(serviceProvider);
    }
}