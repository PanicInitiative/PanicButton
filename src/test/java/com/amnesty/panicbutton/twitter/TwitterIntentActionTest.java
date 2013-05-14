package com.amnesty.panicbutton.twitter;

import org.junit.Test;

import static junit.framework.Assert.assertNull;

public class TwitterIntentActionTest {
    @Test
    public void shouldReturnNullOnInvalidAction(){
        assertNull(TwitterIntentAction.get(null));
    }
}
