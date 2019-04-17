package com.lnbinfotech.msplfootwear.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.lnbinfotech.msplfootwear.R;
import com.lnbinfotech.msplfootwear.TouchImageView;
import com.lnbinfotech.msplfootwear.constant.Constant;
import com.squareup.picasso.Picasso;

public class Fragment_SchemeFullImage extends Fragment {

    private String img_name = "", url = "";
    private TouchImageView imgv_img;
    private ImageView img;

    public static Fragment_SchemeFullImage newInstance(String imgName) {
        Fragment_SchemeFullImage absFragment = new Fragment_SchemeFullImage();
        Bundle args = new Bundle();
        args.putString("Imagename", imgName);
        absFragment.setArguments(args);
        return absFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        img_name = getArguments().getString("Imagename");
    }

    @Override
    public void onResume() {
        super.onResume();
        Constant.showLog("AboutUsFragment_onResume");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_schemefullimage, container, false);
        imgv_img = view.findViewById(R.id.touch_imageview);
        img = view.findViewById(R.id.img);
        saveImagesToInternal();
        return view;
    }

    private void saveImagesToInternal() {
        img_name = img_name.replace(" ", "%20");
        url = Constant.imgUrl+img_name+".jpg";
        Constant.showLog(url);
        Picasso.with(getContext())
                .load("http://103.109.13.200:24086/IMAGES/Scheme/SchoolShoes.jpg")
                .placeholder(R.drawable.placehoder)
                .error(R.drawable.placehoder)
                .into(imgv_img);
        /*Glide.with(getContext()).load(url)
                .thumbnail(0.5f)
                .placeholder(R.drawable.placehoder)
                .error(R.drawable.placehoder)
                .crossFade()
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(img);*/

    }
}
