package com.lnbinfotech.msplfootwear;

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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lnbinfotech.msplfootwear.adapters.DispatchCenterListAdapter;
import com.lnbinfotech.msplfootwear.adapters.ViewCustomerOrderAdapter;
import com.lnbinfotech.msplfootwear.constant.Constant;
import com.lnbinfotech.msplfootwear.db.DBHandler;
import com.lnbinfotech.msplfootwear.interfaces.RecyclerViewToActivityInterface;
import com.lnbinfotech.msplfootwear.interfaces.ServerCallbackList;
import com.lnbinfotech.msplfootwear.log.WriteLog;
import com.lnbinfotech.msplfootwear.model.CompanyMasterClass;
import com.lnbinfotech.msplfootwear.model.CustomerOrderClass;
import com.lnbinfotech.msplfootwear.volleyrequests.VolleyRequests;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class TrackOrderDetailActivityChanged extends AppCompatActivity implements View.OnClickListener,RecyclerViewToActivityInterface {


    private Constant constant, constant1;
    private Toast toast;

    private TextView tv_totset, tv_totqty, tv_totamnt, tv_tot_gstamt, tv_tot_grossamt, tv_disc_per, tv_discamnt, tv_creaditlimit;
    private ListView lv_vOrder;
    private Button btn_proceed;
    private String from, filter = "";
    private DBHandler db;
    private List<CustomerOrderClass> list;
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
        setContentView(R.layout.activity_track_order_detail_changed);

        from = getIntent().getExtras().getString("from");

        if (getSupportActionBar() != null) {
            assert from != null;
            if (from.equals("addtocard")) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }
            getSupportActionBar().setTitle("Your Order");
        }

        init();
        imgv_i.setOnClickListener(this);

        btn_proceed.setOnClickListener(this);

        setDispatchCenterData();

        setData();

        lv_vOrder.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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

        if (DisplayCustOutstandingActivity.outClass == null) {
            loadOustandingdetail();
        } else {
            String str = "Credit Limit :  " + DisplayCustOutstandingActivity.outClass.getCreditlimit();
            tv_creaditlimit.setText(str);
        }
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
        if (!from.equals("addtocard")) {
            getMenuInflater().inflate(R.menu.vieworderactivity_menu, menu);
        }
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

    private void setData() {
        list.clear();
        lv_vOrder.setAdapter(null);
        Cursor cursor = db.getViewOrderData(allBranch, filter);
        if (cursor.moveToFirst()) {
            do {
                CustomerOrderClass order = new CustomerOrderClass();
                order.setAuto(cursor.getInt(cursor.getColumnIndex(DBHandler.CO_Auto)));
                order.setProductid(cursor.getInt(cursor.getColumnIndex(DBHandler.CO_Productid)));
                order.setSizeGroup(cursor.getString(cursor.getColumnIndex(DBHandler.CO_SizeGroup)));
                order.setColor(cursor.getString(cursor.getColumnIndex(DBHandler.CO_Color)));
                order.setHashCode(cursor.getString(cursor.getColumnIndex(DBHandler.CO_HashCode)));
                order.setLooseQty(cursor.getInt(cursor.getColumnIndex(DBHandler.CO_LooseQty)));
                order.setMrp(cursor.getString(cursor.getColumnIndex(DBHandler.CO_MRP)));
                order.setAmount(cursor.getString(cursor.getColumnIndex(DBHandler.CO_Amount)));
                order.setGstper(cursor.getString(cursor.getColumnIndex(DBHandler.CO_GSTPer)));
                order.setRate(cursor.getString(cursor.getColumnIndex(DBHandler.CO_Rate)));
                order.setQty(cursor.getInt(cursor.getColumnIndex(DBHandler.CO_Qty)));
                order.setActLooseQty(cursor.getInt(cursor.getColumnIndex(DBHandler.CO_ActLooseQty)));
                order.setLoosePackTyp(cursor.getString(cursor.getColumnIndex(DBHandler.CO_LoosePackTyp)));
                order.setPerPackQty(cursor.getInt(cursor.getColumnIndex(DBHandler.CO_PerPackQty)));
                order.setOrderType(cursor.getString(cursor.getColumnIndex(DBHandler.CO_OrderType)));
                order.setAvailQty(cursor.getInt(cursor.getColumnIndex(DBHandler.CO_AvailQty)));
                order.setProdId(cursor.getString(cursor.getColumnIndex(DBHandler.CO_Prodid)));
                list.add(order);
            } while (cursor.moveToNext());
        }
        cursor.close();
        ViewCustomerOrderAdapter adapter = new ViewCustomerOrderAdapter(list, getApplicationContext());
        lv_vOrder.setAdapter(adapter);
        totalCalculations();
    }

    private void totalCalculations() {
        String totalSet = "0", totalQty = "0", totalAmnt = "0", totalNetAmnt = "0",
                totalGrossAmnt = "0", totalDiscAmnt = "0", totalGSTAmnt = "0";

        Cursor res = db.getCustOrderTotalsAtViewOrder(allBranch, filter);
        if (res.moveToFirst()) {
            totalQty = res.getString(res.getColumnIndex(DBHandler.CO_LooseQty));
            totalSet = res.getString(res.getColumnIndex(DBHandler.CO_Auto));
            totalAmnt = res.getString(res.getColumnIndex(DBHandler.CO_Amount));
            totalNetAmnt = res.getString(res.getColumnIndex(DBHandler.CO_NetAmt));
            totalGrossAmnt = res.getString(res.getColumnIndex(DBHandler.CO_AmtAfterDisc));
            totalGSTAmnt = res.getString(res.getColumnIndex(DBHandler.CO_GSTAmt));
            totalDiscAmnt = res.getString(res.getColumnIndex(DBHandler.CO_DiscAmt));
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
        if (totalGrossAmnt == null) {
            totalGrossAmnt = "0";
        }
        if (totalGSTAmnt == null) {
            totalGSTAmnt = "0";
        }
        if (totalDiscAmnt == null) {
            totalDiscAmnt = "0";
        }

        tv_totqty.setText(totalQty);
        tv_totset.setText(totalSet);
        tv_totamnt.setText(totalNetAmnt);
        tv_tot_gstamt.setText(totalGSTAmnt);
        tv_tot_grossamt.setText(totalAmnt);
        tv_disc_per.setText(String.valueOf(OptionsActivity.custDisc));
        tv_discamnt.setText(totalDiscAmnt);

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
        tv_totset = (TextView) findViewById(R.id.tv_tot_set);
        tv_totqty = (TextView) findViewById(R.id.tv_tot_qty);
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
        if (a == 0) {
            builder.setMessage("Do You Want To Exit App?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    new Constant(TrackOrderDetailActivityChanged.this).doFinish();
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
            builder.setMessage("Do You Want To Update Order?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    AddToCartActivity.activityToFrom = 2;
                    new Constant(TrackOrderDetailActivityChanged.this).doFinish();
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
            builder.setMessage("Do You Want To Add Order?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                    startActivity(new Intent(getApplicationContext(), CutsizeSetwiseOrderActivity.class));
                    overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
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
            builder.setMessage("Please Try Again");
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    loadOustandingdetail();
                    dialog.dismiss();
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        } else if (a == 4) {
            builder.setTitle("Payment");
            builder.setMessage("Your Order Amount Exceed Credit Limit");
            builder.setPositiveButton("Details", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    startActivity(new Intent(getApplicationContext(), DisplayCustOutstandingActivity.class));
                    overridePendingTransition(R.anim.enter, R.anim.exit);
                }
            });
            builder.setNegativeButton("Continue", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    finish();
                    Intent intent = new Intent(getApplicationContext(), CheckoutCustOrderActivity.class);
                    intent.putExtra("from", from);
                    startActivity(intent);
                    overridePendingTransition(R.anim.enter, R.anim.exit);
                }
            });
            builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
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
                    intent.putExtra("from", from);
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