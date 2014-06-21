package org.iilab.pb.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aoe on 2/28/14.
 */
public class HelpPage {
    private String id;
    private String lang;
    private String type;
    private String title;
    private String heading;
//    private String categories;
    private String section_order;
//    private String alert;
//    private String toc;
    private String content;
//    private HelpImage image;
    private List<PageItem> items;


    public HelpPage() {
    }

    public HelpPage(String id, String lang, String type, String title, String heading, String section_order,
                    String content, List<PageItem> items) {
        this.id = id;
        this.lang = lang;
        this.type = type;
        this.title = title;
        this.heading = heading;
        this.section_order = section_order;
        this.content = content;
        this.items = items;
    }

    public static List<HelpPage> parseHelpPages(JSONArray pageArray){
        List<HelpPage> pageList = new ArrayList<HelpPage>();

        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();

        try {
            for(int i=0; i < pageArray.length(); i++){

                JSONObject thisPage = pageArray.getJSONObject(i);
                if(thisPage != null){
                    String jsonString = thisPage.toString();
                    HelpPage page = gson.fromJson(jsonString, HelpPage.class);
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

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public String getSectionOrder() {
        return section_order;
    }

    public void setSectionOrder(String section_order) {
        this.section_order = section_order;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<PageItem> getItems() {
        return items;
    }

    public void setItems(List<PageItem> items) {
        this.items = items;
    }
}
