package com.example.madcampserverapp.ui.home;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.ContactsContract;
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

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class HomeRecyclerAdapter extends RecyclerView.Adapter <HomeRecyclerAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Post> postArrayList;
    private ArrayList<Bitmap> imageList;
    private ArrayList<byte[]> byteimagelist;
    private Bitmap first_img;
    private ByteArrayOutputStream stream=new ByteArrayOutputStream();
    private byte[] b;
    private Bitmap bitmap;


    public HomeRecyclerAdapter(Context context, ArrayList<Post> postArrayList, ArrayList<Bitmap> imageList, Bitmap first_img){
        this.context=context;
        this.postArrayList=postArrayList;
        this.imageList=imageList;
        this.first_img=first_img;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate item layout
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_home_row, parent, false);
        System.out.println("size of imageList in HomeRecyclerAdapter ::: "+imageList.size());
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        //data binding
        final Post post=postArrayList.get(position);
        holder.goods_name.setText(post.getGoods_name());
        holder.goods_price.setText(post.getGoods_price()+"");
        holder.goods_location.setText(post.getGoods_location());
        holder.writer.setText(post.getName());
        holder.like_cnt.setText(post.getLike_cnt()+"");
        holder.goods_detail.setText(post.getGoods_detail());
        holder.goods_firstimg.setImageBitmap(first_img);


        /*Click event*/
        holder.itemView.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
            /*Goto big post activity*/
            int i=position;
            if (i!=RecyclerView.NO_POSITION){
                Intent intent=new Intent(view.getContext(),BigPostActivity.class);

   //   post1.getGoods_images() 이거 받아왔으니까 여기서 byte[] 담은 배열로 변환해서 새로만든 arraylist<byte[]>에 담고 이걸 보냄
                byteimagelist=new ArrayList<>();
                Post post1=postArrayList.get(i);

                imageList=post1.getGoods_images(); // bitmap arrayList

                for (int j=1;j<imageList.size();j++) {
                    imageList.get(j).compress(Bitmap.CompressFormat.JPEG,100,stream); //////////////////
                    byte[] b=stream.toByteArray();
                    byteimagelist.add(b);
                }
                System.out.println("size of imageList in HomeRecyclerAdapter at this point-1 ::: "+imageList.size());
                System.out.println("size of byteimageList in HomeRecyclerAdapter at this point-1 ::: "+byteimagelist.size());

                intent.putExtra("goods_name", post1.getGoods_name());
                intent.putExtra("goods_price", post1.getGoods_price());
                intent.putExtra("goods_location", post1.getGoods_location());
                intent.putExtra("goods_byteimagelist", byteimagelist);
              // intent.putExtra("goods_imageList",post1.getGoods_images()); //Send bitmap arrayList to BigPostActivity
                intent.putExtra("like_cnt", post1.getLike_cnt());
                intent.putExtra("writer", post1.getName());
                intent.putExtra("goods_detail", post1.getGoods_detail());

                view.getContext().startActivity(intent);
            }
            }
        });
    }

    @Override
    public int getItemCount() { return postArrayList.size(); }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView goods_firstimg;
        TextView goods_name, goods_price, goods_location;
        ImageView like;
        TextView like_cnt;
        TextView writer;
        TextView goods_detail;

        public ViewHolder(View itemView){
            super(itemView);

            goods_name=(TextView) itemView.findViewById(R.id.home_goods_name);
            goods_location=(TextView)itemView.findViewById(R.id.home_goods_location);
            goods_price=(TextView) itemView.findViewById(R.id.home_goods_price);
            goods_detail=(TextView) itemView.findViewById(R.id.home_goods_detail);
          //  photo=(ImageView) itemView.findViewById(R.id.home_image);
            like=(ImageView) itemView.findViewById(R.id.home_goods_like);
            like_cnt=(TextView) itemView.findViewById(R.id.home_goods_like_cnt);
            writer=(TextView) itemView.findViewById(R.id.home_goods_writer);
            goods_firstimg=(ImageView) itemView.findViewById(R.id.home_image); //view에는 first_img만 띄움
        }
    }

}
