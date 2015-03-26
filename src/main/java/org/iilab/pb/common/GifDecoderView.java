package org.iilab.pb.common;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;

public class GifDecoderView extends ImageView {

	private boolean mIsPlayingGif = false;
	private boolean mShouldClear = false;
	public static int IMAGE_FULL_WIDTH = 1;
	private GifDecoder mGifDecoder;
	private Bitmap mTmpBitmap;
	private Thread mAnimationThread;

	private DisplayMetrics metrics;

	private final Handler mHandler = new Handler();
	private final Runnable mUpdateResults = new Runnable() {
		public void run() {
			if (mTmpBitmap != null && !mTmpBitmap.isRecycled()) {
				Drawable drawable = new BitmapDrawable(getResources(), mTmpBitmap);
				drawable = setDownloadedImageMetrices(drawable, metrics, metrics.scaledDensity, IMAGE_FULL_WIDTH);
				GifDecoderView.this.setImageDrawable(drawable);
			}
		}
	};

    private final Runnable mCleanupRunnable = new Runnable() {
//        @Override
        public void run() {
            if (mTmpBitmap != null && !mTmpBitmap.isRecycled())
            	mTmpBitmap.recycle();
            mTmpBitmap = null;
            mGifDecoder = null;
            mAnimationThread = null;
            mShouldClear = false;
        }
    };
    
	public GifDecoderView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void playGif(InputStream stream, DisplayMetrics metrics) {
		this.metrics = metrics;
		mGifDecoder = new GifDecoder();
		mGifDecoder.read(stream);
		mIsPlayingGif = true;
		mAnimationThread = new Thread(new Runnable() {
			public void run() {
				final int n = mGifDecoder.getFrameCount();
				final int ntimes = mGifDecoder.getLoopCount();
				int repetitionCounter = 0;

				if (mShouldClear) {
		            mHandler.post(mCleanupRunnable);
		            return;
		        }
				
				do {
					for (int i = 0; i < n; i++) {
						mTmpBitmap = mGifDecoder.getFrame(i);
						final int t = mGifDecoder.getDelay(i);
						mHandler.post(mUpdateResults);
						try {
							Thread.sleep(t);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					if (ntimes != 0) {
						repetitionCounter++;
					}
				} while (mIsPlayingGif && (repetitionCounter <= ntimes));
			}
		});
		mAnimationThread.start();
	}


    public void stopGif() {
    	mIsPlayingGif = false;

        if (mAnimationThread != null) {
        	mAnimationThread.interrupt();
        	mAnimationThread = null;
        }
    }

    public void clear() {
    	mIsPlayingGif = false;
        mShouldClear = true;
        stopGif();
    }

	public Drawable setDownloadedImageMetrices(Drawable drawable, DisplayMetrics metrics, double densityRatio,
			int imageScaleFlag) {
		int width, height;
		int originalWidthScaled = (int) (drawable.getIntrinsicWidth() * metrics.density * densityRatio);
		int originalHeightScaled = (int) (drawable.getIntrinsicHeight() * metrics.density * densityRatio);

		Log.e("dimn", "width" + originalWidthScaled + "height" + originalHeightScaled);
		if (imageScaleFlag == IMAGE_FULL_WIDTH) {
			Log.e(">>>>>>>>>", "image full width -> metrics widthpixel = " + metrics.widthPixels
					+ " drawable.getIntrinsicWidth = " + drawable.getIntrinsicWidth());
			height = drawable.getIntrinsicHeight() * metrics.widthPixels / drawable.getIntrinsicWidth();
			width = metrics.widthPixels;
		} else {
			if (originalWidthScaled > metrics.widthPixels) {
				height = drawable.getIntrinsicHeight() * metrics.widthPixels / drawable.getIntrinsicWidth();
				width = metrics.widthPixels;
			} else {
				height = originalHeightScaled;
				width = originalWidthScaled;
			}
		}
		try {
			Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
			drawable = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, width, height, true));
			// drawable.setBounds(0, 0, width, height);
			Log.e(">>>>>>>>>>>>>>",
					"image width = " + drawable.getIntrinsicWidth() + " & height = " + drawable.getIntrinsicHeight());
		} catch (Exception ex) {
		}
		return drawable;
	}
}
