package org.iilab.pb.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aoe on 1/5/14.
 */
public class PageStatus {
    private String title;
    private String color;
    private String link;

    public PageStatus() {
    }

    public PageStatus(String title, String color, String link) {
        this.title = title;
        this.color = color;
        this.link = link;
    }

    public static List<PageStatus> parseStatusList(JSONArray statusArray){
        List<PageStatus> statusList = new ArrayList<PageStatus>();

        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();

        try {
            for(int i = 0; i < statusArray.length(); i++){

                JSONObject thisStatusItem = statusArray.getJSONObject(i);
                if(thisStatusItem != null){
                    String jsonString = thisStatusItem.toString();
                    PageStatus status = gson.fromJson(jsonString, PageStatus.class);
                    statusList.add(status);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return statusList;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
