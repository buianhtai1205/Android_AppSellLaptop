package com.appbanlaptop.fragment;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.appbanlaptop.R;
import com.appbanlaptop.adapter.BrandAdapter;
import com.appbanlaptop.adapter.LaptopAdapter;
import com.appbanlaptop.model.Brand;
import com.appbanlaptop.model.Laptop;
import com.appbanlaptop.model.LaptopModel;
import com.appbanlaptop.retrofit.ApiShopLapTop;
import com.appbanlaptop.retrofit.RetrofitClient;
import com.appbanlaptop.utils.Utils;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.plugins.RxJavaPlugins;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //myStartCode
    View layoutView;
    ViewFlipper viewFlipperMain;
    RecyclerView recyclerViewBrand, recyclerViewGamingProduct, recyclerViewMacbookProduct, recyclerViewStudyProduct;
    RecyclerView recyclerViewTechnologyProduct, recyclerViewThinProduct, recyclerViewDiamondProduct;
    BrandAdapter brandAdapter;
    LaptopAdapter laptopAdapter;
    List<Brand> arrayBrand;
    List<Laptop> arrayLaptopGaming, arrayLaptopMacbook, arrayLaptopStudy;
    List<Laptop> arrayLaptopTechnology, arrayLaptopThin, arrayLaptopDiamond;
    ApiShopLapTop apiShopLapTop;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    NavController navController;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        layoutView = inflater.inflate(R.layout.fragment_home, container, false);
        RxJavaPlugins.setErrorHandler(Timber::e);

        apiShopLapTop = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiShopLapTop.class);

        AnhXa();

        //Function
        if (isConnected(getContext())) {
            ActionViewFlipper();
            getListBrands();
            getListLaptops("gaming");
            getListLaptops("macbook");
            getListLaptops("study");
            getListLaptops("technology");
            getListLaptops("thin");
            getListLaptops("diamond");

        } else {
            Toast.makeText(getContext(), "Connect fail!", Toast.LENGTH_LONG).show();
        }

        // Inflate the layout for this fragment
        return layoutView;
    }

    private void getListLaptops(String type) {
        Call<LaptopModel> call = apiShopLapTop.getLaptops(type);
        call.enqueue(new Callback<LaptopModel>() {
            @Override
            public void onResponse(Call<LaptopModel> call, Response<LaptopModel> response) {
                LaptopModel laptopModel = response.body();
                if (laptopModel.isSuccess()) {
                    setAdapterForRecyclerViewProduct(laptopModel, type);
                }
            }
            @Override
            public void onFailure(Call<LaptopModel> call, Throwable t) {
                call.cancel();
            }
        });
    }

    private void setAdapterForRecyclerViewProduct(LaptopModel laptopModel, String type) {
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        switch (type) {
            case "gaming": {
                arrayLaptopGaming = laptopModel.getResult();
                laptopAdapter = new LaptopAdapter(arrayLaptopGaming);
                recyclerViewGamingProduct.setLayoutManager(layoutManager);
                recyclerViewGamingProduct.setAdapter(laptopAdapter);
                break;
            }
            case "macbook": {
                arrayLaptopMacbook = laptopModel.getResult();
                laptopAdapter = new LaptopAdapter(arrayLaptopMacbook);
                recyclerViewMacbookProduct.setLayoutManager(layoutManager);
                recyclerViewMacbookProduct.setAdapter(laptopAdapter);
                break;
            }
            case "study": {
                arrayLaptopStudy = laptopModel.getResult();
                laptopAdapter = new LaptopAdapter(arrayLaptopStudy);
                recyclerViewStudyProduct.setLayoutManager(layoutManager);
                recyclerViewStudyProduct.setAdapter(laptopAdapter);
                break;
            }
            case "technology": {
                arrayLaptopTechnology = laptopModel.getResult();
                laptopAdapter = new LaptopAdapter(arrayLaptopTechnology);
                recyclerViewTechnologyProduct.setLayoutManager(layoutManager);
                recyclerViewTechnologyProduct.setAdapter(laptopAdapter);
                break;
            }
            case "thin": {
                arrayLaptopThin = laptopModel.getResult();
                laptopAdapter = new LaptopAdapter(arrayLaptopThin);
                recyclerViewThinProduct.setLayoutManager(layoutManager);
                recyclerViewThinProduct.setAdapter(laptopAdapter);
                break;
            }
            case "diamond": {
                arrayLaptopDiamond = laptopModel.getResult();
                laptopAdapter = new LaptopAdapter(arrayLaptopDiamond);
                recyclerViewDiamondProduct.setLayoutManager(layoutManager);
                recyclerViewDiamondProduct.setAdapter(laptopAdapter);
                break;
            }
            default: {
                Toast.makeText(getContext(), "Has Error in setAdaterForRecyclerView", Toast.LENGTH_LONG).show();
                break;
            }
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
                                brandAdapter = new BrandAdapter(arrayBrand);

                                LinearLayoutManager linearLayoutManager =new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                                recyclerViewBrand.setLayoutManager(linearLayoutManager);
                                recyclerViewBrand.setAdapter(brandAdapter);
                            }
                        }
                ));
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
        //AnhXa
        viewFlipperMain = (ViewFlipper)layoutView.findViewById(R.id.viewFlipperMain);
        recyclerViewBrand = layoutView.findViewById(R.id.recyclerViewBrand);
        recyclerViewGamingProduct = layoutView.findViewById(R.id.recyclerViewGamingProduct);
        recyclerViewMacbookProduct = layoutView.findViewById(R.id.recyclerViewMacbookProduct);
        recyclerViewStudyProduct = layoutView.findViewById(R.id.recyclerViewStudyProduct);
        recyclerViewTechnologyProduct = layoutView.findViewById(R.id.recyclerViewTechnologyProduct);
        recyclerViewThinProduct = layoutView.findViewById(R.id.recyclerViewThinProduct);
        recyclerViewDiamondProduct = layoutView.findViewById(R.id.recyclerViewDiamondProduct);

        // khoi tao list
        arrayBrand = new ArrayList<>();
        arrayLaptopGaming = new ArrayList<>();
        arrayLaptopMacbook = new ArrayList<>();
        arrayLaptopStudy = new ArrayList<>();
        arrayLaptopTechnology = new ArrayList<>();
        arrayLaptopThin = new ArrayList<>();
        arrayLaptopDiamond = new ArrayList<>();
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
            ImageView imageView = new ImageView(getContext());
            Glide.with(getContext()).load(arrayAds.get(i)).into(imageView);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            viewFlipperMain.addView(imageView);
        }

        viewFlipperMain.setFlipInterval(3000);
        viewFlipperMain.setAutoStart(true);
        Animation slide_in = AnimationUtils.loadAnimation(getContext(), R.anim.slide_in_right);
        Animation slide_out = AnimationUtils.loadAnimation(getContext(), R.anim.slide_out_right);
        viewFlipperMain.setInAnimation(slide_in);
        viewFlipperMain.setOutAnimation(slide_out);
    }

    @Override
    public void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }

}