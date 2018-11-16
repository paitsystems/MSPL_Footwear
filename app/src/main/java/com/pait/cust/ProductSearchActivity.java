package com.pait.cust;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.pait.cust.adapters.ProductSearchAdapter;
import com.pait.cust.constant.Constant;
import com.pait.cust.db.DBHandler;
import com.pait.cust.log.WriteLog;
import com.pait.cust.model.GentsCategoryClass;
import com.pait.cust.model.ProductMasterClass;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ProductSearchActivity extends AppCompatActivity implements View.OnClickListener {

    private Constant constant, constant1;
    private Toast toast;
    private EditText ed_search;
    private ListView listView;
    private DBHandler db;
    private List<ProductMasterClass> prodList;
    private ProductSearchAdapter adapter;
    private String cat2, cat9, from;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(Constant.liveTestFlag==1) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }

        setTheme(R.style.AppThemeAddToCard);
        setContentView(R.layout.activity_product_search);

        cat9 = getIntent().getExtras().getString("cat9");
        cat2 = getIntent().getExtras().getString("cat2");
        from = getIntent().getExtras().getString("from");

        init();

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        ed_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String text = ed_search.getText().toString().toLowerCase(Locale.getDefault());
                if(adapter!=null) {
                    adapter.filter(text);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ((InputMethodManager)getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(listView.getWindowToken(),0);
                ProductMasterClass prodClass = (ProductMasterClass) listView.getItemAtPosition(i);
                AddToCartActivity.selProd = prodClass.getFinal_prod();
                AddToCartActivity.selProdId = prodClass.getProduct_id();
                AddToCartActivity.activityToFrom = 1;
                Constant.showLog(AddToCartActivity.selProd);
                Constant.showLog(""+AddToCartActivity.selProdId);
                if(from.equals("cutsize")) {
                    Intent intent = new Intent(getApplicationContext(), AddToCartActivity.class);
                    intent.putExtra("cat9",prodClass.getCat9());
                    intent.putExtra("cat2",prodClass.getCat2());
                    intent.putExtra("from", "prodsearch");
                    finish();
                    startActivity(intent);
                    overridePendingTransition(R.anim.left_to_right,R.anim.right_to_left);
                }else if(from.equals("imagewisesize")) {
                    GentsCategoryClass gentsClass = new GentsCategoryClass();
                    Cursor res = db.getImageSubCategory2(prodClass.getCat9(), prodClass.getCat2());
                    if (res.moveToFirst()) {
                        do {
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
                            String img = Constant.imgUrl + "NA.jpg";
                            Constant.showLog(img);
                            gentsClass.setImgName(img);
                        } while (res.moveToNext());
                    }
                    res.close();
                    Intent intent = new Intent(getApplicationContext(), ImageWiseAddToCartActivity.class);
                    intent.putExtra("data", gentsClass);
                    intent.putExtra("cat9",prodClass.getCat9());
                    intent.putExtra("cat2",prodClass.getCat2());
                    finish();
                    startActivity(intent);
                    overridePendingTransition(R.anim.enter, R.anim.exit);
                }else{
                    new Constant(ProductSearchActivity.this).doFinish();
                }
            }
        });
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
        //showDia(0);
        new Constant(ProductSearchActivity.this).doFinish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //showDia(0);
                new Constant(ProductSearchActivity.this).doFinish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void init() {
        constant = new Constant(ProductSearchActivity.this);
        constant1 = new Constant(getApplicationContext());
        toast = Toast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        ed_search = (EditText) findViewById(R.id.ed_search);
        listView = (ListView) findViewById(R.id.listView);
        db = new DBHandler(getApplicationContext());
        prodList = new ArrayList<>();
        FirstActivity.pref = getSharedPreferences(FirstActivity.PREF_NAME,MODE_PRIVATE);
        Cursor res = db.getFinalProduct(cat2,cat9);
        if(res.moveToFirst()){
            do{
                ProductMasterClass prodClass = new ProductMasterClass();
                prodClass.setProduct_id(res.getInt(res.getColumnIndex(DBHandler.PM_ProductID)));
                prodClass.setFinal_prod(res.getString(res.getColumnIndex(DBHandler.PM_Finalprod)));
                prodClass.setCat2(res.getString(res.getColumnIndex(DBHandler.PM_Cat2)));
                prodClass.setCat9(res.getString(res.getColumnIndex(DBHandler.PM_Cat9)));
                prodList.add(prodClass);
            }while(res.moveToNext());
        }
        res.close();
        if(prodList.size()==0) {
            ProductMasterClass prodClass = new ProductMasterClass();
            prodClass.setProduct_id(0);
            prodClass.setFinal_prod("NA");
            prodClass.setCat2("1");
            prodClass.setCat9("1");
            prodList.add(prodClass);
        }
        adapter = new ProductSearchAdapter(prodList, getApplicationContext());
        listView.setAdapter(adapter);
    }

    private void showDia(int a) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ProductSearchActivity.this);
        builder.setCancelable(false);
        if (a == 0) {
            builder.setMessage("Do You Want To Exit App?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    new Constant(ProductSearchActivity.this).doFinish();
                    dialog.dismiss();
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        }
        builder.create().show();
    }

    private void writeLog(String _data) {
        new WriteLog().writeLog(getApplicationContext(), "ProductSearchActivity_" + _data);
    }
}
