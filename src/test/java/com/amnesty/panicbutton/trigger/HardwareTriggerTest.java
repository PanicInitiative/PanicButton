package com.amnesty.panicbutton.trigger;

import android.app.Application;
import android.content.Intent;
import com.amnesty.panicbutton.MessageAlerter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import static android.content.Intent.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(RobolectricTestRunner.class)
public class HardwareTriggerTest {
    private Application context;
    private HardwareTrigger hardwareTrigger;
    @Mock
    private Triggers mockTriggers;
    @Mock
    private MessageAlerter mockMessageAlerter;

    @Before
    public void setUp() {
        initMocks(this);
        hardwareTrigger = new HardwareTrigger(mockMessageAlerter, mockTriggers);
        context = Robolectric.application;
    }

    @Test
    public void shouldActivateAlertForScreenOnIntentAndWhenTriggersThresholdIsReached() {
        when(mockTriggers.isActive()).thenReturn(true);
        hardwareTrigger.onReceive(context, new Intent(ACTION_SCREEN_ON));
        verify(mockMessageAlerter).start();
    }

    @Test
    public void shouldActivateAlertForScreenOffIntentAndWhenTriggersThresholdIsReached() {
        when(mockTriggers.isActive()).thenReturn(true);
        hardwareTrigger.onReceive(context, new Intent(ACTION_SCREEN_OFF));
        verify(mockMessageAlerter).start();
    }

    @Test
    public void shouldNotActivateAlertWhenTriggersThresholdIsNotReached() {
        when(mockTriggers.isActive()).thenReturn(false);
        hardwareTrigger.onReceive(context, new Intent(ACTION_SCREEN_ON));
        verifyNoMoreInteractions(mockMessageAlerter);
    }

    @Test
    public void shouldNotActivateAlertForOtherIntents() {
        hardwareTrigger.onReceive(context, new Intent(ACTION_CAMERA_BUTTON));
        verifyNoMoreInteractions(mockMessageAlerter);
    }
}
