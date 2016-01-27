package org.iilab.pb;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.crashlytics.android.Crashlytics;

import org.iilab.pb.data.PBDatabase;
import org.iilab.pb.model.Page;
import org.iilab.pb.model.PageAction;
import org.iilab.pb.trigger.HardwareTriggerService;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.fabric.sdk.android.Fabric;

import static org.iilab.pb.common.AppConstants.DEFAULT_LANGUAGE_ENG;
import static org.iilab.pb.common.AppConstants.DELIMITER_COMMA;
import static org.iilab.pb.common.AppConstants.FRESH_INSTALL_APP_RELEASE_NO;
import static org.iilab.pb.common.AppConstants.JSON_ARRAY_DATA;
import static org.iilab.pb.common.AppConstants.JSON_EXTENSION;
import static org.iilab.pb.common.AppConstants.JSON_OBJECT_HELP;
import static org.iilab.pb.common.AppConstants.JSON_OBJECT_MOBILE;
import static org.iilab.pb.common.AppConstants.PAGE_HOME_NOT_CONFIGURED;
import static org.iilab.pb.common.AppConstants.PAGE_HOME_NOT_CONFIGURED_ALARM;
import static org.iilab.pb.common.AppConstants.PAGE_HOME_NOT_CONFIGURED_DISGUISE;
import static org.iilab.pb.common.AppConstants.PAGE_HOME_READY;
import static org.iilab.pb.common.AppConstants.PAGE_ID;
import static org.iilab.pb.common.AppConstants.PAGE_SETUP_LANGUAGE;
import static org.iilab.pb.common.AppConstants.PREFIX_HELP_DATA;
import static org.iilab.pb.common.AppConstants.PREFIX_MOBILE_DATA;
import static org.iilab.pb.common.AppConstants.SKIP_WIZARD;
import static org.iilab.pb.common.AppConstants.VERSION;
import static org.iilab.pb.common.AppConstants.WIZARD_FLAG_HOME_NOT_CONFIGURED;
import static org.iilab.pb.common.AppConstants.WIZARD_FLAG_HOME_NOT_CONFIGURED_ALARM;
import static org.iilab.pb.common.AppConstants.WIZARD_FLAG_HOME_NOT_CONFIGURED_DISGUISE;
import static org.iilab.pb.common.AppConstants.WIZARD_FLAG_HOME_READY;
import static org.iilab.pb.common.AppUtil.insertMobileDataToLocalDB;
import static org.iilab.pb.common.AppUtil.isLanguageDataExists;
import static org.iilab.pb.common.AppUtil.loadJSONFromAsset;
import static org.iilab.pb.common.ApplicationSettings.addDBLoadedLanguage;
import static org.iilab.pb.common.ApplicationSettings.getLastUpdatedVersion;
import static org.iilab.pb.common.ApplicationSettings.getSelectedLanguage;
import static org.iilab.pb.common.ApplicationSettings.getSupportedLanguages;
import static org.iilab.pb.common.ApplicationSettings.getWizardState;
import static org.iilab.pb.common.ApplicationSettings.isHardwareTriggerServiceEnabled;
import static org.iilab.pb.common.ApplicationSettings.setAppUpdated;
import static org.iilab.pb.common.ApplicationSettings.setFirstRun;
import static org.iilab.pb.common.ApplicationSettings.setLastUpdatedVersion;
import static org.iilab.pb.common.ApplicationSettings.setPBSupportedLanguages;
import static org.iilab.pb.common.ApplicationSettings.setSelectedLanguage;


public class HomeActivity extends Activity {

    ProgressDialog pDialog;

    String pageId;
    String selectedLang;
    int currentLocalContentVersion;
    int newLocalContentVersion;

