package org.iilab.pb.test.support;

import org.junit.runners.model.InitializationError;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

public class PanicButtonRobolectricTestRunner extends RobolectricTestRunner {

    public PanicButtonRobolectricTestRunner(Class<?> testClass) throws InitializationError {
        super(testClass);
    }

    @Override protected void bindShadowClasses() {
        Robolectric.bindShadowClass(CustomShadowRelativeLayout.class);
    }
}
