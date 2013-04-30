package com.amnesty.panicbutton.wizard;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import com.amnesty.panicbutton.fragment.SMSSettingsFragment;

import java.util.ArrayList;
import java.util.List;

import static com.amnesty.panicbutton.R.layout.wizard_start_screen;
import static com.amnesty.panicbutton.R.string.start_action;
import static com.amnesty.panicbutton.fragment.SimpleFragment.create;

public class WizardPageAdapter extends FragmentStatePagerAdapter {
    private List<Fragment> fragments = new ArrayList<Fragment>();

    public WizardPageAdapter(FragmentManager fm) {
        super(fm);
        fragments.add(create(wizard_start_screen, start_action));
        fragments.add(new SMSSettingsFragment());
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public Fragment getItem(int i) {
        return fragments.get(i);
    }
}
