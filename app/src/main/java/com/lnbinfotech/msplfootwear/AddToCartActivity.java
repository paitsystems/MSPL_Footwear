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
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.lnbinfotech.msplfootwear.adapters.CustomerOrderUnpackGridAdpater;
import com.lnbinfotech.msplfootwear.adapters.SizeGroupWiseColourAdapter;
import com.lnbinfotech.msplfootwear.adapters.SizeGroupWiseQtyAdapter;
import com.lnbinfotech.msplfootwear.adapters.ViewCustomerOrderAdapter;
import com.lnbinfotech.msplfootwear.constant.Constant;
import com.lnbinfotech.msplfootwear.db.DBHandler;
import com.lnbinfotech.msplfootwear.interfaces.RecyclerViewToActivityInterface;
import com.lnbinfotech.msplfootwear.log.WriteLog;
import com.lnbinfotech.msplfootwear.model.CustomerOrderClass;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class AddToCartActivity extends AppCompatActivity implements View.OnClickListener,RecyclerViewToActivityInterface {

    private Constant constant, constant1;
    private Toast toast;

    private EditText ed_prod_search;
    private RadioButton rdo_pack, rdo_unpack;
    private Spinner sp_sizeGroup;
    private TextView tv_wsp, tv_mrp, tv_hsncode, tv_gstper, tv_add_to_card, tv_checkstock,
                        tv_add_to_card_final, tv_checkout, tv_vieworder;
    private RecyclerView rv_size, rv_color;
    private GridView gridView;
    private LinearLayout lay_pack, lay_unpack;

    private String cat2, cat9;
    private DBHandler db;
    private List<String>  size_list;
    public static List<String> sizeGroup_list;
    public static List<String> colour_list;
    private String selSizeGroup = "";
    public static String selProd = null;
    private int selQtyLocal = 0, compPackQty = 0;
    public static int selProdId = 0, selQty = -1;
    public static HashMap<Integer, Integer> map;
    private List<String> unpackSizeList;
    public static List<Integer> unClickablePositionsList, allSizeChangeList;
    
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
            if(getPackUnPack().equals("M")) {
                setData();
            }else{
                setUnpackData();
            }
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
                if(selProdId!=0) {
                    setData();
                }
                lay_pack.setVisibility(View.VISIBLE);
                lay_unpack.setVisibility(View.GONE);
                gridView.setVisibility(View.GONE);
                break;
            case R.id.rdo_unpack:
                rdo_pack.setChecked(false);
                rdo_unpack.setChecked(true);
                if(selProdId!=0) {
                    setUnpackData();
                }
                lay_pack.setVisibility(View.GONE);
                lay_unpack.setVisibility(View.VISIBLE);
                gridView.setVisibility(View.VISIBLE);
                break;
            case R.id.tv_add_to_card:
                if(getPackUnPack().equals("M")) {
                    if (validateOrder()) {
                        addToCard();
                    } else {
                        toast.setText("Please Select Product");
                        toast.show();
                    }
                }else{
                    String  str1 = "";
                    for (String str : unpackSizeList){
                        str1 = str1 + str +"-";
                    }
                    Constant.showLog(str1);
                    addToCardUnpack();
                }
                break;
            case R.id.tv_check_stock:
                break;
            case R.id.tv_add_to_card_final:
                break;
            case R.id.tv_checkout:
                break;
            case R.id.tv_vieworder:
                Intent i = new Intent(this, ViewCustomerOrderActiviy.class);
                startActivity(i);
                overridePendingTransition(R.anim.enter,R.anim.exit);
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
                tv_mrp.setText(res.getString(res.getColumnIndex(DBHandler.PM_MRPRate)));
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

    private void setUnpackData(){
        unpackSizeList.clear();
        unClickablePositionsList.clear();
        allSizeChangeList.clear();
        sizeGroup_list.clear();
        colour_list.clear();
        gridView.setAdapter(null);

        int unClickCount = -1;

        Cursor res = db.getProductDetails(getPackUnPack());
        if(res.moveToFirst()){
            do{
                tv_wsp.setText(res.getString(res.getColumnIndex(DBHandler.PM_SRate)));
                tv_mrp.setText(res.getString(res.getColumnIndex(DBHandler.PM_MRPRate)));
                tv_hsncode.setText(res.getString(res.getColumnIndex(DBHandler.PM_HSNCode)));
                tv_gstper.setText(res.getString(res.getColumnIndex(DBHandler.PM_GSTGroup)));
            }while (res.moveToNext());
        }
        res.close();

        Cursor res2 = db.getDistinctSizeGroup(getPackUnPack());
        if(res2.moveToFirst()){
            do{
                sizeGroup_list.add(res2.getString(res2.getColumnIndex(DBHandler.ARSD_SizeGroup)));
            }while (res2.moveToNext());
        }else{
            sizeGroup_list.add("NA");
        }
        res2.close();

        Cursor res1 = db.getDistinctColour(sizeGroup_list.get(0));
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

        int gridColumns = colour_list.size()+1;
        gridView.setNumColumns(gridColumns);

        unClickCount++;
        unpackSizeList.add("+2");
        for(int i=0;i<colour_list.size();i++){
            unClickCount++;
            unpackSizeList.add(colour_list.get(i));
        }

        unClickCount++;
        unpackSizeList.add("+1");
        for(int i=0;i<colour_list.size();i++){
            unClickCount++;
            allSizeChangeList.add(unClickCount);
            unpackSizeList.add("0");
        }

        for(int i=0;i<sizeGroup_list.size();i++) {
            unClickCount++;
            unClickablePositionsList.add(unClickCount);
            unpackSizeList.add(sizeGroup_list.get(i));
            for(int j=0;j<colour_list.size();j++){
                unClickCount++;
                unpackSizeList.add("0");
            }
        }
        //gridView.setAdapter(new ArrayAdapter<>(getApplicationContext(),android.R.layout.test_list_item,unpackSizeList));
        gridView.setAdapter(new CustomerOrderUnpackGridAdpater(getApplicationContext(),unpackSizeList));
    }

    private void setSizeData(String sizegroup) {
        size_list.clear();
        colour_list.clear();
        rv_size.setAdapter(null);
        rv_color.setAdapter(null);

        Cursor res1 = db.getDistinctSizes(getPackUnPack(), sizegroup);
        if (res1.moveToFirst()) {
            do {
                String qty = res1.getString(res1.getColumnIndex(DBHandler.ARSD_Total));
                size_list.add(qty);
                compPackQty = Integer.parseInt(qty);
            } while (res1.moveToNext());
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
        map.clear();
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
        if(!map.isEmpty()) {
            Set<Integer> set = map.keySet();
            for (Integer pos : set) {
                int isSelected = map.get(pos);
                if (isSelected == 1) {
                    stat = true;
                    break;
                }else{
                    stat = false;
                }
            }
        }else{
            stat = false;
        }
        return stat;
    }

    private void addToCardUnpack(){
        //db.deleteTable(DBHandler.Table_CustomerOrder);
        int counter = 0, isDataInserted = 0;
        HashMap<String,List<String>> output = new HashMap<>();
        for(int i=0; i<unpackSizeList.size();i++){
            int getColour = -1;
            List<String> list = new ArrayList<>();
            for(int j=0;j<=colour_list.size();j++) {
                if (j == 0) {
                    output.put(unpackSizeList.get(counter), list);
                }else {
                    getColour++;
                    String colour = colour_list.get(getColour);
                    String qty = unpackSizeList.get(counter);
                    if (isDataInserted == 0) {
                        if (!qty.equals("0")) {
                            isDataInserted = 1;
                        }
                    }
                    list.add(qty + "^" + colour);
                }
                counter++;
            }
            i = counter;
        }

        if(isDataInserted==0){
            toast.setText("Please Enter Value");
            toast.show();
        }else{
            Constant.showLog(output.size()+"");
        }

        if(!output.isEmpty()) {
            float rate = Float.parseFloat(tv_wsp.getText().toString());
            float mrp = Float.parseFloat(tv_mrp.getText().toString());
            String gstgroupName = tv_gstper.getText().toString();
            double gstPer = 0, cgstPer = 0, sgstPer = 0, cessPer = 0, cgstShare = 0, sgstShare = 0;
            double amount = 0, gstAmt = 0, cgstAmt = 0, sgstAmt = 0, cessAmt = 0, igstAmt = 0, totalAmt;
            String gstStatus = "";
            Cursor res = db.getGSTDetails(gstgroupName);
            if (res.moveToFirst()) {
                gstStatus = res.getString(res.getColumnIndex(DBHandler.GST_Status));
                gstPer = res.getDouble(res.getColumnIndex(DBHandler.GST_GSTPer));
                cgstPer = res.getDouble(res.getColumnIndex(DBHandler.GST_CGSTPer));
                sgstPer = res.getDouble(res.getColumnIndex(DBHandler.GST_SGSTPer));
                cessPer = res.getDouble(res.getColumnIndex(DBHandler.GST_CESSPer));
                cgstShare = res.getDouble(res.getColumnIndex(DBHandler.GST_CGSTSHARE));
                sgstShare = res.getDouble(res.getColumnIndex(DBHandler.GST_SGSTSHARE));
            }
            res.close();

            if (gstStatus.equalsIgnoreCase("A")) {

                int hocode = FirstActivity.pref.getInt(getString(R.string.pref_hocode), 0);
                String prodCol = "";
                if (hocode == 1) {
                    prodCol = DBHandler.PM_HKHO;
                } else if (hocode == 12) {
                    prodCol = DBHandler.PM_HKRD;
                } else if (hocode == 13) {
                    prodCol = DBHandler.PM_HANR;
                }
                int branchId = db.getDispatchCenter(prodCol);

                Set<String> set = output.keySet();
                for (String sizeGroup : set) {
                    if (!sizeGroup.equals("+2") && !sizeGroup.equals("+1")) {
                        List<String> list = output.get(sizeGroup);
                        for (int i = 0; i < list.size(); i++) {
                            String qtyColourStr = list.get(i);
                            String qtyColour[] = qtyColourStr.split("\\^");

                            int looseQty = Integer.parseInt(qtyColour[0]);
                            String colourStr = qtyColour[1];
                            String colourhashCode[] = colourStr.split("\\-");
                            String colour = colourhashCode[0];
                            String hashCode = colourhashCode[1];
                            if (looseQty != 0) {

                                amount = 0;gstAmt = 0; totalAmt = 0;
                                cgstAmt = 0; sgstAmt = 0; cessAmt = 0;

                                amount = mrp * looseQty;
                                gstAmt = (amount * gstPer) / 100;
                                totalAmt = amount + gstAmt;

                                cgstAmt = (amount * cgstPer) / 100;
                                sgstAmt = (amount * sgstPer) / 100;
                                cessAmt = (amount * cessPer) / 100;

                                CustomerOrderClass custOrder = new CustomerOrderClass();
                                int auto = db.getCustOrderMax();
                                custOrder.setAuto(auto);
                                custOrder.setBranchId(branchId);
                                custOrder.setProductid(selProdId);
                                custOrder.setSizeGroup(sizeGroup);
                                custOrder.setRequiredSize(sizeGroup);
                                custOrder.setPerPackQty(1);
                                custOrder.setColor(colour);
                                custOrder.setHashCode(hashCode);
                                custOrder.setRate(rate);
                                custOrder.setMrp(mrp);
                                custOrder.setQty(looseQty);
                                custOrder.setLooseQty(looseQty);
                                custOrder.setActLooseQty(looseQty);
                                custOrder.setAmount(amount);
                                custOrder.setLoosePackTyp("Unpack");
                                custOrder.setTotalamt(totalAmt);
                                custOrder.setGstper(gstPer);
                                custOrder.setCgstamt(cgstAmt);
                                custOrder.setSgstamt(sgstAmt);
                                custOrder.setIgstamt(igstAmt);
                                custOrder.setCgstper(cgstPer);
                                custOrder.setSgstper(sgstPer);
                                custOrder.setCessper(cessPer);
                                custOrder.setCessamt(cessAmt);

                                db.addCustomerOrder(custOrder);

                                Constant.showLog("HOCODE-" + hocode + "-ProdCol-" + prodCol + "-Auto-" + auto + "-BranchId-" + branchId +
                                        "-SelProdId-" + selProdId + "-SizeGroup-" + sizeGroup + "-SelColor-" + colour +
                                        "-RequiredSize-" + sizeGroup + "-PerPackQty-1" +
                                        "-SelQty-" + looseQty + "-MRP-" + mrp + "-Amount-" + amount + "-TotalAmt-" + totalAmt + "-GSTPer-" + gstPer +
                                        "-CGSTAMt-" + cgstAmt + "-SGSTAMt-" + sgstAmt + "-IGSTAMt-" + igstAmt + "-CGSTPer-" + cgstPer + "-SGSTPer-" + sgstPer +
                                        "-CESSPer-" + cessPer + "-CESSAmt-" + cessAmt);
                            }
                        }
                    }

                }
            }
        }
    }

    private void addToCard(){
        float rate = Float.parseFloat(tv_wsp.getText().toString());
        float mrp = Float.parseFloat(tv_mrp.getText().toString());
        String gstgroupName = tv_gstper.getText().toString();
        double gstPer = 0, cgstPer= 0, sgstPer=0, cessPer=0, cgstShare = 0, sgstShare = 0;
        double amount = 0, gstAmt = 0, cgstAmt = 0, sgstAmt = 0, cessAmt = 0, igstAmt = 0, totalAmt;
        String gstStatus = "";
        Cursor res = db.getGSTDetails(gstgroupName);
        if(res.moveToFirst()){
            gstStatus = res.getString(res.getColumnIndex(DBHandler.GST_Status));
            gstPer = res.getDouble(res.getColumnIndex(DBHandler.GST_GSTPer));
            cgstPer = res.getDouble(res.getColumnIndex(DBHandler.GST_CGSTPer));
            sgstPer = res.getDouble(res.getColumnIndex(DBHandler.GST_SGSTPer));
            cessPer = res.getDouble(res.getColumnIndex(DBHandler.GST_CESSPer));
            cgstShare = res.getDouble(res.getColumnIndex(DBHandler.GST_CGSTSHARE));
            sgstShare = res.getDouble(res.getColumnIndex(DBHandler.GST_SGSTSHARE));
        }
        res.close();

        if(gstStatus.equalsIgnoreCase("A")) {

            int looseQty = selQtyLocal;
            amount = mrp * looseQty;
            gstAmt = (amount*gstPer)/100;
            totalAmt = amount+gstAmt;

            cgstAmt = (amount*cgstPer)/100;
            sgstAmt = (amount*sgstPer)/100;
            cessAmt = (amount*cessPer)/100;

            int hocode = FirstActivity.pref.getInt(getString(R.string.pref_hocode), 0);
            String prodCol = "";
            if (hocode == 1) {
                prodCol = DBHandler.PM_HKHO;
            } else if (hocode == 12) {
                prodCol = DBHandler.PM_HKRD;
            } else if (hocode == 13) {
                prodCol = DBHandler.PM_HANR;
            }
            int branchId = db.getDispatchCenter(prodCol);
            String sizeGroup = sizeGroup_list.get(sp_sizeGroup.getSelectedItemPosition());

            Set<Integer> set = map.keySet();
            for (Integer pos : set) {
                int isSelected = map.get(pos);
                if (isSelected == 1) {
                    String colo = colour_list.get(pos);
                    String[] colour = colo.split("\\-");
                    String requiredSize, total;
                    String requiredSizeNTotal = db.getRequiredSize(sizeGroup, colour[0]);
                    if (!requiredSizeNTotal.equals("")) {
                        String[] arr = requiredSizeNTotal.split("\\^");
                        if(arr.length>1) {
                            requiredSize = arr[0];
                            total = arr[1];

                            CustomerOrderClass custOrder = new CustomerOrderClass();
                            int auto = db.getCustOrderMax();
                            custOrder.setAuto(auto);
                            custOrder.setBranchId(branchId);
                            custOrder.setProductid(selProdId);
                            custOrder.setSizeGroup(sizeGroup);
                            custOrder.setRequiredSize(requiredSize);
                            custOrder.setPerPackQty(Integer.parseInt(total));
                            custOrder.setColor(colour[0]);
                            custOrder.setHashCode(colour[1]);
                            custOrder.setRate(rate);
                            custOrder.setMrp(mrp);
                            custOrder.setQty(1);
                            custOrder.setLooseQty(selQtyLocal);
                            custOrder.setActLooseQty(selQtyLocal);
                            custOrder.setAmount(amount);
                            custOrder.setLoosePackTyp("Unpack");
                            custOrder.setTotalamt(totalAmt);
                            custOrder.setGstper(gstPer);
                            custOrder.setCgstamt(cgstAmt);
                            custOrder.setSgstamt(sgstAmt);
                            custOrder.setIgstamt(igstAmt);
                            custOrder.setCgstper(cgstPer);
                            custOrder.setSgstper(sgstPer);
                            custOrder.setCessper(cessPer);
                            custOrder.setCessamt(cessAmt);

                            db.addCustomerOrder(custOrder);

                            Constant.showLog("HOCODE-" + hocode + "-ProdCol-" + prodCol+"-Auto-"+auto+ "-BranchId-" + branchId +
                                    "-SelProdId-" + selProdId + "-SizeGroup-" + sizeGroup + "-SelColor-" + colour[0] +
                                    "-RequiredSize-" + requiredSize + "-PerPackQty-" + total +
                                    "-SelQty-" + selQtyLocal + "-MRP-" + mrp + "-Amount-" + amount + "-TotalAmt-" + totalAmt + "-GSTPer-" + gstPer +
                                    "-CGSTAMt-" + cgstAmt + "-SGSTAMt-" + sgstAmt + "-IGSTAMt-" + igstAmt + "-CGSTPer-" + cgstPer + "-SGSTPer-" + sgstPer +
                                    "-CESSPer-" + cessPer + "-CESSAmt-" + cessAmt);
                        }else {
                            toast.setText("Something Went Wrong");
                            toast.show();
                        }
                    }
                }
            }
            toast.setText("Added To Card");
            toast.show();
        }else{
            toast.setText("GST Status Cancelled");
            toast.show();
        }
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

        gridView = (GridView) findViewById(R.id.gridView);

        lay_pack = (LinearLayout) findViewById(R.id.lay_pack);
        lay_unpack = (LinearLayout) findViewById(R.id.lay_unpack);

        sizeGroup_list = new ArrayList<>();
        size_list = new ArrayList<>();
        colour_list = new ArrayList<>();
        db = new DBHandler(getApplicationContext());
        map = new HashMap<>();
        unpackSizeList = new ArrayList<>();
        unClickablePositionsList = new ArrayList<>();
        allSizeChangeList = new ArrayList<>();
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

    private void showCompPackDia(int a) {
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
        if(!size.equalsIgnoreCase("NA")) {
            selQtyLocal = Integer.parseInt(size);
            if(selQtyLocal != compPackQty) {
                setColour(selSizeGroup);
            }else{
                rv_color.setAdapter(null);
                toast.setText("Dialog Logic");
                toast.show();
            }
        }else{
            toast.setText("No Colour Available");
            toast.show();
        }
    }
}
