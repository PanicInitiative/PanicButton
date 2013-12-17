package com.apb.beacon;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.apb.beacon.common.AppUtil;
import com.apb.beacon.data.PBDatabase;
import com.apb.beacon.model.LocalCachePage;
import com.apb.beacon.model.MarkDownResponse;
import com.apb.beacon.parser.JsonParser;
import com.apb.beacon.wizard.WizardActivity;

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
    JsonParser jsonParser;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        long lastRunTimeInMillis = ApplicationSettings.getLastRunTimeInMillis(this);
        jsonParser = new JsonParser();

        /*
        hard-code initial data to the database.
         */
        if (!getHardcodeInsertion(HomeActivity.this)) {
            insertHardcodedDataToDatabase();
            setHardcodeInsertion(HomeActivity.this, true);
        }

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
                startActivity(new Intent(HomeActivity.this, WizardActivity.class));
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
            for (int i = 0; i < AppConstants.RELATIVE_URLS.length; i++) {
                String url = AppConstants.BASE_URL + AppConstants.RELATIVE_URLS[i];
                MarkDownResponse response = jsonParser.retrieveMarkDownData(AppConstants.HTTP_REQUEST_TYPE_GET, url, null);
                if (response.getStatus() == 200) {
                    Log.d(">>>><<<<", "success in retrieving markdown info for url = " + url);
                    ApplicationSettings.setLastRunTimeInMillis(HomeActivity.this, System.currentTimeMillis());          // if we can retrieve a single data, we change it up-to-date
                    String mdContent = response.getMdData();
                    LocalCachePage page = AppUtil.parseMarkDown(AppConstants.PAGE_NUMBER_FOR_DATA[i], AppConstants.PAGE_NAME_FOR_DATA[i], mdContent);

                    PBDatabase dbInstance = new PBDatabase(HomeActivity.this);
                    dbInstance.open();
                    dbInstance.insertOrUpdateLocalCachePage(page);
                    dbInstance.close();

                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void response) {
            super.onPostExecute(response);
            if (pDialog.isShowing())
                pDialog.dismiss();

            if (isFirstRun(HomeActivity.this)) {
                startActivity(new Intent(HomeActivity.this, WizardActivity.class));
            } else {
                startActivity(new Intent(HomeActivity.this, CalculatorActivity.class));
            }
        }
    }


    private void insertHardcodedDataToDatabase(){
        String[] initial_page_content = {
                getResources().getString(R.string.wizard_welcome_body),
                getResources().getString(R.string.wizard_training_details),
                getResources().getString(R.string.wizard_training_pin_details),
                getResources().getString(R.string.wizard_training_contacts_details_intro),
                "loren epsum - will add later",
                getResources().getString(R.string.wizard_training_contacts_details),
                getResources().getString(R.string.wizard_training_message_details_intro),
                getResources().getString(R.string.wizard_training_message_details)
        };

        PBDatabase dbInstance = new PBDatabase(HomeActivity.this);
        dbInstance.open();

        dbInstance.insertOrUpdateLocalCachePage(new LocalCachePage(0, "Wizard Welcome", "Welcome", "action", "option", initial_page_content[0]));
        dbInstance.insertOrUpdateLocalCachePage(new LocalCachePage(1, "Wizard Training", "Welcome", "action", "option", initial_page_content[1]));
        dbInstance.insertOrUpdateLocalCachePage(new LocalCachePage(2, "Wizard Training Pin", "Welcome", "action", "option", initial_page_content[2]));
        dbInstance.insertOrUpdateLocalCachePage(new LocalCachePage(3, "Wizard Contact Intro", "Welcome", "action", "option", initial_page_content[3]));
        dbInstance.insertOrUpdateLocalCachePage(new LocalCachePage(4, "Wizard Contact Learn More", "Welcome", "action", "option", initial_page_content[4]));
        dbInstance.insertOrUpdateLocalCachePage(new LocalCachePage(5, "Wizard Contact", "Welcome", "action", "option", initial_page_content[5]));
        dbInstance.insertOrUpdateLocalCachePage(new LocalCachePage(6, "Wizard Message Intro", "Welcome", "action", "option", initial_page_content[6]));
        dbInstance.insertOrUpdateLocalCachePage(new LocalCachePage(7, "Wizard Message", "Welcome", "action", "option", initial_page_content[7]));

        dbInstance.close();
    }

}