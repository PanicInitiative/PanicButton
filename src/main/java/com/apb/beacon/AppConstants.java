package com.apb.beacon;

public class AppConstants {
    public static final int HAPTIC_FEEDBACK_DURATION = 3000;

    public static final float GPS_MIN_DISTANCE = 0;
    public static final long GPS_MIN_TIME = 1000 * 60 * 2;

    public static final float NETWORK_MIN_DISTANCE = 0;
    public static final long NETWORK_MIN_TIME = 1000 * 60 * 2;

    public static final long ALERT_FREQUENCY = 1000 * 60 * 5;

    public static final int PAGE_NUMBER_WIZARD_WELCOME = 0;
    public static final int PAGE_NUMBER_PANIC_BUTTON_TRAINING = 1;
    public static final int PAGE_NUMBER_PANIC_BUTTON_TRAINING_PIN = 2;
    public static final int PAGE_NUMBER_TRAINING_CONTACTS_INTRO = 3;
    public static final int PAGE_NUMBER_TRAINING_CONTACTS_LEARN_MORE = 4;
    public static final int PAGE_NUMBER_TRAINING_CONTACTS = 5;
    public static final int PAGE_NUMBER_TRAINING_MESSAGE_INTRO = 6;
    public static final int PAGE_NUMBER_TRAINING_MESSAGE = 7;
    public static final int PAGE_NUMBER_EMERGENCY_ALERT1 = 8;
    public static final int PAGE_NUMBER_EMERGENCY_ALERT2 = 9;
    public static final int PAGE_NUMBER_EMERGENCY_ALERT3 = 10;
    public static final int PAGE_NUMBER_DISGUISE_INTRO = 11;
    public static final int PAGE_NUMBER_FINISH_WIZARD = 12;

    public static final int WIZARD_PAGE_COUNT_TO_INSERT_INTO_DB = 8;
    public static final int WIZARD_TOTAL_PAGE_COUNT = PAGE_NUMBER_FINISH_WIZARD + 1;

    public static final int WARNING_TRAINING_MESSAGE_MINIMUM_CHARACTER = 30;

    public static final int HTTP_REQUEST_TYPE_GET = 1;
    public static final int HTTP_REQUEST_TYPE_POST = 2;
    public static final int HTTP_REQUEST_TYPE_PUT = 3;

    public static final int WIZARD_FLAG_HOME_NOT_COMPLETED = 601;
    public static final int WIZARD_FLAG_HOME_NOT_CONFIGURED_ALARM = 602;
    public static final int WIZARD_FLAG_HOME_NOT_CONFIGURED_DISGUISE = 603;
    public static final int WIZARD_FLAG_HOME_READY = 604;
    public static final int WIZARD_FLAG_COMPLETE = 605;

    public static boolean wizard_is_back_button_pressed = false;

    public static final int FROM_WIZARD_ACTIVITY = 1;
    public static final int FROM_MAIN_ACTIVITY = 2;

    public static final String TABLE_PRIMARY_KEY = "_id";

//    public static final String BASE_URL = "https://rawgithub.com/TeamPanicButton/Content/latest/Mobile/";
    public static final String BASE_URL = "http://teampanicbutton.github.io/api/mobile.json";

    public static final String[] RELATIVE_URLS = {
      "Wizard_Welcome.md", "Wizard_Training.md", "Wizard_Training_Pin.md", "Wizard_Training_Contacts.md", "Wizard_Training_Message.md"
    };

    public static final int[] PAGE_NUMBER_FOR_DATA = {
            PAGE_NUMBER_WIZARD_WELCOME, PAGE_NUMBER_PANIC_BUTTON_TRAINING, PAGE_NUMBER_PANIC_BUTTON_TRAINING_PIN,
            PAGE_NUMBER_TRAINING_CONTACTS_INTRO, PAGE_NUMBER_TRAINING_MESSAGE_INTRO
    };

    public static final String[] PAGE_NAME_FOR_DATA = {
      "Wizard Welcome", "Wizard Training", "Wizard Training Pin", "Wizard Training Contacts", "Wizard Training Messages"
    };
}
