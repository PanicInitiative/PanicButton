package com.amnesty.panicbutton.fragment;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import com.amnesty.panicbutton.R;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import roboguice.activity.RoboFragmentActivity;

import static org.junit.Assert.assertEquals;

@RunWith(RobolectricTestRunner.class)
public class SMSSettingsFragmentTest {
    private SMSSettingsFragment smsSettingsFragment;

    @Before
    public void setUp() {
        smsSettingsFragment = new SMSSettingsFragment();
        FragmentManager fragmentManager = new RoboFragmentActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(smsSettingsFragment, null);
        fragmentTransaction.commit();
    }

    @Test
    public void shouldSetTheFragmentLayoutOnCreate() {
        assertEquals(R.id.sms_settings_fragment_root, smsSettingsFragment.getView().getId());
    }
}
