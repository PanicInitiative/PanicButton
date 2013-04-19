package com.amnesty.panicbutton.location;

import android.location.Location;

public class LocationFormatter {
    public static final String GOOGLE_MAP_URL = "http://maps.google.com/maps?q=";
    public static final String URL_PREFIX = ". I'm at ";
    public static final String VIA = " via ";

    private Location location;

    public LocationFormatter(Location location) {
        this.location = location;
    }

    public String format() {
        if (location != null) {
            String provider = location.getProvider();
            double longitude = location.getLongitude();
            double latitude = location.getLatitude();
            return URL_PREFIX + GOOGLE_MAP_URL + latitude + "," + longitude + VIA + provider;
        }
        return "";
    }
}
