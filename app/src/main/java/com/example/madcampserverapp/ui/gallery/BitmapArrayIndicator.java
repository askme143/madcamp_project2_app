package com.example.madcampserverapp.ui.gallery;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;

public class BitmapArrayIndicator implements Serializable {
    private ArrayList<Bitmap> mBitmapArrayList;

    public BitmapArrayIndicator(ArrayList<Bitmap> bitmapArrayList) {
        mBitmapArrayList = bitmapArrayList;
    }

    public ArrayList<Bitmap> getBitmapArrayList() { return mBitmapArrayList; }
}
