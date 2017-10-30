package com.lnbinfotech.msplfootwear.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.lnbinfotech.msplfootwear.R;
import com.lnbinfotech.msplfootwear.adapters.GentsCategoryGridAdapter;
import com.lnbinfotech.msplfootwear.model.GentsCategoryClass;

import java.util.ArrayList;
import java.util.List;

//Created by lnb on 9/26/2017.

public class IGentsCategoryFragment extends Fragment {

    private GridView gridView;
    private GentsCategoryGridAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gents_category_i,container,false);
        gridView = (GridView) view.findViewById(R.id.gridView);
        setData();
        return view;
    }

    private void setData(){
        List<GentsCategoryClass> list = new ArrayList<>();
        GentsCategoryClass gents1 = new GentsCategoryClass();
        gents1.setCategoryName("Category1");
        list.add(gents1);
        GentsCategoryClass gents2 = new GentsCategoryClass();
        gents2.setCategoryName("Category2");
        list.add(gents2);
        GentsCategoryClass gents3 = new GentsCategoryClass();
        gents3.setCategoryName("Category3");
        list.add(gents3);
        GentsCategoryClass gents4 = new GentsCategoryClass();
        gents4.setCategoryName("Category4");
        list.add(gents4);
        GentsCategoryClass gents5 = new GentsCategoryClass();
        gents5.setCategoryName("Category5");
        list.add(gents5);
        adapter = new GentsCategoryGridAdapter(getContext(),list);
        gridView.setAdapter(adapter);
    }
}
