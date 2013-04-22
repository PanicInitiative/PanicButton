package com.amnesty.panicbutton.trigger;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowApplication;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.robolectric.Robolectric.shadowOf;

@RunWith(RobolectricTestRunner.class)
public class HardwareTriggerServiceTest {
    @Test
    public void shouldRegisterHardwareTriggerListener() {
        HardwareTriggerService hardwareTriggerService = new HardwareTriggerService();
        hardwareTriggerService.onCreate();
        hardwareTriggerService.onBind(null);

        ShadowApplication shadowApplication = shadowOf(Robolectric.application);
        List<ShadowApplication.Wrapper> registeredReceivers = shadowApplication.getRegisteredReceivers();

        assertEquals(1, registeredReceivers.size());

        ShadowApplication.Wrapper wrapper = registeredReceivers.get(0);
        BroadcastReceiver broadcastReceiver = wrapper.getBroadcastReceiver();
        assertEquals(HardwareTriggerReceiver.class.getName(), broadcastReceiver.getClass().getName());

        IntentFilter intentFilter = wrapper.getIntentFilter();
        assertEquals(Intent.ACTION_SCREEN_ON, intentFilter.getAction(0));
        assertEquals(Intent.ACTION_SCREEN_OFF, intentFilter.getAction(1));
    }
}
