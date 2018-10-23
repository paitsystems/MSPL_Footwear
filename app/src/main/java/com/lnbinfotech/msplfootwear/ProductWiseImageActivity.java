package com.lnbinfotech.msplfootwear;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.lnbinfotech.msplfootwear.adapters.CategoryWiseImageAdapter;
import com.lnbinfotech.msplfootwear.adapters.ProductWiseImageRecyclerAdapter;
import com.lnbinfotech.msplfootwear.constant.Constant;
import com.lnbinfotech.msplfootwear.db.DBHandler;
import com.lnbinfotech.msplfootwear.interfaces.RecyclerViewToActivityInterface;
import com.lnbinfotech.msplfootwear.log.WriteLog;
import com.lnbinfotech.msplfootwear.model.GentsCategoryClass;
import com.lnbinfotech.msplfootwear.model.ImagewiseAddToCartClass;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ProductWiseImageActivity extends AppCompatActivity implements RecyclerViewToActivityInterface {

    private RecyclerView rv_images;
    private GridView gridView;
    private CategoryWiseImageAdapter adapter;
    private ImagewiseAddToCartClass prod = null;
    private DBHandler db;
    private String cat2, cat9, from, prodIdStr;
    private TextView tv_selColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_wise_image);

        init();

        try {
            cat9 = getIntent().getExtras().getString("cat9");
            cat2 = getIntent().getExtras().getString("cat2");
            from = getIntent().getExtras().getString("from");
            prodIdStr = getIntent().getExtras().getString("prodIdStr");
        }catch (Exception e){
            e.printStackTrace();
        }

        RecyclerView.LayoutManager mLayoutManager1 =
                new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        rv_images.setLayoutManager(mLayoutManager1);
        rv_images.setItemAnimator(new DefaultItemAnimator());

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
        ImageWiseAddToCartActivity.map.clear();
        new Constant(ProductWiseImageActivity.this).doFinish();
    }

    @Override
    public void onItemClick(String size) {

    }

    @Override
    public void onImageClick(ImagewiseAddToCartClass _prod) {
        this.prod = _prod;
        String str = "Selected Color :- "+this.prod.getColour();
        tv_selColor.setText(str);
        setData1();
    }

    @Override
    public void onSizeGroupClick(String sizeGroup) {

    }

    private void init(){
        rv_images = (RecyclerView) findViewById(R.id.rv_images);
        gridView = (GridView) findViewById(R.id.gridView);
        db = new DBHandler(getApplicationContext());
        tv_selColor = (TextView) findViewById(R.id.tv_selColor);
        ImageWiseAddToCartActivity.map = new HashMap<>();
    }

    private void setData() {
        try {
            List<ImagewiseAddToCartClass> prodList = new ArrayList<>();
            Cursor res = db.getDistinctColourImageWise(AddToCartActivity.selProdId);
            int count = 0;
            if(res.moveToFirst()){
                do{
                    ImagewiseAddToCartClass prod = new ImagewiseAddToCartClass();
                    String imageName = prodIdStr+"_"+cat2+"_"
                            +res.getString(res.getColumnIndex(DBHandler.ARSD_Colour))+"_P1"+","+
                            prodIdStr+"_"+cat2+"_"
                            +res.getString(res.getColumnIndex(DBHandler.ARSD_Colour))+"_P2"+","+
                            prodIdStr+"_"+cat2+"_"
                            +res.getString(res.getColumnIndex(DBHandler.ARSD_Colour))+"_P3"+","+
                            prodIdStr+"_"+cat2+"_"
                            +res.getString(res.getColumnIndex(DBHandler.ARSD_Colour))+"_P4"+",NA";
                    imageName = imageName.replace(" ", "%20");
                    imageName = imageName.replace("+", "_Pls");
                    prod.setImageName(imageName);
                    prod.setColour(res.getString(res.getColumnIndex(DBHandler.ARSD_Colour)));
                    prod.setHashCode(res.getString(res.getColumnIndex(DBHandler.ARSD_HashCode)));
                    prodList.add(prod);
                    if(count==0){
                        this.prod = prod;
                        String str = "Selected Color :- "+prod.getColour();
                        tv_selColor.setText(str);
                    }
                    count++;
                }while (res.moveToNext());
            }
            res.close();
            ProductWiseImageRecyclerAdapter adapter = new ProductWiseImageRecyclerAdapter(getApplicationContext(), prodList);
            adapter.setOnClickListener1(this);
            rv_images.setAdapter(adapter);
            setData1();
        }catch (Exception e){
            e.printStackTrace();
            writeLog("setData_"+e.getMessage());
        }
    }

    private void setData1() {
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
            writeLog("setData1_"+e.getMessage());
        }
    }

    private void writeLog(String data){
        new WriteLog().writeLog(getApplicationContext(),"ProductWiseImageActivity_"+data);
    }

}
