package com.pait.exec.interfaces;

//Created by ANUP on 11/12/2017.

import com.pait.exec.model.ImagewiseAddToCartClass;

public interface RecyclerViewToActivityInterface {

    void onItemClick(String size);
    void onImageClick(ImagewiseAddToCartClass prod);
    void onSizeGroupClick(String sizeGroup);
}
