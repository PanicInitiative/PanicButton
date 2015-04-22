package org.iilab.pb.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

/**
 * Created by aoe on 2/28/14.
 */
public class HelpImage {
    private String title;
    private String caption;
    private String src;

    public HelpImage() {
    }

    public HelpImage(String title, String caption, String src) {
        this.title = title;
        this.caption = caption;
        this.src = src;
    }


    public static HelpImage parsePageImage(JSONObject imageObj){
        HelpImage image = new HelpImage();

        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();

        if(imageObj != null){
            String jsonString = imageObj.toString();
            image = gson.fromJson(jsonString, HelpImage.class);
        }
        return image;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }
}
