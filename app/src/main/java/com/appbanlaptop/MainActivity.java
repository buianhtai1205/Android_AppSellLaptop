package com.appbanlaptop;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ViewFlipper;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    DrawerLayout drawerLayoutMain;
    Toolbar toolBarMain;
    ViewFlipper viewFlipperMain;
    RecyclerView recyclerViewMain;
    NavigationView navigationViewMain;
    ListView listViewMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AnhXa();
        ActionBar();
        ActionViewFlipper();
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
    }
}