package com.im.imstagram.http;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.net.ssl.HttpsURLConnection;

/** 
 * 자바 네트워크 클래스 사용
 */
public class HttpAgent
{
	public static final String TAG = HttpAgent.class.getSimpleName();

	public static final int CONNECTION_TIMEOUT = 5000; /* Connection Timeout */
	public static final int SOCKET_TIMEOUT = 10000; /* Read Timeout */
	
	/** 
	 * Get 요청
	 */
	public static String requestGet(String url)
	{
		String result = "";
		
		/* Header 설정 */
		HashMap<String, String> hmHeader = new HashMap<String, String>();
		hmHeader.put("Accept", "application/json");
		hmHeader.put("Content-Type", "application/json");
		
		String body = "";
		
		if(url.toLowerCase().contains("https://") == true) {
			/* Https 설정 */
			result = requestHttps(url, "GET", hmHeader, body);
		} else {
			/* Http 설정 */
			result = requestHttp(url, "GET", hmHeader, body);
		}
		
		return result;
	}
	
	/** 
	 * Post 요청
	 */
	public static String requestPost(String url)
	{
		String result = "";
		
		/* Header 설정 */
		HashMap<String, String> hmHeader = new HashMap<String, String>();
		hmHeader.put("Accept", "application/json");
		hmHeader.put("Content-Type", "application/json");
		
		String body = "";
		
		if(url.toLowerCase().contains("https://") == true) {
			/* Https 설정 */
			result = requestHttps(url, "POST", hmHeader, body);
		} else {
			/* Http 설정 */
			result = requestHttp(url, "POST", hmHeader, body);
		}
		
		return result;
	}
	
	/** 
	 * Put 요청
	 */
	public static String requestPut(String url)
	{
		String result = "";
		
		/* Header 설정 */
		HashMap<String, String> hmHeader = new HashMap<String, String>();
		hmHeader.put("Accept", "application/json");
		hmHeader.put("Content-Type", "application/json");
		
		String body = "";
		
		if(url.toLowerCase().contains("https://") == true) {
			/* Https 설정 */
			result = requestHttps(url, "Put", hmHeader, body);
		} else {
			/* Http 설정 */
			result = requestHttp(url, "Put", hmHeader, body);
		}
		
		return result;
	}
	
	/** 
	 * Delete 요청
	 */
	public static String requestDelete(String url)
	{
		String result = "";
		
		/* Header 설정 */
		HashMap<String, String> hmHeader = new HashMap<String, String>();
		hmHeader.put("Accept", "application/json");
		hmHeader.put("Content-Type", "application/json");
		
		String body = "";
		
		if(url.toLowerCase().contains("https://") == true) {
			/* Https 설정 */
			result = requestHttps(url, "Delete", hmHeader, body);
		} else {
			/* Http 설정 */
			result = requestHttp(url, "Delete", hmHeader, body);
		}
		
		return result;
	}
	
	/** 
	 * Http 요청 <-- 주의!! HTTP_OK 결과가 아닐때 결과값을 안 읽음 
	 * type : POST, PUT, DELETE, GET
	 */
	public static String requestHttp(String url, String type, HashMap<String, String> hmHeader, String body)
	{
		StringBuilder stringBuilder = new StringBuilder(); 
		
		URL uri = null;
		HttpURLConnection con = null;
		try {
			uri = new URL(url);
			con = (HttpURLConnection) uri.openConnection();
			con.setRequestMethod(type); /* resp 가 없을때 주석처리 */
			con.setDoOutput(true); /* resp 가 없을때 주석처리 */
            con.setDoInput(true);
			con.setConnectTimeout(CONNECTION_TIMEOUT);
			con.setReadTimeout(SOCKET_TIMEOUT);
			con.setUseCaches(false);
			
			/* Header에 요청 데이터 타입 설정 */
			//con.setRequestProperty("Accept", "application/json");
			
			/* Header에 전달 데이터 타입 */
			//con.setRequestProperty("Content-Type", "application/json");
			
			/* Header 설정 */
			if(hmHeader != null && hmHeader.size() > 0) {
				for(Entry<String, String> entry : hmHeader.entrySet()) {
					String field = entry.getKey();
					String value = entry.getValue();
					
					con.setRequestProperty(field, value);
				}
			}
			
			/* Body 데이터 설정 */
			if(body != null && body.length() > 0) {
				OutputStream os = con.getOutputStream();
				os.write(body.getBytes("UTF-8"));
				os.flush();
				os.close();
			}
			
			int r = con.getResponseCode();
			
			if(con.getResponseCode() == HttpURLConnection.HTTP_OK) {
				BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
				for(;;) {
					String line = bufferedReader.readLine();
					if(line == null) {
						break;
					}
					
					stringBuilder.append(line + '\n'); 
				}
				
				bufferedReader.close();
			}
			con.disconnect();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return stringBuilder.toString();
	}
	
	/** 
	 * Https 요청 <-- 주의!! HTTP_OK 결과가 아닐때 결과값을 안 읽음 
	 * type : POST, PUT, DELETE, GET
	 */
	public static String requestHttps(String url, String type, HashMap<String, String> hmHeader, String body)
	{
		StringBuilder stringBuilder = new StringBuilder(); 
		
		URL uri = null;
		HttpsURLConnection con = null;
		try {
			uri = new URL(url);
			con = (HttpsURLConnection) uri.openConnection();
			con.setRequestMethod(type);
			con.setDoOutput(true);
            con.setDoInput(true);
			con.setConnectTimeout(CONNECTION_TIMEOUT);
			con.setReadTimeout(SOCKET_TIMEOUT);
			con.setUseCaches(false);
			
			/* Header에 요청 데이터 타입 설정 */
			//con.setRequestProperty("Accept", "application/json");
			
			/* Header에 전달 데이터 타입 */
			//con.setRequestProperty("Content-Type", "application/json");
			
			/* Header 설정 */
			if(hmHeader != null && hmHeader.size() > 0) {
				for(Entry<String, String> entry : hmHeader.entrySet()) {
					String field = entry.getKey();
					String value = entry.getValue();
					
					con.setRequestProperty(field, value);
				}
			}
			
			/* Body 데이터 설정 */
			if(body != null && body.length() > 0) {
				OutputStream os = con.getOutputStream();
				os.write(body.getBytes("UTF-8"));
				os.flush();
				os.close();
			}
			
			if(con.getResponseCode() == HttpsURLConnection.HTTP_OK) {
				BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
				for(;;) {
					String line = bufferedReader.readLine();
					if(line == null) {
						break;
					}
					
					stringBuilder.append(line + '\n'); 
				}
				
				bufferedReader.close();
			}
			con.disconnect();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return stringBuilder.toString();
	}
	
}
