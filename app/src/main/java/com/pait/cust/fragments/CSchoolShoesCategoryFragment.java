package com.pait.cust.fragments;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.pait.cust.AddToCartActivity;
import com.pait.cust.R;
import com.pait.cust.adapters.SchoolShoeCategoryListAdapter;
import com.pait.cust.constant.Constant;
import com.pait.cust.db.DBHandler;
import com.pait.cust.model.GentsCategoryClass;

import java.util.ArrayList;
import java.util.List;

//Created by lnb on 9/26/2017.

public class CSchoolShoesCategoryFragment extends Fragment {

    private ListView listView;
    private SchoolShoeCategoryListAdapter adapter;
    private DBHandler db;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_schoolshoe_category_c,container,false);
        listView = (ListView) view.findViewById(R.id.listView);
        db = new DBHandler(getContext());
        setData();


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                GentsCategoryClass getClass = (GentsCategoryClass) adapter.getItem(i);
                Constant.showLog(getClass.getCategoryName());
                Intent intent = new Intent(getContext(), AddToCartActivity.class);
                intent.putExtra("cat9","School Shoes");
                intent.putExtra("cat2",getClass.getCategoryName());
                intent.putExtra("from", "cutsize");
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.enter,R.anim.exit);
            }
        });

        return view;
    }

    private void setData(){
        List<GentsCategoryClass> list = new ArrayList<>();
        Cursor res = db.getSubCategory("School Shoes");
        if(res.moveToFirst()){
            do {
                GentsCategoryClass gentsClass = new GentsCategoryClass();
                gentsClass.setCategoryName(res.getString(res.getColumnIndex(DBHandler.PM_Cat2)));
                list.add(gentsClass);
            }while (res.moveToNext());
        }
        res.close();
        adapter = new SchoolShoeCategoryListAdapter(getContext(),list);
        listView.setAdapter(adapter);
    }

}
