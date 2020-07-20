package com.example.madcampserverapp.ui.write;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.madcampserverapp.MainActivity;
import com.example.madcampserverapp.R;
import com.example.madcampserverapp.ui.gallery.Image;

import java.util.ArrayList;

public class WriteRecyclerAdapter  extends RecyclerView.Adapter <WriteRecyclerAdapter.ViewHolder>  {
    private Context mContext;
    ArrayList<Bitmap> mImageArrayList;

    public WriteRecyclerAdapter(Context context, ArrayList<Bitmap> imageArrayList){
        mContext = context;
        mImageArrayList = imageArrayList;
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
        if (position != 0) {
            Bitmap bitmap = mImageArrayList.get(position - 1);
            holder.imageView.setImageBitmap(bitmap);
        } else {
            holder.addText.setVisibility(View.VISIBLE);

            /* Set add button click listener */
            holder.addText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    /* If there are already 5 images, ignore click and toast a message */
                    if (getItemCount() == 6) {
                        Toast.makeText(mContext, "사진은 최대 5 장 까지 가능합니다.",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        ((MainActivity) mContext).startWriteImageSelection();
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mImageArrayList.size() + 1;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView addText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.image05);
            addText = (TextView) itemView.findViewById(R.id.add);
        }
    }
}
