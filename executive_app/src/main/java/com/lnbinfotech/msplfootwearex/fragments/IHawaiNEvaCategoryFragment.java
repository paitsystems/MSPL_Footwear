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

import com.lnbinfotech.msplfootwearex.ImageWiseAddToCartActivity;
import com.lnbinfotech.msplfootwearex.R;
import com.lnbinfotech.msplfootwearex.adapters.HawaiNEvaCategoryGridAdapter;
import com.lnbinfotech.msplfootwearex.constant.Constant;
import com.lnbinfotech.msplfootwearex.db.DBHandler;
import com.lnbinfotech.msplfootwearex.model.GentsCategoryClass;

import java.util.ArrayList;
import java.util.List;

public class IHawaiNEvaCategoryFragment extends Fragment{


    private GridView gridView;
    private HawaiNEvaCategoryGridAdapter adapter;
    //private int[] drawId = {R.drawable.formal,R.drawable.simulus,R.drawable.paralite,R.drawable.acusole,R.drawable.casual};

    private String[] drawId = {"http://103.68.10.9:24086/IMAGES/770G_Classic Boys Double Velcro_Black_P3.jpg",
            "http://103.68.10.9:24086/IMAGES/156S_Stimulus Hawai Slippers_Yellow_P1.jpg",
            "http://103.68.10.9:24086/IMAGES/1395_Paralite_Violet_P2.jpg",
            "http://103.68.10.9:24086/IMAGES/7018XL_Solea_Violet_P2.jpg",
            "http://103.68.10.9:24086/IMAGES/7948_Solea_Cherry_P4.jpg"};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hawaineva_category_i,container,false);
        gridView = (GridView) view.findViewById(R.id.gridView);
        setData();
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                GentsCategoryClass gentClass = (GentsCategoryClass) adapterView.getItemAtPosition(i);
                Intent intent = new Intent(getContext(), ImageWiseAddToCartActivity.class);
                intent.putExtra("data",gentClass);
                //intent.putExtra("id",drawId[i]);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.enter,R.anim.exit);
            }
        });

        return view;
    }

    private void setData(){
        List<GentsCategoryClass> list = new ArrayList<>();
        /*GentsCategoryClass gents1 = new GentsCategoryClass();
        gents1.setCategoryName("Formal");
        gents1.setMrp("275");
        gents1.setMargin("30%");
        gents1.setProductName("9505-Max Premium-Gents-6 x 10-PU-Shoes");
        list.add(gents1);

        GentsCategoryClass gents3 = new GentsCategoryClass();
        gents3.setCategoryName("Simulus");
        gents3.setMrp("175");
        gents3.setMargin("20%");
        gents3.setProductName("9701Old-Simulus-Gents-6X10-TPR-Shoes");
        list.add(gents3);

        GentsCategoryClass gents4 = new GentsCategoryClass();
        gents4.setCategoryName("Paralite");
        gents4.setMrp("425");
        gents4.setMargin("60%");
        gents4.setProductName("1210-Paralite-Gents-6 x 10-EVA-Flat");
        list.add(gents4);

        GentsCategoryClass gents5 = new GentsCategoryClass();
        gents5.setCategoryName("Acusole");
        gents5.setMrp("625");
        gents5.setMargin("15%");
        gents5.setProductName("34Acu-Acusole-Gents-6X9-EVA Rubber-Flat");
        list.add(gents5);

        GentsCategoryClass gents6 = new GentsCategoryClass();
        gents6.setCategoryName("Casual");
        gents5.setMrp("325");
        gents5.setMargin("11%");
        gents5.setProductName("66050-Casual-Gents-6X10-PU-Flat Chappals");
        list.add(gents6);
        adapter = new HawaiNEvaCategoryGridAdapter(getContext(),list,drawId);
        gridView.setAdapter(adapter);*/

        DBHandler db = new DBHandler(getContext());
        Cursor res = db.getImageSubCategory("Hawai & Eva");
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
        adapter = new HawaiNEvaCategoryGridAdapter(getContext(),list,drawId);
        gridView.setAdapter(adapter);
    }
}

