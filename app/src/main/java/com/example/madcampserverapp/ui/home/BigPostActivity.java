package com.example.madcampserverapp.ui.home;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.madcampserverapp.R;

public class BigPostActivity extends Activity {
    private long photoID;
    private String goods_name;
    private int goods_price;
    private String goods_location;
    private int like_cnt;
    private String writer;
    private String goods_detail;

    ImageView imageView;
    TextView textgoodsname, textgoodsprice, textgoodslocation, textlikecnt,textgoodsdetail;
    TextView textwriter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.big_post);
        Intent intent=getIntent();

        /*Get intent from HomeRecyclerAdapter*/
        photoID=intent.getExtras().getLong("photoID");
        goods_name=intent.getExtras().getString("goods_name");
        goods_price=intent.getExtras().getInt("goods_price");
        goods_location=intent.getExtras().getString("goods_location");
        like_cnt=intent.getExtras().getInt("like_cnt");
        writer=intent.getExtras().getString("writer");
   //     goods_detail=intent.getExtras().getString("goods_detail");

        /*View 불러오기*/
        imageView=(ImageView) findViewById(R.id.big_goods_image);
        textgoodsname=(TextView) findViewById(R.id.big_goods_name);
        textgoodsprice=(TextView) findViewById(R.id.big_goods_price);
        textgoodslocation=(TextView) findViewById(R.id.big_goods_location);
        textlikecnt=(TextView) findViewById(R.id.big_like_cnt);
        textwriter=(TextView) findViewById(R.id.big_writer);
    //    textgoodsdetail=(TextView) findViewById(R.id.big_goods_detail);

        /*Set texts and image*/
        //imageView.setImage ???
        textgoodsname.setText(goods_name);
        textgoodsprice.setText(goods_price+"");
        textgoodslocation.setText(goods_location);
        textlikecnt.setText(like_cnt+"");
        textwriter.setText(writer);
    //    textgoodsdetail.setText(goods_detail);


    }
}
