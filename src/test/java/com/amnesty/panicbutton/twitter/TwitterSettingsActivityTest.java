package com.amnesty.panicbutton.twitter;

import android.widget.CheckBox;
import android.widget.Spinner;
import com.amnesty.panicbutton.R;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.*;
import static org.robolectric.Robolectric.shadowOf;

@RunWith(RobolectricTestRunner.class)
public class TwitterSettingsActivityTest {
    private TwitterSettingsActivity twitterSettingsActivity;
    private CheckBox optTwitterCheckbox;
    private Spinner countrySpinner;
    private Spinner phoneServiceSpinner;

    @Before
    public void setup() {
        twitterSettingsActivity = new TwitterSettingsActivity();
        twitterSettingsActivity.onCreate(null);

        optTwitterCheckbox = (CheckBox) twitterSettingsActivity.findViewById(R.id.opt_twitter_checkbox);
        countrySpinner = (Spinner) twitterSettingsActivity.findViewById(R.id.country_spinner);
        phoneServiceSpinner = (Spinner) twitterSettingsActivity.findViewById(R.id.phone_service_spinner);
    }

    @Test
    public void shouldLoadTwitterLayoutOnCreate() {
        assertEquals(R.id.twitter_settings_layout_root, shadowOf(twitterSettingsActivity).getContentView().getId());
        assertFalse(countrySpinner.isShown());
        assertFalse(phoneServiceSpinner.isShown());
    }

    @Test
    public void shouldShowCountryAndPhoneServiceListOnEnablingTwitterAlert() {
        optTwitterCheckbox.performClick();
        assertTrue(countrySpinner.isShown());
        assertTrue(phoneServiceSpinner.isShown());
    }

    @Test
    public void shouldHideCountryAndPhoneServiceListOnDisablingTwitterAlert() {
        optTwitterCheckbox.setChecked(true);
        optTwitterCheckbox.performClick();
        assertFalse(countrySpinner.isShown());
        assertFalse(phoneServiceSpinner.isShown());
    }
}
