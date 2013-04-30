package com.amnesty.panicbutton.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import com.amnesty.panicbutton.R;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowLog;
import org.robolectric.tester.android.util.TestFragmentManager;
import org.robolectric.tester.android.util.TestFragmentTransaction;
import roboguice.activity.RoboFragmentActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.amnesty.panicbutton.R.id.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.robolectric.Robolectric.shadowOf;

@RunWith(RobolectricTestRunner.class)
public class SMSSettingsFragmentTest {
    private SMSSettingsFragment smsSettingsFragment;
    private FragmentManager fragmentManager;

    @Before
    public void setUp() {
        smsSettingsFragment = new SMSSettingsFragment();
        fragmentManager = new RoboFragmentActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(smsSettingsFragment, null);
        fragmentTransaction.commit();
    }

    @Test
    public void shouldSetTheFragmentLayoutOnCreate() {
        assertEquals(R.id.sms_settings_fragment_root, smsSettingsFragment.getView().getId());
    }

    @Test
    public void shouldReturnTheActionText() {
        assertEquals("Save", smsSettingsFragment.action());
    }

    @Test
    public void shouldRemoveFragmentsOnViewDestroy() {
        TestFragmentManager testFragmentManager = (TestFragmentManager) fragmentManager;
        smsSettingsFragment.onDestroyView();
        List<TestFragmentTransaction> committedTransactions = testFragmentManager.getCommittedTransactions();

        List<Integer> actualIds = new ArrayList<Integer>();
        List<Integer> expectedIds = Arrays.asList(first_contact, second_contact, third_contact, sms_message);

        for (TestFragmentTransaction committedTransaction : committedTransactions) {
            Fragment fragmentToRemove = committedTransaction.getFragmentToRemove();
            if (fragmentToRemove != null) {
                actualIds.add(shadowOf(fragmentToRemove).getContainerViewId());
            }
        }

        assertEquals(expectedIds, actualIds);
    }

    @Test
    public void shouldHandleDestroyViewException() {
        SMSSettingsFragment smsSettingsFragment = new SMSSettingsFragment() {
            @Override
            protected int[] getFragmentIds() {
                throw new RuntimeException("Test Exception");
            }
        };

        smsSettingsFragment.onDestroyView();
        List<ShadowLog.LogItem> logs = ShadowLog.getLogs();
        assertEquals(1, logs.size());
        ShadowLog.LogItem logItem = logs.get(0);
        assertTrue(logItem.msg.startsWith("Error on destroy view : "));
    }
}
