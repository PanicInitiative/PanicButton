package org.iilab.pb.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import org.iilab.pb.MainActivity;
import org.iilab.pb.R;
import org.iilab.pb.WizardActivity;
import org.iilab.pb.model.PageAction;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Locale;

import static org.iilab.pb.common.AppConstants.DEFAULT_CONFIRMATION_MESSAGE;
import static org.iilab.pb.common.AppConstants.FROM_WIZARD_ACTIVITY;
import static org.iilab.pb.common.AppConstants.JSON_ARRAY_DATA;
import static org.iilab.pb.common.AppConstants.JSON_EXTENSION;
import static org.iilab.pb.common.AppConstants.JSON_OBJECT_HELP;
import static org.iilab.pb.common.AppConstants.JSON_OBJECT_MOBILE;
import static org.iilab.pb.common.AppConstants.PAGE_HOME_NOT_CONFIGURED;
import static org.iilab.pb.common.AppConstants.PAGE_HOME_NOT_CONFIGURED_ALARM;
import static org.iilab.pb.common.AppConstants.PAGE_HOME_NOT_CONFIGURED_DISGUISE;
import static org.iilab.pb.common.AppConstants.PAGE_HOME_READY;
import static org.iilab.pb.common.AppConstants.PAGE_ID;
import static org.iilab.pb.common.AppConstants.PREFIX_HELP_DATA;
import static org.iilab.pb.common.AppConstants.PREFIX_MOBILE_DATA;
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
import static org.iilab.pb.common.ApplicationSettings.getWizardState;
import static org.iilab.pb.common.ApplicationSettings.isAppUpdated;
import static org.iilab.pb.common.ApplicationSettings.setSelectedLanguage;


/**
 * Created by aoe on 2/25/14.
 */
public class PageLanguageSettingsAdapter extends ArrayAdapter<PageAction> {

    private Context mContext;
    private LayoutInflater mInflater;
    private ProgressDialog pDialog;
    private String currentLang;
    private String selectedLang;
    private int lastUpdatedVersion;
    private int latestVersion;
    private int parentActivity;
    private static final String TAG = PageLanguageSettingsAdapter.class.getName();


    public PageLanguageSettingsAdapter(Context context, int parentActivity) {

        super(context, R.layout.row_page_language_settings);
        this.mContext = context;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.currentLang = getSelectedLanguage(mContext);
        latestVersion = -1;
        lastUpdatedVersion = getLastUpdatedVersion(mContext);
        this.parentActivity = parentActivity;
    }

    private static class ViewHolder {
        Button bAction;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.row_page_language_settings, null);
            holder = new ViewHolder();
            holder.bAction = (Button) convertView.findViewById(R.id.b_action);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final PageAction item = getItem(position);
        holder.bAction.setText(item.getTitle());
        holder.bAction.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String url = null;
                selectedLang = item.getLanguage();

