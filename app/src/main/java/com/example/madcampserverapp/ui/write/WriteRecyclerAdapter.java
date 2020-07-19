package com.example.madcampserverapp.ui.write;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.madcampserverapp.R;
import com.example.madcampserverapp.ui.home.HomeRecyclerAdapter;
import com.example.madcampserverapp.ui.home.Post;

import java.util.ArrayList;

public class WriteRecyclerAdapter  extends RecyclerView.Adapter <WriteRecyclerAdapter.ViewHolder>  {
    private Context context;
    ArrayList<Bitmap> bitmapArrayList;

    public WriteRecyclerAdapter(Context context, ArrayList<Bitmap> bitmapArrayList){
        this.context=context;
        this.bitmapArrayList=bitmapArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        /*inflate item layout*/
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_write_image, parent, false);
        return new WriteRecyclerAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        /*Image bitmap data binding*/
        Bitmap bitmap=bitmapArrayList.get(position);
        holder.iamgeView.setImageBitmap(bitmap);
    }

    @Override
    public int getItemCount() {
        return bitmapArrayList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iamgeView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            iamgeView=(ImageView) itemView.findViewById(R.id.image05);

        }
    }
}
