package com.appbanlaptop.retrofit;


import com.appbanlaptop.model.BrandModel;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.GET;

public interface ApiShopLapTop {
    @GET("get_brands.php")
    Observable<BrandModel> getBrands();

}
