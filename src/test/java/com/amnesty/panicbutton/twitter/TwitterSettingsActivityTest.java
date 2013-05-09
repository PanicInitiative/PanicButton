package com.amnesty.panicbutton.twitter;

import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;
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
    private Spinner serviceProviderSpinner;
    private ViewGroup shortCodeLayout;
    private TextView shortCodeTextView;

    @Before
    public void setup() {
        twitterSettingsActivity = new TwitterSettingsActivity();
        twitterSettingsActivity.onCreate(null);

        optTwitterCheckbox = (CheckBox) twitterSettingsActivity.findViewById(R.id.opt_twitter_checkbox);
        countrySpinner = (Spinner) twitterSettingsActivity.findViewById(R.id.country_spinner);
        serviceProviderSpinner = (Spinner) twitterSettingsActivity.findViewById(R.id.service_provider_spinner);
        shortCodeLayout = (ViewGroup) twitterSettingsActivity.findViewById(R.id.twitter_short_code_layout);
        shortCodeTextView = (TextView) twitterSettingsActivity.findViewById(R.id.twitter_short_code);
    }

    @Test
    public void shouldLoadTwitterLayoutOnCreate() {
        assertEquals(R.id.twitter_settings_layout_root, shadowOf(twitterSettingsActivity).getContentView().getId());
        assertFalse(countrySpinner.isShown());
        assertFalse(serviceProviderSpinner.isShown());
        assertFalse(shortCodeLayout.isShown());
    }

    @Test
    public void shouldLoadCountrySpinnerOnCreate() {
        assertEquals(99, countrySpinner.getCount());
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

    @Test
    public void shouldShowServiceProvidersOnCountrySelection() {
        optTwitterCheckbox.performClick();
        countrySpinner.setSelection(38);
        assertEquals(3, serviceProviderSpinner.getCount());
        assertFalse(shortCodeLayout.isShown());
    }

    @Test
    public void shouldShowShortCodeInfoOnServiceProviderSelection() {
        optTwitterCheckbox.performClick();
        countrySpinner.setSelection(38);
        serviceProviderSpinner.setSelection(1);

        assertTrue(shortCodeLayout.isShown());
        assertEquals("53000", shortCodeTextView.getText().toString());
    }
}

