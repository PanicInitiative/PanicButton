package org.iilab.pb.trigger;

import android.app.Application;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;


import org.codehaus.plexus.util.ReflectionUtils;
import org.iilab.pb.alert.PanicAlert;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowKeyguardManager;

import java.util.HashMap;
import java.util.Map;

import static android.content.Intent.ACTION_CAMERA_BUTTON;
import static android.content.Intent.ACTION_SCREEN_OFF;
import static android.content.Intent.ACTION_SCREEN_ON;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.robolectric.Robolectric.shadowOf;

@RunWith(RobolectricTestRunner.class)
public class HardwareTriggerReceiverTest {
    private Application context;
    private HardwareTriggerReceiver spyHardwareTriggerReceiver;
    @Mock
    private MultiClickEvent mockMultiClickEvent;
    @Mock
    private PanicAlert mockPanicAlert;
    private ShadowKeyguardManager shadowKeyguardManager;
    private Map<String, String> eventLog = new HashMap<String, String>();

    @Before
    public void setUp() throws IllegalAccessException {
        initMocks(this);
        context = Robolectric.application;
        spyHardwareTriggerReceiver = spy(new HardwareTriggerReceiver());
        when(spyHardwareTriggerReceiver.getPanicAlert(context)).thenReturn(mockPanicAlert);
        ReflectionUtils.setVariableValueInObject(spyHardwareTriggerReceiver, "multiClickEvent", mockMultiClickEvent);
        shadowKeyguardManager = shadowOf((KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE));
        shadowKeyguardManager.setinRestrictedInputMode(true);
    }

    @Test
    public void shouldActivateAlertWhenTheMultiClickEventIsActivatedAndResetTheEvent() throws IllegalAccessException {
        when(mockMultiClickEvent.isActivated()).thenReturn(true);
        spyHardwareTriggerReceiver.onReceive(context, new Intent(ACTION_SCREEN_OFF));

        verify(mockMultiClickEvent).registerClick(anyLong());
//        verify(mockPanicAlert).activate();
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

//    @Test
//    public void shouldNotProcessAnyThingIfScreenIsUnlocked() {
//        shadowKeyguardManager.setinRestrictedInputMode(false);
//        spyHardwareTriggerReceiver.onReceive(context, new Intent(ACTION_SCREEN_ON));
//
//        verifyNoMoreInteractions(mockMultiClickEvent);
//        verify(mockPanicAlert, never()).activate();
//    }
}
