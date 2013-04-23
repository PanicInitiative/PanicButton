package com.amnesty.panicbutton.trigger;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class MultiClickEventTest {
    @Test
    public void shouldBeActivatedWhenEventsAreFiredWithInTheTimeLimit() throws InterruptedException {
        MultiClickEvent multiClickEvent = new MultiClickEvent();
        for (int i = 0; i < 5; i++) {
            multiClickEvent.registerClick();
        }
        assertTrue(multiClickEvent.isActivated());

        multiClickEvent = new MultiClickEvent();
        multiClickEvent.registerClick();
        multiClickEvent.registerClick();
        multiClickEvent.registerClick();
        Thread.sleep(5000);
        multiClickEvent.registerClick();
        multiClickEvent.registerClick();
        multiClickEvent.registerClick();
        multiClickEvent.registerClick();
        multiClickEvent.registerClick();
        assertTrue(multiClickEvent.isActivated());
    }

    @Test
    public void shouldNotBeActivatedWhenEventsAreNotFiredWithInThTimeLimit() throws InterruptedException {
        MultiClickEvent multiClickEvent = new MultiClickEvent();
        multiClickEvent.registerClick();
        multiClickEvent.registerClick();
        multiClickEvent.registerClick();
        Thread.sleep(5000);
        multiClickEvent.registerClick();
        multiClickEvent.registerClick();
        assertFalse(multiClickEvent.isActivated());
    }
}
