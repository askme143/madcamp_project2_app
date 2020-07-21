package com.example.madcampserverapp.ui.home;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.madcampserverapp.MainActivity;
import com.example.madcampserverapp.R;

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
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_home_row, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final Post post=postArrayList.get(position);

        if (post.getGoods_images().size() != 0) {
            holder.goodsFirstImage.setImageBitmap(post.getGoods_images().get(0));
        }
        holder.goodsName.setText(post.getGoods_name());
        holder.goodsPrice.setText(post.getGoods_price()+"");
        holder.goodsLocation.setText(post.getGoods_location());
        holder.goodsDetail.setText(post.getGoods_detail());

        holder.likeCount.setText(post.getLike_cnt()+"");

        holder.writer.setText(post.getName());

        /* On item click, go to Bit Post Activity */
        holder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if (position != RecyclerView.NO_POSITION){
                    /* Get facebook id and post id. Put them in the intent as extra data */
                    String fbID = ((MainActivity) context).getFacebookID();
                    String postID = post.getPostID();

                    Intent intent = new Intent(view.getContext(),BigPostActivity.class);

                    intent.putExtra("fb_id", fbID);
                    intent.putExtra("post_id", postID);

                    view.getContext().startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() { return postArrayList.size(); }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView goodsFirstImage;
        TextView goodsName, goodsPrice, goodsLocation, goodsDetail;
        TextView likeCount;
        TextView writer;

        public ViewHolder(View itemView){
            super(itemView);

            goodsFirstImage = (ImageView) itemView.findViewById(R.id.home_image);

            goodsName = (TextView) itemView.findViewById(R.id.home_goods_name);
            goodsPrice = (TextView) itemView.findViewById(R.id.home_goods_price);
            goodsLocation = (TextView)itemView.findViewById(R.id.home_goods_location);
            goodsDetail = (TextView) itemView.findViewById(R.id.home_goods_detail);

            likeCount = (TextView) itemView.findViewById(R.id.home_goods_like_cnt);

            writer = (TextView) itemView.findViewById(R.id.home_goods_writer);
        }
    }

}
