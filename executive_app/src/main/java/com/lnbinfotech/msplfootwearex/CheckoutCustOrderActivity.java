package com.lnbinfotech.msplfootwearex;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lnbinfotech.msplfootwearex.adapters.CheckoutCustOrderAdapter;
import com.lnbinfotech.msplfootwearex.connectivity.ConnectivityTest;
import com.lnbinfotech.msplfootwearex.constant.Constant;
import com.lnbinfotech.msplfootwearex.db.DBHandler;
import com.lnbinfotech.msplfootwearex.interfaces.ServerCallbackList;
import com.lnbinfotech.msplfootwearex.log.WriteLog;
import com.lnbinfotech.msplfootwearex.model.CheckoutCustOrderClass;
import com.lnbinfotech.msplfootwearex.model.CustomerOrderClass;
import com.lnbinfotech.msplfootwearex.post.Post;
import com.lnbinfotech.msplfootwearex.volleyrequests.VolleyRequests;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class CheckoutCustOrderActivity extends AppCompatActivity implements View.OnClickListener {

    private Constant constant, constant1;
    private Toast toast;

    private ListView lv_check;
    private Button btn_save;
    private TextView tv_tot_looseQty, tv_tot_availQty, tv_custname;

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
            if (from.equals("addtocard")) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }
        }

        checkStock();

        btn_save.setOnClickListener(this);

        lv_check.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (from.equals("addtocard")) {
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
                if (ConnectivityTest.getNetStat(getApplicationContext())) {
                    getSaveOrderData();
                } else {
                    toast.setText("You Are Offline");
                    toast.show();
                }
                //new saveOrderAsyncTask(1).execute("");
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

    @Override
    protected void onResume() {
        super.onResume();
        //constant = new Constant(CheckoutCustOrderActivity.this);
    }

    private void init() {
        constant = new Constant(CheckoutCustOrderActivity.this);
        constant1 = new Constant(getApplicationContext());
        toast = Toast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        lv_check = (ListView) findViewById(R.id.lv_check);
        tv_tot_looseQty = (TextView) findViewById(R.id.tv_tot_looseQty);
        tv_tot_availQty = (TextView) findViewById(R.id.tv_tot_availQty);
        tv_custname = (TextView) findViewById(R.id.tv_custname1);
        tv_custname.setText(FirstActivity.pref.getString(getString(R.string.pref_selcustname), ""));
        btn_save = (Button) findViewById(R.id.btn_save);
        list = new ArrayList<>();
        urlList = new ArrayList<>();
        db = new DBHandler(getApplicationContext());
    }

    private void checkStock() {
        String data = "";
        Cursor res = new DBHandler(getApplicationContext()).getDataToCheck();
        if (res.moveToFirst()) {
            do {
                int branchId = res.getInt(res.getColumnIndex(DBHandler.CO_BranchId));
                int prodId = res.getInt(res.getColumnIndex(DBHandler.CO_Productid));
                String size = res.getString(res.getColumnIndex(DBHandler.CO_SizeGroup));
                String color = res.getString(res.getColumnIndex(DBHandler.CO_Color));
                String hashcode = res.getString(res.getColumnIndex(DBHandler.CO_HashCode));
                String rate = res.getString(res.getColumnIndex(DBHandler.CO_MRP));
                String looseqty = res.getString(res.getColumnIndex(DBHandler.CO_LooseQty));
                String otype = res.getString(res.getColumnIndex(DBHandler.CO_OrderType));
                String str = branchId + "^" + prodId + "^" + size + "^" + color + "^" + hashcode + "^" + rate + "^" +looseqty+ "^" + otype + ",";
                data = data + str;
            } while (res.moveToNext());
        }
        res.close();
        if (!data.equals("")) {
            data = data.substring(0, data.length() - 1);
        }
        Constant.showLog(data);
        //Constant.showLog(data);
        //showCheckoutOrderDetails(data);
        new showCheckoutOrderDetailsClass(0).execute(data);
    }

    private void showCheckoutOrderDetails(String data) {
        if (ConnectivityTest.getNetStat(CheckoutCustOrderActivity.this)) {
            try {
                data = URLEncoder.encode(data, "UTF-8");
                Constant.showLog(data);
                String url = Constant.ipaddress + "/CheckStock?data=" + data;
                Constant.showLog(url);
                writeLog("showCheckoutOrderDetails_" + url);
                constant.showPD();
                Constant.showLog("1");
                VolleyRequests requests = new VolleyRequests(CheckoutCustOrderActivity.this);
                requests.loadCheckoutOrder(url, new ServerCallbackList() {
                    @Override
                    public void onSuccess(Object result) {
                        constant.showPD();
                        Constant.showLog("2");
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
                        writeLog("showCheckoutOrderDetails_onFailure_" + result);
                        showDia(2);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                writeLog("showCheckoutOrderDetails_" + e.getMessage());
            }
        } else {
            toast.setText("You Are Offline");
            toast.show();
        }
    }

    private class showCheckoutOrderDetailsClass extends AsyncTask<String, Void, String>{
        private int branchId = 0;
        private ProgressDialog pd;

        public showCheckoutOrderDetailsClass(int _branchId) {
            this.branchId = _branchId;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(CheckoutCustOrderActivity.this);
            pd.setMessage("Please Wait...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... url) {
            String value = "";
            HttpPost request = new HttpPost(Constant.ipaddress + "/json/checkStock");
            request.setHeader("Accept", "application/json");
            request.setHeader("Content-type", "application/json");
            try {
                JSONStringer vehicle = new JSONStringer().object().key("rData")
                        .object().key("details").value(url[0]).endObject().endObject();

                StringEntity entity = new StringEntity(vehicle.toString());
                request.setEntity(entity);

                // Send request to WCF service
                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpResponse response = httpClient.execute(request);
                Constant.showLog("Saving : " + response.getStatusLine().getStatusCode());
                value = new BasicResponseHandler().handleResponse(response);
                //return Post.POST(url[0]);
            } catch (Exception e) {
                e.printStackTrace();
                writeLog("showCheckoutOrderDetailsClass_result_" + e.getMessage());
            }
            return value;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Constant.showLog(result);
            pd.dismiss();
            List<CheckoutCustOrderClass> list = new ArrayList<>();
            try{
                JSONArray jsonArray = new JSONArray(new JSONObject(result).get("CheckStockFinalResult").toString());
                if (jsonArray.length() >= 1) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        CheckoutCustOrderClass checkout = new CheckoutCustOrderClass();
                        checkout.setBranchId(jsonArray.getJSONObject(i).getString("Branch_id"));
                        checkout.setProductId(jsonArray.getJSONObject(i).getString("Product_id"));
                        checkout.setColor(jsonArray.getJSONObject(i).getString("Color"));
                        checkout.setSizeGroup(jsonArray.getJSONObject(i).getString("SizeGroup"));
                        checkout.setAvailableQty(jsonArray.getJSONObject(i).getString("AvailQty"));
                        checkout.setRate(jsonArray.getJSONObject(i).getString("Rate"));
                        checkout.setHashCode(jsonArray.getJSONObject(i).getString("HashCode"));
                        checkout.setEnterQty(jsonArray.getJSONObject(i).getString("EnterQty"));
                        list.add(checkout);
                    }
                    db.updateAvailQty(list);
                    setData();
                }else{
                    showDia(2);
                }
            }catch (Exception e){
                e.printStackTrace();
                writeLog("showCheckoutOrderDetailsClass_" + e.getMessage());
                showDia(2);
            }
        }
    }

    private void setData() {
        int totLooseQty = 0, totAvailQty = 0;
        list.clear();
        Cursor cursor = new DBHandler(getApplicationContext()).getViewOrderData(1, "");
        if (cursor.moveToFirst()) {
            do {
                CustomerOrderClass order = new CustomerOrderClass();
                order.setAuto(cursor.getInt(cursor.getColumnIndex(DBHandler.CO_Auto)));
                order.setProductid(cursor.getInt(cursor.getColumnIndex(DBHandler.CO_Productid)));
                order.setSizeGroup(cursor.getString(cursor.getColumnIndex(DBHandler.CO_SizeGroup)));
                order.setColor(cursor.getString(cursor.getColumnIndex(DBHandler.CO_Color)));
                order.setHashCode(cursor.getString(cursor.getColumnIndex(DBHandler.CO_HashCode)));
                order.setProdId(cursor.getString(cursor.getColumnIndex(DBHandler.CO_Prodid)));

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
            } while (cursor.moveToNext());
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
            if (res.getCount() == 1) {
                counter = 1;
            }
        }
        res.close();

        int hoCode = FirstActivity.pref.getInt(getString(R.string.pref_hocode), 0);
        int custId = FirstActivity.pref.getInt(getString(R.string.pref_selcustid), 0);
        int saleExe = FirstActivity.pref.getInt(getString(R.string.pref_retailCustId), 0);

        //for(int branchId:branchIdList){
        //db.getCustOrderMaster(branchId);
        if (branchIdList.size() != 0) {
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

                        data = data + prodId + "^" + sizeGroup + "^" + requiredSize + "^" + perPackQty + "^" + color + "^" + rate + "^" + mrp + "^" + qty + "^" + looseQty + "^" + actLooseQty + "^" + amount + "^" + loosePackTyp + "^" + gstAmt + "^" + totalAmt + "^" + pendingLooseQty + "^" + gstPer + "^" + CGSTAmt + "^" + SGSTAmt + "^" + IGSTAmt + "^" + CGSTPer + "^" + SGSTPer
                                + "^" + CessPer + "^" + CESSAmt + ",";

                        Constant.showLog(data);
                    } while (custOrderRes.moveToNext());
                }
                custOrderRes.close();
                if (!data.equals("")) {

                    data = data.substring(0, data.length() - 1);
                    remark = URLEncoder.encode(remark, "UTF-8");
                    groupNm = URLEncoder.encode(groupNm, "UTF-8");
                    //data = URLEncoder.encode(data, "UTF-8");

                    /*String url = Constant.ipaddress + "/SaveCustOrderMast?branchid=" + hoCode + "&CustId=" + custId + "&SalesExe=" + saleExe + "&TotalQty=" + totQty
                            + "&LooseQty=" + totLooseQty + "&Amount=" + totAmt + "&Vat=" + totGSTAmt + "&Total=" + totTotalAmt + "&OtherAddLess=0&NetAmt=" + totNetAmt
                            + "&createby=" + saleExe + "&Remark=" + remark + "&Transporter=1&ItemVat=0&GroupNm=" + groupNm + "&discPer=" + custDiscPer
                            + "&discAmt=" + totDisc + "&AmtAfterDisc=" + totAmtAfterDisc + "&DispatchCmp=" + dispatchCmp + "&data=" + data;*/

                    String url = hoCode + "|" + custId + "|" + saleExe + "|" + totQty + "|" + totLooseQty + "|" + totAmt + "|" + totGSTAmt + "|" + totTotalAmt + "|0|" + totNetAmt + "|" + saleExe + "|" + remark + "|1|0|" + groupNm + "|" + custDiscPer + "|" + totDisc + "|" + totAmtAfterDisc + "|" + dispatchCmp + "|" + data;
                    Constant.showLog(url);
                    //urlList.add(url);
                    //constant.showPD();
                    writeLog("getSaveOrderData_" + url);
                    new saveOrderAsyncTask(branchId).execute(url);
                } else {
                    toast.setText("Something Went Wrong");
                    toast.show();
                }
            } catch (Exception e) {
                writeLog("getSaveOrderData_" + e.getMessage());
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

    private void saveCustOrder() {
        if (urlList.size() != 0) {
            if (counter < urlList.size()) {
                new saveOrderAsyncTask(0).execute(urlList.get(counter));
            } else {
                constant.showPD();
                Constant.showLog("3");
                showDia(3);
            }
        } else {
            constant.showPD();
            Constant.showLog("4");
        }
    }

    private class saveOrderAsyncTask extends AsyncTask<String, Void, String> {
        private int branchId = 0;
        private ProgressDialog pd;

        public saveOrderAsyncTask(int _branchId) {
            this.branchId = _branchId;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(CheckoutCustOrderActivity.this);
            pd.setMessage("Please Wait...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... url) {
            String value = "";
            HttpPost request = new HttpPost(Constant.ipaddress + "/json/saveOrder");
            request.setHeader("Accept", "application/json");
            request.setHeader("Content-type", "application/json");
            try {
                /*JSONStringer vehicle = new JSONStringer()
                    .object()
                    .key("rData")
                    .object()
                    .key("details").value("bar|bob|b@h.us|why")
                    .endObject()
                    .endObject();*/

                JSONStringer vehicle = new JSONStringer()
                        .object()
                        .key("rData")
                        .object()
                        .key("details").value(url[0])
                        .endObject()
                        .endObject();

                StringEntity entity = new StringEntity(vehicle.toString());
                request.setEntity(entity);

                // Send request to WCF service
                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpResponse response = httpClient.execute(request);
                Constant.showLog("Saving : " + response.getStatusLine().getStatusCode());
                value = new BasicResponseHandler().handleResponse(response);
                //return Post.POST(url[0]);
            } catch (Exception e) {
                e.printStackTrace();
                writeLog("saveOrderAsyncTask_result_" + e.getMessage());
            }
            return value;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                Constant.showLog(result);
                //String str = new JSONObject(result).getString("SaveCustOrderMasterResult");
                String str = new JSONObject(result).getString("SaveCustOrderResult");
                str = str.replace("\"", "");
                Constant.showLog(str);
                pd.dismiss();
                writeLog("saveOrderAsyncTask_result_" + str + "_" + result);
                String[] retAutoBranchId = str.split("\\-");
                if (retAutoBranchId.length > 1) {
                    if (!retAutoBranchId[0].equals("0")) {
                        if (retAutoBranchId[1].equals(String.valueOf(branchId))) {
                            db.deleteOrderTableAfterSave(branchId);
                            if (counter == 1) {
                                showDia(3);
                            } else {
                                getSaveOrderData();
                            }
                        } else {
                            showDia(4);
                        }
                    } else {
                        showDia(4);
                    }
                } else {
                    showDia(4);
                }
                //counter++;
                //saveCustOrder();
            } catch (Exception e) {
                writeLog("saveOrderAsyncTask_" + e.getMessage());
                e.printStackTrace();
                showDia(4);
                pd.dismiss();
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
        } else if (a == 1) {
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
        } else if (a == 2) {
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
        } else if (a == 3) {
            builder.setMessage("Order Saved Successfully");
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    SharedPreferences.Editor editor = FirstActivity.pref.edit();
                    editor.putInt(getString(R.string.pref_selcustid), 0);
                    editor.putString(getString(R.string.pref_selcustname), "");
                    editor.apply();
                    dialog.dismiss();
                    checkLimit();
                }
            });
        } else if (a == 4) {
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
        } else if (a == 5) {
            builder.setTitle("Payment");
            builder.setMessage("Please Make Payment To Process Order Successful");
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    doFinish();
                }
            });
        }
        builder.create().show();
    }

    private void checkLimit() {
        String currOrder = FirstActivity.pref.getString("totalNetAmnt", "0");
        float netAmt = Float.parseFloat(currOrder);
        float creditLimit = Float.parseFloat(DisplayCustOutstandingActivity.outClass.getCreditlimit());
        if (netAmt > creditLimit) {
            showDia(5);
        } else {
            doFinish();
        }
    }

    private void doFinish() {
        SharedPreferences.Editor editor = FirstActivity.pref.edit();
        editor.putString("totalNetAmnt", "0");
        editor.apply();
        AddToCartActivity.activityToFrom = 0;
        new Constant(CheckoutCustOrderActivity.this).doFinish();
    }

    private void writeLog(String _data) {
        new WriteLog().writeLog(getApplicationContext(), "CheckoutCustOrderActivity_" + _data);
    }

    private void postTest() {
        HttpPost request = new HttpPost(Constant.ipaddress + "/json/adduser");
        request.setHeader("Accept", "application/json");
        request.setHeader("Content-type", "application/json");

        String not = "";
        try {
            String str = "1|4701|28|171|171|40024.31|2273.3308|42297.65|0|42297.65|28|NA|1|0|NA|0.0|0.0|40024.31|4|1109^7^7^1^Black^332.65^499.0^1^1^1^332.65^Unpack^16.63^349.28^1^5.0^8.32^8.32^0.0^2.5^2.5^0.0^0.0,1109^7^7^1^Brown^332.65^499.0^1^1^1^332.65^Unpack^16.63^349.28^1^5.0^8.32^8.32^0.0^2.5^2.5^0.0^0.0,1109^9^9^1^Black^332.65^499.0^1^1^1^332.65^Unpack^16.63^349.28^1^5.0^8.32^8.32^0.0^2.5^2.5^0.0^0.0,1109^9^9^1^Brown^332.65^499.0^1^1^1^332.65^Unpack^16.63^349.28^1^5.0^8.32^8.32^0.0^2.5^2.5^0.0^0.0,1109^8^8^1^Black^332.65^499.0^1^1^1^332.65^Unpack^16.63^349.28^1^5.0^8.32^8.32^0.0^2.5^2.5^0.0^0.0,1109^8^8^1^Brown^332.65^499.0^1^1^1^332.65^Unpack^16.63^349.28^1^5.0^8.32^8.32^0.0^2.5^2.5^0.0^0.0,1109^10^10^1^Black^332.65^499.0^1^1^1^332.65^Unpack^16.63^349.28^1^5.0^8.32^8.32^0.0^2.5^2.5^0.0^0.0,1109^10^10^1^Brown^332.65^499.0^1^1^1^332.65^Unpack^16.63^349.28^1^5.0^8.32^8.32^0.0^2.5^2.5^0.0^0.0,1063^7^7^1^Black^332.65^499.0^1^1^1^332.65^Unpack^16.63^349.28^1^5.0^8.32^8.32^0.0^2.5^2.5^0.0^0.0,1063^7^7^1^Brown^332.65^499.0^1^1^1^332.65^Unpack^16.63^349.28^1^5.0^8.32^8.32^0.0^2.5^2.5^0.0^0.0,1063^9^9^1^Black^332.65^499.0^1^1^1^332.65^Unpack^16.63^349.28^1^5.0^8.32^8.32^0.0^2.5^2.5^0.0^0.0,1063^9^9^1^Brown^332.65^499.0^1^1^1^332.65^Unpack^16.63^349.28^1^5.0^8.32^8.32^0.0^2.5^2.5^0.0^0.0,1063^8^8^1^Black^332.65^499.0^1^1^1^332.65^Unpack^16.63^349.28^1^5.0^8.32^8.32^0.0^2.5^2.5^0.0^0.0,1063^8^8^1^Brown^332.65^499.0^1^1^1^332.65^Unpack^16.63^349.28^1^5.0^8.32^8.32^0.0^2.5^2.5^0.0^0.0,1063^10^10^1^Black^332.65^499.0^1^1^1^332.65^Unpack^16.63^349.28^1^5.0^8.32^8.32^0.0^2.5^2.5^0.0^0.0,1063^10^10^1^Brown^332.65^499.0^1^1^1^332.65^Unpack^16.63^349.28^1^5.0^8.32^8.32^0.0^2.5^2.5^0.0^0.0,977^8^8^1^Brown^199.4^299.0^1^1^1^199.4^Unpack^9.97^209.37^1^5.0^4.99^4.99^0.0^2.5^2.5^0.0^0.0,977^8^8^1^Black^199.4^299.0^1^1^1^199.4^Unpack^9.97^209.37^1^5.0^4.99^4.99^0.0^2.5^2.5^0.0^0.0,977^10^10^1^Brown^199.4^299.0^1^1^1^199.4^Unpack^9.97^209.37^1^5.0^4.99^4.99^0.0^2.5^2.5^0.0^0.0,977^10^10^1^Black^199.4^299.0^1^1^1^199.4^Unpack^9.97^209.37^1^5.0^4.99^4.99^0.0^2.5^2.5^0.0^0.0,977^6^6^1^Brown^199.4^299.0^1^1^1^199.4^Unpack^9.97^209.37^1^5.0^4.99^4.99^0.0^2.5^2.5^0.0^0.0,977^6^6^1^Black^199.4^299.0^1^1^1^199.4^Unpack^9.97^209.37^1^5.0^4.99^4.99^0.0^2.5^2.5^0.0^0.0,977^7^7^1^Brown^199.4^299.0^2^2^2^398.8^Unpack^19.94^418.74^2^5.0^9.97^9.97^0.0^2.5^2.5^0.0^0.0,977^7^7^1^Black^199.4^299.0^2^2^2^398.8^Unpack^19.94^418.74^2^5.0^9.97^9.97^0.0^2.5^2.5^0.0^0.0,977^9^9^1^Brown^199.4^299.0^1^1^1^199.4^Unpack^9.97^209.37^1^5.0^4.99^4.99^0.0^2.5^2.5^0.0^0.0,977^9^9^1^Black^199.4^299.0^1^1^1^199.4^Unpack^9.97^209.37^1^5.0^4.99^4.99^0.0^2.5^2.5^0.0^0.0,1059^8^8^1^Black^182.6^299.0^1^1^1^182.6^Unpack^9.13^191.73^1^5.0^4.57^4.57^0.0^2.5^2.5^0.0^0.0,1059^6^6^1^Black^182.6^299.0^1^1^1^182.6^Unpack^9.13^191.73^1^5.0^4.57^4.57^0.0^2.5^2.5^0.0^0.0,1059^8^8^1^Brown^182.6^299.0^1^1^1^182.6^Unpack^9.13^191.73^1^5.0^4.57^4.57^0.0^2.5^2.5^0.0^0.0,1059^10^10^1^Brown^182.6^299.0^1^1^1^182.6^Unpack^9.13^191.73^1^5.0^4.57^4.57^0.0^2.5^2.5^0.0^0.0,1059^6^6^1^Brown^182.6^299.0^1^1^1^182.6^Unpack^9.13^191.73^1^5.0^4.57^4.57^0.0^2.5^2.5^0.0^0.0,1059^7^7^1^Brown^182.6^299.0^2^2^2^365.2^Unpack^18.26^383.46^2^5.0^9.13^9.13^0.0^2.5^2.5^0.0^0.0,1059^9^9^1^Brown^182.6^299.0^1^1^1^182.6^Unpack^9.13^191.73^1^5.0^4.57^4.57^0.0^2.5^2.5^0.0^0.0,1009^7^7^1^Black^332.65^499.0^1^1^1^332.65^Unpack^16.63^349.28^1^5.0^8.32^8.32^0.0^2.5^2.5^0.0^0.0,1009^9^9^1^Black^332.65^499.0^1^1^1^332.65^Unpack^16.63^349.28^1^5.0^8.32^8.32^0.0^2.5^2.5^0.0^0.0,1009^8^8^1^Black^332.65^499.0^1^1^1^332.65^Unpack^16.63^349.28^1^5.0^8.32^8.32^0.0^2.5^2.5^0.0^0.0,1009^10^10^1^Black^332.65^499.0^1^1^1^332.65^Unpack^16.63^349.28^1^5.0^8.32^8.32^0.0^2.5^2.5^0.0^0.0,1114^7^7^1^Black^332.65^499.0^1^1^1^332.65^Unpack^16.63^349.28^1^5.0^8.32^8.32^0.0^2.5^2.5^0.0^0.0,1114^7^7^1^Brown^332.65^499.0^1^1^1^332.65^Unpack^16.63^349.28^1^5.0^8.32^8.32^0.0^2.5^2.5^0.0^0.0,1114^9^9^1^Black^332.65^499.0^1^1^1^332.65^Unpack^16.63^349.28^1^5.0^8.32^8.32^0.0^2.5^2.5^0.0^0.0,1114^9^9^1^Brown^332.65^499.0^1^1^1^332.65^Unpack^16.63^349.28^1^5.0^8.32^8.32^0.0^2.5^2.5^0.0^0.0,1114^8^8^1^Black^332.65^499.0^1^1^1^332.65^Unpack^16.63^349.28^1^5.0^8.32^8.32^0.0^2.5^2.5^0.0^0.0,1114^8^8^1^Brown^332.65^499.0^1^1^1^332.65^Unpack^16.63^349.28^1^5.0^8.32^8.32^0.0^2.5^2.5^0.0^0.0,1114^10^10^1^Black^332.65^499.0^1^1^1^332.65^Unpack^16.63^349.28^1^5.0^8.32^8.32^0.0^2.5^2.5^0.0^0.0,1114^10^10^1^Brown^332.65^499.0^1^1^1^332.65^Unpack^16.63^349.28^1^5.0^8.32^8.32^0.0^2.5^2.5^0.0^0.0,983^7^7^1^Black^332.65^499.0^1^1^1^332.65^Unpack^16.63^349.28^1^5.0^8.32^8.32^0.0^2.5^2.5^0.0^0.0,983^9^9^1^Black^332.65^499.0^1^1^1^332.65^Unpack^16.63^349.28^1^5.0^8.32^8.32^0.0^2.5^2.5^0.0^0.0,983^8^8^1^Black^332.65^499.0^1^1^1^332.65^Unpack^16.63^349.28^1^5.0^8.32^8.32^0.0^2.5^2.5^0.0^0.0,983^10^10^1^Black^332.65^499.0^1^1^1^332.65^Unpack^16.63^349.28^1^5.0^8.32^8.32^0.0^2.5^2.5^0.0^0.0,1053^7^7^1^Black^332.65^499.0^1^1^1^332.65^Unpack^16.63^349.28^1^5.0^8.32^8.32^0.0^2.5^2.5^0.0^0.0,1053^7^7^1^Brown^332.65^499.0^1^1^1^332.65^Unpack^16.63^349.28^1^5.0^8.32^8.32^0.0^2.5^2.5^0.0^0.0,1053^9^9^1^Black^332.65^499.0^1^1^1^332.65^Unpack^16.63^349.28^1^5.0^8.32^8.32^0.0^2.5^2.5^0.0^0.0,1053^9^9^1^Brown^332.65^499.0^1^1^1^332.65^Unpack^16.63^349.28^1^5.0^8.32^8.32^0.0^2.5^2.5^0.0^0.0,1053^8^8^1^Black^332.65^499.0^1^1^1^332.65^Unpack^16.63^349.28^1^5.0^8.32^8.32^0.0^2.5^2.5^0.0^0.0,1053^8^8^1^Brown^332.65^499.0^1^1^1^332.65^Unpack^16.63^349.28^1^5.0^8.32^8.32^0.0^2.5^2.5^0.0^0.0,1053^10^10^1^Black^332.65^499.0^1^1^1^332.65^Unpack^16.63^349.28^1^5.0^8.32^8.32^0.0^2.5^2.5^0.0^0.0,1053^10^10^1^Brown^332.65^499.0^1^1^1^332.65^Unpack^16.63^349.28^1^5.0^8.32^8.32^0.0^2.5^2.5^0.0^0.0,80^8^8^1^Brown^185.95^279.0^2^2^2^371.9^Unpack^18.6^390.5^2^5.0^9.3^9.3^0.0^2.5^2.5^0.0^0.0,80^8^8^1^Black^185.95^279.0^2^2^2^371.9^Unpack^18.6^390.5^2^5.0^9.3^9.3^0.0^2.5^2.5^0.0^0.0,80^10^10^1^Brown^185.95^279.0^1^1^1^185.95^Unpack^9.3^195.25^1^5.0^4.65^4.65^0.0^2.5^2.5^0.0^0.0,80^10^10^1^Black^185.95^279.0^1^1^1^185.95^Unpack^9.3^195.25^1^5.0^4.65^4.65^0.0^2.5^2.5^0.0^0.0,80^6^6^1^Brown^185.95^279.0^1^1^1^185.95^Unpack^9.3^195.25^1^5.0^4.65^4.65^0.0^2.5^2.5^0.0^0.0,80^6^6^1^Black^185.95^279.0^1^1^1^185.95^Unpack^9.3^195.25^1^5.0^4.65^4.65^0.0^2.5^2.5^0.0^0.0,80^7^7^1^Brown^185.95^279.0^1^1^1^185.95^Unpack^9.3^195.25^1^5.0^4.65^4.65^0.0^2.5^2.5^0.0^0.0,80^7^7^1^Black^185.95^279.0^1^1^1^185.95^Unpack^9.3^195.25^1^5.0^4.65^4.65^0.0^2.5^2.5^0.0^0.0,80^9^9^1^Brown^185.95^279.0^1^1^1^185.95^Unpack^9.3^195.25^1^5.0^4.65^4.65^0.0^2.5^2.5^0.0^0.0,80^9^9^1^Black^185.95^279.0^1^1^1^185.95^Unpack^9.3^195.25^1^5.0^4.65^4.65^0.0^2.5^2.5^0.0^0.0,1157^8^8^1^Brown^152.05^249.0^1^1^1^152.05^Unpack^7.6^159.65^1^5.0^3.8^3.8^0.0^2.5^2.5^0.0^0.0,1157^8^8^1^Black^152.05^249.0^1^1^1^152.05^Unpack^7.6^159.65^1^5.0^3.8^3.8^0.0^2.5^2.5^0.0^0.0,1157^10^10^1^Brown^152.05^249.0^1^1^1^152.05^Unpack^7.6^159.65^1^5.0^3.8^3.8^0.0^2.5^2.5^0.0^0.0,1157^10^10^1^Black^152.05^249.0^1^1^1^152.05^Unpack^7.6^159.65^1^5.0^3.8^3.8^0.0^2.5^2.5^0.0^0.0,1157^6^6^1^Brown^152.05^249.0^1^1^1^152.05^Unpack^7.6^159.65^1^5.0^3.8^3.8^0.0^2.5^2.5^0.0^0.0,1157^6^6^1^Black^152.05^249.0^1^1^1^152.05^Unpack^7.6^159.65^1^5.0^3.8^3.8^0.0^2.5^2.5^0.0^0.0,1157^7^7^1^Brown^152.05^249.0^2^2^2^304.1^Unpack^15.21^319.31^2^5.0^7.6^7.6^0.0^2.5^2.5^0.0^0.0,1157^7^7^1^Black^152.05^249.0^2^2^2^304.1^Unpack^15.21^319.31^2^5.0^7.6^7.6^0.0^2.5^2.5^0.0^0.0,1157^9^9^1^Brown^152.05^249.0^1^1^1^152.05^Unpack^7.6^159.65^1^5.0^3.8^3.8^0.0^2.5^2.5^0.0^0.0,1157^9^9^1^Black^152.05^249.0^1^1^1^152.05^Unpack^7.6^159.65^1^5.0^3.8^3.8^0.0^2.5^2.5^0.0^0.0,1120^8^8^1^Tan^182.6^299.0^1^1^1^182.6^Unpack^9.13^191.73^1^5.0^4.57^4.57^0.0^2.5^2.5^0.0^0.0,1120^6^6^1^Tan^182.6^299.0^1^1^1^182.6^Unpack^9.13^191.73^1^5.0^4.57^4.57^0.0^2.5^2.5^0.0^0.0,1120^7^7^1^Tan^182.6^299.0^2^2^2^365.2^Unpack^18.26^383.46^2^5.0^9.13^9.13^0.0^2.5^2.5^0.0^0.0,1120^9^9^1^Tan^182.6^299.0^1^1^1^182.6^Unpack^9.13^191.73^1^5.0^4.57^4.57^0.0^2.5^2.5^0.0^0.0,1025^8^8^1^Brown^188.75^309.0^1^1^1^188.75^Unpack^9.44^198.19^1^5.0^4.72^4.72^0.0^2.5^2.5^0.0^0.0,1025^8^8^1^Black^188.75^309.0^1^1^1^188.75^Unpack^9.44^198.19^1^5.0^4.72^4.72^0.0^2.5^2.5^0.0^0.0,1025^10^10^1^Brown^188.75^309.0^1^1^1^188.75^Unpack^9.44^198.19^1^5.0^4.72^4.72^0.0^2.5^2.5^0.0^0.0,1025^10^10^1^Black^188.75^309.0^1^1^1^188.75^Unpack^9.44^198.19^1^5.0^4.72^4.72^0.0^2.5^2.5^0.0^0.0,1025^6^6^1^Brown^188.75^309.0^1^1^1^188.75^Unpack^9.44^198.19^1^5.0^4.72^4.72^0.0^2.5^2.5^0.0^0.0,1025^6^6^1^Black^188.75^309.0^1^1^1^188.75^Unpack^9.44^198.19^1^5.0^4.72^4.72^0.0^2.5^2.5^0.0^0.0,1025^7^7^1^Brown^188.75^309.0^2^2^2^377.5^Unpack^18.88^396.38^2^5.0^9.44^9.44^0.0^2.5^2.5^0.0^0.0,1025^7^7^1^Black^188.75^309.0^2^2^2^377.5^Unpack^18.88^396.38^2^5.0^9.44^9.44^0.0^2.5^2.5^0.0^0.0,1025^9^9^1^Brown^188.75^309.0^1^1^1^188.75^Unpack^9.44^198.19^1^5.0^4.72^4.72^0.0^2.5^2.5^0.0^0.0,1025^9^9^1^Black^188.75^309.0^1^1^1^188.75^Unpack^9.44^198.19^1^5.0^4.72^4.72^0.0^2.5^2.5^0.0^0.0,992^8^8^1^Tan^201.05^329.0^1^1^1^201.05^Unpack^10.05^211.1^1^5.0^5.03^5.03^0.0^2.5^2.5^0.0^0.0,992^10^10^1^Tan^201.05^329.0^1^1^1^201.05^Unpack^10.05^211.1^1^5.0^5.03^5.03^0.0^2.5^2.5^0.0^0.0,992^6^6^1^Tan^201.05^329.0^1^1^1^201.05^Unpack^10.05^211.1^1^5.0^5.03^5.03^0.0^2.5^2.5^0.0^0.0,992^7^7^1^Tan^201.05^329.0^2^2^2^402.1^Unpack^20.11^422.21^2^5.0^10.05^10.05^0.0^2.5^2.5^0.0^0.0,992^9^9^1^Tan^201.05^329.0^1^1^1^201.05^Unpack^10.05^211.1^1^5.0^5.03^5.03^0.0^2.5^2.5^0.0^0.0,476^10^10^1^Tan^226.0^339.0^1^1^1^226.0^Unpack^11.3^237.3^1^5.0^5.65^5.65^0.0^2.5^2.5^0.0^0.0,476^6^6^1^Tan^226.0^339.0^1^1^1^226.0^Unpack^11.3^237.3^1^5.0^5.65^5.65^0.0^2.5^2.5^0.0^0.0,476^7^7^1^Tan^226.0^339.0^1^1^1^226.0^Unpack^11.3^237.3^1^5.0^5.65^5.65^0.0^2.5^2.5^0.0^0.0,476^9^9^1^Tan^226.0^339.0^1^1^1^226.0^Unpack^11.3^237.3^1^5.0^5.65^5.65^0.0^2.5^2.5^0.0^0.0,1020^8^8^1^Black^201.05^329.0^1^1^1^201.05^Unpack^10.05^211.1^1^5.0^5.03^5.03^0.0^2.5^2.5^0.0^0.0,1020^10^10^1^Black^201.05^329.0^1^1^1^201.05^Unpack^10.05^211.1^1^5.0^5.03^5.03^0.0^2.5^2.5^0.0^0.0,1020^6^6^1^Black^201.05^329.0^1^1^1^201.05^Unpack^10.05^211.1^1^5.0^5.03^5.03^0.0^2.5^2.5^0.0^0.0,1020^7^7^1^Black^201.05^329.0^2^2^2^402.1^Unpack^20.11^422.21^2^5.0^10.05^10.05^0.0^2.5^2.5^0.0^0.0,1020^9^9^1^Black^201.05^329.0^1^1^1^201.05^Unpack^10.05^211.1^1^5.0^5.03^5.03^0.0^2.5^2.5^0.0^0.0,1117^8^8^1^Brown^274.15^449.0^1^1^1^274.15^Unpack^13.71^287.86^1^5.0^6.85^6.85^0.0^2.5^2.5^0.0^0.0,1117^10^10^1^Brown^274.15^449.0^1^1^1^274.15^Unpack^13.71^287.86^1^5.0^6.85^6.85^0.0^2.5^2.5^0.0^0.0,1117^6^6^1^Brown^274.15^449.0^1^1^1^274.15^Unpack^13.71^287.86^1^5.0^6.85^6.85^0.0^2.5^2.5^0.0^0.0,1117^7^7^1^Brown^274.15^449.0^2^2^2^548.3^Unpack^27.42^575.72^2^5.0^13.71^13.71^0.0^2.5^2.5^0.0^0.0,1117^9^9^1^Brown^274.15^449.0^1^1^1^274.15^Unpack^13.71^287.86^1^5.0^6.85^6.85^0.0^2.5^2.5^0.0^0.0,930^8^8^1^Green^182.6^299.0^1^1^1^182.6^Unpack^9.13^191.73^1^5.0^4.57^4.57^0.0^2.5^2.5^0.0^0.0,930^10^10^1^Green^182.6^299.0^1^1^1^182.6^Unpack^9.13^191.73^1^5.0^4.57^4.57^0.0^2.5^2.5^0.0^0.0,930^6^6^1^Green^182.6^299.0^1^1^1^182.6^Unpack^9.13^191.73^1^5.0^4.57^4.57^0.0^2.5^2.5^0.0^0.0,930^7^7^1^Green^182.6^299.0^2^2^2^365.2^Unpack^18.26^383.46^2^5.0^9.13^9.13^0.0^2.5^2.5^0.0^0.0,930^9^9^1^Green^182.6^299.0^1^1^1^182.6^Unpack^9.13^191.73^1^5.0^4.57^4.57^0.0^2.5^2.5^0.0^0.0,1177^8^8^1^Black^182.6^299.0^1^1^1^182.6^Unpack^9.13^191.73^1^5.0^4.57^4.57^0.0^2.5^2.5^0.0^0.0,1177^10^10^1^Black^182.6^299.0^1^1^1^182.6^Unpack^9.13^191.73^1^5.0^4.57^4.57^0.0^2.5^2.5^0.0^0.0,1177^6^6^1^Black^182.6^299.0^1^1^1^182.6^Unpack^9.13^191.73^1^5.0^4.57^4.57^0.0^2.5^2.5^0.0^0.0,1177^7^7^1^Black^182.6^299.0^2^2^2^365.2^Unpack^18.26^383.46^2^5.0^9.13^9.13^0.0^2.5^2.5^0.0^0.0,1177^9^9^1^Black^182.6^299.0^1^1^1^182.6^Unpack^9.13^191.73^1^5.0^4.57^4.57^0.0^2.5^2.5^0.0^0.0,1164^8^8^1^Brown^158.2^259.0^1^1^1^158.2^Unpack^7.91^166.11^1^5.0^3.96^3.96^0.0^2.5^2.5^0.0^0.0,1164^10^10^1^Brown^158.2^259.0^1^1^1^158.2^Unpack^7.91^166.11^1^5.0^3.96^3.96^0.0^2.5^2.5^0.0^0.0,1164^6^6^1^Brown^158.2^259.0^1^1^1^158.2^Unpack^7.91^166.11^1^5.0^3.96^3.96^0.0^2.5^2.5^0.0^0.0,1164^7^7^1^Brown^158.2^259.0^2^2^2^316.4^Unpack^15.82^332.22^2^5.0^7.91^7.91^0.0^2.5^2.5^0.0^0.0,1164^9^9^1^Brown^158.2^259.0^1^1^1^158.2^Unpack^7.91^166.11^1^5.0^3.96^3.96^0.0^2.5^2.5^0.0^0.0,882^8^8^1^Black^201.05^329.0^1^1^1^201.05^Unpack^10.05^211.1^1^5.0^5.03^5.03^0.0^2.5^2.5^0.0^0.0,882^10^10^1^Black^201.05^329.0^1^1^1^201.05^Unpack^10.05^211.1^1^5.0^5.03^5.03^0.0^2.5^2.5^0.0^0.0,882^6^6^1^Black^201.05^329.0^1^1^1^201.05^Unpack^10.05^211.1^1^5.0^5.03^5.03^0.0^2.5^2.5^0.0^0.0,882^7^7^1^Black^201.05^329.0^2^2^2^402.1^Unpack^20.11^422.21^2^5.0^10.05^10.05^0.0^2.5^2.5^0.0^0.0,882^9^9^1^Black^201.05^329.0^1^1^1^201.05^Unpack^10.05^211.1^1^5.0^5.03^5.03^0.0^2.5^2.5^0.0^0.0,295^8^8^1^Black^206.1^309.0^2^2^2^412.2^Unpack^20.61^432.81^2^5.0^10.31^10.31^0.0^2.5^2.5^0.0^0.0,295^10^10^1^Black^206.1^309.0^1^1^1^206.1^Unpack^10.31^216.41^1^5.0^5.15^5.15^0.0^2.5^2.5^0.0^0.0,295^6^6^1^Black^206.1^309.0^1^1^1^206.1^Unpack^10.31^216.41^1^5.0^5.15^5.15^0.0^2.5^2.5^0.0^0.0,295^7^7^1^Black^206.1^309.0^1^1^1^206.1^Unpack^10.31^216.41^1^5.0^5.15^5.15^0.0^2.5^2.5^0.0^0.0,295^9^9^1^Black^206.1^309.0^1^1^1^206.1^Unpack^10.31^216.41^1^5.0^5.15^5.15^0.0^2.5^2.5^0.0^0.0,332^8^8^1^Tan^272.75^409.0^2^2^2^545.5^Unpack^27.28^572.78^2^5.0^13.64^13.64^0.0^2.5^2.5^0.0^0.0,332^10^10^1^Tan^272.75^409.0^1^1^1^272.75^Unpack^13.64^286.39^1^5.0^6.82^6.82^0.0^2.5^2.5^0.0^0.0,332^6^6^1^Tan^272.75^409.0^1^1^1^272.75^Unpack^13.64^286.39^1^5.0^6.82^6.82^0.0^2.5^2.5^0.0^0.0,332^7^7^1^Tan^272.75^409.0^1^1^1^272.75^Unpack^13.64^286.39^1^5.0^6.82^6.82^0.0^2.5^2.5^0.0^0.0,332^9^9^1^Tan^272.75^409.0^1^1^1^272.75^Unpack^13.64^286.39^1^5.0^6.82^6.82^0.0^2.5^2.5^0.0^0.0,115^8^8^1^Tan^348.9^599.0^2^2^2^697.8^Unpack^125.6^823.4^2^18.0^62.8^62.8^0.0^9.0^9.0^0.0^0.0,115^10^10^1^Tan^348.9^599.0^1^1^1^348.9^Unpack^62.8^411.7^1^18.0^31.4^31.4^0.0^9.0^9.0^0.0^0.0,115^6^6^1^Tan^348.9^599.0^1^1^1^348.9^Unpack^62.8^411.7^1^18.0^31.4^31.4^0.0^9.0^9.0^0.0^0.0,115^7^7^1^Tan^348.9^599.0^1^1^1^348.9^Unpack^62.8^411.7^1^18.0^31.4^31.4^0.0^9.0^9.0^0.0^0.0,115^9^9^1^Tan^348.9^599.0^1^1^1^348.9^Unpack^62.8^411.7^1^18.0^31.4^31.4^0.0^9.0^9.0^0.0^0.0,82^8^8^1^Black^226.0^339.0^2^2^2^452.0^Unpack^22.6^474.6^2^5.0^11.3^11.3^0.0^2.5^2.5^0.0^0.0,82^10^10^1^Black^226.0^339.0^1^1^1^226.0^Unpack^11.3^237.3^1^5.0^5.65^5.65^0.0^2.5^2.5^0.0^0.0,82^6^6^1^Black^226.0^339.0^1^1^1^226.0^Unpack^11.3^237.3^1^5.0^5.65^5.65^0.0^2.5^2.5^0.0^0.0,82^7^7^1^Black^226.0^339.0^1^1^1^226.0^Unpack^11.3^237.3^1^5.0^5.65^5.65^0.0^2.5^2.5^0.0^0.0,82^9^9^1^Black^226.0^339.0^1^1^1^226.0^Unpack^11.3^237.3^1^5.0^5.65^5.65^0.0^2.5^2.5^0.0^0.0";

            /*JSONStringer vehicle = new JSONStringer()
                    .object()
                    .key("rData")
                    .object()
                    .key("details").value("bar|bob|b@h.us|why")
                    .endObject()
                    .endObject();*/

            JSONStringer vehicle = new JSONStringer()
                    .object()
                    .key("rData")
                    .object()
                    .key("details").value(str)
                    .endObject()
                    .endObject();

            StringEntity entity = new StringEntity(vehicle.toString());

            Toast.makeText(this, vehicle.toString() + "\n", Toast.LENGTH_LONG).show();

            request.setEntity(entity);

            // Send request to WCF service
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpResponse response = httpClient.execute(request);
            Log.d("WebInvoke", "Saving : " + response.getStatusLine().getStatusCode());


        } catch (Exception e) {
            not = "NOT ";
        }
    }

}
