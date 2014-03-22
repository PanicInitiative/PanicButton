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

import com.apb.beacon.common.AppUtil;
import com.apb.beacon.data.PBDatabase;
import com.apb.beacon.model.Page;
import com.apb.beacon.sms.SetupContactsFragment;
import com.apb.beacon.sms.SetupMessageFragment;
import com.apb.beacon.wizard.LanguageSettingsFragment;
import com.apb.beacon.wizard.NewSimpleFragment;
import com.apb.beacon.wizard.SetupCodeFragment;

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
        
        callFinishActivityReceivier();
        
        tvToastMessage = (TextView) findViewById(R.id.tv_toast);
//        tvToastMessage.setVisibility(View.GONE);

        pageId = getIntent().getExtras().getString("page_id");
        selectedLang = ApplicationSettings.getSelectedLanguage(this);

        Log.e("MainActivity.onCreate", "pageId = " + pageId);

        PBDatabase dbInstance = new PBDatabase(this);
        dbInstance.open();
        currentPage = dbInstance.retrievePage(pageId, selectedLang);
        dbInstance.close();

        if (currentPage == null) {
            Log.e("MainActivity.onCreate", "page = null");
            Toast.makeText(this, "Still to be implemented.", Toast.LENGTH_SHORT).show();
            AppConstants.PAGE_FROM_NOT_IMPLEMENTED = true;
            finish();
        } else {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            Fragment fragment = null;

            if (currentPage.getType().equals("simple")) {
                tvToastMessage.setVisibility(View.INVISIBLE);
                fragment = new NewSimpleFragment().newInstance(pageId, AppConstants.FROM_MAIN_ACTIVITY);
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
                else if (currentPage.getComponent().equals("language"))
                    fragment = new LanguageSettingsFragment().newInstance(pageId);
                else
                    fragment = new NewSimpleFragment().newInstance(pageId, AppConstants.FROM_MAIN_ACTIVITY);
            }
            fragmentTransaction.add(R.id.fragment_container, fragment);
            fragmentTransaction.commit();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("MainActivity.onResume", "flagRiseFromPause = " + flagRiseFromPause);

        if(AppConstants.PAGE_FROM_NOT_IMPLEMENTED){
            Log.e("MainActivity.onResume", "returning from not-implemented page.");
            AppConstants.PAGE_FROM_NOT_IMPLEMENTED = false;
            return;
        }

        if(AppConstants.MAIN_IS_BACK_BUTTON_PRESSED){
            Log.e("MainActivity.onResume", "back button pressed");
            AppConstants.MAIN_IS_BACK_BUTTON_PRESSED = false;
            return;
        }
        
        if (flagRiseFromPause) {
            Intent i = new Intent(MainActivity.this, CalculatorActivity.class);
            startActivity(i);
            overridePendingTransition(R.anim.show_from_bottom, R.anim.hide_to_top);

            callFinishActivityReceivier();

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
        Log.d("MainActivity.onStop", ".");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("MainActivity.onStart", ".");
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
        AppConstants.MAIN_IS_BACK_BUTTON_PRESSED = true;
    }





}
