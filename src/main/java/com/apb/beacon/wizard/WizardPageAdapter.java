package com.apb.beacon.wizard;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.apb.beacon.sms.SMSContactNumberFragment;
import com.apb.beacon.sms.SMSMessageFragment;

import java.util.ArrayList;
import java.util.List;

import static com.apb.beacon.R.layout.wizard_emergency_alert1;
import static com.apb.beacon.R.layout.wizard_emergency_alert2;
import static com.apb.beacon.R.layout.wizard_emergency_alert3;
import static com.apb.beacon.R.string.next_action;
import static com.apb.beacon.wizard.SimpleFragment.create;

public class WizardPageAdapter extends FragmentStatePagerAdapter {
    private List<Fragment> fragments = new ArrayList<Fragment>();

    public WizardPageAdapter(FragmentManager fm) {
        super(fm);
        fragments.add(new WizardWelcomeFragment());          // 0   -
//        fragments.add(new CreateDialCodeFragment());
//        fragments.add(new CreatePinFragment());
        fragments.add(new WizardTrainingFragment());                        // 1 -
        fragments.add(new CreatePinFragment());                             // 2 -
        fragments.add(new WizardTrainingContactIntroFragment());            // 3 -
        fragments.add(new WizardTrainingContactLearnMoreFragment());        // 4
        fragments.add(new SMSContactNumberFragment());                      // 5
        fragments.add(new WizardTrainingMessageIntroFragment());            // 6 -
        fragments.add(new SMSMessageFragment());                            // 7
//        fragments.add(SMSSettingsFragment.create(R.string.sms_settings_wizard_header));
        fragments.add(create(wizard_emergency_alert1, next_action));
        fragments.add(create(wizard_emergency_alert2, next_action));
        fragments.add(create(wizard_emergency_alert3, next_action));
        fragments.add(new WizardFinishFragment());
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
