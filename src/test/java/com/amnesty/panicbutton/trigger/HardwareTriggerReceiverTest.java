package com.amnesty.panicbutton.trigger;

import android.app.Application;
import android.content.Intent;
import com.amnesty.panicbutton.alert.PanicAlert;
import org.codehaus.plexus.util.ReflectionUtils;
import org.junit.Assert;
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
public class HardwareTriggerReceiverTest {
    private Application context;
    private HardwareTriggerReceiver spyHardwareTriggerReceiver;
    @Mock
    private MultiClickEvent mockMultiClickEvent;
    @Mock
    private PanicAlert mockPanicAlert;

    @Before
    public void setUp() throws IllegalAccessException {
        initMocks(this);
        context = Robolectric.application;
        spyHardwareTriggerReceiver = spy(new HardwareTriggerReceiver());
        when(spyHardwareTriggerReceiver.getPanicAlert(context)).thenReturn(mockPanicAlert);
        ReflectionUtils.setVariableValueInObject(spyHardwareTriggerReceiver, "multiClickEvent", mockMultiClickEvent);
    }

    @Test
    public void shouldActivateAlertWhenTheMultiClickEventIsActivatedAndResetTheEvent() throws IllegalAccessException {
        when(mockMultiClickEvent.isActivated()).thenReturn(true);
        spyHardwareTriggerReceiver.onReceive(context, new Intent(ACTION_SCREEN_OFF));

        verify(mockMultiClickEvent).registerClick(anyLong());
        verify(mockPanicAlert).activate();
        MultiClickEvent actualEvent = (MultiClickEvent) ReflectionUtils.getValueIncludingSuperclasses("multiClickEvent", spyHardwareTriggerReceiver);
        Assert.assertNotSame(mockMultiClickEvent, actualEvent);
    }

    @Test
    public void shouldNotActivateAlertWhenTheMultiClickEventIsNotActivated() {
        when(mockMultiClickEvent.isActivated()).thenReturn(false);
        spyHardwareTriggerReceiver.onReceive(context, new Intent(ACTION_SCREEN_ON));

        verify(mockMultiClickEvent).registerClick(anyLong());
        verify(mockPanicAlert, never()).activate();
    }

    @Test
    public void shouldNotProcessAnyThingForIntentsOtherThanScreenOnAndOff() {
        spyHardwareTriggerReceiver.onReceive(context, new Intent(ACTION_CAMERA_BUTTON));

        verifyNoMoreInteractions(mockMultiClickEvent);
        verify(mockPanicAlert, never()).activate();
    }
}
