package com.im.imstagram.imagecache;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.util.LruCache;

import java.io.File;

/**
 * Created by vioooiv on 2017-01-06.
 */

public class PhotoMemCache implements PhotoCache
{
    private LruCache<String, Bitmap> mLruCache;

    private static volatile PhotoMemCache instance = null;

    public static PhotoMemCache getInstance() {
        if(instance == null) {
            synchronized (PhotoMemCache.class) {
                if(instance == null) {
                    instance = new PhotoMemCache();
                }
            }
        }
        return instance;
    }

    private PhotoMemCache() {
        mLruCache = new LruCache<String, Bitmap>(DEFAULT_MEM_CACHE_COUNT);
    }

    public PhotoMemCache(int maxCount) {
        mLruCache = new LruCache<String, Bitmap>(maxCount);
    }

    @Override
    public void addBitmap(String key, Bitmap bitmap) {
        if (bitmap == null)
            return;
        mLruCache.put(key, bitmap);

        //int count = mLruCache.putCount();
        //Log.d("PhotoMemCache", "putCount : " +  count);
    }

    @Override
    public void addBitmap(String key, File bitmapFile) {
        if (bitmapFile == null)
            return;
        if (!bitmapFile.exists())
            return;

        Bitmap bitmap = BitmapFactory.decodeFile(bitmapFile.getAbsolutePath());
        mLruCache.put(key, bitmap);
    }

    @Override
    public Bitmap getBitmap(String key) {
        return mLruCache.get(key);
    }

    @Override
    public void clear() {
        mLruCache.evictAll();
    }

}