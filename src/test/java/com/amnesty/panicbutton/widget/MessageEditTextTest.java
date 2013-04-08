package com.amnesty.panicbutton.widget;

import android.app.Activity;
import android.text.InputFilter;
import android.widget.EditText;
import android.widget.TextView;
import com.amnesty.panicbutton.R;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowEditText;

import static com.amnesty.panicbutton.AppConstants.MAX_CHARACTER_COUNT;
import static junit.framework.Assert.assertEquals;
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

        editText = (EditText) messageEditText.findViewById(R.id.sms_message);
        textView = (TextView) messageEditText.findViewById(R.id.characters_left);
    }

    @Test
    public void shouldInitializeTheMessageEditText() {
        ShadowEditText shadowEditText = (ShadowEditText) shadowOf(editText);

        assertEquals(textView.getText(), String.valueOf(MAX_CHARACTER_COUNT - editText.getText().length()) );
        InputFilter inputFilter = shadowEditText.getFilters()[0];
        assertEquals(inputFilter.getClass(), InputFilter.LengthFilter.class) ;
    }
}
