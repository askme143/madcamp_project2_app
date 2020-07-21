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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, null);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);

        /* Initialize postArrayList */
        postArrayList = new ArrayList<>();

        /* TODO: Request and make postArrayList */
        /* TODO: Make more specific queries ("location or skip or limit, ..") */
        String url = "http://192.249.19.242:7380" + "/post/download/list";

        ContentValues contentValues = new ContentValues();
        contentValues.put("fb_id", ((MainActivity) getActivity()).getFacebookID());

        NetworkTask networkTask = new NetworkTask(url, contentValues, responsePostList);
        networkTask.execute(null);

        /* FIXME: Example post and post list. Erase below in future. */
        imageList = new ArrayList<>();
        imageList.add(((BitmapDrawable) getResources().getDrawable(R.drawable.blankpic)).getBitmap());
        imageList.add(((BitmapDrawable) getResources().getDrawable(R.drawable.mail)).getBitmap());
        imageList.add(((BitmapDrawable) getResources().getDrawable(R.drawable.mail)).getBitmap());
        imageList.add(((BitmapDrawable) getResources().getDrawable(R.drawable.mail)).getBitmap());
        Post examplePost = new Post(imageList, "신발", 10000, "대전", "어쩌고저쩌고", 0,"전우정");
        postArrayList.add(examplePost);

        /* Set adapter */
        mAdapter = new HomeRecyclerAdapter(getActivity(), postArrayList);
        recyclerView.setAdapter(mAdapter);

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
                JSONObject jsonObject = new JSONObject(new String(result));
                JSONArray postArray = jsonObject.getJSONArray("posts");

                for (int i = 0; i < postArray.length(); i++) {
                    JSONObject postDoc = postArray.getJSONObject(i);

                    ArrayList<Bitmap> goodsImages = new ArrayList<>();
                    JSONArray postImageArray = postDoc.getJSONArray("");
                    for (int j = 0; j < postImageArray.length(); j++) {
                        byte[] imageByteArray = Base64.decode(postImageArray.getString(j), Base64.DEFAULT);
                        Bitmap bitmap = BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.length);
                        goodsImages.add(bitmap);
                    }

                    String goodsName = postDoc.getString("");
                    int goodsPrice = Integer.parseInt(postDoc.getString(""));
                    String goodsLocation = postDoc.getString("");
                    String goodsDetail = postDoc.getString("");

                    int lickCount = Integer.parseInt(postDoc.getString(""));

                    String writer = postDoc.getString("");

                    Post post = new Post(goodsImages, goodsName, goodsPrice, goodsLocation, goodsDetail, lickCount, writer);
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
