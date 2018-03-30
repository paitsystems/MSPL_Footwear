package com.lnbinfotech.msplfootwear;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lnbinfotech.msplfootwear.adapters.OuststandingReportAdapter;
import com.lnbinfotech.msplfootwear.connectivity.ConnectivityTest;
import com.lnbinfotech.msplfootwear.constant.Constant;
import com.lnbinfotech.msplfootwear.interfaces.ServerCallbackList;
import com.lnbinfotech.msplfootwear.log.WriteLog;
import com.lnbinfotech.msplfootwear.model.OuststandingReportClass;
import com.lnbinfotech.msplfootwear.volleyrequests.VolleyRequests;

import java.text.DecimalFormat;
import java.util.List;

public class OutstandingBillReportActivity extends AppCompatActivity implements View.OnClickListener {

    private Constant constant, constant1;
    private Toast toast;
    private ListView lv_out;
    private TextView tot_Total;
    private double total = 0;
    private DecimalFormat dc_format;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(Constant.liveTestFlag==1) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }

        setContentView(R.layout.activity_outstanding_bill_report);

        init();

        showOutstandingReport();

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

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
        new Constant(OutstandingBillReportActivity.this).doFinish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //showDia(0);
                new Constant(OutstandingBillReportActivity.this).doFinish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    private void showOutstandingReport() {
        if (ConnectivityTest.getNetStat(OutstandingBillReportActivity.this)) {
            try {
                lv_out.setAdapter(null);
                int id = FirstActivity.pref.getInt(getString(R.string.pref_retailCustId), 0);
                // String url = Constant.ipaddress + "/GetOutstandingRpt?custid=1689";
                String url = Constant.ipaddress + "/GetOutstandingRpt?custid=" + id;
                Constant.showLog(url);
                writeLog("showOutstandingReport" + url);
                constant.showPD();
                VolleyRequests requests = new VolleyRequests(OutstandingBillReportActivity.this);
                requests.loadOuststndReport(url, new ServerCallbackList() {
                    @Override
                    public void onSuccess(Object result) {
                        constant.showPD();
                        List<OuststandingReportClass> list = (List<OuststandingReportClass>) result;
                        if (list.size() != 0) {
                            OuststandingReportAdapter adapter = new OuststandingReportAdapter(list, getApplicationContext());
                            lv_out.setAdapter(adapter);
                            setTotal(list);
                        } else {
                            toast.setText("No Record Available");
                            toast.show();
                        }
                    }

                    @Override
                    public void onFailure(Object result) {
                        constant.showPD();
                        showPopup(2);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                constant.showPD();
                showPopup(2);
                writeLog("showOutstandingReport_" + e.getMessage());
            }
        } else {
            toast.setText("You Are Offline");
            toast.show();
        }
    }


    private void setTotal(List<OuststandingReportClass> list) {
        total = 0;

        for (OuststandingReportClass oclass : list) {
            total = total + oclass.getTotal();
        }

        tot_Total.setText(dc_format.format(total));

    }

    private void init() {
        lv_out = (ListView) findViewById(R.id.lv_out);
        tot_Total = (TextView) findViewById(R.id.tot_Total);
        constant = new Constant(OutstandingBillReportActivity.this);
        constant1 = new Constant(getApplicationContext());
        toast = Toast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);

        dc_format = new DecimalFormat();
        dc_format.setMaximumFractionDigits(2);
    }

    private void showPopup(int id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if (id == 0) {
            builder.setMessage("Do you want to exit?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    finish();
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
        } else if (id == 1) {
            builder.setMessage("Data Loaded Successfully");
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        } else if (id == 2) {
            builder.setMessage("No Record Available");
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    //showOutstandingReport();
                }
            });
        } else if (id == 3) {
            builder.setMessage("");
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        } else if (id == 4) {
            builder.setMessage("select month type:");
            builder.setPositiveButton("3 Month", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            builder.setNegativeButton("6 Month", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
        }

        builder.create().show();
    }

    private void writeLog(String _data) {
        new WriteLog().writeLog(getApplicationContext(), "OutstandingBillReportActivity_" + _data);
    }
}
