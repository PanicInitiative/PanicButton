package com.amnesty.panicbutton.trigger;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

public class MultiClickEventTest {
    private MultiClickEvent multiClickEvent;

    @Before
    public void setUp() {
        multiClickEvent = spy(new MultiClickEvent());
    }

    @Test
    public void shouldBeActivatedWhenEventsAreFiredWithInTheTimeLimit() {
        when(multiClickEvent.currentTime()).thenReturn(15000l,15001l,15002l,15003l,15004l);
        registerClick(5);
        assertTrue(multiClickEvent.isActivated());
    }

    @Test
    public void shouldBeActivatedWhenEventsAreFiredWithInTheTimeLimitAfterDelayInFirstTry() {
        when(multiClickEvent.currentTime()).thenReturn(15000l,15001l,15002l,25000l,25001l,25002l,25003l,25004l);
        registerClick(3);
        assertFalse(multiClickEvent.isActivated());
        registerClick(5);
        assertTrue(multiClickEvent.isActivated());
    }

    @Test
    public void shouldNotBeActivatedWhenEventsAreNotFiredWithInThTimeLimit() throws InterruptedException {
        when(multiClickEvent.currentTime()).thenReturn(15000l,15001l,25002l,25003l,25004l);
        registerClick(5);
        assertFalse(multiClickEvent.isActivated());
    }

    private void registerClick(int times) {
        for (int i = 0; i < times; i++) {
            multiClickEvent.registerClick();
        }
    }
}
