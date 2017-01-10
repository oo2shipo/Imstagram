package com.im.imstagram;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends Activity
{
    public static final String TAG = "MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* 초기 설정 후 이동 */
        delayedStart();
    }

    /**
     * 초기 설정 후 이동
     */
    private void delayedStart()
    {
        /* 초기 설정 후 이동 */
//        Handler mHandler = new Handler();
//        mHandler.postDelayed(new Runnable() {
//            public void run() {
        Intent intent = new Intent(MainActivity.this, ListViewActivity.class);
//                Intent intent = new Intent(MainActivity.this, RecyclerViewActivity.class);
                startActivity(intent);
                finish();
//            }
//        }, 100); //Delay time
    }

}