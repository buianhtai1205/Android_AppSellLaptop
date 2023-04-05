package com.appbanlaptop.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.appbanlaptop.R;
import com.appbanlaptop.adapter.BrandAdapter;
import com.appbanlaptop.fragment.CartFragment;
import com.appbanlaptop.fragment.HomeFragment;
import com.appbanlaptop.fragment.MessageFragment;
import com.appbanlaptop.fragment.NotificationFragment;
import com.appbanlaptop.fragment.OrderHistoryFragment;
import com.appbanlaptop.fragment.ProfileFragment;
import com.appbanlaptop.fragment.SettingFragment;
import com.appbanlaptop.model.Brand;
import com.appbanlaptop.retrofit.ApiShopLapTop;
import com.appbanlaptop.retrofit.RetrofitClient;
import com.appbanlaptop.utils.Utils;
import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.plugins.RxJavaPlugins;
import io.reactivex.rxjava3.schedulers.Schedulers;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity {
    DrawerLayout drawerLayoutMain;
    ImageView imageMenu;
    NavigationView navigationViewMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AnhXa();
        ActionBar();
        MenuHandleWithLogin();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK) {
            // login success handle
            SharedPreferences sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("user_id", data.getIntExtra("id", 0));
            editor.apply();
        }

        if (requestCode == 2 && resultCode == RESULT_OK) {
            // signup success handle, duplicate code of Login. The end project will merge if it don't have difference
            SharedPreferences sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("user_id", data.getIntExtra("id", 0));
            editor.apply();
        }
        setItemInMenuWithLogin();
    }

    private void MenuHandleWithLogin() {
        setItemInMenuWithLogin();

        Menu menu = navigationViewMain.getMenu();
        final MenuItem[] previousMenuItem = {menu.findItem(R.id.menuHome)}; //tùy theo MenuItem đầu tiên sẽ khác nhau
        previousMenuItem[0].setChecked(true); //set trạng thái checked cho MenuItem đầu tiên

        navigationViewMain.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                Intent intent = null;
                switch (item.getItemId()) {
                    case R.id.menuLogin: {
                        intent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivityForResult(intent, 1);
                        break;
                    }
                    case R.id.menuRegister: {
                        intent = new Intent(MainActivity.this, SignUpActivity.class);
                        startActivityForResult(intent, 2);
                        break;
                    }

                    case R.id.menuLogout: {
                        SharedPreferences sharedPreferences = getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.remove("user_id");
                        editor.apply();
                        setItemInMenuWithLogin();
                        break;
                    }

                    case R.id.menuHome: {
                        selectedFragment = new HomeFragment();
                        break;
                    }

                    case R.id.menuProfile: {
                        selectedFragment = new ProfileFragment();
                        break;
                    }

                    case R.id.menuCart: {
                        selectedFragment = new CartFragment();
                        break;
                    }

                    case R.id.menuNotify: {
                        selectedFragment = new NotificationFragment();
                        break;
                    }

                    case R.id.menuMessage: {
                        selectedFragment = new MessageFragment();
                        break;
                    }

                    case R.id.menuOrderHistory: {
                        selectedFragment = new OrderHistoryFragment();
                        break;
                    }

                    case R.id.menuSetting: {
                        selectedFragment = new SettingFragment();
                        break;
                    }

                    default:
                        break;
                }

                // Highlight the selected item in the menu
                if(item.getItemId() != R.id.menuLogin && item.getItemId() != R.id.menuRegister) {
                    previousMenuItem[0].setChecked(false); //bỏ trạng thái check của MenuItem trước đó
                    item.setChecked(true);
                    previousMenuItem[0] = item; //cập nhật MenuItem trước đó
                }

                // Replace the fragment or start the activity
                if (selectedFragment != null) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, selectedFragment).commit();
                }

                // Close the navigation drawer
                drawerLayoutMain.closeDrawer(GravityCompat.START);

                return true;
            }
        });
    }

    private void setItemInMenuWithLogin() {
        Menu menu = navigationViewMain.getMenu();

        SharedPreferences sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
        int user_id = sharedPreferences.getInt("user_id", 0);
        if (user_id == 0) {
            // isLogin == false

            Log.d("isLogin=False", "call()");

            MenuItem itemLogin = menu.findItem(R.id.menuLogin);
            MenuItem itemRegister = menu.findItem(R.id.menuRegister);
            MenuItem itemLogout = menu.findItem(R.id.menuLogout);

            Log.d("itemLogin", String.valueOf(itemLogin));
            Log.d("itemRegister", String.valueOf(itemRegister));

            itemLogin.setVisible(true);
            itemRegister.setVisible(true);
            itemLogout.setVisible(false);

            invalidateOptionsMenu();
        } else {
            // isLogin == true

            Log.d("isLogin=True", "call()");

            MenuItem itemLogin = menu.findItem(R.id.menuLogin);
            MenuItem itemRegister = menu.findItem(R.id.menuRegister);
            itemLogin.setVisible(false);
            itemRegister.setVisible(false);
            MenuItem itemLogout = menu.findItem(R.id.menuLogout);
            itemLogout.setVisible(true);
            invalidateOptionsMenu();
        }
    }

    private void ActionBar() {
        imageMenu.setOnClickListener(view -> {
            drawerLayoutMain.openDrawer(GravityCompat.START);
        });
        navigationViewMain.setItemIconTintList(null);
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        NavController navController = navHostFragment.getNavController();

        NavigationUI.setupWithNavController(navigationViewMain, navController);
    }

    private void AnhXa() {
        drawerLayoutMain = findViewById(R.id.drawerLayoutMain);
        imageMenu = findViewById(R.id.imageMenu);
        navigationViewMain = findViewById(R.id.navigationViewMain);
    }

}