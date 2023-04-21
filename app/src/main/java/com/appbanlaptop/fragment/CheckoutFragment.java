package com.appbanlaptop.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
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
import com.appbanlaptop.model.User;
import com.appbanlaptop.model.UserModel;
import com.appbanlaptop.retrofit.ApiShopLapTop;
import com.appbanlaptop.retrofit.RetrofitClient;
import com.appbanlaptop.utils.Utils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.HashMap;

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
    RadioGroup rbgGender, rbgReceive;
    RadioButton rbMale, rbFeMale, rbTransHome, rbReceiveStore;
    CheckoutAdapter checkoutAdapter;
    ApiShopLapTop apiShopLapTop;

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

        } else {
            Toast.makeText(getContext(), "Connect fail!", Toast.LENGTH_LONG).show();
        }

        return layoutView;
    }

    private void getInfoUser() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("LoginPrefs", Activity.MODE_PRIVATE);
        int userId = sharedPreferences.getInt("user_id", 0);

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
                Toast.makeText(getContext(), "Lỗi Kết nối đến Server!", Toast.LENGTH_LONG).show();
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
            } else if (checkedId == rbTransHome.getId()) {
                layoutHome.setVisibility(View.VISIBLE);
                layoutStore.setVisibility(View.GONE);
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