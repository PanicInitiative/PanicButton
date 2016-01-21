package org.iilab.pb.location;


import android.location.Location;

public class LocationTestUtil {
    public static Location location(String provider, double latitude, double longitude, long time, float accuracy) {
        Location location = new Location(provider);
        location.setLatitude(latitude);
        location.setLongitude(longitude);
        location.setTime(time);
        location.setAccuracy(accuracy);
        return location;
    }
}
