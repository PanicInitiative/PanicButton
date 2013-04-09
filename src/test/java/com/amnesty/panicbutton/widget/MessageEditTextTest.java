package com.amnesty.panicbutton.widget;

import android.app.Activity;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;
import com.amnesty.panicbutton.R;
import com.amnesty.panicbutton.sms.MessageLimitWatcher;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowEditText;

import java.util.List;

import static com.amnesty.panicbutton.AppConstants.MAX_CHARACTER_COUNT;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.robolectric.Robolectric.shadowOf;

@RunWith(RobolectricTestRunner.class)
public class MessageEditTextTest {
    private MessageEditText messageEditText;
    private EditText editText;
    private TextView textView;

    @Before
    public void setup() {
        messageEditText = new MessageEditText(new Activity());
        MessageEditText messageEditText2 = new MessageEditText(new Activity(), null);

        editText = (EditText) messageEditText.findViewById(R.id.message_edit_text);
        textView = (TextView) messageEditText.findViewById(R.id.characters_left_view);
    }

    @Test
    public void shouldInitializeTheMessageEditText() {
        ShadowEditText shadowEditText = (ShadowEditText) shadowOf(editText);

        assertEquals(textView.getText(), String.valueOf(MAX_CHARACTER_COUNT - editText.getText().length()));

        InputFilter[] filters = shadowEditText.getFilters();
        InputFilter inputFilter = filters[0];
        assertEquals(inputFilter.getClass(), InputFilter.LengthFilter.class);

        List<TextWatcher> watchers = shadowEditText.getWatchers();
        assertEquals(1, watchers.size());
        assertTrue(watchers.get(0).getClass().equals(MessageLimitWatcher.class));
    }
}
