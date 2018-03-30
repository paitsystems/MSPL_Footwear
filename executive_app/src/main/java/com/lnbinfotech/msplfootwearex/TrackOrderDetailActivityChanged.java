package com.lnbinfotech.msplfootwearex;

//Created by ANUP on 12/20/2017.

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lnbinfotech.msplfootwearex.adapters.DispatchCenterListAdapter;
import com.lnbinfotech.msplfootwearex.adapters.TrackOrderDetailChangedAdapter;
import com.lnbinfotech.msplfootwearex.connectivity.ConnectivityTest;
import com.lnbinfotech.msplfootwearex.constant.Constant;
import com.lnbinfotech.msplfootwearex.db.DBHandler;
import com.lnbinfotech.msplfootwearex.interfaces.RecyclerViewToActivityInterface;
import com.lnbinfotech.msplfootwearex.interfaces.ServerCallbackList;
import com.lnbinfotech.msplfootwearex.log.WriteLog;
import com.lnbinfotech.msplfootwearex.model.CompanyMasterClass;
import com.lnbinfotech.msplfootwearex.model.TrackOrderDetailChangedClass;
import com.lnbinfotech.msplfootwearex.model.TrackOrderMasterClass;
import com.lnbinfotech.msplfootwearex.volleyrequests.VolleyRequests;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class TrackOrderDetailActivityChanged extends AppCompatActivity implements View.OnClickListener,RecyclerViewToActivityInterface {

    private Constant constant, constant1;
    private Toast toast;
    private TrackOrderMasterClass orderClass;

    private TextView tv_orderstatus, tv_invno, tv_transporter, tv_creditapp, tv_alltopckg, tv_taxinvmade,
            tv_invamnt,tv_totset, tv_totqty, tv_tot_inv_qty,tv_tot_can_qty, tv_totamnt, tv_tot_gstamt, tv_tot_grossamt,
            tv_disc_per, tv_discamnt, tv_creaditlimit;
    private ListView lv_vOrder;
    private Button btn_proceed;
    private String filter = "";
    private DBHandler db;
    private List<TrackOrderDetailChangedClass> list;
    private ImageView imgv_i;
    private RecyclerView rv_dispatchcenter;
    public static List<CompanyMasterClass> dispatchcenter_list;
    public static HashMap<Integer, Integer> cbMap;
    private HashMap<Integer, Integer> dispatchCenterTotalMap;
    private List<String> workingDispatchCenter;
    private int allBranch = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(Constant.liveTestFlag==1) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }

        setContentView(R.layout.activity_track_order_detail_changed);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Your Order");
        }

        init();
        imgv_i.setOnClickListener(this);

        btn_proceed.setOnClickListener(this);

        //setDispatchCenterData();

        lv_vOrder.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                /*if (from.equals("addtocard")) {
                    list.get(i);
                    Constant.showLog("selected pos:" + i);
                    AddToCartActivity.updateCustOrder = list.get(i);
                    showDia(1);
                }*/
            }
        });

        getOrder();

        /*if (DisplayCustOutstandingActivity.outClass == null) {
            loadOustandingdetail();
        } else {
            String str = "Credit Limit :  " + DisplayCustOutstandingActivity.outClass.getCreditlimit();
            tv_creaditlimit.setText(str);
        }*/
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_proceed:
                checkLimit();
                break;
            case R.id.imgv_i:
                //finish();
                Intent in = new Intent(this, DisplayCustOutstandingActivity.class);
                startActivity(in);
                overridePendingTransition(R.anim.enter, R.anim.exit);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        //showDia(0);
        new Constant(TrackOrderDetailActivityChanged.this).doFinish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /*if (!from.equals("addtocard")) {
            getMenuInflater().inflate(R.menu.vieworderactivity_menu, menu);
        }*/
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //showDia(0);
                new Constant(TrackOrderDetailActivityChanged.this).doFinish();
                break;
            case R.id.add:
                showDia(2);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setDispatchCenterData() {
        dispatchcenter_list = new ArrayList<>();
        dispatchCenterTotalMap = new HashMap<>();
        cbMap = new HashMap<>();

        Cursor res2 = db.getDispatchCenterWiseTotal();
        if (res2.moveToFirst()) {
            do {
                int id = res2.getInt(res2.getColumnIndex(DBHandler.CO_BranchId));
                int total = res2.getInt(res2.getColumnIndex(DBHandler.CO_LooseQty));
                dispatchCenterTotalMap.put(id, total);
            } while (res2.moveToNext());
        }
        res2.close();

        int hocode = FirstActivity.pref.getInt(getString(R.string.pref_hocode), 0);
        Cursor res1 = db.getDispatchCenter(hocode);
        if (res1.moveToFirst()) {
            do {
                CompanyMasterClass comClass = new CompanyMasterClass();
                String id = res1.getString(res1.getColumnIndex(DBHandler.Company_Id));
                String initial = res1.getString(res1.getColumnIndex(DBHandler.Company_Initial));
                if (workingDispatchCenter.contains(initial)) {
                    int total = 0;
                    if (!dispatchCenterTotalMap.isEmpty()) {
                        if (dispatchCenterTotalMap.containsKey(Integer.parseInt(id))) {
                            total = dispatchCenterTotalMap.get(Integer.parseInt(id));
                        } else {
                            total = 0;
                        }
                    }
                    initial = initial + " - " + total;
                    comClass.setCompanyId(id);
                    comClass.setCompanyInitial(initial);
                    dispatchcenter_list.add(comClass);
                    if (cbMap.isEmpty()) {
                        cbMap.put(Integer.parseInt(id), 0);
                    } else {
                        if (!cbMap.containsKey(Integer.parseInt(id))) {
                            cbMap.put(Integer.parseInt(id), 0);
                        }
                    }
                }
            } while (res1.moveToNext());
        }
        res1.close();
        if (dispatchcenter_list.size() != 0) {
            DispatchCenterListAdapter adapter = new DispatchCenterListAdapter(dispatchcenter_list, getApplicationContext());
            adapter.setOnClickListener1(this);
            rv_dispatchcenter.setAdapter(adapter);
        } else {
            //showToast("No Qty Avalilable");
        }
    }

    private void getOrder() {
        orderClass = (TrackOrderMasterClass) getIntent().getSerializableExtra("trackorderclass");
        /*String stat = orderClass.getApprove();
        if (stat.equalsIgnoreCase("Y")) {
            tv_orderstatus.setText("Approved");
            tv_orderstatus.setTextColor(getResources().getColor(R.color.darkgreen));
        } else {
            tv_orderstatus.setText("Not Approved");
            tv_orderstatus.setTextColor(getResources().getColor(R.color.red));
        }*/
        if (ConnectivityTest.getNetStat(getApplicationContext())) {
            loadOrderDetails();
        } else {
            toast.setText("You Are Offline");
            toast.show();
        }
    }

    private void loadOrderDetails() {
        String url = Constant.ipaddress + "/GetTrackOrderDetail?mastId=" + orderClass.getAuto();
        Constant.showLog(url);
        writeLog("loadOrderDetails_" + url);
        constant.showPD();
        VolleyRequests requests = new VolleyRequests(TrackOrderDetailActivityChanged.this);
        requests.loadDetailOrder(url, new ServerCallbackList() {
            @Override
            public void onSuccess(Object result) {
                constant.showPD();
                setData();
            }
            @Override
            public void onFailure(Object result) {
                constant.showPD();
                showDia(2);
            }
        });
    }

    private void setData() {
        list.clear();
        lv_vOrder.setAdapter(null);
        Cursor cursor = db.getTrackOrderDetailData();
        String invNo="", invDate="", transporter="", transporterNo="", creditApp="", allotedTopck="",
                taxInvMade="", status="",invamt = "";
        if (cursor.moveToFirst()) {
            do {
                TrackOrderDetailChangedClass order = new TrackOrderDetailChangedClass();
                order.setAuto(cursor.getInt(cursor.getColumnIndex(DBHandler.TCO_Auto)));
                order.setProductid(cursor.getInt(cursor.getColumnIndex(DBHandler.TCO_Productid)));
                order.setSize_group(cursor.getString(cursor.getColumnIndex(DBHandler.TCO_SizeGroup)));
                order.setColor(cursor.getString(cursor.getColumnIndex(DBHandler.TCO_Color)));
                order.setHashcode(cursor.getString(cursor.getColumnIndex(DBHandler.TCO_HashCode)));
                order.setMrp(cursor.getString(cursor.getColumnIndex(DBHandler.TCO_MRP)));
                order.setRate(cursor.getString(cursor.getColumnIndex(DBHandler.TCO_Rate)));
                order.setOrderqty(cursor.getInt(cursor.getColumnIndex(DBHandler.TCO_OrderQty)));
                order.setLoosePackTyp(cursor.getString(cursor.getColumnIndex(DBHandler.TCO_LoosePackTyp)));
                creditApp = cursor.getString(cursor.getColumnIndex(DBHandler.TCO_CreditApp));
                order.setCreditapp(creditApp);
                allotedTopck = cursor.getString(cursor.getColumnIndex(DBHandler.TCO_AllotedToPck));
                order.setAllowtopck(allotedTopck);
                taxInvMade = cursor.getString(cursor.getColumnIndex(DBHandler.TCO_TaxInvMade));
                order.setTaxinvmade(taxInvMade);
                invNo = cursor.getString(cursor.getColumnIndex(DBHandler.TCO_InvNo));
                order.setInvno(invNo);
                invDate = cursor.getString(cursor.getColumnIndex(DBHandler.TCO_InvDate));
                invamt = cursor.getString(cursor.getColumnIndex(DBHandler.TCO_InvAmt));
                order.setInvamnt(invamt);
                transporter = cursor.getString(cursor.getColumnIndex(DBHandler.TCO_Transporter));
                order.setTransporter(transporter);
                transporterNo = cursor.getString(cursor.getColumnIndex(DBHandler.TCO_TransporterNo));
                order.setProdid(cursor.getString(cursor.getColumnIndex(DBHandler.TCO_Prodid)));
                order.setInvqty(cursor.getInt(cursor.getColumnIndex(DBHandler.TCO_InvQty)));
                order.setCanqty(cursor.getInt(cursor.getColumnIndex(DBHandler.TCO_CancelQty)));
                status = cursor.getString(cursor.getColumnIndex(DBHandler.TCO_Status));
                order.setStatus(status);
                list.add(order);
            } while (cursor.moveToNext());
        }
        cursor.close();
        status = "Order Status :- "+status;
        tv_orderstatus.setText(status);
        invNo = "Invoice No :- "+invNo +" Inv.Date :- "+invDate;
        tv_invno.setText(invNo);
        transporter = "Transporter :- "+transporter + "  " + transporterNo;
        tv_transporter.setText(transporter);
        tv_creditapp.setText(creditApp);
        tv_alltopckg.setText(allotedTopck);
        tv_taxinvmade.setText(taxInvMade);
        tv_invamnt.setText(invamt);
        TrackOrderDetailChangedAdapter adapter = new TrackOrderDetailChangedAdapter(list, getApplicationContext());
        lv_vOrder.setAdapter(adapter);
        totalCalculations();
    }

    private void totalCalculations() {
        String totalSet = "0", totalQty = "0", totalInvQty = "0", totalCanQty = "0";
        //totalAmnt = "0", totalNetAmnt = "0",totalGrossAmnt = "0", totalDiscAmnt = "0", totalGSTAmnt = "0";

        Cursor res = db.getCustOrderTotalsAtTrackOrder(1, filter);
        if (res.moveToFirst()) {
            totalSet = res.getString(res.getColumnIndex(DBHandler.TCO_Auto));
            totalQty = res.getString(res.getColumnIndex(DBHandler.TCO_OrderQty));
            totalInvQty = res.getString(res.getColumnIndex(DBHandler.TCO_InvQty));
            totalCanQty = res.getString(res.getColumnIndex(DBHandler.TCO_CancelQty));
        }
        res.close();

        if (totalQty == null) {
            totalQty = "0";
        }
        if (totalSet == null) {
            totalSet = "0";
        }
        if (totalInvQty == null) {
            totalInvQty = "0";
        }
        if (totalCanQty == null) {
            totalCanQty = "0";
        }
        /*if (totalGrossAmnt == null) {
            totalGrossAmnt = "0";
        }
        if (totalGSTAmnt == null) {
            totalGSTAmnt = "0";
        }
        if (totalDiscAmnt == null) {
            totalDiscAmnt = "0";
        }*/

        tv_totset.setText(totalSet);
        tv_totqty.setText(totalQty);
        tv_tot_inv_qty.setText(totalInvQty);
        tv_tot_can_qty.setText(totalCanQty);

        //tv_totamnt.setText(totalNetAmnt);
        //tv_tot_gstamt.setText(totalGSTAmnt);
        //tv_tot_grossamt.setText(totalAmnt);
        //tv_disc_per.setText(String.valueOf(OptionsActivity.custDisc));
        //tv_discamnt.setText(totalDiscAmnt);

    }

    private void init() {
        imgv_i = (ImageView) findViewById(R.id.imgv_i);
        constant = new Constant(TrackOrderDetailActivityChanged.this);
        constant1 = new Constant(getApplicationContext());
        toast = Toast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        lv_vOrder = (ListView) findViewById(R.id.lv_vOrder);
        db = new DBHandler(TrackOrderDetailActivityChanged.this);
        list = new ArrayList<>();
        tv_orderstatus = (TextView) findViewById(R.id.tv_orderStatus);
        tv_invno = (TextView) findViewById(R.id.tv_invoiceno);
        tv_transporter = (TextView) findViewById(R.id.tv_trasporter);
        tv_creditapp = (TextView) findViewById(R.id.tv_creditapp);
        tv_alltopckg = (TextView) findViewById(R.id.tv_alltopckg);
        tv_taxinvmade = (TextView) findViewById(R.id.tv_tavinvmade);
        tv_invamnt = (TextView) findViewById(R.id.tv_invamnt);
        tv_totset = (TextView) findViewById(R.id.tv_tot_set);
        tv_totqty = (TextView) findViewById(R.id.tv_tot_ord_qty);
        tv_tot_inv_qty = (TextView) findViewById(R.id.tv_tot_inv_qty);
        tv_tot_can_qty = (TextView) findViewById(R.id.tv_tot_can_qty);
        tv_totamnt = (TextView) findViewById(R.id.tv_amt);
        tv_tot_gstamt = (TextView) findViewById(R.id.tv_gst_amt);
        tv_tot_grossamt = (TextView) findViewById(R.id.tv_gross_amt);
        tv_disc_per = (TextView) findViewById(R.id.tv_disc_per);
        tv_discamnt = (TextView) findViewById(R.id.tv_disc_amt);
        tv_creaditlimit = (TextView) findViewById(R.id.tv_creaditlimit);
        btn_proceed = (Button) findViewById(R.id.btn_proceed);
        rv_dispatchcenter = (RecyclerView) findViewById(R.id.rv_dispatchcenter);

        workingDispatchCenter = new ArrayList<>();
        workingDispatchCenter.add("U5%");
        workingDispatchCenter.add("UGNT");
        workingDispatchCenter.add("ULKS");
        workingDispatchCenter.add("USCH");
        workingDispatchCenter.add("AMPU");
        workingDispatchCenter.add("AMOT");
        workingDispatchCenter.add("SKRD");
        workingDispatchCenter.add("KRDA");
    }

    private void showDia(int a) {
        AlertDialog.Builder builder = new AlertDialog.Builder(TrackOrderDetailActivityChanged.this);
        builder.setCancelable(false);
        if (a == 2) {
            builder.setMessage("Error While Loading Data?");
            builder.setPositiveButton("Try again", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    loadOrderDetails();
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    new Constant(TrackOrderDetailActivityChanged.this).doFinish();
                }
            });
        }
        builder.create().show();
    }

    private void writeLog(String _data) {
        new WriteLog().writeLog(getApplicationContext(), "ViewCustomerOrderActiviy_" + _data);
    }

    @Override
    public void onItemClick(String size) {
        //Constant.showLog(size);
        filter = "";
        Set<Integer> branchIdSet = cbMap.keySet();
        for (int branchId : branchIdSet) {
            int a = cbMap.get(branchId);
            if (a != 0) {
                filter = filter + branchId + ",";
            }
        }
        if (filter.equals("")) {
            allBranch = 1;
        } else {
            allBranch = 0;
            filter = filter.substring(0, filter.length() - 1);
        }
        Constant.showLog(filter);
        setData();
    }

    private void loadOustandingdetail() {
        int cust_id = FirstActivity.pref.getInt(getString(R.string.pref_retailCustId), 0);
        String url = Constant.ipaddress + "/GetCustOutstanding?Id=" + cust_id;
        Constant.showLog(url);
        writeLog("loadOustandingdetail_" + url);
        constant.showPD();
        final VolleyRequests requests = new VolleyRequests(TrackOrderDetailActivityChanged.this);
        requests.loadCustOutstanding(url, new ServerCallbackList() {
            @Override
            public void onSuccess(Object result) {
                constant.showPD();
                String str = "Credit Limit :  " + DisplayCustOutstandingActivity.outClass.getCreditlimit();
                tv_creaditlimit.setText(str);
                //checkLimit();
            }

            @Override
            public void onFailure(Object result) {
                constant.showPD();
                writeLog("loadOustandingdetail_onFailure_" + result);
                showDia(3);
            }
        });
    }

    private void checkLimit() {
        String currOrder = FirstActivity.pref.getString("totalNetAmnt", "0");
        if (!currOrder.equals("0")) {
            float netAmt = Float.parseFloat(currOrder);
            float creditLimit = 0;
            String str = DisplayCustOutstandingActivity.outClass.getCreditlimit();
            if (str != null && !str.equals("")) {
                creditLimit = Float.parseFloat(str);
                if (netAmt > creditLimit) {
                    showDia(4);
                } else {
                    finish();
                    Intent intent = new Intent(this, CheckoutCustOrderActivity.class);
                    intent.putExtra("from", "");
                    startActivity(intent);
                    overridePendingTransition(R.anim.enter, R.anim.exit);
                }
            } else {
                toast.setText("Something Went Wrong");
                toast.show();
            }
        } else {
            toast.setText("Please Place Order");
            toast.show();
        }
    }
}