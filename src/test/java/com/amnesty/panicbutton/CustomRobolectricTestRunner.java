package com.amnesty.panicbutton;

import com.amnesty.panicbutton.shadow.ShadowCustomSmsManager;
import org.junit.runners.model.InitializationError;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import java.lang.reflect.Method;

public class CustomRobolectricTestRunner extends RobolectricTestRunner {
    public CustomRobolectricTestRunner(Class testClass) throws InitializationError {
        super(testClass);
    }

    @Override
    public void beforeTest(Method method) {
        Robolectric.bindShadowClass(ShadowCustomSmsManager.class);
    }
}
