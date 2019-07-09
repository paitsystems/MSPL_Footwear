package com.pait.dispatch_app.interfaces;

//Created by Anup on 1/10/2019.

import com.pait.dispatch_app.model.DispatchMasterClass;
import com.pait.dispatch_app.model.StockTakeClass;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface RetrofitApiInterface {

    @Headers("Content-Type: application/json")
    @POST("GetDispatchData")
    Call<List<DispatchMasterClass>> getDispatchMaster(@Body RequestBody url);

    @Headers("Content-Type: application/json")
    @POST("GetStockTakeData")
    Call<List<StockTakeClass>> getStockTakeMaster(@Body RequestBody url);

}
