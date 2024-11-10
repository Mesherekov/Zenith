package com.example.zenith;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
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
    private final SelectMassageListener listener;

    public CustomMassageAdapter(Context context, List<ItemMassage> items, SelectMassageListener listener) {
        this.context = context;
        this.items = items;
        this.listener = listener;
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @NonNull
    @Override
    public CustomMassageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CustomMassageViewHolder(LayoutInflater.from(context).inflate(R.layout.item_massage_view, parent, false));
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull CustomMassageViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.textmass.setText(items.get(position).getTextMassage());
        holder.massrel.setOnLongClickListener(view -> {
            listener.onItemMassageClick(items.get(position));
            if (items.get(position).getTextistrigger()){
                //ViewCompat.setBackgroundTintList(holder.massrel, ContextCompat.getColorStateList(context, items.get(position).getColorrelat()));
                holder.massrel.setBackgroundColor(items.get(position).getColorrelat());
            } else {
                holder.massrel.setBackgroundColor(R.color.Transparent);
            }
            return false;
        });
        items.get(position).setListener(() -> holder.massrel.setBackgroundColor(items.get(position).getColorrelat()));
        if(items.get(position).getOwnMassage()){
            //holder.textmass.getBackground().setTint(R.color.LightGreen);
            ViewCompat.setBackgroundTintList(holder.textmass, ContextCompat.getColorStateList(context, R.color.LightBlue));
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.ALIGN_PARENT_END);
            params.setMargins(20, 0, 20, 0);
            holder.sendimage.setLayoutParams(params);
            holder.textmass.setLayoutParams(params);
        }
        if(items.get(position).getSendYourImage()!=null){
            holder.sendimage.setImageDrawable(items.get(position).getSendYourImage());
            holder.sendimage.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
