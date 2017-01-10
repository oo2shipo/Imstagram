package com.im.imstagram.http;

import android.os.AsyncTask;
import android.util.Log;

import com.im.imstagram.common.HndResp;

import java.io.IOException;

/**
 * Created by vioooiv on 2016-09-20.
 */
public class HttpFetcher extends HttpBasic
{
    public static final String TAG = "HttpFetcher";

    public static void runFetcher(String urlString) {
        new DownloadTask().execute(urlString);
    }

    /**
     * 백그라운드 및 핸들러 처리
     */
    public static void runFetcher(final HndResp hndResp, final String urlString)
    {
        if(urlString == null || urlString.trim().length() < 1) {
            return;
        }

        new Thread(new Runnable() {
            public void run() {
                try {
		    		/* Get 요청 */
                    String resp = fetchHttp(urlString);

                    /* Url 추출 및 저장 */
                    //Util4Custom.extractUrl(resp);

                    hndResp.onComplete(resp);
                } catch(Exception e) {
                    Log.e(TAG, e.getMessage());
                } finally {
                }
            }
        }).start();
    }

    /**
     * AsyncTask 이용해서 Ui thread 및 백그라운드 처리
     */
    public static class DownloadTask extends AsyncTask<String, Void, String>
    {
        /* 실행 */
        @Override
        protected String doInBackground(String... urls) {
            try {
                String result = fetchHttp(urls[0]);

                /* Url 추출 및 저장 */
                //Util4Custom.extractUrl(result);

                return result;
            } catch (IOException e) {
                return "Connection error.";
            }
        }

        /* UI 갱신만 처리 */
        @Override
        protected void onPostExecute(String result) {
            Log.i(TAG, result);

        }
    }

}
