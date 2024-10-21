package com.example.zenith;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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
            holder.imageView.setImageDrawable(items.get(position).getImage());
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
        }catch (Exception ex){
            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

}
