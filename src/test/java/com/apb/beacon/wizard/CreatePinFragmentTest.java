package com.apb.beacon.wizard;

import android.app.Application;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.EditText;

import com.apb.beacon.AppConstants;
import com.apb.beacon.ApplicationSettings;
import com.apb.beacon.R;
import com.apb.beacon.common.TestFragmentActivity;
import com.apb.beacon.data.PBDatabase;
import com.apb.beacon.model.LocalCachePage;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowPreferenceManager;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
public class CreatePinFragmentTest {
    private SetupCodeFragment createPinFragment;
    private EditText pinInput;
    private SharedPreferences sharedPreferences;
    private static Application context;
    private TestFragmentActivity testActivity;

    @Before
    public void setUp() {
        createPinFragment = new SetupCodeFragment();
        testActivity = new TestFragmentActivity();
        context = Robolectric.application;
        FragmentManager fragmentManager = testActivity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(createPinFragment, null);
        fragmentTransaction.commit();

        pinInput = (EditText) createPinFragment.getView().findViewById(R.id.create_pin_edittext);

        sharedPreferences = ShadowPreferenceManager.getDefaultSharedPreferences(context);
    }

    @Test
    public void shouldReturnTheActionText() {

        LocalCachePage page = new LocalCachePage(AppConstants.PAGE_NUMBER_PANIC_BUTTON_TRAINING_PIN, "Wizard Training Pin", "Step 1: Set a pin",
                "Next", "", "page contents");
        PBDatabase dbInstance = new PBDatabase(context);
        dbInstance.open();
        dbInstance.insertOrUpdateLocalCachePage(page);

        page = dbInstance.retrievePage(AppConstants.PAGE_NUMBER_PANIC_BUTTON_TRAINING_PIN);
        dbInstance.close();

        assertEquals("Next", page.getPageAction());
    }

    @Test
    public void shouldSaveThePin() {
        String expectedPin = "1234";
        pinInput.setText(expectedPin);
        ApplicationSettings.savePassword(context, pinInput.getText().toString());

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
