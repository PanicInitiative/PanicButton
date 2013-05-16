package com.amnesty.panicbutton.wizard;

import android.app.Application;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.EditText;
import com.amnesty.panicbutton.R;
import com.amnesty.panicbutton.common.TestFragmentActivity;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowPreferenceManager;

import static org.junit.Assert.*;

@RunWith(RobolectricTestRunner.class)
public class CreatePasswordFragmentTest {
    private CreatePasswordFragment createPasswordFragment;
    private EditText passwordEditText;
    private SharedPreferences sharedPreferences;
    private Application context;
    private TestFragmentActivity testActivity;

    @Before
    public void setUp() {
        createPasswordFragment = new CreatePasswordFragment();
        testActivity = new TestFragmentActivity();
        FragmentManager fragmentManager = testActivity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(createPasswordFragment, null);
        fragmentTransaction.commit();

        passwordEditText = (EditText) createPasswordFragment.getView().findViewById(R.id.create_password_edittext);
        context = Robolectric.application;
        sharedPreferences = ShadowPreferenceManager.getDefaultSharedPreferences(context);
    }

    @Test
    public void shouldReturnTheActionText() {
        assertEquals("Save", createPasswordFragment.action());
    }

    @Test
    public void shouldSaveThePassword() {
        String expectedPassword = "Abc 1234";
        passwordEditText.setText(expectedPassword);

        assertTrue(createPasswordFragment.performAction());
        assertEquals(expectedPassword, sharedPreferences.getString("PASS_CODE", null));
    }

    @Test
    public void shouldShowErrorMessageOnInvalidPassword() {
        String invalidPassword = "abc123";
        passwordEditText.setText(invalidPassword);

        assertFalse(createPasswordFragment.performAction());
        String expectedError = "Password must contain at least one lowercase, one uppercase, one number, and one space";
        assertEquals(expectedError, passwordEditText.getError());
    }

    @Test
    public void shouldNotifyActivityToDisableActionButtonOnCreate() {
        passwordEditText.setText("");

        createPasswordFragment.onFragmentSelected();
        assertFalse(testActivity.isActionButtonEnabled());
    }

    @Test
    public void shouldNotifyActivityToEnableActionButtonWhenPasswordIsAlreadySet() {
        passwordEditText.setText("Abc 1234");

        createPasswordFragment.onFragmentSelected();
        assertTrue(testActivity.isActionButtonEnabled());
    }

    @Test
    public void shouldResetPasswordErrorOnNavigatingBack() {
        passwordEditText.setError("Test Error");

        createPasswordFragment.onBackPressed();
        assertNull(passwordEditText.getError());
    }
}
