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
import com.appbanlaptop.model.UserModel;
import com.appbanlaptop.retrofit.ApiShopLapTop;
import com.appbanlaptop.retrofit.RetrofitClient;
import com.appbanlaptop.utils.Utils;

import io.reactivex.rxjava3.plugins.RxJavaPlugins;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChangePasswordFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChangePasswordFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    View layoutView;

    EditText etPresentPassword, etNewPassword, getEtNewPasswordConfirm;
    Button btnCancel, btnChange;

    ApiShopLapTop apiShopLapTop;

    public ChangePasswordFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ForgotPasswordFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChangePasswordFragment newInstance(String param1, String param2) {
        ChangePasswordFragment fragment = new ChangePasswordFragment();
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
        layoutView = inflater.inflate(R.layout.fragment_change_password, container, false);

        RxJavaPlugins.setErrorHandler(Timber::e);

        apiShopLapTop = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiShopLapTop.class);

        AnhXa();

        if (isConnected(getContext())) {
            handleButton();
        } else {
            Toast.makeText(getContext(), "Connect fail!", Toast.LENGTH_LONG).show();
        }

        return layoutView;
    }

    private void handleButton() {
        btnCancel.setOnClickListener(view -> {
            Navigation.findNavController(view).navigate(R.id.action_changePasswordFragment_to_menuProfile);
        });
        btnChange.setOnClickListener(view -> {
            String presentPassword = etPresentPassword.getText().toString();
            String newPassword = etNewPassword.getText().toString();
            String newPasswordConfirm = getEtNewPasswordConfirm.getText().toString();

            if (newPassword.equals(newPasswordConfirm)) {
                Call<UserModel> call = apiShopLapTop.changePassword(Utils.user_id, presentPassword, newPassword);
                call.enqueue(new Callback<UserModel>() {
                    @Override
                    public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                        UserModel userModel = response.body();
                        if (userModel.isSuccess()) {
                            Toast.makeText(getContext(), userModel.getMessage(), Toast.LENGTH_LONG).show();
                            Navigation.findNavController(view).navigate(R.id.action_changePasswordFragment_to_menuProfile);
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
            } else {
                Toast.makeText(getContext(), "Nhập lại password không chính xác!", Toast.LENGTH_LONG).show();
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
        etPresentPassword = layoutView.findViewById(R.id.etPresentPassword);
        etNewPassword = layoutView.findViewById(R.id.etNewPassword);
        getEtNewPasswordConfirm = layoutView.findViewById(R.id.etNewPasswordConfirm);

        btnCancel = layoutView.findViewById(R.id.btnCancel);
        btnChange = layoutView.findViewById(R.id.btnChange);
    }
}