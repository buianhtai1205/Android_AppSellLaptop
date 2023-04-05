package com.appbanlaptop.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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

public class SignUpActivity extends AppCompatActivity  {

    ApiShopLapTop apiShopLapTop;
    EditText etUsername, etPassword, etEmail, etFullname, etAddress, etPhoneNumber;
    Button btnSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        RxJavaPlugins.setErrorHandler(Timber::e);

        apiShopLapTop = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiShopLapTop.class);

        Log.d("SignUpActivity", "onCreate() called");

        AnhXa();
        handleSignup();

    }

    private void handleSignup() {
        btnSignup.setOnClickListener(view -> {
            String username = etUsername.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String fullname = etFullname.getText().toString();
            String address = etAddress.getText().toString();
            String phone_number = etPhoneNumber.getText().toString();

            if (username.equals("")) {
                Toast.makeText(SignUpActivity.this, "Vui lòng nhập tên đăng nhập", Toast.LENGTH_LONG).show();
            } else if (password.equals("")) {
                Toast.makeText(SignUpActivity.this, "Vui lòng nhập mật khẫu", Toast.LENGTH_LONG).show();
            } else if (email.equals("")) {
                Toast.makeText(SignUpActivity.this, "Vui lòng nhập Email", Toast.LENGTH_LONG).show();
            } else {
                Call<UserModel> call = apiShopLapTop.signup(username, password, email, fullname, address, phone_number);
                call.enqueue(new Callback<UserModel>() {
                    @Override
                    public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                        UserModel userModel = response.body();
                        if (userModel.isSuccess()) {
                            Toast.makeText(SignUpActivity.this, "Đăng ký thành công.", Toast.LENGTH_LONG).show();
                            // set value to local
                            int id = userModel.getResult().get(0).getId();

                            // finish
                            Intent resultIntent = new Intent();
                            resultIntent.putExtra("id", id);
                            setResult(RESULT_OK, resultIntent);
                            finish();
                        } else {
                            Toast.makeText(SignUpActivity.this, "Tên đăng nhập hoặc email đã tồn tại!", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<UserModel> call, Throwable t) {
                        Toast.makeText(SignUpActivity.this, "Lỗi mất kết nối đến máy chủ", Toast.LENGTH_LONG).show();
                        call.cancel();
                    }
                });

            }
        });
    }

    private void AnhXa() {
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        etFullname = findViewById(R.id.etFullname);
        etEmail = findViewById(R.id.etEmail);
        etAddress = findViewById(R.id.etAddress);
        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        btnSignup =findViewById(R.id.btnSignup);
    }
}
