package com.apb.beacon;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.apb.beacon.common.AppUtil;
import com.apb.beacon.data.PBDatabase;
import com.apb.beacon.model.HelpPage;
import com.apb.beacon.model.Page;
import com.apb.beacon.model.ServerResponse;
import com.apb.beacon.parser.JsonParser;
import com.apb.beacon.wizard.WizardActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static com.apb.beacon.ApplicationSettings.isFirstRun;

public class HomeActivity extends Activity {

    ProgressDialog pDialog;

    String pageId;
    String selectedLang;
    String mobileDataUrl;
    String helpDataUrl;

    int lastUpdatedVersion;
    int latestVersion;
    long lastRunTimeInMillis;
    int lastDBVersion;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_screen);

        latestVersion = -1;
        lastUpdatedVersion = ApplicationSettings.getLastUpdatedVersion(HomeActivity.this);

        int wizardState = ApplicationSettings.getWizardState(this);
        if (AppConstants.SKIP_WIZARD) {
            pageId = "home-ready";
        } else if (wizardState == AppConstants.WIZARD_FLAG_HOME_NOT_COMPLETED) {
            pageId = "home-not-configured";
        } else if (wizardState == AppConstants.WIZARD_FLAG_HOME_NOT_CONFIGURED_ALARM) {
            pageId = "home-not-configured-alarm";
        } else if (wizardState == AppConstants.WIZARD_FLAG_HOME_NOT_CONFIGURED_DISGUISE) {
            pageId = "home-not-configured-disguise";
        } else if (wizardState == AppConstants.WIZARD_FLAG_HOME_READY) {
            pageId = "home-ready";
        }

        selectedLang = ApplicationSettings.getSelectedLanguage(this);
        helpDataUrl = AppConstants.BASE_URL + AppConstants.HELP_DATA_URL;

        lastRunTimeInMillis = ApplicationSettings.getLastRunTimeInMillis(this);

        lastDBVersion = ApplicationSettings.getLastUpdatedDBVersion(this);
