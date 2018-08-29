package com.lnbinfotech.msplfootwearex.interfaces;

//Created by ANUP on 11/12/2017.

import com.lnbinfotech.msplfootwearex.model.ImagewiseAddToCartClass;

public interface RecyclerViewToActivityInterface {

    void onItemClick(String size);
    void onImageClick(ImagewiseAddToCartClass prod);
}
