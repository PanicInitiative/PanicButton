package com.apb.beacon.twitter;

import android.content.Context;
import android.content.SharedPreferences;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowPreferenceManager;

import java.util.Map;

import static org.junit.Assert.*;

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
    public void shouldSaveTwitterSettings() {
        ShortCodeSettings shortCodeSettings = new ShortCodeSettings("India");
        shortCodeSettings.setServiceProvider("Airtel");
        shortCodeSettings.setShortCode("53000");
        TwitterSettings twitterSettings  = new TwitterSettings(shortCodeSettings, "Test Message");

        TwitterSettings.save(context, twitterSettings);

        Map<String, String> allPreferences = (Map<String, String>) sharedPreferences.getAll();
        assertEquals("Test Message", allPreferences.get("TWITTER_MESSAGE"));
        assertEquals("India", allPreferences.get("TWITTER_COUNTRY"));
        assertEquals("Airtel", allPreferences.get("TWITTER_SERVICE_PROVIDER"));
        assertEquals("53000", allPreferences.get("TWITTER_SHORT_CODE"));
    }

    @Test
    public void shouldReturnValidOnlyIfShortCodeIsConfiguredProperly() {
        ShortCodeSettings shortCodeSettings = new ShortCodeSettings("India");
        TwitterSettings settings = new TwitterSettings(shortCodeSettings,"Test Message");
        assertFalse(settings.isValid());

        shortCodeSettings.setShortCode("53000");
        assertTrue(settings.isValid());
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
