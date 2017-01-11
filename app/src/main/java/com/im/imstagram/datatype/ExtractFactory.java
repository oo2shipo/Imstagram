package com.im.imstagram.datatype;

/**
 * Created by vioooiv on 2017-01-11.
 */

public class ExtractFactory
{
    public static final int N_TYPE_I = 0; /* Instagram */
    public static final int N_TYPE_N = 1; /* Naver */

    /**
     * 사용 예
     * ArrayList<PhotoEntry> alPhotoEntry = ExtractFactory.create(ExtractFactory.N_TYPE_I).extract(msg);
     */

    public static Extract create(int type)
    {
        Extract extract = null;

        switch(type) {
            case N_TYPE_I:
                extract = new ExtractInstagram();
                break;
            case N_TYPE_N:
                extract = new ExtractNaver();
                break;
        }

        return extract;
    }
}
