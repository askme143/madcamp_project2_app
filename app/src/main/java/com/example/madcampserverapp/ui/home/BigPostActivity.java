package com.example.madcampserverapp.ui.home;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.madcampserverapp.R;
import com.example.madcampserverapp.ui.contact.FragmentContact;

import java.util.ArrayList;

public class BigPostActivity extends AppCompatActivity {
    private ArrayList<Bitmap> imageArraylist;
    private ArrayList<byte[]> imagebyteArraylist;
    private String goods_name;
    private int goods_price;
    private String goods_location;
    public int like_cnt;
    private String writer;
    private String goods_detail;
    private byte[] b;
    private Bitmap bitmap;
    private boolean click_heart;
    private int is_click;

    ImageView imageView;
    TextView textgoodsname, textgoodsprice, textgoodslocation, textlikecnt,textgoodsdetail;
    TextView textwriter;
    ImageButton imageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.big_post);
        Intent intent=getIntent();
        click_heart=false;
        is_click=0;
        bitmap=null;
        imageArraylist=new ArrayList<>();

        /*Get intent from HomeRecyclerAdapter*/
        goods_name=intent.getExtras().getString("goods_name");
        goods_price=intent.getExtras().getInt("goods_price");
        goods_location=intent.getExtras().getString("goods_location");
        like_cnt=intent.getExtras().getInt("like_cnt");
        writer=intent.getExtras().getString("writer");
        goods_detail=intent.getExtras().getString("goods_detail");
        imagebyteArraylist= (ArrayList<byte[]>) intent.getSerializableExtra("goods_byteimagelist");
        //imagebyteArraylist 얘는 intent로 arratList<byte[]>받아온거.
        //그래서 iamgeArraylist에 byte[]->bitmap으로 변환해야함!

        for (int j=1;j<imagebyteArraylist.size();j++){
            b=imagebyteArraylist.get(j);
            bitmap= BitmapFactory.decodeByteArray(b,0,b.length);
            imageArraylist.add(bitmap);
        }
        System.out.println("-----------imageArraylist : "+imageArraylist.size());
        /*View 불러오기*/
        imageView=(ImageView) findViewById(R.id.big_goods_image);
        textgoodsname=(TextView) findViewById(R.id.big_goods_name);
        textgoodsprice=(TextView) findViewById(R.id.big_goods_price);
        textgoodslocation=(TextView) findViewById(R.id.big_goods_location);
        textlikecnt=(TextView) findViewById(R.id.big_like_cnt);
        textwriter=(TextView) findViewById(R.id.big_writer);
        textgoodsdetail=(TextView) findViewById(R.id.big_goods_detail);
        imageButton=(ImageButton) findViewById(R.id.big_goods_like);

        /*Set texts and image*/
        imageView.setImageBitmap(imageArraylist.get(0));
        textgoodsname.setText(goods_name);
        textgoodsprice.setText(goods_price+"");
        textgoodslocation.setText(goods_location);
        textlikecnt.setText(like_cnt+"");
        textwriter.setText(writer);
        textgoodsdetail.setText(goods_detail);

        /*Heart Click event : like_cnt +1 */
        imageButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                click_heart=true;

                if (click_heart && is_click==0){
                    like_cnt++;
                    textlikecnt.setText(like_cnt+"");
                    click_heart=false;
                    is_click++;
                }
            }
        });
//        textlikecnt.setOnClickListener(new View.OnClickListener() {
//            ImageView heart=(ImageView) findViewById(R.id.big_heart);
//            Animation anima = AnimationUtils.loadAnimation(imageView.getContext(), R.anim.alpha);
//            @Override
//            public void onClick(View view) {
//                click_heart=true;
//                if (click_heart) {
//                    like_cnt++;
//                    click_heart = false;
//                    heart.startAnimation(anima);
//                }
//            }
//        });

        /*Writer name Click Event : goto Contacts*/
        textwriter.setOnClickListener(new View.OnClickListener(){
            FragmentContact fragmentContact=new FragmentContact();
             FragmentManager fragmentManager = getSupportFragmentManager();
             FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            @Override
            public void onClick(View view) {
                Bundle bundle=new Bundle(1);
                bundle.putString("writer_name",writer);
                fragmentContact.setArguments(bundle);
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container,fragmentContact);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();



            }
        });


    }
}
