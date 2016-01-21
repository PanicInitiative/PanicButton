//package org.iilab.pb.common;
//
//import android.content.Intent;
//import android.database.Cursor;
//import android.net.Uri;
//import android.support.v4.app.FragmentActivity;
//import android.support.v4.app.FragmentManager;
//import android.support.v4.app.FragmentTransaction;
//import android.widget.EditText;
//import android.widget.ImageButton;
//
//import org.iilab.pb.BuildConfig;
//import org.iilab.pb.R;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.robolectric.RobolectricGradleTestRunner;
//import org.robolectric.Shadows;
//import org.robolectric.annotation.Config;
//import org.robolectric.fakes.RoboCursor;
//import org.robolectric.shadows.ShadowActivity;
//
//import static android.app.Activity.RESULT_CANCELED;
//import static android.app.Activity.RESULT_OK;
//import static org.hamcrest.CoreMatchers.equalTo;
//import static org.iilab.pb.R.id.contact_picker_button;
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertThat;
//
//@RunWith(RobolectricGradleTestRunner.class)
//@Config(constants = BuildConfig.class, sdk=21)
//public class ContactPickerFragmentTest {
//    private ContactPickerFragment contactPickerFragment;
//    private final String expectedPhoneNumber = "123456789";
//    private EditText contactEditText;
//
//
//    @Before
//    public void setUp() {
//        contactPickerFragment = new ContactPickerFragment() {
//            Cursor getCursor(Uri contactData, String[] projection) {
//                RoboCursor simpleTestCursor = new RoboCursor();
//                simpleTestCursor.setResults(new Object[][]{new Object[]{expectedPhoneNumber}});
//                return simpleTestCursor;
//            }
//        };
//        FragmentManager fragmentManager = new FragmentActivity().getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.add(contactPickerFragment, null);
//        fragmentTransaction.commit();
//
//        contactEditText = (EditText) contactPickerFragment.getView().findViewById(R.id.contact_edit_text);
//    }
//
//    @Test
//    public void shouldOpenContactsOnClick() {
//        ImageButton contactPickerButton = (ImageButton) contactPickerFragment.getView().findViewById(contact_picker_button);
//        contactPickerButton.performClick();
//
//        ShadowActivity shadowActivity = Shadows.shadowOf(contactPickerFragment.getActivity());
//        ShadowActivity.IntentForResult startedIntentForResult = shadowActivity.getNextStartedActivityForResult();
//        Intent intent = startedIntentForResult.intent;
//
//        assertThat(intent.getAction(), equalTo(Intent.ACTION_PICK));
////        assertThat(intent.getType(), equalTo(CONTENT_ITEM_TYPE));
//    }
//
//    @Test
//    public void shouldDisplayTheSelectedContactNumber() {
//        Uri.Builder uriBuilder = Uri.parse("content://com.android.contacts/data/6802").buildUpon();
//        Uri contactURI = uriBuilder.build();
//        Intent intent = new Intent();
//        intent.setData(contactURI);
//
//        contactPickerFragment.onActivityResult(100, RESULT_OK, intent);
//
//        assertEquals(expectedPhoneNumber, contactEditText.getText().toString());
//    }
//
//    @Test
//    public void shouldNotChangeThePhoneForNonContactApplication() {
//        contactPickerFragment.onActivityResult(7, RESULT_OK, new Intent());
//        assertEquals("", contactEditText.getText().toString());
//    }
//
//    @Test
//    public void shouldNotChangeThePhoneWhenContactApplicationIsCanceled() {
//        contactPickerFragment.onActivityResult(100, RESULT_CANCELED, new Intent());
//        assertEquals("", contactEditText.getText().toString());
//    }
//}