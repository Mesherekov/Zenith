package com.example.zenith;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CustomFriendsAdapter extends RecyclerView.Adapter<CustomFriendsViewHolder> {
    Context context;
    List<ItemFriends> items;

    public CustomFriendsAdapter(Context context, List<ItemFriends> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public CustomFriendsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CustomFriendsViewHolder(LayoutInflater.from(context).inflate(R.layout.item_friends, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CustomFriendsViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.name.setText(items.get(position).getName());
        holder.imageButton.setImageDrawable(items.get(position).getImage());
        holder.relativeLayoutfriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

}
