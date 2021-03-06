package com.example.madcampserverapp.ui.home;

import android.content.ContentValues;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

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
import com.example.madcampserverapp.ui.contact.ContactAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FragmentHome extends Fragment {
    private static final String TAG = "FragmentHome";

    private ArrayList<Post> postArrayList;
    private HomeRecyclerAdapter mAdapter;
    private ArrayList<Bitmap> imageList;
    private EditText editText;
    private ImageButton refreshImageButton;
    private RecyclerView mRecyclerView;
    private ImageButton xButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, null);

        /* Set EditText View */
        editText = (EditText) view.findViewById(R.id.search_bar);

        mRecyclerView= view.findViewById(R.id.recyclerView);
        editText.addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i_, int i1, int i2) { }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i_, int i1, int i2) {
                        ArrayList<Post> postList_retrieve = new ArrayList<>();

                        final String searchContent = editText.getText().toString();

                        for (int i = 0; i < postArrayList.size(); i++) {
                            if (postArrayList.get(i).getGoods_location().contains(searchContent) ) {
                                postList_retrieve.add(postArrayList.get(i));
                            }
                        }
                        HomeRecyclerAdapter postAdapter_retrieve = new HomeRecyclerAdapter(getActivity(), postList_retrieve);
                        mRecyclerView.setAdapter(postAdapter_retrieve);
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                        mRecyclerView.setLayoutManager(linearLayoutManager);
                    }

                    @Override
                    public void afterTextChanged(Editable editable) { }
                }
        );

        /* X image button : delete all edittext */
        xButton = (ImageButton) view.findViewById(R.id.x_btn);
        xButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editText.setText("");
            }
        });

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);

        /* Initialize postArrayList */
        postArrayList = new ArrayList<>();

        /* Set adapter */
        mAdapter = new HomeRecyclerAdapter(getActivity(), postArrayList);
        recyclerView.setAdapter(mAdapter);

        /* TODO: Request and make postArrayList */
        /* TODO: Make more specific queries ("location or skip or limit, ..") */
        String url = "http://192.249.19.242:7380" + "/post/download/list";

        ContentValues contentValues = new ContentValues();
        contentValues.put("fb_id", ((MainActivity) getActivity()).getFacebookID());

        Log.e(TAG, "Hello");
        NetworkTask networkTask = new NetworkTask(url, contentValues, responsePostList);
        networkTask.execute(null);

        /* Refresh Image Button */
//        refreshImageButton = (ImageButton) view.findViewById(R.id.download_post);
//        refreshImageButton.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v) {
//                String url = "http://192.249.19.242:7380" + "/post/download/list";
//
//                ContentValues contentValues = new ContentValues();
//                contentValues.put("fb_id", ((MainActivity) getActivity()).getFacebookID());
//
//                NetworkTask networkTask = new NetworkTask(url, contentValues, responsePostList);
//                networkTask.execute(null);
//            }
//        });


        /* FIXME: Example post and post list. Erase below in future. */
//        imageList = new ArrayList<>();
//        imageList.add(((BitmapDrawable) getResources().getDrawable(R.drawable.blankpic)).getBitmap());
//        imageList.add(((BitmapDrawable) getResources().getDrawable(R.drawable.mail)).getBitmap());
//        imageList.add(((BitmapDrawable) getResources().getDrawable(R.drawable.mail)).getBitmap());
//        imageList.add(((BitmapDrawable) getResources().getDrawable(R.drawable.mail)).getBitmap());
//        Post examplePost = new Post(imageList, "신발", 10000, "대전", "어쩌고저쩌고", 0,"전우정");
//        postArrayList.add(examplePost);

        return view;
    }

    private MyResponse responsePostList = new MyResponse() {
        @Override
        public void response(byte[] result) {
            if (result == null) {
                Log.e(TAG, "Fail to receive a response");
                return;
            }
            try {
                Log.e(TAG, new String(result));
                JSONObject jsonObject = new JSONObject(new String(result));
                JSONArray postArray = jsonObject.getJSONArray("posts");

                for (int i = 0; i < postArray.length(); i++) {
                    JSONObject postDoc = postArray.getJSONObject(i);

                    ArrayList<Bitmap> goodsImages = new ArrayList<>();
                    JSONArray postImageArray = postDoc.getJSONArray("images");
                    for (int j = 0; j < postImageArray.length(); j++) {
                        byte[] imageByteArray = Base64.decode(postImageArray.getString(j), Base64.DEFAULT);
                        Bitmap bitmap = BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.length);
                        goodsImages.add(bitmap);
                    }

                    String goodsName = postDoc.getString("name");
                    int goodsPrice = Integer.parseInt(postDoc.getString("price"));
                    String goodsLocation = postDoc.getString("location");
                    String goodsDetail = postDoc.getString("detail");

                    int lickCount = Integer.parseInt(postDoc.getString("like_count"));

                    String writer = postDoc.getString("writer");

                    String postID = postDoc.getString("_id");

                    Post post = new Post(goodsImages, goodsName, goodsPrice, goodsLocation, goodsDetail, lickCount, writer, postID);
                    postArrayList.add(post);
                }

                mAdapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };


    public static class NetworkTask extends ThreadTask<Void, byte[]> {
        private String mUrl;
        private ContentValues mValues;
        private MyResponse mMyResponse;

        public NetworkTask(String url, ContentValues contentValues, MyResponse myResponse) {
            mUrl = url;
            mValues = contentValues;
            mMyResponse = myResponse;
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected byte[] doInBackground(Void arg) {
            RequestHttpURLConnection requestHttpURLConnection = new RequestHttpURLConnection();

            if (mValues != null)
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
