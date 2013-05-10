package com.amnesty.panicbutton.twitter;

import android.content.Context;
import android.content.SharedPreferences;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowPreferenceManager;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
public class TwitterSettingsTest {
    private SharedPreferences sharedPreferences;
    private Context context;

    @Before
    public void setUp() {
        context = Robolectric.application;
        sharedPreferences = ShadowPreferenceManager.getDefaultSharedPreferences(context);
    }

    @Test
    public void shouldReturnTwitterDisabledWhenNotConfigured() {
        assertFalse(TwitterSettings.isEnabled(context));
    }

    @Test
    public void shouldReturnTwitterIsDisabled() {
        setupTwitterOption(false);
        assertFalse(TwitterSettings.isEnabled(context));
    }

    @Test
    public void shouldReturnTwitterIsEnabled() {
        setupTwitterOption(true);
        assertTrue(TwitterSettings.isEnabled(context));
    }

    @Test
    public void shouldEnableTwitterOption() {
        TwitterSettings.enable(context);
        assertTrue(sharedPreferences.getBoolean("TWITTER_ENABLED", false));
    }

    @Test
    public void shouldDisableTwitterOption() {
        TwitterSettings.disable(context);
        assertFalse(sharedPreferences.getBoolean("TWITTER_ENABLED", true));
    }

    private void setupTwitterOption(boolean isEnabled) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("TWITTER_ENABLED", isEnabled);
        editor.commit();
    }
}
