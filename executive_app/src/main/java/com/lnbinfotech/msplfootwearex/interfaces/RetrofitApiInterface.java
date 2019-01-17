package com.lnbinfotech.msplfootwearex.interfaces;

//Created by Anup on 1/10/2019.

import com.lnbinfotech.msplfootwearex.model.BankBranchMasterClass;
import com.lnbinfotech.msplfootwearex.model.CustomerDetailClass;
import com.lnbinfotech.msplfootwearex.model.ProductMasterClass;
import com.lnbinfotech.msplfootwearex.model.SizeDesignMastDetClass;
import com.lnbinfotech.msplfootwearex.model.SizeNDesignClass;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface RetrofitApiInterface {

    @Headers("Content-Type: application/json")
    @POST("GetProductMasterPostV6")
    Call<List<ProductMasterClass>> getProductMasterV6(@Body RequestBody url);


    @Headers("Content-Type: application/json")
    @POST("GetAllSizeDesignMastDetV6")
    Call<List<SizeNDesignClass>> getAllSizeDesignMastDetV6(@Body RequestBody url);

    @Headers("Content-Type: application/json")
    @POST("GetSizeDesignMastDetV6")
    Call<List<SizeDesignMastDetClass>> getSizeDesignMastDetV6(@Body RequestBody url);

    @Headers("Content-Type: application/json")
    @POST("GetBankBranchMasterV6")
    Call<List<BankBranchMasterClass>> getBankBrnachMasterV6(@Body RequestBody url);

    @Headers("Content-Type: application/json")
    @POST("GetCustomerMasterV6")
    Call<List<CustomerDetailClass>> getCustomerMasterV6(@Body RequestBody url);
}
