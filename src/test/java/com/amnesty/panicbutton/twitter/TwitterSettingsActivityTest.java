package com.amnesty.panicbutton.twitter;

import android.view.ViewGroup;
import android.widget.CheckBox;
import com.amnesty.panicbutton.R;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static android.view.View.VISIBLE;
import static org.junit.Assert.*;
import static org.robolectric.Robolectric.shadowOf;

@RunWith(RobolectricTestRunner.class)
public class TwitterSettingsActivityTest {
    private TwitterSettingsActivity twitterSettingsActivity;
    private CheckBox optTwitterCheckbox;
    private ViewGroup shortCodeLayout;
    private ViewGroup messageLayout;

    @Before
    public void setup() {
        twitterSettingsActivity = new TwitterSettingsActivity();
        twitterSettingsActivity.onCreate(null);

        optTwitterCheckbox = (CheckBox) twitterSettingsActivity.findViewById(R.id.opt_twitter_checkbox);
        shortCodeLayout = (ViewGroup) twitterSettingsActivity.findViewById(R.id.twitter_short_code_layout);
        messageLayout = (ViewGroup) twitterSettingsActivity.findViewById(R.id.twitter_message_layout);
    }

    @Test
    public void shouldLoadTwitterLayoutOnCreate() {
        assertEquals(R.id.twitter_settings_layout_root, shadowOf(twitterSettingsActivity).getContentView().getId());
        assertFalse(shortCodeLayout.isShown());
        assertFalse(messageLayout.isShown());
    }

    @Test
    public void shouldShowShortCodeFragmentOnEnablingTwitter() {
        optTwitterCheckbox.performClick();
        assertTrue(shortCodeLayout.isShown());
        assertFalse(messageLayout.isShown());
    }

    @Test
    public void shouldHideShortCodeFragmentAndMessageFragmentOnDisablingTwitter() {
        optTwitterCheckbox.setChecked(true);
        shortCodeLayout.setVisibility(VISIBLE);
        messageLayout.setVisibility(VISIBLE);

        optTwitterCheckbox.performClick();

        assertFalse(shortCodeLayout.isShown());
        assertFalse(messageLayout.isShown());
    }

    @Test
    public void shouldShowTwitterEditTextOnSuccessfulShortCodeSelection() {
        twitterSettingsActivity.onShortCodeSelection(true);
        assertTrue(messageLayout.isShown());
    }

    @Test
    public void shouldHideTwitterEditTextOnUnSuccessfulShortCodeSelection() {
        twitterSettingsActivity.onShortCodeSelection(false);
        assertFalse(messageLayout.isShown());
    }
}

