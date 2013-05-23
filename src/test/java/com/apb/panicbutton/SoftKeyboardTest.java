package com.apb.panicbutton;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class SoftKeyboardTest {
    @Test
    public void shouldReturnSoftKeyboard() {
        assertNotNull(new SoftKeyboard());
    }
}
