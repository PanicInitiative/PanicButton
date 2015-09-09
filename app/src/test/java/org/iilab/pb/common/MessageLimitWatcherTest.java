package org.iilab.pb.common;

import android.app.Activity;
import android.widget.TextView;

import org.iilab.pb.common.MessageLimitWatcher;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.assertEquals;

@RunWith(RobolectricTestRunner.class)
public class MessageLimitWatcherTest {
    @Test
    public void shouldUpdateMessageLimitOnMainTextChange() {
        TextView textView = new TextView(new Activity());
        MessageLimitWatcher messageLimitWatcher = new MessageLimitWatcher(textView, "Characters left : ", 85, null);

        messageLimitWatcher.beforeTextChanged(null,-1,-1,-1);
        messageLimitWatcher.onTextChanged("test", -1, -1, -1);
        messageLimitWatcher.afterTextChanged(null);

        assertEquals("Characters left : 81", Robolectric.shadowOf(textView).getText());
    }
}
