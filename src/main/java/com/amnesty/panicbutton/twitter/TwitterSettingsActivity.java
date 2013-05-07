package com.amnesty.panicbutton.twitter;

import android.os.Bundle;
import com.amnesty.panicbutton.R;
import roboguice.activity.RoboFragmentActivity;

public class TwitterSettingsActivity extends RoboFragmentActivity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.twitter_settings_layout);
    }
}