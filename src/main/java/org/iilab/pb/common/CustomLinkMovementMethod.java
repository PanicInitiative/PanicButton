package org.iilab.pb.common;

import android.content.Context;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.TextView;


/**
 * Created by aoe on 6/13/2014.
 */
public class CustomLinkMovementMethod extends LinkMovementMethod {

    private static Context movementContext;

    private static CustomLinkMovementMethod linkMovementMethod = new CustomLinkMovementMethod();

    @Override
    public boolean onTouchEvent( TextView widget, Spannable buffer, MotionEvent event ) {
        try {
            return super.onTouchEvent( widget, buffer, event ) ;
        } catch( Exception ex ) {
            Log.e(">>>>>>", "INVALID link");

//            Toast.makeText(MainActivity.this, "Could not load link", Toast.LENGTH_LONG).show();
            return true;
        }
    }

//    public boolean onTouchEvent(android.widget.TextView widget, android.text.Spannable buffer, android.view.MotionEvent event)
//    {
//        int action = event.getAction();
//
//        if (action == MotionEvent.ACTION_UP)
//        {
//            int x = (int) event.getX();
//            int y = (int) event.getY();
//
//            x -= widget.getTotalPaddingLeft();
//            y -= widget.getTotalPaddingTop();
//
//            x += widget.getScrollX();
//            y += widget.getScrollY();
//
//            Layout layout = widget.getLayout();
//            int line = layout.getLineForVertical(y);
//            int off = layout.getOffsetForHorizontal(line, x);
//
//            URLSpan[] link = buffer.getSpans(off, off, URLSpan.class);
//            if (link.length != 0)
//            {
//                String url = link[0].getURL();
//                if (url.contains("https"))
//                {
//                    Log.d("Link", url);
//                    Toast.makeText(movementContext, "Link was clicked", Toast.LENGTH_LONG).show();
//                } else if (url.contains("tel"))
//                {
//                    Log.d("Link", url);
//                    Toast.makeText(movementContext, "Tel was clicked", Toast.LENGTH_LONG).show();
//                } else if (url.contains("mailto"))
//                {
//                    Log.d("Link", url);
//                    Toast.makeText(movementContext, "Mail link was clicked", Toast.LENGTH_LONG).show();
//                }
//                return true;
//            }
//        }
//
//        return super.onTouchEvent(widget, buffer, event);
//    }

    public static android.text.method.MovementMethod getInstance(Context c)
    {
        movementContext = c;
        return linkMovementMethod;
    }

}
