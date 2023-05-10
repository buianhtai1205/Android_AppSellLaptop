package com.appbanlaptop.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.appbanlaptop.R;
import com.appbanlaptop.activity.MainActivity;
import com.appbanlaptop.adapter.OrderHistoryAdapter;
import com.appbanlaptop.model.OrderHistory;
import com.appbanlaptop.model.OrderModel;
import com.appbanlaptop.retrofit.ApiShopLapTop;
import com.appbanlaptop.retrofit.RetrofitClient;
import com.appbanlaptop.utils.Utils;

import java.util.List;

import io.reactivex.rxjava3.plugins.RxJavaPlugins;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OrderHistoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OrderHistoryFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    View layoutView;
    RecyclerView recyclerViewOrderHistory;
    ApiShopLapTop apiShopLapTop;

    public OrderHistoryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OrderHistoryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OrderHistoryFragment newInstance(String param1, String param2) {
        OrderHistoryFragment fragment = new OrderHistoryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        layoutView = inflater.inflate(R.layout.fragment_order_history, container, false);
        RxJavaPlugins.setErrorHandler(Timber::e);

        apiShopLapTop = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiShopLapTop.class);

        AnhXa();

        if (isConnected(getContext())) {
            checkLogin();
            getListOrderHistory();
        } else {
            Toast.makeText(getContext(), "Connect fail!", Toast.LENGTH_LONG).show();
        }

        return layoutView;
    }

    private void checkLogin() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("LoginPrefs", Activity.MODE_PRIVATE);
        int userId = sharedPreferences.getInt("user_id", 0);

        if (userId == 0) {
            Toast.makeText(getContext(), "Vui lòng đăng nhập trước!", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
            getActivity().finish();
        } else {
            Utils.user_id = userId;
        }
    }

    private void getListOrderHistory() {
        Call<OrderModel> call = apiShopLapTop.getOrder(String.valueOf(Utils.user_id));
        call.enqueue(new Callback<OrderModel>() {
            @Override
            public void onResponse(Call<OrderModel> call, Response<OrderModel> response) {
                OrderModel orderModel = response.body();
                if (orderModel.isSuccess()) {
                    LinearLayoutManager linearLayoutManager =new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                    recyclerViewOrderHistory.setLayoutManager(linearLayoutManager);
                    List<OrderHistory> arrayOrderHistory = orderModel.getResult();
                    OrderHistoryAdapter orderHistoryAdapter = new OrderHistoryAdapter(arrayOrderHistory);
                    recyclerViewOrderHistory.setAdapter(orderHistoryAdapter);

                } else {
                    Toast.makeText(getContext(), orderModel.getMessage(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<OrderModel> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi đến nối đến server!", Toast.LENGTH_LONG).show();
                call.cancel();
            }
        });
    }

    private boolean isConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobile = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if ((wifi != null && wifi.isConnected()) || (mobile != null && mobile.isConnected())) {
            return true;
        }
        return false;
    }

    private void AnhXa() {
        recyclerViewOrderHistory = layoutView.findViewById(R.id.recyclerViewOrderHistory);
    }
}