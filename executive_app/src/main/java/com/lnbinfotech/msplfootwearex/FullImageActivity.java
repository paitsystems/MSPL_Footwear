package com.lnbinfotech.msplfootwearex;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.lnbinfotech.msplfootwearex.adapters.CategoryWiseImageAdapter;
import com.lnbinfotech.msplfootwearex.adapters.FullImageAdapter;
import com.lnbinfotech.msplfootwearex.constant.Constant;
import com.lnbinfotech.msplfootwearex.log.WriteLog;
import com.lnbinfotech.msplfootwearex.model.GentsCategoryClass;
import com.lnbinfotech.msplfootwearex.model.ImagewiseAddToCartClass;

import java.util.ArrayList;
import java.util.List;

//Created by Anup on 21-08-2018.

public class FullImageActivity extends AppCompatActivity implements View.OnClickListener {

    private Constant constant, constant1;
    private Toast toast;
    private ViewPager pager;
    private List<String> mImageList;
    private FullImageAdapter adapter;
    private ImagewiseAddToCartClass prod = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullimage);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }

        init();

        try {
            prod = (ImagewiseAddToCartClass) getIntent().getExtras().getSerializable("data");
        }catch (Exception e){
            e.printStackTrace();
        }

        setViewPager();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case 0:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        new Constant(FullImageActivity.this).doFinish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //showDia(0);
                new Constant(FullImageActivity.this).doFinish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setViewPager() {
        adapter = new FullImageAdapter(getSupportFragmentManager());
        getImgTitleList();
        adapter.getImageTitle(mImageList);
        pager.setAdapter(adapter);
    }

    private void getImgTitleList() {
        try {
            String imageName = prod.getImageName();
            String[] arr = imageName.split("\\,");
            if (arr.length > 1) {
                for (String img : arr) {
                    if(!img.equals("NA")) {
                        mImageList.add(img);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void init() {
        mImageList = new ArrayList<>();
        constant = new Constant(FullImageActivity.this);
        constant1 = new Constant(getApplicationContext());
        toast = Toast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        pager = (ViewPager) findViewById(R.id.pager);
    }

    private void writeLog(String _data) {
        new WriteLog().writeLog(getApplicationContext(), "FullImageActivity_" + _data);
    }
}
