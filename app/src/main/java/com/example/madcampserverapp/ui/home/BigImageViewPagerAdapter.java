package com.example.madcampserverapp.ui.home;

import android.content.Context;
import android.graphics.Bitmap;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.madcampserverapp.R;
import com.example.madcampserverapp.ui.gallery.Image;

import java.util.ArrayList;

public class BigImageViewPagerAdapter extends PagerAdapter {
    private Context context;
    private ArrayList<Bitmap> imageArrayList;

    public BigImageViewPagerAdapter(Context context, ArrayList<Bitmap> imageArrayList){
        this.context=context;
        this.imageArrayList=imageArrayList;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position){
       View view=null;
        if (context !=null){
            LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view=inflater.inflate(R.layout.postimage_viewpager,container,false);

            ImageView imageView=view.findViewById(R.id.goods_images);
            imageView.setImageBitmap(imageArrayList.get(position));
        }
        container.addView(view) ;
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object view) {
        container.removeView((View) view);
    }

    @Override
    public int getCount() {
        return imageArrayList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return (view == (View) object);
    }
}
