package com.apb.beacon.wizard;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.apb.beacon.AppConstants;
import com.apb.beacon.R;
import com.apb.beacon.data.PBDatabase;
import com.apb.beacon.model.Page;
import com.apb.beacon.sms.SetupContactsFragment;
import com.apb.beacon.sms.SetupMessageFragment;

public class WizardActivity extends FragmentActivity{
    private WizardViewPager viewPager;
    private FragmentStatePagerAdapter pagerAdapter;

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
        Page currentPage = dbInstance.retrievePage(pageId, defaultLang);
        dbInstance.close();



        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        Fragment fragment = null;

        if(currentPage.getType().equals("simple")){
            fragment = new NewSimpleFragment().newInstance(pageId);
        }
        else{
            if(currentPage.getComponent().equals("contacts"))
                fragment = new SetupContactsFragment().newInstance(pageId);
            else if(currentPage.getComponent().equals("message"))
                fragment = new SetupMessageFragment().newInstance(pageId);
            else if(currentPage.getComponent().equals("code"))
                fragment = new SetupCodeFragment().newInstance(pageId);
            else
                fragment = new NewSimpleFragment().newInstance(pageId);
        }
        fragmentTransaction.add(R.id.fragment_container, fragment);
        fragmentTransaction.commit();



//        previousButton.setVisibility(INVISIBLE);
//
//        PBDatabase dbInstance = new PBDatabase(WizardActivity.this);
//        dbInstance.open();
//        LocalCachePage page = dbInstance.retrievePage(AppConstants.PAGE_NUMBER_WIZARD_WELCOME);
//        dbInstance.close();
//        Log.e(">>>>>>", "setting action text from onCreate -> " + page.getPageAction());
//        actionButton.setText(page.getPageAction());
//
//        viewPager = (WizardViewPager) findViewById(R.id.wizard_view_pager);
//        pagerAdapter = getWizardPagerAdapter();
//        viewPager.setAdapter(pagerAdapter);
//        viewPager.setOffscreenPageLimit(2);
//        viewPager.setOnPageChangeListener(pageChangeListener);
    }

    public void setActionButtonVisibility(int pageNumber){
//        if(pageNumber == AppConstants.PAGE_NUMBER_PANIC_BUTTON_TRAINING)
//            actionButton.setVisibility(View.INVISIBLE);
//        else if(pageNumber == AppConstants.PAGE_NUMBER_TRAINING_CONTACTS_INTRO)
//            actionButton.setVisibility(View.INVISIBLE);
//        else if(pageNumber == AppConstants.PAGE_NUMBER_TRAINING_CONTACTS_LEARN_MORE)
//            actionButton.setVisibility(View.INVISIBLE);
//        if(pageNumber == AppConstants.PAGE_NUMBER_TRAINING_MESSAGE_INTRO)
//            actionButton.setVisibility(View.INVISIBLE);
//        if(pageNumber == pagerAdapter.getCount() -1)
//            actionButton.setVisibility(View.INVISIBLE);
//        else
//            actionButton.setVisibility(View.VISIBLE);
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
        if(viewPager.getCurrentItem() == AppConstants.PAGE_NUMBER_TRAINING_CONTACTS){
            viewPager.previousWithSkip();
        }
//        getCurrentWizardFragment().onBackPressed();
        else{
            viewPager.previous();
        }
    }

    public void previousWithSkip() {
//        getCurrentWizardFragment().onBackPressed();
        viewPager.previousWithSkip();
    }

//    @Override
//    public void onBackPressed() {
//        getCurrentWizardFragment().onBackPressed();
//        if(viewPager.isFirstPage()) {
//            this.finish();
//        }
//        else if(viewPager.getCurrentItem() == AppConstants.PAGE_NUMBER_TRAINING_CONTACTS){
//            viewPager.previousWithSkip();
//        }
//        else{
//            viewPager.previous();
//        }
//    }

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
