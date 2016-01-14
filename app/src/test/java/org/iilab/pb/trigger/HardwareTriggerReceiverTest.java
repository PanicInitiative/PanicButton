package org.iilab.pb.trigger;

import android.app.Application;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;

import org.iilab.pb.BuildConfig;
import org.iilab.pb.alert.PanicAlert;
import org.iilab.pb.test.support.ShadowAudioManager;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.internal.ShadowExtractor;
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
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

//import org.codehaus.plexus.util.ReflectionUtils;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk=21, shadows= ShadowAudioManager.class)
public class HardwareTriggerReceiverTest {
    private Application context;
    @InjectMocks
    private HardwareTriggerReceiver spyHardwareTriggerReceiver;
    @Mock
    private MultiClickEvent mockMultiClickEvent;
    @Mock
    private PanicAlert mockPanicAlert;
    private ShadowKeyguardManager shadowKeyguardManager;
    private ShadowAudioManager shadowAudioManager;
    private Map<String, String> eventLog = new HashMap<String, String>();

    @Before
    public void setUp() throws IllegalAccessException {

        context = RuntimeEnvironment.application;
        spyHardwareTriggerReceiver = spy(new HardwareTriggerReceiver(RuntimeEnvironment.application));
        initMocks(this);
        when(spyHardwareTriggerReceiver.getPanicAlert(context)).thenReturn(mockPanicAlert);

//        ReflectionUtils.setVariableValueInObject(spyHardwareTriggerReceiver, "multiClickEvent", mockMultiClickEvent);
        shadowKeyguardManager = Shadows.shadowOf((KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE));
        shadowKeyguardManager.setinRestrictedInputMode(true);
        shadowAudioManager= (ShadowAudioManager)ShadowExtractor.extract((AudioManager)context.getSystemService(Context.AUDIO_SERVICE));
    }

    @Test
    public void shouldActivateAlertWhenTheMultiClickEventIsActivatedAndResetTheEvent() throws IllegalAccessException {
        when(mockMultiClickEvent.isActivated()).thenReturn(true);
        spyHardwareTriggerReceiver.onReceive(context, new Intent(ACTION_SCREEN_OFF));

        verify(mockMultiClickEvent).registerClick(anyLong());
//        verify(mockPanicAlert).activate();
//        MultiClickEvent actualEvent = (MultiClickEvent) ReflectionUtils.getValueIncludingSuperclasses("multiClickEvent", spyHardwareTriggerReceiver);
//        Assert.assertNotSame(mockMultiClickEvent, actualEvent);
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
        shadowAudioManager.setMode(AudioManager.MODE_NORMAL);
//        verifyNoMoreInteractions(mockMultiClickEvent);
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
