package com.amnesty.panicbutton.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class PhoneNumberUtilTest {
    @Test
    public void shouldMaskPhoneNumberExceptTheLastTwoChars() {
        PhoneNumberUtil phoneNumberUtil = new PhoneNumberUtil();
        assertEquals("**********89", phoneNumberUtil.mask("111-222-2289"));
        assertEquals("*******89", phoneNumberUtil.mask("123456789"));
        assertEquals("*******89", phoneNumberUtil.mask("*******89"));
        assertEquals("1", phoneNumberUtil.mask("1"));
        assertEquals("22", phoneNumberUtil.mask("22"));
        assertEquals("", phoneNumberUtil.mask(""));
        assertNull(phoneNumberUtil.mask(null));
    }
}
