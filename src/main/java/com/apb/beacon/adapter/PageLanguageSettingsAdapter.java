package com.apb.beacon.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import com.apb.beacon.AppConstants;
import com.apb.beacon.ApplicationSettings;
import com.apb.beacon.R;
import com.apb.beacon.data.PBDatabase;
import com.apb.beacon.model.Page;
import com.apb.beacon.model.PageAction;
import com.apb.beacon.model.ServerResponse;
import com.apb.beacon.parser.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by aoe on 2/25/14.
 */
public class PageLanguageSettingsAdapter extends ArrayAdapter<PageAction> {

    private Context mContext;
    private LayoutInflater mInflater;
    private ProgressDialog pDialog;
    private String selectedLang;

    public PageLanguageSettingsAdapter(Context context, List<PageAction> actionList) {
        super(context, R.layout.row_page_language_settings);
        this.mContext = context;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.selectedLang = ApplicationSettings.getSelectedLanguage(mContext);
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
                if(selectedLang.equals("en")){
                    url = AppConstants.BASE_ENGLISH_URL;
                }else if(selectedLang.equals("es")){
                    url = AppConstants.BASE_SPANISH_URL;
                } else if(selectedLang.equals("ph")){
                    url = AppConstants.BASE_FILIPINO_URL;
                }
                new GetLanguageUpdate().execute(url);
            }
        });

        return convertView;
    }


    public void setData(List<PageAction> actionList) {
        clear();
        if (actionList != null) {
            for (int i = 0; i < actionList.size(); i++) {
                add(actionList.get(i));
            }
        }
    }


    private class GetLanguageUpdate extends AsyncTask<String, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = ProgressDialog.show(mContext, "Panic Button", "Checking for updates...", true, false);
        }

        @Override
        protected Boolean doInBackground(String... params) {
            String url = params[0];
            JsonParser jsonParser = new JsonParser();
            ServerResponse response = jsonParser.retrieveServerData(AppConstants.HTTP_REQUEST_TYPE_GET, url, null, null, null);
            if (response.getStatus() == 200) {
                Log.d(">>>><<<<", "success in retrieving update of new language = " + url);
                ApplicationSettings.setLastRunTimeInMillis(mContext, System.currentTimeMillis());          // if we can retrieve a single data, we change it up-to-date

                try {
                    JSONObject responseObj = response.getjObj();
                    JSONObject mobObj = responseObj.getJSONObject("mobile");
                    JSONArray dataArray = mobObj.getJSONArray("data");
                    insertJsonDataToLocalDB(dataArray);
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
                pDialog.dismiss();

            if(!response){
                Toast.makeText(mContext, "Language content couldn't be updated. Please try again.", Toast.LENGTH_SHORT).show();
            } else{
                Toast.makeText(mContext, "New language applied.", Toast.LENGTH_SHORT).show();
                ApplicationSettings.setSelectedLanguage(mContext, selectedLang);
                ((Activity)mContext).finish();
            }
        }
    }


    private void insertJsonDataToLocalDB(JSONArray dataArray){
        List<Page> pageList = Page.parsePages(dataArray);

        PBDatabase dbInstance = new PBDatabase(mContext);
        dbInstance.open();

        for(int i = 0; i< pageList.size(); i++){
            dbInstance.insertOrUpdatePage(pageList.get(i));
        }
        dbInstance.close();
    }

}
