package org.iilab.pb.trigger;

import org.iilab.pb.trigger.MultiClickEvent;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class MultiClickEventTest {
    private MultiClickEvent multiClickEvent;

    @Before
    public void setUp() {
        multiClickEvent = new MultiClickEvent();
    }
/*
    @Test
    public void shouldBeActivatedWhenEventsAreFiredWithInTheTimeLimit() {
        registerClick(5, 15000l,15001l,15002l,15003l,15004l);
        assertTrue(multiClickEvent.isActivated());
    }

    @Test
    public void shouldBeActivatedWhenEventsAreFiredWithInTheTimeLimitAfterDelayInFirstTry() {
        registerClick(3, 15000l, 15001l, 15002l);
        assertFalse(multiClickEvent.isActivated());
        registerClick(5, 25000l, 25001l, 25002l, 25003l, 25004l);
        assertTrue(multiClickEvent.isActivated());
    }

    @Test
    public void shouldNotBeActivatedWhenEventsAreNotFiredWithInThTimeLimit() throws InterruptedException {
        registerClick(5, 15000l, 15001l, 25002l, 25003l, 25004l);
        assertFalse(multiClickEvent.isActivated());
    }
*/
    private void registerClick(int noOfTimes, long... eventTimes) {
        for (int i = 0; i < noOfTimes; i++) {
            multiClickEvent.registerClick(eventTimes[i]);
        }
    }
}
