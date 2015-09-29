package org.iilab.pb.trigger;

import android.content.Intent;
import android.content.IntentFilter;

import org.iilab.pb.BuildConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowApplication;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk=21)
public class HardwareTriggerServiceTest {
    @Test
    public void shouldRegisterHardwareTriggerListener() {
        HardwareTriggerService hardwareTriggerService = new HardwareTriggerService();
        hardwareTriggerService.onCreate();
        hardwareTriggerService.onBind(null);

        ShadowApplication shadowApplication = Shadows.shadowOf(RuntimeEnvironment.application);
        List<ShadowApplication.Wrapper> registeredReceivers = shadowApplication.getRegisteredReceivers();

        ShadowApplication.Wrapper hardwareReceiverWrapper = null;
        for (ShadowApplication.Wrapper registeredReceiver : registeredReceivers) {
            if (registeredReceiver.getBroadcastReceiver().getClass().equals(HardwareTriggerReceiver.class)) {
                hardwareReceiverWrapper = registeredReceiver;
            }
        }

        assertNotNull(hardwareReceiverWrapper);

        IntentFilter intentFilter = hardwareReceiverWrapper.getIntentFilter();
        assertEquals(Intent.ACTION_SCREEN_ON, intentFilter.getAction(0));
        assertEquals(Intent.ACTION_SCREEN_OFF, intentFilter.getAction(1));
    }
}
