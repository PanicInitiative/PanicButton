package com.amnesty.panicbutton.trigger;

import android.app.Application;
import android.content.Intent;
import com.amnesty.panicbutton.MessageAlerter;
import org.codehaus.plexus.util.ReflectionUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import static android.content.Intent.ACTION_SCREEN_OFF;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(RobolectricTestRunner.class)
public class HardwareTriggerReceiverTest {
    private Application context;
    private HardwareTriggerReceiver hardwareTriggerReceiver;
    @Mock
    private MultiClickEvent mockMultiClickEvent;

    @Before
    public void setUp() throws IllegalAccessException {
        initMocks(this);
        hardwareTriggerReceiver = spy(new HardwareTriggerReceiver());
        ReflectionUtils.setVariableValueInObject(hardwareTriggerReceiver, "multiClickEvent", mockMultiClickEvent);
        context = Robolectric.application;
    }

    @Test
    public void shouldActivateAlertWhenTheMultiClickEventIsActivatedAndResetTheEvent() throws IllegalAccessException {
        when(mockMultiClickEvent.isActivated()).thenReturn(true);
        hardwareTriggerReceiver.onReceive(context, new Intent(ACTION_SCREEN_OFF));

        verify(mockMultiClickEvent).registerClick();
        verify(hardwareTriggerReceiver).activateAlert(any(MessageAlerter.class));
        MultiClickEvent actualEvent = (MultiClickEvent) ReflectionUtils.getValueIncludingSuperclasses("multiClickEvent", hardwareTriggerReceiver);
        Assert.assertNotSame(mockMultiClickEvent, actualEvent);
    }

    @Test
    public void shouldNotActivateAlertWhenTheMultiClickEventIsNotActivated() {
        when(mockMultiClickEvent.isActivated()).thenReturn(false);
        hardwareTriggerReceiver.onReceive(context, new Intent(ACTION_SCREEN_OFF));

        verify(mockMultiClickEvent).registerClick();
        verify(hardwareTriggerReceiver, never()).activateAlert(any(MessageAlerter.class));
    }
}
