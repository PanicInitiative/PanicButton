package com.amnesty.panicbutton.wizard;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import com.amnesty.panicbutton.R;
import com.amnesty.panicbutton.common.TestFragmentActivity;
import org.codehaus.plexus.util.ReflectionUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import roboguice.activity.RoboFragmentActivity;

import static org.junit.Assert.*;

@RunWith(RobolectricTestRunner.class)
public class SimpleFragmentTest {
    private SimpleFragment simpleFragment;
    private TestFragmentActivity testFragmentActivity;

    @Before
    public void setUp() {
        testFragmentActivity = new TestFragmentActivity();
        simpleFragment = SimpleFragment.create(R.layout.wizard_start_screen, R.string.start_action);
        FragmentManager fragmentManager = testFragmentActivity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(simpleFragment, null);
        fragmentTransaction.commit();
    }

    @Test
    public void shouldCreateFragmentWithGivenLayout() {
        assertEquals(R.id.wizard_start_root, simpleFragment.getView().getId());
    }

    @Test
    public void shouldGetActionFromBundle() {
        assertEquals("Start", simpleFragment.action());
    }

    @Test
    public void shouldAlwaysEnableActionForSimpleFragments(){
        simpleFragment.onFragmentSelected();
        assertTrue(testFragmentActivity.getActionButtonState());
    }

    @Test
    public void shouldNotRegisterListenerWhenActivityIsNotOfTypeActionButtonStateListener() throws IllegalAccessException {
        RoboFragmentActivity activity = new RoboFragmentActivity();
        SimpleFragment fragment = SimpleFragment.create(R.layout.wizard_start_screen, R.string.start_action);
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(fragment, null);
        fragmentTransaction.commit();

        fragment.onFragmentSelected();
        assertNull(ReflectionUtils.getValueIncludingSuperclasses("actionButtonStateListener", fragment));
    }
}
