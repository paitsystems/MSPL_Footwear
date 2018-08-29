package com.lnbinfotech.msplfootwearex.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.lnbinfotech.msplfootwearex.R;
import com.lnbinfotech.msplfootwearex.TouchImageView;
import com.lnbinfotech.msplfootwearex.constant.Constant;

//Created by Anup on 21-08-2018.

public class Fragment_FullImage extends Fragment {

    private String img_name = "", url = "";
    private TouchImageView imgv_img;
    private ImageView img;

    public static Fragment_FullImage newInstance(String imgName) {
        Fragment_FullImage absFragment = new Fragment_FullImage();
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
        View view = inflater.inflate(R.layout.fragment_fullimage, container, false);
        imgv_img = (TouchImageView) view.findViewById(R.id.touch_imageview);
        img = (ImageView) view.findViewById(R.id.img);
        saveImagesToInternal();
        return view;
    }

    private void saveImagesToInternal() {
        url = Constant.imgUrl+img_name+".jpg";
        Constant.showLog(url);
        Glide.with(getContext()).load(url)
                .thumbnail(0.5f)
                .placeholder(R.drawable.placehoder)
                .error(R.drawable.placehoder)
                .crossFade()
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(img);

    }

}
