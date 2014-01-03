package com.apb.beacon.model;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aoe on 1/3/14.
 */
public class WizardSimplePage {

    private String id;
    private String lang;
    private String type;
    private String title;
    //    private String introduction;
//    private String warning;
//    private String component;
    private List<WizardPageAction> action;
    private List<WizardPageItem> items;
    private String content;

    public WizardSimplePage() {
    }

    public WizardSimplePage(String id, String lang, String type, String title, List<WizardPageAction> action, List<WizardPageItem> items, String content) {
        this.id = id;
        this.lang = lang;
        this.type = type;
        this.title = title;
        this.action = action;
        this.items = items;
        this.content = content;
    }


    public static List<WizardSimplePage> parsePages(JSONArray pageArray) {
        List<WizardSimplePage> pageList = new ArrayList<WizardSimplePage>();

        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();

        try {
            for (int i = 0; i < pageArray.length(); i++) {

                JSONObject thisItem = pageArray.getJSONObject(i);
                if (thisItem != null) {
                    String jsonString = thisItem.toString();
                    Log.e(">>>>>>>>", "thisString = " + jsonString);
                    WizardSimplePage page = gson.fromJson(jsonString, WizardSimplePage.class);
                    pageList.add(page);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return pageList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<WizardPageAction> getAction() {
        return action;
    }

    public void setAction(List<WizardPageAction> action) {
        this.action = action;
    }

    public List<WizardPageItem> getItems() {
        return items;
    }

    public void setItems(List<WizardPageItem> items) {
        this.items = items;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
