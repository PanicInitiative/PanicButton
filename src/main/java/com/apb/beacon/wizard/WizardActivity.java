package com.apb.beacon.wizard;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.apb.beacon.AppConstants;
import com.apb.beacon.R;
import com.apb.beacon.data.PBDatabase;
import com.apb.beacon.model.Page;
import com.apb.beacon.sms.SetupContactsFragment;
import com.apb.beacon.sms.SetupMessageFragment;

public class WizardActivity extends FragmentActivity {
    private WizardViewPager viewPager;
    private FragmentStatePagerAdapter pagerAdapter;

    Page currentPage;

//    @InjectView(R.id.previous_button)
//    Button previousButton;
//    @InjectView(R.id.action_button)
//    public Button actionButton;

//    private SimpleOnPageChangeListener pageChangeListener = new SimpleOnPageChangeListener() {
//        @Override
//        public void onPageSelected(int position) {
//            super.onPageSelected(position);
//            SoftKeyboard.hide(getApplicationContext(), getCurrentWizardFragment().getView());
//            previousButton.setVisibility(position != 0 ? VISIBLE : INVISIBLE);
////            actionButton.setVisibility(position != (pagerAdapter.getCount() - 1) ? VISIBLE : INVISIBLE);
//            setActionButtonVisibility(position);
//            if(position == AppConstants.PAGE_NUMBER_TRAINING_MESSAGE)
//                Toast.makeText(WizardActivity.this, "Enter your message.", Toast.LENGTH_SHORT).show();
//
//            Log.e(">>>>>>", "setting action text from pageChangeListener");
//            actionButton.setText(getCurrentWizardFragment().action());
//            getCurrentWizardFragment().onFragmentSelected();
//        }
//    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wizard_layout);

        String pageId = getIntent().getExtras().getString("page_id");
        String defaultLang = "en";

        PBDatabase dbInstance = new PBDatabase(this);
        dbInstance.open();
        currentPage = dbInstance.retrievePage(pageId, defaultLang);
        dbInstance.close();

        if (currentPage == null) {
            Log.e(">>>>>>", "page = null");
            Toast.makeText(this, "Still to be implemented.", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            Fragment fragment = null;

            if (currentPage.getType().equals("simple")) {
                fragment = new NewSimpleFragment().newInstance(pageId);
            }
            else if (currentPage.getType().equals("modal")){
                Intent i = new Intent(WizardActivity.this, WizardModalActivity.class);
                i.putExtra("page_id", pageId);
                startActivity(i);
                finish();
                return;
            }
            else {
                if (currentPage.getComponent().equals("contacts"))
                    fragment = new SetupContactsFragment().newInstance(pageId);
                else if (currentPage.getComponent().equals("message"))
                    fragment = new SetupMessageFragment().newInstance(pageId);
                else if (currentPage.getComponent().equals("code"))
                    fragment = new SetupCodeFragment().newInstance(pageId);
                else if (currentPage.getComponent().equals("alarm-test-hardware")){
                    fragment = new AlarmTestHardwareFragment().newInstance(pageId);
                }
                else if (currentPage.getComponent().equals("alarm-test-disguise")){
                    fragment = new AlarmTestDisguiseFragment().newInstance(pageId);
                }
                else if (currentPage.getComponent().equals("disguise-test-open")){
                    fragment = new TestDisguiseOpenFragment().newInstance(pageId);
                }
                else if (currentPage.getComponent().equals("disguise-test-unlock")){
                    fragment = new TestDisguiseUnlockFragment().newInstance(pageId);
                }
                else if (currentPage.getComponent().equals("disguise-test-code")){
                    fragment = new TestDisguiseCodeFragment().newInstance(pageId);
                }
                else
                    fragment = new NewSimpleFragment().newInstance(pageId);
            }
            fragmentTransaction.add(R.id.fragment_container, fragment);
            fragmentTransaction.commit();
        }
    }

    public void setActionButtonVisibility(int pageNumber) {
    }

//    public void performAction(View view) {
//        if(viewPager.getCurrentItem() == AppConstants.PAGE_NUMBER_TRAINING_CONTACTS_INTRO && view != null){
//            viewPager.nextWithSkip();
//        }
//        else if(getCurrentWizardFragment().performAction()){
//            viewPager.next();
//        }
//    }

    /*
    skip one fragment in the middle
     */
    public void performActionWithSkip() {
        viewPager.nextWithSkip();
    }

    public void previous(View view) {
        if (viewPager.getCurrentItem() == AppConstants.PAGE_NUMBER_TRAINING_CONTACTS) {
            viewPager.previousWithSkip();
        }
//        getCurrentWizardFragment().onBackPressed();
        else {
            viewPager.previous();
        }
    }

    public void previousWithSkip() {
//        getCurrentWizardFragment().onBackPressed();
        viewPager.previousWithSkip();
    }


    private WizardFragment getCurrentWizardFragment() {
        return (WizardFragment) pagerAdapter.getItem(viewPager.getCurrentItem());
    }

    FragmentStatePagerAdapter getWizardPagerAdapter() {
        return new WizardPageAdapter(getSupportFragmentManager());
    }

    //    @Override
//    public void enableActionButton(boolean isEnabled) {
//        actionButton.setEnabled(isEnabled);
//    }

//    @Override
//    public void setText(String buttonText) {
//        Log.e(">>>>>>>>>", "buttonText = " + buttonText);
//        actionButton.setText(buttonText);
//    }
}
