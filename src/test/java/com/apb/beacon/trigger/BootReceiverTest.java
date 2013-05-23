package com.apb.beacon.trigger;

import android.content.Intent;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowIntent;

import static android.content.Intent.ACTION_BOOT_COMPLETED;
import static org.junit.Assert.*;
import static org.robolectric.Robolectric.shadowOf;

@RunWith(RobolectricTestRunner.class)
public class BootReceiverTest {
    private BootReceiver bootReceiver;
    private Intent bootIntent;

    @Before
    public void setUp() {
        bootReceiver = new BootReceiver();
        bootIntent = new Intent(ACTION_BOOT_COMPLETED);
    }

    @Test
    public void shouldStartHardwareTriggerServiceOnBoot() {
        bootReceiver.onReceive(Robolectric.application, bootIntent);

        Intent startedIntent = Robolectric.getShadowApplication().getNextStartedService();
        ShadowIntent shadowIntent = shadowOf(startedIntent);
        assertNotNull(startedIntent);
        assertEquals(HardwareTriggerService.class, shadowIntent.getIntentClass());
    }

    @Test
    public void shouldNotProcessOtherIntents() {
        bootReceiver.onReceive(Robolectric.application, new Intent(Intent.ACTION_SCREEN_ON));
        assertNull(Robolectric.getShadowApplication().getNextStartedService());
    }

}
