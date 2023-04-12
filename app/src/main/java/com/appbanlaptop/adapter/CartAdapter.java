package com.appbanlaptop.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    HashMap<Integer, HashMap<String, String>> cart;

    public CartAdapter(HashMap<Integer, HashMap<String, String>> cart) {
        Log.d("CartAdapter", "Constructor call()");
        this.cart = cart;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView laptopImage;
        TextView laptopName, laptopSalePrice, laptopPrice, laptopQuantity, cartSum;
        Button btnDelete, btnDecrease, btnIncrease;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            laptopImage = itemView.findViewById(R.id.laptopImage);
            laptopName = itemView.findViewById(R.id.laptopName);
            laptopSalePrice = itemView.findViewById(R.id.laptopSalePrice);
            laptopPrice = itemView.findViewById(R.id.laptopPrice);
            laptopQuantity = itemView.findViewById(R.id.laptopQuantity);
            cartSum = itemView.findViewById(R.id.cartSum);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            btnDecrease = itemView.findViewById(R.id.btnDecrease);
            btnIncrease = itemView.findViewById(R.id.btnIncrease);
            Log.d("CartAdapter", "ViewHolder call()");
        }
    }

    @NonNull
    @Override
    public CartAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_in_cart, parent,false);
        return new CartAdapter.ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.ViewHolder holder, int position) {
        Log.d("CartAdapter", "onBlindVIewHolder call()");

        Integer[] laptopIds = this.cart.keySet().toArray(new Integer[0]); // Lấy danh sách các id trong cart

        int laptopId = laptopIds[position]; // Lấy id của laptop tại vị trí position
        Log.d("CartAdapter", String.valueOf(laptopId));

        HashMap<String, String> laptop = this.cart.get(laptopId); // Lấy thông tin của laptop tương ứng với id đó

        Glide.with(holder.itemView.getContext()).load(laptop.get("image_url")).into(holder.laptopImage);
        holder.laptopName.setText(laptop.get("name"));
        holder.laptopSalePrice.setText(laptop.get("sale_price"));
        holder.laptopPrice.setText(laptop.get("price"));
        holder.laptopQuantity.setText(laptop.get("quantity"));
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
