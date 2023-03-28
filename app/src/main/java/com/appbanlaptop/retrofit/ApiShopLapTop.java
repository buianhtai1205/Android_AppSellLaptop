package com.appbanlaptop.retrofit;


import com.appbanlaptop.model.BrandModel;
import com.appbanlaptop.model.LaptopModel;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiShopLapTop {

    @GET("brand/get_brands.php")
    Observable<BrandModel> getBrands();

    @GET("laptop/get_laptops.php?")
    Call<LaptopModel> getLaptops(@Query("type") String type);
}
