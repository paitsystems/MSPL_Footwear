package com.lnbinfotech.msplfootwear.fragments;

//Created by lnb on 9/26/2017.

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lnbinfotech.msplfootwear.R;

public class HawaiNEvaCategoryFragment extends Fragment{

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hawaineva_category,container,false);
        return view;
    }
}
