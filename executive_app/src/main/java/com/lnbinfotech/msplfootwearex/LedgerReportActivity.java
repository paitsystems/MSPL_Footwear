package com.lnbinfotech.msplfootwearex;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.Gravity;

import com.lnbinfotech.msplfootwearex.adapters.LedgerReportAdapter;
import com.lnbinfotech.msplfootwearex.connectivity.ConnectivityTest;
import com.lnbinfotech.msplfootwearex.constant.Constant;
import com.lnbinfotech.msplfootwearex.interfaces.ServerCallbackList;
import com.lnbinfotech.msplfootwearex.log.WriteLog;
import com.lnbinfotech.msplfootwearex.model.LedgerReportClass;
import com.lnbinfotech.msplfootwearex.volleyrequests.VolleyRequests;

import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class LedgerReportActivity extends AppCompatActivity implements View.OnClickListener {

    private Constant constant, constant1;
    private Toast toast;
    private ListView lv_ledger;
    private TextView tv_fdate,tv_tdate,tot_clb,tot_ob,tot_credit,tot_debit,tv_outstanding;
    private CheckBox cb_all;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
    private SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
    private int day, month, year;
    private Calendar cal = Calendar.getInstance();
    private static final int fdt = 1,tdt = 2;
    private String fromdate="",todate="", all = "";
    private double total_op = 0, total_cl = 0,total_debit = 0,total_credit = 0;
    private DecimalFormat flt_price;
    private Button btn_show;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ledger_report);

        init();
        // visibility();

        todate = sdf1.format(cal.getTime());
        // todate = "1-Nov-2017";
        Constant.showLog("todate_dafault-"+todate);
        tv_tdate.setText(todate);

        int day = 1;
        cal.set(year, month, day);

        int numOfDaysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        Constant.showLog("First Day of month: " + cal.getTime());
        fromdate = sdf1.format(cal.getTime());
        Constant.showLog("fromdate_dafault-"+fromdate);

        // fromdate = "1-Aug-2017";
        tv_fdate.setText(fromdate);

        //showLedgerReport();

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        btn_show.setOnClickListener(this);
        tv_outstanding.setOnClickListener(this);
        //tv_fdate.setOnClickListener(this);
        //tv_tdate.setOnClickListener(this);
        tv_fdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(fdt);
                cb_all.setChecked(false);
            }
        });

        tv_tdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showDialog(tdt);
                cb_all.setChecked(false);
            }
        });
        cb_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                visibility();
                // tv_fdate.setEnabled(false);
                // tv_tdate.setEnabled(false);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        constant = new Constant(LedgerReportActivity.this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_show:
                if(cb_all.isChecked()){
                    all = "Y";
                    fromdate = "NA";
                    todate = "NA";
                }else {
                    all = "N";
                    fromdate = tv_fdate.getText().toString();
                    Constant.showLog("fromdate-"+fromdate);
                    todate =  tv_tdate.getText().toString();
                    Constant.showLog("todate-"+todate);
                }
                showLedgerReport();
                break;
            case R.id.tv_outstanding:
                startActivity(new Intent(this,OutstandingBillReportActivity.class));
                overridePendingTransition(R.anim.enter,R.anim.exit);
                break;

        }
    }

    @Override
    public void onBackPressed() {
        //showDia(0);
        new Constant(LedgerReportActivity.this).doFinish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //showDia(0);
                new Constant(LedgerReportActivity.this).doFinish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showLedgerReport() {
        if (ConnectivityTest.getNetStat(LedgerReportActivity.this)) {
            try {
                visibility();
                /*String all = "";
                if(cb_all.isChecked()){
                    all = "Y";
                }else {
                    all = "N";
                }*/


                // fromdate =  URLEncoder.encode(sdf1.format(sdf.parse(tv_fdate.getText().toString())),"UTF-8");
                // fromdate = sdf1.format(sdf.parse(tv_fdate.getText().toString()));
                //Constant.showLog("fdate-"+fdate);
                //  todate =  URLEncoder.encode(sdf1.format(sdf.parse(tv_tdate.getText().toString())),"UTF-8");
                //String tdate = sdf1.format(sdf.parse(tv_tdate.getText().toString()));
                // Constant.showLog("tdate-"+tdate);
                lv_ledger.setAdapter(null);
                String _fromdate = URLEncoder.encode(fromdate, "UTF-8");
                String _todate = URLEncoder.encode(todate, "UTF-8");

                String _all =  URLEncoder.encode(all, "UTF-8");
                Constant.showLog("all"+all);

                int id = FirstActivity.pref.getInt(getString(R.string.pref_retailCustId),0);
                Constant.showLog("id"+id);

                String url = Constant.ipaddress + "/GetLedgerReport?custid=100&fdate="+_fromdate+"&tdate="+_todate+"&all="+_all;
                 // String url = Constant.ipaddress + "/GetLedgerReport?custid=100&fdate=1-Aug-2017&tdate=1-Dec-2017&all=N";
                Constant.showLog(url);
                writeLog("superfastSellingDetails" + url);
                constant.showPD();
                VolleyRequests requests = new VolleyRequests(LedgerReportActivity.this);
                requests.loadLedgerReport(url, new ServerCallbackList() {
                    @Override
                    public void onSuccess(Object result) {
                        constant.showPD();
                        List<LedgerReportClass> list = (List<LedgerReportClass>) result;
                        if (list.size() != 0) {

                            LedgerReportAdapter adapter = new LedgerReportAdapter(list, getApplicationContext());
                            lv_ledger.setAdapter(adapter);
                            //showPopup(1);
                            setTotal(list);
                        } else {
                            showPopup(4);
                        }
                    }

                    @Override
                    public void onFailure(Object result) {
                        constant.showPD();
                        showPopup(4);
                        // showPopup(2);

                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                constant.showPD();
                //  showPopup(2);
                showPopup(4);
                writeLog("superfastSellingDetails_" + e.getMessage());
            }
        } else {
            toast.setText("You Are Offline");
            toast.show();
        }
    }

    private void setTotal(List<LedgerReportClass> list) {

        total_op = 0;
        total_cl = 0;
        total_debit = 0;
        total_credit = 0;

        for (LedgerReportClass ledger : list) {
            total_op = total_op + ledger.getOpnbal();
            total_cl = total_cl + ledger.getClsbal();
            total_debit = total_debit + ledger.getDebit();
            total_credit = total_credit + ledger.getCredit();

        }

        tot_clb.setText(flt_price.format(total_cl));
        tot_debit.setText(flt_price.format(total_debit));
        tot_credit.setText(flt_price.format(total_credit));
        tot_ob.setText(flt_price.format(total_op));

    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case fdt:
                return new DatePickerDialog(this, fdate, year,month,day);

            case tdt:
                return new DatePickerDialog(this, tdate,year,month,day);

        }
        return  null;
    }

    DatePickerDialog.OnDateSetListener fdate = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
            try {
                Date date = sdf.parse(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                tv_fdate.setText(sdf1.format(date));
                Constant.showLog("tv_fdate:"+sdf1.format(date));
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    };

    DatePickerDialog.OnDateSetListener tdate = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
            try {
                Date date = sdf.parse(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                tv_tdate.setText(sdf1.format(date));
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    };

    private void init() {
        btn_show = (Button) findViewById(R.id.btn_show);
        tv_outstanding= (TextView) findViewById(R.id.tv_outstanding);
        tv_fdate = (TextView) findViewById(R.id.tv_fdate);
        tv_tdate = (TextView) findViewById(R.id.tv_tdate);
        lv_ledger = (ListView) findViewById(R.id.lv_ledger);
        tot_clb = (TextView) findViewById(R.id.tot_clb);
        tot_ob = (TextView) findViewById(R.id.tot_ob);
        tot_credit = (TextView) findViewById(R.id.tot_credit);
        tot_debit = (TextView) findViewById(R.id.tot_debit);

        cb_all = (CheckBox) findViewById(R.id.cb_all);
        constant = new Constant(LedgerReportActivity.this);
        constant1 = new Constant(getApplicationContext());
        toast = Toast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);

        flt_price = new DecimalFormat();
        flt_price.setMaximumFractionDigits(2);

        day = cal.get(Calendar.DAY_OF_MONTH);
        month = cal.get(Calendar.MONTH);
        year = cal.get(Calendar.YEAR);
        cb_all.setChecked(false);

        // tv_fdate.setEnabled(true);
        //tv_tdate.setEnabled(true);
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
            builder.setMessage("Error While Loading Data?");
            builder.setPositiveButton("Try again", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    showLedgerReport();

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
            builder.setMessage("Data Not Available");
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();

                }
            });
        }

        builder.create().show();
    }

    private void visibility(){
        if(cb_all.isChecked()){
            tv_fdate.setEnabled(false);
            tv_tdate.setEnabled(false);

        }else{
            tv_fdate.setEnabled(true);
            tv_tdate.setEnabled(true);
        }
    }

    private void writeLog(String _data) {
        new WriteLog().writeLog(getApplicationContext(), "LedgerReportActivity_" + _data);
    }
}
