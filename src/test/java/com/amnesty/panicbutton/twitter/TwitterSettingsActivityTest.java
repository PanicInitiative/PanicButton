package com.amnesty.panicbutton.twitter;

import android.widget.CheckBox;
import android.widget.Spinner;
import com.amnesty.panicbutton.R;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.robolectric.Robolectric.shadowOf;

@RunWith(RobolectricTestRunner.class)
public class TwitterSettingsActivityTest {
    private TwitterSettingsActivity twitterSettingsActivity;
    private CheckBox optTwitterCheckbox;
    private Spinner countrySpinner;
    private Spinner serviceProviderSpinner;

    @Before
    public void setup() {
        twitterSettingsActivity = new TwitterSettingsActivity() {
            List<String> countries() {
                return Arrays.asList("India", "UK");
            }
        };
        twitterSettingsActivity.onCreate(null);

        optTwitterCheckbox = (CheckBox) twitterSettingsActivity.findViewById(R.id.opt_twitter_checkbox);
        countrySpinner = (Spinner) twitterSettingsActivity.findViewById(R.id.country_spinner);
        serviceProviderSpinner = (Spinner) twitterSettingsActivity.findViewById(R.id.service_provider_spinner);
    }

    @Test
    public void shouldLoadTwitterLayoutOnCreate() {
        assertEquals(R.id.twitter_settings_layout_root, shadowOf(twitterSettingsActivity).getContentView().getId());
        assertFalse(countrySpinner.isShown());
        assertFalse(serviceProviderSpinner.isShown());
    }

    @Test
    public void shouldLoadCountrySpinnerOnCreate() {
        assertEquals(2, countrySpinner.getCount());
    }

    @Test
    public void shouldShowCountryAndServiceProviderListOnEnablingTwitterAlert() {
        optTwitterCheckbox.performClick();
        assertTrue(countrySpinner.isShown());
        assertTrue(serviceProviderSpinner.isShown());
    }

    @Test
    public void shouldHideCountryAndServiceProviderListOnDisablingTwitterAlert() {
        optTwitterCheckbox.setChecked(true);
        optTwitterCheckbox.performClick();
        assertFalse(countrySpinner.isShown());
        assertFalse(serviceProviderSpinner.isShown());
    }
}
