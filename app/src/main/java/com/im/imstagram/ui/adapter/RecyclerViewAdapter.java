package com.im.imstagram.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.im.imstagram.PhotoDetailActivity;
import com.im.imstagram.R;
import com.im.imstagram.datatype.PhotoEntry;
import com.im.imstagram.photo.PhotoLoaderTask;

import java.util.ArrayList;

/**
 * Created by vioooiv on 2017-01-07.
 */

public class RecyclerViewAdapter extends BaseAdapter
{
    public static final String TAG = ListViewAdapter.class.getSimpleName();

    private Activity mActivity = null;
    private ArrayList<PhotoEntry> mAlPhotoEntry = new ArrayList<PhotoEntry>();

    public RecyclerViewAdapter(Activity context, ArrayList<PhotoEntry> alPhotoEntry)
    {
        mActivity = context;
        mAlPhotoEntry = alPhotoEntry;
    }

    public ArrayList<PhotoEntry> getAlPhotoEntry()
    {
        return mAlPhotoEntry;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        try {
            RecyclerViewAdapter.ViewHolder viewHolder;

//            if(convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.item_photo, parent, false);

            viewHolder = new RecyclerViewAdapter.ViewHolder(convertView);
            convertView.setTag(viewHolder);
//            } else {
//                viewHolder = (ViewHolder) convertView.getTag();
//            }

            final PhotoEntry photoEntry = (PhotoEntry) getItem(position);

            viewHolder.imageViewPhoto.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View v)
                {
                    /* 상세 이미지 보기 */
                    final Intent intent = new Intent(mActivity, PhotoDetailActivity.class);
                    intent.putExtra(PhotoDetailActivity.EXTRA_IMAGE, (int) position);
                    mActivity.startActivity(intent);
                }
            });

            /* 이미지 로딩 */
            PhotoLoaderTask photoLoaderTask = new PhotoLoaderTask(viewHolder.imageViewPhoto, photoEntry.getStdUrl());
            photoLoaderTask.execute();

        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }

        return convertView;
    }

//    @Override
//    public View getView(final int position, View convertView, ViewGroup parent)
//    {
//        try
//        {
//            LayoutInflater layoutInflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            convertView = layoutInflater.inflate(R.layout.item_photo, parent, false);
//
//            final PhotoEntry photoEntry = (PhotoEntry) getItem(position);
//
//            ImageView ImageViewPhoto = (ImageView) convertView.findViewById(R.id.imageview_photo);
//            ImageViewPhoto.setOnClickListener(new View.OnClickListener() {
//                public void onClick(View v) {
//                    /* 상세 이미지 보기 */
//                }
//            });
//
//            /* 이미지 로딩 */
//            PhotoLoaderTask imageLoaderTask = new PhotoLoaderTask(ImageViewPhoto, photoEntry.getStdUrl());
//            imageLoaderTask.execute();
//
//        } catch(Exception e) {
//            Log.e(TAG, e.getMessage());
//        }
//
//        return convertView;
//    }


    @Override
    public int getCount()
    {
        int count = 0;

        try {
            if (mAlPhotoEntry != null) {
                count = mAlPhotoEntry.size();
            }
        } catch (Exception e) {
            Log.e(TAG, "getCount()");
        }

        return count;
    }

    @Override
    public PhotoEntry getItem(int index)
    {
        return mAlPhotoEntry.get(index);
    }

    @Override
    public long getItemId(int i)
    {
        return 0;
    }

    /**
     * ViewHolder
     */
    static class ViewHolder
    {
        ImageView imageViewPhoto;

        /* 레이아웃 설정 */
        public ViewHolder(View baseView)
        {
            imageViewPhoto = (ImageView) baseView.findViewById(R.id.imageview_photo);
        }
    }
}