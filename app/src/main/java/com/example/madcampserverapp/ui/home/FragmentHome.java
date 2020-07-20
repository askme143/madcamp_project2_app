package com.example.madcampserverapp.ui.home;

import android.content.ContentValues;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.madcampserverapp.R;
import com.example.madcampserverapp.ThreadTask;
import com.example.madcampserverapp.server.MyResponse;
import com.example.madcampserverapp.server.RequestHttpURLConnection;

import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class FragmentHome extends Fragment {
    private ArrayList<Post> postArrayList;
    private HomeRecyclerAdapter mAdapter;
    private ArrayList<Bitmap> imageList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, null);

        final RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);

        /*example code : post add*/
        imageList = new ArrayList<>();
        imageList.add(((BitmapDrawable) getResources().getDrawable(R.drawable.blankpic)).getBitmap());
        imageList.add(((BitmapDrawable) getResources().getDrawable(R.drawable.mail)).getBitmap());
        imageList.add(((BitmapDrawable) getResources().getDrawable(R.drawable.mail)).getBitmap());
        imageList.add(((BitmapDrawable) getResources().getDrawable(R.drawable.mail)).getBitmap());
        Post examplePost = new Post(imageList, "신발", 10000, "대전", "어쩌고저쩌고", 0,"전우정");

        postArrayList = new ArrayList<>();
        postArrayList.add(examplePost);

        mAdapter = new HomeRecyclerAdapter(getActivity(), postArrayList, imageList, imageList.get(1));
        recyclerView.setAdapter(mAdapter);// set the Adapter to RecyclerView

        return view;
    }

    public static class NetworkTask extends ThreadTask<Void, byte[]> {

        private String mUrl;
        private MyResponse mMyResponse;

        private Bitmap mBitmap;
        private ContentValues mValues;

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
        protected byte[] doInBackground(Void arg) {////
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
