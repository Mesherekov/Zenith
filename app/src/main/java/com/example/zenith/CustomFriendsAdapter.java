package com.example.zenith;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.List;

public class CustomFriendsAdapter extends RecyclerView.Adapter<CustomFriendsViewHolder> {
    Context context;
    List<ItemFriends> items;
    SelectFriendsListener selectFriendsListener;
    SelectListenerDelFriend selectListenerDelFriend;

    public CustomFriendsAdapter(Context context, List<ItemFriends> items, SelectFriendsListener selectFriendsListener, SelectListenerDelFriend selectListenerDelFriend) {
        this.context = context;
        this.items = items;
        this.selectFriendsListener = selectFriendsListener;
        this.selectListenerDelFriend = selectListenerDelFriend;
    }

    @NonNull
    @Override
    public CustomFriendsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CustomFriendsViewHolder(LayoutInflater.from(context).inflate(R.layout.item_friends, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CustomFriendsViewHolder holder, @SuppressLint("RecyclerView") int position) {
        try {
            holder.name.setTextColor(items.get(position).getColortext());
            holder.name.setText(items.get(position).getName());
            holder.relativeLayoutfriend.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    selectListenerDelFriend.onLongItemClick(items.get(position), position);
                    return true;
                }
            });
            holder.relativeLayoutfriend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectFriendsListener.onItemFriendClick(items.get(position), position);
                }
            });
            Target target1 = new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    Drawable drawable = new BitmapDrawable(context.getResources(), bitmap);
                   holder.imageView.setImageDrawable(drawable);
                   items.get(position).setImage(holder.imageView.getDrawable());
                }
                @Override
                public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            };
            holder.imageView.setTag(target1);
            Picasso.get().load(items.get(position).getUri()).resize(400, 400).centerCrop().placeholder(R.drawable.profileicon).into(target1);
        }catch (Exception ex){
            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

}
