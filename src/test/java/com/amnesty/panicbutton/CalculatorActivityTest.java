package com.amnesty.panicbutton;

import android.content.Intent;
import android.widget.Button;
import com.amnesty.panicbutton.alert.PanicAlert;
import com.amnesty.panicbutton.trigger.HardwareTriggerService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowActivity;
import org.robolectric.shadows.ShadowIntent;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.robolectric.Robolectric.shadowOf;

@RunWith(RobolectricTestRunner.class)
public class CalculatorActivityTest {
    private CalculatorActivity calculatorActivity;
    private Button equalsButton;
    private ShadowActivity shadowActivity;
    @Mock
    private PanicAlert mockPanicAlert;

    @Before
    public void setUp() {
        initMocks(this);
        calculatorActivity = new CalculatorActivity() {
            @Override
            PanicAlert getPanicAlert() {
                return mockPanicAlert;
            }
        };
        calculatorActivity.onCreate(null);
        equalsButton = (Button) calculatorActivity.findViewById(R.id.equals_sign);
        shadowActivity = shadowOf(calculatorActivity);
    }

    @Test
    public void shouldNavigateToSettingsScreenOnEqualSignLongPress() {
        equalsButton.performLongClick();

        Intent startedIntent = shadowActivity.getNextStartedActivity();
        assertThat(startedIntent.getComponent().getClassName(), equalTo(SettingsActivity.class.getName()));
    }

    @Test
    public void shouldActivateAlertOnPressingEqualSignRepeatedlyFiveTimes() {
        for (int i = 0; i < 5; i++) {
            equalsButton.performClick();
        }
        verify(mockPanicAlert).start();
    }

    @Test
    public void shouldNotActivateAlertOnPressingEqualSignRepeatedlyLesserThanFiveTimes() {
        for (int i = 0; i < 3; i++) {
            equalsButton.performClick();
        }
        verifyNoMoreInteractions(mockPanicAlert);
    }

    @Test
    public void shouldStartHardwareTriggerServiceOnApplicationCreation() {
        Intent startedIntent = shadowActivity.getNextStartedService();
        ShadowIntent shadowIntent = shadowOf(startedIntent);
        assertNotNull(startedIntent);
        assertEquals(HardwareTriggerService.class, shadowIntent.getIntentClass());
    }

    @Test
    public void shouldCreateNewPanicAlert() {
        assertNotNull(new CalculatorActivity().getPanicAlert());
    }
}
