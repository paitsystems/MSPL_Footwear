package com.lnbinfotech.msplfootwear;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.lnbinfotech.msplfootwear.adapters.SizeGroupWiseColourAdapter;
import com.lnbinfotech.msplfootwear.adapters.SizeGroupWiseQtyAdapter;
import com.lnbinfotech.msplfootwear.constant.Constant;
import com.lnbinfotech.msplfootwear.db.DBHandler;
import com.lnbinfotech.msplfootwear.interfaces.RecyclerViewToActivityInterface;
import com.lnbinfotech.msplfootwear.log.WriteLog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AddToCartActivity extends AppCompatActivity implements View.OnClickListener,RecyclerViewToActivityInterface {

    private Constant constant, constant1;
    private Toast toast;

    private EditText ed_prod_search;
    private RadioButton rdo_pack, rdo_unpack;
    private Spinner sp_sizeGroup;
    private TextView tv_wsp, tv_mrp, tv_hsncode, tv_gstper, tv_add_to_card, tv_checkstock,
                        tv_add_to_card_final, tv_checkout, tv_vieworder;
    private RecyclerView rv_size, rv_color;

    private String cat2, cat9;
    private DBHandler db;
    private List<String> sizeGroup_list, size_list, colour_list;
    private String selSizeGroup = "";
    public static String selProd = null;
    public static int selProdId = 0, selQty = -1, selColor = -1;
    public static HashMap<Integer, Integer> map;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_cart);

        init();

        cat9 = getIntent().getExtras().getString("cat9");
        cat2 = getIntent().getExtras().getString("cat2");

        ed_prod_search.setOnClickListener(this);
        rdo_pack.setOnClickListener(this);
        rdo_unpack.setOnClickListener(this);
        tv_add_to_card.setOnClickListener(this);
        tv_checkstock.setOnClickListener(this);
        tv_add_to_card_final.setOnClickListener(this);
        tv_checkout.setOnClickListener(this);
        tv_vieworder.setOnClickListener(this);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        sp_sizeGroup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selSizeGroup = (String) adapterView.getItemAtPosition(i);
                Constant.showLog(selSizeGroup);
                setSizeData(selSizeGroup);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(selProd!=null){
            ed_prod_search.setText(selProd);
            setData();
        }
        constant = new Constant(AddToCartActivity.this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ed_prod_search:
                Intent intent = new Intent(getApplicationContext(), ProductSearchActivity.class);
                intent.putExtra("cat9",cat9);
                intent.putExtra("cat2",cat2);
                startActivity(intent);
                overridePendingTransition(R.anim.enter,R.anim.exit);
                break;
            case R.id.rdo_pack:
                rdo_pack.setChecked(true);
                rdo_unpack.setChecked(false);
                setData();
                break;
            case R.id.rdo_unpack:
                rdo_pack.setChecked(false);
                rdo_unpack.setChecked(true);
                setData();
                break;
            case R.id.tv_add_to_card:
                if(validateOrder()){
                    addToCard();
                }else{
                    toast.setText("Please Select Product");
                    toast.show();
                }
                break;
            case R.id.tv_check_stock:
                break;
            case R.id.tv_add_to_card_final:
                break;
            case R.id.tv_checkout:
                break;
            case R.id.tv_vieworder:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        //showDia(0);
        selProd = null;
        new Constant(AddToCartActivity.this).doFinish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //showDia(0);
                selProd = null;
                new Constant(AddToCartActivity.this).doFinish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private String getPackUnPack(){
        String packUnpackType;
        if(rdo_pack.isChecked()){
            packUnpackType = "M";
        }else{
            packUnpackType = "D";
        }
        return packUnpackType;
    }

    private void setData(){
        Cursor res = db.getProductDetails(getPackUnPack());
        if(res.moveToFirst()){
            do{
                tv_wsp.setText(res.getString(res.getColumnIndex(DBHandler.PM_SRate)));
                tv_mrp.setText(res.getString(res.getColumnIndex(DBHandler.PM_PRate)));
                tv_hsncode.setText(res.getString(res.getColumnIndex(DBHandler.PM_HSNCode)));
                tv_gstper.setText(res.getString(res.getColumnIndex(DBHandler.PM_GSTGroup)));
            }while (res.moveToNext());
        }
        res.close();

        sizeGroup_list.clear();
        sizeGroup_list.add("Select SizeGroup");
        Cursor res1 = db.getDistinctSizeGroup(getPackUnPack());
        if(res1.moveToFirst()){
            do{
                sizeGroup_list.add(res1.getString(res1.getColumnIndex(DBHandler.ARSD_SizeGroup)));
            }while (res1.moveToNext());
        }else{
            sizeGroup_list.add("NA");
        }
        res1.close();
        sp_sizeGroup.setAdapter(new ArrayAdapter<>(getApplicationContext(), R.layout.sizegroup_spinner_row, sizeGroup_list));
    }

    private void setSizeData(String sizegroup) {
        size_list.clear();
        colour_list.clear();
        rv_size.setAdapter(null);
        rv_color.setAdapter(null);
        String compPack;
        Cursor res1 = db.getDistinctSizes(getPackUnPack(), sizegroup);
        if (res1.moveToFirst()) {
            do {
                size_list.add(res1.getString(res1.getColumnIndex(DBHandler.ARSD_Total)));
                compPack = res1.getString(1);
            } while (res1.moveToNext());
            size_list.add(compPack);
        } else {
            size_list.add("NA");
        }
        res1.close();
        if(size_list.size()!=0) {
            SizeGroupWiseQtyAdapter adapter = new SizeGroupWiseQtyAdapter(size_list, getApplicationContext());
            adapter.setOnClickListener1(this);
            rv_size.setAdapter(adapter);
        }else{
            toast.setText("No Qty Avalilable");
            toast.show();
        }
    }

    private void setColour(String size){
        colour_list.clear();
        rv_color.setAdapter(null);
        Cursor res1 = db.getDistinctColour(size);
        if (res1.moveToFirst()) {
            do {
                String colourHashcode = res1.getString(res1.getColumnIndex(DBHandler.ARSD_Colour)) +
                                        "-" +
                                        res1.getString(res1.getColumnIndex(DBHandler.ARSD_HashCode));
                colour_list.add(colourHashcode);
            } while (res1.moveToNext());
        } else {
            colour_list.add("NA");
        }
        res1.close();
        if(colour_list.size()!=0) {
            SizeGroupWiseColourAdapter adapter = new SizeGroupWiseColourAdapter(colour_list, getApplicationContext());
            rv_color.setAdapter(adapter);
        }else{
            toast.setText("No Colour Available");
        }
    }

    private boolean validateOrder(){
        boolean stat = false;
        stat = !ed_prod_search.getText().toString().equals("");
        stat = (sp_sizeGroup.getSelectedItemPosition() != 0) && (sp_sizeGroup.getSelectedItemPosition() != -1);
        stat = selQty != -1;
        stat = map.size() != 0;
        return stat;
    }

    private void addToCard(){
        toast.setText("Add To Card");
        toast.show();
    }

    private void init() {
        constant = new Constant(AddToCartActivity.this);
        constant1 = new Constant(getApplicationContext());
        toast = Toast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        ed_prod_search = (EditText) findViewById(R.id.ed_prod_search);
        rdo_pack = (RadioButton) findViewById(R.id.rdo_pack);
        rdo_unpack = (RadioButton) findViewById(R.id.rdo_unpack);

        tv_wsp = (TextView) findViewById(R.id.tv_wsp);
        tv_mrp = (TextView) findViewById(R.id.tv_mrp);
        tv_hsncode = (TextView) findViewById(R.id.tv_hsncode);
        tv_gstper = (TextView) findViewById(R.id.tv_gstper);
        tv_add_to_card = (TextView) findViewById(R.id.tv_add_to_card);
        tv_checkstock = (TextView) findViewById(R.id.tv_check_stock);
        tv_add_to_card_final = (TextView) findViewById(R.id.tv_add_to_card_final);
        tv_checkout = (TextView) findViewById(R.id.tv_checkout);
        tv_vieworder = (TextView) findViewById(R.id.tv_vieworder);
        sp_sizeGroup = (Spinner) findViewById(R.id.sp_sizegroup);

        rv_size = (RecyclerView) findViewById(R.id.rv_size);
        rv_color = (RecyclerView) findViewById(R.id.rv_color);

        sizeGroup_list = new ArrayList<>();
        size_list = new ArrayList<>();
        colour_list = new ArrayList<>();
        db = new DBHandler(getApplicationContext());
        map = new HashMap<>();

    }

    private void showDia(int a) {
        AlertDialog.Builder builder = new AlertDialog.Builder(AddToCartActivity.this);
        builder.setCancelable(false);
        if (a == 0) {
            builder.setMessage("Do You Want To Exit App?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    new Constant(AddToCartActivity.this).doFinish();
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
        new WriteLog().writeLog(getApplicationContext(), "AddToCartActivity_" + _data);
    }

    @Override
    public void onItemClick(String size) {
        Constant.showLog(size);
        setColour(selSizeGroup);
    }
}
