package com.im.imstagram.http;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

public class UrlParam
{
	private StringBuilder mStringBuilder;
	private boolean mFirstParam;
	
    public UrlParam(final String url)
    {
    	mStringBuilder = new StringBuilder(url);
    	
    	mFirstParam = true;
    }
    
    public String getUrl()
	{
        return mStringBuilder.toString();
	}

    public void addParam(String key, String value) {
        if (value == null || value.equals("")) {
            return;
        }

        mStringBuilder.append(getParamDelimiter());
        mStringBuilder.append(key);
        mStringBuilder.append("=");
        mStringBuilder.append(encodeUrl(value));
    }

    private String getParamDelimiter() {
        if (mFirstParam) {
            mFirstParam = false;
            return "?";
        }
        return "&";
    }
    
	/**
	 * URLEncoder
	 */
	public static String encodeUrl(String src) 
	{
		String result = "";
		if(src != null && src.length() > 0)
		try {
			result = URLEncoder.encode(src, "UTF-8");
		}
		catch(UnsupportedEncodingException unsupportedencodingexception) { }
		return result;
	}

	/**
	 * URLDecoder
	 */
	public static String decodeUrl(String src) 
	{
		String result = "";
		if(src != null && src.length() > 0)
		try {
			result = URLDecoder.decode(src, "UTF-8");
		}
		catch(UnsupportedEncodingException unsupportedencodingexception) { }
		return result;
	}
}
