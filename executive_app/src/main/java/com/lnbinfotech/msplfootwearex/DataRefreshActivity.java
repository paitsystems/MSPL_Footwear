package com.lnbinfotech.msplfootwearex;

import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.view.Gravity;

import com.lnbinfotech.msplfootwearex.connectivity.ConnectivityTest;
import com.lnbinfotech.msplfootwearex.constant.Constant;
import com.lnbinfotech.msplfootwearex.db.DBHandler;
import com.lnbinfotech.msplfootwearex.interfaces.ServerCallback;
import com.lnbinfotech.msplfootwearex.log.WriteLog;
import com.lnbinfotech.msplfootwearex.model.StockInfoMasterClass;
import com.lnbinfotech.msplfootwearex.volleyrequests.VolleyRequests;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class DataRefreshActivity extends AppCompatActivity implements View.OnClickListener {

    private Constant constant, constant1;
    private Toast toast;
    private ListView listView;
    private List<String> refreshList;
    private String writeFilename = "Write.txt";
    private DBHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_refresh);

        init();

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(ConnectivityTest.getNetStat(DataRefreshActivity.this)) {
                    if (i == 0) {
                        refreshDataDia(0);
                    } else if (i == 1) {
                        refreshDataDia(1);
                    } else if (i == 2) {
                        refreshDataDia(2);
                    } else if (i == 3) {
                        refreshDataDia(3);
                    } else if (i == 4) {
                        refreshDataDia(4);
                    } else if (i == 5) {
                        refreshDataDia(5);
                    } else if (i == 6) {
                        refreshDataDia(6);
                    }else if (i == 7) {
                        refreshDataDia(7);
                    }else if (i == 8) {
                        refreshDataDia(8);
                    }else if(i == 9){
                        refreshDataDia(9);
                    }else if(i == 10){
                        refreshDataDia(10);
                    }
                }else{
                    toast.setText("You Are Offline");
                    toast.show();
                }
            }
        });
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
        new Constant(DataRefreshActivity.this).doFinish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //showDia(0);
                new Constant(DataRefreshActivity.this).doFinish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadAreaMaster(){
        String url = Constant.ipaddress+"/GetAreaMaster?Id=0";
        Constant.showLog(url);
        writeLog("loadAreaMaster_"+url);
        constant.showPD();
        VolleyRequests requests = new VolleyRequests(DataRefreshActivity.this);
        requests.refreshAreaMaster(url, new ServerCallback() {
            @Override
            public void onSuccess(String result) {
                constant.showPD();
                showDia(1);
            }

            @Override
            public void onFailure(String result) {
                constant.showPD();
                showDia(2);
            }
        });
    }

    private void loadCityMaster(){
        String url = Constant.ipaddress+"/GetCityMaster?Id=0";
        Constant.showLog(url);
        writeLog("loadCityMaster_"+url);
        constant.showPD();
        VolleyRequests requests = new VolleyRequests(DataRefreshActivity.this);
        requests.refreshCityMaster(url, new ServerCallback() {
            @Override
            public void onSuccess(String result) {
                constant.showPD();
                showDia(1);
            }

            @Override
            public void onFailure(String result) {
                constant.showPD();
                showDia(2);
            }
        });
    }

    private void loadHOMaster(){
        String url = Constant.ipaddress+"/GetHOMaster?Id=0";
        Constant.showLog(url);
        writeLog("loadHOMaster_"+url);
        constant.showPD();
        VolleyRequests requests = new VolleyRequests(DataRefreshActivity.this);
        requests.refreshHOMaster(url, new ServerCallback() {
            @Override
            public void onSuccess(String result) {
                constant.showPD();
                showDia(1);
            }

            @Override
            public void onFailure(String result) {
                constant.showPD();
                showDia(2);
            }
        });
    }

    private void loadEmployeeMaster(){
        String url = Constant.ipaddress+"/GetEmployeeMaster?Id=0";
        Constant.showLog(url);
        writeLog("loadEmployeeMaster_"+url);
        constant.showPD();
        VolleyRequests requests = new VolleyRequests(DataRefreshActivity.this);
        requests.refreshEmployeeMaster(url, new ServerCallback() {
            @Override
            public void onSuccess(String result) {
                constant.showPD();
                showDia(1);
            }

            @Override
            public void onFailure(String result) {
                constant.showPD();
                showDia(2);
            }
        });
    }

    private void loadStockInfo(){
        String url = Constant.ipaddress+"/GetStockInfo?Id=0";
        Constant.showLog(url);
        writeLog("loadStockInfo_"+url);
        constant.showPD();
        VolleyRequests requests = new VolleyRequests(DataRefreshActivity.this);
        requests.refreshStockInfo(url, new ServerCallback() {
            @Override
            public void onSuccess(String result) {
                AtomicInteger workInteger = new AtomicInteger(1);
                new readJSON(workInteger,result).execute();
                //showDia(1);
            }

            @Override
            public void onFailure(String result) {
                constant.showPD();
                showDia(2);
            }
        });
    }

    private void loadProductMaster(){
        String url = Constant.ipaddress+"/GetProductMaster?Id=0";
        Constant.showLog(url);
        writeLog("loadProductMaster_"+url);
        constant.showPD();
        VolleyRequests requests = new VolleyRequests(DataRefreshActivity.this);
        requests.refreshProductMaster(url, new ServerCallback() {
            @Override
            public void onSuccess(String result) {
                constant.showPD();
                showDia(1);
            }

            @Override
            public void onFailure(String result) {
                constant.showPD();
                showDia(2);
            }
        });
    }

    private void loadCustomerMaster(){
        String url = Constant.ipaddress+"/GetCustomerMaster?Id=0";
        Constant.showLog(url);
        writeLog("loadCustomerMaster_" + url);
        constant.showPD();
        VolleyRequests requests = new VolleyRequests(DataRefreshActivity.this);
        requests.refreshCustomerMaster(url, new ServerCallback() {
            @Override
            public void onSuccess(String result) {
                constant.showPD();
                showDia(1);
            }
            @Override
            public void onFailure(String result) {
                constant.showPD();
                showDia(2);
            }
        });
    }

    private void loadCompanyMaster(){
        db.createCompanyMaster();
        String url = Constant.ipaddress+"/GetCompanyMaster?Id=0";
        Constant.showLog(url);
        writeLog("loadCompanyMaster_" + url);
        constant.showPD();
        VolleyRequests requests = new VolleyRequests(DataRefreshActivity.this);
        requests.refreshCompanyMaster(url, new ServerCallback() {
            @Override
            public void onSuccess(String result) {
                constant.showPD();
                showDia(1);
            }
            @Override
            public void onFailure(String result) {
                constant.showPD();
                showDia(2);
            }
        });
    }

    private void loadBankMaster(){
        db.createBankMaster();
        String url = Constant.ipaddress+"/GetBankMaster?Id=0";
        Constant.showLog(url);
        writeLog("loadBankMaster_" + url);
        constant.showPD();
        VolleyRequests requests = new VolleyRequests(DataRefreshActivity.this);
        requests.refreshBankMaster(url, new ServerCallback() {
            @Override
            public void onSuccess(String result) {
                constant.showPD();
                showDia(1);
            }
            @Override
            public void onFailure(String result) {
                constant.showPD();
                showDia(2);
            }
        });
    }

    private void loadBankBranchMaster(){
        db.createBankBranchMaster();
        String url = Constant.ipaddress+"/GetBankBranchMaster?Id=0";
        Constant.showLog(url);
        writeLog("loadBankBranchMaster_" + url);
        constant.showPD();
        VolleyRequests requests = new VolleyRequests(DataRefreshActivity.this);
        requests.refreshBankBranchMaster(url, new ServerCallback() {
            @Override
            public void onSuccess(String result) {
                constant.showPD();
                showDia(1);
            }

            @Override
            public void onFailure(String result) {
               constant.showPD();
                showDia(2);
            }
        });

    }

    private void loadDocumentMaster(){
        db.createDocumentMaster();
        String url = Constant.ipaddress+"/GetDocumentMaster?Id=0";
        Constant.showLog(url);
        writeLog("loadDocumentMaster_" + url);
        constant.showPD();
        VolleyRequests requests = new VolleyRequests(DataRefreshActivity.this);
        requests.refreshDocumentMaster(url, new ServerCallback() {
            @Override
            public void onSuccess(String result) {
                constant.showPD();
                showDia(1);
            }

            @Override
            public void onFailure(String result) {
                constant.showPD();
                showDia(2);
            }
        });

    }

    private void init() {
        db = new DBHandler(DataRefreshActivity.this);
        constant = new Constant(DataRefreshActivity.this);
        constant1 = new Constant(getApplicationContext());
        toast = Toast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        listView = (ListView) findViewById(R.id.listView);
        refreshList = new ArrayList<>();
        refreshList.add("Area  Master");refreshList.add("City Master");
        refreshList.add("Customer Master");refreshList.add("Employee Master");
        refreshList.add("HOMaster Master");refreshList.add("Product Master");
        refreshList.add("Stock Master");
        refreshList.add("Company Master");
        refreshList.add("Bank Master");
        refreshList.add("Bank_Branch Master");
        refreshList.add("Document Master");
        listView.setAdapter(new ArrayAdapter<>(getApplicationContext(),R.layout.list_item_data_refresh,refreshList));
    }

    private void showDia(int a) {
        AlertDialog.Builder builder = new AlertDialog.Builder(DataRefreshActivity.this);
        builder.setCancelable(false);
        if (a == -1) {
            builder.setTitle(R.string.somethingwentwrong);
            builder.setMessage(R.string.pleasecontactyouradministrator);
            builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        }else if (a == 0) {
            builder.setMessage("Do You Want To Exit App?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    new Constant(DataRefreshActivity.this).doFinish();
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
            builder.setMessage("Data Refreshed Successfully");
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        }else if (a == 2) {
            builder.setMessage("Error While Loading Data?");
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        }
        builder.create().show();
    }

    private void refreshDataDia(final int a) {
        AlertDialog.Builder builder = new AlertDialog.Builder(DataRefreshActivity.this);
        builder.setCancelable(false);
        builder.setMessage("Do You Want To Refresh Data?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (a == 0) {
                    loadAreaMaster();
                }else if (a == 1) {
                    loadCityMaster();
                }else if (a == 2) {
                    loadCustomerMaster();
                }else if (a == 3) {
                    loadEmployeeMaster();
                }else if (a == 4) {
                    loadHOMaster();
                }else if (a == 5) {
                    loadProductMaster();
                }else if (a == 6) {
                    loadStockInfo();
                }else if(a == 7){
                    loadCompanyMaster();
                }else if(a == 8){
                    loadBankMaster();
                }else if(a == 9){
                    loadBankBranchMaster();
                }else if(a == 10){
                    loadDocumentMaster();
                }
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    private class readJSON extends AsyncTask<Void,Void,String> {
        private final AtomicInteger workCounter1;
        String result;

        readJSON(AtomicInteger _workCounter,String _result){
            workCounter1 = _workCounter;
            result = _result;
        }

        @Override
        protected String doInBackground(Void... voids) {
            String retValue = "A";
            File sdFile = Constant.checkFolder(Constant.folder_name);
            FileWriter writer;
            try {
                String search = "\\\\",replace = "";
                File writeFile = new File(sdFile,writeFilename);
                writer = new FileWriter(writeFile);
                int size = result.length();
                if(size>2) {
                    Log.d("Log","Replacing");
                    int b = 50000;
                    for (int i = 0; i < size; i++) {
                        if (b >= size) {
                            b = size;
                        }
                        String q = result.substring(i, b);
                        String g = q.replaceAll(search, replace);
                        System.gc();
                        writer.append(g);
                        i = b - 1;
                        b = b + 50000;
                    }
                    retValue = "";
                }
                writer.flush();
                writer.close();
                return retValue;
            }catch (IOException | OutOfMemoryError e){
                try {
                    writer = new FileWriter(new File(sdFile, "Log.txt"));
                    writer.append(e.getMessage());
                    writer.flush();
                    writer.close();
                }catch (Exception e1){
                    e.printStackTrace();
                }
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            int tasksLeft = this.workCounter1.decrementAndGet();
            if(tasksLeft==0 && s.equals("")){
                AtomicInteger workInteger = new AtomicInteger(1);
                new writeDB(workInteger).execute();
            }else if(tasksLeft==0){
                //showDia(2);
            }
        }
    }

    private class writeDB extends AsyncTask<Void,Void,String> {
        private final AtomicInteger workCounter2;
        File writeFile;

        writeDB(AtomicInteger _workCounter) {
            workCounter2 = _workCounter;
        }

        @Override
        protected String doInBackground(Void... voids) {
            File sdFile = Constant.checkFolder(Constant.folder_name);
            JsonFactory f = new JsonFactory();
            try {
                writeFile = new File(sdFile, writeFilename);
                JsonParser jp = f.createJsonParser(writeFile);
                int count = 0;
                DBHandler db = new DBHandler(getApplicationContext());
                db.deleteTable(DBHandler.Table_StockInfo);
                while (jp.nextToken() != JsonToken.END_ARRAY) {
                    count++;
                    StockInfoMasterClass stockInfo = new StockInfoMasterClass();
                    while (jp.nextToken() != JsonToken.END_OBJECT) {
                        String token = jp.getCurrentName();
                        if("Company".equals(token)){
                            jp.nextToken();
                            stockInfo.setCompany(jp.getText());
                        }else if("ProductId".equals(token)){
                            jp.nextToken();
                            stockInfo.setProductId(jp.getText());
                        }else if("Color".equals(token)){
                            jp.nextToken();
                            stockInfo.setColor(jp.getText());
                        }else if("Size".equals(token)) {
                            jp.nextToken();
                            stockInfo.setSize(jp.getText());
                        }else if("Rate".equals(token)) {
                            jp.nextToken();
                            stockInfo.setRate(jp.getText());
                        }else if("LQty".equals(token)) {
                            jp.nextToken();
                            String a = jp.getText();
                            String[] b = a.split("\\.");
                            stockInfo.setLQty(Integer.parseInt(b[0]));
                        }else if("PQty".equals(token)) {
                            jp.nextToken();
                            String a = jp.getText();
                            String[] b = a.split("\\.");
                            stockInfo.setPQty(Integer.parseInt(b[0]));
                        }else if("PackUnpack".equals(token)) {
                            jp.nextToken();
                            stockInfo.setPackUnpack(jp.getText());
                        }else if("PerPackQty".equals(token)) {
                            jp.nextToken();
                            String a = jp.getText();
                            String[] b = a.split("\\.");
                            stockInfo.setPerPackQty(Integer.parseInt(b[0]));
                        }else if("SaleRate".equals(token)) {
                            jp.nextToken();
                            stockInfo.setSaleRate(jp.getText());
                        }else if("Product_id".equals(token)) {
                            jp.nextToken();
                            String a = jp.getText();
                            String[] b = a.split("\\.");
                            stockInfo.setProduct_id(Integer.parseInt(b[0]));
                        }
                    }
                    db.addStockInfo(stockInfo);
                }
                Log.d("Log",""+count);
                return "";
            }catch (Exception e){
                try {
                    FileWriter writer = new FileWriter(new File(sdFile, "Log.txt"));
                    writer.append(e.getMessage());
                    writer.flush();
                    writer.close();
                }catch (Exception e1){
                    e.printStackTrace();
                    return null;
                }
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            int tasksLeft = this.workCounter2.decrementAndGet();
            if(tasksLeft==0 && s.equals("")) {
                if(writeFile.delete()){
                    Log.d("Log","Write Delete");
                    constant.showPD();
                    showDia(1);
                }
                //showDia(3);
            }else if(tasksLeft==0) {
                if(writeFile.delete()){
                    Log.d("Log","Write Delete");
                }
                //showDia(4);
            }
        }
    }

    private void writeLog(String _data){
        new WriteLog().writeLog(getApplicationContext(),"DataRefreshActivity_"+_data);
    }
}
