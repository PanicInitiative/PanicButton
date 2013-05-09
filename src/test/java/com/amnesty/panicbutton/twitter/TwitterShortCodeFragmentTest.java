package com.amnesty.panicbutton.twitter;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.TextView;
import com.amnesty.panicbutton.R;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import roboguice.activity.RoboFragmentActivity;

import static org.junit.Assert.*;

@RunWith(RobolectricTestRunner.class)
public class TwitterShortCodeFragmentTest {
    private TwitterShortCodeFragment twitterShortCodeFragment;

    private Spinner countrySpinner;
    private Spinner serviceProviderSpinner;
    private ViewGroup shortCodeLayout;
    private TextView shortCodeTextView;

    @Before
    public void setup() {
        twitterShortCodeFragment = new TwitterShortCodeFragment();
        FragmentManager fragmentManager = new RoboFragmentActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(twitterShortCodeFragment, null);
        fragmentTransaction.commit();

        countrySpinner = (Spinner) twitterShortCodeFragment.getView().findViewById(R.id.country_spinner);
        serviceProviderSpinner = (Spinner) twitterShortCodeFragment.getView().findViewById(R.id.service_provider_spinner);
        shortCodeLayout = (ViewGroup) twitterShortCodeFragment.getView().findViewById(R.id.twitter_short_code_layout);
        shortCodeTextView = (TextView) twitterShortCodeFragment.getView().findViewById(R.id.twitter_short_code);
    }

    @Test
    public void shouldHaveSpinnersAndShortCodeHiddenOnCreate() {
        assertTrue(countrySpinner.isShown());
        assertTrue(serviceProviderSpinner.isShown());
        assertFalse(shortCodeLayout.isShown());
    }

    @Test
    public void shouldLoadCountrySpinnerOnCreate() {
        assertEquals(99, countrySpinner.getCount());
        assertFalse(shortCodeLayout.isShown());
    }

    @Test
    public void shouldShowServiceProvidersOnCountrySelection() {
        countrySpinner.setSelection(38);
        assertEquals(3, serviceProviderSpinner.getCount());
        assertFalse(shortCodeLayout.isShown());
    }

    @Test
    public void shouldShowShortCodeInfoOnServiceProviderSelection() {
        countrySpinner.setSelection(38);
        serviceProviderSpinner.setSelection(1);

        assertTrue(shortCodeLayout.isShown());
        assertEquals("53000", shortCodeTextView.getText().toString());
    }
}
