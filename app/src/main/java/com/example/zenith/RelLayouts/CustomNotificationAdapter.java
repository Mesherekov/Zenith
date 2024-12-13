package com.example.zenith.RelLayouts;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.zenith.ItemNotification;
import com.example.zenith.R;

import java.util.List;

public class CustomNotificationAdapter extends RecyclerView.Adapter {
    Context context;
    List<ItemNotification> itemNotifications;

    public CustomNotificationAdapter(Context context, List<ItemNotification> itemNotifications) {
        this.context = context;
        this.itemNotifications = itemNotifications;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CustomNotificationViewHolder(LayoutInflater.from(context).inflate(R.layout.item_notification, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return itemNotifications.size();
    }
}
