package com.example.zenith;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
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
            //holder.textmass.getBackground().setTint(R.color.LightGreen);
            ViewCompat.setBackgroundTintList(holder.textmass, ContextCompat.getColorStateList(context, R.color.LightBlue));
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.ALIGN_PARENT_END);
            params.setMargins(20, 0, 20, 0);
            holder.textmass.setLayoutParams(params);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
