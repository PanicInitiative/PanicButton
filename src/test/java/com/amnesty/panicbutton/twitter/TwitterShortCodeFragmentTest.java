package com.amnesty.panicbutton.twitter;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.TextView;
import com.amnesty.panicbutton.R;
import com.amnesty.panicbutton.common.TestFragmentActivity;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.*;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(RobolectricTestRunner.class)
public class TwitterShortCodeFragmentTest {
    public static final int COUNTRIES_COUNT = 99;
    public static final int INDIA_INDEX = 38;
    public static final int INDIA_SERVICE_PROVIDERS_COUNT = 4;

    private Spinner countrySpinner;
    private Spinner serviceProviderSpinner;
    private ViewGroup shortCodeLayout;
    private TextView shortCodeTextView;
    private TextView shortCodeHelpText;
    private TestFragmentActivity testFragmentActivity = new TestFragmentActivity();
    private TwitterShortCodeFragment twitterShortCodeFragment;

    @Before
    public void setup() {
        initMocks(this);
        twitterShortCodeFragment = createFragment(testFragmentActivity);

        countrySpinner = (Spinner) twitterShortCodeFragment.getView().findViewById(R.id.country_spinner);
        serviceProviderSpinner = (Spinner) twitterShortCodeFragment.getView().findViewById(R.id.service_provider_spinner);
        shortCodeLayout = (ViewGroup) twitterShortCodeFragment.getView().findViewById(R.id.twitter_short_code_layout);
        shortCodeTextView = (TextView) twitterShortCodeFragment.getView().findViewById(R.id.twitter_short_code);
        shortCodeHelpText = (TextView) twitterShortCodeFragment.getView().findViewById(R.id.twitter_help_text);
    }

    private TwitterShortCodeFragment createFragment(FragmentActivity activity) {
        TwitterShortCodeFragment twitterShortCodeFragment = new TwitterShortCodeFragment();
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(twitterShortCodeFragment, null);
        fragmentTransaction.commit();

        return twitterShortCodeFragment;
    }

    @Test
    public void shouldHaveSpinnersAndShortCodeHiddenOnCreate() {
        assertTrue(countrySpinner.isShown());
        assertTrue(serviceProviderSpinner.isShown());
        assertFalse(shortCodeLayout.isShown());
    }

    @Test
    public void shouldLoadCountrySpinnerOnCreate() {
        assertEquals(COUNTRIES_COUNT, countrySpinner.getCount());
        assertEquals("Select country", countrySpinner.getSelectedItem());
        assertFalse(shortCodeLayout.isShown());
    }

    @Test
    public void shouldShowServiceProvidersOnCountrySelectionWithHintAndOtherService() {
        countrySpinner.setSelection(INDIA_INDEX);

        assertEquals(INDIA_SERVICE_PROVIDERS_COUNT, serviceProviderSpinner.getCount());
        assertEquals("Select phone service", serviceProviderSpinner.getSelectedItem());
        assertEquals("Other phone service", serviceProviderSpinner.getItemAtPosition(INDIA_SERVICE_PROVIDERS_COUNT - 1));
        assertFalse(shortCodeLayout.isShown());
    }

    @Test
    public void shouldNotProcessAnythingOnCountryHintSelection() {
        countrySpinner.setSelection(COUNTRIES_COUNT);

        assertEquals(0, serviceProviderSpinner.getCount());
        assertFalse(shortCodeLayout.isShown());
    }

    @Test
    public void shouldShowShortCodeInfoOnServiceProviderSelection() {
        countrySpinner.setSelection(INDIA_INDEX);
        serviceProviderSpinner.setSelection(1);

        assertTrue(shortCodeLayout.isShown());
        assertEquals("53000", shortCodeTextView.getText().toString());
        assertEquals("To connect your phone to Twitter by SMS, send a text with the word START to:", shortCodeHelpText.getText().toString());
    }

    @Test
    public void shouldShowErrorTextOnOtherPhoneServiceSelection() {
        countrySpinner.setSelection(INDIA_INDEX);
        serviceProviderSpinner.setSelection(INDIA_SERVICE_PROVIDERS_COUNT - 1);

        assertTrue(shortCodeLayout.isShown());
        assertEquals("", shortCodeTextView.getText().toString());
        assertEquals("We are only able to connect with phone service providers supported by Twitter.", shortCodeHelpText.getText().toString());
    }

    @Test
    public void shouldNotProcessAnythingOnServiceProviderHintSelection() {
        countrySpinner.setSelection(INDIA_INDEX);
        serviceProviderSpinner.setSelection(INDIA_SERVICE_PROVIDERS_COUNT);

        assertFalse(shortCodeLayout.isShown());
    }

    @Test
    public void shouldDisplayTheGivenSettings() {
        ShortCodeSettings shortCodeSettings = new ShortCodeSettings("UK");
        shortCodeSettings.setServiceProvider("O2");
        shortCodeSettings.setShortCode("86444");

        twitterShortCodeFragment.displaySettings(shortCodeSettings);

        assertEquals("UK", countrySpinner.getSelectedItem());
        assertEquals("O2", serviceProviderSpinner.getSelectedItem());
        assertEquals("86444", shortCodeTextView.getText().toString());
    }
}
