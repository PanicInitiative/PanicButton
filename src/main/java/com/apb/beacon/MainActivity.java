package com.apb.beacon;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.apb.beacon.alert.PanicAlert;
import com.apb.beacon.common.AppConstants;
import com.apb.beacon.common.AppUtil;
import com.apb.beacon.common.ApplicationSettings;
import com.apb.beacon.data.PBDatabase;
import com.apb.beacon.model.Page;
import com.apb.beacon.fragment.SetupContactsFragment;
import com.apb.beacon.fragment.SetupMessageFragment;
import com.apb.beacon.fragment.LanguageSettingsFragment;
import com.apb.beacon.fragment.SimpleFragment;
import com.apb.beacon.fragment.MainSetupAlertFragment;
import com.apb.beacon.fragment.SetupCodeFragment;
import com.apb.beacon.fragment.WarningFragment;
import com.apb.beacon.trigger.HardwareTriggerService;

/**
 * Created by aoe on 2/15/14.
 */
public class MainActivity extends BaseFragmentActivity {

    TextView tvToastMessage;

    Page currentPage;
    String pageId;
    String selectedLang;

    Boolean flagRiseFromPause = false;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.root_layout);
        
        tvToastMessage = (TextView) findViewById(R.id.tv_toast);

        try {
            pageId = getIntent().getExtras().getString("page_id");
        } catch (Exception e) {
            pageId = "home-not-configured";
            e.printStackTrace();
        }
        selectedLang = ApplicationSettings.getSelectedLanguage(this);

        Log.e("MainActivity.onCreate", "pageId = " + pageId);

        if(pageId.equals("home-not-configured")){
            Log.e("??????????????", "Restarting the Wizard");

            if((ApplicationSettings.isAlertActive(this))){
                new PanicAlert(this).deActivate();
            }

            // We're restarting the wizard so we deactivate the HardwareTriggerService
            stopService(new Intent(this, HardwareTriggerService.class));

            ApplicationSettings.setWizardState(MainActivity.this, AppConstants.WIZARD_FLAG_HOME_NOT_CONFIGURED);
            Intent i = new Intent(MainActivity.this, WizardActivity.class);
            i.putExtra("page_id", pageId);
            startActivity(i);

            callFinishActivityReceiver();
            finish();
            return;
        }

        // The app is now configured. Start HardwareTriggerService 
		startService(new Intent(this, HardwareTriggerService.class));

        PBDatabase dbInstance = new PBDatabase(this);
        dbInstance.open();
        currentPage = dbInstance.retrievePage(pageId, selectedLang);
        dbInstance.close();

        if (currentPage == null) {
            Log.e(">>>>>>", "page = null");
            Toast.makeText(this, "Still to be implemented.", Toast.LENGTH_SHORT).show();
            AppConstants.PAGE_FROM_NOT_IMPLEMENTED = true;
            finish();
            return;
        } else {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            Fragment fragment = null;

            if (currentPage.getType().equals("simple")) {
                tvToastMessage.setVisibility(View.INVISIBLE);
                fragment = new SimpleFragment().newInstance(pageId, AppConstants.FROM_MAIN_ACTIVITY);
            }else if (currentPage.getType().equals("warning")) {
                    tvToastMessage.setVisibility(View.INVISIBLE);
                    fragment = new WarningFragment().newInstance(pageId,AppConstants.FROM_MAIN_ACTIVITY);
                
            } else if (currentPage.getType().equals("modal")){
                tvToastMessage.setVisibility(View.INVISIBLE);
                Intent i = new Intent(MainActivity.this, MainModalActivity.class);
                i.putExtra("page_id", pageId);
                i.putExtra("parent_activity", AppConstants.FROM_MAIN_ACTIVITY);
                startActivity(i);
                finish();
                return;
            } else {
                if (currentPage.getComponent().equals("contacts"))
                    fragment = new SetupContactsFragment().newInstance(pageId, AppConstants.FROM_MAIN_ACTIVITY);
                else if (currentPage.getComponent().equals("message"))
                    fragment = new SetupMessageFragment().newInstance(pageId, AppConstants.FROM_MAIN_ACTIVITY);
                else if (currentPage.getComponent().equals("code"))
                    fragment = new SetupCodeFragment().newInstance(pageId, AppConstants.FROM_MAIN_ACTIVITY);
                else if (currentPage.getComponent().equals("alert"))
                    fragment = new MainSetupAlertFragment().newInstance(pageId, AppConstants.FROM_MAIN_ACTIVITY);
                else if (currentPage.getComponent().equals("language"))
                    fragment = new LanguageSettingsFragment().newInstance(pageId, AppConstants.FROM_MAIN_ACTIVITY);
                else
                    fragment = new SimpleFragment().newInstance(pageId, AppConstants.FROM_MAIN_ACTIVITY);
            }
            fragmentTransaction.add(R.id.fragment_container, fragment);
            fragmentTransaction.commit();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("MainActivity.onResume", "flagRiseFromPause = " + flagRiseFromPause);

//        if(AppConstants.PAGE_FROM_NOT_IMPLEMENTED){
//            Log.e("MainActivity.onResume", "returning from not-implemented page.");
//            AppConstants.PAGE_FROM_NOT_IMPLEMENTED = false;
//            return;
//        }

        if(AppConstants.IS_BACK_BUTTON_PRESSED){
            Log.e("MainActivity.onResume", "back button pressed");
            AppConstants.IS_BACK_BUTTON_PRESSED = false;
            return;
        }
        
        if (flagRiseFromPause) {
            Intent i = new Intent(MainActivity.this, CalculatorActivity.class);
            startActivity(i);
            overridePendingTransition(R.anim.show_from_bottom, R.anim.hide_to_top);

            callFinishActivityReceiver();

            finish();
            return;
        }
        return;
    }
    
    @Override
    protected void onPause() {
        super.onPause();
        Log.e("MainActivity.onPause", ".");
        Log.e("MainActivity.onPause", "flagRiseFromPause = " + true);
        flagRiseFromPause = true;
    }
    
    protected void onStop(){
        super.onStop();
        Log.e("MainActivity.onStop", ".");
    }

    @Override
    protected void onStart() {
        super.onStart();
//        if(pageId.equals("home-not-configured")){
//            Log.e("??????????????", "home-not-configured");
//        	ApplicationSettings.setWizardState(MainActivity.this, AppConstants.WIZARD_FLAG_HOME_NOT_CONFIGURED);
//        	Intent i = new Intent(MainActivity.this, WizardActivity.class);
//        	i.putExtra("page_id", "home-not-configured");
//        	startActivity(i);
//        }
        Log.e("MainActivity.onStart", ".");
    }

    @Override
    public void onBackPressed() {
        if(pageId.equals("home-ready")){
            // don't go back
        	finish();
        	startActivity(AppUtil.behaveAsHomeButton());
        }
        else{
            super.onBackPressed();
        }
        AppConstants.IS_BACK_BUTTON_PRESSED = true;
    }





}
