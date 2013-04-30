package com.amnesty.panicbutton.wizard;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import com.amnesty.panicbutton.R;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.assertEquals;

@RunWith(RobolectricTestRunner.class)
public class SimpleFragmentTest {
    private SimpleFragment simpleFragment;

    @Before
    public void setUp() {
        simpleFragment = SimpleFragment.create(R.layout.wizard_start_screen, R.string.start_action);
        FragmentManager fragmentManager = new FragmentActivity().getSupportFragmentManager();
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
}
