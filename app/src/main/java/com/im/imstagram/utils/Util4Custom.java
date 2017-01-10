package com.im.imstagram.utils;

import com.im.imstagram.datatype.PhotoEntry;

import java.util.ArrayList;

/**
 * Created by vioooiv on 2016-09-20.
 */
public class Util4Custom
{
    public static final String TAG = "Util4Custom";

    public static final String HTTP_URL_PREFIX = "https://www.instagram.com/";


    /**
     * url 가져오기
     * 예) https://www.instagram.com/design/media/?max_id=132662604287759%203244_259875
     */
    public static String getUrl(String userId, String maxId)
    {
        String url = HTTP_URL_PREFIX + userId + "/media/?max_id=" + maxId;
        return url;
    }

    /**
     * Url 추출 및 저장
     */
    public static ArrayList<PhotoEntry> extractUrl(String input)
    {
        if(input == null || input.length() < 1) {
            return null;
        }

        /* Html 결과 추출 */
        ArrayList<PhotoEntry> alPhotoEntry = extractHtml(input);

        return alPhotoEntry;
    }

    /**
     * Html 결과 추출
     */
    public static ArrayList<PhotoEntry> extractHtml(String input)
    {
        if(input == null || input.length() < 1) {
            return null;
        }

        /* Html 추출 */
        ArrayList<PhotoEntry> alPhotoEntry = new ArrayList<PhotoEntry>();

        String imgUrlPart = "/Images/Thumbnails/1524/152466.jpg";
        String imgUrl = HTTP_URL_PREFIX + imgUrlPart;

        /* Html에서 Image 태그 추출 */
        //alPhotoEntry = extractImageTag(input);

        return alPhotoEntry;
    }

}
