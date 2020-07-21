package com.example.madcampserverapp.ui.gallery;


import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.madcampserverapp.MainActivity;
import com.example.madcampserverapp.R;
import com.example.madcampserverapp.ThreadTask;
import com.example.madcampserverapp.server.MyResponse;
import com.example.madcampserverapp.server.RequestHttpURLConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

public class ImageSelectAdapter extends BaseAdapter {
    private Context mContext;
    private int mCellSize;
    private ArrayList<Image> mImageArrayList;
    Bitmap circle;
    Bitmap check;
    SparseBooleanArray checked = new SparseBooleanArray();

    private String mFacebookID;

    public ImageSelectAdapter(Context c, String facebookID, int cellSize, ArrayList<Image> imageArrayList){
        mContext = c;
        mCellSize = cellSize;
        mImageArrayList = imageArrayList;
        mFacebookID = facebookID;

        circle = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.uncheck_circle);
        circle = Bitmap.createScaledBitmap(circle, mCellSize/8, mCellSize/8, true);

        check = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.check_circle);
        check = Bitmap.createScaledBitmap(check, mCellSize/8, mCellSize/8, true);
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
    public View getView(final int i, View view, ViewGroup viewGroup) {
        @SuppressLint("ViewHolder") View newView = LayoutInflater.from(mContext).inflate(R.layout.image_delete, null);

        final ImageView imageView = newView.findViewById(R.id.image);
        final ImageView checkView = newView.findViewById(R.id.check);
        final ImageView unCheckView = newView.findViewById(R.id.uncheck);

        imageView.setImageBitmap(mImageArrayList.get(i).getScaledImage());
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setLayoutParams(new ConstraintLayout.LayoutParams(mCellSize, mCellSize));

        checkView.setLayoutParams(new ConstraintLayout.LayoutParams(mCellSize/8, mCellSize/8));
        unCheckView.setLayoutParams(new ConstraintLayout.LayoutParams(mCellSize/8, mCellSize/8));

        changeVisibility(i, checkView, unCheckView, false);

        imageView.setOnClickListener(new ImageView.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeVisibility(i, checkView, unCheckView, true);
            }
        });

        return newView;
    }

    private void changeVisibility(int i, View checkView, View unCheckView, boolean clicked) {
        if (clicked){
            if (checked.get(i)) {
                checked.delete(i);
                unCheckView.setVisibility(View.VISIBLE);
                checkView.setVisibility(View.INVISIBLE);
            } else {
                checked.put(i, true);
                unCheckView.setVisibility(View.INVISIBLE);
                checkView.setVisibility(View.VISIBLE);
            }
        } else {
            if (checked.get(i)) {
                unCheckView.setVisibility(View.INVISIBLE);
                checkView.setVisibility(View.VISIBLE);
            } else {
                unCheckView.setVisibility(View.VISIBLE);
                checkView.setVisibility(View.INVISIBLE);
            }
        }
    }

    public void deleteChecked() {
        ArrayList<Image> removeList = new ArrayList<>();
        ArrayList<Integer> removeIndexes = new ArrayList<>();
        for (int i = 0; i < mImageArrayList.size(); i++) {
            if (checked.get(i)) {
                removeList.add(mImageArrayList.get(i));
                removeIndexes.add(i);
            }
        }

        String testUrl = "http://192.249.19.244:1780" + "/gallery/delete";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("fb_id", mFacebookID);

            JSONArray jsonArray = new JSONArray();
            for (int index : removeIndexes) {
                jsonArray.put(index);
            }
            jsonObject.put("indexes", jsonArray);

            System.out.println(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        MyResponse response = new MyResponse() {
            @Override
            public void response(byte[] result) {
                if (result == null) {
                    Log.e("hello", "error");
                    /* Error handling */
                    return;
                }

                String resultString = new String(result);

                if (resultString.equals("success")) {
                    Log.e("hello", "success");
                    /* Toast success message */
                } else if (resultString.equals("fail")) {
                    Log.e("hello", "fails");
                    /* Toast fail message */
                }
            }
        };

        NetworkTask networkTask = new NetworkTask(testUrl, jsonObject, response);
        networkTask.execute(null);
        mImageArrayList.removeAll(removeList);
    }

    public static class NetworkTask extends ThreadTask<Void, byte[]> {

        private String mUrl;
        private MyResponse mMyResponse;

        private JSONObject mJSONObject;

        public NetworkTask(String url, JSONObject jsonObject, MyResponse myResponse) {
            mUrl = url;
            mJSONObject = jsonObject;
            mMyResponse = myResponse;
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected byte[] doInBackground(Void arg) {
            RequestHttpURLConnection requestHttpURLConnection = new RequestHttpURLConnection();

            if (mJSONObject != null)
                return requestHttpURLConnection.request(mUrl, mJSONObject);
            else
                return null;
        }

        @Override
        protected void onPostExecute(byte[] result) {
            mMyResponse.response(result);
        }
    }
}