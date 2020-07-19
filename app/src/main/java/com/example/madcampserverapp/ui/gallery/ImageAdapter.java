package com.example.madcampserverapp.ui.gallery;


import android.content.Context;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.madcampserverapp.R;

import java.util.ArrayList;


public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private int mCellSize;
    private ArrayList<Image> mImageArrayList;

    public ImageAdapter(Context c, int cellSize, ArrayList<Image> imageArrayList){
        mContext = c;
        mCellSize = cellSize;
        mImageArrayList = imageArrayList;
    }

    @Override
    public int getCount() {
        return mImageArrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return mImageArrayList.get(i).getScaledImage();
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ImageView imageView = new ImageView(mContext);
        imageView.setImageBitmap(mImageArrayList.get(i).getScaledImage());
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setLayoutParams(new GridView.LayoutParams(mCellSize, mCellSize));
        return imageView;
    }
}