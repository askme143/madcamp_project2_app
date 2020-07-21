package com.example.madcampserverapp.ui.home;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.madcampserverapp.MainActivity;
import com.example.madcampserverapp.R;
import com.example.madcampserverapp.ThreadTask;
import com.example.madcampserverapp.server.MyResponse;
import com.example.madcampserverapp.server.RequestHttpURLConnection;
import com.example.madcampserverapp.ui.contact.Contact;
import com.example.madcampserverapp.ui.write.FragmentWrite;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedHashSet;

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

        System.out.println("size of imageList in Fragment Home ::: "+imageList.size());
        mAdapter = new HomeRecyclerAdapter(getActivity(), postArrayList, imageList, imageList.get(1));
        recyclerView.setAdapter(mAdapter);// set the Adapter to RecyclerView

        return view;
    }


    /* Get All Posts from server */
    public ArrayList<Post> getAllPostList() {
        String url = "http://192.249.19.242:7380" + "/post/download";
        String fbID = ((MainActivity) getActivity()).getFacebookID();

        MyResponse myResponse = new MyResponse() {
            @Override
            public void response(byte[] result) {
                Log.e("YAYA!", new String(result));
            }
        };

        FragmentHome.NetworkTask networkTask = new FragmentHome.NetworkTask(url, fbID, myResponse);
        networkTask.execute(null);

        return postArrayList;
    }


    public static class NetworkTask extends ThreadTask<Void, byte[]> {

        private String mUrl;
        private MyResponse mMyResponse;
        private Post mPost;
        private String fbID;
        private Bitmap mBitmap;
        private ContentValues mValues;

        public NetworkTask(String url, String fbID, MyResponse myResponse) {
            mUrl = url;
            this.fbID = fbID;
            mMyResponse = myResponse;
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected byte[] doInBackground(Void arg) {////
            RequestHttpURLConnection requestHttpURLConnection = new RequestHttpURLConnection();


                return null;
        }

        @Override
        protected void onPostExecute(byte[] result) {
            mMyResponse.response(result);
        }
    }
}
