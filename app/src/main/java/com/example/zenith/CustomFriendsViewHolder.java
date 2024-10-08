package com.example.zenith;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CustomFriendsViewHolder extends RecyclerView.ViewHolder {
    ImageView imageView;
    TextView name;
    RelativeLayout relativeLayoutfriend;
    public CustomFriendsViewHolder(@NonNull View itemView) {
        super(itemView);
        name = itemView.findViewById(R.id.nameuserfriend);
        relativeLayoutfriend = itemView.findViewById(R.id.relativefriends);
        imageView = itemView.findViewById(R.id.avatarfriend);
    }
}
