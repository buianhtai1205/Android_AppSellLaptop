package com.appbanlaptop.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.appbanlaptop.R;
import com.appbanlaptop.activity.MainActivity;
import com.appbanlaptop.activity.admin.AdminActivity;
import com.appbanlaptop.model.User;
import com.appbanlaptop.model.UserModel;
import com.appbanlaptop.retrofit.ApiShopLapTop;
import com.appbanlaptop.retrofit.RetrofitClient;
import com.appbanlaptop.utils.Utils;
import com.bumptech.glide.Glide;

import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.rxjava3.plugins.RxJavaPlugins;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    View layoutView;
    CircleImageView imageViewAvatar;
    TextView tvFullName, tvRole, tvUsername, tvEmail, tvAddress, tvPhoneNumber;
    Button btnUpdate, btnChangePassword;

    ApiShopLapTop apiShopLapTop;
    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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
        layoutView = inflater.inflate(R.layout.fragment_profile, container, false);
        RxJavaPlugins.setErrorHandler(Timber::e);

        apiShopLapTop = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiShopLapTop.class);

        AnhXa();

        if (isConnected(getContext())) {
            setData();
            handleEventButton();
        } else {
            Toast.makeText(getContext(), "Connect fail!", Toast.LENGTH_LONG).show();
        }

        return layoutView;
    }

    private void handleEventButton() {
        btnUpdate.setOnClickListener(view -> {

        });

        btnChangePassword.setOnClickListener(view -> {

        });
    }

    private void setData() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("LoginPrefs", Activity.MODE_PRIVATE);
        int userId = sharedPreferences.getInt("user_id", 0);

        if (userId == 0) {
            Toast.makeText(getContext(), "Vui lòng đăng nhập trước!", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
            getActivity().finish();
        } else {
            Call<UserModel> call = apiShopLapTop.getUser(String.valueOf(userId));
            call.enqueue(new Callback<UserModel>() {
                @Override
                public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                    UserModel userModel = response.body();
                    if (userModel.isSuccess()) {
                        User user = userModel.getResult().get(0);
                        Glide.with(getContext()).load(user.getImage_url()).into(imageViewAvatar);
                        tvFullName.setText(user.getFullname());
                        tvRole.setText("Vai trò: " + user.getRole());
                        tvUsername.setText(user.getUsername());
                        tvEmail.setText(user.getEmail());
                        tvAddress.setText(user.getAddress());
                        tvPhoneNumber.setText(user.getPhone_number());
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
    }

    private void AnhXa() {
        imageViewAvatar = layoutView.findViewById(R.id.imageViewAvatar);
        tvFullName = layoutView.findViewById(R.id.tvFullName);
        tvRole = layoutView.findViewById(R.id.tvRole);
        tvUsername = layoutView.findViewById(R.id.tvUsername);
        tvEmail = layoutView.findViewById(R.id.tvEmail);
        tvAddress = layoutView.findViewById(R.id.tvAddress);
        tvPhoneNumber = layoutView.findViewById(R.id.tvPhoneNumber);
        btnUpdate = layoutView.findViewById(R.id.btnUpdate);
        btnChangePassword = layoutView.findViewById(R.id.btnChangePassword);
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