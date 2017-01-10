package com.im.imstagram.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.im.imstagram.R;
import com.im.imstagram.photo.PhotoLoaderTask;

/**
 * Created by vioooiv on 2017-01-07.
 */

public class PhotoDetailFragment extends Fragment
{
    private static final String IMAGE_DATA_EXTRA = "extra_image_data";

    private String mImageUrl;
    private ImageView mImageView;
    private ProgressBar mProgressBar;


    public PhotoDetailFragment() {}

    /**
     * new instance를 생성하는 Factory method
     */
    public static PhotoDetailFragment newInstance(String imageUrl)
    {
        final PhotoDetailFragment f = new PhotoDetailFragment();

        final Bundle args = new Bundle();
        args.putString(IMAGE_DATA_EXTRA, imageUrl);
        f.setArguments(args);

        return f;
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
    }

    /**
     * 시작 onPause onStop 에서 resume 할때 호출
     */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mImageUrl = getArguments() != null ? getArguments().getString(IMAGE_DATA_EXTRA) : null;
    }

    /**
     * 처음 UI 그림
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        final View view = inflater.inflate(R.layout.fragment_photo_detail, container, false);
        mImageView = (ImageView) view.findViewById(R.id.imageview_photo);
        //mProgressBar = (ProgressBar) view.findViewById(R.id.progressbar);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        /* 이미지 로딩 */
        PhotoLoaderTask photoLoaderTask = new PhotoLoaderTask(mImageView, mImageUrl);
        photoLoaderTask.execute();
    }

    @Override
    public void onStart()
    {
        super.onStart();
    }

    @Override
    public void onResume()
    {
        super.onResume();
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
    }
}