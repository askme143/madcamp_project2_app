package com.example.madcampserverapp.ui.gallery;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import androidx.viewpager.widget.ViewPager;

import com.example.madcampserverapp.MainActivity;
import com.example.madcampserverapp.R;
import com.example.madcampserverapp.ThreadTask;
import com.example.madcampserverapp.server.MyResponse;
import com.example.madcampserverapp.server.RequestHttpURLConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FullImageActivity extends Activity {
    ArrayList<Image> mImageArrayList = new ArrayList<>();
    ArrayList<Bitmap> mBitmapArrayList;
    int mHeight, mWidth;
    int mStartPosition;
    String mFacebookID;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.full_image);

        mHeight = getResources().getDisplayMetrics().heightPixels;
        mWidth = getResources().getDisplayMetrics().widthPixels;

        /* Get image index and image array list */
        Intent i = getIntent();
        mStartPosition = i.getExtras().getInt("id");
        mFacebookID = i.getExtras().getString("fb_id");

        requestBitmapArrayList();
    }

    private void requestBitmapArrayList() {
        String url = "http://192.249.19.244:1780";
        String testUrl = url + "/gallery/download";
        ContentValues contentValues = new ContentValues();
        contentValues.put("fb_id", mFacebookID);
        contentValues.put("skip_number", "0");
        contentValues.put("require_number", "0");

        MyResponse response = new MyResponse() {
            @Override
            public void response(byte[] result) {
                try {
                    JSONObject jsonObject = new JSONObject(new String(result));
                    JSONArray jsonArray = jsonObject.getJSONArray("images");

                    mBitmapArrayList = new ArrayList<>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        byte[] imageByteArray = Base64.decode(jsonArray.getJSONObject(i).getString("image"), Base64.DEFAULT);
                        Bitmap bitmap = BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.length);
                        mBitmapArrayList.add(bitmap);
                    }

                    initActivity();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        NetworkTask networkTask = new NetworkTask(testUrl, contentValues, response);
        networkTask.execute(null);
    }

    private void initActivity() {
        for (Bitmap bitmap : mBitmapArrayList) {
            mImageArrayList.add(new Image (bitmap, mHeight, mWidth));
        }

        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        PageImageAdapter adapter = new PageImageAdapter(this, mImageArrayList);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(mStartPosition);
    }

    private static class NetworkTask extends ThreadTask<Void, byte[]> {

        private String mUrl;
        private MyResponse mMyResponse;

        private Bitmap mBitmap;
        private ContentValues mValues;
        private JSONObject mJSONObject;

        public NetworkTask(String url, Bitmap bitmap, ContentValues contentValues, MyResponse myResponse) {
            mUrl = url;
            mBitmap = bitmap;
            mValues = contentValues;
            mMyResponse = myResponse;
        }

        public NetworkTask(String url, ContentValues values, MyResponse myResponse) {
            mUrl = url;
            mValues = values;
            mMyResponse = myResponse;
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected byte[] doInBackground(Void arg) {
            RequestHttpURLConnection requestHttpURLConnection = new RequestHttpURLConnection();

            if (mBitmap != null)
                return requestHttpURLConnection.uploadImage(mUrl, mBitmap, mValues);
            else if (mValues != null)
                return requestHttpURLConnection.request(mUrl, mValues);
            else
                return null;
        }

        @Override
        protected void onPostExecute(byte[] result) {
            mMyResponse.response(result);
        }
    }
}