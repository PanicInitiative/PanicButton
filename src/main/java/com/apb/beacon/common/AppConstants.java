package com.apb.beacon.common;

public class AppConstants {
    //Test values
	//public static final boolean SKIP_WIZARD = true;
    //public static final int PHONE_NUMBER_LIMIT = 3;

	//Prod values
    public static final boolean SKIP_WIZARD = false;
    public static final int PHONE_NUMBER_LIMIT = 4;
    public static final long ONE_MINUTE = 1000 * 60;
	
    public static final int HAPTIC_FEEDBACK_DURATION = 3000;

    public static final float GPS_MIN_DISTANCE = 0;
    public static final long GPS_MIN_TIME = 1000 * 60 * 2;

    public static final float NETWORK_MIN_DISTANCE = 0;
    public static final long NETWORK_MIN_TIME = 1000 * 60 * 2;

    
    public static final int WARNING_TRAINING_MESSAGE_MINIMUM_CHARACTER = 30;

    public static final int HTTP_REQUEST_TYPE_GET = 1;
    public static final int HTTP_REQUEST_TYPE_POST = 2;
    public static final int HTTP_REQUEST_TYPE_PUT = 3;

    public static final int WIZARD_FLAG_HOME_NOT_CONFIGURED = 601;
    public static final int WIZARD_FLAG_HOME_NOT_CONFIGURED_ALARM = 602;
    public static final int WIZARD_FLAG_HOME_NOT_CONFIGURED_DISGUISE = 603;
    public static final int WIZARD_FLAG_HOME_READY = 604;
//    public static final int WIZARD_FLAG_COMPLETE = 605;


    public static boolean IS_BACK_BUTTON_PRESSED = false;
//    public static boolean IS_ACTION_ITEM_PRESSED = false;
    public static boolean PAGE_FROM_NOT_IMPLEMENTED = false;
    
    public static final int FROM_WIZARD_ACTIVITY = 1;
    public static final int FROM_MAIN_ACTIVITY = 2;

    public static final String TABLE_PRIMARY_KEY = "_id";

    public static final String BASE_URL = "https://panicbutton.iilab.org/api/";
    public static final String HELP_DATA_URL = "help.json";
    public static final String VERSION_CHECK_URL = "version.json";

    public static final int DATABASE_VERSION = 13;

    public static final int SPLASH_DELAY_TIME = 200;

}
