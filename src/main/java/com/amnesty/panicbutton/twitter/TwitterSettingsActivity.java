package com.amnesty.panicbutton.twitter;

import android.os.Bundle;
import android.widget.CheckBox;
import com.amnesty.panicbutton.R;
import roboguice.activity.RoboFragmentActivity;
import roboguice.inject.InjectView;

public class TwitterSettingsActivity extends RoboFragmentActivity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.twitter_settings_layout);
    }

    @InjectView(R.id.opt_twitter_checkbox)
    private CheckBox optTwitterCheckbox;
}