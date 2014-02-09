package com.apb.beacon.wizard;


import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.apb.beacon.AppConstants;
import com.apb.beacon.ApplicationSettings;
import com.apb.beacon.CalculatorActivity;
import com.apb.beacon.R;
import com.apb.beacon.common.MyTagHandler;
import com.apb.beacon.data.PBDatabase;
import com.apb.beacon.model.Page;
import com.apb.beacon.sms.SetupContactsFragment;
import com.apb.beacon.sms.SetupMessageFragment;

public class WizardActivity extends FragmentActivity {
    private WizardViewPager viewPager;
    private FragmentStatePagerAdapter pagerAdapter;

    Page currentPage;
    String pageId;
    String defaultLang;

    TextView tvToastMessage;
    Boolean flagRiseFromPause = false;

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

        tvToastMessage = (TextView) findViewById(R.id.tv_toast);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.package.ACTION_LOGOUT");
        registerReceiver(activityFinishReceiver, intentFilter);

        pageId = getIntent().getExtras().getString("page_id");
        defaultLang = "en";

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
                tvToastMessage.setVisibility(View.INVISIBLE);
                fragment = new NewSimpleFragment().newInstance(pageId);
            }
            else if (currentPage.getType().equals("warning")) {
                tvToastMessage.setVisibility(View.INVISIBLE);
                fragment = new WarningFragment().newInstance(pageId);
            }
            else if (currentPage.getType().equals("modal")){
                tvToastMessage.setVisibility(View.INVISIBLE);
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
                    tvToastMessage.setVisibility(View.VISIBLE);
                    if(currentPage.getIntroduction() != null){
                        tvToastMessage.setText(Html.fromHtml(currentPage.getIntroduction(), null, new MyTagHandler()));
                    }
                    fragment = new AlarmTestHardwareFragment().newInstance(pageId);
                }
                else if (currentPage.getComponent().equals("alarm-test-disguise")){
                    tvToastMessage.setVisibility(View.VISIBLE);
                    if(currentPage.getIntroduction() != null){
                        tvToastMessage.setText(Html.fromHtml(currentPage.getIntroduction(), null, new MyTagHandler()));
                    }
                    fragment = new AlarmTestDisguiseFragment().newInstance(pageId);
                }
                else if (currentPage.getComponent().equals("disguise-test-open")){
                    findViewById(R.id.wizard_layout_root).setBackgroundColor(Color.BLACK);
                    tvToastMessage.setVisibility(View.VISIBLE);
                    if(currentPage.getIntroduction() != null){
                        tvToastMessage.setText(Html.fromHtml(currentPage.getIntroduction(), null, new MyTagHandler()));
                    }
                    fragment = new TestDisguiseOpenFragment().newInstance(pageId);
                }
                else if (currentPage.getComponent().equals("disguise-test-unlock")){
                    tvToastMessage.setVisibility(View.VISIBLE);
                    if(currentPage.getIntroduction() != null){
                        tvToastMessage.setText(Html.fromHtml(currentPage.getIntroduction(), null, new MyTagHandler()));
                    }

                    fragment = new TestDisguiseUnlockFragment().newInstance(pageId);
                }
                else if (currentPage.getComponent().equals("disguise-test-code")){
                    tvToastMessage.setVisibility(View.VISIBLE);
                    if(currentPage.getIntroduction() != null){
                        tvToastMessage.setText(Html.fromHtml(currentPage.getIntroduction(), null, new MyTagHandler()));
                    }
                    fragment = new TestDisguiseCodeFragment().newInstance(pageId);
                }
                else
                    fragment = new NewSimpleFragment().newInstance(pageId);
            }
            fragmentTransaction.add(R.id.fragment_container, fragment);
            fragmentTransaction.commit();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(">>>>>", "onPause");
        if(currentPage.getId().equals("home-ready")){
            ApplicationSettings.completeFirstRun(WizardActivity.this);
        }

        if(!pageId.equals("setup-alarm-test-hardware")){            // we block this page for pause-resume action
            Log.e(">>>>>>", "assert flagRiseFromPause = " + true);
            flagRiseFromPause = true;
        }
    }

    @Override
    protected void onStop(){
        super.onStop();
        Log.d(">>>>>>>>>>", "onStop");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(">>>>>>>>>>", "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(">>>>>", "onResume with flagRiseFromPause = " + flagRiseFromPause);
        if(!ApplicationSettings.isFirstRun(WizardActivity.this)){
            getPackageManager().setComponentEnabledSetting(
                    new ComponentName("com.apb.beacon", "com.apb.beacon.HomeActivity-calculator"),
                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);

            getPackageManager().setComponentEnabledSetting(
                    new ComponentName("com.apb.beacon", "com.apb.beacon.HomeActivity-setup"),
                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);

            Intent i = new Intent(WizardActivity.this, CalculatorActivity.class);
            startActivity(i);
            overridePendingTransition(R.anim.show_from_bottom, R.anim.hide_to_top);

            Intent broadcastIntent = new Intent();
            broadcastIntent.setAction("com.package.ACTION_LOGOUT");
            sendBroadcast(broadcastIntent);

            finish();
            return;
        }

        if(AppConstants.wizard_is_back_button_pressed){
            Log.e(">>>>>>>>", "back button pressed");
            AppConstants.wizard_is_back_button_pressed = false;
            return;
        }

        if (!pageId.equals("setup-alarm-test-hardware-success") && flagRiseFromPause) {
//        if (flagRiseFromPause) {
            int wizardState = ApplicationSettings.getWizardState(WizardActivity.this);
            if (wizardState == AppConstants.wizard_flag_home_not_completed) {
                pageId = "home-not-configured";
            } else if (wizardState == AppConstants.wizard_flag_home_not_configured_alarm) {
                pageId = "home-not-configured-alarm";
            } else if (wizardState == AppConstants.wizard_flag_home_not_configured_disguise) {
                pageId = "home-not-configured-disguise";
            } else if (wizardState == AppConstants.wizard_flag_home_ready) {
                pageId = "home-ready";
            }

            Intent i = new Intent(WizardActivity.this, WizardActivity.class);
            i.putExtra("page_id", pageId);
            startActivity(i);
//            overridePendingTransition(R.anim.show_from_bottom, R.anim.hide_to_top);

            Intent broadcastIntent = new Intent();
            broadcastIntent.setAction("com.package.ACTION_LOGOUT");
            sendBroadcast(broadcastIntent);

            finish();
        }
    }

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        hideToastMessageInInteractiveFragment();
    }

    public void hideToastMessageInInteractiveFragment(){
        tvToastMessage.setVisibility(View.INVISIBLE);
    }


    @Override
    public void onBackPressed() {
        if(pageId.equals("home-ready")){
            // don't go back
        }
        else{
            super.onBackPressed();
        }
        AppConstants.wizard_is_back_button_pressed = true;
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(activityFinishReceiver);
    }

    private WizardFragment getCurrentWizardFragment() {
        return (WizardFragment) pagerAdapter.getItem(viewPager.getCurrentItem());
    }

    FragmentStatePagerAdapter getWizardPagerAdapter() {
        return new WizardPageAdapter(getSupportFragmentManager());
    }

    BroadcastReceiver activityFinishReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("onReceive","Logout in progress");
            finish();
        }
    };
}
