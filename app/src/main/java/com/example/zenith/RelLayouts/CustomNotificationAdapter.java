package com.example.zenith.RelLayouts;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.zenith.ItemNotification;
import com.example.zenith.OnClickListeners.AddFriendNotiListener;
import com.example.zenith.OnClickListeners.DelFriendListener;
import com.example.zenith.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.List;

public class CustomNotificationAdapter extends RecyclerView.Adapter<CustomNotificationViewHolder> {
    Context context;
    List<ItemNotification> itemNotifications;
    AddFriendNotiListener addFriendNotiListener;
    DelFriendListener delFriendListener;

    public CustomNotificationAdapter(Context context, List<ItemNotification> itemNotifications, AddFriendNotiListener addFriendNotiListener, DelFriendListener delFriendListener) {
        this.context = context;
        this.itemNotifications = itemNotifications;
        this.addFriendNotiListener = addFriendNotiListener;
        this.delFriendListener = delFriendListener;
    }

    @NonNull
    @Override
    public CustomNotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CustomNotificationViewHolder(LayoutInflater.from(context).inflate(R.layout.item_notification, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull CustomNotificationViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.name.setTextColor(itemNotifications.get(position).getColortext());
        holder.name.setText(itemNotifications.get(position).getName());
        holder.typemassage.setText(itemNotifications.get(position).getTypeMassage());
        if(itemNotifications.get(position).getName().length()>=11){
            holder.name.setText(itemNotifications.get(position).getName().substring(0, 8)+"...");
        }
        holder.addnotifriend.setOnClickListener(view -> {
            addFriendNotiListener.OnClickNotiListener(itemNotifications.get(position), position);
        });
        holder.delnotifriendp.setOnClickListener(view -> {
            delFriendListener.delFriendOnClick(itemNotifications.get(position), position);
        });
        Target target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                Drawable drawable = new BitmapDrawable(context.getResources(), bitmap);
                holder.imageView.setImageDrawable(drawable);
                itemNotifications.get(position).setImage(holder.imageView.getDrawable());
            }
            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };
        holder.imageView.setTag(target);
        Picasso.get().load(itemNotifications.get(position).getUri()).resize(400, 400).centerCrop().placeholder(R.drawable.profileicon).into(target);
    }

    @Override
    public int getItemCount() {
        return itemNotifications.size();
    }
}
