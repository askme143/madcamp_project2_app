package com.example.madcampserverapp.ui.write;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

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
import com.example.madcampserverapp.ui.gallery.Image;
import com.example.madcampserverapp.ui.home.FragmentHome;
import com.example.madcampserverapp.ui.home.HomeRecyclerAdapter;
import com.example.madcampserverapp.ui.home.Post;

import java.lang.reflect.Array;
import java.util.ArrayList;

import static java.lang.Integer.parseInt;

public class FragmentWrite extends Fragment {
    private String goodsName;
    private int goodsPrice;
    private String goodsDetail;
    private String goodsLocation;
    private ArrayList<Bitmap> goodsImageList = new ArrayList<>();
    private String name;

    private EditText editGoodsName;
    private EditText editGoodsPrice;
    private EditText editGoodsDetail;
    private Button postButton;

    private WriteRecyclerAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_write, null);

        /* Set linear layout manager of the recycler view */
        RecyclerView imgRecyclerView=(RecyclerView) view.findViewById(R.id.img_recycler);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(),
                LinearLayoutManager.HORIZONTAL, false);
        imgRecyclerView.setLayoutManager(linearLayoutManager);

        /* Make adapter of the recycler view */
        mAdapter = new WriteRecyclerAdapter(getActivity(), goodsImageList);
        imgRecyclerView.setAdapter(mAdapter);

        /* Edit views */
        editGoodsName= (EditText) view.findViewById(R.id.ed_goods_name);
        editGoodsPrice= (EditText) view.findViewById(R.id.ed_goods_price);
        editGoodsDetail= (EditText) view.findViewById(R.id.ed_goods_detail);

        /* Post button */
        postButton = (Button) view.findViewById(R.id.write_btn);
        postButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                Post tempPost = null;

                /* If name or price is blank */
                if ((editGoodsName.getText().toString().length() == 0) || (editGoodsPrice.getText().toString().length()) == 0) {
                    Toast myToast = Toast.makeText(getActivity(),"상품이름과 가격을 입력해주세요.", Toast.LENGTH_SHORT);
                    myToast.show();
                } else {
                    goodsName = editGoodsName.getText().toString();
                    goodsPrice = parseInt(editGoodsPrice.getText().toString());
                    goodsDetail = editGoodsDetail.getText().toString();

                    goodsLocation = ((MainActivity) getActivity()).getLocation();
                    if (goodsLocation == null) {
                        goodsLocation = "";
                    }
                    name = ((MainActivity) getActivity()).getName();

                    tempPost = new Post(goodsImageList, goodsName, goodsPrice, goodsLocation, goodsDetail, 0, name);

                    /* TODO: Upload the post / Testing */
                    String url = "http://192.249.19.244:1780" + "/post/upload";
                    String fbID = ((MainActivity) getActivity()).getFacebookID();

                    MyResponse myResponse = new MyResponse() {
                        @Override
                        public void response(byte[] result) {
                            Log.e("YAYA!", new String(result));
                        }
                    };

                    NetworkTask networkTask = new NetworkTask(url, tempPost, fbID, myResponse);
                    networkTask.execute(null);

                    /* Flush all selected images */
                    goodsImageList = new ArrayList<>();

                    /* TODO: Change to home fragment */
                }
            }
        });

        return view;
    }

    public void addWriteImage(Bitmap bitmap) {
        goodsImageList.add(bitmap);
    }

    public static class NetworkTask extends ThreadTask<Void, byte[]> {

        private String mUrl;
        private Post mPost;
        private String fbID;
        private MyResponse mMyResponse;

        public NetworkTask(String url, Post post, String fbID, MyResponse myResponse) {
            mUrl = url;
            mPost = post;
            this.fbID = fbID;
            mMyResponse = myResponse;
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected byte[] doInBackground(Void arg) {
            if (mPost != null) {
                RequestHttpURLConnection requestHttpURLConnection = new RequestHttpURLConnection();
                return requestHttpURLConnection.uploadPost(mUrl, mPost, fbID);
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(byte[] result) {
            mMyResponse.response(result);
        }
    }
}
