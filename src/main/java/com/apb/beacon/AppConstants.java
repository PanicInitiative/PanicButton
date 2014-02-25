package com.apb.beacon;

public class AppConstants {
    public static final int HAPTIC_FEEDBACK_DURATION = 3000;

    public static final float GPS_MIN_DISTANCE = 0;
    public static final long GPS_MIN_TIME = 1000 * 60 * 2;

    public static final float NETWORK_MIN_DISTANCE = 0;
    public static final long NETWORK_MIN_TIME = 1000 * 60 * 2;

    public static final long ALERT_FREQUENCY = 1000 * 60 * 5;

    public static final int WARNING_TRAINING_MESSAGE_MINIMUM_CHARACTER = 30;

    public static final int HTTP_REQUEST_TYPE_GET = 1;
    public static final int HTTP_REQUEST_TYPE_POST = 2;
    public static final int HTTP_REQUEST_TYPE_PUT = 3;

    public static final int WIZARD_FLAG_HOME_NOT_COMPLETED = 601;
    public static final int WIZARD_FLAG_HOME_NOT_CONFIGURED_ALARM = 602;
    public static final int WIZARD_FLAG_HOME_NOT_CONFIGURED_DISGUISE = 603;
    public static final int WIZARD_FLAG_HOME_READY = 604;
    public static final int WIZARD_FLAG_COMPLETE = 605;


    public static boolean WIZARD_IS_BACK_BUTTON_PRESSED = false;

    public static final int FROM_WIZARD_ACTIVITY = 1;
    public static final int FROM_MAIN_ACTIVITY = 2;

    public static final String TABLE_PRIMARY_KEY = "_id";

    public static final String BASE_ENGLISH_URL = "http://teampanicbutton.github.io/api/mobile.json";
    public static final String BASE_SPANISH_URL = "http://teampanicbutton.github.io/api/es/mobile.json";
    public static final String BASE_FILIPINO_URL = "http://teampanicbutton.github.io/api/ph/mobile.json";

}
