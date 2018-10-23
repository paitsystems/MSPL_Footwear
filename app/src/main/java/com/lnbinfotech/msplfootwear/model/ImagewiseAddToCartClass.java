package com.lnbinfotech.msplfootwear.model;

//Created by Anup on 10/17/2018.

import java.io.Serializable;

public class ImagewiseAddToCartClass implements Serializable {

    private String imageName, colour, hashCode;

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getColour() {
        return colour;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }

    public String getHashCode() {
        return hashCode;
    }

    public void setHashCode(String hashCode) {
        this.hashCode = hashCode;
    }
}
