package com.pait.dispatch_app;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.pait.dispatch_app.adapters.StockTakeAdapter;
import com.pait.dispatch_app.constant.Constant;
import com.pait.dispatch_app.db.DBHandler;
import com.pait.dispatch_app.interfaces.RetrofitApiInterface;
import com.pait.dispatch_app.interfaces.TestInterface;
import com.pait.dispatch_app.log.WriteLog;
import com.pait.dispatch_app.model.StockTakeClass;
import com.pait.dispatch_app.parse.UserClass;
import com.pait.dispatch_app.utility.RetrofitApiBuilder;

import org.json.JSONObject;

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

public class StockTakeActivity extends AppCompatActivity implements View.OnClickListener,TestInterface {

    private int hoCode, dpID, empId, flag = 0, designId = 0, branchId, requestCode = 0, mYear, mMonth, mDay;
    private UserClass userClass;
    private DBHandler db;
    private Toast toast;
    private List<StockTakeClass> list;
    private StockTakeAdapter adapter;
    private NonScrollListView listView;
    private TextView tv_stDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_take);

        init();

        userClass = (UserClass) getIntent().getExtras().get("cust");
        empId = userClass.getCustID();
        hoCode = userClass.getHOCode();
        branchId = userClass.getBranchId();
        dpID = userClass.getDpId();
        designId = FirstActivity.pref.getInt(getString(R.string.pref_design), 0);

        if(db.getStockTakeMaxAuto()>0){
            showDia(3);
        } else {
            setData();
        }
    }

    @Override
    public void onBackPressed() {
        showDia(1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.stocktakeactivity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.dm_save:
                break;
            case R.id.check:
                showDia(4);
                break;
            case R.id.reset:
                showDia(5);
                break;
            case R.id.pause:
                showDia(6);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
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
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    public void onResumeFragment(String data1, String data2, Context context) {

    }

    @Override
    public void onPauseFragment(String data1, String data2, Context context) {

    }

    @Override
    public void onAmountChange(int amnt) {

    }

    private void getStockTakeMaster(int type) {
        final Constant constant = new Constant(StockTakeActivity.this);
        constant.showPD();
        try {
            int maxAuto = db.getMaxAuto();
            //Auto + "|"+ CustId + "|"+ HOCode + "|"+ dispatchId + "|"+ empId + "|"+ type
            String url = maxAuto + "|" + 0 + "|" + hoCode + "|" + dpID + "|" + empId + "|" + type + "|" + designId;
            writeLog("getStockTakeMaster_" + url);
            final JSONObject jsonBody = new JSONObject();
            jsonBody.put("details", url);
            RequestBody body = RequestBody.create(okhttp3.MediaType.
                    parse("application/json; charset=utf-8"), (jsonBody).toString());
            Constant.showLog(jsonBody.toString());
            list.clear();

            Call<List<StockTakeClass>> call = new RetrofitApiBuilder().getApiBuilder().
                    create(RetrofitApiInterface.class).
                    getStockTakeMaster(body);
            call.enqueue(new Callback<List<StockTakeClass>>() {
                @Override
                public void onResponse(Call<List<StockTakeClass>> call, Response<List<StockTakeClass>> response) {
                    Constant.showLog("onResponse");
                    List<StockTakeClass> _list = response.body();
                    if (list != null) {
                        if (flag == 0) {
                            list = _list;
                            setData();
                            Constant.showLog(list.size() + "_getStockTakeMaster");
                        }
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

    private void setData() {
        list.clear();
        for (int i = 0; i < 10; i++) {
            StockTakeClass st = new StockTakeClass();
            st.setProduct_id(""+i);
            st.setProductId(""+(i+1));
            st.setArticleName("600"+i);
            st.setPackQty("0");
            st.setLooseQty("0");
            st.setTotalQty("0");
            st.setStockQty("6"+i);
            st.setHOCode(String.valueOf(hoCode));
            st.setBranchId(String.valueOf(branchId));
            st.setCrBy(String.valueOf(empId));
            list.add(st);
        }
        if (!list.isEmpty()) {
            adapter = new StockTakeAdapter(getApplicationContext(), list);
            adapter.initInterface(StockTakeActivity.this);
            listView.setAdapter(adapter);
        }
    }

    private void loadFromLocal() {
        list.clear();
        Cursor res = db.getStockData();
        if(res.moveToFirst()){
            do{
                StockTakeClass st = new StockTakeClass();
                st.setProduct_id(res.getString(res.getColumnIndex(DBHandler.ST_Product_id)));
                st.setProductId(res.getString(res.getColumnIndex(DBHandler.ST_ProductId)));
                st.setArticleName(res.getString(res.getColumnIndex(DBHandler.ST_ArticleName)));
                st.setPackQty(res.getString(res.getColumnIndex(DBHandler.ST_PackQty)));
                st.setLooseQty(res.getString(res.getColumnIndex(DBHandler.ST_LooseQty)));
                st.setTotalQty(res.getString(res.getColumnIndex(DBHandler.ST_TotalQty)));
                st.setStockQty(res.getString(res.getColumnIndex(DBHandler.ST_StockQty)));
                st.setHOCode(res.getString(res.getColumnIndex(DBHandler.ST_HOCode)));
                st.setBranchId(res.getString(res.getColumnIndex(DBHandler.ST_BranchId)));
                st.setCrBy(res.getString(res.getColumnIndex(DBHandler.ST_CrBy)));
                list.add(st);
            }while (res.moveToNext());
        }
        if (!list.isEmpty()) {
            adapter = new StockTakeAdapter(getApplicationContext(), list);
            adapter.initInterface(StockTakeActivity.this);
            listView.setAdapter(adapter);
        }
    }

    private void getData() {
        if(!list.isEmpty()) {
            int auto = db.getStockTakeMaxAuto();
            List<StockTakeClass> _list = new ArrayList<>();
            list = adapter.getDataSet();
            for (StockTakeClass st : list) {
                Constant.showLog(st.getArticleName() + "-" + st.getPackQty() + "-" + st.getLooseQty());
                st.setAuto(String.valueOf(++auto));
                st.setPackQty(st.getPackQty());
                st.setLooseQty(st.getLooseQty());
                st.setTotalQty(st.getTotalQty());
                st.setStockQty(st.getStockQty());
                st.setCrDate(new Constant().getDate());
                st.setCrTime(new Constant().getTime());
                st.setStock_Check_Date(tv_stDate.getText().toString());
                _list.add(st);
            }
            db.deleteTable(DBHandler.Table_StockTakeMaster);
            db.addStockTakeMaster(_list);
            showDia(7);
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
        tv_stDate.setText(new Constant().getDate());
        tv_stDate.setOnClickListener(this);

        Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
    }

    private void showDia(int a) {
        AlertDialog.Builder builder = new AlertDialog.Builder(StockTakeActivity.this);
        builder.setCancelable(false);
        if (a == 1) {
            builder.setMessage("Do You Want To Go Back ?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    new Constant(StockTakeActivity.this).doFinish();
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
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
        } else if (a == 3) {
            builder.setMessage("Do You Want To Continue From Last Save?");
            builder.setPositiveButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    db.deleteTable(DBHandler.Table_StockTakeMaster);
                    setData();
                }
            });
            builder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    loadFromLocal();
                }
            });
            builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    new Constant(StockTakeActivity.this).doFinish();
                }
            });
        } else if (a == 4) {
            builder.setMessage("Do You Want To Check Stock?");
            builder.setPositiveButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        } else if (a == 5) {
            builder.setMessage("Do You Want To Reset Data?");
            builder.setPositiveButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    db.deleteTable(DBHandler.Table_StockTakeMaster);
                    setData();
                }
            });
        } else if (a == 6) {
            builder.setMessage("Do You Want To Save Data And Continue Later?");
            builder.setPositiveButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    getData();
                }
            });
        } else if (a == 7) {
            builder.setMessage("Data Saved Locally You Can Edit Later.");
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    finish();
                }
            });
        }
        builder.create().show();
    }

    private void writeLog(String _data) {
        new WriteLog().writeLog(getApplicationContext(), "StockTakeActivity_" + _data);
    }
}
