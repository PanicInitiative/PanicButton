package com.apb.beacon.model;

/**
 * Created by aoe on 12/12/13.
 */
public class LocalCachePage {
    private int pageNumber;
    private String pageName;
    private String pageTitle;
    private String pageAction;
    private String pageOption;
    private String pageContent;

    public LocalCachePage() {
    }

    public LocalCachePage(int pageNumber, String pageName, String pageTitle, String pageAction, String pageOption, String pageContent) {
        this.pageNumber = pageNumber;
        this.pageName = pageName;
        this.pageTitle = pageTitle;
        this.pageAction = pageAction;
        this.pageOption = pageOption;
        this.pageContent = pageContent;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public String getPageName() {
        return pageName;
    }

    public void setPageName(String pageName) {
        this.pageName = pageName;
    }

    public String getPageTitle() {
        return pageTitle;
    }

    public void setPageTitle(String pageTitle) {
        this.pageTitle = pageTitle;
    }

    public String getPageAction() {
        return pageAction;
    }

    public void setPageAction(String pageAction) {
        this.pageAction = pageAction;
    }

    public String getPageOption() {
        return pageOption;
    }

    public void setPageOption(String pageOption) {
        this.pageOption = pageOption;
    }

    public String getPageContent() {
        return pageContent;
    }

    public void setPageContent(String pageContent) {
        this.pageContent = pageContent;
    }
}
