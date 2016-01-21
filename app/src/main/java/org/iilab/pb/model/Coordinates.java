package org.iilab.pb.model;

/**
 * Created by aoe on 5/29/14.
 */
public class Coordinates {

    public static final Coordinates UNDEFINED = new Coordinates(0.000, 0.000);
    private double latitude;
    private double longitude;

    public Coordinates(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
