package com.apb.beacon;

import android.content.Intent;
import android.widget.Button;
import com.apb.beacon.alert.PanicAlert;
import com.apb.beacon.trigger.HardwareTriggerService;
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
	private Button equalsButton, zeroButton;
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
		zeroButton = (Button) calculatorActivity.findViewById(R.id.zero);
		shadowActivity = shadowOf(calculatorActivity);
	}

	@Test
	public void shouldNavigateToLoginScreenOnButtonLongPress() {
		equalsButton.performLongClick();

		Intent startedIntent = shadowActivity.getNextStartedActivity();
		assertThat(startedIntent.getComponent().getClassName(), equalTo(LoginActivity.class.getName()));
	}

	@Test
	public void shouldActivateAlertOnPressingButtonRepeatedlyFiveTimes() {
		for (int i = 0; i < 5; i++) {
			equalsButton.performClick();
		}
		verify(mockPanicAlert).activate();
		assertTrue(shadowActivity.isFinishing());
	}

	@Test
	public void shouldActivateAlertOnPressingButtonRepeatedlyFiveTimesAfterOtherButtons() {
		zeroButton.performClick();
		for (int i = 0; i < 5; i++) {
			equalsButton.performClick();
		}
		verify(mockPanicAlert).activate();
		assertTrue(shadowActivity.isFinishing());
	}

	@Test
	public void shouldNotActivateAlertOnPressingButtonRepeatedlyLessThanFiveTimes() {
		for (int i = 0; i < 4; i++) {
			equalsButton.performClick();
		}
		verifyNoMoreInteractions(mockPanicAlert);
	}

	@Test
	public void shouldNotActivateAlertOnPressingButtonRepeatedlyLessThanFiveTimesAfterOtherButtons() {
		zeroButton.performClick();
		for (int i = 0; i < 4; i++) {
			equalsButton.performClick();
		}
		verifyNoMoreInteractions(mockPanicAlert);
	}

	@Test
	public void shouldNotActivateAlertOnPressingButtonRepeatedlyMixedWithOtherButtons() {
		for (int i = 0; i < 5; i++) {
			equalsButton.performClick();
			zeroButton.performClick();
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
