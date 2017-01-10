package com.im.imstagram.datatype;

import com.im.imstagram.utils.Utils4Comparator;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class PhotoEntry
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

//	public static final String SEQ = "seq";
//	public static final String COMMENTS = "comments";
//	public static final String EXTRA = "extra";

	public static boolean mMoreAvailable = true;

	private int seq = 0;

	private String id = "";

	private String lowUrl = "";
	private String lowW = "";
	private String lowH = "";

	private String thumbUrl = "";
	private String thumbW = "";
	private String thumbH = "";

	private String stdUrl = "";
	private String stdW = "";
	private String stdH = "";

	private String comment = "";
	private String extra = "";

	public int getSeq()
	{
		return seq;
	}

	public void setSeq(int seq)
	{
		this.seq = seq;
	}

	public String getId()
	{
		if(id == null) {
			return "";
		}
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getLowUrl()
	{
		return lowUrl;
	}

	public void setLowUrl(String lowUrl)
	{
		this.lowUrl = lowUrl;
	}

	public String getLowW()
	{
		return lowW;
	}

	public void setLowW(String lowW)
	{
		this.lowW = lowW;
	}

	public String getLowH()
	{
		return lowH;
	}

	public void setLowH(String lowH)
	{
		this.lowH = lowH;
	}

	public String getThumbUrl()
	{
		return thumbUrl;
	}

	public void setThumbUrl(String thumbUrl)
	{
		this.thumbUrl = thumbUrl;
	}

	public String getThumbW()
	{
		return thumbW;
	}

	public void setThumbW(String thumbW)
	{
		this.thumbW = thumbW;
	}

	public String getThumbH()
	{
		return thumbH;
	}

	public void setThumbH(String thumbH)
	{
		this.thumbH = thumbH;
	}

	public String getStdUrl()
	{
		return stdUrl;
	}

	public void setStdUrl(String stdUrl)
	{
		this.stdUrl = stdUrl;
	}

	public String getStdW()
	{
		return stdW;
	}

	public void setStdW(String stdW)
	{
		this.stdW = stdW;
	}

	public String getStdH()
	{
		return stdH;
	}

	public void setStdH(String stdH)
	{
		this.stdH = stdH;
	}

	public String getComment()
	{
		if(comment == null) {
			return "";
		}
		return comment;
	}

	public void setComment(String comment)
	{
		this.comment = comment;
	}

	public String getExtra()
	{
		return extra;
	}

	public void setExtra(String extra)
	{
		this.extra = extra;
	}


	/**
	 * json -> PhotoEntry
	 */
	public static PhotoEntry convertJson2Entry(String jsonString)
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
	 * PhotoEntry -> json
	 */
	public static JSONObject convertEntry2Json(PhotoEntry repoEntry)
	{
		if(repoEntry == null)
		{
			return null;
		}

		JSONObject jsonObject = new JSONObject();

		try {
			///jsonObject.put(STD_RESOLUTION, photoEntry.getStdUrl());
		} catch(Exception e) {
			System.out.println("convertEntry2Json()");
		}

		return jsonObject;
	}

	/**
	 * json -> PhotoEntry 리스트
	 */
	public static ArrayList<PhotoEntry> convertJson2AlEntry(String jsonString)
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
				PhotoEntry userEntry = PhotoEntry.convertJson2Entry(jsonEntry.toString());

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

	/**
	 * PhotoEntry -> json
	 */
	public static JSONArray convertAlEntry2Json(ArrayList<PhotoEntry> alPhotoEntry)
	{
		if(alPhotoEntry == null || alPhotoEntry.size() < 1) {
			return null;
		}

		JSONArray jsonArray = new JSONArray();

		try {
			for(PhotoEntry photoEntry : alPhotoEntry) {
				JSONObject jsonObject = new JSONObject();
				///jsonObject.put(STD_RESOLUTION, photoEntry.getStdUrl());
			}
		} catch(Exception e) {
			System.out.println("convertAlEntry2Json()");
		}

		return jsonArray;
	}
}
