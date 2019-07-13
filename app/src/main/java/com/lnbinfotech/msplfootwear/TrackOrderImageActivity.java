package com.lnbinfotech.msplfootwear;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.lnbinfotech.msplfootwear.adapters.CategoryWiseImageAdapter;
import com.lnbinfotech.msplfootwear.adapters.ProductWiseImageRecyclerAdapter;
import com.lnbinfotech.msplfootwear.adapters.TrackOrderImageAdapter;
import com.lnbinfotech.msplfootwear.constant.Constant;
import com.lnbinfotech.msplfootwear.db.DBHandler;
import com.lnbinfotech.msplfootwear.interfaces.RecyclerViewToActivityInterface;
import com.lnbinfotech.msplfootwear.log.WriteLog;
import com.lnbinfotech.msplfootwear.model.GentsCategoryClass;
import com.lnbinfotech.msplfootwear.model.ImagewiseAddToCartClass;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TrackOrderImageActivity extends AppCompatActivity {

    private GridView gridView;
    private TrackOrderImageAdapter adapter;
    private ImagewiseAddToCartClass prod = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Constant.liveTestFlag == 1) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }

        setContentView(R.layout.activity_track_order_image);

        init();

        setData();

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), SchemeFullImageActivity.class);
                intent.putExtra("data", prod);
                intent.putExtra("pos", String.valueOf(i));
                startActivity(intent);
                overridePendingTransition(R.anim.enter, R.anim.exit);
            }
        });
    }

    @Override
    public void onBackPressed() {
        ImageWiseAddToCartActivity.map.clear();
        new Constant(TrackOrderImageActivity.this).doFinish();
    }

    private void init() {
        gridView = findViewById(R.id.gridView);
        ImageWiseAddToCartActivity.map = new HashMap<>();
    }

    private void setData() {
        try {
            List<GentsCategoryClass> list = new ArrayList<>();
            String imageName = TrackOrderDetailActivityChanged.imagePath + "," + TrackOrderDetailActivityChanged.PSImage;
            String fullImageName = "";
            String[] arr = imageName.split("\\,");
            if (arr.length > 1) {
                for (String img : arr) {
                    if (!img.equals("NA")) {
                        //5491_UGNT_PS_5159_ADESH_04_Jun_2019_15_20_02.jpg
                        try {
                            String imgArr[] = img.split("_");
                            String _imageName = Constant.trackOrderUrl + imgArr[6] + "/" + imgArr[5] + "/" + imgArr[1] + "/" + img;
                            fullImageName = fullImageName + _imageName + ",";
                            GentsCategoryClass cat = new GentsCategoryClass();
                            cat.setImgName(_imageName);
                            list.add(cat);
                        } catch (Exception e) {
                            e.printStackTrace();
                            writeLog("setData_" + e.getMessage());
                        }
                    }
                }
            }
            if (fullImageName.length() > 1) {
                fullImageName = fullImageName.substring(0, fullImageName.length() - 1);
            }
            prod = new ImagewiseAddToCartClass();
            prod.setImageName(fullImageName);
            adapter = new TrackOrderImageAdapter(getApplicationContext(), list);
            gridView.setAdapter(adapter);
        } catch (Exception e) {
            e.printStackTrace();
            writeLog("setData1_" + e.getMessage());
        }
    }

    private void writeLog(String data) {
        new WriteLog().writeLog(getApplicationContext(), "TrackOrderImageActivity_" + data);
    }

}

