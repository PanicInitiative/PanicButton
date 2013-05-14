package com.amnesty.panicbutton.twitter;

import android.app.Application;
import android.support.v4.app.FragmentManager;
import android.widget.Button;
import android.widget.CheckBox;
import com.amnesty.panicbutton.R;
import org.codehaus.plexus.util.ReflectionUtils;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.robolectric.Robolectric.shadowOf;

@RunWith(RobolectricTestRunner.class)
public class TwitterSettingsActivityTest {
    private TwitterSettingsActivity twitterSettingsActivity;
    private TwitterSettingsFragment twitterSettingsFragment;
    private CheckBox optTwitterCheckbox;
    private Application context;

    private Button saveButton;

    @Before
    public void setup() throws IllegalAccessException {
        initMocks(this);
        context = Robolectric.application;
        twitterSettingsActivity = new TwitterSettingsActivity();
        twitterSettingsActivity.onCreate(null);

        optTwitterCheckbox = (CheckBox) twitterSettingsActivity.findViewById(R.id.opt_twitter_checkbox);
        saveButton = (Button) twitterSettingsActivity.findViewById(R.id.twitter_save_button);

        FragmentManager supportFragmentManager = twitterSettingsActivity.getSupportFragmentManager();
        twitterSettingsFragment = (TwitterSettingsFragment) supportFragmentManager.findFragmentById(R.id.twitter_settings_fragment);
    }

    @Test
    public void shouldHideTwitterSettingsFragmentOnCreateWhenTwitterConfigured() {
        TwitterSettings.disable(Robolectric.application);
        twitterSettingsActivity = new TwitterSettingsActivity();
        twitterSettingsActivity.onCreate(null);

        FragmentManager supportFragmentManager = twitterSettingsActivity.getSupportFragmentManager();
        twitterSettingsFragment = (TwitterSettingsFragment) supportFragmentManager.findFragmentById(R.id.twitter_settings_fragment);

        assertEquals(R.id.twitter_settings_layout_root,
                shadowOf(twitterSettingsActivity).getContentView().getId());
    }

    @Test
    public void shouldShowTwitterSettingsFragmentOnCreateWhenTwitterConfigured() {
        TwitterSettings.enable(Robolectric.application);
        twitterSettingsActivity = new TwitterSettingsActivity();
        twitterSettingsActivity.onCreate(null);

        FragmentManager supportFragmentManager = twitterSettingsActivity.getSupportFragmentManager();
        twitterSettingsFragment = (TwitterSettingsFragment) supportFragmentManager.findFragmentById(R.id.twitter_settings_fragment);

        assertTrue(twitterSettingsFragment.isVisible());
    }

    @Test
    public void shouldShowTwitterSettingsOnEnablingCheckbox() {
        optTwitterCheckbox.setChecked(false);
        optTwitterCheckbox.performClick();
        assertTrue(twitterSettingsFragment.isVisible());
    }

    @Test
    @Ignore
    public void shouldHideTwitterSettingsOnDisablingCheckbox() {
        optTwitterCheckbox.setChecked(false);
        saveButton.performClick();
        assertTrue(twitterSettingsFragment.isHidden());
    }

    @Test
    public void shouldDisableTwitterSettings() {
        optTwitterCheckbox.setChecked(false);
        saveButton.performClick();

        assertFalse(TwitterSettings.isEnabled(context));
    }

    @Test
    public void shouldEnableAndSaveTwitterSettings() throws IllegalAccessException {
        TwitterSettingsFragment mock = mock(TwitterSettingsFragment.class);
        ReflectionUtils.setVariableValueInObject(twitterSettingsActivity, "twitterSettingsFragment", mock);

        optTwitterCheckbox.setChecked(true);
        saveButton.performClick();

        assertTrue(TwitterSettings.isEnabled(context));
        verify(mock).save();
    }
}