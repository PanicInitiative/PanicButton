package com.apb.beacon.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aoe on 12/24/13.
 */
public class WizardPageItem {
    private String title;
    private String link;

    public WizardPageItem() {
    }

    public WizardPageItem(String title, String link) {
        this.title = title;
        this.link = link;
    }

    public static List<WizardPageItem> parsePageItems(JSONArray itemArray){
        List<WizardPageItem> itemList = new ArrayList<WizardPageItem>();

        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();

        try {
            for(int i=0; i < itemArray.length(); i++){

                JSONObject thisItem = itemArray.getJSONObject(i);
                if(thisItem != null){
                    String jsonString = thisItem.toString();
                    WizardPageItem item = gson.fromJson(jsonString, WizardPageItem.class);
                    itemList.add(item);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return itemList;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
