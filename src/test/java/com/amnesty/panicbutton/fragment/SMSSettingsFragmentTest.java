package com.amnesty.panicbutton.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.EditText;
import com.amnesty.panicbutton.R;
import com.amnesty.panicbutton.model.SMSSettings;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowHandler;
import org.robolectric.shadows.ShadowLog;
import org.robolectric.shadows.ShadowToast;
import org.robolectric.tester.android.util.TestFragmentManager;
import org.robolectric.tester.android.util.TestFragmentTransaction;
import roboguice.activity.RoboFragmentActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.amnesty.panicbutton.R.id.*;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.*;
import static org.robolectric.Robolectric.shadowOf;

@RunWith(RobolectricTestRunner.class)
public class SMSSettingsFragmentTest {
    private SMSSettingsFragment smsSettingsFragment;
    private FragmentManager fragmentManager;

    private EditText smsEditText;
    private EditText firstContactEditText;
    private EditText secondContactEditText;
    private EditText thirdContactEditText;

    private String alreadySavedMessage;
    private String number1;
    private String number2;
    private String number3;
    private String expectedMessage;

    @Before
    public void setUp() {
        setupExistingSettings();
        smsSettingsFragment = new SMSSettingsFragment();
        fragmentManager = new RoboFragmentActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(smsSettingsFragment, null);
        fragmentTransaction.commit();


        Fragment firstContact = fragmentManager.findFragmentById(R.id.first_contact);
        Fragment secondContact = fragmentManager.findFragmentById(R.id.second_contact);
        Fragment thirdContact = fragmentManager.findFragmentById(R.id.third_contact);
        Fragment smsFragment = fragmentManager.findFragmentById(R.id.sms_message);

        smsEditText = (EditText) smsFragment.getView().findViewById(R.id.message_edit_text);
        firstContactEditText = (EditText) firstContact.getView().findViewById(R.id.contact_edit_text);
        secondContactEditText = (EditText) secondContact.getView().findViewById(R.id.contact_edit_text);
        thirdContactEditText = (EditText) thirdContact.getView().findViewById(R.id.contact_edit_text);

        number1 = "123-456-789";
        number2 = "9874641321";
        number3 = "4564523423";
        expectedMessage = "Help! I am in trouble";
    }

    private void setupExistingSettings() {
        alreadySavedMessage = "Already saved message";
        List<String> alreadySavedPhoneNumbers = Arrays.asList("123245697", "345665-5656", "45234234345");
        SMSSettings smsSettings = new SMSSettings(alreadySavedPhoneNumbers, alreadySavedMessage);
        SMSSettings.save(Robolectric.application, smsSettings);
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

    @Test
    public void shouldLoadExistingSettings() {
        assertEquals(alreadySavedMessage, smsEditText.getText().toString());
        assertEquals("*******97", firstContactEditText.getText().toString());
        assertEquals("*********56", secondContactEditText.getText().toString());
        assertEquals("*********45", thirdContactEditText.getText().toString());
    }

    @Test
    public void shouldSaveSMSSettingsAndMaskPhoneNumbers() throws Exception {
        setViewData();
        smsSettingsFragment.performAction();
        assertSavedSMSSettings();
    }

    @Test
    public void shouldNotUpdateMaskedPhoneNumbers() {
        setViewData();
        smsSettingsFragment.performAction();
        smsSettingsFragment.performAction();
        assertSavedSMSSettings();
    }

    private void setViewData() {
        smsEditText.setText(expectedMessage);
        firstContactEditText.setText(number1);
        secondContactEditText.setText(number2);
        thirdContactEditText.setText(number3);
    }

    private void assertSavedSMSSettings() {
        ShadowHandler.idleMainLooper();
        assertThat(ShadowToast.getTextOfLatestToast(), equalTo("SMS settings saved successfully"));

        SMSSettings retrievedSMSSettings = SMSSettings.retrieve(Robolectric.application);

        assertEquals(expectedMessage, retrievedSMSSettings.message());
        assertEquals(number1, retrievedSMSSettings.phoneNumberAt(0));
        assertEquals(number2, retrievedSMSSettings.phoneNumberAt(1));
        assertEquals(number3, retrievedSMSSettings.phoneNumberAt(2));

        assertEquals("*********89", firstContactEditText.getText().toString());
        assertEquals("********21", secondContactEditText.getText().toString());
        assertEquals("********23", thirdContactEditText.getText().toString());
    }
}
