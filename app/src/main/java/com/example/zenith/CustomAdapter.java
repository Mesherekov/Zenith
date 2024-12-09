package com.example.zenith;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import javax.xml.namespace.QName;

public class CustomAdapter extends RecyclerView.Adapter<CustomViewHolder> {
    Context context;
    List<Item> items;
    private SelectListener listener;
    private AddFriendListener addlistener;
    @SuppressLint("NotifyDataSetChanged")
    public void setFilterList(List<Item> filterList){
        this.items = filterList;
        notifyDataSetChanged();
    }
    public CustomAdapter(Context context, List<Item> items, SelectListener listener, AddFriendListener addlistener) {
        this.context = context;
        this.items = items;
        this.listener = listener;
        this.addlistener = addlistener;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CustomViewHolder(LayoutInflater.from(context).inflate(R.layout.item_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.name.setText(items.get(position).getName());
        holder.name.setTextColor(items.get(position).getColortext());
        holder.avatar.setImageDrawable(items.get(position).getImage());
        holder.addfriend.setOnClickListener(view -> {
            addlistener.addFriendOnClick(items.get(position), position);
        });
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClick(items.get(position), position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
