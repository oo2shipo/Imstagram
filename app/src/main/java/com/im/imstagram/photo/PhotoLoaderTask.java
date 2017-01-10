package com.im.imstagram.photo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.widget.ImageView;

import com.im.imstagram.imagecache.PhotoMemCache;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * 사용 예)
 * PhotoLoaderTask imageLoaderTask = new PhotoLoaderTask(imageView, url);
 * imageLoaderTask.execute();
 */

public class PhotoLoaderTask extends AsyncTask<Void, Void, Bitmap>
{
	public static final String TAG = "PhotoLoaderTask";

    public static final boolean useCache = true;
	
    private ImageView mImageView;
    private String mUrlString;
    private PhotoMemCache mPhotoCache;


    public PhotoLoaderTask(ImageView imageView, String urlString) {
    	mImageView = imageView;
        mUrlString = urlString;
        mPhotoCache = PhotoMemCache.getInstance();
    }

    /**
     * URL에서 Bitmap 가져오기
     */
    @Override
    protected Bitmap doInBackground(Void... params)
    {
        Bitmap bitmap = null;

        try {

            if(useCache == false) {
                /* 캐시 사용 안함 */
                bitmap = downloadBitmap(mUrlString);
            } else {
                /* 캐시 사용 */
                bitmap = mPhotoCache.getBitmap(mUrlString);
                if(bitmap == null) {
                    bitmap = downloadBitmap(mUrlString);
                    /* resizeBitmap */
                    int scaleOnPixel = 720;
                    bitmap = PhotoDeco.resizeBitmap(bitmap, scaleOnPixel);

                    mPhotoCache.addBitmap(mUrlString, bitmap);
                }
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        } finally {
        }

        return bitmap;
    }

    /**
     * Bitmap 다운로드
     */
    protected Bitmap downloadBitmap(String urlString)
    {
        disableConnectionReuseIfNecessary();
        HttpURLConnection connection = null;

        Bitmap bitmap = null;

        try {
            final URL url = new URL(urlString);
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream inputStream = connection.getInputStream();

            //InputStream inputStream = new java.net.URL(mUrlString).openStream();

            bitmap = BitmapFactory.decodeStream(inputStream);
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }

        return bitmap;
    }

    /**
     * 프로요 이전 버전의 버그 처리
     * Workaround for bug pre-Froyo, see here for more info:
     * http://android-developers.blogspot.com/2011/09/androids-http-clients.html
     */
    public static void disableConnectionReuseIfNecessary() {
        // HTTP connection reuse which was buggy pre-froyo
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.FROYO) {
            System.setProperty("http.keepAlive", "false");
        }
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
    	mImageView.setImageBitmap(bitmap);
    }
}
