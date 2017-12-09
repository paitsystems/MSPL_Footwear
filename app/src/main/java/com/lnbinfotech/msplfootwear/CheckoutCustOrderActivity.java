package com.lnbinfotech.msplfootwear;

import android.content.DialogInterface;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.Gravity;

import com.lnbinfotech.msplfootwear.adapters.CheckoutCustOrderAdapter;
import com.lnbinfotech.msplfootwear.adapters.ViewCustomerOrderAdapter;
import com.lnbinfotech.msplfootwear.connectivity.ConnectivityTest;
import com.lnbinfotech.msplfootwear.constant.Constant;
import com.lnbinfotech.msplfootwear.db.DBHandler;
import com.lnbinfotech.msplfootwear.interfaces.ServerCallbackList;
import com.lnbinfotech.msplfootwear.log.WriteLog;
import com.lnbinfotech.msplfootwear.model.CheckoutCustOrderClass;
import com.lnbinfotech.msplfootwear.model.CustomerOrderClass;
import com.lnbinfotech.msplfootwear.post.Post;
import com.lnbinfotech.msplfootwear.volleyrequests.VolleyRequests;

import org.json.JSONObject;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class CheckoutCustOrderActivity extends AppCompatActivity implements View.OnClickListener {

    private Constant constant, constant1;
    private Toast toast;

    private ListView lv_check;
    private Button btn_save;
    private TextView tv_tot_looseQty, tv_tot_availQty;

    private List<CustomerOrderClass> list;
    private DBHandler db;
    private List<String> urlList;
    private int counter = 0;
    private String from;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout_cust_order);

        init();

        from = getIntent().getExtras().getString("from");

        if (getSupportActionBar() != null) {
            if(from.equals("addtocard")) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }
        }

        checkStock();

        btn_save.setOnClickListener(this);

        lv_check.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(from.equals("addtocard")) {
                    list.get(i);
                    Constant.showLog("selected pos:" + i);
                    AddToCartActivity.updateCustOrder = list.get(i);
                    showDia(1);
                }
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_save:
                if(ConnectivityTest.getNetStat(getApplicationContext())) {
                    getSaveOrderData();
                }else {
                    toast.setText("You Are Offline");
                    toast.show();
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        //showDia(0);
        new Constant(CheckoutCustOrderActivity.this).doFinish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //showDia(0);
                new Constant(CheckoutCustOrderActivity.this).doFinish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void init() {
        constant = new Constant(CheckoutCustOrderActivity.this);
        constant1 = new Constant(getApplicationContext());
        toast = Toast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        lv_check = (ListView) findViewById(R.id.lv_check);
        tv_tot_looseQty = (TextView) findViewById(R.id.tv_tot_looseQty);
        tv_tot_availQty = (TextView) findViewById(R.id.tv_tot_availQty);
        btn_save = (Button) findViewById(R.id.btn_save);
        list = new ArrayList<>();
        urlList = new ArrayList<>();
        db = new DBHandler(getApplicationContext());
    }

    private void checkStock(){
        String data = "";
        Cursor res = new DBHandler(getApplicationContext()).getDataToCheck();
        if(res.moveToFirst()){
            do{
                int branchId = res.getInt(res.getColumnIndex(DBHandler.CO_BranchId));
                int prodId = res.getInt(res.getColumnIndex(DBHandler.CO_Productid));
                String size = res.getString(res.getColumnIndex(DBHandler.CO_SizeGroup));
                String color = res.getString(res.getColumnIndex(DBHandler.CO_Color));
                String hashcode = res.getString(res.getColumnIndex(DBHandler.CO_HashCode));
                String rate = res.getString(res.getColumnIndex(DBHandler.CO_MRP));
                String looseqty = res.getString(res.getColumnIndex(DBHandler.CO_LooseQty));
                String str = branchId+"^"+prodId+"^"+size+"^"+color+"^"+hashcode+"^"+rate+"^"+looseqty+",";
                data = data + str;
            }while (res.moveToNext());
        }
        res.close();
        if(!data.equals("")){
            data = data.substring(0,data.length()-1);
        }
        showCheckoutOrderDetails(data);
    }

    private void showCheckoutOrderDetails(String data) {
        if (ConnectivityTest.getNetStat(CheckoutCustOrderActivity.this)) {
            try {
                data = URLEncoder.encode(data, "UTF-8");
                Constant.showLog(data);
                String url = Constant.ipaddress + "/CheckStock?data=" + data;
                Constant.showLog(url);
                writeLog("showCheckoutOrderDetails" + url);
                constant.showPD();
                VolleyRequests requests = new VolleyRequests(CheckoutCustOrderActivity.this);
                requests.loadCheckoutOrder(url, new ServerCallbackList() {
                    @Override
                    public void onSuccess(Object result) {
                        constant.showPD();
                        /*List<CheckoutCustOrderClass> list = (List<CheckoutCustOrderClass>) result;
                        if (list.size() != 0) {
                            // setTotal(list);
                            showPopup(1);
                        } else {
                            showPopup(3);
                        }*/
                        setData();
                    }

                    @Override
                    public void onFailure(Object result) {
                        constant.showPD();
                        showDia(2);
                    }
                });
            }catch (Exception e){
                e.printStackTrace();
            }
        } else {
            toast.setText("You Are Offline");
            toast.show();
        }
    }

    private void setData(){
        int totLooseQty = 0, totAvailQty = 0;
        list.clear();
        Cursor cursor = new DBHandler(getApplicationContext()).getViewOrderData();
        if(cursor.moveToFirst()){
            do{
                CustomerOrderClass order = new CustomerOrderClass();
                order.setAuto(cursor.getInt(cursor.getColumnIndex(DBHandler.CO_Auto)));
                order.setProductid(cursor.getInt(cursor.getColumnIndex(DBHandler.CO_Productid)));
                order.setSizeGroup(cursor.getString(cursor.getColumnIndex(DBHandler.CO_SizeGroup)));
                order.setColor(cursor.getString(cursor.getColumnIndex(DBHandler.CO_Color)));
                order.setHashCode(cursor.getString(cursor.getColumnIndex(DBHandler.CO_HashCode)));

                int _totLooseQty = cursor.getInt(cursor.getColumnIndex(DBHandler.CO_LooseQty));
                totLooseQty = totLooseQty + _totLooseQty;
                order.setLooseQty(_totLooseQty);
                order.setMrp(cursor.getString(cursor.getColumnIndex(DBHandler.CO_MRP)));
                order.setAmount(cursor.getString(cursor.getColumnIndex(DBHandler.CO_Amount)));
                order.setGstper(cursor.getString(cursor.getColumnIndex(DBHandler.CO_GSTPer)));
                order.setRate(cursor.getString(cursor.getColumnIndex(DBHandler.CO_Rate)));
                order.setQty(cursor.getInt(cursor.getColumnIndex(DBHandler.CO_Qty)));
                order.setActLooseQty(cursor.getInt(cursor.getColumnIndex(DBHandler.CO_ActLooseQty)));
                order.setLoosePackTyp(cursor.getString(cursor.getColumnIndex(DBHandler.CO_LoosePackTyp)));
                order.setPerPackQty(cursor.getInt(cursor.getColumnIndex(DBHandler.CO_PerPackQty)));
                order.setOrderType(cursor.getString(cursor.getColumnIndex(DBHandler.CO_OrderType)));

                int _totAvailQty = cursor.getInt(cursor.getColumnIndex(DBHandler.CO_AvailQty));
                order.setAvailQty(_totAvailQty);
                totAvailQty = totAvailQty + _totAvailQty;

                list.add(order);
            }while (cursor.moveToNext());
        }
        cursor.close();
        tv_tot_looseQty.setText(String.valueOf(totLooseQty));
        tv_tot_availQty.setText(String.valueOf(totAvailQty));
        CheckoutCustOrderAdapter adapter = new CheckoutCustOrderAdapter(list, getApplicationContext());
        lv_check.setAdapter(adapter);
    }

    private void getSaveOrderData() {
        List<Integer> branchIdList = new ArrayList<>();
        urlList.clear();

        Cursor res = db.getDistinctBrachIdFromCustOrder();
        if (res.moveToFirst()) {
            do {
                branchIdList.add(res.getInt(res.getColumnIndex(DBHandler.CO_BranchId)));
            } while (res.moveToNext());
            if(res.getCount()==1){
                counter=1;
            }
        }
        res.close();

        int hoCode = FirstActivity.pref.getInt(getString(R.string.pref_hocode), 0);
        int custId = FirstActivity.pref.getInt(getString(R.string.pref_retailCustId), 0);
        int saleExe = 1;

        //for(int branchId:branchIdList){
        //db.getCustOrderMaster(branchId);
        if(branchIdList.size()!=0) {
            try {
                int branchId = branchIdList.get(0);
                int dispatchCmp = branchId;

                int totQty = 0, totLooseQty = 0;
                float totAmt = 0, totGSTAmt = 0, totTotalAmt = 0,
                        totNetAmt = 0, totAmtAfterDisc = 0, totPendingQty = 0, totDisc = 0;
                float custDiscPer = OptionsActivity.custDisc;
                String remark = "NA";
                String groupNm = "NA";
                String data = "";

                Cursor custOrderRes = db.getCustOrderDetail(branchId);
                if (custOrderRes.moveToFirst()) {
                    do {
                        int prodId, perPackQty, qty, looseQty, actLooseQty, pendingLooseQty;
                        String sizeGroup, requiredSize, color, loosePackTyp;
                        float rate, mrp, amount, totalAmt, netAmt, gstPer, gstAmt, CGSTAmt, amtAfterDisc,
                                SGSTAmt, IGSTAmt, CGSTPer, SGSTPer, CessPer, CESSAmt, discPer, discAmt;

                        prodId = custOrderRes.getInt(custOrderRes.getColumnIndex(DBHandler.CO_Productid));
                        sizeGroup = custOrderRes.getString(custOrderRes.getColumnIndex(DBHandler.CO_SizeGroup));
                        requiredSize = custOrderRes.getString(custOrderRes.getColumnIndex(DBHandler.CO_RequiredSize));
                        perPackQty = custOrderRes.getInt(custOrderRes.getColumnIndex(DBHandler.CO_PerPackQty));
                        color = custOrderRes.getString(custOrderRes.getColumnIndex(DBHandler.CO_Color));
                        rate = custOrderRes.getFloat(custOrderRes.getColumnIndex(DBHandler.CO_Rate));
                        mrp = custOrderRes.getFloat(custOrderRes.getColumnIndex(DBHandler.CO_MRP));
                        qty = custOrderRes.getInt(custOrderRes.getColumnIndex(DBHandler.CO_Qty));
                        totQty = totQty + qty;
                        looseQty = custOrderRes.getInt(custOrderRes.getColumnIndex(DBHandler.CO_LooseQty));
                        totLooseQty = totLooseQty + looseQty;
                        actLooseQty = custOrderRes.getInt(custOrderRes.getColumnIndex(DBHandler.CO_ActLooseQty));
                        amount = custOrderRes.getFloat(custOrderRes.getColumnIndex(DBHandler.CO_Amount));
                        totAmt = totAmt + amount;
                        loosePackTyp = custOrderRes.getString(custOrderRes.getColumnIndex(DBHandler.CO_LoosePackTyp));
                        pendingLooseQty = custOrderRes.getInt(custOrderRes.getColumnIndex(DBHandler.CO_PendingLooseQty));
                        totPendingQty = totPendingQty + pendingLooseQty;
                        totalAmt = custOrderRes.getFloat(custOrderRes.getColumnIndex(DBHandler.CO_TotalAmt));
                        totTotalAmt = totTotalAmt + totalAmt;
                        netAmt = custOrderRes.getFloat(custOrderRes.getColumnIndex(DBHandler.CO_NetAmt));
                        totNetAmt = totNetAmt + netAmt;
                        amtAfterDisc = custOrderRes.getFloat(custOrderRes.getColumnIndex(DBHandler.CO_AmtAfterDisc));
                        totAmtAfterDisc = totAmtAfterDisc + amtAfterDisc;
                        gstPer = custOrderRes.getFloat(custOrderRes.getColumnIndex(DBHandler.CO_GSTPer));
                        gstAmt = custOrderRes.getFloat(custOrderRes.getColumnIndex(DBHandler.CO_GSTAmt));
                        totGSTAmt = totGSTAmt + gstAmt;
                        CGSTAmt = custOrderRes.getFloat(custOrderRes.getColumnIndex(DBHandler.CO_CGSTAmt));
                        SGSTAmt = custOrderRes.getFloat(custOrderRes.getColumnIndex(DBHandler.CO_SGSTAmt));
                        IGSTAmt = custOrderRes.getFloat(custOrderRes.getColumnIndex(DBHandler.CO_IGSTAmt));
                        CGSTPer = custOrderRes.getFloat(custOrderRes.getColumnIndex(DBHandler.CO_CGSTPer));
                        SGSTPer = custOrderRes.getFloat(custOrderRes.getColumnIndex(DBHandler.CO_SGSTPer));
                        CessPer = custOrderRes.getFloat(custOrderRes.getColumnIndex(DBHandler.CO_CESSPer));
                        CESSAmt = custOrderRes.getFloat(custOrderRes.getColumnIndex(DBHandler.CO_CESSAmt));
                        discPer = custOrderRes.getFloat(custOrderRes.getColumnIndex(DBHandler.CO_DiscPer));
                        discAmt = custOrderRes.getFloat(custOrderRes.getColumnIndex(DBHandler.CO_DiscAmt));
                        totDisc = totDisc + discAmt;

                        /*totQty = custOrderRes.getFloat(custOrderRes.getColumnIndex(DBHandler.CO_Qty));
                        totLooseQty = custOrderRes.getFloat(custOrderRes.getColumnIndex(DBHandler.CO_LooseQty));
                        totAmt = custOrderRes.getFloat(custOrderRes.getColumnIndex(DBHandler.CO_Amount));
                        totGSTAmt = custOrderRes.getFloat(custOrderRes.getColumnIndex(DBHandler.CO_GSTAmt));
                        totTotalAmt = custOrderRes.getFloat(custOrderRes.getColumnIndex(DBHandler.CO_TotalAmt));
                        totNetAmt = custOrderRes.getFloat(custOrderRes.getColumnIndex(DBHandler.CO_NetAmt));
                        totAmtAfterDisc = custOrderRes.getFloat(custOrderRes.getColumnIndex(DBHandler.CO_AmtAfterDisc));*/

                        data = data + prodId + "^" + sizeGroup + "^" + requiredSize + "^" + perPackQty + "^" + color + "^" + rate + "^" + mrp + "^" + qty
                                + "^" + looseQty + "^" + actLooseQty + "^" + amount + "^" + loosePackTyp + "^" + gstAmt + "^" + totalAmt
                                + "^" + pendingLooseQty + "^" + gstPer + "^" + CGSTAmt + "^" + SGSTAmt + "^" + IGSTAmt + "^" + CGSTPer + "^" + SGSTPer
                                + "^" + CessPer + "^" + CESSAmt + ",";

                        Constant.showLog(data);

                    } while (custOrderRes.moveToNext());

                }
                custOrderRes.close();
                if (!data.equals("")) {

                    data = data.substring(0, data.length() - 1);
                    remark = URLEncoder.encode(remark, "UTF-8");
                    groupNm = URLEncoder.encode(groupNm, "UTF-8");
                    data = URLEncoder.encode(data, "UTF-8");

                    //TODO : Change createBy At SaleExecutive Module, Change SalesExe,transporter,ItemVat value

                    String url = Constant.ipaddress + "/SaveCustOrderMast?branchid=" + hoCode + "&CustId=" + custId + "&SalesExe=" + saleExe + "&TotalQty=" + totQty
                            + "&LooseQty=" + totLooseQty + "&Amount=" + totAmt + "&Vat=" + totGSTAmt + "&Total=" + totTotalAmt + "&OtherAddLess=0&NetAmt=" + totNetAmt
                            + "&createby=" + custId + "&Remark=" + remark + "&Transporter=1&ItemVat=0&GroupNm=" + groupNm + "&discPer=" + custDiscPer
                            + "&discAmt=" + totDisc + "&AmtAfterDisc=" + totAmtAfterDisc + "&DispatchCmp=" + dispatchCmp + "&data=" + data;

                    Constant.showLog(url);
                    //urlList.add(url);
                    constant.showPD();
                    new saveOrderAsyncTask(branchId).execute(url);
                }else{
                    toast.setText("Something Went Wrong");
                    toast.show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                toast.setText("Something Went Wrong");
                toast.show();
            }
            //}
            //counter = 0;
            //constant.showPD();
            //saveCustOrder();
        }
    }

    private void saveCustOrder(){
        if(urlList.size()!=0){
            if(counter<urlList.size()) {
                new saveOrderAsyncTask(0).execute(urlList.get(counter));
            }else{
                constant.showPD();
                showDia(3);
            }
        }else{
            constant.showPD();
        }
    }

    private class saveOrderAsyncTask extends AsyncTask<String,Void,String>{
        int branchId = 0;

        public saveOrderAsyncTask(int _branchId){
            this.branchId = _branchId;
        }

        @Override
        protected String doInBackground(String... url) {
            return Post.POST(url[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                Constant.showLog(result);
                String str = new JSONObject(result).getString("SaveCustOrderMasterResult");
                str = str.replace("\"","");
                Constant.showLog(str);
                constant.showPD();
                String[] retAutoBranchId = str.split("\\-");
                if(retAutoBranchId.length>1) {
                    if(!retAutoBranchId[0].equals("0")) {
                        if (retAutoBranchId[1].equals(String.valueOf(branchId))) {
                            db.deleteOrderTableAfterSave(branchId);
                            if(counter==1){
                                showDia(3);
                            }else {
                                getSaveOrderData();
                            }
                        } else {
                            showDia(4);
                        }
                    }else {
                        showDia(4);
                    }
                }else {
                    showDia(4);
                }
                //counter++;
                //saveCustOrder();
            }catch (Exception e){
                e.printStackTrace();
                showDia(4);
                constant.showPD();
            }
        }
    }

    private void showDia(int a) {
        AlertDialog.Builder builder = new AlertDialog.Builder(CheckoutCustOrderActivity.this);
        builder.setCancelable(false);
        if (a == 0) {
            builder.setMessage("Do You Want To Exit App?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    new Constant(CheckoutCustOrderActivity.this).doFinish();
                    dialog.dismiss();
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        }else if (a == 1) {
            builder.setMessage("Do You Want To Update Order");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    AddToCartActivity.activityToFrom = 2;
                    new Constant(CheckoutCustOrderActivity.this).doFinish();
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
            builder.setMessage("Error While Loading Data");
            builder.setPositiveButton("Try Again", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    checkStock();
                    dialog.dismiss();
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        }else if (a == 3) {
            builder.setMessage("Order Saved Successfully");
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    new Constant(CheckoutCustOrderActivity.this).doFinish();
                    dialog.dismiss();
                }
            });
        }else if (a == 4) {
            builder.setMessage("Error While Saving Order");
            builder.setPositiveButton("Try Again", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //saveCustOrder();
                    getSaveOrderData();
                    dialog.dismiss();
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        }
        builder.create().show();
    }

    private void writeLog(String _data) {
        new WriteLog().writeLog(getApplicationContext(), "CheckoutCustOrderActivity_" + _data);
    }
}
