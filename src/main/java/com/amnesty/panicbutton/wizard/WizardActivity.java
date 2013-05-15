package com.amnesty.panicbutton.wizard;

import android.os.Bundle;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import com.amnesty.panicbutton.R;
import com.amnesty.panicbutton.SoftKeyboard;
import roboguice.activity.RoboFragmentActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

import static android.support.v4.view.ViewPager.SimpleOnPageChangeListener;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

@ContentView(R.layout.wizard_layout)
public class WizardActivity extends RoboFragmentActivity implements ActionButtonStateListener {
    private WizardViewPager viewPager;
    private FragmentStatePagerAdapter pagerAdapter;

    @InjectView(R.id.previous_button)
    Button previousButton;
    @InjectView(R.id.action_button)
    Button actionButton;

    private SimpleOnPageChangeListener pageChangeListener = new ViewPager.SimpleOnPageChangeListener() {
        @Override
        public void onPageSelected(int position) {
            super.onPageSelected(position);
            SoftKeyboard.hide(getApplicationContext(), getCurrentWizardFragment().getView());
            previousButton.setVisibility(position != 0 ? VISIBLE : INVISIBLE);
            actionButton.setVisibility(position != (pagerAdapter.getCount() - 1) ? VISIBLE : INVISIBLE);
            actionButton.setText(getCurrentWizardFragment().action());
            getCurrentWizardFragment().onFragmentSelected();
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        previousButton.setVisibility(INVISIBLE);
        actionButton.setText(getString(R.string.start_action));

        viewPager = (WizardViewPager) findViewById(R.id.wizard_view_pager);
        pagerAdapter = getWizardPagerAdapter();
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOnPageChangeListener(pageChangeListener);
    }

    public void performAction(View view) {
        if(getCurrentWizardFragment().performAction()) {
            viewPager.next();
        }
    }

    public void previous(View view) {
        viewPager.previous();
    }

    private WizardFragment getCurrentWizardFragment() {
        return (WizardFragment) pagerAdapter.getItem(viewPager.getCurrentItem());
    }

    FragmentStatePagerAdapter getWizardPagerAdapter() {
        return new WizardPageAdapter(getSupportFragmentManager());
    }

    @Override
    public void onActionStateChanged(boolean state) {
        actionButton.setEnabled(state);
    }

    @Override
    public void onBackPressed() {
        if(viewPager.isFirstPage()) {
            this.finish();
        }
        viewPager.previous();
    }
}
