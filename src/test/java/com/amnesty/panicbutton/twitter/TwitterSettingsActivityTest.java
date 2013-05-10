package com.amnesty.panicbutton.twitter;

import android.app.Application;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import com.amnesty.panicbutton.R;
import com.amnesty.panicbutton.common.MessageFragment;
import org.codehaus.plexus.util.ReflectionUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import static android.view.View.VISIBLE;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.robolectric.Robolectric.shadowOf;

@RunWith(RobolectricTestRunner.class)
public class TwitterSettingsActivityTest {
    private TwitterSettingsActivity twitterSettingsActivity;
    private CheckBox optTwitterCheckbox;
    private Button saveButton;
    private ViewGroup shortCodeLayout;
    private ViewGroup messageLayout;

    private ShortCodeSettings shortCodeSettings;

    @Mock
    private TwitterShortCodeFragment mockTwitterShortCodeFragment;

    @Mock
    private MessageFragment mockTwitterMessageFragment;
    private String country;
    private String serviceProvider;
    private String shortCode;
    private Application context;

    @Before
    public void setup() throws IllegalAccessException {
        initMocks(this);
        context = Robolectric.application;

        country = "India";
        serviceProvider = "Airtel";
        shortCode = "53000";
        setShortCodeSettings();

        twitterSettingsActivity = new TwitterSettingsActivity();
        twitterSettingsActivity.onCreate(null);
        ReflectionUtils.setVariableValueInObject(twitterSettingsActivity, "twitterShortCodeFragment", mockTwitterShortCodeFragment);
        ReflectionUtils.setVariableValueInObject(twitterSettingsActivity, "twitterMessageFragment", mockTwitterMessageFragment);

        optTwitterCheckbox = (CheckBox) twitterSettingsActivity.findViewById(R.id.opt_twitter_checkbox);
        saveButton = (Button) twitterSettingsActivity.findViewById(R.id.twitter_save_button);
        shortCodeLayout = (ViewGroup) twitterSettingsActivity.findViewById(R.id.twitter_short_code_layout);
        messageLayout = (ViewGroup) twitterSettingsActivity.findViewById(R.id.twitter_message_layout);
    }

    private void setShortCodeSettings() {
        shortCodeSettings = new ShortCodeSettings(country);
        shortCodeSettings.setServiceProvider(serviceProvider);
        shortCodeSettings.setShortCode(shortCode);
    }

    @Test
    public void shouldLoadTwitterLayoutOnCreate() {
        assertEquals(R.id.twitter_settings_layout_root, shadowOf(twitterSettingsActivity).getContentView().getId());
        assertFalse(shortCodeLayout.isShown());
        assertFalse(messageLayout.isShown());
    }

    @Test
    public void shouldShowShortCodeFragmentOnEnablingTwitter() {
        optTwitterCheckbox.performClick();
        assertTrue(shortCodeLayout.isShown());
        assertFalse(messageLayout.isShown());
    }

    @Test
    public void shouldHideShortCodeFragmentAndMessageFragmentOnDisablingTwitter() {
        optTwitterCheckbox.setChecked(true);
        shortCodeLayout.setVisibility(VISIBLE);
        messageLayout.setVisibility(VISIBLE);

        optTwitterCheckbox.performClick();

        assertFalse(shortCodeLayout.isShown());
        assertFalse(messageLayout.isShown());
    }

    @Test
    public void shouldShowTwitterEditTextOnSuccessfulShortCodeSelection() {
        twitterSettingsActivity.onShortCodeSelection(true);
        assertTrue(messageLayout.isShown());
    }

    @Test
    public void shouldHideTwitterEditTextOnUnSuccessfulShortCodeSelection() {
        twitterSettingsActivity.onShortCodeSelection(false);
        assertFalse(messageLayout.isShown());
    }

    @Test
    public void shouldSaveTwitterSettings() {
        String testMessage = "Test Message";
        optTwitterCheckbox.setChecked(true);

        when(mockTwitterShortCodeFragment.getShortCodeSettings()).thenReturn(shortCodeSettings);
        when(mockTwitterMessageFragment.getMessage()).thenReturn(testMessage);

        saveButton.performClick();

        assertTrue(TwitterSettings.isEnabled(context));
        TwitterSettings twitterSettings = TwitterSettings.retrieve(context);
        ShortCodeSettings shortCodeSettings = twitterSettings.getShortCodeSettings();
        assertEquals(testMessage, twitterSettings.getMessage());
        assertEquals(country, shortCodeSettings.getCountry());
        assertEquals(serviceProvider, shortCodeSettings.getServiceProvider());
        assertEquals(shortCode, shortCodeSettings.getShortCode());
    }

    @Test
    public void shouldSaveThatTwitterSettingsIsDisabled() {
        optTwitterCheckbox.setChecked(false);
        saveButton.performClick();
        assertFalse(TwitterSettings.isEnabled(context));
    }
}