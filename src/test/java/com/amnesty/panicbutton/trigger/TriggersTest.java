package com.amnesty.panicbutton.trigger;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TriggersTest {
    private Triggers triggers;
    private long currentTime;

    @Before
    public void setUp() {
        triggers = new Triggers();
        currentTime = System.currentTimeMillis();
    }

    @Test
    public void shouldBeActiveWhenEventsAreAddedWithTimeLimit() {
        for (int i = 0; i < 5; i++) {
            triggers.add(currentTime + (i * 500));
        }
        assertTrue(triggers.isActive());
    }

    @Test
    public void shouldBeInActiveWhenEventsAreAddedOutsideTimeLimit() {
        triggers.add(currentTime);
        triggers.add(currentTime + 5500);
        assertFalse(triggers.isActive());
    }

    @Test
    public void shouldNotAddTimestampWhenTriggerIsActive() {
        for (int i = 0; i < 6; i++) {
            triggers.add(currentTime + (i * 500));
        }
        assertEquals(5, triggers.count());
    }
}
