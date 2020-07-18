package com.example.madcampserverapp.ui.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.madcampserverapp.R;
import com.example.madcampserverapp.ui.contact.CustomAdapter;

import java.util.ArrayList;

public class HomeRecyclerAdapter extends RecyclerView.Adapter <HomeRecyclerAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Post> postArrayList;

    public HomeRecyclerAdapter(Context context, ArrayList<Post> postArrayList){
        this.context=context;
        this.postArrayList=postArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate item layout
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_home_row, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //data binding
        final Post post=postArrayList.get(position);
        holder.email

    }

    @Override
    public int getItemCount() { return postArrayList.size(); }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView photo;
        TextView goods_name, goods_price, goods_location;
        ImageView like;
        RelativeLayout hidden_layer;
        View line;


        public ViewHolder(View itemView){
            super(itemView);


        }
    }

}
