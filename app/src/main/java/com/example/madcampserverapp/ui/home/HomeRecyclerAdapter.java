package com.example.madcampserverapp.ui.home;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.madcampserverapp.R;
import com.example.madcampserverapp.ui.contact.ContactAdapter;

import java.util.ArrayList;

public class HomeRecyclerAdapter extends RecyclerView.Adapter <HomeRecyclerAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Post> postArrayList;
   // private Intent intent;


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
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        //data binding
        final Post post=postArrayList.get(position);
        holder.goods_name.setText(post.getGoods_name());
        holder.goods_price.setText(post.getGoods_price()+"");
        holder.goods_location.setText(post.getGoods_location());
        holder.photo.setImageDrawable(context.getResources().getDrawable(R.drawable.person_1));
        holder.writer.setText(post.getName());
        holder.like_cnt.setText(post.getLike_cnt()+"");
     //   holder.goods_detail.

        /*Click event*/
        holder.itemView.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                /*Goto big post activity*/
                int i=position;
                if (i!=RecyclerView.NO_POSITION){
                    Intent intent=new Intent(view.getContext(),BigPostActivity.class);

                    Post post1=postArrayList.get(i);
                    intent.putExtra("goods_name",post1.getGoods_name());
                    intent.putExtra("goods_price",post1.getGoods_price());
                    intent.putExtra("goods_location",post1.getGoods_location());
                    intent.putExtra("photoID",post1.getGoods_photoID());
                    intent.putExtra("like_cnt",post1.getLike_cnt());
                    intent.putExtra("writer",post1.getName());
                    //          intent.putExtra("goods_detail"),post1.getGoods_detail;

                    view.getContext().startActivity(intent);

                }
            }
        });
    }

    @Override
    public int getItemCount() { return postArrayList.size(); }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView photo;
        TextView goods_name, goods_price, goods_location;
        ImageView like;
        TextView like_cnt;
        TextView writer;
        TextView Goods_detail;

        public ViewHolder(View itemView){
            super(itemView);

            goods_name=(TextView) itemView.findViewById(R.id.home_goods_name);
            goods_location=(TextView)itemView.findViewById(R.id.home_goods_location);
            goods_price=(TextView) itemView.findViewById(R.id.home_goods_price);
    //        goods_detail=(TextView) itemView.findViewById(R.id.home_goods_detail);
            photo=(ImageView) itemView.findViewById(R.id.home_image);
            like=(ImageView) itemView.findViewById(R.id.home_goods_like);
            like_cnt=(TextView) itemView.findViewById(R.id.home_goods_like_cnt);
            writer=(TextView) itemView.findViewById(R.id.home_goods_writer);



        }
    }

}
