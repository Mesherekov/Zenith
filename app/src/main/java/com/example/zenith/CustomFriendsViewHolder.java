package com.example.zenith;

import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CustomFriendsViewHolder extends RecyclerView.ViewHolder {
    ImageButton imageButton;
    TextView name;
    RelativeLayout relativeLayoutfriend;
    public CustomFriendsViewHolder(@NonNull View itemView) {
        super(itemView);
        imageButton = itemView.findViewById(R.id.friendimage);
        name = itemView.findViewById(R.id.nameuserfriend);
        relativeLayoutfriend = itemView.findViewById(R.id.relativefriends);
    }
}
