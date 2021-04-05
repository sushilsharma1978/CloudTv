package com.cloud6.pargmedia.App;

import com.cloud6.pargmedia.Interface.LoadInterface;


import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AppConfig {
    private static Retrofit retrofit = null;
    private static LoadInterface loadInterface = null;

//    http://7starcloud.in/cloudTv/Webservice/
    private static Retrofit getClient() {
        if (retrofit == null) {
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(1000, TimeUnit.SECONDS)
                    .readTimeout(1000, TimeUnit.SECONDS).build();
                     retrofit = new Retrofit.Builder().baseUrl("http://7starcloud.in/tamilcloud/webservice/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

        }
        return retrofit;
    }

    public static LoadInterface loadInterface() {
        if (loadInterface == null) {
            loadInterface = AppConfig.getClient().create(LoadInterface.class);
        }
        return loadInterface;
    }




}
