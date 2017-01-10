package com.im.imstagram.common;


/**
 * 메시지 핸들러
 */

public abstract class HndResp
{
	public static final String TAG = HndResp.class.getSimpleName();
	
	public HndResp()
	{
	}

	/**
	 * 요청이 성공되었을때 호출되는 매서드
	 */
	public abstract void onComplete(String msg);

	/**
	 * 요청이 실패했을 때 호출되는 매서드
	 */
	public abstract void onError(String msg);
}
