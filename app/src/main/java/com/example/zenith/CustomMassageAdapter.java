package com.example.zenith;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CustomMassageAdapter extends RecyclerView.Adapter<CustomMassageViewHolder> {
    Context context;
    List<ItemMassage> items;

    public CustomMassageAdapter(Context context, List<ItemMassage> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public CustomMassageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CustomMassageViewHolder(LayoutInflater.from(context).inflate(R.layout.item_massage_view, parent, false));
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull CustomMassageViewHolder holder, int position) {
        holder.textmass.setText(items.get(position).getTextMassage());
        if(items.get(position).getOwnMassage()){
            holder.textmass.setBackgroundColor(R.color.LightBlue);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.ALIGN_PARENT_END);
            holder.textmass.setLayoutParams(params);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
