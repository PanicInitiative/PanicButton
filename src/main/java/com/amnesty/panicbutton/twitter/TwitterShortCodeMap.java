package com.amnesty.panicbutton.twitter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TwitterShortCodeMap {
    private String country;
    private Map<String, String> serviceProviders = new HashMap<String, String>();

    public TwitterShortCodeMap(String country, Map<String, String> serviceProviders) {
        this.country = country;
        this.serviceProviders = serviceProviders;
    }

    public List<String> getServiceProviders() {
        return new ArrayList<String>(serviceProviders.keySet());
    }

    public String getShortCode(String selectedServiceProvider) {
        return serviceProviders.get(selectedServiceProvider);
    }
}
