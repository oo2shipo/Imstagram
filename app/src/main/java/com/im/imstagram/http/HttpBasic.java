package com.im.imstagram.http;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Android Sample (NetworkConnect) 네트워크 클래스 사용
 */
public class HttpBasic
{
    public static final String TAG = "HttpBasic";

    public static final int CONNECTION_TIMEOUT = 15000; /* Connection Timeout */
    public static final int SOCKET_TIMEOUT = 10000; /* Read Timeout */

    /**
     * 테스트
     */
    public static void runTest() {
        new DownloadTask().execute("http://www.google.com");
    }

    /**
     * AsyncTask 이용해서 Ui thread 및 백그라운드 처리
     */
    private static class DownloadTask extends AsyncTask<String, Void, String>
    {
        /* 실행 */
        @Override
        protected String doInBackground(String... urls) {
            try {
                return fetchHttp(urls[0]);
            } catch (IOException e) {
                return "Connection error.";
            }
        }

        /* 결과 처리 및 UI 갱신 */
        @Override
        protected void onPostExecute(String result) {
            Log.i(TAG, result);
        }
    }

    /**
     * Http Fetch
     */
    public static String fetchHttp(String urlString) throws IOException {
        InputStream stream = null;
        String str ="";

        try {
            stream = requestGet(urlString);
            str = readStream(stream);
            //str = readIt(stream, 500);
        } finally {
            if (stream != null) {
                stream.close();
            }
        }
        return str;
    }

    /**
     * Http Request : GET
     */
    public static InputStream requestGet(String urlString) throws IOException {
        return requestHttp(urlString, "GET");
    }

    /**
     * Http Request : POST
     */
    public static InputStream requestPost(String urlString) throws IOException {
        return requestHttp(urlString, "POST");
    }

    /**
     * Http Request
     */
    private static InputStream requestHttp(String urlString, String type) throws IOException {
        // BEGIN_INCLUDE(get_inputstream)
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(SOCKET_TIMEOUT);
        conn.setConnectTimeout(CONNECTION_TIMEOUT);
        conn.setRequestMethod(type);
        //conn.setRequestMethod("GET");
        conn.setDoInput(true);
        // Start the query
        conn.connect();
        InputStream stream = conn.getInputStream();
        return stream;
        // END_INCLUDE(get_inputstream)
    }

    /**
     * InputStream -> String
     */
    private static String readIt(InputStream stream, int len) throws IOException, UnsupportedEncodingException
    {
        Reader reader = null;
        reader = new InputStreamReader(stream, "UTF-8");
        char[] buffer = new char[len];
        reader.read(buffer);
        return new String(buffer);
    }

    /**
     * InputStream -> String
     */
    public static String readStream(InputStream stream) throws IOException, UnsupportedEncodingException
    {
        StringBuilder stringBuilder = new StringBuilder();

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
        for(; ; ) {
            String line = bufferedReader.readLine();
            if (line == null) {
                break;
            }
            stringBuilder.append(line + '\n');
        }
        bufferedReader.close();

        return stringBuilder.toString();
    }
}