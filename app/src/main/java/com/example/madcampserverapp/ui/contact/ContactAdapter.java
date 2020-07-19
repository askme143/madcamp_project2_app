package com.example.madcampserverapp.ui.contact;

import android.animation.ValueAnimator;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.madcampserverapp.R;

import java.util.ArrayList;
public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.MyViewHolder>{
    private ArrayList<Contact> contactList;
    private Context context;
    private SparseBooleanArray selectedItems = new SparseBooleanArray();
    private int prePosition = -1;

    public ContactAdapter(Context context, ArrayList<Contact> contactList) {
        this.context = context;
        this.contactList = contactList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_contact_row, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        /* Contact object */
        final Contact contact = contactList.get(position);

        /* Set text and default image */
        holder.name.setText(contact.getName());
        holder.phone_number.setText(contact.getPhoneNumber());
        holder.photo.setImageDrawable(context.getResources().getDrawable(R.drawable.person_1));

        /* Change visibility of a detail part. */
        changeVisibility(holder.hidden_layer, selectedItems.get(position));

        /* Click listener for the accordion ui */
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedItems.get(position)) {
                    selectedItems.delete(position);
                } else {
                    selectedItems.delete(prePosition);
                    selectedItems.put(position, true);
                }

                if (prePosition != -1) notifyItemChanged(prePosition);
                notifyItemChanged(position);

                prePosition = position;
            }
        });

        /* Call icon click listener */
        holder.call.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"
                        + contact.getPhoneNumber()));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

        /* Message icon click listener */
        holder.message.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:"
                        + contact.getPhoneNumber()));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    /* If the row was selected, close the row.
        Otherwise, open the row. */
    private void changeVisibility(final View v, final boolean isExpanded) {
        float d = context.getResources().getDisplayMetrics().density;
        ValueAnimator va = isExpanded ? ValueAnimator.ofInt(0, (int) (40 * d)) : ValueAnimator.ofInt((int) (40 * d), 0);
        int duration = isExpanded ? 60 : 1;

        va.setDuration(duration);
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                v.getLayoutParams().height = (int) animation.getAnimatedValue();
                v.requestLayout();
                v.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
            }
        });

        va.start();
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView name, phone_number;
        ImageView photo;
        RelativeLayout hidden_layer;
        ImageButton call, message;

        public MyViewHolder(View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.name);
            phone_number = (TextView) itemView.findViewById(R.id.phoneNumber);
            photo = (ImageView) itemView.findViewById(R.id.dial_image);
            hidden_layer = (RelativeLayout) itemView.findViewById(R.id.hiddenButtons);
            call = (ImageButton) itemView.findViewById(R.id.call);
            message = (ImageButton) itemView.findViewById(R.id.message);
        }
    }
}
