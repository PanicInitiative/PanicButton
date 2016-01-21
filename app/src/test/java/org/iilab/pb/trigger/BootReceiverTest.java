package org.iilab.pb.trigger;

import android.content.Intent;

import org.iilab.pb.BuildConfig;
import org.iilab.pb.common.AppConstants;
import org.iilab.pb.common.ApplicationSettings;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowApplication;
import org.robolectric.shadows.ShadowIntent;

import static android.content.Intent.ACTION_BOOT_COMPLETED;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk=21)
public class BootReceiverTest {
    private BootReceiver bootReceiver;
    private Intent bootIntent;
    private int wizardState;

    @Before
    public void setUp() {
        bootReceiver = new BootReceiver();
        bootIntent = new Intent(ACTION_BOOT_COMPLETED);
        ApplicationSettings.setWizardState(RuntimeEnvironment.application, AppConstants.WIZARD_FLAG_HOME_READY);
    }

    @Test
    public void shouldStartHardwareTriggerServiceOnBootWhenHomeReady() {
        bootReceiver.onReceive(RuntimeEnvironment.application, bootIntent);

        Intent startedIntent = ShadowApplication.getInstance().getNextStartedService();
        ShadowIntent shadowIntent = Shadows.shadowOf(startedIntent);
        assertNotNull(startedIntent);
        assertEquals(HardwareTriggerService.class, shadowIntent.getIntentClass());
    }

    @Test
    public void shouldNotProcessOtherIntents() {
        bootReceiver.onReceive(RuntimeEnvironment.application, new Intent(Intent.ACTION_SCREEN_ON));
        assertNull(ShadowApplication.getInstance().getNextStartedService());
    }

}
