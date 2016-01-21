package org.iilab.pb.common;

import android.widget.TextView;

import org.iilab.pb.BuildConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import static org.junit.Assert.assertEquals;
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk=21)
public class MessageLimitWatcherTest{
    TextView textView;
    @Before
    public void setUp() {
         textView = new TextView(RuntimeEnvironment.application);
    }
    @Test
    public void shouldUpdateMessageLimitOnMainTextChange() {
        MessageLimitWatcher messageLimitWatcher = new MessageLimitWatcher(textView, "Characters left : ", 85, null);
        messageLimitWatcher.beforeTextChanged(null,-1,-1,-1);
        messageLimitWatcher.onTextChanged("test", -1, -1, -1);
        messageLimitWatcher.afterTextChanged(null);

        assertEquals("Characters left : 81", (Shadows.shadowOf(textView)).innerText());
    }
}
