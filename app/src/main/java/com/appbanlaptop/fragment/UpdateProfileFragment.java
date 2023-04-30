package com.appbanlaptop.fragment;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.appbanlaptop.R;
import com.appbanlaptop.model.User;
import com.appbanlaptop.model.UserModel;
import com.appbanlaptop.retrofit.ApiShopLapTop;
import com.appbanlaptop.retrofit.RetrofitClient;
import com.appbanlaptop.utils.Utils;
import com.bumptech.glide.Glide;

import io.reactivex.rxjava3.plugins.RxJavaPlugins;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UpdateProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UpdateProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    View layoutView;
    EditText etFullName, etAddress, etPhoneNumber, etImageUrl;
    Button btnUpdate, btnCancel;

    ApiShopLapTop apiShopLapTop;

    public UpdateProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UpdateProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UpdateProfileFragment newInstance(String param1, String param2) {
        UpdateProfileFragment fragment = new UpdateProfileFragment();
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
        layoutView = inflater.inflate(R.layout.fragment_update_profile, container, false);
        RxJavaPlugins.setErrorHandler(Timber::e);

        apiShopLapTop = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiShopLapTop.class);

        AnhXa();

        if (isConnected(getContext())) {
            setData();
            handleButton();
        } else {
            Toast.makeText(getContext(), "Connect fail!", Toast.LENGTH_LONG).show();
        }

        return layoutView;
    }

    private void handleButton() {
        btnUpdate.setOnClickListener(view -> {
            String fullName = etFullName.getText().toString();
            String address = etAddress.getText().toString();
            String phoneNumber = etPhoneNumber.getText().toString();
            String imageUrl = etImageUrl.getText().toString();

            Call<UserModel> call = apiShopLapTop.updateProfile(Utils.user_id, fullName, address, phoneNumber, imageUrl);
            call.enqueue(new Callback<UserModel>() {
                @Override
                public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                    UserModel userModel = response.body();
                    if (userModel.isSuccess()) {
                        Toast.makeText(getContext(), userModel.getMessage(), Toast.LENGTH_LONG).show();
                        Navigation.findNavController(view).navigate(R.id.action_updateProfileFragment_to_menuProfile);
                    } else {
                        Toast.makeText(getContext(), userModel.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<UserModel> call, Throwable t) {
                    Toast.makeText(getContext(), "Lỗi kết nối đến server!", Toast.LENGTH_LONG).show();
                    call.cancel();
                }
            });
        });

        btnCancel.setOnClickListener(view -> {
            Navigation.findNavController(view).navigate(R.id.action_updateProfileFragment_to_menuProfile);
        });
    }

    private void setData() {
        Call<UserModel> call = apiShopLapTop.getUser(String.valueOf(Utils.user_id));
        call.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                UserModel userModel = response.body();
                if (userModel.isSuccess()) {
                    User user = userModel.getResult().get(0);
                    etFullName.setText(user.getFullname());
                    etAddress.setText(user.getAddress());
                    etPhoneNumber.setText(user.getPhone_number());
                    etImageUrl.setText(user.getImage_url());
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
        etFullName = layoutView.findViewById(R.id.etFullName);
        etAddress = layoutView.findViewById(R.id.etAddress);
        etPhoneNumber = layoutView.findViewById(R.id.etPhoneNumber);
        etImageUrl = layoutView.findViewById(R.id.etImageUrl);

        btnCancel = layoutView.findViewById(R.id.btnCancel);
        btnUpdate = layoutView.findViewById(R.id.btnUpdate);
    }
}