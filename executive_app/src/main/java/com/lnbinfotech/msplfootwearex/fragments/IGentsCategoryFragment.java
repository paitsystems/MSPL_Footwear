package com.lnbinfotech.msplfootwearex.fragments;

import android.content.Context;
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
import com.lnbinfotech.msplfootwearex.adapters.GentsCategoryGridAdapter;
import com.lnbinfotech.msplfootwearex.adapters.GentsCategoryListAdapter;
import com.lnbinfotech.msplfootwearex.constant.Constant;
import com.lnbinfotech.msplfootwearex.db.DBHandler;
import com.lnbinfotech.msplfootwearex.interfaces.TestInterface;
import com.lnbinfotech.msplfootwearex.model.GentsCategoryClass;

import java.util.ArrayList;
import java.util.List;

//Created by lnb on 9/26/2017.

public class IGentsCategoryFragment extends Fragment {

    private GridView gridView;
    private GentsCategoryGridAdapter adapter;
    //private int[] drawId = {R.drawable.formal,
    //R.drawable.simulus,R.drawable.paralite,R.drawable.acusole,R.drawable.casual};
    private String cat = "",subCat = "";
    private Context context;

    private String[] drawId = {"http://103.68.10.9:24086/IMAGES/66001_Vertex_Tan_P1.jpg", "http://103.68.10.9:24086/IMAGES/66115_Vertex_Brown_P2.jpg", "http://103.68.10.9:24086/IMAGES/6115V+_Vertex Plus_Brown_P2.jpg", "http://103.68.10.9:24086/IMAGES/9542_Max Premium_Tan_P3.jpg", "http://103.68.10.9:24086/IMAGES/1024S_Stimulus Chappals_Red_P1.jpg"};

    public static IGentsCategoryFragment newInstance(String _cat, String _subCat){
        IGentsCategoryFragment frag = new IGentsCategoryFragment();
        Bundle args = new Bundle();
        args.putString("cat", _cat);
        args.putString("subcat", _subCat);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Constant.showLog("onCreate()");
        cat = getArguments().getString("cat");
        subCat = getArguments().getString("subcat");
        //Constant.showLog(cat+"-"+subCat);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gents_category_i, container, false);
        gridView = (GridView) view.findViewById(R.id.gridView);

        setData();

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                GentsCategoryClass gentClass = (GentsCategoryClass) adapterView.getItemAtPosition(i);
                Intent intent = new Intent(getContext(), FullImageActivity.class);
                intent.putExtra("data", gentClass);
                //intent.putExtra("id", drawId[i]);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.enter, R.anim.exit);
            }
        });
        Constant.showLog(cat+"-onCreateView()");
        return view;
    }

    private void setData() {
        List<GentsCategoryClass> list = new ArrayList<>();
        DBHandler db = new DBHandler(getContext());
        Cursor res = db.getImageSubCategory1(cat,subCat);
        if(res.moveToFirst()){
            do {
                GentsCategoryClass gentsClass = new GentsCategoryClass();
                gentsClass.setCategoryName(res.getString(res.getColumnIndex(DBHandler.PM_ProdId)));
                String img = res.getString(res.getColumnIndex(DBHandler.ARSD_ImageName));
                String imgArr[] = img.split("\\,");
                img = Constant.imgUrl;
                if(imgArr.length>1){
                    img = img+imgArr[0];
                }
                img = img + ".jpg";
                //Constant.showLog(img);
                gentsClass.setImgName(img);
                list.add(gentsClass);
            }while (res.moveToNext());
        }
        res.close();
        adapter = new GentsCategoryGridAdapter(getContext(),list,drawId);
        gridView.setAdapter(adapter);
    }

}
