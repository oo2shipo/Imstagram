package com.im.imstagram;

import android.annotation.TargetApi;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageButton;
import android.widget.Toast;

import com.im.imstagram.common.HndResp;
import com.im.imstagram.datatype.ExtractFactory;
import com.im.imstagram.datatype.PhotoEntry;
import com.im.imstagram.http.HttpFetcher;
import com.im.imstagram.ui.PhotoDetailFragment;
import com.im.imstagram.utils.Util4Custom;

import java.util.ArrayList;

import static com.im.imstagram.R.id.pager;

/**
 * Created by vioooiv on 2017-01-07.
 */

public class PhotoDetailActivity extends FragmentActivity implements OnClickListener
{
    public static final String TAG = "PhotoDetailActivity";

    public static final String EXTRA_IMAGE = "extra_image";

    private PhotoPagerAdapter mAdapter;
    private ViewPager mPager;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_detail);

        getWindow().addFlags(LayoutParams.FLAG_FULLSCREEN);

        /* View 초기화 */
        initView();

    }

    private void initView()
    {
        /* ViewPager 설정 */
        mAdapter = new PhotoPagerAdapter(getSupportFragmentManager(), PhotoAgent.getInstance().mAlPhotoEntry);
        mPager = (ViewPager) findViewById(pager);
        mPager.setAdapter(mAdapter);
        mPager.setPageMargin((int) getResources().getDimension(R.dimen.horizontal_page_margin));
        mPager.setOffscreenPageLimit(2);

        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrollStateChanged(int state) { }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) { }

            @Override
            public void onPageSelected(int position) {
                /* 마지막 페이지에서 추가 요청 */
                if(position >= mPager.getAdapter().getCount() - 1) {
                    Log.d(TAG, "last page");

                    /* 마지막 maxId */
                    PhotoEntry photoEntry = PhotoAgent.getInstance().mAlPhotoEntry.get(PhotoAgent.getInstance().mAlPhotoEntry.size() -1);
                    String maxId = photoEntry.getId();

                    /* 이미지 Url 데이터 가져오기 : 추가 요청 */
                    request(PhotoAgent.getInstance().mSearchWord, maxId);
                }
            }
        });

        /* 현재 아이템 설정 */
        final int extraCurrentItem = getIntent().getIntExtra(EXTRA_IMAGE, -1);
        if (extraCurrentItem != -1) {
            mPager.setCurrentItem(extraCurrentItem);
        }

        ImageButton buttonCancel = (ImageButton) findViewById(R.id.button_cancel);
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * 이미지 Url 데이터 요청
     */
    private void request(String userId, String maxId)
    {
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
                        ArrayList<PhotoEntry> alPhotoEntry = ExtractFactory.create(ExtractFactory.N_TYPE_I).extract(msg);
                        if(alPhotoEntry != null && alPhotoEntry.size() > 0) {
                            PhotoAgent.getInstance().mAlPhotoEntry.addAll(alPhotoEntry); /* 검색 결과 추가 */
                        } else {
                            Toast.makeText(PhotoDetailActivity.this, "No Data", Toast.LENGTH_LONG).show();
                            return;
                        }

                        /* ViewPager 화면 갱신 */
                        if(mAdapter == null) {
                            mAdapter = new PhotoPagerAdapter(getSupportFragmentManager(), PhotoAgent.getInstance().mAlPhotoEntry);
                            mPager.setAdapter(mAdapter);
                        } else {
                            //mAdapter = new PhotoPagerAdapter(getSupportFragmentManager(), PhotoAgent.getInstance().mAlPhotoEntry);
                            mAdapter.notifyDataSetChanged();
                            //mPager.setAdapter(mAdapter);
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
     * PhotoPagerAdapter
     */
    private class PhotoPagerAdapter extends FragmentStatePagerAdapter
    {
        private ArrayList<PhotoEntry> mAlPhotoEntry = new ArrayList<PhotoEntry>();
        private int size;

        public PhotoPagerAdapter(FragmentManager fm, ArrayList<PhotoEntry> alPhotoEntry) {
            super(fm);
            mAlPhotoEntry = alPhotoEntry;
        }

        @Override
        public int getCount() {
            int size = 0;
            if(mAlPhotoEntry != null) {
                size = mAlPhotoEntry.size();
            }
            return size;
        }

        @Override
        public Fragment getItem(int position) {
            String url = mAlPhotoEntry.get(position).getStdUrl();

            return PhotoDetailFragment.newInstance(url);
        }
    }

    /**
     * Set on the ImageView in the ViewPager children fragments, to enable/disable low profile mode
     * when the ImageView is touched.
     */
    @TargetApi(VERSION_CODES.HONEYCOMB)
    @Override
    public void onClick(View v) {
        final int vis = mPager.getSystemUiVisibility();
        if ((vis & View.SYSTEM_UI_FLAG_LOW_PROFILE) != 0) {
            mPager.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
        } else {
            mPager.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
        }
    }
}