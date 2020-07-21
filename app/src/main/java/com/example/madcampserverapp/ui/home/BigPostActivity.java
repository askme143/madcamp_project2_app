package com.example.madcampserverapp.ui.home;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.example.madcampserverapp.MainActivity;
import com.example.madcampserverapp.R;
import com.example.madcampserverapp.ThreadTask;
import com.example.madcampserverapp.server.MyResponse;
import com.example.madcampserverapp.server.RequestHttpURLConnection;
import com.example.madcampserverapp.ui.contact.FragmentContact;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.zip.Inflater;

public class BigPostActivity extends AppCompatActivity {
    private static final String TAG = "BitPostActivity";

    private String mFbID;
    private String mPhotoID;

    private boolean liked;
    private Post post = null;

    private ViewPager viewPager;
    private BigImageViewPagerAdapter bigImageViewPagerAdapter;

    TextView textGoodsName, textGoodsPrice, textGoodsLocation, textLikeCnt, textGoodsDetail;
    TextView textWriter;
    ImageButton likeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.big_post);

        /* Get facebook id and photo id */
        Intent intent = getIntent();
        mFbID = intent.getStringExtra("fb_id");
        mPhotoID = intent.getStringExtra("photo_id");

        /* Get views */
        viewPager = (ViewPager) findViewById(R.id.post_view_pager);
        textGoodsName = (TextView) findViewById(R.id.big_goods_name);
        textGoodsPrice = (TextView) findViewById(R.id.big_goods_price);
        textGoodsLocation = (TextView) findViewById(R.id.big_goods_location);
        textGoodsDetail = (TextView) findViewById(R.id.big_goods_detail);

        textLikeCnt = (TextView) findViewById(R.id.big_like_cnt);

        textWriter = (TextView) findViewById(R.id.big_writer);
        likeButton = (ImageButton) findViewById(R.id.big_goods_like);

        /* TODO: Request and a get post object */
        /* TODO: Make more specific queries ("location or skip or limit, ..") */
        String url = "http://192.249.19.242:7380" + "/post/download/list";

        ContentValues contentValues = new ContentValues();
        contentValues.put("fb_id", mFbID);
        contentValues.put("photo_id", mPhotoID);

        NetworkTask networkTask = new NetworkTask(url, contentValues, responsePost);
        networkTask.execute(null);
    }

    private MyResponse responsePost = new MyResponse() {
        @Override
        public void response(byte[] result) {
            if (result == null) {
                Log.e(TAG, "Fail to get response");
                return;
            }

            /* Get post and liked */
            try {
                JSONObject postDoc = new JSONObject(new String(result));

                ArrayList<Bitmap> goodsImages = new ArrayList<>();
                JSONArray postImageArray = postDoc.getJSONArray("goods_images");
                for (int j = 0; j < postImageArray.length(); j++) {
                    byte[] imageByteArray = Base64.decode(postImageArray.getString(j), Base64.DEFAULT);
                    Bitmap bitmap = BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.length);
                    goodsImages.add(bitmap);
                }

                String goodsName = postDoc.getString("goods_name");
                int goodsPrice = Integer.parseInt(postDoc.getString("goods_price"));
                String goodsLocation = postDoc.getString("goods_location");
                String goodsDetail = postDoc.getString("goods_detail");

                int lickCount = Integer.parseInt(postDoc.getString("like_count"));

                String writer = postDoc.getString("writer");

                post = new Post(goodsImages, goodsName, goodsPrice, goodsLocation, goodsDetail, lickCount, writer);
                liked = Boolean.parseBoolean(postDoc.getString("liked"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            /* Draw and set listener */
            drawView();
            setListener();
        }
    };

    private void drawView() {
        /* Set views */
        textGoodsName.setText(post.getGoods_name());
        textGoodsPrice.setText(post.getGoods_price() + "");
        textGoodsLocation.setText(post.getGoods_location());
        textGoodsDetail.setText(post.getGoods_detail());

        textLikeCnt.setText(post.getLike_cnt() + "");
        textWriter.setText(post.getName());

        /* Set viewpager Adapter */
        bigImageViewPagerAdapter = new BigImageViewPagerAdapter(this, post.getGoods_images());
        viewPager.setAdapter(bigImageViewPagerAdapter);
        viewPager.setCurrentItem(0);
    }

    private void setListener() {
        /* Like click listener */
        likeButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if (!liked){
                    post.increaseLikeCnt();
                    textLikeCnt.setText(post.getLike_cnt() + "");
                    liked = true;

                    /* TODO: Notify to the server */
                }
            }
        });

        /* Contact click listener: Go to contact tab and search automatically */
        textWriter.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), MainActivity.class);

                intent.putExtra("writer_name", post.getName());

                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                startActivity(intent);
            }
        });
    }

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
