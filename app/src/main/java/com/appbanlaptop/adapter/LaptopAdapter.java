package com.appbanlaptop.adapter;

import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.appbanlaptop.R;
import com.appbanlaptop.model.Laptop;
import com.bumptech.glide.Glide;

import java.text.DecimalFormat;
import java.util.List;

public class LaptopAdapter extends RecyclerView.Adapter<LaptopAdapter.ViewHolder> {
    List<Laptop> arrayLaptop;

    public LaptopAdapter(List<Laptop> arrayLaptop) { this.arrayLaptop = arrayLaptop; }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageLaptop;
        TextView tvLaptopName, tvLaptopRam, tvLaptopRom, tvLaptopPrice, tvLaptopDescPercent, tvLaptopSalePrice;
        TextView tvLaptopScreen, tvLaptopCpu, tvLaptopCard, tvLaptopPin, tvLaptopWeight;
        Button btnShowDetailLaptop;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageLaptop = itemView.findViewById(R.id.imageLaptop);
            tvLaptopName = itemView.findViewById(R.id.tvLaptopName);
            tvLaptopRam = itemView.findViewById(R.id.tvLaptopRam);
            tvLaptopRom = itemView.findViewById(R.id.tvLaptopRom);
            tvLaptopPrice = itemView.findViewById(R.id.tvLaptopPrice);
            tvLaptopDescPercent = itemView.findViewById(R.id.tvLaptopDescPercent);
            tvLaptopSalePrice = itemView.findViewById(R.id.tvLaptopSalePrice);
            tvLaptopScreen = itemView.findViewById(R.id.tvLaptopScreen);
            tvLaptopCpu = itemView.findViewById(R.id.tvLaptopCpu);
            tvLaptopCard = itemView.findViewById(R.id.tvLaptopCard);
            tvLaptopPin = itemView.findViewById(R.id.tvLaptopPin);
            tvLaptopWeight = itemView.findViewById(R.id.tvLaptopWeight);
            btnShowDetailLaptop = itemView.findViewById(R.id.btnShowDetailLaptop);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_laptop, parent,false);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(holder.itemView.getContext()).load(arrayLaptop.get(position).getImage_url()).into(holder.imageLaptop);
        holder.tvLaptopName.setText(arrayLaptop.get(position).getName());
        holder.tvLaptopRam.setText(arrayLaptop.get(position).getRam());
        holder.tvLaptopRom.setText(arrayLaptop.get(position).getRom());
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        holder.tvLaptopPrice.setText(decimalFormat.format(arrayLaptop.get(position).getPrice()) + "đ");
        holder.tvLaptopPrice.setPaintFlags(holder.tvLaptopPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        int desPercent = (arrayLaptop.get(position).getPrice() - arrayLaptop.get(position).getSale_price())*100/arrayLaptop.get(position).getPrice();
        System.out.print(desPercent);
        holder.tvLaptopDescPercent.setText("-" + String.valueOf(desPercent) + "%");
        holder.tvLaptopSalePrice.setText(decimalFormat.format(arrayLaptop.get(position).getSale_price()) + "đ");
        holder.tvLaptopScreen.setText("Màn hình: " + arrayLaptop.get(position).getScreen());
        holder.tvLaptopCpu.setText("CPU: " + arrayLaptop.get(position).getCpu());
        holder.tvLaptopCard.setText("Card: " + arrayLaptop.get(position).getCard());
        holder.tvLaptopPin.setText("Pin: " + arrayLaptop.get(position).getPin());
        holder.tvLaptopWeight.setText("Khối lượng: " + Float.toString(arrayLaptop.get(position).getWeight()) + "kg");

        holder.btnShowDetailLaptop.setOnClickListener(view -> {
            Bundle bundle = new Bundle();
            bundle.putString("id", String.valueOf(arrayLaptop.get(position).getId()));
            Navigation.findNavController(view).navigate(R.id.laptopDetailFragment, bundle);
        });
    }

    @Override
    public int getItemCount() {
        return arrayLaptop.size();
    }
}
