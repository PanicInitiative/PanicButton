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
public class WizardPageAction {
    private String title;
    private String link;

    public WizardPageAction() {
    }

    public WizardPageAction(String title, String link) {
        this.title = title;
        this.link = link;
    }

    public static List<WizardPageAction> parsePageItems(JSONArray actionArray){
        List<WizardPageAction> actionList = new ArrayList<WizardPageAction>();

        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();

        try {
            for(int i=0; i < actionArray.length(); i++){

                JSONObject thisActionItem = actionArray.getJSONObject(i);
                if(thisActionItem != null){
                    String jsonString = thisActionItem.toString();
                    WizardPageAction item = gson.fromJson(jsonString, WizardPageAction.class);
                    actionList.add(item);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return actionList;
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
