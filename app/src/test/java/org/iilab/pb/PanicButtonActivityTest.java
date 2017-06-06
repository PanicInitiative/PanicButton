package org.iilab.pb;

import android.content.Context;
import android.view.ViewGroup;

import org.hamcrest.core.Is;
import org.iilab.pb.alert.PanicAlert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowActivity;
import org.robolectric.shadows.ShadowViewGroup;

import static org.junit.Assert.assertThat;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk=21)
public class PanicButtonActivityTest {
    @Mock
    private PanicAlert mockPanicAlert;
    private ShadowViewGroup alertStatusStrip;
    @InjectMocks
    private CalculatorActivity calculatorActivity;
    private ShadowActivity shadowActivity;

    @Before
    public void setUp() {
//        calculatorActivity=spy(new CalculatorActivity());
//        initMocks(this);
//        when(calculatorActivity.getPanicAlert()).thenReturn(mockPanicAlert);
//        calculatorActivity = new CalculatorActivity() {
//            @Override
//            PanicAlert getPanicAlert() {
//                return mockPanicAlert;
//            }
//        };
//        calculatorActivity.onCreate(null);
//        calculatorActivity.onPostCreate(null);
        Context context = RuntimeEnvironment.application;
        calculatorActivity= Robolectric.setupActivity(CalculatorActivity.class);
        shadowActivity = Shadows.shadowOf(calculatorActivity);
        ViewGroup alertStatusStripOrig = (ViewGroup) shadowActivity.findViewById(R.id.alert_status_strip);
        alertStatusStrip = Shadows.shadowOf(alertStatusStripOrig);
    }


    @Test
    public void shouldShowRedAlertStripWhenAlertStatusIsActive(){
//    	mockPanicAlert.activate();
//    	when(mockPanicAlert.isActive()).thenReturn(true);
//        calculatorActivity.onResume();
//        assertThat(alertStatusStrip.getBackgroundColor(), Is.is(Robolectric.application.getResources().getColor(R.color.active_color)));
    }

    @Test
    public void shouldShowAmberAlertStripWhenAlertStatusIsStandby(){
//        when(mockPanicAlert.isActive()).thenReturnn(false);
//        calculatorActivity.onResume();
        assertThat(alertStatusStrip.getBackgroundColor(), Is.is(RuntimeEnvironment.application.getResources().getColor(R.color.standby_color)));
    }
}
