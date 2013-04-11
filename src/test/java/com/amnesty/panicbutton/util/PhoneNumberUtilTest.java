package com.amnesty.panicbutton.util;

import org.junit.Test;

import static com.amnesty.panicbutton.util.PhoneNumberUtil.mask;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class PhoneNumberUtilTest {
    @Test
    public void shouldMaskPhoneNumberExceptTheLastTwoChars() {
        assertEquals("**********89", mask("111-222-2289"));
        assertEquals("*******89", mask("123456789"));
        assertEquals("*******89", mask("*******89"));
        assertEquals("1", mask("1"));
        assertEquals("22", mask("22"));
        assertEquals("", mask(""));
        assertNull(mask(null));
    }
}
