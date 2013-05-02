package com.amnesty.panicbutton.wizard;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.Button;
import com.amnesty.panicbutton.CalculatorActivity;
import com.amnesty.panicbutton.R;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowActivity;
import roboguice.activity.RoboFragmentActivity;

import static org.junit.Assert.assertEquals;
import static org.robolectric.Robolectric.shadowOf;

@RunWith(RobolectricTestRunner.class)
public class FinishWizardFragmentTest {
    private Button finishButton;
    private RoboFragmentActivity roboFragmentActivity;

    @Before
    public void setUp() {
        FinishWizardFragment finishWizardFragment = new FinishWizardFragment();
        roboFragmentActivity = new RoboFragmentActivity();
        FragmentManager fragmentManager = roboFragmentActivity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(finishWizardFragment, null);
        fragmentTransaction.commit();
        finishButton = (Button) finishWizardFragment.getView().findViewById(R.id.finish_wizard_button);
    }

    @Test
    public void shouldNavigateToFacadeOnClickingFinish() {
        finishButton.performClick();
        ShadowActivity shadowActivity = shadowOf(roboFragmentActivity);
        Intent startedIntent = shadowActivity.getNextStartedActivity();

        assertEquals(CalculatorActivity.class.getName(), startedIntent.getComponent().getClassName());
    }

    @Test
    public void shouldReturnEmptyAction() {
        WizardFragment fragment = new WizardFragment();
        assertEquals("", fragment.action());
    }
}
