package com.lnbinfotech.msplfootwear;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.lnbinfotech.msplfootwear.adapters.FullImageAdapter;
import com.lnbinfotech.msplfootwear.constant.Constant;
import com.lnbinfotech.msplfootwear.log.WriteLog;
import com.lnbinfotech.msplfootwear.model.GentsCategoryClass;
import com.lnbinfotech.msplfootwear.model.ImagewiseAddToCartClass;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class FullImageActivity extends AppCompatActivity implements View.OnClickListener {

    private Constant constant, constant1;
    private Toast toast;
    private ViewPager pager;
    private List<String> mImageList;
    private FullImageAdapter adapter;
    private ImagewiseAddToCartClass prod = null;
    private int pos = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_image);

        if(Constant.liveTestFlag==1) {
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
        pager.setCurrentItem(pos);
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
            writeLog("getImgTitleList_"+e.getMessage());
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
