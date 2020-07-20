package com.example.madcampserverapp.ui.gallery;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Environment;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;

public class Image {
    private int mHieght;
    private int mWidth;

    private Bitmap mScaledImage;
    private Bitmap mOriginalImage = null;

    public Image(Bitmap bitmap, int cellSize) {
        mHieght = cellSize;
        mWidth = cellSize;
        mOriginalImage = bitmap;
    }

    public Image (Bitmap bitmap, int hieght, int width) {
        mHieght = hieght;
        mWidth = width;
        mOriginalImage = bitmap;
    }

    public Bitmap getOriginalImage() {
        return mOriginalImage;
    }

    public Bitmap getScaledImage() {
        if (mScaledImage == null)
            try {
                int imageHeight = mOriginalImage.getHeight();
                int imageWidth = mOriginalImage.getWidth();

                /* Get the SCALE_FACTOR that is a power of 2 and
                    keeps both height and width larger than CELL_SIZE. */
                int scaleFactor = 1;
                if (imageHeight > mHieght || imageWidth > mWidth) {
                    final int halfHeight = imageHeight / 2;
                    final int halfWidth = imageWidth / 2;

                    while ((halfHeight / scaleFactor) >= mHieght
                            && (halfWidth / scaleFactor) >= mWidth) {
                        scaleFactor *= 2;
                    }
                }

                mScaledImage = Bitmap.createScaledBitmap(mOriginalImage, imageWidth/scaleFactor,
                        imageHeight/scaleFactor, true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        return mScaledImage;
    }
}