//        Log.e(">>>>>>", "lastDBVersion = " + lastDBVersion);

        if(lastDBVersion < AppConstants.DATABASE_VERSION){
            ApplicationSettings.setLocalDataInsertion(this, false);
            lastRunTimeInMillis = -1;
        }

        if (!ApplicationSettings.getLocalDataInsertion(HomeActivity.this)) {
            new InitializeLocalData().execute();
        }

        else if (!AppUtil.isToday(lastRunTimeInMillis) && AppUtil.hasInternet(HomeActivity.this)) {
            Log.e(">>>>", "last run not today");
            new GetLatestVersion().execute();
        }

        else{
            startNextActivity();
        }
    }
    
    @Override
    protected void onDestroy() {
    	super.onDestroy();
    	AppUtil.unbindDrawables(getWindow().getDecorView().findViewById(android.R.id.content));
        System.gc();
    }

    private void startNextActivity(){
        if (isFirstRun(HomeActivity.this)) {
            Intent i = new Intent(HomeActivity.this, WizardActivity.class);
            i.putExtra("page_id", pageId);
            startActivity(i);
        } else {
            Intent i = new Intent(HomeActivity.this, CalculatorActivity.class);
            startActivity(i);
        }
    }


    private void startWizard() {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                Intent i = new Intent(HomeActivity.this, WizardActivity.class);
                i = AppUtil.clearBackStack(i);
                i.putExtra("page_id", pageId);
                startActivity(i);
            }
        }, AppConstants.SPLASH_DELAY_TIME);
    }

    private class InitializeLocalData extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = ProgressDialog.show(HomeActivity.this, "Panic Button", "Installing...", true, false);
        }

        @Override
        protected Boolean doInBackground(Void... params) {
	        try {
	            JSONObject jsonObj = new JSONObject(loadJSONFromAsset("mobile_en.json"));
	            JSONObject mobileObj = jsonObj.getJSONObject("mobile");

	            lastUpdatedVersion = mobileObj.getInt("version");
	            ApplicationSettings.setLastUpdatedVersion(HomeActivity.this, lastUpdatedVersion);

	            JSONArray dataArray = mobileObj.getJSONArray("data");
	            insertMobileDataToLocalDB(dataArray);
	        } catch (JSONException e) {
	            e.printStackTrace();
	        }

	        try {
	            JSONObject jsonObj = new JSONObject(loadJSONFromAsset("mobile_es.json"));
	            JSONObject mobileObj = jsonObj.getJSONObject("mobile");

	            JSONArray dataArray = mobileObj.getJSONArray("data");
	            insertMobileDataToLocalDB(dataArray);
	        } catch (JSONException e) {
	            e.printStackTrace();
	        }

	        try {
	            JSONObject jsonObj = new JSONObject(loadJSONFromAsset("mobile_ph.json"));
	            JSONObject mobileObj = jsonObj.getJSONObject("mobile");

	            JSONArray dataArray = mobileObj.getJSONArray("data");
	            insertMobileDataToLocalDB(dataArray);
	        } catch (JSONException e) {
	            e.printStackTrace();
	        }

	        try {
	            JSONObject jsonObj = new JSONObject(loadJSONFromAsset("mobile_fr.json"));
	            JSONObject mobileObj = jsonObj.getJSONObject("mobile");

	            JSONArray dataArray = mobileObj.getJSONArray("data");
	            insertMobileDataToLocalDB(dataArray);
	        } catch (JSONException e) {
	            e.printStackTrace();
	        }

	        try {
	            JSONObject jsonObj = new JSONObject(loadJSONFromAsset("help_en.json"));
	            JSONObject mobileObj = jsonObj.getJSONObject("help");

	            lastUpdatedVersion = mobileObj.getInt("version");
	            ApplicationSettings.setLastUpdatedVersion(HomeActivity.this, lastUpdatedVersion);

	            JSONArray dataArray = mobileObj.getJSONArray("data");
	            insertMobileDataToLocalDB(dataArray);
	        } catch (JSONException e) {
	            e.printStackTrace();
	        }

	        try {
	            JSONObject jsonObj = new JSONObject(loadJSONFromAsset("help_es.json"));
	            JSONObject mobileObj = jsonObj.getJSONObject("help");

	            JSONArray dataArray = mobileObj.getJSONArray("data");
	            insertMobileDataToLocalDB(dataArray);
	        } catch (JSONException e) {
	            e.printStackTrace();
	        }

	        try {
	            JSONObject jsonObj = new JSONObject(loadJSONFromAsset("help_fr.json"));
	            JSONObject mobileObj = jsonObj.getJSONObject("help");

	            JSONArray dataArray = mobileObj.getJSONArray("data");
	            insertMobileDataToLocalDB(dataArray);
	        } catch (JSONException e) {
	            e.printStackTrace();
	        }

	        try {
	            JSONObject jsonObj = new JSONObject(loadJSONFromAsset("help_ph.json"));
	            JSONObject mobileObj = jsonObj.getJSONObject("help");

	            JSONArray dataArray = mobileObj.getJSONArray("data");
	            insertMobileDataToLocalDB(dataArray);
	        } catch (JSONException e) {
	            e.printStackTrace();
	        }
	        return true;
        }

        @Override
        protected void onPostExecute(Boolean response) {
            super.onPostExecute(response);
            if (pDialog.isShowing())
				try {
					pDialog.dismiss();
				} catch (Exception e) {
					e.printStackTrace();
				}

            ApplicationSettings.setLocalDataInsertion(HomeActivity.this, true);
            ApplicationSettings.setLastUpdatedDBVersion(HomeActivity.this, AppConstants.DATABASE_VERSION);

            if (!AppUtil.isToday(lastRunTimeInMillis) && AppUtil.hasInternet(HomeActivity.this)) {
                Log.e(">>>>", "last run not today");
                new GetLatestVersion().execute();
            } else{
                startNextActivity();;
            }
        }
    }
    
    private class GetLatestVersion extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = ProgressDialog.show(HomeActivity.this, "Panic Button", "Starting...", true, false);
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            String url = AppConstants.BASE_URL + AppConstants.VERSION_CHECK_URL;
            JsonParser jsonParser = new JsonParser();
            ServerResponse response = jsonParser.retrieveServerData(AppConstants.HTTP_REQUEST_TYPE_GET, url, null, null, null);
            if (response.getStatus() == 200) {
                try {
                    JSONObject responseObj = response.getjObj();
                    latestVersion = responseObj.getInt("version");
                    Log.e("??????", "latest version = " + latestVersion);
                    return true;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean response) {
            super.onPostExecute(response);

            if (latestVersion > lastUpdatedVersion) {
                new GetMobileDataUpdate().execute();
            } else {
                ApplicationSettings.setLastRunTimeInMillis(HomeActivity.this, System.currentTimeMillis());
                if (pDialog.isShowing())
					try {
						pDialog.dismiss();
					} catch (Exception e) {
						e.printStackTrace();
					}
                    startNextActivity();
            }
        }
    }



    private class GetMobileDataUpdate extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try {
				pDialog = ProgressDialog.show(HomeActivity.this, "Panic Button", "Downloading updates...", true, false);
			} catch (Exception e) {
				e.printStackTrace();
			}
       }

        @Override
        protected Boolean doInBackground(Void... params) {

            int version = 0;
            for(version = lastUpdatedVersion + 1; version <= latestVersion; version ++){
                if (selectedLang.equals("en")) {
                    mobileDataUrl = AppConstants.BASE_URL + "mobile." + version + ".json";
                } else {
                    mobileDataUrl = AppConstants.BASE_URL + selectedLang + "/" + "mobile." + version + ".json";
                }

                JsonParser jsonParser = new JsonParser();
                ServerResponse response = jsonParser.retrieveServerData(AppConstants.HTTP_REQUEST_TYPE_GET, mobileDataUrl, null, null, null);
                if (response.getStatus() == 200) {
                    Log.d(">>>><<<<", "success in retrieving server-response for url = " + mobileDataUrl);
                    try {
                        JSONObject responseObj = response.getjObj();
                        JSONObject mobObj = responseObj.getJSONObject("mobile");
                        JSONArray dataArray = mobObj.getJSONArray("data");
                        insertMobileDataToLocalDB(dataArray);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        return false;
                    }
                }
            }

            if(version > latestVersion){
                return true;
            } else{
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean response) {
            super.onPostExecute(response);

            if(response){
                new GetHelpDataUpdate().execute();
            }
            else{
                if (pDialog.isShowing())
                    pDialog.dismiss();

                startNextActivity();
            }
        }
    }




    private class GetHelpDataUpdate extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try {
				pDialog = ProgressDialog.show(HomeActivity.this, "Panic Button", "Downloading help pages...", true, false);
			} catch (Exception e) {
				e.printStackTrace();
			}
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            JsonParser jsonParser = new JsonParser();
            ServerResponse response = jsonParser.retrieveServerData(AppConstants.HTTP_REQUEST_TYPE_GET, helpDataUrl, null, null, null);
            if (response.getStatus() == 200) {
                Log.d(">>>><<<<", "success in retrieving server-response for url = " + helpDataUrl);
                ApplicationSettings.setLastRunTimeInMillis(HomeActivity.this, System.currentTimeMillis());          // if we can retrieve a single data, we change it up-to-date
                try {
                    JSONObject responseObj = response.getjObj();
                    JSONObject mobObj = responseObj.getJSONObject("help");
                    JSONArray dataArray = mobObj.getJSONArray("data");
                    insertHelpDataToLocalDB(dataArray);
                    ApplicationSettings.setLastUpdatedVersion(HomeActivity.this, latestVersion);
                    return true;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean response) {
            super.onPostExecute(response);
            if (pDialog.isShowing())
				try {
					pDialog.dismiss();
				} catch (Exception e) {
					e.printStackTrace();
				}

            startNextActivity();
        }
    }



    private void insertHelpDataToLocalDB(JSONArray dataArray) {
        List<HelpPage> pageList = HelpPage.parseHelpPages(dataArray);

        PBDatabase dbInstance = new PBDatabase(HomeActivity.this);
        dbInstance.open();

        for (int i = 0; i < pageList.size(); i++) {
            dbInstance.insertOrUpdateHelpPage(pageList.get(i));
        }
        dbInstance.close();
    }


    private void insertMobileDataToLocalDB(JSONArray dataArray) {
        List<Page> pageList = Page.parsePages(dataArray);

        PBDatabase dbInstance = new PBDatabase(HomeActivity.this);
        dbInstance.open();

        for (int i = 0; i < pageList.size(); i++) {
            dbInstance.insertOrUpdatePage(pageList.get(i));
        }
        dbInstance.close();
    }

    public String loadJSONFromAsset(String jsonFileName) {
        String json = null;
        try {
            InputStream is = getAssets().open(jsonFileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

}