package com.appbanlaptop.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.appbanlaptop.R;
import com.appbanlaptop.model.Brand;
import com.bumptech.glide.Glide;

import java.util.List;

public class BrandAdapter extends RecyclerView.Adapter<BrandAdapter.ViewHolder> {
    List<Brand> arrayBrand;

    public BrandAdapter(List<Brand> arrayBrand) {
        this.arrayBrand = arrayBrand;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView image_url;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image_url =itemView.findViewById(R.id.item_brand_image);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_brand, parent,false);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(holder.itemView.getContext()).load(arrayBrand.get(position).getImage_url()).into(holder.image_url);
    }

    @Override
    public int getItemCount() {
        return arrayBrand.size();
    }

}
