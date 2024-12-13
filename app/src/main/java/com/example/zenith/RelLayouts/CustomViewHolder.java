package com.example.zenith.RelLayouts;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.zenith.R;

public class CustomViewHolder extends RecyclerView.ViewHolder {
    ImageView avatar;
    ImageView addfriend;
    TextView name;
    RelativeLayout relativeLayout;

    public CustomViewHolder(@NonNull View itemView) {
        super(itemView);
        avatar = itemView.findViewById(R.id.avatar);
        name = itemView.findViewById(R.id.nameuser);
        relativeLayout = itemView.findViewById(R.id.relative_item);
        addfriend = itemView.findViewById(R.id.addfriend);
    }
}
