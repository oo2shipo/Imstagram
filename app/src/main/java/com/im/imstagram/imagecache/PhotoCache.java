package com.im.imstagram.imagecache;

import android.graphics.Bitmap;

import java.io.File;

/**
 * Created by vioooiv on 2017-01-06.
 */

public interface PhotoCache
{
    public static final int DEFAULT_MEM_CACHE_COUNT = 1024; // 갯수

    public static final int DEFAULT_CACHE_SIZE = (int) (Runtime.getRuntime().maxMemory() / 1024) / 8;
    public static final int DEFAULT_MEM_CACHE_SIZE = 1024 * 5; // 5MB (KB)
    public static final int DEFAULT_DISK_CACHE_SIZE = 1024 * 10; // 10MB (KB)

    public void addBitmap(String key, Bitmap bitmap);

    public void addBitmap(String key, File bitmapFile);

    public Bitmap getBitmap(String key);

    public void clear();
}