                if (currentLang.equals(selectedLang)) {
                    Toast.makeText(mContext,"Language already applied.", Toast.LENGTH_SHORT);

                    /* why we need to restart app here?
                    we are finishing this fragment & activity just below, so it will go to previous activity's onResume.
                    As we are not handling the scenario explicitly in Main/Wizard activity's onResume method that what will happen
                    when we return from language-fragment, we do a restart app so that the flow will go to home-ready page automatically after the restart.
                     */
                    restartApp();

                    ((Activity) mContext).finish();
                    return;
                }
//                else if (!AppUtil.hasInternet(mContext)) {
//                    changeStaticLanguageSettings(((item.getConfirmation() == null) ? DEFAULT_CONFIRMATION_MESSAGE : item.getConfirmation()));
//                    return;
//                }
                changeStaticLanguageSettings(((item.getConfirmation() == null) ? DEFAULT_CONFIRMATION_MESSAGE : item.getConfirmation()));
//                new GetLatestVersion(((item.getConfirmation() == null) ? DEFAULT_CONFIRMATION_MESSAGE : item.getConfirmation())).execute();

            }
        });

        return convertView;
    }

    public void restartApp() {

        int wizardState = getWizardState(mContext);
        String pageId = null;
        if (wizardState == WIZARD_FLAG_HOME_NOT_CONFIGURED) {
            pageId = PAGE_HOME_NOT_CONFIGURED;
        } else if (wizardState == WIZARD_FLAG_HOME_NOT_CONFIGURED_ALARM) {
            pageId = PAGE_HOME_NOT_CONFIGURED_ALARM;
        } else if (wizardState == WIZARD_FLAG_HOME_NOT_CONFIGURED_DISGUISE) {
            pageId = PAGE_HOME_NOT_CONFIGURED_DISGUISE;
        } else if (wizardState == WIZARD_FLAG_HOME_READY) {
            pageId = PAGE_HOME_READY;
        }
        Log.d(TAG, "restarting app with pageId = " + pageId);

        if (parentActivity == FROM_WIZARD_ACTIVITY) {
            Intent i = new Intent(mContext, WizardActivity.class);
            i.putExtra(PAGE_ID, pageId);
            mContext.startActivity(i);

            ((WizardActivity) mContext).callFinishActivityReceiver();
        } else {
            Intent i = new Intent(mContext, MainActivity.class);
            i.putExtra(PAGE_ID, pageId);
            mContext.startActivity(i);

            ((MainActivity) mContext).callFinishActivityReceiver();
        }

        ((Activity) mContext).finish();
    }

    public void setData(List<PageAction> actionList) {
        clear();
        if (actionList != null) {
            for (int i = 0; i < actionList.size(); i++) {
                add(actionList.get(i));
            }
        }
    }

    private void changeStaticLanguageSettings(String confirmation) {
        Toast.makeText(mContext, confirmation, Toast.LENGTH_SHORT).show();
        setSelectedLanguage(mContext, selectedLang);

        Resources res = mContext.getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        android.content.res.Configuration conf = res.getConfiguration();

        loadLanguageData(selectedLang);

        conf.locale = new Locale(selectedLang);
        res.updateConfiguration(conf, dm);
    }


    private void loadLanguageData(String language) {
        //Only load language data if it doesn't exists locally
        if (!isLanguageDataExists(mContext, language)|| isAppUpdated(mContext) ) {
            new LoadLanguageData().execute();
            addDBLoadedLanguage(mContext, language);
        } else {
            restartApp();
        }
    }

    private class LoadLanguageData extends AsyncTask<Void, Void, Boolean> {
        int lastUpdatedVersion;

        @Override
        protected void onPreExecute() {

            pDialog = ProgressDialog.show(mContext, "Application", "Loading...", true, false);
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            Log.i(TAG, "starting loading of json files in background thread");
            String dataFileName = PREFIX_MOBILE_DATA + selectedLang + JSON_EXTENSION;
            String helpFileName = PREFIX_HELP_DATA + selectedLang + JSON_EXTENSION;
            Log.d(TAG, "selected language is " + getSelectedLanguage(mContext));
            Log.d(TAG, "Loading mobile data ");
            try {
                JSONObject jsonObj = new JSONObject(loadJSONFromAsset(dataFileName, mContext));
                JSONObject mobileObj = jsonObj.getJSONObject(JSON_OBJECT_MOBILE);
                lastUpdatedVersion = mobileObj.getInt(VERSION);
                JSONArray dataArray = mobileObj.getJSONArray(JSON_ARRAY_DATA);
                insertMobileDataToLocalDB(dataArray, mContext);
            } catch (JSONException jsonException) {
                Log.e(TAG, "Exception in reading" + dataFileName + " from asset" + jsonException.getMessage());
                jsonException.printStackTrace();
            }

            try {
                Log.d(TAG, "Loading help data ");
                JSONObject jsonObj = new JSONObject(loadJSONFromAsset(helpFileName, mContext));
                JSONObject mobileObj = jsonObj.getJSONObject(JSON_OBJECT_HELP);

                JSONArray dataArray = mobileObj.getJSONArray(JSON_ARRAY_DATA);
                insertMobileDataToLocalDB(dataArray, mContext);
            } catch (JSONException jsonException) {
                Log.e(TAG, "Exception in reading " + helpFileName + "  from asset" + jsonException.getMessage());
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
            restartApp();

        }
    }
}
