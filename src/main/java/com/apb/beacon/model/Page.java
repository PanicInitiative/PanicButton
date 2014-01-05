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
public class Page {
    private String id;
    private String lang;
    private String type;
    private String title;
    private String introduction;
    private String warning;
    private String component;
    private List<PageStatus> status;
    private List<PageAction> action;
    private List<PageItem> items;
    private String content;

    public Page() {
    }

    public Page(String id, String lang, String type, String title, String introduction, String warning, String component, String content) {
        this.id = id;
        this.lang = lang;
        this.type = type;
        this.title = title;
        this.introduction = introduction;
        this.warning = warning;
        this.component = component;
        this.content = content;
    }

    public Page(String id, String lang, String type, String title, String introduction, String warning, String component,
                List<PageStatus> status, List<PageAction> action, List<PageItem> items, String content) {
        this.id = id;
        this.lang = lang;
        this.type = type;
        this.title = title;
        this.introduction = introduction;
        this.warning = warning;
        this.component = component;
        this.status = status;
        this.action = action;
        this.items = items;
        this.content = content;
    }

    public static List<Page> parsePages(JSONArray pageArray){
        List<Page> pageList = new ArrayList<Page>();

        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();

        try {
            for(int i=0; i < pageArray.length(); i++){

                JSONObject thisItem = pageArray.getJSONObject(i);
                if(thisItem != null){
                    String jsonString = thisItem.toString();
                    Page page = gson.fromJson(jsonString, Page.class);
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

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getWarning() {
        return warning;
    }

    public void setWarning(String warning) {
        this.warning = warning;
    }

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public List<PageStatus> getStatus() {
        return status;
    }

    public void setStatus(List<PageStatus> status) {
        this.status = status;
    }

    public List<PageAction> getAction() {
        return action;
    }

    public void setAction(List<PageAction> action) {
        this.action = action;
    }

    public List<PageItem> getItems() {
        return items;
    }

    public void setItems(List<PageItem> items) {
        this.items = items;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
