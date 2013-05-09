package com.amnesty.panicbutton.twitter;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import com.amnesty.panicbutton.R;
import roboguice.activity.RoboFragmentActivity;
import roboguice.inject.InjectView;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

public class TwitterSettingsActivity extends RoboFragmentActivity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.twitter_settings_layout);
    }

    public void toggleTwitterSettings(View view) {
        shortCodeLayout.setVisibility(optTwitterCheckbox.isChecked() ? VISIBLE : INVISIBLE);
    }

    @InjectView(R.id.twitter_short_code_layout)
    private ViewGroup shortCodeLayout;

    @InjectView(R.id.opt_twitter_checkbox)
    private CheckBox optTwitterCheckbox;
}