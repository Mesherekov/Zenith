package com.example.zenith;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CustomMassageViewHolder extends RecyclerView.ViewHolder{
    TextView textmass;
    public CustomMassageViewHolder(@NonNull View itemView) {
        super(itemView);
        textmass = itemView.findViewById(R.id.massageu);
    }
}