    private static final String TAG = HomeActivity.class.getName();
    String supportedLangs;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.welcome_screen);
        //deleteShortCut();
        int wizardState = getWizardState(this);
        if (SKIP_WIZARD) {
            pageId = PAGE_HOME_READY;
        } else if (wizardState == WIZARD_FLAG_HOME_NOT_CONFIGURED) {

            pageId = PAGE_HOME_NOT_CONFIGURED;
        } else if (wizardState == WIZARD_FLAG_HOME_NOT_CONFIGURED_ALARM) {
            pageId = PAGE_HOME_NOT_CONFIGURED_ALARM;
        } else if (wizardState == WIZARD_FLAG_HOME_NOT_CONFIGURED_DISGUISE) {
            pageId = PAGE_HOME_NOT_CONFIGURED_DISGUISE;
        } else if (wizardState == WIZARD_FLAG_HOME_READY) {
            pageId = PAGE_HOME_READY;
        }
        setSelectedLanguage(this, getDefaultOSLanguage());
        selectedLang = getSelectedLanguage(this);
        /*
        lastLocalDBVersion is used for local db version update. If local db version is changed, then all local data will be deleted,
        tables will be reformed & database is blank. So at that point we will force local-data update from assets, then a retrieval-try
        from the remote database even if the data was retrieved within last 24-hours period.
         */

        // this is the version number from installed mobile_en.json
        currentLocalContentVersion = getLastUpdatedVersion(HomeActivity.this);

        try {
            JSONObject jsonObj = new JSONObject(loadJSONFromAsset("mobile_en.json", getApplicationContext()));
            JSONObject mobileObj = jsonObj.getJSONObject(JSON_OBJECT_MOBILE);
            //this is from new assets folder
            newLocalContentVersion = Integer.parseInt(mobileObj.getString(VERSION));
        } catch (JSONException | NumberFormatException exception) {
            Log.e(TAG, "Exception in reading mobile_en.json from asset" + exception.getMessage());
            exception.printStackTrace();
        }

		/* We update the device language content if the english mobile_en.json version has increased or
        * if the language of the device OS was previously not installed*/

        if ((newLocalContentVersion > currentLocalContentVersion) || (!isLanguageDataExists(getApplicationContext(), selectedLang))) {
            Log.d(TAG, "Update local data as the english mobile_en.json version has increased");
            // to check if the app is installed first time, -1 is the default value
            if(currentLocalContentVersion== FRESH_INSTALL_APP_RELEASE_NO){
                setFirstRun(getApplicationContext(),true);
                setAppUpdated(getApplicationContext(), false);
            }else {
                setAppUpdated(getApplicationContext(), true);
            }
            new InitializeLocalData().execute();
            addDBLoadedLanguage(getApplicationContext(), selectedLang);

        }
        else {
            Log.d(TAG, "no update of local data needed");
            startNextActivity();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private String getDefaultOSLanguage() {
        //	Default language of OS:
        return Locale.getDefault().getLanguage();
    }

    private void deleteShortCut() {

        Intent shortcutIntent = new Intent(Intent.ACTION_MAIN);
        shortcutIntent.setClassName("org.iilab.pb", "HomeActivity");
        shortcutIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        shortcutIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        Intent removeIntent = new Intent();
        removeIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
        removeIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "ShortcutName");
        removeIntent.putExtra("duplicate", false);

        removeIntent.setAction("com.android.launcher.action.UNINSTALL_SHORTCUT");
        sendBroadcast(removeIntent);
    }

    private void setSupportedLanguages(Page languagesPage) {
        List<String> allowedLanguages = new ArrayList<>();
        List<PageAction> actionLanguages = languagesPage.getAction();
        for (PageAction actionLanguage : actionLanguages) {
            allowedLanguages.add(actionLanguage.getLanguage());
        }
        supportedLangs = TextUtils.join(DELIMITER_COMMA, allowedLanguages);
        setPBSupportedLanguages(this, supportedLangs);
    }

    private void startNextActivity() {
        Log.d(TAG, "starting next activity");
        int wizardState = getWizardState(this);
        supportedLangs = getSupportedLanguages(this);
        Log.d(TAG, "Checking supported languages " + supportedLangs);
        if (null == supportedLangs) {
            PBDatabase dbInstance = new PBDatabase(this);
            dbInstance.open();
            Page languagesPage = dbInstance.retrievePage(PAGE_SETUP_LANGUAGE, DEFAULT_LANGUAGE_ENG);
            setSupportedLanguages(languagesPage);
            dbInstance.close();
        }
        if ((supportedLangs == null) || !(supportedLangs.contains(selectedLang))) {
            setSelectedLanguage(this, DEFAULT_LANGUAGE_ENG);
        }
        if (wizardState != WIZARD_FLAG_HOME_READY) {
            Log.d(TAG, "First run TRUE, running WizardActivity with pageId = " + pageId);
            Intent i = new Intent(HomeActivity.this, WizardActivity.class);
            i.putExtra(PAGE_ID, pageId);
            startActivity(i);
        } else {
            Log.d(TAG, "First run FALSE, running CalculatorActivity");
            Intent i = new Intent(HomeActivity.this, CalculatorActivity.class);
            // Make sure the HardwareTriggerService is started
            if(isHardwareTriggerServiceEnabled(this)) {
                startService(new Intent(this, HardwareTriggerService.class));
            }
            startActivity(i);
        }
    }

    private class InitializeLocalData extends AsyncTask<Void, Void, Boolean> {
        int lastUpdatedVersion;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = ProgressDialog.show(HomeActivity.this, "Application", "Installing...", true, false);
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            Log.d(TAG, "starting loading of json files in background thread");
            String dataFileName = PREFIX_MOBILE_DATA + selectedLang + JSON_EXTENSION;
            String helpFileName = PREFIX_HELP_DATA + selectedLang + JSON_EXTENSION;
            try {
                JSONObject jsonObj = new JSONObject(loadJSONFromAsset(dataFileName, getApplicationContext()));
                JSONObject mobileObj = jsonObj.getJSONObject(JSON_OBJECT_MOBILE);

                lastUpdatedVersion = mobileObj.getInt(VERSION);
                setLastUpdatedVersion(HomeActivity.this, lastUpdatedVersion);

                JSONArray dataArray = mobileObj.getJSONArray(JSON_ARRAY_DATA);
                insertMobileDataToLocalDB(dataArray, getApplicationContext());
            } catch (JSONException jsonException) {
                Log.e(TAG, "Exception in reading mobile_en.json from asset" + jsonException.getMessage());
                jsonException.printStackTrace();
            }

            try {
                JSONObject jsonObj = new JSONObject(loadJSONFromAsset(helpFileName, getApplicationContext()));
                JSONObject mobileObj = jsonObj.getJSONObject(JSON_OBJECT_HELP);

                JSONArray dataArray = mobileObj.getJSONArray(JSON_ARRAY_DATA);
                insertMobileDataToLocalDB(dataArray, getApplicationContext());
            } catch (JSONException jsonException) {
                Log.e(TAG, "Exception in reading help_en.json from asset" + jsonException.getMessage());
                jsonException.printStackTrace();
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
                    Log.e(TAG, "Exception while dismissing progress dialog " + e.getMessage());
                    e.printStackTrace();
                }

//            setLocalDataInsertion(HomeActivity.this, true);
//            setLastUpdatedDBVersion(HomeActivity.this, DATABASE_VERSION);

            startNextActivity();
        }
    }

