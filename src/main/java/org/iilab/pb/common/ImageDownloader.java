package org.iilab.pb.common;

import android.os.AsyncTask;
import android.util.Log;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import org.iilab.pb.model.AsyncTaskResult;

/**
 * Created by aoe on 1/3/14.
 */
public class ImageDownloader extends AsyncTask {

    public interface ImageDownloadListener {
        public void onImageDownloadComplete(byte[] bitmapData);
        public void onImageDownloadFailed(Exception ex);
    }

    private ImageDownloadListener mListener = null;

    public ImageDownloader(ImageDownloadListener listener) {
        mListener = listener;
    }

    protected Object doInBackground(Object... urls) {
        String url = AppConstants.BASE_URL + (String)urls[0];
        ByteArrayOutputStream baos = null;
        InputStream mIn = null;
        try {
            long timeBeforeConnection = System.currentTimeMillis();
            URLConnection urlCon = new URL(url).openConnection();
            urlCon.setConnectTimeout(AppConstants.IMAGE_DOWNLOAD_TIMEOUT_MS);
            urlCon.setReadTimeout(AppConstants.IMAGE_DOWNLOAD_TIMEOUT_MS);
            mIn = urlCon.getInputStream();
            long timeAfterConnection = System.currentTimeMillis();
            Log.e("try", "connection timeout = " + (float)(timeAfterConnection - timeBeforeConnection)/1000 + " seconds");

            int bytesRead;
            byte[] buffer = new byte[64];
            baos = new ByteArrayOutputStream();
            while ((bytesRead = mIn.read(buffer)) > 0) {
                if (isCancelled()) return null;
                baos.write(buffer, 0, bytesRead);
            }
            return new AsyncTaskResult<byte[]>(baos.toByteArray());

        } catch (Exception ex) {
            Log.e("catch", "couldn't retrieve the image");
            return new AsyncTaskResult<byte[]>(ex);
        }
        finally {
            Log.e("finally", "couldn't retrieve the image");
            try {
                mIn.close();
                baos.close();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
    }

    protected void onPostExecute(Object objResult) {
        AsyncTaskResult<byte[]> result = (AsyncTaskResult<byte[]>)objResult;
        if (isCancelled() || result == null) return;
        if (result.getError() != null) {
            mListener.onImageDownloadFailed(result.getError());
        }
        else if (mListener != null)
            mListener.onImageDownloadComplete(result.getResult());
    }
}
