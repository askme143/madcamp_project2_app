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
        holder.goods_name.setText(post.getGoods_name());
        holder.goods_price.setText(post.getGoods_price());
        holder.goods_location.setText(post.getGoods_location());
        holder.photo.setImageDrawable(context.getResources().getDrawable(R.drawable.person_1));


    }

    @Override
    public int getItemCount() { return postArrayList.size(); }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView photo;
        TextView goods_name, goods_price, goods_location;
        ImageView like;
        TextView like_cnt;
        RelativeLayout hidden_layer;
        View line;


        public ViewHolder(View itemView){
            super(itemView);

            goods_name=(TextView) itemView.findViewById(R.id.home_goods_name);
            goods_location=(TextView)itemView.findViewById(R.id.home_goods_location);
            goods_price=(TextView) itemView.findViewById(R.id.home_goods_price);
            photo=(ImageView) itemView.findViewById(R.id.home_image);
            like=(ImageView) itemView.findViewById(R.id.home_goods_like);
            like_cnt=(TextView) itemView.findViewById(R.id.home_goods_like_cnt);


        }
    }

}