//    private class GetLatestVersion extends AsyncTask<Void, Void, Boolean> {
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            pDialog = ProgressDialog.show(HomeActivity.this, "Application", "Starting...", true, false);
//        }
//
//        @Override
//        protected Boolean doInBackground(Void... params) {
//
//            String url = AppConstants.BASE_URL + AppConstants.VERSION_CHECK_URL;
//            JsonParser jsonParser = new JsonParser();
//            ServerResponse response = jsonParser.retrieveServerData(AppConstants.HTTP_REQUEST_TYPE_GET, url, null, null, null);
//            if (response.getStatus() == 200) {
//                try {
//                    JSONObject responseObj = response.getjObj();
//                    latestVersion = responseObj.getInt("version");
//                    Log.e("??????", "latest version = " + latestVersion + " last updated version = " + lastUpdatedVersion);
//                    return true;
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//            return false;
//        }
//
//        @Override
//        protected void onPostExecute(Boolean response) {
//            super.onPostExecute(response);
//
//            if (latestVersion > lastUpdatedVersion) {
//                new GetMobileDataUpdate().execute();
//            } else {
//                setLastRunTimeInMillis(HomeActivity.this, System.currentTimeMillis());
//                if (pDialog.isShowing())
//					try {
//						pDialog.dismiss();
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//                    startNextActivity();
//            }
//        }
//    }


