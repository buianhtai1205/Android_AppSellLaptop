package com.appbanlaptop.retrofit;


import com.appbanlaptop.model.BrandModel;
import com.appbanlaptop.model.LaptopModel;
import com.appbanlaptop.model.UserModel;

import java.util.HashMap;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiShopLapTop {

    @GET("brand/get_brands.php")
    Observable<BrandModel> getBrands();

    @GET("laptop/get_laptops.php?")
    Call<LaptopModel> getLaptops(@Query("type") String type);

    @GET("laptop/get_laptop_detail.php?")
    Call<LaptopModel> getLaptopDetail(@Query("id") String id);

    @FormUrlEncoded
    @POST("user/login.php?")
    Call<UserModel> login(@Field("username") String username, @Field("password") String password);
}

