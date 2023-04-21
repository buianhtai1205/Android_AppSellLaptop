package com.appbanlaptop.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.appbanlaptop.R;
import com.appbanlaptop.utils.Utils;
import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;

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