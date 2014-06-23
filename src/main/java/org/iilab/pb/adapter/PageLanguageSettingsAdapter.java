package org.iilab.pb.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
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
import org.iilab.pb.common.AppConstants;
import org.iilab.pb.common.AppUtil;
import org.iilab.pb.common.ApplicationSettings;
import org.iilab.pb.model.PageAction;

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
                    AppUtil.showToast("Language already applied.", Toast.LENGTH_SHORT, mContext);

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
//                    changeStaticLanguageSettings(((item.getConfirmation() == null) ? AppConstants.DEFAULT_CONFIRMATION_MESSAGE : item.getConfirmation()));
//                    return;
//                }
                changeStaticLanguageSettings(((item.getConfirmation() == null) ? AppConstants.DEFAULT_CONFIRMATION_MESSAGE : item.getConfirmation()));
//                new GetLatestVersion(((item.getConfirmation() == null) ? AppConstants.DEFAULT_CONFIRMATION_MESSAGE : item.getConfirmation())).execute();

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

        Log.e(">>>>>>>", "restarting app with pageId = " + pageId);

        if(parentActivity == AppConstants.FROM_WIZARD_ACTIVITY){
            Intent i = new Intent(mContext, WizardActivity.class);
            i.putExtra("page_id", pageId);
            mContext.startActivity(i);

            ((WizardActivity) mContext).callFinishActivityReceiver();
        } else{
            Intent i = new Intent(mContext, MainActivity.class);
            i.putExtra("page_id", pageId);
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


//    private class GetLatestVersion extends AsyncTask<Void, Void, Boolean> {
//
//        private String confirmationMsg;
//
//        private GetLatestVersion(String confirmationMsg) {
//            this.confirmationMsg = confirmationMsg;
//        }
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            pDialog = ProgressDialog.show(mContext, "Panic Button", "Checking for updates...", true, false);
//        }
//
//        @Override
//        protected Boolean doInBackground(Void... params) {
//
//            String url = AppConstants.BASE_URL + AppConstants.VERSION_CHECK_URL;
//            JsonParser jsonParser = new JsonParser();
//            Log.d(">>>><<<<", "attempting to retrieve server-response for url = " + url);
//            ServerResponse response = jsonParser.retrieveServerData(AppConstants.HTTP_REQUEST_TYPE_GET, url, null, null, null);
//            if (response.getStatus() == 200) {
//                try {
//                    JSONObject responseObj = response.getjObj();
//                    latestVersion = responseObj.getInt("version");
//                    Log.e("??????", "latest version = " + latestVersion);
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
//            if (!response) {
//                if (pDialog.isShowing())
//                    pDialog.dismiss();
//                Toast.makeText(mContext, "App content couldn't be updated for the selected language. Please try again.", Toast.LENGTH_SHORT).show();
//            } else {
//                if (latestVersion > lastUpdatedVersion) {
//                    new GetMobileDataUpdate(confirmationMsg).execute();
//                } else {
//                    if (pDialog.isShowing())
//                        pDialog.dismiss();
//                    changeStaticLanguageSettings(confirmationMsg);
//                }
//            }
//        }
//    }

    private void changeStaticLanguageSettings(String confirmation) {
        Toast.makeText(mContext, confirmation, Toast.LENGTH_SHORT).show();
        ApplicationSettings.setSelectedLanguage(mContext, selectedLang);

        Resources res = mContext.getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        android.content.res.Configuration conf = res.getConfiguration();

        conf.locale = new Locale(selectedLang);
        res.updateConfiguration(conf, dm);

        restartApp();
    }


//    private class GetMobileDataUpdate extends AsyncTask<Void, Void, Boolean> {
//
//        String confirmationMsg;
//
//        private GetMobileDataUpdate(String confirmationMsg) {
//            this.confirmationMsg = confirmationMsg;
//        }
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//        }
//
//        @Override
//        protected Boolean doInBackground(Void... params) {
//
//            int version = 0;
//            String mobileDataUrl;
//            for (version = lastUpdatedVersion + 1; version <= latestVersion; version++) {
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
//                        JSONObject mobObj = responseObj.getJSONObject("mobile");
//                        JSONArray dataArray = mobObj.getJSONArray("data");
//                        insertMobileDataToLocalDB(dataArray);
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                        return false;
//                    }
//                }
//            }
//
//            if (version > latestVersion) {
//                return true;
//            } else {
//                return false;
//            }
//        }
//
//        @Override
//        protected void onPostExecute(Boolean response) {
//            super.onPostExecute(response);
//
//            if (pDialog.isShowing())
//                pDialog.dismiss();
//            if (!response) {
//                Toast.makeText(mContext, "App content couldn't be updated for the selected language. Please try again.", Toast.LENGTH_SHORT).show();
//            } else {
//                changeStaticLanguageSettings(confirmationMsg);
//            }
//        }
//    }


//    private void insertMobileDataToLocalDB(JSONArray dataArray) {
//        List<Page> pageList = Page.parsePages(dataArray);
//
//        PBDatabase dbInstance = new PBDatabase(mContext);
//        dbInstance.open();
//
//        for (int i = 0; i < pageList.size(); i++) {
//            dbInstance.insertOrUpdatePage(pageList.get(i));
//        }
//        dbInstance.close();
//    }


}
