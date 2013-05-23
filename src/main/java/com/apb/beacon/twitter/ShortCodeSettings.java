package com.apb.beacon.twitter;

public class ShortCodeSettings {
    private String country;
    private String serviceProvider;
    private String shortCode;

    public ShortCodeSettings(String country) {
        this.country = country;
    }

    public ShortCodeSettings(String country, String serviceProvider, String shortCode) {
        this.country = country;
        this.serviceProvider = serviceProvider;
        this.shortCode = shortCode;
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

    public String getServiceProvider() {
        return serviceProvider;
    }

    public String getShortCode() {
        return shortCode;
    }
}