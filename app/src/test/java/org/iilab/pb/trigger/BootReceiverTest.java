package org.iilab.pb.trigger;

import android.content.Intent;


import org.iilab.pb.common.AppConstants;
import org.iilab.pb.common.ApplicationSettings;
import org.iilab.pb.trigger.BootReceiver;
import org.iilab.pb.trigger.HardwareTriggerService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowIntent;

import static android.content.Intent.ACTION_BOOT_COMPLETED;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.robolectric.Robolectric.shadowOf;

@RunWith(RobolectricTestRunner.class)
public class BootReceiverTest {
    private BootReceiver bootReceiver;
    private Intent bootIntent;
    private int wizardState;

    @Before
    public void setUp() {
        bootReceiver = new BootReceiver();
        bootIntent = new Intent(ACTION_BOOT_COMPLETED);
        ApplicationSettings.setWizardState(Robolectric.application, AppConstants.WIZARD_FLAG_HOME_READY);
    }

    @Test
    public void shouldStartHardwareTriggerServiceOnBootWhenHomeReady() {
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
