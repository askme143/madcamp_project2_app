package com.example.madcampserverapp.ui.home;

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

        System.out.println("size of imageList in Fragment Home ::: "+imageList.size());
        mAdapter = new HomeRecyclerAdapter(getActivity(), postArrayList, imageList, imageList.get(1));
        recyclerView.setAdapter(mAdapter);// set the Adapter to RecyclerView

        return view;
    }
}
