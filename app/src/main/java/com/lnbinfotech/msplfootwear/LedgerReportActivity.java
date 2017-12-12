package com.lnbinfotech.msplfootwear;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.Gravity;

import com.lnbinfotech.msplfootwear.adapters.LedgerReportAdapter;
import com.lnbinfotech.msplfootwear.connectivity.ConnectivityTest;
import com.lnbinfotech.msplfootwear.constant.Constant;
import com.lnbinfotech.msplfootwear.interfaces.ServerCallbackList;
import com.lnbinfotech.msplfootwear.log.WriteLog;
import com.lnbinfotech.msplfootwear.model.LedgerReportClass;
import com.lnbinfotech.msplfootwear.volleyrequests.VolleyRequests;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class LedgerReportActivity extends AppCompatActivity implements View.OnClickListener {

    private Constant constant, constant1;
    private Toast toast;
    private ListView lv_ledger;
    private TextView tv_fdate,tv_tdate;
    private CheckBox cb_all;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
    private int day, month, year;
    private Calendar cal = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ledger_report);

        init();

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
                String _branch = URLEncoder.encode("JAYNAGAR", "UTF-8");
                String url = Constant.ipaddress + "/GetSuperfastSellingReport?fromdate=01/Sep/2017&todate=11/Nov/2017&percent=80";
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
                            lv_ledger.setAdapter(null);
                            //setMonths();
                            LedgerReportAdapter adapter = new LedgerReportAdapter(list, getApplicationContext());
                            lv_ledger.setAdapter(adapter);
                            //setTotal(list);
                        } else {
                            showPopup(1);
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
                writeLog("superfastSellingDetails_" + e.getMessage());
            }
        } else {
            toast.setText("You Are Offline");
            toast.show();
        }
    }

    @Override
    protected Dialog onCreateDialog(int id) {
       DatePickerDialog d1 =  new DatePickerDialog(this,fdate,year,month,day);
        return  d1;
    }

    DatePickerDialog.OnDateSetListener fdate = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
            try {
                Date date = sdf.parse(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                tv_fdate.setText(sdf.format(date));
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
                tv_tdate.setText(sdf.format(date));
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    };

    private void init() {
        tv_fdate = (TextView) findViewById(R.id.tv_fdate);
        tv_tdate = (TextView) findViewById(R.id.tv_tdate);
        lv_ledger = (ListView) findViewById(R.id.lv_ledger);
        cb_all = (CheckBox) findViewById(R.id.cb_all);
        constant = new Constant(LedgerReportActivity.this);
        constant1 = new Constant(getApplicationContext());
        toast = Toast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);

        day = cal.get(Calendar.DAY_OF_MONTH);
        month = cal.get(Calendar.MONTH);
        year = cal.get(Calendar.YEAR);
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
        new WriteLog().writeLog(getApplicationContext(), "LedgerReportActivity_" + _data);
    }
}
