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
import com.lnbinfotech.msplfootwearex.adapters.GentsCategoryGridAdapter;
import com.lnbinfotech.msplfootwearex.constant.Constant;
import com.lnbinfotech.msplfootwearex.db.DBHandler;
import com.lnbinfotech.msplfootwearex.model.GentsCategoryClass;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

//Created by lnb on 9/26/2017.

public class IGentsCategoryFragment extends Fragment {

    private GridView gridView;
    private GentsCategoryGridAdapter adapter;
    private String cat = "",subCat = "";

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
        cat = getArguments().getString("cat");
        subCat = getArguments().getString("subcat");
        Constant.showLog(cat+"-"+subCat);
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
                Intent intent = new Intent(getContext(), ImageWiseAddToCartActivity.class);
                intent.putExtra("data", gentClass);
                intent.putExtra("cat9", cat);
                intent.putExtra("cat2", subCat);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.enter, R.anim.exit);
            }
        });
        Constant.showLog(cat+"-onCreateView()");
        return view;
    }

    private void setData() {
        try {
            List<GentsCategoryClass> list = new ArrayList<>();
            DBHandler db = new DBHandler(getContext());
            Cursor res = db.getImageSubCategory1(cat, subCat);
            if (res.moveToFirst()) {
                do {
                    GentsCategoryClass gentsClass = new GentsCategoryClass();
                    gentsClass.setCategoryName(res.getString(res.getColumnIndex(DBHandler.PM_ProdId)));
                    gentsClass.setMrp(res.getString(res.getColumnIndex(DBHandler.PM_MRPRate)));
                    gentsClass.setMarkup(res.getString(res.getColumnIndex(DBHandler.PM_MarkUp)));
                    gentsClass.setMarkdown(res.getString(res.getColumnIndex(DBHandler.PM_MarkDown)));
                    gentsClass.setWsp(res.getString(res.getColumnIndex(DBHandler.PM_SRate)));
                    gentsClass.setProductName(res.getString(res.getColumnIndex(DBHandler.PM_Finalprod)));
                    gentsClass.setHsnCode(res.getString(res.getColumnIndex(DBHandler.PM_HSNCode)));
                    gentsClass.setProdId(res.getInt(res.getColumnIndex(DBHandler.PM_ProductID)));
                    gentsClass.setProductId(res.getString(res.getColumnIndex(DBHandler.PM_ProdId)));
                    gentsClass.setGstPer(res.getString(res.getColumnIndex(DBHandler.GST_GSTPer)));
                    gentsClass.setGstGroupName(res.getString(res.getColumnIndex(DBHandler.GST_GroupNm)));
                    gentsClass.setCat3(res.getString(res.getColumnIndex(DBHandler.PM_Cat3)));

                    String img = res.getString(res.getColumnIndex(DBHandler.ARSD_ImageName));
                    String imgArr[] = img.split("\\,");
                    img = Constant.imgUrl;
                    if (imgArr.length > 1) {
                        String img1 = imgArr[0];
                        //img1 = URLEncoder.encode(img1, "UTF-8");
                        img1 = img1.replace(" ", "%20");
                        img = img + img1;
                    }
                    img = img + ".jpg";
                    Constant.showLog(img);
                    gentsClass.setImgName(img);
                    list.add(gentsClass);
                } while (res.moveToNext());
            }
            res.close();
            adapter = new GentsCategoryGridAdapter(getContext(), list);
            gridView.setAdapter(adapter);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
