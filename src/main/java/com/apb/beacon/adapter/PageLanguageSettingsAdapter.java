package com.apb.beacon.adapter;

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

import com.apb.beacon.common.AppConstants;
import com.apb.beacon.common.ApplicationSettings;
import com.apb.beacon.R;
import com.apb.beacon.common.AppUtil;
import com.apb.beacon.data.PBDatabase;
import com.apb.beacon.model.Page;
import com.apb.beacon.model.PageAction;
import com.apb.beacon.model.ServerResponse;
import com.apb.beacon.common.JsonParser;
import com.apb.beacon.WizardActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Locale;

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

    public PageLanguageSettingsAdapter(Context context, int parentActivity) {
        super(context, R.layout.row_page_language_settings);
        this.mContext = context;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.currentLang = ApplicationSettings.getSelectedLanguage(mContext);
        latestVersion = -1;
        lastUpdatedVersion = ApplicationSettings.getLastUpdatedVersion(mContext);
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
                    ((Activity) mContext).finish();
                	AppUtil.showToast("Language already applied.", Toast.LENGTH_SHORT, mContext);
                    return;
                }
                new GetLatestVersion().execute();
            }
        });

        return convertView;
    }

    public void restartApp(){

        int wizardState = ApplicationSettings.getWizardState(mContext);
        String pageId = null;
        if (wizardState == AppConstants.WIZARD_FLAG_HOME_NOT_CONFIGURED) {
            pageId = "home-not-configured";
        } else if (wizardState == AppConstants.WIZARD_FLAG_HOME_NOT_CONFIGURED_ALARM) {
            pageId = "home-not-configured-alarm";
        } else if (wizardState == AppConstants.WIZARD_FLAG_HOME_NOT_CONFIGURED_DISGUISE) {
            pageId = "home-not-configured-disguise";
        } else if (wizardState == AppConstants.WIZARD_FLAG_HOME_READY) {
            pageId = "home-ready";
        }

        Intent i = new Intent(mContext, WizardActivity.class);
        i.putExtra("page_id", pageId);
        mContext.startActivity(i);

        if(parentActivity == AppConstants.FROM_WIZARD_ACTIVITY){
            ((WizardActivity) mContext).callFinishActivityReceiver();
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


    private class GetLatestVersion extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = ProgressDialog.show(mContext, "Panic Button", "Checking for updates...", true, false);
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            String url = AppConstants.BASE_URL + AppConstants.VERSION_CHECK_URL;
            JsonParser jsonParser = new JsonParser();
            Log.d(">>>><<<<", "attempting to retrieve server-response for url = " + url);
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

            if (!response) {
                if (pDialog.isShowing())
                    pDialog.dismiss();
                Toast.makeText(mContext, "App content couldn't be updated for the selected language. Please try again.", Toast.LENGTH_SHORT).show();
            } else {
                if (latestVersion > lastUpdatedVersion) {
                    new GetMobileDataUpdate().execute();
                } else {
                    if (pDialog.isShowing())
                        pDialog.dismiss();
                    changeStaticLanguageSettings();
                }
            }
        }
    }

    private void changeStaticLanguageSettings() {
        Toast.makeText(mContext, "New language applied.", Toast.LENGTH_SHORT).show();
        ApplicationSettings.setSelectedLanguage(mContext, selectedLang);

        Resources res = mContext.getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        android.content.res.Configuration conf = res.getConfiguration();

        conf.locale = new Locale(selectedLang);
        res.updateConfiguration(conf, dm);

        restartApp();
    }


    private class GetMobileDataUpdate extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            int version = 0;
            String mobileDataUrl;
            for (version = lastUpdatedVersion + 1; version <= latestVersion; version++) {
                if (selectedLang.equals("en")) {
                    mobileDataUrl = AppConstants.BASE_URL + "/api/mobile." + version + ".json";
                } else {
                    mobileDataUrl = AppConstants.BASE_URL + "/api/" + selectedLang + "/" + "mobile." + version + ".json";
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

            if (version > latestVersion) {
                return true;
            } else {
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean response) {
            super.onPostExecute(response);

            if (pDialog.isShowing())
                pDialog.dismiss();
            if (!response) {
                Toast.makeText(mContext, "App content couldn't be updated for the selected language. Please try again.", Toast.LENGTH_SHORT).show();
            } else {
                changeStaticLanguageSettings();
            }
        }
    }


    private void insertMobileDataToLocalDB(JSONArray dataArray) {
        List<Page> pageList = Page.parsePages(dataArray);

        PBDatabase dbInstance = new PBDatabase(mContext);
        dbInstance.open();

        for (int i = 0; i < pageList.size(); i++) {
            dbInstance.insertOrUpdatePage(pageList.get(i));
        }
        dbInstance.close();
    }


}
