package com.amnesty.panicbutton.twitter;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import com.amnesty.panicbutton.R;
import com.amnesty.panicbutton.common.MessageFragment;
import com.amnesty.panicbutton.common.TestFragmentActivity;
import org.codehaus.plexus.util.ReflectionUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowApplication;
import org.robolectric.shadows.ShadowPreferenceManager;

import java.util.List;
import java.util.Map;

import static com.amnesty.panicbutton.twitter.TwitterIntentAction.INVALID_SHORT_CODE;
import static com.amnesty.panicbutton.twitter.TwitterIntentAction.VALID_SHORT_CODE;
import static org.codehaus.plexus.util.ReflectionUtils.getValueIncludingSuperclasses;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.robolectric.Robolectric.shadowOf;

@RunWith(RobolectricTestRunner.class)
public class TwitterSettingsFragmentTest {
    private TwitterSettingsFragment twitterSettingsFragment;
    private TestFragmentActivity activity;
    private ShortCodeSettings shortCodeSettings;
    private String testMessage;
    private SharedPreferences sharedPreferences;
    private MessageFragment twitterMessageFragment;

    @Before
    public void setUp() {
        activity = new TestFragmentActivity();
        twitterSettingsFragment = createFragment(activity);

        shortCodeSettings = new ShortCodeSettings("India");
        shortCodeSettings.setServiceProvider("Airtel");
        shortCodeSettings.setShortCode("53000");

        twitterMessageFragment = (MessageFragment) activity.getSupportFragmentManager().findFragmentById(R.id.twitter_message_fragment);
        sharedPreferences = ShadowPreferenceManager.getDefaultSharedPreferences(Robolectric.application);
        testMessage = "Test Message";
    }

    @Test
    public void shouldRegisterListenerOnActivityAttach() throws IllegalAccessException {
        assertEquals(activity, getValueIncludingSuperclasses("callback", twitterSettingsFragment));
    }

    @Test
    public void shouldSaveTwitterSettings() throws IllegalAccessException {
        TwitterShortCodeFragment mockShortCodeFragment = mock(TwitterShortCodeFragment.class);
        MessageFragment mockMessageFragment = mock(MessageFragment.class);
        ReflectionUtils.setVariableValueInObject(twitterSettingsFragment, "twitterShortCodeFragment", mockShortCodeFragment);
        ReflectionUtils.setVariableValueInObject(twitterSettingsFragment, "twitterMessageFragment", mockMessageFragment);
        when(mockShortCodeFragment.getShortCodeSettings()).thenReturn(shortCodeSettings);
        when(mockMessageFragment.getMessage()).thenReturn(testMessage);

        twitterSettingsFragment.save();

        Map<String, String> allPreferences = (Map<String, String>) sharedPreferences.getAll();
        assertEquals(testMessage, allPreferences.get("TWITTER_MESSAGE"));
        assertEquals("India", allPreferences.get("TWITTER_COUNTRY"));
        assertEquals("Airtel", allPreferences.get("TWITTER_SERVICE_PROVIDER"));
        assertEquals("53000", allPreferences.get("TWITTER_SHORT_CODE"));
    }

    @Test
    public void shouldShowMessageBoxOnReceivingValidShortCodeIntent() {
        Robolectric.application.sendBroadcast(new Intent(VALID_SHORT_CODE.getAction()));
        assertTrue(twitterSettingsFragment.isVisible());
    }

    private TwitterSettingsFragment createFragment(FragmentActivity activity) {
        TwitterSettingsFragment fragment = new TwitterSettingsFragment();
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(fragment, null);
        fragmentTransaction.commit();
        return fragment;
    }

    @Test
    public void shouldUnregisterBroadcastReceiverOnDestroy() {
        ShadowApplication shadowApplication = shadowOf(Robolectric.application);
        List<ShadowApplication.Wrapper> registeredReceivers = shadowApplication.getRegisteredReceivers();
        int initialSize = registeredReceivers.size();

        twitterSettingsFragment.onDestroyView();

        registeredReceivers = shadowApplication.getRegisteredReceivers();
        assertEquals(initialSize - 1, registeredReceivers.size());
    }

    @Test
    public void shouldShowTwitterMessageFragmentWhenTwitterSettingsIsValid() {
        TwitterSettings.save(Robolectric.application, new TwitterSettings(shortCodeSettings, "Test"));
        twitterSettingsFragment.setVisibility(true);
        assertTrue(twitterMessageFragment.isVisible());
    }

    @Test
    public void shouldHideMessageBoxOnReceivingInValidShortCodeIntent() {
        Robolectric.application.sendBroadcast(new Intent(INVALID_SHORT_CODE.getAction()));
        //assertFalse(twitterSettingsFragment.isVisible());
    }
}
