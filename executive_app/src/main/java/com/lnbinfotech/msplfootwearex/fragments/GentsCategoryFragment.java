package com.lnbinfotech.msplfootwearex.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.lnbinfotech.msplfootwearex.R;
import com.lnbinfotech.msplfootwearex.adapters.GentsCategoryListAdapter;
import com.lnbinfotech.msplfootwearex.constant.Constant;
import com.lnbinfotech.msplfootwearex.db.DBHandler;
import com.lnbinfotech.msplfootwearex.model.GentsCategoryClass;

import java.util.ArrayList;
import java.util.List;

//Created by lnb on 9/26/2017.

public class GentsCategoryFragment extends Fragment {

    private ListView listView;
    private GentsCategoryListAdapter adapter;
    private DBHandler db;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gents_category,container,false);
        listView = (ListView) view.findViewById(R.id.listView);
        db = new DBHandler(getContext());
        setData();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                GentsCategoryClass getClass = (GentsCategoryClass) adapter.getItem(i);
                Constant.showLog(getClass.getCategoryName());
            }
        });

        return view;
    }

    private void setData(){
        List<GentsCategoryClass> list = new ArrayList<>();
        Cursor res = db.getSubCategory("Gents");
        if(res.moveToFirst()){
            do {
                GentsCategoryClass gentsClass = new GentsCategoryClass();
                gentsClass.setCategoryName(res.getString(res.getColumnIndex(DBHandler.PM_Cat2)));
                list.add(gentsClass);
            }while (res.moveToNext());
        }
        adapter = new GentsCategoryListAdapter(getContext(),list);
        listView.setAdapter(adapter);
    }
}
