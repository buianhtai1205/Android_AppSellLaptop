package com.appbanlaptop.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.appbanlaptop.R;
import com.bumptech.glide.Glide;

import java.util.HashMap;

public class CheckoutAdapter extends RecyclerView.Adapter<CheckoutAdapter.ViewHolder>{

    HashMap<Integer, HashMap<String, String>> cart;

    public CheckoutAdapter(HashMap<Integer, HashMap<String, String>> cart) {
        this.cart = cart;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageViewLaptop;
        TextView tvLaptopName, tvLaptopSalePrice, tvLaptopCount;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewLaptop = itemView.findViewById(R.id.imageViewLaptop);
            tvLaptopName = itemView.findViewById(R.id.tvLaptopName);
            tvLaptopSalePrice = itemView.findViewById(R.id.tvLaptopSalePrice);
            tvLaptopCount = itemView.findViewById(R.id.tvLaptopCount);
        }
    }

    @NonNull
    @Override
    public CheckoutAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_in_checkout, parent,false);
        return new CheckoutAdapter.ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull CheckoutAdapter.ViewHolder holder, int position) {
        Integer[] laptopIds = this.cart.keySet().toArray(new Integer[0]); // Lấy danh sách các id trong cart

        int laptopId = laptopIds[position]; // Lấy id của laptop tại vị trí position

        HashMap<String, String> laptop = this.cart.get(laptopId); // Lấy thông tin của laptop tương ứng với id đó

        Glide.with(holder.itemView.getContext()).load(laptop.get("image_url")).into(holder.imageViewLaptop);
        holder.tvLaptopName.setText(laptop.get("name"));
        holder.tvLaptopSalePrice.setText(laptop.get("sale_price"));
        holder.tvLaptopCount.setText("Số lượng: " + laptop.get("quantity"));

    }

    @Override
    public int getItemCount() {
        if (this.cart != null) {
            return this.cart.size();
        } else {
            return 0;
        }
    }
}
