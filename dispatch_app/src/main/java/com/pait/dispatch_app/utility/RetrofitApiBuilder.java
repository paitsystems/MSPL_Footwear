package com.pait.dispatch_app.utility;

//Created by Anup on 1/10/2019.

import com.pait.dispatch_app.constant.Constant;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitApiBuilder {

    private Retrofit retrofit = null;

    final OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .readTimeout(Constant.TIMEOUT_SO, TimeUnit.SECONDS)
            .connectTimeout(Constant.TIMEOUT_CON, TimeUnit.SECONDS)
            .build();

    public Retrofit getApiBuilder(){
        if(retrofit==null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(Constant.ipaddress+"/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)
                    .build();
        }
        return retrofit;
    }
}
