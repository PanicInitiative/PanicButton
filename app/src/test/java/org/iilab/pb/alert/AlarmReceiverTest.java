package org.iilab.pb.alert;

import android.content.Context;
import android.content.Intent;
import android.location.Location;


import org.iilab.pb.common.Intents;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import static junit.framework.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
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
        verify(mockPanicMessage).sendAlertMessage(any(Location.class));
    }

    @Test
    public void shouldIgnoreOtherIntents() {
        doReturn(mockPanicMessage).when(alarmReceiver).getPanicMessage(any(Context.class));
        alarmReceiver.onReceive(context, new Intent(Intent.ACTION_ANSWER));
        verify(mockPanicMessage, never()).sendAlertMessage(any(Location.class));
    }

    @Test
    public void shouldReturnPanicMessage() {
        assertNotNull(alarmReceiver.getPanicMessage(context));
    }
}
