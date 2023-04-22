package com.appbanlaptop.adapter;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.appbanlaptop.R;
import com.appbanlaptop.utils.Utils;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    HashMap<Integer, HashMap<String, String>> cart;

    public CartAdapter(HashMap<Integer, HashMap<String, String>> cart) {
        Log.d("CartAdapter", "Constructor call()");
        this.cart = cart;

        Utils.total = 0;
        if (cart != null) {
            for (Map.Entry<Integer, HashMap<String, String>> entry : cart.entrySet()) {
                HashMap<String, String> laptop = entry.getValue();
                String sale_price =laptop.get("sale_price");
                sale_price = sale_price.replace(",", "");
                sale_price = sale_price.replace("đ", "");
                int price = Integer.parseInt(sale_price);
                int quantity = Integer.parseInt(laptop.get("quantity"));
                Utils.total += price * quantity;
            }
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView laptopImage;
        TextView laptopName, laptopSalePrice, laptopPrice, laptopQuantity, laptopSum;
        Button btnDelete, btnDecrease, btnIncrease;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            laptopImage = itemView.findViewById(R.id.laptopImage);
            laptopName = itemView.findViewById(R.id.laptopName);
            laptopSalePrice = itemView.findViewById(R.id.laptopSalePrice);
            laptopPrice = itemView.findViewById(R.id.laptopPrice);
            laptopQuantity = itemView.findViewById(R.id.laptopQuantity);
            laptopSum = itemView.findViewById(R.id.laptopSum);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            btnDecrease = itemView.findViewById(R.id.btnDecrease);
            btnIncrease = itemView.findViewById(R.id.btnIncrease);
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

        Integer[] laptopIds = this.cart.keySet().toArray(new Integer[0]); // Lấy danh sách các id trong cart

        int laptopId = laptopIds[position]; // Lấy id của laptop tại vị trí position

        HashMap<String, String> laptop = this.cart.get(laptopId); // Lấy thông tin của laptop tương ứng với id đó

        Glide.with(holder.itemView.getContext()).load(laptop.get("image_url")).into(holder.laptopImage);
        holder.laptopName.setText(laptop.get("name"));
        holder.laptopSalePrice.setText(laptop.get("sale_price"));
        holder.laptopPrice.setText(laptop.get("price"));
        holder.laptopPrice.setPaintFlags(holder.laptopPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        holder.laptopQuantity.setText(laptop.get("quantity"));

        String sale_price =laptop.get("sale_price");
        sale_price = sale_price.replace(",", "");
        sale_price = sale_price.replace("đ", "");

        int laptopSumInt = Integer.parseInt(sale_price)  * Integer.parseInt(laptop.get("quantity"));
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        holder.laptopSum.setText(decimalFormat.format(laptopSumInt) + "đ");

        holder.btnIncrease.setOnClickListener(view -> {
            int newQuantity = Integer.parseInt(holder.laptopQuantity.getText().toString()) + 1;

            updateCartToSharedPreferences(holder, laptopId, newQuantity);

            Navigation.findNavController(view).navigate(R.id.menuCart);
        });

        holder.btnDecrease.setOnClickListener(view -> {
            int newQuantity = Integer.parseInt(holder.laptopQuantity.getText().toString());
            if (newQuantity >= 2) {
                newQuantity--;

                updateCartToSharedPreferences(holder, laptopId, newQuantity);
                Navigation.findNavController(view).navigate(R.id.menuCart);
            }
        });

        holder.btnDelete.setOnClickListener(view -> {
            if (cart.containsKey(laptopId)) {
                cart.remove(laptopId);
                notifyDataSetChanged();
                saveCartToSharedPreferences(cart, holder);
                Navigation.findNavController(view).navigate(R.id.menuCart);
            }
        });
    }

    private void saveCartToSharedPreferences(HashMap<Integer, HashMap<String, String>> cart, @NonNull CartAdapter.ViewHolder holder) {
        SharedPreferences sharedPreferences = holder.itemView.getContext().getSharedPreferences("CartPrefs", Activity.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = gson.toJson(cart);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("cart", json);
        editor.apply();
    }

    private void updateCartToSharedPreferences(@NonNull ViewHolder holder, int laptopId, int newQuantity) {
        SharedPreferences sharedPreferences = holder.itemView.getContext().getSharedPreferences("CartPrefs", Activity.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("cart", "");
        Type type = new TypeToken<HashMap<Integer, HashMap<String, String>>>() {}.getType();
        HashMap<Integer, HashMap<String, String>> newCart = gson.fromJson(json, type);

        if (newCart != null) {
            if (newCart.containsKey(laptopId)) {
                HashMap<String, String> laptop = newCart.get(laptopId);
                laptop.put("quantity", String.valueOf(newQuantity));
                newCart.put(laptopId, laptop);
                saveCartToSharedPreferences(newCart, holder);
            }
        }
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
