package com.amnesty.panicbutton.twitter;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import com.amnesty.panicbutton.R;
import roboguice.activity.RoboFragmentActivity;
import roboguice.inject.InjectFragment;
import roboguice.inject.InjectView;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

public class TwitterSettingsActivity extends RoboFragmentActivity implements TwitterShortCodeFragment.ShortCodeSelectedListener {

    @InjectView(R.id.opt_twitter_checkbox)
    private CheckBox optTwitterCheckbox;

    @InjectView(R.id.twitter_short_code_layout)
    private ViewGroup shortCodeLayout;

    @InjectView(R.id.twitter_message_layout)
    private ViewGroup messageLayout;

    @InjectFragment(R.id.twitter_short_code_fragment)
    private TwitterShortCodeFragment twitterShortCodeFragment;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.twitter_settings_layout);
    }

    public void toggleTwitterSettings(View view) {
        messageLayout.setVisibility(INVISIBLE);
        shortCodeLayout.setVisibility(optTwitterCheckbox.isChecked() ? VISIBLE : INVISIBLE);
    }

    @Override
    public void onShortCodeSelection(boolean successFlag) {
        messageLayout.setVisibility(successFlag ? VISIBLE : INVISIBLE);
    }
}