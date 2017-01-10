package com.im.imstagram;

import com.im.imstagram.datatype.PhotoEntry;

import java.util.ArrayList;

/**
 * Created by vioooiv on 2017-01-07.
 */

public class PhotoAgent
{
    public static final String TAG = "PhotoAgent";

    public ArrayList<PhotoEntry> mAlPhotoEntry = new ArrayList<PhotoEntry>();
    public String mSearchWord = ""; /* 검색어 */

    private static volatile PhotoAgent instance = null;

    public static PhotoAgent getInstance() {
        if(instance == null) {
            synchronized (PhotoAgent.class) {
                if(instance == null) {
                    instance = new PhotoAgent();
                }
            }
        }
        return instance;
    }


}
