package com.apb.beacon.twitter;

public enum TwitterIntentAction {
    VALID_SHORT_CODE("com.apb.beacon.twitter.TwitterIntentAction.VALID_SHORT_CODE", true),
    INVALID_SHORT_CODE("com.apb.beacon.twitter.TwitterIntentAction.INVALID_SHORT_CODE", false);

    private final String action;
    private boolean state;

    TwitterIntentAction(String action, boolean state) {
        this.action = action;
        this.state = state;
    }

    public String getAction() {
        return action;
    }

    public boolean getState() {
        return state;
    }

    public static TwitterIntentAction get(String action) {
        for (TwitterIntentAction twitterIntentAction : values()) {
            if (twitterIntentAction.action.equals(action)) {
                return twitterIntentAction;
            }
        }
        return null;
    }
}
