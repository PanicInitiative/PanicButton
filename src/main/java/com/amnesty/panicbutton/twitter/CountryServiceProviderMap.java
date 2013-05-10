package com.amnesty.panicbutton.twitter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CountryServiceProviderMap {
    private String country;
    private Map<String, String> serviceProviders = new HashMap<String, String>();

    public CountryServiceProviderMap(String country, Map<String, String> serviceProviderShortCodeMap) {
        this.country = country;
        this.serviceProviders = serviceProviderShortCodeMap;
    }

    public List<String> getServiceProviders() {
        return new ArrayList<String>(serviceProviders.keySet());
    }

    public String getShortCode(String selectedServiceProvider) {
        return serviceProviders.get(selectedServiceProvider);
    }
}
