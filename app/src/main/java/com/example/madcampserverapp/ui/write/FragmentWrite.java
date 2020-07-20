package com.example.madcampserverapp.ui.write;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
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
import com.example.madcampserverapp.ui.home.FragmentHome;
import com.example.madcampserverapp.ui.home.HomeRecyclerAdapter;
import com.example.madcampserverapp.ui.home.Post;

import java.lang.reflect.Array;
import java.util.ArrayList;

import static java.lang.Integer.parseInt;

public class FragmentWrite extends Fragment {
    private String goods_name;
    private int goods_price;
    private String goods_detail;
    private String goods_location;
    private ArrayList<Bitmap> goods_images;
    private String name;
    private int like_cnt;

    private EditText ed_goods_name;
    private EditText ed_goods_price;
    private EditText ed_goods_detail;
    private Button button_post;
    private ImageView imageView;
    ////////////// image, location 받아오기 //////////////
    private ArrayList<Bitmap> imageList;
    private ArrayList<Post> postArrayList;

    private WriteRecyclerAdapter mAdapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_write, null);

        RecyclerView imgRecyclerView=(RecyclerView) view.findViewById(R.id.img_recycler);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(),
                LinearLayoutManager.HORIZONTAL, false);
        imgRecyclerView.setLayoutManager(linearLayoutManager);

        /*imageList add*/
        imageList = new ArrayList<>();
//        imageList.add(((BitmapDrawable) getResources().getDrawable(R.drawable.blankpic)).getBitmap());
        imageList.add(((BitmapDrawable) getResources().getDrawable(R.drawable.mail)).getBitmap());
        imageList.add(((BitmapDrawable) getResources().getDrawable(R.drawable.mail)).getBitmap());

        mAdapter=new WriteRecyclerAdapter(getActivity(),imageList);
        imgRecyclerView.setAdapter(mAdapter);

        ed_goods_name= (EditText) view.findViewById(R.id.ed_goods_name);
        ed_goods_price= (EditText) view.findViewById(R.id.ed_goods_price);
        ed_goods_detail= (EditText) view.findViewById(R.id.ed_goods_detail);
        //add code : 임의 지정 photo id & location

        //글쓰기 저장 버튼 클릭시 post에 추가
        button_post=(Button) view.findViewById(R.id.write_btn);
        button_post.setOnClickListener(new Button.OnClickListener(){


            @Override
            public void onClick(View view) {
            Post temp_post=null;

            /*If name or price is blank*/
            if (ed_goods_name.getText().toString().length()==0 || ed_goods_price.getText().toString().length()==0) {
                Toast myToast = Toast.makeText(getActivity(),"상품이름과 가격을 입력해주세요.", Toast.LENGTH_SHORT);
                myToast.show();
            } else {
                goods_name=ed_goods_name.getText().toString();
                goods_price = parseInt(ed_goods_price.getText().toString());
                goods_detail=ed_goods_detail.getText().toString();

                temp_post=new Post(goods_images, goods_name, goods_price, goods_location,goods_detail,like_cnt, name);

                postArrayList=new ArrayList<>();
                postArrayList.add(temp_post);

                //////서버에 추가

                /*goto FragmentHome*/
                Intent intent1;
                intent1=new Intent(getActivity(), FragmentHome.class);
                startActivity(intent1);

            }

            }
        });


        return view;
    }
}
