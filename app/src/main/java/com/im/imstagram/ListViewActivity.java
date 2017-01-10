package com.im.imstagram;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.im.imstagram.common.HndResp;
import com.im.imstagram.datatype.PhotoEntry;
import com.im.imstagram.http.HttpFetcher;
import com.im.imstagram.ui.adapter.ListViewAdapter;
import com.im.imstagram.utils.Util4Custom;
import com.im.imstagram.utils.Utils4App;

import java.util.ArrayList;

/**
 * Created by vioooiv on 2017-01-07.
 */

public class ListViewActivity extends Activity implements AbsListView.OnScrollListener
{
    public static final String TAG = "ListViewActivity";

    static final String DEFAULT_USER_ID = "design";
    static final String DEFAULT_MAX_ID = "0";

    private boolean mIsShownLastItem = true; /* 리스트뷰 맨 밑에 있는지 판단 */
    private int mFirstVisiblePosition = 0; /* 현재 보이는 화면에서 맨 위에 아이템의 위치 */

    //private String mSearchWord = ""; /* 검색어 */

    private EditText mEditTextSearchInput = null;
    private ImageView mImageViewSearch = null;
    private ListView mListviewImage = null;

    private ListViewAdapter mListViewAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview);

        /* 초기화 */
        init();

        /* View 초기화 */
        initView();
    }

    private void init()
    {
        /* 기본값 요청 */
        getUrlFromWeb(DEFAULT_USER_ID, DEFAULT_MAX_ID);

    }

    private void initView()
    {
        /* 검색어 입력 */
        mEditTextSearchInput = (EditText) findViewById(R.id.edittext_search_input);
        mEditTextSearchInput.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
				/* xxx */
            }
        });

		/* EditText 이벤트 : 키보드 */
        mEditTextSearchInput.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        mEditTextSearchInput.setOnEditorActionListener(new EditText.OnEditorActionListener()
        {
            @Override
            public boolean onEditorAction(TextView view, int actionId, KeyEvent event)
            {
				/* EditText에 포커스 요청 : LG 폰에서 포커싱이 안되는 경우 방지 */
                if(view.isFocused() == false) {
                    view.requestFocus();
                }

				/* 키패드에서 "xxx" 버튼 클릭할 경우 */
                if(actionId == EditorInfo.IME_ACTION_SEARCH) {

                    String searchWord = mEditTextSearchInput.getText().toString().trim();
                    if(searchWord.length() > 0) {
                        mImageViewSearch.performClick();
                        return true;
                    }

                    return true;
                }

                return false;
            }
        });

        mImageViewSearch = (ImageView) findViewById(R.id.imageview_search);
        mImageViewSearch.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                /* Photo 검색 요청 */
                actionSearch();
            }
        });

        /* 리스트뷰 */
        mListviewImage = (ListView) findViewById(R.id.listview_photo);
        mListviewImage.setOnScrollListener(this);
    }


    /**
     * Photo 검색 요청
     */
    public void actionSearch()
    {
        /* user_id로 검색 요청 */
        String userId = mEditTextSearchInput.getText().toString().trim();
        if(userId == null || userId.trim().length() < 1) {
            return;
        }

        userId = userId.trim();

        if(PhotoAgent.getInstance().mSearchWord.equalsIgnoreCase(userId) == true) {
            return;
        }

		/* ListviewPhoto 화면 초기화 */
        mListViewAdapter = new ListViewAdapter(ListViewActivity.this, null);
        mListviewImage.setAdapter(mListViewAdapter);

        /* 검색어 변경시 데이터 초기화 */
        PhotoAgent.getInstance().mAlPhotoEntry = new ArrayList<PhotoEntry>();
        mListViewAdapter = null;
        PhotoEntry.mMoreAvailable = true; /* 검색 가능으로 초기화 */

        /* Get 요청 */
        getUrlFromWeb(userId, DEFAULT_MAX_ID);

        /* 키보드 Hide */
        hideSoftKeyBoard();

        //mEditTextSearchInput.setText("");
    }

    /**
     * 이미지 Url 데이터 가져오기
     */
    private void getUrlFromWeb(String userId, String maxId)
    {
        //Toast.makeText(ListViewActivity.this, "Loading started", Toast.LENGTH_LONG).show();

        /* 요청한 userId 저장 */
        PhotoAgent.getInstance().mSearchWord = userId;

        /* 추가 데이터 요청 가능 체크 */
        if(PhotoEntry.mMoreAvailable == false) {
            Log.d(TAG, "mMoreAvailable == false");
            return;
        }

        /* 메시지 핸들러 설정 */
        final HndResp hndResp = new HndResp() {
            @Override
            public void onComplete(final String msg) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        /* Json 추출 */
                        ArrayList<PhotoEntry> alPhotoEntry = PhotoEntry.convertJson2AlEntry(msg);
                        if(alPhotoEntry != null && alPhotoEntry.size() > 0) {
                            PhotoAgent.getInstance().mAlPhotoEntry.addAll(alPhotoEntry); /* 검색 결과 추가 */
                        } else {
                            Toast.makeText(ListViewActivity.this, "No Data", Toast.LENGTH_LONG).show();
                            return;
                        }

                        /* ListviewPhoto 화면 갱신 */
                        if(mListViewAdapter == null) {
                            mListViewAdapter = new ListViewAdapter(ListViewActivity.this, PhotoAgent.getInstance().mAlPhotoEntry);
                            mListviewImage.setAdapter(mListViewAdapter);
                        } else {
                            //mListViewAdapter = new ListViewAdapter(ListViewActivity.this, PhotoAgent.getInstance().mAlPhotoEntry);
                            mListViewAdapter.notifyDataSetChanged();
                        }

                    }
                });
            }

            @Override
            public void onError(final String msg) {
                Log.d(TAG, msg);
            }
        };

        /* 이미지 Url 요청 및 저장 */
        HttpFetcher.runFetcher(hndResp, Util4Custom.getUrl(userId, maxId));
    }

    /**
     * 키보드 Hide
     */
    private void hideSoftKeyBoard()
    {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    InputMethodManager inputMethodManager = (InputMethodManager) ListViewActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(mEditTextSearchInput.getWindowToken(), 0);
                } catch(Exception e) {
                    Log.e(TAG, "hideSoftKeyBoard()");
                }
            }
        });
    }

    @Override
    public void onBackPressed()
    {
        /* 뒤로가기 더블 클릭해서 종료하기 */
        Utils4App.exitSystemOnDoubleBackpressed(this);
    }

    /**
     * 리스트뷰 Scroll 상태
     */
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState)
    {
        try {
            if(view.isShown() == false) {
                return;
            }

            switch(scrollState) {
                case SCROLL_STATE_IDLE:

                    /* 리스트뷰에 마지막까지 Scroll 됐을때 : 메모리 왜곡으로 메시지 리스트의 끝에 있는 메시지가 없어지면 다시 가져옴 */
                    if(mIsShownLastItem == true) {
                        /* 마지막 maxId */
                        PhotoEntry photoEntry = PhotoAgent.getInstance().mAlPhotoEntry.get(PhotoAgent.getInstance().mAlPhotoEntry.size() -1);
                        String maxId = photoEntry.getId();

                        /* 이미지 Url 데이터 가져오기 : 추가 요청 */
                        getUrlFromWeb(PhotoAgent.getInstance().mSearchWord, maxId);
                    }

                    //mOnScrollState = SCROLL_STATE_IDLE;
                    break;

                case SCROLL_STATE_TOUCH_SCROLL:
                    //mOnScrollState = SCROLL_STATE_TOUCH_SCROLL;
                    break;

                case SCROLL_STATE_FLING:
                    //mOnScrollState = SCROLL_STATE_FLING;
                    break;
            }
        } catch(Exception e) {
            Log.e(TAG, "onScrollStateChanged()");
        }
    }

    public int getFirstVisiblePosition()
    {
        return mFirstVisiblePosition;
    }

    public void setFirstVisiblePosition(int firstVisiblePosition)
    {
        this.mFirstVisiblePosition = firstVisiblePosition;
    }

    /**
     * 리스트뷰 OnScrollListener
     */
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
    {
        try {
            /* 현재 보이는 아이템 위치 설정 */
            setFirstVisiblePosition(firstVisibleItem);

			/* 리스트뷰에서 마지막까지 Scroll 됐을때 체크 */
            if(firstVisibleItem + visibleItemCount < totalItemCount - 1) {
				/* 마지막까지 Scroll 되지 않았을때 */
                mIsShownLastItem = false;
            } else {
				/* 마지막까지 Scroll 됐을때 */
                mIsShownLastItem = true;
            }

            if(firstVisibleItem != 0) {
                return;
            }
        } catch(Exception e) {
            Log.e(TAG, "onScroll()");
        }
    }

}