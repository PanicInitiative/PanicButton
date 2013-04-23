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

import static android.content.Intent.ACTION_SCREEN_OFF;
import static android.content.Intent.ACTION_SCREEN_ON;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(RobolectricTestRunner.class)
public class HardwareTriggerReceiverTest {
    private Application context;
    private HardwareTriggerReceiver hardwareTriggerReceiver;
    @Mock
    private Triggers mockTriggers;
    @Mock
    private MessageAlerter mockMessageAlerter;

    @Before
    public void setUp() {
        initMocks(this);
        hardwareTriggerReceiver = new HardwareTriggerReceiver(mockMessageAlerter, mockTriggers);
        context = Robolectric.application;
    }

    @Test
    public void shouldActivateAlertWhenTriggersThresholdIsReached() {
        when(mockTriggers.isActive()).thenReturn(true);
        hardwareTriggerReceiver.onReceive(context, new Intent(ACTION_SCREEN_OFF));
        verify(mockMessageAlerter).start();
    }

    @Test
    public void shouldNotActivateAlertWhenTriggersThresholdIsNotReached() {
        when(mockTriggers.isActive()).thenReturn(false);
        hardwareTriggerReceiver.onReceive(context, new Intent(ACTION_SCREEN_ON));
        verifyNoMoreInteractions(mockMessageAlerter);
    }
}
