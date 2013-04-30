package com.amnesty.panicbutton.wizard;

import com.amnesty.panicbutton.R;

public enum WizardAction {
    START(R.string.start_action),
    NEXT(R.string.next_action),
    SAVE(R.string.save_action);

    private int actionId;

    private WizardAction(int actionId) {
        this.actionId = actionId;
    }

    public int actionId() {
        return this.actionId;
    }
}
