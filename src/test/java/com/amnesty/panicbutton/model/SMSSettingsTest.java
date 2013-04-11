package com.amnesty.panicbutton.model;

import android.content.Context;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(RobolectricTestRunner.class)
public class SMSSettingsTest {

    @Test
    public void shouldSaveAndRetrieveSMSSettings() throws Exception {
        Context context = Robolectric.application;

        List<String> expectedPhoneNumbers = new ArrayList<String>();
        expectedPhoneNumbers.add("123456789");
        expectedPhoneNumbers.add("987654321");
        String expectedMessage = "Test Message";

        SMSSettings smsSettings = new SMSSettings(expectedPhoneNumbers, expectedMessage);

        SMSSettings.save(context, smsSettings);
        SMSSettings retrievedSMSSettings = SMSSettings.retrieve(context);

        assertEquals(expectedMessage, retrievedSMSSettings.message());
        assertEquals(expectedPhoneNumbers.get(0), retrievedSMSSettings.phoneNumber(0));
        assertEquals(expectedPhoneNumbers.get(1), retrievedSMSSettings.phoneNumber(1));
    }

    @Test
    public void shouldReturnEmptyPhoneNumberForNonExistentIndex() {
        SMSSettings smsSettings = new SMSSettings(new ArrayList<String>(), "msg");
        assertEquals("", smsSettings.phoneNumber(1));
    }
}
