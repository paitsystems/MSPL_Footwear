package com.pait.dispatch_app.interfaces;

//Created by Anup on 1/10/2019.

import com.pait.dispatch_app.model.DispatchMasterClass;
import com.pait.dispatch_app.model.ProductMasterClass;
import com.pait.dispatch_app.model.StockTakeClass;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface RetrofitApiInterface {

    @Headers("Content-Type: application/json")
    @POST("GetDispatchData")
    Call<List<DispatchMasterClass>> getDispatchMaster(@Body RequestBody url);

    @Headers("Content-Type: application/json")
    @POST("GetStockTakeData")
    Call<List<StockTakeClass>> getStockTakeMaster(@Body RequestBody url);

    @Headers("Content-Type: application/json")
    @POST("GetStockTakeReport")
    Call<List<StockTakeClass>> getStockTakeReport(@Body RequestBody url);

    @Headers("Content-Type: application/json")
    @POST("GetProductMasterPostV6")
    Call<List<ProductMasterClass>> getProductMasterV6(@Body RequestBody url);

    @Multipart
    @POST("/upload")
    Call<ResponseBody> uploadImage(@Part MultipartBody.Part file, @Part("name") RequestBody requestBody);

}
