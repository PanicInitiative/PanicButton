package com.apb.beacon.wizard;

import com.apb.beacon.R;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class WizardActionTest {
    @Test
    public void shouldReturnResourceIdForStates() {
        assertEquals(R.string.start_action, WizardAction.START.actionId());
        assertEquals(R.string.next_action, WizardAction.NEXT.actionId());
        assertEquals(R.string.save_action, WizardAction.SAVE.actionId());
    }
}
