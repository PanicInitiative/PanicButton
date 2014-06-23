package org.iilab.pb.location;

import android.content.Context;
import android.location.Location;

import org.iilab.pb.R;

public class LocationFormatter {
    public static final String GOOGLE_MAP_URL = "https://maps.google.com/maps?q=";
//    public static final String GOOGLE_MAP_URL =    "https://maps.google.com/maps?q=https:%2F%2Floc_panicbutton.iilab.org%2F%3Flat%3D33.66121%26long%3D-95.563889%26rad%3D100000"
//    public static final String IAMAT = "I'm at";
    public static final String VIA = " via ";

    private Location location;

    public LocationFormatter(Location location) {
        this.location = location;
    }

    public String format(Context context) {
        if (location != null) {
            String provider = location.getProvider();
            double longitude = location.getLongitude();
            double latitude = location.getLatitude();
            return context.getResources().getString(R.string.i_am_at) + " " + GOOGLE_MAP_URL + latitude + "," + longitude + VIA + provider;
//            double radius = (location.getAccuracy() != 0) ? location.getAccuracy() : 1000;
//            return ". " + getString(R.string.i_am_at) + " " + GOOGLE_MAP_URL + "%2F%3Flat%3D33.66121" + latitude + "%26long%3D-95.563889" + longitude + "%26rad%3D" + radius + " " VIA + provider;

        }

        return "";
    }
}
