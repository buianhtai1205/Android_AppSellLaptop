package com.appbanlaptop.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.appbanlaptop.R;
import com.appbanlaptop.adapter.CheckoutAdapter;
import com.appbanlaptop.model.Order;
import com.appbanlaptop.model.OrderDetail;
import com.appbanlaptop.model.OrderModel;
import com.appbanlaptop.model.User;
import com.appbanlaptop.model.UserModel;
import com.appbanlaptop.retrofit.ApiShopLapTop;
import com.appbanlaptop.retrofit.RetrofitClient;
import com.appbanlaptop.utils.Utils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import io.reactivex.rxjava3.plugins.RxJavaPlugins;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CheckoutFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CheckoutFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    View layoutView;
    RecyclerView recyclerViewCart;
    TextView tvTotal;
    EditText etFullName, etPhoneNumber, etAddress, etMessage;
    Button btnCheckout;
    LinearLayout layoutHome, layoutStore;
    RadioGroup rbgGender, rbgReceive, rbgPayment;
    RadioButton rbMale, rbFeMale, rbTransHome, rbReceiveStore, rbOffline, rbOnline;
    CheckoutAdapter checkoutAdapter;
    ApiShopLapTop apiShopLapTop;
    String payMethod = "ATHOME", oldPM;
    Order order;

    public CheckoutFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CheckoutFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CheckoutFragment newInstance(String param1, String param2) {
        CheckoutFragment fragment = new CheckoutFragment();
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
        layoutView = inflater.inflate(R.layout.fragment_checkout, container, false);
        RxJavaPlugins.setErrorHandler(Timber::e);

        apiShopLapTop = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiShopLapTop.class);

        AnhXa();

        if (isConnected(getContext())) {
            getListLaptopBought();

            getInfoUser();

            // set total_price
            DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
            tvTotal.setText(decimalFormat.format(Utils.total) + "đ");

            handleRadioGroup();

            handleCheckout();

        } else {
            Toast.makeText(getContext(), "Connect fail!", Toast.LENGTH_LONG).show();
        }

        return layoutView;
    }

    private void setInfoForOrder() {
        order.setUser_id(Utils.user_id);
        order.setName_receive(etFullName.getText().toString());
        order.setAddress_receive(etAddress.getText().toString());
        order.setPhone_receive(etPhoneNumber.getText().toString());
        order.setMessage(etMessage.getText().toString());
        order.setTotal_price(Utils.total);
        order.setPay_method(payMethod);

        List<OrderDetail> list = new ArrayList<>();
        HashMap<Integer, HashMap<String, String>> cart;
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("CartPrefs", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("cart", "");
        Type type = new TypeToken<HashMap<Integer, HashMap<String, String>>>() {}.getType();
        cart = gson.fromJson(json, type);

        for (Map.Entry<Integer, HashMap<String, String>> entry : cart.entrySet()) {
            Integer laptopId = entry.getKey();
            HashMap<String, String> laptop = entry.getValue();
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setLaptop_id(laptopId);
            orderDetail.setQuantity(Integer.parseInt(laptop.get("quantity")));
            list.add(orderDetail);
        }
        order.setOrderDetails(list);
    }

    private void checkout() {
        Call<OrderModel> call = apiShopLapTop.createOrder(order);
        call.enqueue(new Callback<OrderModel>() {
            @Override
            public void onResponse(Call<OrderModel> call, Response<OrderModel> response) {
                OrderModel orderModel = response.body();
                if (orderModel.isSuccess()) {
                    Toast.makeText(getContext(), orderModel.getMessage(), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getContext(), orderModel.getMessage(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<OrderModel> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi kết nối đến server", Toast.LENGTH_LONG).show();
                call.cancel();
            }
        });
    }

    private void afterCheckout() {
        Utils.total = 0;
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("CartPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("cart");
        editor.apply();
    }

    private void handleCheckout() {
        btnCheckout.setOnClickListener(view -> {
            order = new Order();
            setInfoForOrder();
            checkout();
            afterCheckout();
            Navigation.findNavController(view).navigate(R.id.menuHome);
        });
    }

    private void getInfoUser() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("LoginPrefs", Activity.MODE_PRIVATE);
        int userId = sharedPreferences.getInt("user_id", 0);
        Utils.user_id = userId;

        Call<UserModel> call = apiShopLapTop.getUser(String.valueOf(userId));
        call.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                UserModel userModel = response.body();
                if (userModel.isSuccess()) {
                    User user = userModel.getResult().get(0);
                    etFullName.setText(user.getFullname());
                    etAddress.setText(user.getAddress());
                    etPhoneNumber.setText(user.getPhone_number());
                } else {
                    Toast.makeText(getContext(), "Người dùng không tồn tại, lỗi hệ thống!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                call.cancel();
                Toast.makeText(getContext(), "Lỗi kết nối đến Server!", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void handleRadioGroup() {
        int genderSelectedId = rbgGender.getCheckedRadioButtonId();

        if (genderSelectedId == rbMale.getId()) {
            // handle
        } else if (genderSelectedId == rbFeMale.getId()) {
            // handle
        }

        rbgReceive.setOnCheckedChangeListener((radioGroup, checkedId) -> {
            if (checkedId == rbReceiveStore.getId()) {
                layoutStore.setVisibility(View.VISIBLE);
                layoutHome.setVisibility(View.GONE);
                payMethod = "ATSTORE";
            } else if (checkedId == rbTransHome.getId()) {
                layoutHome.setVisibility(View.VISIBLE);
                layoutStore.setVisibility(View.GONE);
                payMethod = "ATHOME";
            }
        });

        rbgPayment.setOnCheckedChangeListener((radioGroup, checkedId) -> {
            if (checkedId == rbOffline.getId()) {
                payMethod = oldPM;
            } else if (checkedId == rbOnline.getId()) {
                oldPM = payMethod;
                payMethod = "ZALOPAY";
            }
        });

    }

    private void getListLaptopBought() {
        HashMap<Integer, HashMap<String, String>> cart;
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("CartPrefs", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("cart", "");
        Type type = new TypeToken<HashMap<Integer, HashMap<String, String>>>() {}.getType();
        cart = gson.fromJson(json, type);

        checkoutAdapter = new CheckoutAdapter(cart);
        LinearLayoutManager linearLayoutManager =new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerViewCart.setLayoutManager(linearLayoutManager);

        recyclerViewCart.setAdapter(checkoutAdapter);
    }

    private void AnhXa() {
        recyclerViewCart = layoutView.findViewById(R.id.recyclerViewCart);
        tvTotal = layoutView.findViewById(R.id.tvTotal);
        etFullName = layoutView.findViewById(R.id.etFullName);
        etPhoneNumber = layoutView.findViewById(R.id.etPhoneNumber);
        etAddress = layoutView.findViewById(R.id.etAddress);
        etMessage = layoutView.findViewById(R.id.etMessage);

        btnCheckout = layoutView.findViewById(R.id.btnCheckout);

        layoutHome = layoutView.findViewById(R.id.layoutHome);
        layoutStore = layoutView.findViewById(R.id.layoutStore);

        rbgGender = layoutView.findViewById(R.id.rbgGender);
        rbMale = layoutView.findViewById(R.id.rbMale);
        rbFeMale = layoutView.findViewById(R.id.rbFemale);

        rbgReceive = layoutView.findViewById(R.id.rbgReceive);
        rbTransHome = layoutView.findViewById(R.id.rbTransHome);
        rbReceiveStore = layoutView.findViewById(R.id.rbReceiveStore);

        rbgPayment = layoutView.findViewById(R.id.rbgPayment);
        rbOffline = layoutView.findViewById(R.id.rbOffline);
        rbOnline = layoutView.findViewById(R.id.rbOnline);
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
}