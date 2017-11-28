package com.lnbinfotech.msplfootwear;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.lnbinfotech.msplfootwear.connectivity.ConnectivityTest;
import com.lnbinfotech.msplfootwear.constant.Constant;
import com.lnbinfotech.msplfootwear.db.DBHandler;
import com.lnbinfotech.msplfootwear.interfaces.ServerCallback;
import com.lnbinfotech.msplfootwear.log.WriteLog;
import com.lnbinfotech.msplfootwear.model.SizeNDesignClass;
import com.lnbinfotech.msplfootwear.model.StockInfoMasterClass;
import com.lnbinfotech.msplfootwear.post.Post;
import com.lnbinfotech.msplfootwear.volleyrequests.VolleyRequests;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.NumberFormat;
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
    private ProgressDialog pd1;
    private int maxProdId = 0;


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
                    //0-AreaMaster,1-BankMaster,2-BankBranchMaster,3-CityMaster
                    //4-CompanyMaster,5-CustomerMaster,6-DocumentMaster,7-EmployeeMaster,8-HOMaster
                    //9-ProductMaster,10-LoadAllSizeNDesign,11-StockMaster,12-GSTMaster
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
                    }else if(i == 11){
                        refreshDataDia(11);
                    }else if(i == 12){
                        refreshDataDia(12);
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
                new readJSON(result,"StockInfo");
            }

            @Override
            public void onFailure(String result) {
                constant.showPD();
                showDia(2);
            }
        });
    }

    private void loadSizeNDesignMaster(int from, int to){
        String url = Constant.ipaddress+"/GetAllRequiredSizesdesigns?Id="+from+"&ToId="+to;
        Constant.showLog(url);
        writeLog("loadSizeNDesignMaster_"+url);
        constant.showPD();
        new getSizeNDesignMaster().execute(url);
        /*VolleyRequests requests = new VolleyRequests(DataRefreshActivity.this);
        requests.refreshSizeNDesignMaster1(url, new ServerCallback() {
            @Override
            public void onSuccess(String result) {
                new readJSON(result,"SizeNDesign").execute();
            }

            @Override
            public void onFailure(String result) {
                constant.showPD();
                showDia(2);
                writeLog("loadSizeNDesignMaster_onFailure_"+result);
            }
        });*/
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
        //int a = db.getCustMax();
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

    private void loadGSTMaster(){
        String url = Constant.ipaddress+"/GetGSTMaster?Id=0";
        Constant.showLog(url);
        writeLog("loadGSTMaster_" + url);
        constant.showPD();
        VolleyRequests requests = new VolleyRequests(DataRefreshActivity.this);
        requests.refreshGSTMaster(url, new ServerCallback() {
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
        refreshList.add("Area Master");
        refreshList.add("Bank Master");
        refreshList.add("Bank's Branch Master");
        refreshList.add("City Master");
        refreshList.add("Company Master");
        refreshList.add("Customer Master");
        refreshList.add("Document Master");
        refreshList.add("Employee Master");
        refreshList.add("HOMaster Master");
        refreshList.add("Product Master");
        refreshList.add("SizeAndDesign Master");
        refreshList.add("Stock Master");
        refreshList.add("GST Master");
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
            builder.setMessage("Error While Loading Data");
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
                //0-AreaMaster,1-BankMaster,2-BankBranchMaster,3-CityMaster
                //4-CompanyMaster,5-CustomerMaster,6-DocumentMaster,7-EmployeeMaster,8-HOMaster
                //9-ProductMaster,10-LoadAllSizeNDesign,11-StockMaster,12-GSTMaster
                if (a == 0) {
                    loadAreaMaster();
                }else if (a == 1) {
                    loadBankMaster();
                }else if (a == 2) {
                    loadBankBranchMaster();
                }else if (a == 3) {
                    loadCityMaster();
                }else if (a == 4) {
                    loadCompanyMaster();
                }else if (a == 5) {
                    loadCustomerMaster();
                }else if (a == 6) {
                    loadDocumentMaster();
                }else if(a == 7){
                    loadEmployeeMaster();
                }else if(a == 8){
                    loadHOMaster();
                }else if(a == 9){
                    loadProductMaster();
                }else if(a == 10){
                    maxProdId = db.getMaxProdId();
                    loadSizeNDesignMaster(0,100);
                }else if(a == 11){
                    loadStockInfo();
                }else if(a == 12){
                    loadGSTMaster();
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

    private class getSizeNDesignMaster extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... strings) {
            return Post.POST(strings[0]);
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);
            response = response.substring(1, response.length() - 1);
            constant.showPD();
            new readJSON(response,"SizeNDesign").execute();
        }
    }

    private class readJSON extends AsyncTask<Void,Void,String> {
        private String result, parseType;
        private ProgressDialog pd;

        readJSON(String _result,String _parseType){
            this.result = _result;
            this.parseType = _parseType;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(DataRefreshActivity.this);
            pd.setCancelable(false);
            pd.setMessage("Preparing To Download");
            pd.show();
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
                    retValue = "A";
                }
                writer.flush();
                writer.close();
                return retValue;
            }catch (IOException | OutOfMemoryError e){
                pd.dismiss();
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
            pd.dismiss();
            if(s.equals("A")){
                new writeDB(parseType).execute();
            }else {
                showDia(2);
            }
        }
    }

    private class writeDB extends AsyncTask<Void,String,String> {
        private File writeFile;
        private String parseType;

        public writeDB(String _parseType) {
            this.parseType = _parseType;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd1 = new ProgressDialog(DataRefreshActivity.this);
            pd1.setCancelable(false);
            pd1.setProgressNumberFormat(null);
            pd1.setProgressPercentFormat(null);
            pd1.setProgressNumberFormat("%1d/%2d");
            NumberFormat percentInstance = NumberFormat.getPercentInstance();
            percentInstance.setMaximumFractionDigits(0);
            pd1.setProgressPercentFormat(percentInstance);
            pd1.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            pd1.setTitle("Please Wait");
            pd1.setMessage("It will take app. 10-15 min");
            pd1.show();
        }

        @Override
        protected String doInBackground(Void... voids) {
            File sdFile = Constant.checkFolder(Constant.folder_name);
            JsonFactory f = new JsonFactory();
            try {
                writeFile = new File(sdFile, writeFilename);
                JsonParser jp = f.createJsonParser(writeFile);
                if (parseType.equals("SizeNDesign")) {
                    parseSizeNDesign(jp,pd1);
                } else if (parseType.equals("StockInfo")) {
                    db.deleteTable(DBHandler.Table_StockInfo);
                    parseStockInfo(jp);
                }
                return "";
            }catch (Exception e){
                pd1.dismiss();
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
            pd1.dismiss();
            if(s.equals("")) {
                if(writeFile.delete()){
                    Constant.showLog("Write Delete");
                    showDia(1);
                }
            }else{
                showDia(2);
            }
        }
    }

    private void parseSizeNDesign(JsonParser jp,ProgressDialog pd){
        try {
            int count = 0;
            List<SizeNDesignClass> list = new ArrayList<>();
            while (jp.nextToken() != JsonToken.END_ARRAY) {
                count ++;
                SizeNDesignClass sizeNDesignClass = new SizeNDesignClass();
                while (jp.nextToken() != JsonToken.END_OBJECT) {
                    String token = jp.getCurrentName();
                    if (DBHandler.ARSD_Productid.equals(token)) {
                        jp.nextToken();
                        sizeNDesignClass.setProductid(jp.getValueAsInt());
                    } else if (DBHandler.ARSD_DesignNo.equals(token)) {
                        jp.nextToken();
                        sizeNDesignClass.setDesignNo(jp.getText());
                    } else if (DBHandler.ARSD_Colour.equals(token)) {
                        jp.nextToken();
                        sizeNDesignClass.setColour(jp.getText());
                    } else if (DBHandler.ARSD_SizeGroup.equals(token)) {
                        jp.nextToken();
                        sizeNDesignClass.setSizeGroup(jp.getText());
                    } else if (DBHandler.ARSD_typ.equals(token)) {
                        jp.nextToken();
                        sizeNDesignClass.setTyp(jp.getText());
                    } else if (DBHandler.ARSD_SizeFrom.equals(token)) {
                        jp.nextToken();
                        sizeNDesignClass.setSizeFrom(jp.getValueAsInt());
                    } else if (DBHandler.ARSD_SizeTo.equals(token)) {
                        jp.nextToken();
                        sizeNDesignClass.setSizeTo(jp.getValueAsInt());
                    }else if (DBHandler.ARSD_GSTGroup.equals(token)) {
                        jp.nextToken();
                        sizeNDesignClass.setGSTGroup(jp.getText());
                    }else if (DBHandler.ARSD_InOutType.equals(token)) {
                        jp.nextToken();
                        sizeNDesignClass.setInOutType(jp.getText());
                    }else if (DBHandler.ARSD_Total.equals(token)) {
                        jp.nextToken();
                        sizeNDesignClass.setTotal(jp.getValueAsInt());
                    }else if (DBHandler.ARSD_HashCode.equals(token)) {
                        jp.nextToken();
                        sizeNDesignClass.setHashCode(jp.getText());
                    }/* else if (DBHandler.ARSD_Cat1.equals(token)) {
                        jp.nextToken();
                        sizeNDesignClass.setCat1(jp.getText());
                    } else if (DBHandler.ARSD_Cat2.equals(token)) {
                        jp.nextToken();
                        sizeNDesignClass.setCat2(jp.getText());
                    } else if (DBHandler.ARSD_Cat3.equals(token)) {
                        jp.nextToken();
                        sizeNDesignClass.setCat3(jp.getText());
                    } else if (DBHandler.ARSD_Cat4.equals(token)) {
                        jp.nextToken();
                        sizeNDesignClass.setCat4(jp.getText());
                    } else if (DBHandler.ARSD_Cat5.equals(token)) {
                        jp.nextToken();
                        sizeNDesignClass.setCat5(jp.getText());
                    } else if (DBHandler.ARSD_Cat6.equals(token)) {
                        jp.nextToken();
                        sizeNDesignClass.setCat6(jp.getText());
                    } else if (DBHandler.ARSD_Final_prod.equals(token)) {
                        jp.nextToken();
                        sizeNDesignClass.setFinal_prod(jp.getText());
                    } else if (DBHandler.ARSD_Uom.equals(token)) {
                        jp.nextToken();
                        sizeNDesignClass.setUom(jp.getText());
                    } else if (DBHandler.ARSD_Vat.equals(token)) {
                        jp.nextToken();
                        sizeNDesignClass.setVat(jp.getText());
                    }else if (DBHandler.ARSD_ActualInw.equals(token)) {
                        jp.nextToken();
                        sizeNDesignClass.setActualInw(jp.getText());
                    }*/
                }
                list.add(sizeNDesignClass);
                //db.addSizeNDesignMaster(sizeNDesignClass);
                pd.setProgress((count*100)/62412);
            }
            if(list.size()!=0){
                db.deleteTable(DBHandler.Table_AllRequiredSizesDesigns);
            }
            db.addSizeNDesignMaster(list);
            Constant.showLog(""+count);
        } catch (Exception e) {
            e.printStackTrace();
            constant.showPD();
            showDia(2);
        }
    }

    private void parseStockInfo(JsonParser jp) {
        try {
            while (jp.nextToken() != JsonToken.END_ARRAY) {
                StockInfoMasterClass stockInfo = new StockInfoMasterClass();
                while (jp.nextToken() != JsonToken.END_OBJECT) {
                    String token = jp.getCurrentName();
                    if ("Company".equals(token)) {
                        jp.nextToken();
                        stockInfo.setCompany(jp.getText());
                    } else if ("ProductId".equals(token)) {
                        jp.nextToken();
                        stockInfo.setProductId(jp.getText());
                    } else if ("Color".equals(token)) {
                        jp.nextToken();
                        stockInfo.setColor(jp.getText());
                    } else if ("Size".equals(token)) {
                        jp.nextToken();
                        stockInfo.setSize(jp.getText());
                    } else if ("Rate".equals(token)) {
                        jp.nextToken();
                        stockInfo.setRate(jp.getText());
                    } else if ("LQty".equals(token)) {
                        jp.nextToken();
                        String a = jp.getText();
                        String[] b = a.split("\\.");
                        stockInfo.setLQty(Integer.parseInt(b[0]));
                    } else if ("PQty".equals(token)) {
                        jp.nextToken();
                        String a = jp.getText();
                        String[] b = a.split("\\.");
                        stockInfo.setPQty(Integer.parseInt(b[0]));
                    } else if ("PackUnpack".equals(token)) {
                        jp.nextToken();
                        stockInfo.setPackUnpack(jp.getText());
                    } else if ("PerPackQty".equals(token)) {
                        jp.nextToken();
                        String a = jp.getText();
                        String[] b = a.split("\\.");
                        stockInfo.setPerPackQty(Integer.parseInt(b[0]));
                    } else if ("SaleRate".equals(token)) {
                        jp.nextToken();
                        stockInfo.setSaleRate(jp.getText());
                    } else if ("Product_id".equals(token)) {
                        jp.nextToken();
                        String a = jp.getText();
                        String[] b = a.split("\\.");
                        stockInfo.setProduct_id(Integer.parseInt(b[0]));
                    }
                }
                db.addStockInfo(stockInfo);
            }
        } catch (Exception e) {
            e.printStackTrace();
            constant.showPD();
            showDia(2);
        }
    }

    private void writeLog(String _data){
        new WriteLog().writeLog(getApplicationContext(),"DataRefreshActivity_"+_data);
    }
}
