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

public class TwitterShortCodeReader {
    private static final String FILE_NAME = "twitter_short_codes.json";
    private static final String TAG = TwitterShortCodeReader.class.getSimpleName();
    private static TwitterShortCodeReader twitterShortCodeReader;

    private Map<String, Map<String, String>> shortCodeMap = new Hashtable<String, Map<String, String>>();

    private TwitterShortCodeReader() {
    }

    public static TwitterShortCodeReader getInstance(Context context) {
        if (twitterShortCodeReader == null) {
            try {
                AssetManager assetManager = context.getAssets();
                InputStreamReader inputStreamReader = new InputStreamReader(assetManager.open(FILE_NAME));
                Map<String, Map<String, String>> shortCodeMap = new Gson().fromJson(inputStreamReader, Map.class);
                twitterShortCodeReader = new TwitterShortCodeReader();
                twitterShortCodeReader.shortCodeMap = shortCodeMap;
            } catch (Exception e) {
                Log.e(TAG, "Error reading shortCodes : " + e.getMessage());
            }
        }
        return twitterShortCodeReader;
    }

    public List<String> countries() {
        return new ArrayList<String>(shortCodeMap.keySet());
    }
}