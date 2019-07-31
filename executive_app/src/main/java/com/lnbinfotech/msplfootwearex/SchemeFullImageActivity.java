package com.lnbinfotech.msplfootwearex;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.lnbinfotech.msplfootwearex.adapters.SchemeFullImageAdapter;
import com.lnbinfotech.msplfootwearex.constant.Constant;
import com.lnbinfotech.msplfootwearex.log.WriteLog;
import com.lnbinfotech.msplfootwearex.model.ImagewiseAddToCartClass;

import java.util.ArrayList;
import java.util.List;

public class SchemeFullImageActivity extends AppCompatActivity  implements View.OnClickListener {

    private Constant constant, constant1;
    private Toast toast;
    private ViewPager pager;
    private List<String> mImageList;
    private SchemeFullImageAdapter adapter;
    private ImagewiseAddToCartClass prod = null;
    private int pos = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schemefullimage);

        if (Constant.liveTestFlag == 1) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }

        init();

        try {
            prod = (ImagewiseAddToCartClass) getIntent().getExtras().getSerializable("data");
            pos = Integer.parseInt(getIntent().getExtras().getString("pos"));
        }catch (Exception e){
            e.printStackTrace();
            writeLog("onCreate_"+e.getMessage());
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
        new Constant(SchemeFullImageActivity.this).doFinish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //showDia(0);
                new Constant(SchemeFullImageActivity.this).doFinish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setViewPager() {
        adapter = new SchemeFullImageAdapter(getSupportFragmentManager());
        getImgTitleList();
        adapter.getImageTitle(mImageList);
        pager.setAdapter(adapter);
        pager.setCurrentItem(pos);
    }

    private void getImgTitleList() {
        try {
            String imageName = prod.getImageName();
            String[] arr = imageName.split(",");
            if (arr.length >= 1) {
                for (String img : arr) {
                    if(!img.equals("NA")) {
                        mImageList.add(img);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            writeLog("getImgTitleList_"+e.getMessage());
        }
    }

    private void init() {
        mImageList = new ArrayList<>();
        constant = new Constant(SchemeFullImageActivity.this);
        constant1 = new Constant(getApplicationContext());
        toast = Toast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        pager = findViewById(R.id.pager);
    }

    private void writeLog(String _data) {
        new WriteLog().writeLog(getApplicationContext(), "SchemeFullImageActivity_" + _data);
    }
}