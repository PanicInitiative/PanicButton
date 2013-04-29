package com.amnesty.panicbutton.wizard;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import com.amnesty.panicbutton.fragment.SMSSettingsFragment;
import roboguice.fragment.RoboFragment;

import java.util.ArrayList;
import java.util.List;

public class WizardPageAdapter extends FragmentStatePagerAdapter {

    private List<RoboFragment> fragments = new ArrayList<RoboFragment>();

    public WizardPageAdapter(FragmentManager fm) {
        super(fm);
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
