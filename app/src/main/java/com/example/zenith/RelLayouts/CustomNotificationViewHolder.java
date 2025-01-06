package com.example.zenith.RelLayouts;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.zenith.R;

public class CustomNotificationViewHolder extends RecyclerView.ViewHolder {
    ImageView imageView;
    ImageView addnotifriend, delnotifriendp;
    TextView name, typemassage;
    RelativeLayout relativeLayoutfriend;
    public CustomNotificationViewHolder(@NonNull View itemView) {
        super(itemView);
        name = itemView.findViewById(R.id.notinameuser);
        relativeLayoutfriend = itemView.findViewById(R.id.notirelative_item);
        imageView = itemView.findViewById(R.id.avatarnot);
        addnotifriend = itemView.findViewById(R.id.notiaddtoffriend);
        delnotifriendp = itemView.findViewById(R.id.notideltoffriend);
        typemassage = itemView.findViewById(R.id.typemassage);
    }
}
