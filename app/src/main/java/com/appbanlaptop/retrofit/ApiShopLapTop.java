package com.appbanlaptop.retrofit;


import com.appbanlaptop.model.BrandModel;
import com.appbanlaptop.model.LaptopModel;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.GET;

public interface ApiShopLapTop {

    @GET("brand/get_brands.php")
    Observable<BrandModel> getBrands();

    @GET("laptop/get_laptops.php?type=gaming")
    Observable<LaptopModel> getLaptopGamings();

    @GET("laptop/get_laptops.php?type=macbook")
    Observable<LaptopModel> getLaptopMacbooks();

    @GET("laptop/get_laptops.php?type=study")
    Observable<LaptopModel> getLaptopStudys();

    @GET("laptop/get_laptops.php?type=technology")
    Observable<LaptopModel> getLaptopTechnologys();

    @GET("laptop/get_laptops.php?type=thin")
    Observable<LaptopModel> getLaptopThins();

    @GET("laptop/get_laptops.php?type=diamond")
    Observable<LaptopModel> getLaptopDiamonds();
}
