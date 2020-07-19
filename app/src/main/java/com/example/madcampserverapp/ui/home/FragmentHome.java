package com.example.madcampserverapp.ui.home;

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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, null);

        final RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);

        Post examplePost = new Post(1, "신발", 10000, "대전", 3, "전우정");

        postArrayList = new ArrayList<>();
        postArrayList.add(examplePost);

        mAdapter = new HomeRecyclerAdapter(getActivity(), postArrayList);
        recyclerView.setAdapter(mAdapter);// set the Adapter to RecyclerView

        return view;
    }
}
