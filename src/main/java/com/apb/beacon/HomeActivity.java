package com.apb.beacon;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.apb.beacon.common.AppUtil;
import com.apb.beacon.data.PBDatabase;
import com.apb.beacon.model.Page;
import com.apb.beacon.model.ServerResponse;
import com.apb.beacon.parser.JsonParser;
import com.apb.beacon.wizard.WizardActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;

import static com.apb.beacon.ApplicationSettings.getHardcodeInsertion;
import static com.apb.beacon.ApplicationSettings.isFirstRun;
import static com.apb.beacon.ApplicationSettings.setHardcodeInsertion;

@ContentView(R.layout.welcome_screen)
public class HomeActivity extends RoboActivity {
    public static final int SPLASH_TIME = 1000;

    ProgressDialog pDialog;
//    JsonParser jsonParser;

    String pageId;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*
        hard-code initial data to the database.
         */
        checkIfDataInitializationNeeded();

        int wizardState = ApplicationSettings.getWizardState(this);
        if (wizardState == AppConstants.wizard_flag_home_not_completed) {
            pageId = "home-not-configured";
        } else if (wizardState == AppConstants.wizard_flag_home_not_configured_alarm) {
            pageId = "home-not-configured-alarm";
        } else if (wizardState == AppConstants.wizard_flag_home_not_configured_disguise) {
            pageId = "home-not-configured-disguise";
        } else if (wizardState == AppConstants.wizard_flag_home_ready) {
            pageId = "home-ready";
        }

