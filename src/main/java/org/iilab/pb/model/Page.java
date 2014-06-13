package org.iilab.pb.model;

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
    private PageTimer timers;
    private String successId;
    private String failedId;
    private List<PageChecklist> checklist;
    private String heading;
    private String section_order;

    public Page() {
    }

    public Page(String id, String lang, String type, String title, String introduction, String warning, String component, List<PageStatus> status,
                List<PageAction> action, List<PageItem> items, String content, PageTimer timers, String successId, String failedId,
                List<PageChecklist> checklist, String heading, String section_order) {
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
        this.timers = timers;
        this.successId = successId;
        this.failedId = failedId;
        this.checklist = checklist;
        this.heading = heading;
        this.section_order = section_order;
    }

    public static List<Page> parsePages(JSONArray pageArray){
        List<Page> pageList = new ArrayList<Page>();

        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();

        try {
            for(int i=0; i < pageArray.length(); i++){

                JSONObject thisPage = pageArray.getJSONObject(i);
                if(thisPage != null){
                    String jsonString = thisPage.toString();
                    Page page = gson.fromJson(jsonString, Page.class);

                    JSONObject successObj = thisPage.optJSONObject("success");
                    if(successObj != null){
                        String successId = successObj.optString("link");
                        page.setSuccessId(successId);
                    }

                    JSONObject failObj = thisPage.optJSONObject("fail");
                    if(failObj != null){
                        String failId = failObj.optString("link");
                        page.setFailedId(failId);
                    }

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

    public PageTimer getTimers() {
        return timers;
    }

    public void setTimers(PageTimer timers) {
        this.timers = timers;
    }

    public String getSuccessId() {
        return successId;
    }

    public void setSuccessId(String successId) {
        this.successId = successId;
    }

    public String getFailedId() {
        return failedId;
    }

    public void setFailedId(String failedId) {
        this.failedId = failedId;
    }

    public List<PageChecklist> getChecklist() {
        return checklist;
    }

    public void setChecklist(List<PageChecklist> checklist) {
        this.checklist = checklist;
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
}
