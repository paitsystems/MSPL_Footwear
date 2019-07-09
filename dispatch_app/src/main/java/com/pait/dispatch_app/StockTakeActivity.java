package com.pait.dispatch_app;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.ListView;
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

public class StockTakeActivity extends AppCompatActivity implements View.OnClickListener,TestInterface {

    private int hoCode, dpID, empId, designId = 0, branchId, requestCode = 0, mYear, mMonth, mDay, roundFlag;
    private UserClass userClass;
    private DBHandler db;
    private Toast toast;
    private List<StockTakeClass> list;
    private StockTakeAdapter adapter;
    //private NonScrollListView listView;
    private ListView listView;
    private TextView tv_stDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_take_2);

        init();

        userClass = (UserClass) getIntent().getExtras().get("cust");
        empId = userClass.getCustID();
        hoCode = userClass.getHOCode();
        branchId = userClass.getBranchId();
        dpID = userClass.getDpId();
        designId = FirstActivity.pref.getInt(getString(R.string.pref_design), 0);

        if (db.getStockTakeMaxAuto() > 0) {
            showDia(3);
        } else {
            //setData();
            getStockTakeMaster(0);
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
                saveToLocal(0);
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
            case R.id.finalSave:
                saveToLocal(0);
                getData();
                break;
        }
        return super.onOptionsItemSelected(item);
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
            //TODO: Remove empid
            empId = 12;
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
                    getStockTakeMaster(body);
            call.enqueue(new Callback<List<StockTakeClass>>() {
                @Override
                public void onResponse(Call<List<StockTakeClass>> call, Response<List<StockTakeClass>> response) {
                    Constant.showLog("onResponse");
                    list = response.body();
                    if (list != null) {
                        db.deleteTable(DBHandler.Table_StockTakeMaster);
                        db.addStockTakeMaster(list);
                        loadFromLocal(0);
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

    private void updateStockTakeMaster(int type) {
        final Constant constant = new Constant(StockTakeActivity.this);
        constant.showPD();
        try {
            int maxAuto = db.getMaxAuto();
            //Auto + "|"+ CustId + "|"+ HOCode + "|"+ dispatchId + "|"+ empId + "|"+ type
            //TODO: Remove empid
            empId = 12;
            String url = maxAuto + "|" + 0 + "|" + hoCode + "|" + dpID + "|" + empId + "|" +
                    type + "|" + designId + "|" + tv_stDate.getText().toString();
            writeLog("updateStockTakeMaster_" + url);
            final JSONObject jsonBody = new JSONObject();
            jsonBody.put("details", url);
            RequestBody body = RequestBody.create(okhttp3.MediaType.
                    parse("application/json; charset=utf-8"), (jsonBody).toString());
            Constant.showLog(jsonBody.toString());
            list.clear();
            listView.setAdapter(null);

            Call<List<StockTakeClass>> call = new RetrofitApiBuilder().getApiBuilder().
                    create(RetrofitApiInterface.class).
                    getStockTakeMaster(body);
            call.enqueue(new Callback<List<StockTakeClass>>() {
                @Override
                public void onResponse(Call<List<StockTakeClass>> call, Response<List<StockTakeClass>> response) {
                    Constant.showLog("onResponse");
                    list = response.body();
                    if (list != null) {
                        db.updateStockTakeMaster(list);
                        loadFromLocal(1);
                        Constant.showLog(list.size() + "_updateStockTakeMaster");
                    } else {
                        Constant.showLog("onResponse_list_null");
                        writeLog("updateStockTakeMaster_onResponse_list_null");
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
                    writeLog("updateStockTakeMaster_onFailure_" + t.getMessage());
                    constant.showPD();
                    showDia(2);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            writeLog("updateStockTakeMaster_" + e.getMessage());
            constant.showPD();
            showDia(2);
        }
    }

    private void setData(int checkStock) {
        if (!list.isEmpty()) {
            adapter = new StockTakeAdapter(getApplicationContext(), list, checkStock);
            adapter.initInterface(StockTakeActivity.this);
            listView.setAdapter(adapter);
        }
    }

    private void loadFromLocal(int checkStock) {
        list.clear();
        Cursor res = db.getStockData();
        if (res.moveToFirst()) {
            do {
                StockTakeClass st = new StockTakeClass();
                st.setAuto(res.getString(res.getColumnIndex(DBHandler.ST_Auto)));
                st.setItemid(res.getString(res.getColumnIndex(DBHandler.ST_Product_id)));
                st.setProductId(res.getString(res.getColumnIndex(DBHandler.ST_ProductId)));
                st.setPackQty(res.getString(res.getColumnIndex(DBHandler.ST_PackQty)));
                st.setLooseQty(res.getString(res.getColumnIndex(DBHandler.ST_LooseQty)));
                st.setTotalQty(res.getString(res.getColumnIndex(DBHandler.ST_TotalQty)));
                st.setStockQty(res.getString(res.getColumnIndex(DBHandler.ST_StockQty)));
                st.setHOCode(res.getString(res.getColumnIndex(DBHandler.ST_HOCode)));
                st.setBranchid(res.getString(res.getColumnIndex(DBHandler.ST_BranchId)));
                st.setCrBy(res.getString(res.getColumnIndex(DBHandler.ST_CrBy)));
                st.setNoOfPices(res.getString(res.getColumnIndex(DBHandler.ST_NoOfPieces)));
                st.setChecker(res.getString(res.getColumnIndex(DBHandler.ST_Checker)));
                st.setPacker(res.getString(res.getColumnIndex(DBHandler.ST_Packer)));
                st.setRound(res.getString(res.getColumnIndex(DBHandler.ST_Round)));
                roundFlag = res.getInt(res.getColumnIndex(DBHandler.ST_Round));
                st.setAllotDate(res.getString(res.getColumnIndex(DBHandler.ST_AllotDate)));
                list.add(st);
            } while (res.moveToNext());
        }
        res.close();
        setData(checkStock);
    }

    private void saveToLocal(int a) {
        if (!list.isEmpty()) {
            list = adapter.getDataSet();
            db.deleteTable(DBHandler.Table_StockTakeMaster);
            db.addStockTakeMaster(list);
            toast.setText("Data Save Locally...");
            toast.show();
            loadFromLocal(0);
            if (a == 1) {
                showDia(7);
            }
        }
    }

    private void getData() {
        if (!list.isEmpty()) {
            String data = "", allotDate = "", branchId = "0", checker = "0", packer = "0", crBy = "0", round = "0";

            for (StockTakeClass st : list) {
                data = data + st.getItemid() + "^" + st.getProductId() + "^" + st.getNoOfPices() + "^" +
                        st.getStockQty() + "^" + st.getPackQty() + "^" + st.getLooseQty() + "^" + st.getTotalQty() + ",";
                allotDate = st.getAllotDate();
                branchId = st.getBranchid();
                checker = st.getChecker();
                packer = st.getPacker();
                crBy = st.getCrBy();
                round = st.getRound();
            }
            if (data.length() > 1) {
                data = data.substring(0, data.length() - 1);
            }
            String url = 1 + "|" + allotDate + "|" + branchId + "|" + checker + "|" + packer + "|" + crBy + "|" + data;
            Constant.showLog(url);
            if(roundFlag == 0) {
                new saveStockDetail(branchId).execute(url);
            } else {
                new updateStockDetail(branchId).execute(url);
            }
        }
    }

    private class saveStockDetail extends AsyncTask<String, Void, String> {
        private String branchId = "0";
        private ProgressDialog pd;

        private saveStockDetail(String _branchId) {
            this.branchId = _branchId;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(StockTakeActivity.this);
            pd.setMessage("Please Wait...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... url) {
            String value = "";
            DefaultHttpClient httpClient = null;
            HttpPost request = new HttpPost(Constant.ipaddress + "/saveStockTakeDetail");
            request.setHeader("Accept", "application/json");
            request.setHeader("Content-type", "application/json");
            try {
                JSONStringer vehicle = new JSONStringer().object().key("rData").object().key("details").value(url[0]).endObject().endObject();
                StringEntity entity = new StringEntity(vehicle.toString());
                Constant.showLog(vehicle.toString());
                writeLog("saveStockDetail_" + vehicle.toString());
                request.setEntity(entity);
                //TODO : Check Timeout
                HttpParams httpParams = new BasicHttpParams();
                HttpConnectionParams.setConnectionTimeout(httpParams, Constant.TIMEOUT_CON);
                HttpConnectionParams.setSoTimeout(httpParams, Constant.TIMEOUT_SO);
                httpClient = new DefaultHttpClient(httpParams);
                //DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpResponse response = httpClient.execute(request);
                Constant.showLog("Saving : " + response.getStatusLine().getStatusCode());
                value = new BasicResponseHandler().handleResponse(response);
                //return Post.POST(url[0]);
            } catch (Exception e) {
                e.printStackTrace();
                writeLog("saveStockDetail_result_" + e.getMessage());
            } finally {
                try {
                    if (httpClient != null) {
                        httpClient.getConnectionManager().shutdown();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    writeLog("saveStockDetail_finally_" + e.getMessage());
                }
            }
            return value;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                Constant.showLog(result);
                String str = new JSONObject(result).getString("SaveStockTakeDetailResult");
                str = str.replace("\"", "");
                Constant.showLog(str);
                pd.dismiss();
                writeLog("saveStockDetail_result_" + str + "_" + result);
                String[] retAutoBranchId = str.split("\\-");
                if (retAutoBranchId.length > 1) {
                    if (!retAutoBranchId[0].equals("0") && !retAutoBranchId[0].equals("+2") && !retAutoBranchId[0].equals("+3")) {
                        showDia(9);
                    } else {
                        showDia(10);
                    }
                } else {
                    showDia(10);
                }
                //counter++;
                //saveCustOrder();
            } catch (Exception e) {
                writeLog("saveStockDetail_" + e.getMessage());
                e.printStackTrace();
                showDia(10);
                pd.dismiss();
            }
        }
    }

    private class updateStockDetail extends AsyncTask<String, Void, String> {
        private String branchId = "0";
        private ProgressDialog pd;

        private updateStockDetail(String _branchId) {
            this.branchId = _branchId;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(StockTakeActivity.this);
            pd.setMessage("Please Wait...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... url) {
            String value = "";
            DefaultHttpClient httpClient = null;
            HttpPost request = new HttpPost(Constant.ipaddress + "/updateStockTakeDetail");
            request.setHeader("Accept", "application/json");
            request.setHeader("Content-type", "application/json");
            try {
                JSONStringer vehicle = new JSONStringer().object().key("rData").object().key("details").value(url[0]).endObject().endObject();
                StringEntity entity = new StringEntity(vehicle.toString());
                Constant.showLog(vehicle.toString());
                writeLog("updateStockDetail_" + vehicle.toString());
                request.setEntity(entity);
                //TODO : Check Timeout
                HttpParams httpParams = new BasicHttpParams();
                HttpConnectionParams.setConnectionTimeout(httpParams, Constant.TIMEOUT_CON);
                HttpConnectionParams.setSoTimeout(httpParams, Constant.TIMEOUT_SO);
                httpClient = new DefaultHttpClient(httpParams);
                //DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpResponse response = httpClient.execute(request);
                Constant.showLog("Saving : " + response.getStatusLine().getStatusCode());
                value = new BasicResponseHandler().handleResponse(response);
                //return Post.POST(url[0]);
            } catch (Exception e) {
                e.printStackTrace();
                writeLog("updateStockDetail_result_" + e.getMessage());
            } finally {
                try {
                    if (httpClient != null) {
                        httpClient.getConnectionManager().shutdown();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    writeLog("updateStockDetail_finally_" + e.getMessage());
                }
            }
            return value;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                Constant.showLog(result);
                String str = new JSONObject(result).getString("UpdateStockTakeDetailResult");
                str = str.replace("\"", "");
                Constant.showLog(str);
                pd.dismiss();
                writeLog("updateStockDetail_result_" + str + "_" + result);
                String[] retAutoBranchId = str.split("\\-");
                if (retAutoBranchId.length > 1) {
                    if (!retAutoBranchId[0].equals("0") && !retAutoBranchId[0].equals("+2") && !retAutoBranchId[0].equals("+3")) {
                        showDia(9);
                    } else {
                        showDia(10);
                    }
                } else {
                    showDia(10);
                }
                //counter++;
                //saveCustOrder();
            } catch (Exception e) {
                writeLog("updateStockDetail_" + e.getMessage());
                e.printStackTrace();
                showDia(10);
                pd.dismiss();
            }
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
            builder.setPositiveButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            builder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    new Constant(StockTakeActivity.this).doFinish();
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
                    setData(0);
                }
            });
            builder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    loadFromLocal(0);
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
                    saveToLocal(0);
                    updateStockTakeMaster(0);
                }
            });
        } else if (a == 5) {
            builder.setMessage("Do You Want To Reset Data?");
            builder.setPositiveButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    getStockTakeMaster(0);
                }
            });
            builder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    db.deleteTable(DBHandler.Table_StockTakeMaster);
                    list.clear();
                    getStockTakeMaster(0);
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
                    saveToLocal(1);
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
                    db.deleteTable(DBHandler.Table_StockTakeMaster);
                    list.clear();
                    getStockTakeMaster(0);
                }
            });
        } else if (a == 9) {
            builder.setMessage("Round "+roundFlag+" Completed");
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    db.deleteTable(DBHandler.Table_StockTakeMaster);
                    list.clear();
                    getStockTakeMaster(roundFlag);

                }
            });
        } else if (a == 10) {
            builder.setMessage("Error While Saving Data");
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        }
        builder.create().show();
    }

    private void writeLog(String _data) {
        new WriteLog().writeLog(getApplicationContext(), "StockTakeActivity_" + _data);
    }
}
