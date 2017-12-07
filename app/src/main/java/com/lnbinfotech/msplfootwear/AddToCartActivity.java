package com.lnbinfotech.msplfootwear;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.lnbinfotech.msplfootwear.adapters.CustomerOrderUnpackGridAdpater;
import com.lnbinfotech.msplfootwear.adapters.SizeGroupWiseColourAdapter;
import com.lnbinfotech.msplfootwear.adapters.SizeGroupWiseQtyAdapter;
import com.lnbinfotech.msplfootwear.constant.Constant;
import com.lnbinfotech.msplfootwear.db.DBHandler;
import com.lnbinfotech.msplfootwear.interfaces.RecyclerViewToActivityInterface;
import com.lnbinfotech.msplfootwear.log.WriteLog;
import com.lnbinfotech.msplfootwear.model.CustomerOrderClass;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class AddToCartActivity extends AppCompatActivity implements View.OnClickListener, RecyclerViewToActivityInterface {

    private Constant constant, constant1;
    private Toast toast;

    private EditText ed_prod_search;
    private RadioButton rdo_pack, rdo_unpack;
    private Spinner sp_sizeGroup;
    private TextView tv_wsp, tv_mrp, tv_hsncode, tv_gstper, tv_add_to_card, tv_checkstock, tv_remove_item, tv_cancel_item,
            tv_add_to_card_final, tv_checkout, tv_vieworder, tv_totqty, tv_totamnt, tv_totset, tv_totnetamt;
    private RecyclerView rv_size, rv_color;
    private GridView gridView;
    private LinearLayout lay_pack, lay_comp_pack;
    private AutoCompleteTextView auto_set;
    private ListView listView;

    private String cat2, cat9;
    private DBHandler db;
    private List<String> size_list;
    public static List<String> sizeGroup_list;
    public static List<String> colour_list;
    private List<String> unpackSizeList;
    public static List<Integer> unClickablePositionsList, allSizeChangeList;
    public static HashMap<Integer, Integer> map;
    private String selSizeGroup = "";
    public static String selProd = null;
    private int selQtyLocal = 0, compPackQty = 0;
    public static int selProdId = 0, selQty = -1, activityToFrom = 0;
    public static CustomerOrderClass updateCustOrder;

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
        tv_remove_item.setOnClickListener(this);
        tv_cancel_item.setOnClickListener(this);
        tv_add_to_card_final.setOnClickListener(this);
        tv_checkout.setOnClickListener(this);
        tv_vieworder.setOnClickListener(this);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        selProdId = 0;
        selQty = -1;
        activityToFrom = 0;

        sp_sizeGroup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selSizeGroup = (String) adapterView.getItemAtPosition(i);
                Constant.showLog(selSizeGroup);
                lay_comp_pack.setVisibility(View.GONE);
                if (activityToFrom != 2) {
                    setSizeData(selSizeGroup);
                } else {
                    setUpdateSizeData(selSizeGroup);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                lay_comp_pack.setVisibility(View.GONE);
            }
        });

        auto_set.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                final int action = motionEvent.getAction();
                if (action == MotionEvent.ACTION_DOWN) {
                    auto_set.showDropDown();
                    auto_set.setThreshold(0);
                }
                return false;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        totalCalculations();
        if (activityToFrom == 0) {
            rdo_pack.setClickable(true);
            rdo_unpack.setClickable(true);
            tv_remove_item.setVisibility(View.GONE);
            tv_cancel_item.setVisibility(View.GONE);
            sp_sizeGroup.setEnabled(true);
            tv_add_to_card.setText("Add-To-Card");
            ed_prod_search.setText(null);
            rdo_pack.setChecked(true);
            rdo_unpack.setChecked(false);
            lay_pack.setVisibility(View.VISIBLE);
            lay_comp_pack.setVisibility(View.GONE);
            gridView.setVisibility(View.GONE);
            tv_wsp.setText("0");
            tv_mrp.setText("0");
            tv_gstper.setText("0");
            tv_hsncode.setText("0");
            sp_sizeGroup.setAdapter(null);
            rv_color.setAdapter(null);
            rv_size.setAdapter(null);
            gridView.setAdapter(null);
            selProdId = 0;
        } else if (activityToFrom == 1) {
            rdo_pack.setClickable(true);
            rdo_unpack.setClickable(true);
            tv_remove_item.setVisibility(View.GONE);
            tv_cancel_item.setVisibility(View.GONE);
            sp_sizeGroup.setEnabled(true);
            tv_add_to_card.setText("Add-To-Card");
            if (selProd != null) {
                ed_prod_search.setText(selProd);
                if (getPackUnPack().equals("M")) {
                    setData();
                } else {
                    setUnpackData();
                }
            }
        } else if (activityToFrom == 2) {
            rdo_pack.setClickable(false);
            rdo_unpack.setClickable(false);
            tv_remove_item.setVisibility(View.VISIBLE);
            tv_cancel_item.setVisibility(View.VISIBLE);
            tv_add_to_card.setText("Update");
            updateOrder();
        }
        constant = new Constant(AddToCartActivity.this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ed_prod_search:
                if (activityToFrom != 2) {
                    Intent intent = new Intent(getApplicationContext(), ProductSearchActivity.class);
                    intent.putExtra("cat9", cat9);
                    intent.putExtra("cat2", cat2);
                    startActivity(intent);
                    overridePendingTransition(R.anim.enter, R.anim.exit);
                }
                break;
            case R.id.rdo_pack:
                rdo_pack.setChecked(true);
                rdo_unpack.setChecked(false);
                if (selProdId != 0) {
                    setData();
                }
                lay_pack.setVisibility(View.VISIBLE);
                lay_comp_pack.setVisibility(View.GONE);
                gridView.setVisibility(View.GONE);
                break;
            case R.id.rdo_unpack:
                rdo_pack.setChecked(false);
                rdo_unpack.setChecked(true);
                if (selProdId != 0) {
                    setUnpackData();
                }
                lay_pack.setVisibility(View.GONE);
                lay_comp_pack.setVisibility(View.GONE);
                gridView.setVisibility(View.VISIBLE);
                break;
            case R.id.tv_add_to_card:
                if (activityToFrom != 0) {
                    if (activityToFrom == 1) {
                        if (getPackUnPack().equals("M")) {
                            if (validateOrder()) {
                                if (selQtyLocal != compPackQty) {
                                    InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                    mgr.hideSoftInputFromWindow(tv_add_to_card.getWindowToken(), 0);
                                    addToCard();
                                } else {
                                    String str = auto_set.getText().toString();
                                    if (!str.equals("")) {
                                        selQtyLocal = Integer.parseInt(auto_set.getText().toString());
                                        if (selQtyLocal != 0) {
                                            InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                            mgr.hideSoftInputFromWindow(tv_add_to_card.getWindowToken(), 0);
                                            addToCardCompPack();
                                        } else {
                                            showToast("Please Enter Non Zero Value");
                                        }
                                    } else {
                                        showToast("Please Enter Proper Value");
                                    }
                                }
                            } else {
                                showToast("Please Select Product");
                            }
                        } else {
                            String str1 = "";
                            for (String str : unpackSizeList) {
                                str1 = str1 + str + "-";
                            }
                            Constant.showLog(str1);
                            InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            mgr.hideSoftInputFromWindow(tv_add_to_card.getWindowToken(), 0);
                            addToCardUnpack();
                        }
                    } else if (activityToFrom == 2) {
                        if (updateCustOrder != null) {
                            if (updateCustOrder.getOrderType().equals("P") || updateCustOrder.getOrderType().equals("C")) {
                                if (validateOrder()) {
                                    if (updateCustOrder.getOrderType().equals("P")) {
                                        InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                        mgr.hideSoftInputFromWindow(tv_add_to_card.getWindowToken(), 0);
                                        addToCardUpdate();
                                    } else if (updateCustOrder.getOrderType().equals("C")) {
                                        String str = auto_set.getText().toString();
                                        if (!str.equals("")) {
                                            selQtyLocal = Integer.parseInt(auto_set.getText().toString());
                                            if (selQtyLocal != 0) {
                                                InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                                mgr.hideSoftInputFromWindow(auto_set.getWindowToken(), 0);
                                                if (lay_comp_pack.getVisibility() == View.VISIBLE) {
                                                    addToCardCompPackUpdate();
                                                } else {
                                                    showToast("Can Not Update");
                                                }
                                            } else {
                                                showToast("Please Enter Non Zero Value");
                                            }
                                        } else {
                                            showToast("Please Enter Proper Value");
                                        }
                                    }
                                } else {
                                    showToast("Please Select Product");
                                }
                            } else if (updateCustOrder.getOrderType().equals("U")) {
                                InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                mgr.hideSoftInputFromWindow(tv_add_to_card.getWindowToken(), 0);
                                addToCardUnpackUpdate();
                            }
                        }
                    }
                } else {
                    showToast("Please Select Product");
                }
                break;
            case R.id.tv_remove_item:
                showDia(1);
                break;
            case R.id.tv_cancel_item:
                showDia(2);
                break;
            case R.id.tv_check_stock:
                db.deleteTable(DBHandler.Table_CustomerOrder);
                break;
            case R.id.tv_add_to_card_final:
                break;
            case R.id.tv_checkout:
                startActivity(new Intent(this, ViewCustomerOrderActiviy.class));
                overridePendingTransition(R.anim.enter, R.anim.exit);
                break;
            case R.id.tv_vieworder:
                Intent i = new Intent(this, ViewCustomerOrderActiviy.class);
                startActivity(i);
                overridePendingTransition(R.anim.enter, R.anim.exit);
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

    private String getPackUnPack() {
        String packUnpackType;
        if (rdo_pack.isChecked()) {
            packUnpackType = "M";
        } else {
            packUnpackType = "D";
        }
        return packUnpackType;
    }

    private void setData() {
        Cursor res = db.getProductDetails(getPackUnPack());
        if (res.moveToFirst()) {
            do {
                tv_wsp.setText(res.getString(res.getColumnIndex(DBHandler.PM_SRate)));
                tv_mrp.setText(res.getString(res.getColumnIndex(DBHandler.PM_MRPRate)));
                tv_hsncode.setText(res.getString(res.getColumnIndex(DBHandler.PM_HSNCode)));
                tv_gstper.setText(res.getString(res.getColumnIndex(DBHandler.PM_GSTGroup)));
            } while (res.moveToNext());
        }
        res.close();

        sizeGroup_list.clear();
        sizeGroup_list.add("Select SizeGroup");
        Cursor res1 = db.getDistinctSizeGroup(getPackUnPack());
        if (res1.moveToFirst()) {
            do {
                sizeGroup_list.add(res1.getString(res1.getColumnIndex(DBHandler.ARSD_SizeGroup)));
            } while (res1.moveToNext());
        } else {
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
        if (size_list.size() != 0) {
            SizeGroupWiseQtyAdapter adapter = new SizeGroupWiseQtyAdapter(size_list, getApplicationContext(), "A", 0);
            adapter.setOnClickListener1(this);
            rv_size.setAdapter(adapter);
        } else {
            showToast("No Qty Avalilable");
        }
    }

    private void setColour(String size) {
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
        if (colour_list.size() != 0) {
            SizeGroupWiseColourAdapter adapter = new SizeGroupWiseColourAdapter(colour_list, getApplicationContext());
            rv_color.setAdapter(adapter);
        } else {
            toast.setText("No Colour Available");
        }
    }

    private void setUnpackData() {
        unpackSizeList.clear();
        unClickablePositionsList.clear();
        allSizeChangeList.clear();
        sizeGroup_list.clear();
        colour_list.clear();
        gridView.setAdapter(null);

        int unClickCount = -1;

        Cursor res = db.getProductDetails(getPackUnPack());
        if (res.moveToFirst()) {
            do {
                tv_wsp.setText(res.getString(res.getColumnIndex(DBHandler.PM_SRate)));
                tv_mrp.setText(res.getString(res.getColumnIndex(DBHandler.PM_MRPRate)));
                tv_hsncode.setText(res.getString(res.getColumnIndex(DBHandler.PM_HSNCode)));
                tv_gstper.setText(res.getString(res.getColumnIndex(DBHandler.PM_GSTGroup)));
            } while (res.moveToNext());
        }
        res.close();

        Cursor res2 = db.getDistinctSizeGroup(getPackUnPack());
        if (res2.moveToFirst()) {
            do {
                sizeGroup_list.add(res2.getString(res2.getColumnIndex(DBHandler.ARSD_SizeGroup)));
            } while (res2.moveToNext());
        } else {
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

        int gridColumns = colour_list.size() + 1;
        gridView.setNumColumns(gridColumns);

        unClickCount++;
        unpackSizeList.add("+2");
        for (int i = 0; i < colour_list.size(); i++) {
            unClickCount++;
            unpackSizeList.add(colour_list.get(i));
        }

        unClickCount++;
        unpackSizeList.add("+1");
        for (int i = 0; i < colour_list.size(); i++) {
            unClickCount++;
            allSizeChangeList.add(unClickCount);
            unpackSizeList.add("0");
        }

        for (int i = 0; i < sizeGroup_list.size(); i++) {
            unClickCount++;
            unClickablePositionsList.add(unClickCount);
            unpackSizeList.add(sizeGroup_list.get(i));
            for (int j = 0; j < colour_list.size(); j++) {
                unClickCount++;
                unpackSizeList.add("0");
            }
        }
        //gridView.setAdapter(new ArrayAdapter<>(getApplicationContext(),android.R.layout.test_list_item,unpackSizeList));
        gridView.setAdapter(new CustomerOrderUnpackGridAdpater(getApplicationContext(), unpackSizeList));
    }

    private boolean validateOrder() {
        boolean stat = false;
        stat = !ed_prod_search.getText().toString().equals("");
        stat = (sp_sizeGroup.getSelectedItemPosition() != 0) && (sp_sizeGroup.getSelectedItemPosition() != -1);
        stat = selQty != -1;
        if (!map.isEmpty()) {
            Set<Integer> set = map.keySet();
            for (Integer pos : set) {
                int isSelected = map.get(pos);
                if (isSelected == 1) {
                    stat = true;
                    break;
                } else {
                    stat = false;
                }
            }
        } else {
            stat = false;
        }
        return stat;
    }

    private void addToCardUnpack() {
        int counter = 0, isDataInserted = 0;
        HashMap<String, List<String>> output = new HashMap<>();
        for (int i = 0; i < unpackSizeList.size(); i++) {
            int getColour = -1;
            List<String> list = new ArrayList<>();
            for (int j = 0; j <= colour_list.size(); j++) {
                if (j == 0) {
                    output.put(unpackSizeList.get(counter), list);
                } else {
                    getColour++;
                    String colour = colour_list.get(getColour);
                    String qty = unpackSizeList.get(counter);
                    if (qty.equals("")) {
                        qty = "0";
                    }
                    list.add(qty + "^" + colour);
                }
                counter++;
            }
            i = counter;
        }

        if (!output.isEmpty()) {
            String rate = tv_wsp.getText().toString();
            String mrp = tv_mrp.getText().toString();
            float floatRate = Float.parseFloat(rate);

            String gstgroupName = tv_gstper.getText().toString();
            float gstPer = 0, cgstPer = 0, sgstPer = 0, cessPer = 0, cgstShare = 0, sgstShare = 0;
            float amount = 0, amountAfterDisc = 0, _discAmnt = 0, gstAmt = 0, cgstAmt = 0, sgstAmt = 0, cessAmt = 0, igstAmt = 0, totalAmt;
            String gstStatus = "";
            Cursor res = db.getGSTDetails(gstgroupName);
            if (res.moveToFirst()) {
                gstStatus = res.getString(res.getColumnIndex(DBHandler.GST_Status));
                gstPer = res.getFloat(res.getColumnIndex(DBHandler.GST_GSTPer));
                cgstPer = res.getFloat(res.getColumnIndex(DBHandler.GST_CGSTPer));
                sgstPer = res.getFloat(res.getColumnIndex(DBHandler.GST_SGSTPer));
                cessPer = res.getFloat(res.getColumnIndex(DBHandler.GST_CESSPer));
                cgstShare = res.getFloat(res.getColumnIndex(DBHandler.GST_CGSTSHARE));
                sgstShare = res.getFloat(res.getColumnIndex(DBHandler.GST_SGSTSHARE));
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
                                if (isDataInserted == 0) {
                                    isDataInserted = 1;
                                }
                                /*amount = 0;
                                gstAmt = 0;
                                totalAmt = 0;
                                cgstAmt = 0;
                                sgstAmt = 0;
                                cessAmt = 0;*/

                                amount = floatRate * looseQty;

                                if (OptionsActivity.custDisc != 0) {
                                    _discAmnt = (amount * OptionsActivity.custDisc) / 100;
                                    _discAmnt = round(_discAmnt, 2);
                                    amountAfterDisc = amount - _discAmnt;
                                } else {
                                    amountAfterDisc = amount;
                                }

                                amount = round(amount, 2);
                                amountAfterDisc = round(amountAfterDisc, 2);

                                gstAmt = (amountAfterDisc * gstPer) / 100;
                                gstAmt = round(gstAmt, 2);
                                totalAmt = amountAfterDisc + gstAmt;
                                totalAmt = round(totalAmt, 2);

                                cgstAmt = (amountAfterDisc * cgstPer) / 100;
                                cgstAmt = round(cgstAmt, 2);
                                sgstAmt = (amountAfterDisc * sgstPer) / 100;
                                sgstAmt = round(sgstAmt, 2);
                                cessAmt = (amountAfterDisc * cessPer) / 100;
                                cessAmt = round(cessAmt, 2);

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
                                custOrder.setAmount(String.valueOf(amount));
                                custOrder.setLoosePackTyp("Unpack");
                                custOrder.setPendingLooseQty(looseQty);
                                custOrder.setTotalamt(String.valueOf(totalAmt));
                                custOrder.setNetamnt(String.valueOf(totalAmt));
                                custOrder.setAmtAfterDisc(String.valueOf(amountAfterDisc));
                                custOrder.setGstper(String.valueOf(gstPer));
                                custOrder.setGstAmt(String.valueOf(gstAmt));
                                custOrder.setCgstamt(String.valueOf(cgstAmt));
                                custOrder.setSgstamt(String.valueOf(sgstAmt));
                                custOrder.setIgstamt(String.valueOf(igstAmt));
                                custOrder.setCgstper(String.valueOf(cgstPer));
                                custOrder.setSgstper(String.valueOf(sgstPer));
                                custOrder.setCessper(String.valueOf(cessPer));
                                custOrder.setCessamt(String.valueOf(cessAmt));
                                custOrder.setDiscamnt(String.valueOf(_discAmnt));
                                custOrder.setDiscPer(String.valueOf(OptionsActivity.custDisc));
                                custOrder.setOrderType("U");
                                custOrder.setAvailQty(0);
                                db.addCustomerOrder(custOrder);

                                Constant.showLog("HOCODE-" + hocode + "-ProdCol-" + prodCol + "-Auto-" + auto + "-BranchId-" + branchId +
                                        "-SelProdId-" + selProdId + "-SizeGroup-" + sizeGroup + "-SelColor-" + colour +
                                        "-RequiredSize-" + sizeGroup + "-PerPackQty-1" +
                                        "-SelQty-" + looseQty + "-MRP-" + mrp + "-Amount-" + amount + "-TotalAmt-" + totalAmt + "-GSTPer-" + gstPer +
                                        "-CGSTAMt-" + cgstAmt + "-SGSTAMt-" + sgstAmt + "-IGSTAMt-" + igstAmt + "-CGSTPer-" + cgstPer + "-SGSTPer-" + sgstPer +
                                        "-CESSPer-" + cessPer + "-CESSAmt-" + cessAmt + "-DiscPer-" + OptionsActivity.custDisc + "-DiscAmnt-" + _discAmnt);
                            }
                        }
                    }

                }
                if (isDataInserted == 1) {
                    showToast("Added-To-Card");
                    clearFiledsUnPack();
                } else {
                    showToast("Please Enter Qty");
                }

            } else {
                showToast("Please Refresh GSTMaster");
            }
        }
        totalCalculations();
    }

    private void addToCard() {
        String rate = tv_wsp.getText().toString();
        String mrp = tv_mrp.getText().toString();
        float floatRate = Float.parseFloat(rate);

        String gstgroupName = tv_gstper.getText().toString();
        float gstPer = 0, cgstPer = 0, sgstPer = 0, cessPer = 0, cgstShare = 0, sgstShare = 0;
        float amount = 0, amountAfterDisc = 0, _discAmnt = 0, gstAmt = 0, cgstAmt = 0, sgstAmt = 0, cessAmt = 0, igstAmt = 0, totalAmt;
        String gstStatus = "";
        Cursor res = db.getGSTDetails(gstgroupName);
        if (res.moveToFirst()) {
            gstStatus = res.getString(res.getColumnIndex(DBHandler.GST_Status));
            gstPer = res.getFloat(res.getColumnIndex(DBHandler.GST_GSTPer));
            cgstPer = res.getFloat(res.getColumnIndex(DBHandler.GST_CGSTPer));
            sgstPer = res.getFloat(res.getColumnIndex(DBHandler.GST_SGSTPer));
            cessPer = res.getFloat(res.getColumnIndex(DBHandler.GST_CESSPer));
            cgstShare = res.getFloat(res.getColumnIndex(DBHandler.GST_CGSTSHARE));
            sgstShare = res.getFloat(res.getColumnIndex(DBHandler.GST_SGSTSHARE));
        }
        res.close();

        if (gstStatus.equalsIgnoreCase("A")) {
            int looseQty = selQtyLocal;

            amount = floatRate * looseQty;

            if (OptionsActivity.custDisc != 0) {
                _discAmnt = (amount * OptionsActivity.custDisc) / 100;
                _discAmnt = round(_discAmnt, 2);
                amountAfterDisc = amount - _discAmnt;
            } else {
                amountAfterDisc = amount;
            }

            amount = round(amount, 2);
            amountAfterDisc = round(amountAfterDisc, 2);

            gstAmt = (amountAfterDisc * gstPer) / 100;
            gstAmt = round(gstAmt, 2);
            totalAmt = amountAfterDisc + gstAmt;
            totalAmt = round(totalAmt, 2);

            cgstAmt = (amountAfterDisc * cgstPer) / 100;
            cgstAmt = round(cgstAmt, 2);
            sgstAmt = (amountAfterDisc * sgstPer) / 100;
            sgstAmt = round(sgstAmt, 2);
            cessAmt = (amountAfterDisc * cessPer) / 100;
            cessAmt = round(cessAmt, 2);

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
                        if (arr.length > 1) {
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
                            custOrder.setAmount(String.valueOf(amount));
                            custOrder.setLoosePackTyp("Unpack");
                            custOrder.setPendingLooseQty(selQtyLocal);
                            custOrder.setTotalamt(String.valueOf(totalAmt));
                            custOrder.setNetamnt(String.valueOf(totalAmt));
                            custOrder.setAmtAfterDisc(String.valueOf(amountAfterDisc));
                            custOrder.setGstAmt(String.valueOf(gstAmt));
                            custOrder.setGstper(String.valueOf(gstPer));
                            custOrder.setCgstamt(String.valueOf(cgstAmt));
                            custOrder.setSgstamt(String.valueOf(sgstAmt));
                            custOrder.setIgstamt(String.valueOf(igstAmt));
                            custOrder.setCgstper(String.valueOf(cgstPer));
                            custOrder.setSgstper(String.valueOf(sgstPer));
                            custOrder.setCessper(String.valueOf(cessPer));
                            custOrder.setCessamt(String.valueOf(cessAmt));
                            custOrder.setDiscamnt(String.valueOf(_discAmnt));
                            custOrder.setDiscPer(String.valueOf(OptionsActivity.custDisc));
                            custOrder.setOrderType("P");
                            custOrder.setAvailQty(0);
                            db.addCustomerOrder(custOrder);
                            Constant.showLog("HOCODE-" + hocode + "-ProdCol-" + prodCol + "-Auto-" + auto + "-BranchId-" + branchId +
                                    "-SelProdId-" + selProdId + "-SizeGroup-" + sizeGroup + "-SelColor-" + colour[0] +
                                    "-RequiredSize-" + requiredSize + "-PerPackQty-" + total +
                                    "-SelQty-" + selQtyLocal + "-MRP-" + mrp + "-Amount-" + amount + "-TotalAmt-" + totalAmt + "-GSTPer-" + gstPer +
                                    "-CGSTAMt-" + cgstAmt + "-SGSTAMt-" + sgstAmt + "-IGSTAMt-" + igstAmt + "-CGSTPer-" + cgstPer + "-SGSTPer-" + sgstPer +
                                    "-CESSPer-" + cessPer + "-CESSAmt-" + cessAmt + "-DiscPer-" + OptionsActivity.custDisc + "-DiscAmnt-" + _discAmnt);
                        } else {
                            showToast("Something Went Wrong");
                        }
                    }
                }
            }
            showToast("Added-To-Card");
            clearFiledsPack();
        } else {
            showToast("Please Refresh GSTMaster");
        }
        totalCalculations();
    }

    private void addToCardCompPack() {
        String rate = tv_wsp.getText().toString();
        String mrp = tv_mrp.getText().toString();
        float floatRate = Float.parseFloat(rate);

        String gstgroupName = tv_gstper.getText().toString();
        float gstPer = 0, cgstPer = 0, sgstPer = 0, cessPer = 0, cgstShare = 0, sgstShare = 0;
        float amount = 0, amountAfterDisc = 0, _discAmnt = 0, gstAmt = 0, cgstAmt = 0, sgstAmt = 0, cessAmt = 0, igstAmt = 0, totalAmt;
        String gstStatus = "";
        Cursor res = db.getGSTDetails(gstgroupName);
        if (res.moveToFirst()) {
            gstStatus = res.getString(res.getColumnIndex(DBHandler.GST_Status));
            gstPer = res.getFloat(res.getColumnIndex(DBHandler.GST_GSTPer));
            cgstPer = res.getFloat(res.getColumnIndex(DBHandler.GST_CGSTPer));
            sgstPer = res.getFloat(res.getColumnIndex(DBHandler.GST_SGSTPer));
            cessPer = res.getFloat(res.getColumnIndex(DBHandler.GST_CESSPer));
            cgstShare = res.getFloat(res.getColumnIndex(DBHandler.GST_CGSTSHARE));
            sgstShare = res.getFloat(res.getColumnIndex(DBHandler.GST_SGSTSHARE));
        }
        res.close();

        if (gstStatus.equalsIgnoreCase("A")) {
            int looseQty = selQtyLocal * compPackQty;
            amount = floatRate * looseQty;

            if (OptionsActivity.custDisc != 0) {
                _discAmnt = (amount * OptionsActivity.custDisc) / 100;
                _discAmnt = round(_discAmnt, 2);
                amountAfterDisc = amount - _discAmnt;
            } else {
                amountAfterDisc = amount;
            }

            amount = round(amount, 2);
            amountAfterDisc = round(amountAfterDisc, 2);

            gstAmt = (amountAfterDisc * gstPer) / 100;
            gstAmt = round(gstAmt, 2);

            totalAmt = amountAfterDisc + gstAmt;
            totalAmt = round(totalAmt, 2);

            cgstAmt = (amountAfterDisc * cgstPer) / 100;
            cgstAmt = round(cgstAmt, 2);

            sgstAmt = (amountAfterDisc * sgstPer) / 100;
            sgstAmt = round(sgstAmt, 2);

            cessAmt = (amountAfterDisc * cessPer) / 100;
            cessAmt = round(cessAmt, 2);

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
                        if (arr.length > 1) {
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
                            custOrder.setQty(selQtyLocal);
                            custOrder.setLooseQty(looseQty);
                            custOrder.setActLooseQty(looseQty);
                            custOrder.setAmount(String.valueOf(amount));
                            custOrder.setLoosePackTyp("Unpack");
                            custOrder.setPendingLooseQty(looseQty);
                            custOrder.setTotalamt(String.valueOf(totalAmt));
                            custOrder.setNetamnt(String.valueOf(totalAmt));
                            custOrder.setAmtAfterDisc(String.valueOf(amountAfterDisc));
                            custOrder.setGstper(String.valueOf(gstPer));
                            custOrder.setGstAmt(String.valueOf(gstAmt));
                            custOrder.setCgstamt(String.valueOf(cgstAmt));
                            custOrder.setSgstamt(String.valueOf(sgstAmt));
                            custOrder.setIgstamt(String.valueOf(igstAmt));
                            custOrder.setCgstper(String.valueOf(cgstPer));
                            custOrder.setSgstper(String.valueOf(sgstPer));
                            custOrder.setCessper(String.valueOf(cessPer));
                            custOrder.setCessamt(String.valueOf(cessAmt));
                            custOrder.setDiscamnt(String.valueOf(_discAmnt));
                            custOrder.setDiscPer(String.valueOf(OptionsActivity.custDisc));
                            custOrder.setOrderType("C");
                            custOrder.setAvailQty(0);
                            db.addCustomerOrder(custOrder);

                            Constant.showLog("HOCODE-" + hocode + "-ProdCol-" + prodCol + "-Auto-" + auto + "-BranchId-" + branchId +
                                    "-SelProdId-" + selProdId + "-SizeGroup-" + sizeGroup + "-SelColor-" + colour[0] +
                                    "-RequiredSize-" + requiredSize + "-PerPackQty-" + total +
                                    "-SelQty-" + selQtyLocal + "-MRP-" + mrp + "-Amount-" + amount + "-TotalAmt-" + totalAmt + "-GSTPer-" + gstPer +
                                    "-CGSTAMt-" + cgstAmt + "-SGSTAMt-" + sgstAmt + "-IGSTAMt-" + igstAmt + "-CGSTPer-" + cgstPer + "-SGSTPer-" + sgstPer +
                                    "-CESSPer-" + cessPer + "-CESSAmt-" + cessAmt + "-DiscPer-" + OptionsActivity.custDisc + "-DiscAmnt-" + _discAmnt);
                        } else {
                            showToast("Something Went Wrong");
                        }
                    }
                }
            }
            lay_comp_pack.setVisibility(View.GONE);
            clearFiledsPack();
            showToast("Added-To-Card");
        } else {
            showToast("Please Update GST Master");
        }
        totalCalculations();
    }

    private float round(float d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd.floatValue();
    }

    private void totalCalculations() {

        String totalSet = "0", totalQty = "0", totalAmnt = "0", totalNetAmnt = "0";

        Cursor res = db.getCustOrderTotals();
        if (res.moveToFirst()) {
            totalQty = res.getString(res.getColumnIndex(DBHandler.CO_LooseQty));
            totalSet = res.getString(res.getColumnIndex(DBHandler.CO_Auto));
            totalAmnt = res.getString(res.getColumnIndex(DBHandler.CO_Amount));
            totalNetAmnt = res.getString(res.getColumnIndex(DBHandler.CO_NetAmt));
        }
        res.close();

        if (totalQty == null) {
            totalQty = "0";
        }
        if (totalSet == null) {
            totalSet = "0";
        }
        if (totalAmnt == null) {
            totalAmnt = "0";
        }
        if (totalNetAmnt == null) {
            totalNetAmnt = "0";
        }

        tv_totqty.setText(totalQty);
        tv_totset.setText(totalSet);
        tv_totamnt.setText(totalAmnt);
        tv_totnetamt.setText(totalNetAmnt);

    }

    private void setAdapter() {
        auto_set.setAdapter(null);
        String[] arr = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.custom_spinner, arr);
        auto_set.setAdapter(adapter);
        auto_set.setSingleLine();
        auto_set.setText(arr[0]);
        auto_set.setSelection(auto_set.getText().length());
    }

    private void updateOrder() {
        String orderType = updateCustOrder.getOrderType();
        if (orderType.equals("P") || orderType.equals("C")) {
            rdo_pack.setChecked(true);
            rdo_unpack.setChecked(false);
            lay_pack.setVisibility(View.VISIBLE);
            lay_comp_pack.setVisibility(View.GONE);
            gridView.setVisibility(View.GONE);
        } else if (orderType.equals("U")) {
            rdo_pack.setChecked(false);
            rdo_unpack.setChecked(true);
            lay_pack.setVisibility(View.GONE);
            lay_comp_pack.setVisibility(View.GONE);
            gridView.setVisibility(View.VISIBLE);
        }
        selProdId = updateCustOrder.getProductid();
        String prodName = "";

        Cursor res = db.getProductDetails(getPackUnPack());
        if (res.moveToFirst()) {
            do {
                prodName = res.getString(res.getColumnIndex(DBHandler.PM_Finalprod));
                tv_wsp.setText(res.getString(res.getColumnIndex(DBHandler.PM_SRate)));
                tv_mrp.setText(res.getString(res.getColumnIndex(DBHandler.PM_MRPRate)));
                tv_hsncode.setText(res.getString(res.getColumnIndex(DBHandler.PM_HSNCode)));
                tv_gstper.setText(res.getString(res.getColumnIndex(DBHandler.PM_GSTGroup)));
            } while (res.moveToNext());
        }
        res.close();

        ed_prod_search.setText(prodName);
        ed_prod_search.setClickable(false);
        ed_prod_search.setFocusable(false);

        if (orderType.equals("P") || orderType.equals("C")) {
            setUpdateSizeGroupData();
        } else {
            setUpdateUnpackData();
        }

    }

    private void setUpdateSizeGroupData() {
        sizeGroup_list.clear();
        sizeGroup_list.add("Select SizeGroup");
        Cursor res1 = db.getDistinctSizeGroup(getPackUnPack());
        if (res1.moveToFirst()) {
            do {
                sizeGroup_list.add(res1.getString(res1.getColumnIndex(DBHandler.ARSD_SizeGroup)));
            } while (res1.moveToNext());
        } else {
            sizeGroup_list.add("NA");
        }
        res1.close();
        sp_sizeGroup.setAdapter(new ArrayAdapter<>(getApplicationContext(), R.layout.sizegroup_spinner_row, sizeGroup_list));

        sp_sizeGroup.setClickable(false);
        sp_sizeGroup.setFocusable(false);
        sp_sizeGroup.setEnabled(false);

        for (int i = 0; i < sizeGroup_list.size(); i++) {
            if (sizeGroup_list.get(i).equals(updateCustOrder.getSizeGroup())) {
                sp_sizeGroup.setSelection(i);
                break;
            }
        }
    }

    private void setUpdateSizeData(String sizegroup) {
        size_list.clear();
        rv_size.setAdapter(null);

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

        int selectedPos = 0;
        for (int i = 0; i < size_list.size(); i++) {
            if (size_list.get(i).equals(String.valueOf(updateCustOrder.getPerPackQty()))) {
                lay_comp_pack.setVisibility(View.VISIBLE);
                setUpdateAdapter();
            } else {
                lay_comp_pack.setVisibility(View.GONE);
            }
            if(updateCustOrder.getOrderType().equals("P")) {
                if (size_list.get(i).equals(String.valueOf(updateCustOrder.getLooseQty()))) {
                    selectedPos = i;
                    break;
                }
            }else{
                if (size_list.get(i).equals(String.valueOf(updateCustOrder.getPerPackQty()))) {
                    selectedPos = i;
                    break;
                }
            }
        }

        if (size_list.size() != 0) {
            SizeGroupWiseQtyAdapter adapter = new SizeGroupWiseQtyAdapter(size_list, getApplicationContext(), "U", selectedPos);
            adapter.setOnClickListener1(this);
            rv_size.setAdapter(adapter);
        } else {
            showToast("No Qty Avalilable");
        }

        setUpdateColour(selSizeGroup);
    }

    private void setUpdateColour(String size) {
        map.clear();
        colour_list.clear();
        rv_color.setAdapter(null);

        int i = 0;
        Cursor res1 = db.getDistinctColour(size);
        if (res1.moveToFirst()) {
            do {
                String col = res1.getString(res1.getColumnIndex(DBHandler.ARSD_Colour));
                if (col.equals(updateCustOrder.getColor())) {
                    map.put(i, 1);
                }
                String colourHashcode = col + "-" + res1.getString(res1.getColumnIndex(DBHandler.ARSD_HashCode));
                colour_list.add(colourHashcode);
                i++;
            } while (res1.moveToNext());
        } else {
            colour_list.add("NA");
        }
        res1.close();

        if (colour_list.size() != 0) {
            SizeGroupWiseColourAdapter adapter = new SizeGroupWiseColourAdapter(colour_list, getApplicationContext());
            rv_color.setAdapter(adapter);
        } else {
            toast.setText("No Colour Available");
        }
    }

    private void setUpdateAdapter() {
        auto_set.setAdapter(null);
        String[] arr = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.custom_spinner, arr);
        auto_set.setAdapter(adapter);
        auto_set.setSingleLine();
        auto_set.setText(arr[0]);
        for (int i = 0; i < arr.length; i++) {
            if (arr[i].equals(String.valueOf(updateCustOrder.getQty()))) {
                auto_set.setText(arr[i]);
                break;
            }
        }
        auto_set.setSelection(auto_set.getText().length());
    }

    private void setUpdateUnpackData() {
        unpackSizeList.clear();
        unClickablePositionsList.clear();
        allSizeChangeList.clear();
        sizeGroup_list.clear();
        colour_list.clear();
        gridView.setAdapter(null);

        List<String> savedSizeList = new ArrayList<>();
        List<String> savedSizeListForColor = new ArrayList<>();
        List<String> savedQty = new ArrayList<>();


        HashMap<String, String> sizeColorQtyMap = new HashMap<>();

        int unClickCount = -1;

        Cursor res3 = db.getSavedUnpackOrder();
        if (res3.moveToFirst()) {
            do {
                String colourHashcode = res3.getString(res3.getColumnIndex(DBHandler.CO_Color)) +
                        "-" +
                        res3.getString(res3.getColumnIndex(DBHandler.CO_HashCode));
                savedSizeListForColor.add(colourHashcode);

                String sizeGrp = res3.getString(res3.getColumnIndex(DBHandler.CO_SizeGroup));
                savedSizeList.add(sizeGrp);

                sizeGrp = sizeGrp + colourHashcode;

                String qty = res3.getString(res3.getColumnIndex(DBHandler.CO_Qty));
                savedQty.add(res3.getString(res3.getColumnIndex(DBHandler.CO_Qty)));

                sizeColorQtyMap.put(sizeGrp, qty);


            } while (res3.moveToNext());
        }
        res3.close();

        Cursor res2 = db.getDistinctSizeGroup(getPackUnPack());
        if (res2.moveToFirst()) {
            do {
                sizeGroup_list.add(res2.getString(res2.getColumnIndex(DBHandler.ARSD_SizeGroup)));
            } while (res2.moveToNext());
        } else {
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

        int gridColumns = colour_list.size() + 1;
        gridView.setNumColumns(gridColumns);

        unClickCount++;
        unpackSizeList.add("+2");
        for (int i = 0; i < colour_list.size(); i++) {
            unClickCount++;
            unpackSizeList.add(colour_list.get(i));
        }

        unClickCount++;
        unpackSizeList.add("+1");
        for (int i = 0; i < colour_list.size(); i++) {
            unClickCount++;
            allSizeChangeList.add(unClickCount);
            unpackSizeList.add("0");
        }

        for (int i = 0; i < sizeGroup_list.size(); i++) {
            unClickCount++;
            unClickablePositionsList.add(unClickCount);
            unpackSizeList.add(sizeGroup_list.get(i));
            for (int j = 0; j < colour_list.size(); j++) {
                unClickCount++;

                String sizeGrp = sizeGroup_list.get(i);
                String colourHashCode = colour_list.get(j);

                String key = sizeGrp + colourHashCode;

                if (sizeColorQtyMap.containsKey(key)) {
                    String qty = sizeColorQtyMap.get(key);
                    unpackSizeList.add(qty);
                } else {
                    unpackSizeList.add("0");
                }
            }
        }
        //gridView.setAdapter(new ArrayAdapter<>(getApplicationContext(),android.R.layout.test_list_item,unpackSizeList));
        gridView.setAdapter(new CustomerOrderUnpackGridAdpater(getApplicationContext(), unpackSizeList));
    }

    private void addToCardUnpackUpdate() {
        db.deleteOrderTableUnpack();
        int counter = 0, isDataInserted = 0;
        HashMap<String, List<String>> output = new HashMap<>();
        for (int i = 0; i < unpackSizeList.size(); i++) {
            int getColour = -1;
            List<String> list = new ArrayList<>();
            for (int j = 0; j <= colour_list.size(); j++) {
                if (j == 0) {
                    output.put(unpackSizeList.get(counter), list);
                } else {
                    getColour++;
                    String colour = colour_list.get(getColour);
                    String qty = unpackSizeList.get(counter);
                    if (qty.equals("")) {
                        qty = "0";
                    }
                    list.add(qty + "^" + colour);
                }
                counter++;
            }
            i = counter;
        }

        if (!output.isEmpty()) {

            db.deleteOrderTable(updateCustOrder.getAuto());

            String rate = tv_wsp.getText().toString();
            String mrp = tv_mrp.getText().toString();
            float floatRate = Float.parseFloat(rate);

            String gstgroupName = tv_gstper.getText().toString();
            float gstPer = 0, cgstPer = 0, sgstPer = 0, cessPer = 0, cgstShare = 0, sgstShare = 0;
            float amount = 0, amountAfterDisc = 0, _discAmnt = 0, gstAmt = 0, cgstAmt = 0, sgstAmt = 0, cessAmt = 0, igstAmt = 0, totalAmt;
            String gstStatus = "";
            Cursor res = db.getGSTDetails(gstgroupName);
            if (res.moveToFirst()) {
                gstStatus = res.getString(res.getColumnIndex(DBHandler.GST_Status));
                gstPer = res.getFloat(res.getColumnIndex(DBHandler.GST_GSTPer));
                cgstPer = res.getFloat(res.getColumnIndex(DBHandler.GST_CGSTPer));
                sgstPer = res.getFloat(res.getColumnIndex(DBHandler.GST_SGSTPer));
                cessPer = res.getFloat(res.getColumnIndex(DBHandler.GST_CESSPer));
                cgstShare = res.getFloat(res.getColumnIndex(DBHandler.GST_CGSTSHARE));
                sgstShare = res.getFloat(res.getColumnIndex(DBHandler.GST_SGSTSHARE));
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

                                if (isDataInserted == 0) {
                                    isDataInserted = 1;
                                }

                                /*amount = 0;
                                gstAmt = 0;
                                totalAmt = 0;
                                cgstAmt = 0;
                                sgstAmt = 0;
                                cessAmt = 0;*/

                                amount = floatRate * looseQty;

                                if (OptionsActivity.custDisc != 0) {
                                    _discAmnt = (amount * OptionsActivity.custDisc) / 100;
                                    _discAmnt = round(_discAmnt, 2);
                                    amountAfterDisc = amount - _discAmnt;
                                } else {
                                    amountAfterDisc = amount;
                                }

                                amount = round(amount, 2);
                                amountAfterDisc = round(amountAfterDisc, 2);

                                gstAmt = (amountAfterDisc * gstPer) / 100;
                                gstAmt = round(gstAmt, 2);
                                totalAmt = amountAfterDisc + gstAmt;
                                totalAmt = round(totalAmt, 2);

                                cgstAmt = (amountAfterDisc * cgstPer) / 100;
                                cgstAmt = round(cgstAmt, 2);
                                sgstAmt = (amountAfterDisc * sgstPer) / 100;
                                sgstAmt = round(sgstAmt, 2);
                                cessAmt = (amountAfterDisc * cessPer) / 100;
                                cessAmt = round(cessAmt, 2);

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
                                custOrder.setAmount(String.valueOf(amount));
                                custOrder.setLoosePackTyp("Unpack");
                                custOrder.setPendingLooseQty(looseQty);
                                custOrder.setTotalamt(String.valueOf(totalAmt));
                                custOrder.setNetamnt(String.valueOf(totalAmt));
                                custOrder.setAmtAfterDisc(String.valueOf(amountAfterDisc));
                                custOrder.setGstper(String.valueOf(gstPer));
                                custOrder.setGstAmt(String.valueOf(gstAmt));
                                custOrder.setCgstamt(String.valueOf(cgstAmt));
                                custOrder.setSgstamt(String.valueOf(sgstAmt));
                                custOrder.setIgstamt(String.valueOf(igstAmt));
                                custOrder.setCgstper(String.valueOf(cgstPer));
                                custOrder.setSgstper(String.valueOf(sgstPer));
                                custOrder.setCessper(String.valueOf(cessPer));
                                custOrder.setCessamt(String.valueOf(cessAmt));
                                custOrder.setDiscamnt(String.valueOf(_discAmnt));
                                custOrder.setDiscPer(String.valueOf(OptionsActivity.custDisc));
                                custOrder.setOrderType("U");
                                custOrder.setAvailQty(updateCustOrder.getAvailQty());
                                db.addCustomerOrder(custOrder);

                                Constant.showLog("HOCODE-" + hocode + "-ProdCol-" + prodCol + "-Auto-" + auto + "-BranchId-" + branchId +
                                        "-SelProdId-" + selProdId + "-SizeGroup-" + sizeGroup + "-SelColor-" + colour +
                                        "-RequiredSize-" + sizeGroup + "-PerPackQty-1" +
                                        "-SelQty-" + looseQty + "-MRP-" + mrp + "-Amount-" + amount + "-TotalAmt-" + totalAmt + "-GSTPer-" + gstPer +
                                        "-CGSTAMt-" + cgstAmt + "-SGSTAMt-" + sgstAmt + "-IGSTAMt-" + igstAmt + "-CGSTPer-" + cgstPer + "-SGSTPer-" + sgstPer +
                                        "-CESSPer-" + cessPer + "-CESSAmt-" + cessAmt + "-DiscPer-" + OptionsActivity.custDisc + "-DiscAmnt-" + _discAmnt);
                            }
                        }
                    }
                }
                if (isDataInserted == 1) {
                    //showToast("Added-To-Card");
                    showDia(22);
                } else {
                    showToast("Please Enter Qty");
                }
            } else {
                showToast("Please Refresh GSTMaster");
            }
        }
        totalCalculations();
    }

    private void addToCardUpdate() {
        String rate = tv_wsp.getText().toString();
        String mrp = tv_mrp.getText().toString();
        float floatRate = Float.parseFloat(rate);

        String gstgroupName = tv_gstper.getText().toString();
        float gstPer = 0, cgstPer = 0, sgstPer = 0, cessPer = 0, cgstShare = 0, sgstShare = 0;
        float amount = 0, amountAfterDisc = 0, _discAmnt = 0, gstAmt = 0, cgstAmt = 0, sgstAmt = 0, cessAmt = 0, igstAmt = 0, totalAmt;
        String gstStatus = "";
        Cursor res = db.getGSTDetails(gstgroupName);
        if (res.moveToFirst()) {
            gstStatus = res.getString(res.getColumnIndex(DBHandler.GST_Status));
            gstPer = res.getFloat(res.getColumnIndex(DBHandler.GST_GSTPer));
            cgstPer = res.getFloat(res.getColumnIndex(DBHandler.GST_CGSTPer));
            sgstPer = res.getFloat(res.getColumnIndex(DBHandler.GST_SGSTPer));
            cessPer = res.getFloat(res.getColumnIndex(DBHandler.GST_CESSPer));
            cgstShare = res.getFloat(res.getColumnIndex(DBHandler.GST_CGSTSHARE));
            sgstShare = res.getFloat(res.getColumnIndex(DBHandler.GST_SGSTSHARE));
        }
        res.close();

        if (gstStatus.equalsIgnoreCase("A")) {

            db.deleteOrderTable(updateCustOrder.getAuto());

            int looseQty = selQtyLocal;

            amount = floatRate * looseQty;

            if(OptionsActivity.custDisc!=0) {
                _discAmnt = (amount * OptionsActivity.custDisc)/100;
                _discAmnt = round(_discAmnt,2);
                amountAfterDisc = amount - _discAmnt;
            }else{
                amountAfterDisc = amount;
            }

            amount = round(amount,2);
            amountAfterDisc = round(amountAfterDisc,2);

            gstAmt = (amountAfterDisc * gstPer) / 100;
            gstAmt = round(gstAmt,2);
            totalAmt = amountAfterDisc + gstAmt;
            totalAmt = round(totalAmt,2);

            cgstAmt = (amountAfterDisc * cgstPer) / 100;
            cgstAmt = round(cgstAmt,2);
            sgstAmt = (amountAfterDisc * sgstPer) / 100;
            sgstAmt = round(sgstAmt,2);
            cessAmt = (amountAfterDisc * cessPer) / 100;
            cessAmt = round(cessAmt,2);

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
                        if (arr.length > 1) {
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
                            custOrder.setAmount(String.valueOf(amount));
                            custOrder.setLoosePackTyp("Unpack");
                            custOrder.setPendingLooseQty(selQtyLocal);
                            custOrder.setTotalamt(String.valueOf(totalAmt));
                            custOrder.setNetamnt(String.valueOf(totalAmt));
                            custOrder.setAmtAfterDisc(String.valueOf(amountAfterDisc));
                            custOrder.setGstAmt(String.valueOf(gstAmt));
                            custOrder.setGstper(String.valueOf(gstPer));
                            custOrder.setCgstamt(String.valueOf(cgstAmt));
                            custOrder.setSgstamt(String.valueOf(sgstAmt));
                            custOrder.setIgstamt(String.valueOf(igstAmt));
                            custOrder.setCgstper(String.valueOf(cgstPer));
                            custOrder.setSgstper(String.valueOf(sgstPer));
                            custOrder.setCessper(String.valueOf(cessPer));
                            custOrder.setCessamt(String.valueOf(cessAmt));
                            custOrder.setDiscamnt(String.valueOf(_discAmnt));
                            custOrder.setDiscPer(String.valueOf(OptionsActivity.custDisc));
                            custOrder.setOrderType("P");
                            custOrder.setAvailQty(updateCustOrder.getAvailQty());
                            db.addCustomerOrder(custOrder);
                            Constant.showLog("HOCODE-" + hocode + "-ProdCol-" + prodCol + "-Auto-" + auto + "-BranchId-" + branchId +
                                    "-SelProdId-" + selProdId + "-SizeGroup-" + sizeGroup + "-SelColor-" + colour[0] +
                                    "-RequiredSize-" + requiredSize + "-PerPackQty-" + total +
                                    "-SelQty-" + selQtyLocal + "-MRP-" + mrp + "-Amount-" + amount + "-TotalAmt-" + totalAmt + "-GSTPer-" + gstPer +
                                    "-CGSTAMt-" + cgstAmt + "-SGSTAMt-" + sgstAmt + "-IGSTAMt-" + igstAmt + "-CGSTPer-" + cgstPer + "-SGSTPer-" + sgstPer +
                                    "-CESSPer-" + cessPer + "-CESSAmt-" + cessAmt+"-DiscPer-"+OptionsActivity.custDisc+"-DiscAmnt-"+_discAmnt);
                        } else {
                            showToast("Something Went Wrong");
                        }
                    }
                }
            }
            showToast("Added To Card");
        } else {
            showToast("GST Status Cancelled");
        }
        totalCalculations();
        showDia(22);
    }

    private void addToCardCompPackUpdate() {
        String rate = tv_wsp.getText().toString();
        String mrp = tv_mrp.getText().toString();
        float floatRate = Float.parseFloat(rate);

        String gstgroupName = tv_gstper.getText().toString();
        float gstPer = 0, cgstPer = 0, sgstPer = 0, cessPer = 0, cgstShare = 0, sgstShare = 0;
        float amount = 0, amountAfterDisc = 0, _discAmnt = 0, gstAmt = 0, cgstAmt = 0, sgstAmt = 0, cessAmt = 0, igstAmt = 0, totalAmt;
        String gstStatus = "";
        Cursor res = db.getGSTDetails(gstgroupName);
        if (res.moveToFirst()) {
            gstStatus = res.getString(res.getColumnIndex(DBHandler.GST_Status));
            gstPer = res.getFloat(res.getColumnIndex(DBHandler.GST_GSTPer));
            cgstPer = res.getFloat(res.getColumnIndex(DBHandler.GST_CGSTPer));
            sgstPer = res.getFloat(res.getColumnIndex(DBHandler.GST_SGSTPer));
            cessPer = res.getFloat(res.getColumnIndex(DBHandler.GST_CESSPer));
            cgstShare = res.getFloat(res.getColumnIndex(DBHandler.GST_CGSTSHARE));
            sgstShare = res.getFloat(res.getColumnIndex(DBHandler.GST_SGSTSHARE));
        }
        res.close();

        if (gstStatus.equalsIgnoreCase("A")) {

            db.deleteOrderTable(updateCustOrder.getAuto());

            int looseQty = selQtyLocal*compPackQty;
            amount = floatRate * looseQty;

            if(OptionsActivity.custDisc!=0) {
                _discAmnt = (amount * OptionsActivity.custDisc)/100;
                _discAmnt = round(_discAmnt,2);
                amountAfterDisc = amount - _discAmnt;
            }else{
                amountAfterDisc = amount;
            }

            amount = round(amount,2);
            amountAfterDisc = round(amountAfterDisc,2);

            gstAmt = (amountAfterDisc * gstPer) / 100;
            gstAmt = round(gstAmt,2);

            totalAmt = amountAfterDisc + gstAmt;
            totalAmt = round(totalAmt,2);

            cgstAmt = (amountAfterDisc * cgstPer) / 100;
            cgstAmt = round(cgstAmt,2);

            sgstAmt = (amountAfterDisc * sgstPer) / 100;
            sgstAmt = round(sgstAmt,2);

            cessAmt = (amountAfterDisc * cessPer) / 100;
            cessAmt = round(cessAmt,2);

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
                        if (arr.length > 1) {
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
                            custOrder.setQty(selQtyLocal);
                            custOrder.setLooseQty(looseQty);
                            custOrder.setActLooseQty(looseQty);
                            custOrder.setAmount(String.valueOf(amount));
                            custOrder.setLoosePackTyp("Unpack");
                            custOrder.setPendingLooseQty(looseQty);
                            custOrder.setTotalamt(String.valueOf(totalAmt));
                            custOrder.setNetamnt(String.valueOf(totalAmt));
                            custOrder.setAmtAfterDisc(String.valueOf(amountAfterDisc));
                            custOrder.setGstper(String.valueOf(gstPer));
                            custOrder.setGstAmt(String.valueOf(gstAmt));
                            custOrder.setCgstamt(String.valueOf(cgstAmt));
                            custOrder.setSgstamt(String.valueOf(sgstAmt));
                            custOrder.setIgstamt(String.valueOf(igstAmt));
                            custOrder.setCgstper(String.valueOf(cgstPer));
                            custOrder.setSgstper(String.valueOf(sgstPer));
                            custOrder.setCessper(String.valueOf(cessPer));
                            custOrder.setCessamt(String.valueOf(cessAmt));
                            custOrder.setDiscamnt(String.valueOf(_discAmnt));
                            custOrder.setDiscPer(String.valueOf(OptionsActivity.custDisc));
                            custOrder.setOrderType("C");
                            custOrder.setAvailQty(updateCustOrder.getAvailQty());
                            db.addCustomerOrder(custOrder);

                            Constant.showLog("HOCODE-" + hocode + "-ProdCol-" + prodCol + "-Auto-" + auto + "-BranchId-" + branchId +
                                    "-SelProdId-" + selProdId + "-SizeGroup-" + sizeGroup + "-SelColor-" + colour[0] +
                                    "-RequiredSize-" + requiredSize + "-PerPackQty-" + total +
                                    "-SelQty-" + selQtyLocal + "-MRP-" + mrp + "-Amount-" + amount + "-TotalAmt-" + totalAmt + "-GSTPer-" + gstPer +
                                    "-CGSTAMt-" + cgstAmt + "-SGSTAMt-" + sgstAmt + "-IGSTAMt-" + igstAmt + "-CGSTPer-" + cgstPer + "-SGSTPer-" + sgstPer +
                                    "-CESSPer-" + cessPer + "-CESSAmt-" + cessAmt+"-DiscPer-"+OptionsActivity.custDisc+"-DiscAmnt-"+_discAmnt);
                        } else {
                            showToast("Something Went Wrong");
                        }
                    }
                }
            }
            clearFiledsPack();
            showToast("Added To Card");
        } else {
            showToast("GST Status Cancelled");
        }
        totalCalculations();
        showDia(22);
    }

    private void clearFiledsPack(){
        lay_comp_pack.setVisibility(View.GONE);
        rv_size.setAdapter(null);
        rv_color.setAdapter(null);
        setSizeData(selSizeGroup);
    }

    private void clearFiledsUnPack(){
        setUnpackData();
    }

    private void setProductWiseOrderList(){
        listView.setAdapter(null);
    }

    private void showToast(String msg){
        toast.setText(msg);
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
        tv_remove_item = (TextView) findViewById(R.id.tv_remove_item);
        tv_cancel_item = (TextView) findViewById(R.id.tv_cancel_item);
        tv_add_to_card_final = (TextView) findViewById(R.id.tv_add_to_card_final);
        tv_checkout = (TextView) findViewById(R.id.tv_checkout);
        tv_vieworder = (TextView) findViewById(R.id.tv_vieworder);
        tv_totqty = (TextView) findViewById(R.id.tv_tqty);
        tv_totamnt = (TextView) findViewById(R.id.tv_tamnt);
        tv_totset = (TextView) findViewById(R.id.tv_tset);
        tv_totnetamt = (TextView) findViewById(R.id.tv_namnt);

        sp_sizeGroup = (Spinner) findViewById(R.id.sp_sizegroup);

        rv_size = (RecyclerView) findViewById(R.id.rv_size);
        rv_color = (RecyclerView) findViewById(R.id.rv_color);

        listView = (ListView) findViewById(R.id.listView);
        gridView = (GridView) findViewById(R.id.gridView);

        lay_pack = (LinearLayout) findViewById(R.id.lay_pack);
        lay_comp_pack = (LinearLayout) findViewById(R.id.lay_comp_pack);

        sizeGroup_list = new ArrayList<>();
        size_list = new ArrayList<>();
        colour_list = new ArrayList<>();
        db = new DBHandler(getApplicationContext());
        map = new HashMap<>();
        unpackSizeList = new ArrayList<>();
        unClickablePositionsList = new ArrayList<>();
        allSizeChangeList = new ArrayList<>();

        auto_set = (AutoCompleteTextView)findViewById(R.id.auto_comppack_set);
        setAdapter();
    }

    private void showDia(int a) {
        AlertDialog.Builder builder = new AlertDialog.Builder(AddToCartActivity.this);
        builder.setCancelable(false);
        if (a == 1) {
            builder.setMessage("Do You Want To Remove Item?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if(updateCustOrder!=null){
                        if(updateCustOrder.getOrderType().equals("U")){
                            db.deleteOrderTableUnpack();
                        }else{
                            db.deleteOrderTable(updateCustOrder.getAuto());
                        }
                    }
                    totalCalculations();
                    showDia(11);
                    dialog.dismiss();
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        }else if (a == 2) {
            builder.setMessage("Discard Changes?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    activityToFrom = 0;
                    selProdId = 0;
                    onResume();
                    dialog.dismiss();
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        }else if (a == 22) {
            builder.setMessage("Order Updated");
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    activityToFrom = 0;
                    selProdId = 0;
                    dialog.dismiss();
                    startActivity(new Intent(getApplicationContext(),ViewCustomerOrderActiviy.class));
                    overridePendingTransition(R.anim.enter,R.anim.exit);
                }
            });
        }else if(a==11){
            builder.setMessage("Order Removed");
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    activityToFrom = 0;
                    selProdId = 0;
                    dialog.dismiss();
                    startActivity(new Intent(getApplicationContext(),ViewCustomerOrderActiviy.class));
                    overridePendingTransition(R.anim.enter,R.anim.exit);
                }
            });
        }

        builder.create().show();
    }

    private void showCompPackDia(int a) {
        LayoutInflater layoutInflater = LayoutInflater.from(AddToCartActivity.this);
        View promptView = layoutInflater.inflate(R.layout.company_pack_customer_order_dialog, null);

        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AddToCartActivity.this);
        alertDialogBuilder.setView(promptView);
        alertDialogBuilder.setTitle(ed_prod_search.getText().toString());
        alertDialogBuilder.setMessage("Company Pack Qty "+compPackQty);

        TextView tv_prod = (TextView) promptView.findViewById(R.id.tv_product);
        TextView tv_compPackQty = (TextView) promptView.findViewById(R.id.tv_comppack_qty);

        tv_prod.setText(ed_prod_search.getText().toString());
        tv_compPackQty.setText(String.valueOf(compPackQty));

        String[] arr = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,R.layout.custom_spinner, arr);

        final AutoCompleteTextView auto_set = (AutoCompleteTextView)promptView.findViewById(R.id.auto_comppack_set);
        auto_set.setAdapter(adapter);
        auto_set.setSingleLine();
        auto_set.setText(arr[0]);
        auto_set.setSelection(auto_set.getText().length());
        alertDialogBuilder.setCancelable(false);

        auto_set.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                final int action = motionEvent.getAction();
                if (action == MotionEvent.ACTION_DOWN) {
                    auto_set.showDropDown();
                    auto_set.setThreshold(0);
                }
                return false;
            }
        });

        alertDialogBuilder.setPositiveButton("Add To Card", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                String str = auto_set.getText().toString();
                if(!str.equals("")) {
                    selQtyLocal = Integer.parseInt(auto_set.getText().toString());
                    if (selQtyLocal != 0) {
                        InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        mgr.hideSoftInputFromWindow(auto_set.getWindowToken(), 0);
                        addToCardCompPack();
                    }else {
                        showToast("Please Enter Non Zero Value");
                    }
                }else{
                    showToast("Please Enter Proper Value");
                }
            }
        });
        alertDialogBuilder.setNeutralButton("CANCEL", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });

        alertDialogBuilder.create().show();

        /*final Dialog dialog = new Dialog(this);
        LayoutInflater inflater = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View post = inflater.inflate(R.layout.company_pack_customer_order_dialog, null);
        AutoCompleteTextView  textView = (AutoCompleteTextView)post.findViewById((R.id.auto_comppack_set));

        String[] arr = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};

        ArrayAdapter adapter = new ArrayAdapter<>(this,android.R.layout.simple_dropdown_item_1line, arr);
        textView.setAdapter(adapter);
        dialog.setContentView(post);
        dialog.setTitle("Company Pack");
        dialog.show();*/
    }

    private void writeLog(String _data) {
        new WriteLog().writeLog(getApplicationContext(), "AddToCartActivity_" + _data);
    }

    @Override
    public void onItemClick(String size) {
        Constant.showLog(size);
        if (!size.equalsIgnoreCase("NA")) {
            if(activityToFrom==1) {
                selQtyLocal = Integer.parseInt(size);
                setColour(selSizeGroup);
                if (selQtyLocal != compPackQty) {
                    lay_comp_pack.setVisibility(View.GONE);
                } else {
                    lay_comp_pack.setVisibility(View.VISIBLE);
                    setAdapter();
                }
            }else{
                selQtyLocal = Integer.parseInt(size);
                setUpdateColour(selSizeGroup);
                if (selQtyLocal != compPackQty) {
                    lay_comp_pack.setVisibility(View.GONE);
                } else {
                    lay_comp_pack.setVisibility(View.VISIBLE);
                    setUpdateAdapter();
                }
            }
        } else {
            showToast("No Colour Available");
        }
    }
}