//    private class GetMobileDataUpdate extends AsyncTask<Void, Void, Boolean> {
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            try {
//				pDialog = ProgressDialog.show(HomeActivity.this, "Application", "Downloading updates...", true, false);
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//       }
//
//        @Override
//        protected Boolean doInBackground(Void... params) {
//
//            int version = 0;
//            for(version = lastUpdatedVersion + 1; version <= latestVersion; version ++){
//                if (selectedLang.equals("en")) {
//                    mobileDataUrl = AppConstants.BASE_URL + "/api/mobile." + version + ".json";
//                } else {
//                    mobileDataUrl = AppConstants.BASE_URL + "/api/" + selectedLang + "/" + "mobile." + version + ".json";
//                }
//
//                JsonParser jsonParser = new JsonParser();
//                ServerResponse response = jsonParser.retrieveServerData(AppConstants.HTTP_REQUEST_TYPE_GET, mobileDataUrl, null, null, null);
//                if (response.getStatus() == 200) {
//                    Log.d(">>>><<<<", "success in retrieving server-response for url = " + mobileDataUrl);
//                    try {
//                        JSONObject responseObj = response.getjObj();
//                        JSONObject mobObj = responseObj.getJSONObject(JSON_OBJECT_MOBILE);
//                        JSONArray dataArray = mobObj.getJSONArray(JSON_ARRAY_DATA);
//                        insertMobileDataToLocalDB(dataArray);
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                        return false;
//                    }
//                }
//            }
//
//            if(version > latestVersion){
//                return true;
//            } else{
//                return false;
//            }
//        }
//
//        @Override
//        protected void onPostExecute(Boolean response) {
//            super.onPostExecute(response);
//
//            if(response){
//                new GetHelpDataUpdate().execute();
//            }
//            else{
//                if (pDialog.isShowing())
//                    pDialog.dismiss();
//
//                startNextActivity();
//            }
//        }
//    }


//    private class GetHelpDataUpdate extends AsyncTask<Void, Void, Boolean> {
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            try {
//				pDialog = ProgressDialog.show(HomeActivity.this, "Application", "Downloading help pages...", true, false);
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//        }
//
//        @Override
//        protected Boolean doInBackground(Void... params) {
//
//            JsonParser jsonParser = new JsonParser();
//            ServerResponse response = jsonParser.retrieveServerData(AppConstants.HTTP_REQUEST_TYPE_GET, helpDataUrl, null, null, null);
//            if (response.getStatus() == 200) {
//                Log.d(">>>><<<<", "success in retrieving server-response for url = " + helpDataUrl);
//                setLastRunTimeInMillis(HomeActivity.this, System.currentTimeMillis());          // if we can retrieve a single data, we change it up-to-date
//                try {
//                    JSONObject responseObj = response.getjObj();
//                    JSONObject mobObj = responseObj.getJSONObject(JSON_OBJECT_HELP);
//                    JSONArray dataArray = mobObj.getJSONArray(JSON_ARRAY_DATA);
//                    insertHelpDataToLocalDB(dataArray);
//                    setLastUpdatedVersion(HomeActivity.this, latestVersion);
//                    return true;
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//            return false;
//        }
//
//        @Override
//        protected void onPostExecute(Boolean response) {
//            super.onPostExecute(response);
//            if (pDialog.isShowing())
//				try {
//					pDialog.dismiss();
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//
//            startNextActivity();
//        }
//    }


//    private void insertHelpDataToLocalDB(JSONArray dataArray) {
//        List<HelpPage> pageList = HelpPage.parseHelpPages(dataArray);
//
//        PBDatabase dbInstance = new PBDatabase(HomeActivity.this);
//        dbInstance.open();
//
//        for (int i = 0; i < pageList.size(); i++) {
//            dbInstance.insertOrUpdateHelpPage(pageList.get(i));
//        }
//        dbInstance.close();
//    }


}