package org.iilab.pb.common;

import android.content.res.TypedArray;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.TextView;


import org.iilab.pb.R;
import org.iilab.pb.common.MessageLimitWatcher;
import org.iilab.pb.fragment.MessageTextFragment;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowEditText;

import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.robolectric.Robolectric.application;
import static org.robolectric.Robolectric.shadowOf;

@RunWith(RobolectricTestRunner.class)
public class MessageFragmentTest {
    private MessageTextFragment messageFragment;
    private EditText messageEditText;
    private TextView charsLeftTextView;
    private TextView messageHeaderView;
    @Mock
    FragmentActivity mockFragmentActivity;
    @Mock
    TypedArray mockTypedArrays;
    @Mock
    AttributeSet mockAttrs;

    @Before
    public void setup() {
        initMocks(this);
        messageFragment = new MessageTextFragment();
        when(mockFragmentActivity.obtainStyledAttributes(mockAttrs, R.styleable.MessageFragmentArguments)).thenReturn(mockTypedArrays);
        when(mockTypedArrays.getInt(R.styleable.MessageFragmentArguments_max_characters, -1)).thenReturn(85);
        when(mockTypedArrays.getString(R.styleable.MessageFragmentArguments_message_header)).thenReturn("Emergency Message");
        messageFragment.onInflate(mockFragmentActivity, mockAttrs, null);

        FragmentActivity roboFragmentActivity = new FragmentActivity();
        FragmentManager fragmentManager = roboFragmentActivity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(messageFragment, null);
        fragmentTransaction.commit();

        messageEditText = (EditText) messageFragment.getView().findViewById(R.id.message_edit_text);
        charsLeftTextView = (TextView) messageFragment.getView().findViewById(R.id.characters_left_view);
        messageHeaderView = (TextView) messageFragment.getView().findViewById(R.id.message_fragment_header);

        messageFragment.setActionButtonStateListener(null);
    }

    @Test
    public void shouldInitializeTheMessageEditText() {
        ShadowEditText shadowEditText = (ShadowEditText) shadowOf(messageEditText);

        InputFilter[] filters = shadowEditText.getFilters();
        InputFilter inputFilter = filters[0];
        assertEquals(inputFilter.getClass(), InputFilter.LengthFilter.class);

        List<TextWatcher> watchers = shadowEditText.getWatchers();
        assertEquals(1, watchers.size());
        assertTrue(watchers.get(0).getClass().equals(MessageLimitWatcher.class));
    }

    @Test
    public void shouldSetTheMessageHeaderAndCharsLeftTextOnCreate() {
        String message = messageFragment.getMessage();
        String expectedMessage = application.getString(R.string.characters_left) + String.valueOf(85 - message.length());
        assertEquals(expectedMessage, charsLeftTextView.getText());
        assertEquals("Emergency Message", messageHeaderView.getText());
    }

    @Test
    public void shouldSetTheGivenTextInMessageBox() {
        String testMessage = "Test Message";
        messageFragment.setMessage(testMessage);
        assertEquals(testMessage, messageFragment.getMessage());
    }

    @Test
    public void shouldIgnoreMessageModificationIfTheInputIsNull() {
        String testMessage = "Test Message";
        messageFragment.setMessage(testMessage);
        messageFragment.setMessage(null);
        assertEquals(testMessage, messageFragment.getMessage());
    }
}
