package com.amnesty.panicbutton.wizard;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import com.amnesty.panicbutton.R;
import roboguice.activity.RoboFragmentActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

import static android.support.v4.view.ViewPager.SimpleOnPageChangeListener;

@ContentView(R.layout.wizard_layout)
public class WizardActivity extends RoboFragmentActivity {
    private ViewPagerWithoutSwipe viewPager;
    @InjectView(R.id.previous_button) Button previousButton;

    private SimpleOnPageChangeListener pageChangeListener = new ViewPager.SimpleOnPageChangeListener() {
        @Override
        public void onPageSelected(int position) {
            super.onPageSelected(position);
            previousButton.setEnabled(position != 0);
        }
    };

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewPager = (ViewPagerWithoutSwipe) findViewById(R.id.pager);
        previousButton.setEnabled(false);
        viewPager.setOnPageChangeListener(pageChangeListener);
    }

    public void next(View view) {
        viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
    }

    public void previous(View view) {
        viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
    }
}
