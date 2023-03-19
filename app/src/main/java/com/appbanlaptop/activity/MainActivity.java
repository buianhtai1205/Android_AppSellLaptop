package com.appbanlaptop.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.appbanlaptop.R;
import com.appbanlaptop.adapter.BrandAdapter;
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
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    DrawerLayout drawerLayoutMain;
    Toolbar toolBarMain;
    ViewFlipper viewFlipperMain;
    RecyclerView recyclerViewMain;
    NavigationView navigationViewMain;
    ListView listViewMain;
    BrandAdapter brandAdapter;
    List<Brand> arrayBrand;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    ApiShopLapTop apiShopLapTop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        apiShopLapTop = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiShopLapTop.class);

        AnhXa();
        ActionBar();

        if (isConnected(this)) {
//            Toast.makeText(getApplicationContext(), "Connect successful!", Toast.LENGTH_LONG).show();
            ActionViewFlipper();

            getListBrands();

        } else {
            Toast.makeText(getApplicationContext(), "Connect failful!", Toast.LENGTH_LONG).show();
        }
    }

    private void getListBrands() {
        compositeDisposable.add(apiShopLapTop.getBrands()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    brandModel -> {
                        if (brandModel.isSuccess()) {
                            arrayBrand = brandModel.getResult();
                            brandAdapter = new BrandAdapter(arrayBrand, getApplicationContext());
                            listViewMain.setAdapter(brandAdapter);
                        }
                    }
                ));
    }

    private void ActionViewFlipper() {
        List<String> arrayAds = new ArrayList<>();
        arrayAds.add("https://cdn.tgdd.vn/2023/03/banner/chungr-MSI-800-200-800x200.png");
        arrayAds.add("https://cdn.tgdd.vn/2023/03/banner/mac-800-200-800x200.png");
        arrayAds.add("https://cdn.tgdd.vn/2023/03/banner/Laptop-Gaming-800-200-800x200.png");
        arrayAds.add("https://cdn.tgdd.vn/2023/03/banner/HP-800-200-800x200.png");
        arrayAds.add("https://cdn.tgdd.vn/2023/03/banner/Lenovo800-200-800x200.png");
        arrayAds.add("https://cdn.tgdd.vn/2023/03/banner/asus-800-200-800x200.png");
        arrayAds.add("https://cdn.tgdd.vn/2023/03/banner/acer-800-200-800x200.png");
        arrayAds.add("https://cdn.tgdd.vn/2023/03/banner/MSI-800-200-800x200-1.png");

        for (int i=0; i<arrayAds.size(); i++) {
            ImageView imageView = new ImageView(getApplicationContext());
            Glide.with(getApplicationContext()).load(arrayAds.get(i)).into(imageView);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            viewFlipperMain.addView(imageView);
        }

        viewFlipperMain.setFlipInterval(3000);
        viewFlipperMain.setAutoStart(true);
        Animation slide_in = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in_right);
        Animation slide_out = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_out_right);
        viewFlipperMain.setInAnimation(slide_in);
        viewFlipperMain.setOutAnimation(slide_out);
    }

    private void ActionBar() {
        setSupportActionBar(toolBarMain);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolBarMain.setNavigationIcon(android.R.drawable.ic_menu_sort_by_size);
        toolBarMain.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayoutMain.openDrawer(GravityCompat.START);
            }
        });
    }

    private void AnhXa() {
        drawerLayoutMain = findViewById(R.id.drawerLayoutMain);
        toolBarMain = findViewById(R.id.toolBarMain);
        viewFlipperMain = findViewById(R.id.viewFlipperMain);
        recyclerViewMain = findViewById(R.id.recyclerViewMain);
        navigationViewMain = findViewById(R.id.navigationViewMain);
        listViewMain = findViewById(R.id.listViewMain);

        // khoi tao list
        arrayBrand = new ArrayList<>();

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

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}