package org.iilab.pb.test.support;


import android.widget.RelativeLayout;

import org.robolectric.internal.Implementation;
import org.robolectric.internal.Implements;
import org.robolectric.shadows.ShadowRelativeLayout;

@Implements(RelativeLayout.class)
public class CustomShadowRelativeLayout extends ShadowRelativeLayout {
    private int color;

    @Implementation
    public void setBackgroundColor(int color) {
        this.color = color;
    }

    @Override
    public int getBackgroundColor() {
        return color;
    }
}
