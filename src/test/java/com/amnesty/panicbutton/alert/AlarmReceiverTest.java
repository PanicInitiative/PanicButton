package com.amnesty.panicbutton.alert;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import com.amnesty.panicbutton.common.Intents;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import static junit.framework.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(RobolectricTestRunner.class)
public class AlarmReceiverTest {
    @Mock
    private PanicMessage mockPanicMessage;
    private Context context = Robolectric.application;
    private AlarmReceiver alarmReceiver;

    @Before
    public void setUp() {
        initMocks(this);
        alarmReceiver = spy(new AlarmReceiver());
    }

    @Test
    public void shouldSendAlertOnReceivingAlarmIntent() {
        doReturn(mockPanicMessage).when(alarmReceiver).getPanicMessage(any(Context.class));
        alarmReceiver.onReceive(context, new Intent(Intents.SEND_ALERT_ACTION));
        verify(mockPanicMessage).send(any(Location.class));
    }

    @Test
    public void shouldIgnoreOtherIntents() {
        doReturn(mockPanicMessage).when(alarmReceiver).getPanicMessage(any(Context.class));
        alarmReceiver.onReceive(context, new Intent(Intent.ACTION_ANSWER));
        verify(mockPanicMessage, never()).send(any(Location.class));
    }

    @Test
    public void shouldReturnPanicMessage() {
        assertNotNull(alarmReceiver.getPanicMessage(context));
    }
}
