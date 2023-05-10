package com.appbanlaptop.retrofit;


import android.content.Intent;

import com.appbanlaptop.model.BrandModel;
import com.appbanlaptop.model.FeedbackModel;
import com.appbanlaptop.model.LaptopModel;
import com.appbanlaptop.model.Order;
import com.appbanlaptop.model.OrderModel;
import com.appbanlaptop.model.UserModel;

import java.util.HashMap;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
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

    @FormUrlEncoded
    @POST("user/signup.php?")
    Call<UserModel> signup(
            @Field("username") String username,
            @Field("password") String password,
            @Field("email") String email,
            @Field("fullname") String fullname,
            @Field("address") String address,
            @Field("phone_number") String phone_number
    );

    @GET("user/get_user.php?")
    Call<UserModel> getUser(@Query("id") String id);

    @POST("order/order.php")
    Call<OrderModel> createOrder(@Body Order order);

    @FormUrlEncoded
    @POST("user/update_user.php?")
    Call<UserModel> updateProfile(
            @Field("id") int id,
            @Field("fullname") String fullname,
            @Field("address") String address,
            @Field("phone_number") String phone_number,
            @Field("image_url") String image_url
    );

    @FormUrlEncoded
    @POST("user/change_password.php?")
    Call<UserModel> changePassword(
            @Field("id") int id,
            @Field("present_password") String present_password,
            @Field("new_password") String new_password
    );

    @GET("order/get_order.php?")
    Call<OrderModel> getOrder(@Query("id") String id);

    @FormUrlEncoded
    @POST("order/update_order_received.php?")
    Call<OrderModel> updateOderReceive(@Field("detail_id") String detail_id);

    @FormUrlEncoded
    @POST("order/feedback.php?")
    Call<OrderModel> sendFeedback(
            @Field("detail_id") String detail_id,
            @Field("star") String star,
            @Field("comment") String comment,
            @Field("user_fullname") String user_fullname,
            @Field("user_avatar") String user_avatar,
            @Field("laptop_id") String laptop_id
    );

    @GET("order/get_feedback.php?")
    Call<FeedbackModel> getFeedbacks(@Query("id") String id);
}

