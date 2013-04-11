package com.amnesty.panicbutton.model;

import android.content.Context;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static org.codehaus.plexus.util.ReflectionUtils.getFieldByNameIncludingSuperclasses;
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

        Field messageField = getFieldByNameIncludingSuperclasses("message", SMSSettings.class);
        messageField.setAccessible(true);
        assertEquals(expectedMessage, messageField.get(retrievedSMSSettings));

        Field phoneNumbersField = getFieldByNameIncludingSuperclasses("phoneNumbers", SMSSettings.class);
        phoneNumbersField.setAccessible(true);
        assertEquals(expectedPhoneNumbers, phoneNumbersField.get(retrievedSMSSettings));
    }
}
