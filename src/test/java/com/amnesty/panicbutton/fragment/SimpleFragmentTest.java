package com.amnesty.panicbutton.fragment;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import com.amnesty.panicbutton.R;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.assertEquals;

@RunWith(RobolectricTestRunner.class)
public class SimpleFragmentTest {
    @Test
    public void shouldCreateFragmentWithGivenLayout() {
        SimpleFragment simpleFragment = new SimpleFragment(R.layout.wizard_start_screen);

        FragmentManager fragmentManager = new FragmentActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(simpleFragment, null);
        fragmentTransaction.commit();

        assertEquals(R.id.wizard_start_root, simpleFragment.getView().getId());
    }
}
