package org.iilab.pb.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aoe on 1/16/14.
 */
public class PageChecklist {

    private String title;
    private String link;

    public PageChecklist() {
    }

    public PageChecklist(String title, String link) {
        this.title = title;
        this.link = link;
    }

    public static List<PageChecklist> parseChecklist(JSONArray clArray){
        List<PageChecklist> checkList = new ArrayList<PageChecklist>();

        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();

        try {
            for(int i=0; i < clArray.length(); i++){

                JSONObject thisItem = clArray.getJSONObject(i);
                if(thisItem != null){
                    String jsonString = thisItem.toString();
                    PageChecklist item = gson.fromJson(jsonString, PageChecklist.class);
                    checkList.add(item);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return checkList;
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
