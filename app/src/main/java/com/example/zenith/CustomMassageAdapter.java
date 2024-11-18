package com.example.zenith;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

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
            ((RelativeLayout.LayoutParams) holder.textmass.getLayoutParams()).addRule(RelativeLayout.ALIGN_PARENT_END);
            RelativeLayout.LayoutParams paramsimage = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            ((RelativeLayout.LayoutParams) holder.sendimage.getLayoutParams()).addRule(RelativeLayout.ALIGN_PARENT_END);
            ((RelativeLayout.LayoutParams) holder.sendimage.getLayoutParams()).addRule(RelativeLayout.ALIGN_PARENT_TOP);
            ((RelativeLayout.LayoutParams) holder.textmass.getLayoutParams()).setMargins(20, 0, 20, 0);
            //holder.textmass.setWidth(holder.sendimage.getWidth());
        }
        if(!items.get(position).getUriImage().equals("null")){
            try {
                Target target = new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        Drawable drawable = new BitmapDrawable(context.getResources(), bitmap);
                        holder.sendimage.setImageDrawable(drawable);
                    }

                    @Override
                    public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {
                    }
                };
                holder.sendimage.setTag(target);
                Picasso.get().load(items.get(position).getUriImage()).resize(400, 400).centerCrop().placeholder(R.drawable.profileicon).into(target);
            } catch (Exception ex){
                Toast.makeText(context, ex.getMessage(), Toast.LENGTH_SHORT).show();
            }
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