        checkIfUpdateNeeded();

    }

    private void checkIfDataInitializationNeeded(){
        if (!getHardcodeInsertion(HomeActivity.this)) {
//            insertHardcodedDataToDatabase();
            setHardcodeInsertion(HomeActivity.this, true);
        }
    }

    private void checkIfUpdateNeeded(){
        long lastRunTimeInMillis = ApplicationSettings.getLastRunTimeInMillis(this);
        if (!AppUtil.isToday(lastRunTimeInMillis)) {
            Log.e(">>>>", "last run not today");
            new GetUpdate().execute();
        }
        else{
            if (isFirstRun(HomeActivity.this)) {
                scheduleTimer();
            } else {
                startFacade();
            }
        }
    }

    private void startFacade() {
        startActivity(new Intent(this, CalculatorActivity.class));
    }

    private void scheduleTimer() {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                Intent i = new Intent(HomeActivity.this, WizardActivity.class);
                i.putExtra("page_id", pageId);
                startActivity(i);
//                startActivity(new Intent(HomeActivity.this, WizardActivity.class));
            }
        }, SPLASH_TIME);
    }


    private class GetUpdate extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = ProgressDialog.show(HomeActivity.this, "Panic Button", "Checking for updates...", true, false);
        }

        @Override
        protected Void doInBackground(Void... params) {
            String url = AppConstants.BASE_URL;
            JsonParser jsonParser = new JsonParser();
            ServerResponse response = jsonParser.retrieveServerData(AppConstants.HTTP_REQUEST_TYPE_GET, url, null, null, null);
            if (response.getStatus() == 200) {
                Log.d(">>>><<<<", "success in retrieving server-response for url = " + url);
//                ApplicationSettings.setLastRunTimeInMillis(HomeActivity.this, System.currentTimeMillis());          // if we can retrieve a single data, we change it up-to-date

                try {
                    JSONObject responseObj = response.getjObj();
//                    Log.e(">>>>>>>>>>>>", "responseObj = " + responseObj);
                    JSONObject mobObj = responseObj.getJSONObject("mobile");
                    JSONArray dataArray = mobObj.getJSONArray("data");
                    List<Page> pageList = Page.parsePages(dataArray);

                    PBDatabase dbInstance = new PBDatabase(HomeActivity.this);
                    dbInstance.open();

                    for(int i = 0; i< pageList.size(); i++){
                        dbInstance.insertOrUpdateWizardPage(pageList.get(i));
//                        String id = pageList.get(i).getId();
//                        if(id.equals("home-not-configured")){
//                            FirstPage = pageList.get(i);
//                        }
                    }
                    dbInstance.close();
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

//            for (int i = 0; i < AppConstants.RELATIVE_URLS.length; i++) {
//                String url = AppConstants.BASE_URL + AppConstants.RELATIVE_URLS[i];
//                JsonParser jsonParser = new JsonParser();
//                MarkDownResponse response = jsonParser.retrieveMarkDownData(AppConstants.HTTP_REQUEST_TYPE_GET, url, null);
//                if (response.getStatus() == 200) {
//                    Log.d(">>>><<<<", "success in retrieving markdown info for url = " + url);
//                    ApplicationSettings.setLastRunTimeInMillis(HomeActivity.this, System.currentTimeMillis());          // if we can retrieve a single data, we change it up-to-date
//                    String mdContent = response.getMdData();
//                    LocalCachePage page = AppUtil.parseMarkDown(AppConstants.PAGE_NUMBER_FOR_DATA[i], AppConstants.PAGE_NAME_FOR_DATA[i], mdContent);
//
//                    PBDatabase dbInstance = new PBDatabase(HomeActivity.this);
//                    dbInstance.open();
//                    dbInstance.insertOrUpdateLocalCachePage(page);
//                    dbInstance.close();
//
//                }
//            }
            return null;
        }

        @Override
        protected void onPostExecute(Void response) {
            super.onPostExecute(response);
            if (pDialog.isShowing())
                pDialog.dismiss();

            if (isFirstRun(HomeActivity.this)) {
                Intent i = new Intent(HomeActivity.this, WizardActivity.class);
                i.putExtra("page_id", pageId);
                startActivity(i);
            } else {
                startActivity(new Intent(HomeActivity.this, CalculatorActivity.class));
            }
        }
    }


//    private void insertHardcodedDataToDatabase(){
//        String[] initial_page_content = {
//                getResources().getString(R.string.wizard_welcome_body),
//                getResources().getString(R.string.wizard_training_details),
//                getResources().getString(R.string.wizard_training_pin_details),
//                getResources().getString(R.string.wizard_training_contacts_details_intro),
//                "loren epsum - will add later",
//                getResources().getString(R.string.wizard_training_contacts_details),
//                getResources().getString(R.string.wizard_training_message_details_intro),
//                getResources().getString(R.string.wizard_training_message_details),
//                "emergency alert 1",
//                "emergency alert 2",
//                "emergency alert 3",
//                getResources().getString(R.string.wizard__training_disguise_intro)
//        };

//        PBDatabase dbInstance = new PBDatabase(HomeActivity.this);
//        dbInstance.open();
//
//        dbInstance.insertOrUpdateLocalCachePage(new LocalCachePage(AppConstants.PAGE_NUMBER_WIZARD_WELCOME, "Wizard Welcome", "Welcome", "action", "option", initial_page_content[AppConstants.PAGE_NUMBER_WIZARD_WELCOME]));
//        dbInstance.insertOrUpdateLocalCachePage(new LocalCachePage(AppConstants.PAGE_NUMBER_PANIC_BUTTON_TRAINING, "Wizard Training", "Welcome", "action", "option", initial_page_content[AppConstants.PAGE_NUMBER_PANIC_BUTTON_TRAINING]));
//        dbInstance.insertOrUpdateLocalCachePage(new LocalCachePage(AppConstants.PAGE_NUMBER_PANIC_BUTTON_TRAINING_PIN, "Wizard Training Pin", "Welcome", "action", "option", initial_page_content[AppConstants.PAGE_NUMBER_PANIC_BUTTON_TRAINING_PIN]));
//        dbInstance.insertOrUpdateLocalCachePage(new LocalCachePage(AppConstants.PAGE_NUMBER_TRAINING_CONTACTS_INTRO, "Wizard Contact Intro", "Welcome", "action", "option", initial_page_content[AppConstants.PAGE_NUMBER_TRAINING_CONTACTS_INTRO]));
//        dbInstance.insertOrUpdateLocalCachePage(new LocalCachePage(AppConstants.PAGE_NUMBER_TRAINING_CONTACTS_LEARN_MORE, "Wizard Contact Learn More", "Welcome", "action", "option", initial_page_content[AppConstants.PAGE_NUMBER_TRAINING_CONTACTS_LEARN_MORE]));
//        dbInstance.insertOrUpdateLocalCachePage(new LocalCachePage(AppConstants.PAGE_NUMBER_TRAINING_CONTACTS, "Wizard Contact", "Welcome", "action", "option", initial_page_content[AppConstants.PAGE_NUMBER_TRAINING_CONTACTS]));
//        dbInstance.insertOrUpdateLocalCachePage(new LocalCachePage(AppConstants.PAGE_NUMBER_TRAINING_MESSAGE_INTRO, "Wizard Message Intro", "Welcome", "action", "option", initial_page_content[AppConstants.PAGE_NUMBER_TRAINING_MESSAGE_INTRO]));
//        dbInstance.insertOrUpdateLocalCachePage(new LocalCachePage(AppConstants.PAGE_NUMBER_TRAINING_MESSAGE, "Wizard Message", "Welcome", "action", "option", initial_page_content[AppConstants.PAGE_NUMBER_TRAINING_MESSAGE]));
//        dbInstance.insertOrUpdateLocalCachePage(new LocalCachePage(AppConstants.PAGE_NUMBER_DISGUISE_INTRO, "Wizard Disguise Intro", "Step 5: Activate Disguise", "Try it now", "option", initial_page_content[AppConstants.PAGE_NUMBER_DISGUISE_INTRO]));
//
//        dbInstance.close();
//    }

}