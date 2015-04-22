package org.iilab.pb.common;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.Layout;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.text.method.MovementMethod;
import android.text.style.URLSpan;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.TextView;

import org.iilab.pb.MainActivity;


/**
 * Created by aoe on 6/13/2014.
 */
public class CustomLinkMovementMethod extends LinkMovementMethod {

    private static Context movementContext;

    private static CustomLinkMovementMethod linkMovementMethod = new CustomLinkMovementMethod();

    public boolean onTouchEvent(TextView widget, Spannable buffer, MotionEvent event) {
        int action = event.getAction();

        if (action == MotionEvent.ACTION_UP) {
            int x = (int) event.getX();
            int y = (int) event.getY();

            x -= widget.getTotalPaddingLeft();
            y -= widget.getTotalPaddingTop();

            x += widget.getScrollX();
            y += widget.getScrollY();

            Layout layout = widget.getLayout();
            int line = layout.getLineForVertical(y);
            int off = layout.getOffsetForHorizontal(line, x);

            URLSpan[] link = buffer.getSpans(off, off, URLSpan.class);
            if (link.length != 0) {
                String url = link[0].getURL();
                if (url.contains("https") || url.contains("http")) {
                    Log.d("Link", url);
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    movementContext.startActivity(browserIntent);
                    return true;
                } else {
                    String pageId = url;
                    Log.e(">>>", "page id = " + pageId);
                    Intent i = new Intent(movementContext, MainActivity.class);
                    i.putExtra("page_id", pageId);
                    movementContext.startActivity(i);
                    return true;
                }

            }
        }

        return super.onTouchEvent(widget, buffer, event);
    }

    public static MovementMethod getInstance(Context c) {
        movementContext = c;
        return linkMovementMethod;
    }
}
