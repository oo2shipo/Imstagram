/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.im.imstagram.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Build.VERSION_CODES;
import android.os.StrictMode;
import android.widget.Toast;

import com.im.imstagram.R;


/**
 * Class containing some static utility methods.
 */
public class Utils4App
{
    private Utils4App() {};

    /**
     * StrictMode (오래 걸리는 작업을 감지) 설정
     * @param target : 예) XXXActivity.class
     */
    @TargetApi(VERSION_CODES.HONEYCOMB)
    public static void enableStrictMode(Class target) {
        if (Utils4App.hasGingerbread()) {
            StrictMode.ThreadPolicy.Builder threadPolicyBuilder = new StrictMode.ThreadPolicy.Builder().detectAll().penaltyLog();
            StrictMode.VmPolicy.Builder vmPolicyBuilder = new StrictMode.VmPolicy.Builder().detectAll().penaltyLog();

            if (Utils4App.hasHoneycomb()) {
                threadPolicyBuilder.penaltyFlashScreen();
                vmPolicyBuilder.setClassInstanceLimit(target, 1);
                //vmPolicyBuilder.setClassInstanceLimit(AAAActivity.class, 1).setClassInstanceLimit(BBBActivity.class, 1);
            }
            StrictMode.setThreadPolicy(threadPolicyBuilder.build());
            StrictMode.setVmPolicy(vmPolicyBuilder.build());
        }
    }

    /**
     * 안드로이드 버전 체크
     */
    public static boolean hasFroyo() {
        // Can use static final constants like FROYO, declared in later versions
        // of the OS since they are inlined at compile time. This is guaranteed behavior.
        return Build.VERSION.SDK_INT >= VERSION_CODES.FROYO;
    }

    public static boolean hasGingerbread() {
        return Build.VERSION.SDK_INT >= VERSION_CODES.GINGERBREAD;
    }

    public static boolean hasHoneycomb() {
        return Build.VERSION.SDK_INT >= VERSION_CODES.HONEYCOMB;
    }

    public static boolean hasHoneycombMR1() {
        return Build.VERSION.SDK_INT >= VERSION_CODES.HONEYCOMB_MR1;
    }

    public static boolean hasJellyBean() {
        return Build.VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN;
    }

    public static boolean hasKitKat() {
        return Build.VERSION.SDK_INT >= VERSION_CODES.KITKAT;
    }

    /**
     * 뒤로가기 더블 클릭해서 종료하기
     */
    private static long backKeyPressedTime = 0;
    private static Toast toastExitBackpressed = null;
    public static void exitSystemOnDoubleBackpressed(final Activity activity)
    {
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            String content = activity.getString(R.string.dialog_txt_exit_backpressed); // "\'뒤로\' 버튼을 한번 더 누르면 종료됩니다."
            toastExitBackpressed = Toast.makeText(activity, content, Toast.LENGTH_SHORT);
            toastExitBackpressed.show();
            return;
        }

        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            if(toastExitBackpressed != null) {
                toastExitBackpressed.cancel();
            }

            activity.moveTaskToBack(true); /* 다른 모든 액티비티 적용 */
            activity.finish();
            systemExit(activity); /* 시스템 강제 종료 */
        }
    }

    /**
     * 시스템 종료
     */
    public static void systemExit(Activity activity)
    {
        new android.os.Handler().postDelayed(new Runnable() {
            public void run() {
                android.os.Process.killProcess(android.os.Process.myPid());
                //	System.exit(0);
            }
        }, 100 * 2);
    }
}
