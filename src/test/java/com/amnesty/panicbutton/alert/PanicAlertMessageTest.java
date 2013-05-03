package com.amnesty.panicbutton.alert;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PanicAlertMessageTest {
    private String normalLocationText;
    private String longLocationText;
    private String message;

    @Before
    public void setUp() {
        message = "Normal test message.Normal test message.Normal test message.Normal test message.12345";
        normalLocationText = ". I'm at http://maps.google.com/maps?q=12.9839562,80.2467788 via network";
        longLocationText = ". I'm at http://maps.google.com/maps?q=12.983956212345,80.246778812345 via network";
    }

    @Test
    public void shouldReturnFullMessageIfItsWithinLimit() {
        String expectedMessage = message + normalLocationText;
        PanicAlertMessage panicAlertMessage = new PanicAlertMessage(message, normalLocationText);
        assertEquals(expectedMessage, panicAlertMessage.getSMSText());
    }

    @Test
    public void shouldTruncateTheMessagePartIfItExceeds() {
        String expectedMessage = "Normal test message.Normal test message.Normal test message.Normal test messag" + longLocationText;
        PanicAlertMessage panicAlertMessage = new PanicAlertMessage(message, longLocationText);
        assertEquals(expectedMessage, panicAlertMessage.getSMSText());
    }
}
