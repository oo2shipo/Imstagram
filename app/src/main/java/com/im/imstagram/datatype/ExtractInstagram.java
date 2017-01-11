package com.im.imstagram.datatype;

import com.im.imstagram.utils.Utils4Comparator;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import static com.im.imstagram.datatype.PhotoEntry.mMoreAvailable;

/**
 * Created by vioooiv on 2017-01-11.
 */

public class ExtractInstagram implements Extract
{
    public static final String ITEMS = "items";
    public static final String IMAGES = "images";
    public static final String MORE_AVAILABLE = "more_available";
    public static final String ID = "id";

    public static final String LOW_RESOLUTION = "low_resolution";
    public static final String THUMBNAIL = "thumbnail";
    public static final String STD_RESOLUTION = "standard_resolution";

    public static final String URL = "url";
    public static final String WIDTH = "width";
    public static final String HEIGHT = "height";

    /* Data 추출 */
    @Override
    public ArrayList<PhotoEntry> extract(String input)
    {
        return convertJson2AlEntry(input);
    }

    /**
     * json -> PhotoEntry
     */
    private PhotoEntry convertJson2Entry(String jsonString)
    {
        if(jsonString == null || jsonString.equals("") == true) {
            return null;
        }

        PhotoEntry photoEntry = null;

        try {
            int seq = 0;

            String id = "";

            String lowUrl = "";
            String lowW = "";
            String lowH = "";

            String thumbUrl = "";
            String thumbW = "";
            String thumbH = "";

            String stdUrl = "";
            String stdW = "";
            String stdH = "";

            String comment = "";
            String extra = "";

            JSONObject jsonObject = new JSONObject(jsonString);

            id = jsonObject.optString(ID);

            String images = jsonObject.optString(IMAGES);
            JSONObject jsonImages = new JSONObject(images);

            String low_resolution = jsonImages.optString(LOW_RESOLUTION);
            JSONObject jsonLowImages = new JSONObject(low_resolution);

            lowUrl = jsonLowImages.optString(URL);
            lowW = jsonLowImages.optString(WIDTH);
            lowH = jsonLowImages.optString(HEIGHT);

            String thumbnail = jsonImages.optString(THUMBNAIL);
            JSONObject jsonThumbImages = new JSONObject(thumbnail);

            thumbUrl = jsonThumbImages.optString(URL);
            thumbW = jsonThumbImages.optString(WIDTH);
            thumbH = jsonThumbImages.optString(HEIGHT);

            String standard_resolution = jsonImages.optString(STD_RESOLUTION);
            JSONObject jsonStdImages = new JSONObject(standard_resolution);

            stdUrl = jsonStdImages.optString(URL);
            stdW = jsonStdImages.optString(WIDTH);
            stdH = jsonStdImages.optString(HEIGHT);

            photoEntry = new PhotoEntry();
            photoEntry.setSeq(seq);

            photoEntry.setId(id);

            photoEntry.setLowUrl(lowUrl);
            photoEntry.setLowW(lowW);
            photoEntry.setLowH(lowH);

            photoEntry.setThumbUrl(thumbUrl);
            photoEntry.setThumbW(thumbW);
            photoEntry.setThumbH(thumbH);

            photoEntry.setStdUrl(stdUrl);
            photoEntry.setStdW(stdW);
            photoEntry.setStdH(stdH);

            photoEntry.setComment(comment);
            photoEntry.setExtra(extra);
        } catch(Exception e) {
            System.out.println("convertJson2Entry()");
        }

        return photoEntry;
    }

    /**
     * json -> PhotoEntry 리스트
     */
    private ArrayList<PhotoEntry> convertJson2AlEntry(String jsonString)
    {
        ArrayList<PhotoEntry> alPhotoEntry = new ArrayList<PhotoEntry>();
        if(jsonString == null || jsonString.equals("")) {
            return alPhotoEntry;
        }

        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            String items = jsonObject.optString(ITEMS);

			/* 추가 데이터 요청 가능 체크 */
            boolean moreAvailable = jsonObject.optBoolean(MORE_AVAILABLE);
            if(moreAvailable == false) {
                mMoreAvailable = false;
            }

            JSONArray jsonArray = new JSONArray(items);

            for(int index = 0; index < jsonArray.length(); index ++) {
                JSONObject jsonEntry = jsonArray.getJSONObject(index);
                PhotoEntry userEntry = convertJson2Entry(jsonEntry.toString());

                alPhotoEntry.add(userEntry);
            }

			/* 정렬 */
            if (alPhotoEntry != null && alPhotoEntry.size() > 0) {
                Comparator<Object> comparator = new Utils4Comparator().create(Utils4Comparator.N_SORT_TYPE_ID);
                Collections.sort(alPhotoEntry, comparator);
            }

        } catch(Exception e) {
            System.out.println("convertJson2AlEntry()");
        }

        return alPhotoEntry;
    }
}