package org.iilab.pb.wizard;

import android.app.Application;
import android.support.v4.app.FragmentStatePagerAdapter;


import org.iilab.pb.WizardActivity;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(RobolectricTestRunner.class)
public class WizardActivityTest {
    private WizardActivity wizardActivity;

    private static Application context;

    @Mock
    private FragmentStatePagerAdapter mockPagerAdapter;


    @Before
    public void setUp() throws IllegalAccessException {
        context = Robolectric.application;
        initMocks(this);
        wizardActivity = new WizardActivity() {
            FragmentStatePagerAdapter getWizardPagerAdapter() {
                return mockPagerAdapter;
            }
        };
//        wizardActivity.onCreate(null);
//
//        when(mockFragment.action()).thenReturn("Action");
//        when(mockFragment.performAction()).thenReturn(true);
//        when(mockFragment.getView()).thenReturn(mock(EditText.class));
//        when(mockPagerAdapter.getItem(anyInt())).thenReturn(mockFragment);
//        when(mockPagerAdapter.getCount()).thenReturn(3);

//        previousButton = (Button) wizardActivity.findViewById(R.id.previous_button);
//        actionButton = (Button) wizardActivity.findViewById(R.id.action_button);
//        viewPager = (WizardViewPager) wizardActivity.findViewById(R.id.wizard_view_pager);
    }

    @Test
    public void shouldLoadTheWizardLayoutAndInitializeViewPagerOnCreate() {
//        assertEquals(R.id.wizard_layout_root, shadowOf(wizardActivity).getContentView().getId());
//        assertEquals(mockPagerAdapter, viewPager.getAdapter());
    }


    @Test
    public void shouldFinishActivityOnHardwareBackFromFirstScreen() {
//        wizardActivity.onBackPressed();
//        assertTrue(wizardActivity.isFinishing());
    }

    @Test
    public void shouldCreateWizardPagerAdapter() {
//        assertNotNull(new WizardActivity().getWizardPagerAdapter());
    }

}
