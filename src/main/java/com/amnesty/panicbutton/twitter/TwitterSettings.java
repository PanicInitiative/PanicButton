package com.amnesty.panicbutton.twitter;

public class TwitterSettings {
    private String country;
    private String serviceProvider;
    private String shortCode;

    public TwitterSettings(String country) {
        this.country = country;
    }

    public String getCountry() {
        return country;
    }

    public void setShortCode(String shortCode) {
        this.shortCode = shortCode;
    }

    public void setServiceProvider(String serviceProvider) {
        this.serviceProvider = serviceProvider;
    }
}
