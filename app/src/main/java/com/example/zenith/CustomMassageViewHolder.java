package com.example.zenith;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CustomMassageViewHolder extends RecyclerView.ViewHolder{
    TextView textmass;
    RelativeLayout massrel;
    ImageView sendimage;
    public CustomMassageViewHolder(@NonNull View itemView) {
        super(itemView);
        textmass = itemView.findViewById(R.id.massageu);
        massrel = itemView.findViewById(R.id.massrel);
        sendimage = itemView.findViewById(R.id.sendimage);
    }
}
