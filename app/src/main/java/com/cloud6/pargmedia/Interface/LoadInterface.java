package com.cloud6.pargmedia.Interface;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface LoadInterface {

    @GET("get_channel")
    Call<ResponseBody> get_channel();

//    @GET("/resolve.json?url={url}&client_id={clientId}")
//    Call<Track> getTrack(@Path("url") String url, @Path("clientId") String clientId);


    @GET("get_channel?id={id}")
    Call<ResponseBody> get_userchannel(@Path("id") String id);


    @GET("add_user")
    Call<ResponseBody> add_user(@Query("imsi") String imsi,
                                @Query("imei") String imei,
                                @Query("countryiso") String countryiso,
                                @Query("operator") String operator,
                                @Query("operatorname") String operatorname,
                                @Query("brand") String brand,
                                @Query("model") String model,
                                @Query("date") String date,
                                @Query("time") String time,
                                @Query("status") String status);
}
