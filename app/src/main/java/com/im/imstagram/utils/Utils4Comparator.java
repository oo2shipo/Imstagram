package com.im.imstagram.utils;

import com.im.imstagram.datatype.PhotoEntry;

import java.util.Comparator;
import java.util.Locale;


public class Utils4Comparator
{
	public static final String TAG = Utils4Comparator.class.getSimpleName();
	
	public static final int N_SORT_TYPE_ID = 1;
	
	public Utils4Comparator(){}
	
	/**
	 * 사용 예
	 */
//	/* 정렬 */
//	if(alPhotoEntry != null && alPhotoEntry.size() > 0) {
//		Comparator<Object> comparator = new Utils4Comparator().create(Utils4Comparator.N_SORT_TYPE_ID);
//		Collections.sort(alPhotoEntry, comparator);
//	}

	
	public Comparator<Object> create(int type)
	{
		Comparator<Object> comparator = null;
		
		switch(type) {
			case N_SORT_TYPE_ID:
				comparator = new Comparator4Id();
				break;
		}
		
		return comparator;
	}
	
	/**
	 * PhotoEntry sort : ID
	 */
	public class Comparator4Id implements Comparator<Object>
	{
		@Override
		public int compare(Object lhs, Object rhs) {
			if(lhs instanceof PhotoEntry && rhs instanceof PhotoEntry) {
				try {
					PhotoEntry obj1 = (PhotoEntry)lhs;
					PhotoEntry obj2 = (PhotoEntry)rhs;

					if(obj1.getId() == null) {
						return -1;
					}

					if(obj2.getId() == null) {
						return 1;
					}

					// 한글 우선 적용
					java.text.Collator collator = java.text.Collator.getInstance(Locale.KOREAN);

					int rc = collator.compare(obj1.getId(), obj2.getId());
					if(rc > 0) {
						return -1; // 오름차순
					} else if(rc < 0) {
						return 1; // 내림차순
					} else {
						return 0;
					}
				} catch(Exception e)
				{ }
			}
			
			return 0;
		}
	}
	
//	/**
//	 * PhotoEntry sort : updateTime
//	 */
//	public class Comparator4PhotoEntryTime implements Comparator<Object>
//	{
//		@Override
//		public int compare(Object lhs, Object rhs) {
//			if(lhs instanceof PhotoEntry && rhs instanceof PhotoEntry) {
//				try {
//					PhotoEntry obj1 = (PhotoEntry)lhs;
//					PhotoEntry obj2 = (PhotoEntry)rhs;
//
//					long rc = obj1.getUpdateTime() - obj2.getUpdateTime();
//
//					if(rc > 0) {
//						return -1; 
//					} else if(rc < 0) {
//						return 1;
//					}else {
//						return 0;
//					}
//
//				} catch(Exception e)
//				{ }
//			}
//			
//			return 0;
//		}
//	}
}
