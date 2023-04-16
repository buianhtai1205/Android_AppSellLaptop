package com.appbanlaptop.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
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
    ImageView imageMenu, imageViewAds;
    static NavigationView navigationViewMain;
    NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        AnhXa();
        ActionBar();
        setItemInMenuWithLogin();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d("MainActivity", "onActivityResult call()");

        setItemInMenuWithLogin();
    }

    public void setItemInMenuWithLogin() {
        Log.d("MainActivity", "setItemInMenuWithLogin call()");
        Menu menu = navigationViewMain.getMenu();

        // auto hide menu Admin
        MenuItem itemAdmin = menu.findItem(R.id.menuAdmin);
        itemAdmin.setVisible(false);

        SharedPreferences sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
        int user_id = sharedPreferences.getInt("user_id", 0);
        if (user_id == 0) {
            // isLogin == false

            Log.d("isLogin=False", "call()");

            MenuItem itemLogin = menu.findItem(R.id.menuLogin);
            MenuItem itemRegister = menu.findItem(R.id.menuRegister);
            MenuItem itemLogout = menu.findItem(R.id.menuLogout);

            itemLogin.setVisible(true);
            itemRegister.setVisible(true);
            itemLogout.setVisible(false);


        } else {
            // isLogin == true

            Log.d("isLogin=True", "call()");

            MenuItem itemLogin = menu.findItem(R.id.menuLogin);
            MenuItem itemRegister = menu.findItem(R.id.menuRegister);
            itemLogin.setVisible(false);
            itemRegister.setVisible(false);
            MenuItem itemLogout = menu.findItem(R.id.menuLogout);
            itemLogout.setVisible(true);

            //check admin user
            if (Utils.isAdmin) {
                itemAdmin.setVisible(true);
            }
        }

        invalidateOptionsMenu();
    }

    private void ActionBar() {
        imageMenu.setOnClickListener(view -> {
            drawerLayoutMain.openDrawer(GravityCompat.START);
        });
        navigationViewMain.setItemIconTintList(null);
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        navController = navHostFragment.getNavController();

        NavigationUI.setupWithNavController(navigationViewMain, navController);
    }

    private void AnhXa() {
        drawerLayoutMain = findViewById(R.id.drawerLayoutMain);
        imageMenu = findViewById(R.id.imageMenu);
        navigationViewMain = findViewById(R.id.navigationViewMain);

        imageViewAds = findViewById(R.id.imageViewAds);
        Glide.with(MainActivity.this).load("https://img.tgdd.vn/imgt/f_webp,fit_outside,quality_100/https://cdn.tgdd.vn/2023/03/banner/big4-680-88-680x88.png?fbclid=IwAR2W1wA3RaMJeQ2n9ieG0fSxmufY2axgLRZ16bZrU3Qlsn3QrpNuZUFYHDM").into(imageViewAds);
        imageViewAds.setScaleType(ImageView.ScaleType.FIT_XY);
    }

}