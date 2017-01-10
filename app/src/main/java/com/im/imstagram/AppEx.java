package com.im.imstagram;

import android.util.Log;

/**
 * Application 클래스
 */
public class AppEx extends android.support.multidex.MultiDexApplication // android.support.multidex.MultiDexApplication // android.app.Application
{
    public static final String TAG = "AppEx";

    private static AppEx mApplication = null;

    @Override
    public void onCreate()
    {
        super.onCreate();

        mApplication = this;

        try
        {
			/* Google Analytics */
            //String versionName = this.getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            //EasyTracker.getInstance().setContext(this);
            //EasyTracker.getTracker().setAppVersion("android_" + versionName);
        } catch (Exception e) {
            Log.e(TAG, "onCreate()");
        }

        /* 초기화 */
        init();
    }

    private void init()
    {
    }

    public static AppEx getApp()
    {
        return mApplication;
    }
}
