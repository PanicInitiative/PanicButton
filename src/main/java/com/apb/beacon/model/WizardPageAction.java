package com.apb.beacon.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

/**
 * Created by aoe on 12/24/13.
 */
public class WizardPageAction {
    private String title;
    private String link;

    public WizardPageAction() {
    }

    public WizardPageAction(String title, String link) {
        this.title = title;
        this.link = link;
    }

    public static WizardPageAction parsePageAction(JSONObject actionObj) {

        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();

        if (actionObj != null) {
            String jsonString = actionObj.toString();
            WizardPageAction action = gson.fromJson(jsonString, WizardPageAction.class);
            return action;
        }

        return null;
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
