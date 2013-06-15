package com.apb.beacon.wizard;

import android.app.Application;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.EditText;
import com.apb.beacon.R;
import com.apb.beacon.common.TestFragmentActivity;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowPreferenceManager;

import static org.junit.Assert.*;

@RunWith(RobolectricTestRunner.class)
public class CreatePinFragmentTest {
    private CreatePinFragment createPinFragment;
    private EditText pinInput;
    private SharedPreferences sharedPreferences;
    private Application context;
    private TestFragmentActivity testActivity;

    @Before
    public void setUp() {
        createPinFragment = new CreatePinFragment();
        testActivity = new TestFragmentActivity();
        FragmentManager fragmentManager = testActivity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(createPinFragment, null);
        fragmentTransaction.commit();

        pinInput = (EditText) createPinFragment.getView().findViewById(R.id.create_pin_edittext);
        context = Robolectric.application;
        sharedPreferences = ShadowPreferenceManager.getDefaultSharedPreferences(context);
    }

    @Test
    public void shouldReturnTheActionText() {
        assertEquals("Save", createPinFragment.action());
    }

    @Test
    public void shouldSaveThePin() {
        String expectedPin = "1234";
        pinInput.setText(expectedPin);

        assertTrue(createPinFragment.performAction());
        assertEquals(expectedPin, sharedPreferences.getString("PASS_CODE", null));
    }

    @Test
    public void shouldNotifyActivityToDisableActionButtonOnCreate() {
        pinInput.setText("");

        createPinFragment.onFragmentSelected();
        assertFalse(testActivity.isActionButtonEnabled());
    }

    @Test
    public void shouldNotifyActivityToEnableActionButtonWhenPinIsAlreadySet() {
        pinInput.setText("1234");

        createPinFragment.onFragmentSelected();
        assertTrue(testActivity.isActionButtonEnabled());
    }

    @Test
    public void shouldResetPinErrorOnNavigatingBack() {
        pinInput.setError("Test Error");

        createPinFragment.onBackPressed();
        assertNull(pinInput.getError());
    }
}
