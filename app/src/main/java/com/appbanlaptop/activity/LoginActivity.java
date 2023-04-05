package com.appbanlaptop.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.appbanlaptop.R;
import com.appbanlaptop.model.User;
import com.appbanlaptop.model.UserModel;
import com.appbanlaptop.retrofit.ApiShopLapTop;
import com.appbanlaptop.retrofit.RetrofitClient;
import com.appbanlaptop.utils.Utils;

import java.util.HashMap;

import io.reactivex.rxjava3.plugins.RxJavaPlugins;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class LoginActivity extends AppCompatActivity {

    EditText etUsername, etPassword;
    Button btnLogin;
    ImageView btnGoogle, btnFacebook, btnTwitter;
    ApiShopLapTop apiShopLapTop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        RxJavaPlugins.setErrorHandler(Timber::e);

        apiShopLapTop = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiShopLapTop.class);

        Log.d("LoginActivity", "onCreate() called");

        AnhXa();
        handleLogin();


    }

    private void handleLogin() {
        btnLogin.setOnClickListener(view -> {
            String username = etUsername.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            if (username.equals("")) {
                Toast.makeText(LoginActivity.this, "Vui lòng nhập tên đăng nhập", Toast.LENGTH_LONG).show();
            } else if (password.equals("")) {
                Toast.makeText(LoginActivity.this, "Vui lòng nhập mật khẫu", Toast.LENGTH_LONG).show();
            } else {
                Call<UserModel> call = apiShopLapTop.login(username, password);
                call.enqueue(new Callback<UserModel>() {
                    @Override
                    public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                        UserModel userModel = response.body();
                        if (userModel.isSuccess()) {
                            Toast.makeText(LoginActivity.this, "Đăng nhập thành công.", Toast.LENGTH_LONG).show();
                            // set value to local

                            // finish
                            Intent resultIntent = new Intent();
                            setResult(RESULT_OK, resultIntent);
                            finish();
                        } else {
                            Toast.makeText(LoginActivity.this, "Tên đăng nhập hoặc mật khẫu không đúng.", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<UserModel> call, Throwable t) {
                        call.cancel();
                    }
                });
            }
        });
    }

    private void AnhXa() {
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnGoogle = findViewById(R.id.btnGoogle);
        btnFacebook = findViewById(R.id.btnFacebook);
        btnTwitter = findViewById(R.id.btnTwitter);
    }
}
