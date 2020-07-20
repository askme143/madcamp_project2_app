package com.example.madcampserverapp.ui.gallery;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import androidx.viewpager.widget.ViewPager;

import com.example.madcampserverapp.MainActivity;
import com.example.madcampserverapp.R;
import java.util.ArrayList;

public class FullImageActivity extends Activity {
    ArrayList<Image> mImageArrayList;
    ArrayList<Bitmap> mBitmapArrayList;
    int mHeight, mWidth;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.full_image);

        /* Get image index and image array list */
        Intent i = getIntent();
        int position = i.getExtras().getInt("id");
        BitmapArrayIndicator bitmapArrayIndicator = (BitmapArrayIndicator) i.getSerializableExtra("bitmapIndicator");

        mBitmapArrayList = bitmapArrayIndicator.getBitmapArrayList();
        mHeight = getResources().getDisplayMetrics().heightPixels;
        mWidth = getResources().getDisplayMetrics().widthPixels;


        /* Make an array list of IMAGEs */
        mImageArrayList = new ArrayList<>();

        for (Bitmap bitmap : mBitmapArrayList) {
            mImageArrayList.add(new Image (bitmap, mHeight, mWidth));
        }

        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        PageImageAdapter adapter = new PageImageAdapter(this, mImageArrayList);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(position);
    }

}