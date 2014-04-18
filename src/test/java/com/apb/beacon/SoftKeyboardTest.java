package com.apb.beacon;

import com.apb.beacon.common.SoftKeyboard;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class SoftKeyboardTest {
    @Test
    public void shouldReturnSoftKeyboard() {
        assertNotNull(new SoftKeyboard());
    }
}
