package org.iilab.pb.alert;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;

import org.iilab.pb.R;
import org.iilab.pb.common.ApplicationSettings;
import org.iilab.pb.location.LocationTestUtil;
import org.iilab.pb.model.SMSSettings;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import static java.lang.System.currentTimeMillis;
import static java.util.Arrays.asList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(RobolectricTestRunner.class)
public class PanicMessageTest {
    private String normalLocationText;
    private String finerLocationText;

    private String message;
    private String mobile1;
    private String mobile3;
    private Location location, finerLocation;
    private Context context;

    @Mock
    public SMSAdapter mockSMSAdapter;

    @Before
    public void setUp() {
        initMocks(this);
        context = Robolectric.application;
        mobile1 = "1231231222";
        String mobile2 = "";
        mobile3 = "6786786789";
        double latitude = 12.9839562;
        double longitude = 80.2467788;
        double finerLatitude = 12.983956212345;
        double finerLongitude = 80.246778812345;

        message = "Normal test message.Normal test message.Normal test message.Normal test message.12345";
        normalLocationText = "I\\'m at https://maps.google.com/maps?q=12.9839562,80.2467788 via network";
        finerLocationText = "I\\'m at https://maps.google.com/maps?q=12.983956212345,80.246778812345 via network";

        location = LocationTestUtil.location(LocationManager.NETWORK_PROVIDER, latitude, longitude, currentTimeMillis(), 10.0f);
        finerLocation = LocationTestUtil.location(LocationManager.NETWORK_PROVIDER, finerLatitude, finerLongitude, currentTimeMillis(), 10.0f);

        SMSSettings smsSettings = new SMSSettings(asList(mobile1, mobile2, mobile3), message);
        SMSSettings.saveContacts(context, smsSettings);
        SMSSettings.saveMessage(context, message);
    }

    @Test
    public void shouldSendFirstSMSWithLocationToAllConfiguredPhoneNumbersIgnoringInValidNumbers() {
        PanicMessage panicMessage = createPanicMessage();
        ApplicationSettings.setFirstMsgSent(context, false);
        panicMessage.sendAlertMessage(location);

        String messageWithLocation = message + " - " + normalLocationText;
        verify(mockSMSAdapter).sendSMS(context, mobile1, messageWithLocation);
        verify(mockSMSAdapter).sendSMS(context, mobile3, messageWithLocation);
        verifyNoMoreInteractions(mockSMSAdapter);
    }


    @Test
    public void shouldSendLaterSMSWithLocationToAllConfiguredPhoneNumbersIgnoringInValidNumbers() {
        ApplicationSettings.setFirstMsgSent(context, true);
        PanicMessage panicMessage = createPanicMessage();
        panicMessage.sendAlertMessage(location);

        String messageWithLocation = message + " - " + normalLocationText;
        verify(mockSMSAdapter).sendSMS(context, mobile1, messageWithLocation);
        verify(mockSMSAdapter).sendSMS(context, mobile3, messageWithLocation);
        verifyNoMoreInteractions(mockSMSAdapter);
    }

    @Test
    public void shouldSendFirstSMSWithOutLocationToAllConfiguredPhoneNumbersIfTheLocationIsNotAvailable() {
        PanicMessage panicMessage = createPanicMessage();
        panicMessage.sendAlertMessage(null);
        ApplicationSettings.setFirstMsgSent(context, false);

        String messageWithoutLocation = message + " - " + context.getResources().getString(R.string.location_not_found);
        verify(mockSMSAdapter).sendSMS(context, mobile1, messageWithoutLocation);
        verify(mockSMSAdapter).sendSMS(context, mobile3, messageWithoutLocation);
        verifyNoMoreInteractions(mockSMSAdapter);
    }


    @Test
    public void shouldSendLaterSMSWithOutLocationToAllConfiguredPhoneNumbersIfTheLocationIsNotAvailable() {
        ApplicationSettings.setFirstMsgSent(context, true);
        PanicMessage panicMessage = createPanicMessage();
        panicMessage.sendAlertMessage(null);

        String messageWithoutLocation = message + " - " + context.getResources().getString(R.string.location_not_found);
        verify(mockSMSAdapter).sendSMS(context, mobile1, messageWithoutLocation);
        verify(mockSMSAdapter).sendSMS(context, mobile3, messageWithoutLocation);
        verifyNoMoreInteractions(mockSMSAdapter);
    }


    @Test
    public void shouldTruncateTheMessagePartIfItExceeds() {
        String expectedMessage = "Normal test message.Normal test message.Normal test message.Normal test messag" + finerLocationText;

        PanicMessage panicMessage = createPanicMessage();
        panicMessage.sendAlertMessage(finerLocation);

        verify(mockSMSAdapter).sendSMS(context, mobile1, expectedMessage);
    }

    private PanicMessage createPanicMessage() {
        return new PanicMessage(context) {
            @Override
            SMSAdapter getSMSAdapter() {
                return mockSMSAdapter;
            }
        };
    }
}
