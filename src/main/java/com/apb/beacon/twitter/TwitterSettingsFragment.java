package com.apb.beacon.twitter;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.apb.beacon.R;
import com.apb.beacon.common.MessageFragment;
import com.apb.beacon.wizard.ActionButtonStateListener;
import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectFragment;

import static com.apb.beacon.twitter.TwitterIntentAction.INVALID_SHORT_CODE;
import static com.apb.beacon.twitter.TwitterIntentAction.VALID_SHORT_CODE;

public class TwitterSettingsFragment extends RoboFragment {
    @InjectFragment(R.id.twitter_message_fragment)
    private MessageFragment twitterMessageFragment;

    @InjectFragment(R.id.twitter_short_code_fragment)
    private TwitterShortCodeFragment twitterShortCodeFragment;
    private ActionButtonStateListener callback;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        callback = (ActionButtonStateListener) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.twitter_settings_fragment, container, false);
        registerShortCodeValidityReceiver();
        return inflate;
    }

    public void save() {
        ShortCodeSettings shortCodeSettings = twitterShortCodeFragment.getShortCodeSettings();
        TwitterSettings twitterSettings = new TwitterSettings(shortCodeSettings, twitterMessageFragment.getMessage());
        TwitterSettings.save(getActivity(), twitterSettings);
    }

    public void setVisibility(boolean isVisible) {
        setFragmentVisibility(this, isVisible);
        TwitterSettings twitterSettings = TwitterSettings.retrieve(getActivity());
        if (isVisible) {
            loadSettings(twitterSettings);
            return;
        }
        callback.enableActionButton(twitterSettings.isValid());
    }

    private void loadSettings(TwitterSettings twitterSettings) {
        twitterMessageFragment.setMessage(twitterSettings.getMessage());
        twitterShortCodeFragment.displaySettings(twitterSettings.getShortCodeSettings());
        if (!twitterSettings.isValid()) {
            setFragmentVisibility(twitterMessageFragment, false);
        }
    }

    private void setFragmentVisibility(Fragment fragment, boolean isVisible) {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        if (isVisible) {
            fragmentTransaction.show(fragment);
        } else {
            fragmentTransaction.hide(fragment);
        }
        fragmentTransaction.commit();
    }

    private BroadcastReceiver shortCodeValidityReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            TwitterIntentAction twitterIntentAction = TwitterIntentAction.get(action);
            setFragmentVisibility(twitterMessageFragment, twitterIntentAction.getState());
            callback.enableActionButton(twitterIntentAction.getState());
        }
    };

    private void registerShortCodeValidityReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(INVALID_SHORT_CODE.getAction());
        filter.addAction(VALID_SHORT_CODE.getAction());
        getActivity().registerReceiver(shortCodeValidityReceiver, filter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroy();
        getActivity().unregisterReceiver(shortCodeValidityReceiver);
    }
}
