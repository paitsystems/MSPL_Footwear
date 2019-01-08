package com.lnbinfotech.msplfootwear;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;

import com.lnbinfotech.msplfootwear.adapters.CategoryWiseImageAdapter;
import com.lnbinfotech.msplfootwear.constant.Constant;
import com.lnbinfotech.msplfootwear.log.WriteLog;
import com.lnbinfotech.msplfootwear.model.GentsCategoryClass;
import com.lnbinfotech.msplfootwear.model.ImagewiseAddToCartClass;

import java.util.ArrayList;
import java.util.List;

public class CategoryWiseImageActivity extends AppCompatActivity {

    private GridView gridView;
    private CategoryWiseImageAdapter adapter;
    private ImagewiseAddToCartClass prod = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Constant.liveTestFlag == 1) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }

        setContentView(R.layout.activity_category_wise_image);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }

        init();

        try {
            prod = (ImagewiseAddToCartClass) getIntent().getExtras().getSerializable("data");
        }catch (Exception e){
            e.printStackTrace();
            writeLog("onCreate_"+e.getMessage());
        }

        setData();

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), FullImageActivity.class);
                intent.putExtra("data", prod);
                intent.putExtra("pos", String.valueOf(i));
                startActivity(intent);
                overridePendingTransition(R.anim.enter, R.anim.exit);
            }
        });
    }

    @Override
    public void onBackPressed() {
        new Constant(CategoryWiseImageActivity.this).doFinish();
    }

    private void init() {
        gridView = (GridView) findViewById(R.id.gridView);
    }

    private void setData() {
        try {
            List<GentsCategoryClass> list = new ArrayList<>();
            String imageName = prod.getImageName();
            String[] arr = imageName.split("\\,");
            if(arr.length>1){
                for(String img : arr){
                    if(!img.equals("NA")) {
                        GentsCategoryClass cat = new GentsCategoryClass();
                        cat.setImgName(img);
                        list.add(cat);
                    }
                }
            }
            adapter = new CategoryWiseImageAdapter(getApplicationContext(), list);
            gridView.setAdapter(adapter);
        } catch (Exception e) {
            e.printStackTrace();
            writeLog("setData_"+e.getMessage());
        }
    }

    private void writeLog(String _data) {
        new WriteLog().writeLog(getApplicationContext(), "CategoryWiseImageActivity_" + _data);
    }

}