package com.lnbinfotech.msplfootwearex.fragments;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.lnbinfotech.msplfootwearex.FullImageActivity;
import com.lnbinfotech.msplfootwearex.R;
import com.lnbinfotech.msplfootwearex.adapters.SchoolShoeCategoryGridAdapter;
import com.lnbinfotech.msplfootwearex.constant.Constant;
import com.lnbinfotech.msplfootwearex.db.DBHandler;
import com.lnbinfotech.msplfootwearex.model.GentsCategoryClass;

import java.util.ArrayList;
import java.util.List;

//Created by ANUP on 3/5/2018.

public class TabChildFragment extends Fragment {

    private GridView gridView;
    private SchoolShoeCategoryGridAdapter adapter;
    private String[] drawId = new String[]{"R.drawable.formal"};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_schoolshoe_category_i,container,false);
        gridView = (GridView) view.findViewById(R.id.gridView);
        setData();
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                GentsCategoryClass gentClass = (GentsCategoryClass) adapterView.getItemAtPosition(i);
                Intent intent = new Intent(getContext(), FullImageActivity.class);
                intent.putExtra("data",gentClass);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.enter,R.anim.exit);
            }
        });

        return view;
    }

    private void setData(){
        List<GentsCategoryClass> list = new ArrayList<>();

        DBHandler db = new DBHandler(getContext());
        Cursor res = db.getImageSubCategory("School Shoes");
        if(res.moveToFirst()){
            do {
                GentsCategoryClass gentsClass = new GentsCategoryClass();
                gentsClass.setCategoryName(res.getString(res.getColumnIndex(DBHandler.PM_Cat2)));
                String img = res.getString(res.getColumnIndex(DBHandler.ARSD_ImageName));
                String imgArr[] = img.split("\\,");
                img = Constant.imgUrl;
                if(imgArr.length>1){
                    img = img+imgArr[0];
                }
                img = img + ".jpg";
                Constant.showLog(img);
                gentsClass.setImgName(img);
                list.add(gentsClass);
            }while (res.moveToNext());
        }
        res.close();
        adapter = new SchoolShoeCategoryGridAdapter(getContext(),list,drawId);
        gridView.setAdapter(adapter);

    }
}
