package com.appbanlaptop.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.appbanlaptop.R;
import com.appbanlaptop.model.OrderHistory;
import com.appbanlaptop.model.OrderModel;
import com.appbanlaptop.model.UserModel;
import com.appbanlaptop.retrofit.ApiShopLapTop;
import com.appbanlaptop.retrofit.RetrofitClient;
import com.appbanlaptop.utils.Utils;
import com.bumptech.glide.Glide;

import java.text.DecimalFormat;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.ViewHolder> {
    List<OrderHistory> arrayOrderHistory;

    public OrderHistoryAdapter(List<OrderHistory> arrayOrderHistory) {
        this.arrayOrderHistory = arrayOrderHistory;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewLaptop;
        TextView tvLaptopName, tvLaptopCount, tvLaptopSalePrice, tvStatus;
        LinearLayout layoutReceiveOrder, layoutFeedback;
        Button btnReceiveOrder, btnReturnOrder, btnFeedback;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewLaptop = itemView.findViewById(R.id.imageViewLaptop);

            tvLaptopName = itemView.findViewById(R.id.tvLaptopName);
            tvLaptopCount = itemView.findViewById(R.id.tvLaptopCount);
            tvLaptopSalePrice = itemView.findViewById(R.id.tvLaptopSalePrice);
            tvStatus = itemView.findViewById(R.id.tvStatus);

            layoutReceiveOrder = itemView.findViewById(R.id.layoutReceiveOrder);
            layoutFeedback = itemView.findViewById(R.id.layoutFeedback);

            btnReceiveOrder = itemView.findViewById(R.id.btnReceiveOrder);
            btnReturnOrder = itemView.findViewById(R.id.btnReturnOrder);
            btnFeedback = itemView.findViewById(R.id.btnFeedback);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_history, parent,false);
        return new OrderHistoryAdapter.ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderHistoryAdapter.ViewHolder holder, int position) {
        Glide.with(holder.itemView.getContext()).load(arrayOrderHistory.get(position).getImage_url()).into(holder.imageViewLaptop);
        holder.tvLaptopName.setText(arrayOrderHistory.get(position).getName());
        holder.tvLaptopCount.setText("Số lượng: " + String.valueOf(arrayOrderHistory.get(position).getQuantity()));
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        holder.tvLaptopSalePrice.setText(decimalFormat.format(arrayOrderHistory.get(position).getSale_price()) + "đ");

        holder.layoutReceiveOrder.setVisibility(View.GONE);
        holder.layoutFeedback.setVisibility(View.GONE);

        int status = arrayOrderHistory.get(position).getStatus();
        String statusString;
        switch (status) {
            case 0:
                statusString = "Đơn hàng đang chờ xử lý.";
                break;
            case -1:
                statusString = "Đơn hàng bị hủy vì lỗi không mong muốn.";
                break;
            case 1:
                statusString = "Đơn hàng đã được xác nhận.";
                break;
            case 2:
                statusString = "Đơn hàng đã được giao cho đơn vị vận chuyển.";
                break;
            case 3:
                statusString = "Đơn hàng đã được giao thành công.";
                if (arrayOrderHistory.get(position).getIsReceived() == 0) {
                    holder.layoutReceiveOrder.setVisibility(View.VISIBLE);
                } else if (arrayOrderHistory.get(position).getIsReceived() == 1) {
                    holder.layoutFeedback.setVisibility(View.VISIBLE);
                }
                break;
            case 4:
                statusString = "Đơn hàng giao thất bại.";
                break;
            default:
                statusString = "Lỗi hệ thống.";
                break;
        }
        holder.tvStatus.setText(statusString);

        holder.btnReceiveOrder.setOnClickListener(view -> {
            if (status == 3) {
                ApiShopLapTop apiShopLapTop = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiShopLapTop.class);
                Call<OrderModel> call = apiShopLapTop.updateOderReceive(String.valueOf(arrayOrderHistory.get(position).getDetail_id()));
                call.enqueue(new Callback<OrderModel>() {
                    @Override
                    public void onResponse(Call<OrderModel> call, Response<OrderModel> response) {
                        OrderModel orderModel = response.body();
                        if (orderModel.isSuccess()) {
                            Navigation.findNavController(view).navigate(R.id.menuOrderHistory);
                        }
                    }

                    @Override
                    public void onFailure(Call<OrderModel> call, Throwable t) {
                        call.cancel();
                    }
                });
            }
        });

        holder.btnReturnOrder.setOnClickListener(view -> {
            Toast.makeText(holder.itemView.getContext(), "Vui lòng liên hệ admin để giải quyết các vấn đề trả hàng.", Toast.LENGTH_LONG).show();
        });

        holder.btnFeedback.setOnClickListener(view -> {
            Bundle bundle = new Bundle();
            bundle.putInt("laptop_id", arrayOrderHistory.get(position).getLaptop_id());
            bundle.putInt("detail_id", arrayOrderHistory.get(position).getDetail_id());
            bundle.putString("name", arrayOrderHistory.get(position).getName());
            bundle.putString("image_url", arrayOrderHistory.get(position).getImage_url());

            ApiShopLapTop apiShopLapTop = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiShopLapTop.class);
            Call<UserModel> call = apiShopLapTop.getUser(String.valueOf(Utils.user_id));
            call.enqueue(new Callback<UserModel>() {
                @Override
                public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                    UserModel userModel = response.body();
                    if (userModel.isSuccess()) {
                        bundle.putString("user_fullname", userModel.getResult().get(0).getFullname());
                        bundle.putString("user_avatar", userModel.getResult().get(0).getImage_url());
                        Navigation.findNavController(view).navigate(R.id.action_menuOrderHistory_to_feedbackFragment, bundle);
                    }
                }

                @Override
                public void onFailure(Call<UserModel> call, Throwable t) {
                    call.cancel();
                    Toast.makeText(holder.itemView.getContext(), "Lỗi kết nối đến Server!", Toast.LENGTH_LONG).show();
                }
            });
        });
    }

    @Override
    public int getItemCount() {
        return arrayOrderHistory.size();
    }

}
