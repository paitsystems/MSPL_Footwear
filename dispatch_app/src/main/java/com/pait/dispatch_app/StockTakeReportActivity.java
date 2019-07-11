package com.pait.dispatch_app;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.pait.dispatch_app.adapters.StockTakeAdapter;
import com.pait.dispatch_app.adapters.StockTakeReportAdapter;
import com.pait.dispatch_app.constant.Constant;
import com.pait.dispatch_app.db.DBHandler;
import com.pait.dispatch_app.interfaces.RetrofitApiInterface;
import com.pait.dispatch_app.interfaces.TestInterface;
import com.pait.dispatch_app.log.WriteLog;
import com.pait.dispatch_app.model.StockTakeClass;
import com.pait.dispatch_app.parse.UserClass;
import com.pait.dispatch_app.utility.RetrofitApiBuilder;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StockTakeReportActivity extends AppCompatActivity implements View.OnClickListener,TestInterface {

    private int hoCode, dpID, empId, designId = 0, branchId, requestCode = 0, mYear, mMonth, mDay, roundFlag;
    private UserClass userClass;
    private DBHandler db;
    private Toast toast;
    private List<StockTakeClass> list;
    private StockTakeReportAdapter adapter;
    private ListView listView;
    private TextView tv_stDate, tv_articleName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(Constant.liveTestFlag==1) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }

        setContentView(R.layout.activity_stock_take_report);

        init();

        userClass = (UserClass) getIntent().getExtras().get("cust");
        empId = userClass.getCustID();
        hoCode = userClass.getHOCode();
        branchId = userClass.getBranchId();
        dpID = userClass.getDpId();
        designId = FirstActivity.pref.getInt(getString(R.string.pref_design), 0);

        getStockTakeReport(0);

    }

    @Override
    public void onBackPressed() {
        showDia(1);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_stDate:
                showDialog(1);
                break;
        }

    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case 1:
                return new DatePickerDialog(this, mtrv_tavel_date, mYear, mMonth, mDay);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener mtrv_tavel_date = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int yearSelected, int monthOfYear, int dayOfMonth) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
                Date fdate = sdf.parse(dayOfMonth + "/" + (monthOfYear + 1) + "/" + yearSelected);
                String dstr = (String) android.text.format.DateFormat.format("dd/MMM/yyyy", fdate);
                tv_stDate.setText(dstr);
                showDia(8);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    public void onResumeFragment(String data1, String data2, Context context) {
        Constant.showLog(data1);
        tv_articleName.setVisibility(View.VISIBLE);
        String prodDet = db.getProductDetail(data1);
        tv_articleName.setText(prodDet);
    }

    @Override
    public void onPauseFragment(String data1, String data2, Context context) {

    }

    @Override
    public void onAmountChange(int amnt) {

    }

    private void getStockTakeReport(int type) {
        final Constant constant = new Constant(StockTakeReportActivity.this);
        constant.showPD();
        try {
            int maxAuto = db.getMaxAuto();
            //Auto + "|"+ CustId + "|"+ HOCode + "|"+ dispatchId + "|"+ empId + "|"+ type
            //TODO: Remove empid
            //empId = 12;
            String url = maxAuto + "|" + 0 + "|" + hoCode + "|" + dpID + "|" + empId + "|" +
                    type + "|" + designId + "|" + tv_stDate.getText().toString();
            writeLog("getStockTakeMaster_" + url);
            final JSONObject jsonBody = new JSONObject();
            jsonBody.put("details", url);
            RequestBody body = RequestBody.create(okhttp3.MediaType.
                    parse("application/json; charset=utf-8"), (jsonBody).toString());
            Constant.showLog(jsonBody.toString());
            list.clear();
            listView.setAdapter(null);

            Call<List<StockTakeClass>> call = new RetrofitApiBuilder().getApiBuilder().
                    create(RetrofitApiInterface.class).
                    getStockTakeReport(body);
            call.enqueue(new Callback<List<StockTakeClass>>() {
                @Override
                public void onResponse(Call<List<StockTakeClass>> call, Response<List<StockTakeClass>> response) {
                    Constant.showLog("onResponse");
                    list = response.body();
                    if (list != null) {
                        setData(1);
                        Constant.showLog(list.size() + "_getStockTakeMaster");
                    } else {
                        Constant.showLog("onResponse_list_null");
                        writeLog("getStockTakeMaster_onResponse_list_null");
                    }
                    constant.showPD();
                }

                @Override
                public void onFailure(Call<List<StockTakeClass>> call, Throwable t) {
                    Constant.showLog("onFailure");
                    if (!call.isCanceled()) {
                        call.cancel();
                    }
                    t.printStackTrace();
                    writeLog("getStockTakeMaster_onFailure_" + t.getMessage());
                    constant.showPD();
                    showDia(2);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            writeLog("getStockTakeMaster_" + e.getMessage());
            constant.showPD();
            showDia(2);
        }
    }

    private void setData(int checkStock) {
        if (!list.isEmpty()) {
            adapter = new StockTakeReportAdapter(getApplicationContext(), list, checkStock);
            adapter.initInterface(StockTakeReportActivity.this);
            listView.setAdapter(adapter);
        }
    }

    private void init() {
        toast = Toast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        db = new DBHandler(getApplicationContext());
        FirstActivity.pref = getSharedPreferences(FirstActivity.PREF_NAME, MODE_PRIVATE);
        list = new ArrayList<>();
        listView = findViewById(R.id.listView);
        tv_stDate = findViewById(R.id.tv_stDate);
        tv_articleName = findViewById(R.id.tv_articlename);
        tv_stDate.setText(new Constant().getDate());
        tv_stDate.setOnClickListener(this);

        Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
    }

    private void showDia(int a) {
        AlertDialog.Builder builder = new AlertDialog.Builder(StockTakeReportActivity.this);
        builder.setCancelable(false);
        if (a == 1) {
            builder.setMessage("Do You Want To Go Back ?");
            builder.setPositiveButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            builder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    new Constant(StockTakeReportActivity.this).doFinish();
                }
            });
        } else if (a == 2) {
            builder.setMessage("Error While Loading Data");
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    new Constant();
                    dialog.dismiss();
                }
            });
        } else if (a == 8) {
            builder.setMessage("Get Data of Selected Date " + tv_stDate.getText().toString());
            builder.setPositiveButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    tv_stDate.setText(new Constant().getDate());
                }
            });
            builder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    list.clear();
                    getStockTakeReport(0);
                }
            });
        }
        builder.create().show();
    }

    private void writeLog(String _data) {
        new WriteLog().writeLog(getApplicationContext(), "StockTakeReportActivity_" + _data);
    }
}