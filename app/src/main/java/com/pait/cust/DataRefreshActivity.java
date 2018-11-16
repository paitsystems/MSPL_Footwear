package com.pait.cust;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.pait.cust.connectivity.ConnectivityTest;
import com.pait.cust.constant.Constant;
import com.pait.cust.db.DBHandler;
import com.pait.cust.interfaces.ServerCallback;
import com.pait.cust.log.WriteLog;
import com.pait.cust.model.BankBranchMasterClass;
import com.pait.cust.model.CustomerDetailClass;
import com.pait.cust.model.SizeDesignMastDetClass;
import com.pait.cust.model.SizeNDesignClass;
import com.pait.cust.model.StockInfoMasterClass;
import com.pait.cust.post.Post;
import com.pait.cust.volleyrequests.VolleyRequests;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DataRefreshActivity extends AppCompatActivity implements View.OnClickListener {

    private Constant constant, constant1;
    private Toast toast;
    private ListView listView;
    private List<String> refreshList;
    private String writeFilename = "Write.txt", prefname = "";;
    private DBHandler db;
    private ProgressDialog pd1;
    private int maxProdId = 0, maxSDMDAuto = 0;
    private ProgressDialog sndpd;
    private String areaMaster = "Area Master", bankMaster = "Bank Master", bankBrancMaster = "Bank's Branch Master",
                            cityMaster = "City Master", companyMaster = "Company Master", custMaster = "Customer Master",
                            docMaster = "Document Master", empMaster = "Employee Master", hoMaster = "HOMaster Master",
                            prodMaster = "Product Master", sizenDesignMaster = "SizeAndDesign Master", stockMaster = "Stock Master",
                            gstMaster = "GST Master", sdmdMaster = "SizeDetail Master";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(Constant.liveTestFlag==1) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }

        setContentView(R.layout.activity_data_refresh);

        setSyncDate();

        init();

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (ConnectivityTest.getNetStat(DataRefreshActivity.this)) {
                    //0-AreaMaster,1-BankMaster,2-BankBranchMaster,3-CityMaster
                    //4-CompanyMaster,5-CustomerMaster,6-DocumentMaster,7-EmployeeMaster,8-HOMaster
                    //9-ProductMaster,10-LoadAllSizeNDesign,11-StockMaster,12-GSTMaster
                    String name = refreshList.get(i);

                    if (name.equals(areaMaster)) {
                        refreshDataDia(0);
                    } else if (name.equals(bankMaster)) {
                        refreshDataDia(1);
                    } else if (name.equals(bankBrancMaster)) {
                        refreshDataDia(2);
                    } else if (name.equals(cityMaster)) {
                        refreshDataDia(3);
                    } else if (name.equals(companyMaster)) {
                        refreshDataDia(4);
                    } else if (name.equals(custMaster)) {
                        refreshDataDia(5);
                    } else if (name.equals(docMaster)) {
                        refreshDataDia(6);
                    } else if (name.equals(empMaster)) {
                        refreshDataDia(7);
                    } else if (name.equals(hoMaster)) {
                        refreshDataDia(8);
                    } else if (name.equals(prodMaster)) {
                        refreshDataDia(9);
                    } else if (name.equals(sizenDesignMaster)) {
                        refreshDataDia(10);
                    } else if (name.equals(stockMaster)) {
                        refreshDataDia(11);
                    } else if (name.equals(gstMaster)) {
                        refreshDataDia(12);
                    }else if (name.equals(sdmdMaster)) {
                        refreshDataDia(13);
                    }
                } else {
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

    private void setSyncDate(){
        String str = isSynced(getString(R.string.pref_autoArea));
        areaMaster = areaMaster+" -- "+str;

        str = isSynced(getString(R.string.pref_autoBank));
        bankMaster = bankMaster+" -- "+str;

        str = isSynced(getString(R.string.pref_autoBankBranch));
        bankBrancMaster = bankBrancMaster+" -- "+str;

        str = isSynced(getString(R.string.pref_autoCity));
        cityMaster = cityMaster+" -- "+str;

        str = isSynced(getString(R.string.pref_autoCompany));
        companyMaster = companyMaster+" -- "+str;

        str = isSynced(getString(R.string.pref_autoCustomer));
        custMaster = custMaster+" -- "+str;

        str = isSynced(getString(R.string.pref_autoDocument));
        docMaster = docMaster+" -- "+str;

        str = isSynced(getString(R.string.pref_autoEmployee));
        empMaster = empMaster+" -- "+str;

        str = isSynced(getString(R.string.pref_autoGST));
        gstMaster = gstMaster+" -- "+str;

        str = isSynced(getString(R.string.pref_autoHO));
        hoMaster = hoMaster+" -- "+str;

        str = isSynced(getString(R.string.pref_autoProduct));
        prodMaster = prodMaster+" -- "+str;

        str = isSynced(getString(R.string.pref_autoSizeNDesign));
        sizenDesignMaster = sizenDesignMaster+" -- "+str;

        str = isSynced(getString(R.string.pref_autoSizeDetail));
        sdmdMaster = sdmdMaster+" -- "+str;

    }

    private String isSynced(String prefName){
        String savedDate = "";
        try {
            FirstActivity.pref = getApplicationContext().getSharedPreferences(FirstActivity.PREF_NAME, Context.MODE_PRIVATE);
            String str = FirstActivity.pref.getString(prefName, "");
            String strArr[] = str.split("\\-");
            if (strArr.length > 1) {
                savedDate = strArr[2];
            }else{
                savedDate = str;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return savedDate;
    }

    private void loadAreaMaster() {
        String url = Constant.ipaddress + "/GetAreaMaster?Id=0";
        Constant.showLog(url);
        writeLog("loadAreaMaster_" + url);
        constant.showPD();
        VolleyRequests requests = new VolleyRequests(DataRefreshActivity.this);
        requests.refreshAreaMaster(url, new ServerCallback() {
            @Override
            public void onSuccess(String result) {
                constant.showPD();
                prefname = getString(R.string.pref_autoArea);
                showDia(1);
            }

            @Override
            public void onFailure(String result) {
                constant.showPD();
                showDia(2);
            }
        });
    }

    private void loadCityMaster() {
        String url = Constant.ipaddress + "/GetCityMaster?Id=0";
        Constant.showLog(url);
        writeLog("loadCityMaster_" + url);
        constant.showPD();
        VolleyRequests requests = new VolleyRequests(DataRefreshActivity.this);
        requests.refreshCityMaster(url, new ServerCallback() {
            @Override
            public void onSuccess(String result) {
                constant.showPD();
                prefname = getString(R.string.pref_autoCity);
                showDia(1);
            }

            @Override
            public void onFailure(String result) {
                constant.showPD();
                showDia(2);
            }
        });
    }

    private void loadHOMaster() {
        String url = Constant.ipaddress + "/GetHOMaster?Id=0";
        Constant.showLog(url);
        writeLog("loadHOMaster_" + url);
        constant.showPD();
        VolleyRequests requests = new VolleyRequests(DataRefreshActivity.this);
        requests.refreshHOMaster(url, new ServerCallback() {
            @Override
            public void onSuccess(String result) {
                constant.showPD();
                prefname = getString(R.string.pref_autoHO);
                showDia(1);
            }

            @Override
            public void onFailure(String result) {
                constant.showPD();
                showDia(2);
            }
        },0);
    }

    private void loadEmployeeMaster() {
        String url = Constant.ipaddress + "/GetEmployeeMaster?Id=0";
        Constant.showLog(url);
        writeLog("loadEmployeeMaster_" + url);
        constant.showPD();
        VolleyRequests requests = new VolleyRequests(DataRefreshActivity.this);
        requests.refreshEmployeeMaster(url, new ServerCallback() {
            @Override
            public void onSuccess(String result) {
                constant.showPD();
                prefname = getString(R.string.pref_autoEmployee);
                showDia(1);
            }

            @Override
            public void onFailure(String result) {
                constant.showPD();
                showDia(2);
            }
        });
    }

    private void loadStockInfo() {
        String url = Constant.ipaddress + "/GetStockInfo?Id=0";
        Constant.showLog(url);
        writeLog("loadStockInfo_" + url);
        constant.showPD();
        VolleyRequests requests = new VolleyRequests(DataRefreshActivity.this);
        requests.refreshStockInfo(url, new ServerCallback() {
            @Override
            public void onSuccess(String result) {
                new readJSON(result, "StockInfo", 0);
            }

            @Override
            public void onFailure(String result) {
                constant.showPD();
                showDia(2);
            }
        });
    }

    private void loadProductMaster() {
        String url = Constant.ipaddress + "/GetProductMaster?Id=0";
        Constant.showLog(url);
        writeLog("loadProductMaster_" + url);
        constant.showPD();
        VolleyRequests requests = new VolleyRequests(DataRefreshActivity.this);
        requests.refreshProductMaster(url, new ServerCallback() {
            @Override
            public void onSuccess(String result) {
                constant.showPD();
                prefname = getString(R.string.pref_autoProduct);
                showDia(1);
            }

            @Override
            public void onFailure(String result) {
                constant.showPD();
                showDia(2);
            }
        },0,0);
    }

    private void loadProductMaster(final int from, final int to) {
        String url = Constant.ipaddress + "/GetProductMasterV5?from="+from+"&to="+to+"&type=C";
        Constant.showLog(url);
        writeLog("loadProductMasterV5_" + url);
        if (from == 0) {
            sndpd = new ProgressDialog(DataRefreshActivity.this);
            sndpd.setCancelable(false);
            sndpd.setProgressNumberFormat(null);
            sndpd.setProgressPercentFormat(null);
            sndpd.setProgressNumberFormat("%1d/%2d");
            NumberFormat percentInstance = NumberFormat.getPercentInstance();
            percentInstance.setMaximumFractionDigits(0);
            sndpd.setProgressPercentFormat(percentInstance);
            sndpd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            sndpd.setTitle("Please Wait");
            sndpd.setMessage("It will take app. 4-5 min");
            sndpd.show();
        }
        VolleyRequests requests = new VolleyRequests(DataRefreshActivity.this);
        requests.refreshProductMaster(url, new ServerCallback() {
            @Override
            public void onSuccess(String result) {
                try {
                    sndpd.setProgress((to * 100) / maxProdId);
                    if (to == maxProdId) {
                        sndpd.dismiss();
                        prefname = getString(R.string.pref_autoProduct);
                        showDia(1);
                    } else {
                        int from = to + 1;
                        int _to = to + 500;
                        Constant.showLog("From-" + from + "-To-" + _to);
                        if (_to > maxProdId) {
                            _to = maxProdId;
                        }
                        loadProductMaster(from, _to);
                    }
                }catch (Exception e){
                    if(sndpd!=null){
                        sndpd.dismiss();
                    }
                    toast.setText("Connection Timeout...Please Try After Sometime");
                    toast.show();
                    e.printStackTrace();
                    writeLog("loadProductMaster_"+e.getMessage());
                    showDia(2);
                }
            }
            @Override
            public void onFailure(String result) {
                //constant.showPD();
                showDia(2);
            }
        },from,to);
    }

    private void loadCustomerMaster() {
        int from = FirstActivity.pref.getInt(getString(R.string.pref_retailCustId), 0);
        String url = Constant.ipaddress + "/GetCustomerMasterV5?from="+from+"&to=0&type=C";
        Constant.showLog(url);
        writeLog("loadCustomerMaster_" + url);
        constant.showPD();
        new getCustomerMaster().execute(url);
    }

    private void loadCompanyMaster() {
        db.createCompanyMaster();
        String url = Constant.ipaddress + "/GetCompanyMaster?Id=0";
        Constant.showLog(url);
        writeLog("loadCompanyMaster_" + url);
        constant.showPD();
        VolleyRequests requests = new VolleyRequests(DataRefreshActivity.this);
        requests.refreshCompanyMaster(url, new ServerCallback() {
            @Override
            public void onSuccess(String result) {
                constant.showPD();
                prefname = getString(R.string.pref_autoCompany);
                showDia(1);
            }

            @Override
            public void onFailure(String result) {
                constant.showPD();
                showDia(2);
            }
        },0);
    }

    private void loadBankMaster() {
        db.createBankMaster();
        String url = Constant.ipaddress + "/GetBankMaster?Id=0";
        Constant.showLog(url);
        writeLog("loadBankMaster_" + url);
        constant.showPD();
        VolleyRequests requests = new VolleyRequests(DataRefreshActivity.this);
        requests.refreshBankMaster(url, new ServerCallback() {
            @Override
            public void onSuccess(String result) {
                constant.showPD();
                prefname = getString(R.string.pref_autoBank);
                showDia(1);
            }

            @Override
            public void onFailure(String result) {
                constant.showPD();
                showDia(2);
            }
        });
    }

    private void loadBankBranchMaster() {
        db.createBankBranchMaster();
        String url = Constant.ipaddress + "/GetBankBranchMaster?Id=0";
        Constant.showLog(url);
        writeLog("loadBankBranchMaster_" + url);
        constant.showPD();
        new getBankBranchMaster().execute(url);
    }

    private void loadDocumentMaster() {
        db.createDocumentMaster();
        String url = Constant.ipaddress + "/GetDocumentMaster?Id=0";
        Constant.showLog(url);
        writeLog("loadDocumentMaster_" + url);
        constant.showPD();
        VolleyRequests requests = new VolleyRequests(DataRefreshActivity.this);
        requests.refreshDocumentMaster(url, new ServerCallback() {
            @Override
            public void onSuccess(String result) {
                constant.showPD();
                prefname = getString(R.string.pref_autoDocument);
                showDia(1);
            }

            @Override
            public void onFailure(String result) {
                constant.showPD();
                showDia(2);
            }
        });
    }

    private void loadGSTMaster() {
        String url = Constant.ipaddress + "/GetGSTMaster?Id=0";
        Constant.showLog(url);
        writeLog("loadGSTMaster_" + url);
        constant.showPD();
        VolleyRequests requests = new VolleyRequests(DataRefreshActivity.this);
        requests.refreshGSTMaster(url, new ServerCallback() {
            @Override
            public void onSuccess(String result) {
                constant.showPD();
                prefname = getString(R.string.pref_autoGST);
                showDia(1);
            }

            @Override
            public void onFailure(String result) {
                constant.showPD();
                showDia(2);
            }
        },0);
    }

    private void init() {
        db = new DBHandler(DataRefreshActivity.this);
        constant = new Constant(DataRefreshActivity.this);
        constant1 = new Constant(getApplicationContext());
        toast = Toast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        listView = (ListView) findViewById(R.id.listView);
        refreshList = new ArrayList<>();
        //refreshList.add(areaMaster);
        //refreshList.add(bankMaster);
        //refreshList.add(bankBrancMaster);
        //refreshList.add(cityMaster);
        refreshList.add(companyMaster);
        refreshList.add(custMaster);
        //refreshList.add(docMaster);
        //refreshList.add(empMaster);
        refreshList.add(gstMaster);
        refreshList.add(hoMaster);
        refreshList.add(prodMaster);
        refreshList.add(sizenDesignMaster);
        //refreshList.add(stockMaster);
        refreshList.add(sdmdMaster);
        listView.setAdapter(new ArrayAdapter<>(getApplicationContext(), R.layout.list_item_data_refresh, refreshList));
        FirstActivity.pref = getSharedPreferences(FirstActivity.PREF_NAME,MODE_PRIVATE);
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
        } else if (a == 0) {
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
        } else if (a == 1) {
            builder.setMessage("Data Refreshed Successfully");
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    updateSharedPref(prefname,"Y");
                    dialog.dismiss();
                }
            });
        } else if (a == 2) {
            builder.setMessage("Error While Loading Data");
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if(sndpd!=null){
                        sndpd.dismiss();
                    }
                    new Constant();
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
                } else if (a == 1) {
                    loadBankMaster();
                } else if (a == 2) {
                    loadBankBranchMaster();
                } else if (a == 3) {
                    loadCityMaster();
                } else if (a == 4) {
                    loadCompanyMaster();
                } else if (a == 5) {
                    loadCustomerMaster();
                } else if (a == 6) {
                    loadDocumentMaster();
                } else if (a == 7) {
                    loadEmployeeMaster();
                } else if (a == 8) {
                    loadHOMaster();
                } else if (a == 9) {
                    getMaxAuto(1);
                } else if (a == 10) {
                    maxProdId = db.getMaxProdId();
                    if (maxProdId != 0) {
                        db.deleteTable(DBHandler.Table_AllRequiredSizesDesigns);
                        loadSizeNDesignMaster(0, 100);
                    } else {
                        toast.setText("Please Update ProductMaster First");
                        toast.show();
                    }
                } else if (a == 11) {
                    //loadStockInfo();
                } else if (a == 12) {
                    loadGSTMaster();
                }else if (a == 13) {
                    maxSDMDAuto = db.getMaxProdId();
                    if (maxSDMDAuto != 0) {
                        Constant.showLog("maxSDMDAuto :- "+maxSDMDAuto);
                        db.deleteTable(DBHandler.Table_SizeDesignMastDet);
                    }else {
                        toast.setText("Please Update ProductMaster First");
                        toast.show();
                    }
                    loadSDMD(0,100);
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

    private void loadSizeNDesignMaster(int from, int to) {
        String url = Constant.ipaddress + "/GetAllRequiredSizesdesigns?Id=" + from + "&ToId=" + to;
        Constant.showLog(url);
        writeLog("loadSizeNDesignMaster_" + url);
        if (from == 0) {
            sndpd = new ProgressDialog(DataRefreshActivity.this);
            sndpd.setCancelable(false);
            sndpd.setProgressNumberFormat(null);
            sndpd.setProgressPercentFormat(null);
            sndpd.setProgressNumberFormat("%1d/%2d");
            NumberFormat percentInstance = NumberFormat.getPercentInstance();
            percentInstance.setMaximumFractionDigits(0);
            sndpd.setProgressPercentFormat(percentInstance);
            sndpd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            sndpd.setTitle("Please Wait");
            sndpd.setMessage("It will take app. 4-5 min");
            sndpd.show();
        }
        new getSizeNDesignMaster(to).execute(url);
    }

    private void loadSDMD(int from, int to) {
        String url = Constant.ipaddress + "/GetAllSizeDesignMastDet?Id=" + from + "&ToId=" + to;
        Constant.showLog(url);
        writeLog("loadSDMD_" + url);
        if (from == 0) {
            sndpd = new ProgressDialog(DataRefreshActivity.this);
            sndpd.setCancelable(false);
            sndpd.setProgressNumberFormat(null);
            sndpd.setProgressPercentFormat(null);
            sndpd.setProgressNumberFormat("%1d/%2d");
            NumberFormat percentInstance = NumberFormat.getPercentInstance();
            percentInstance.setMaximumFractionDigits(0);
            sndpd.setProgressPercentFormat(percentInstance);
            sndpd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            sndpd.setTitle("Please Wait");
            sndpd.setMessage("It will take app. 4-5 min");
            sndpd.show();
        }

        new getSizeDesignMastDet(to).execute(url);
    }

    private class getSizeNDesignMaster extends AsyncTask<String, Void, String> {
        int to;

        private getSizeNDesignMaster(int _to) {
            this.to = _to;
        }

        @Override
        protected String doInBackground(String... strings) {
            return Post.POST(strings[0]);
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);
            if(response!=null && !response.equals("")) {
                response = response.substring(1, response.length() - 1);
                new readJSON(response, "SizeNDesign", to).execute();
            }else{
                if(sndpd!=null){
                    sndpd.dismiss();
                }
                toast.setText("Connection Timeout...Please Tyr After Sometime");
                toast.show();
                writeLog("getSizeNDesignMaster_response_null");
            }
        }
    }

    private class readJSON extends AsyncTask<Void, Void, String> {
        int to;
        private String result, parseType;

        private readJSON(String _result, String _parseType, int _to) {
            this.result = _result;
            this.parseType = _parseType;
            this.to = _to;
        }

        @Override
        protected String doInBackground(Void... voids) {
            String retValue = "A";
            File sdFile = Constant.checkFolder(Constant.folder_name);
            FileWriter writer;
            try {
                String search = "\\\\", replace = "";
                File writeFile = new File(sdFile, writeFilename);
                writer = new FileWriter(writeFile);
                int size = result.length();
                if (size > 2) {
                    Log.d("Log", "Replacing");
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
            } catch (IOException | OutOfMemoryError e) {
                try {
                    writer = new FileWriter(new File(sdFile, "Log.txt"));
                    writer.append(e.getMessage());
                    writer.flush();
                    writer.close();
                } catch (Exception e1) {
                    e.printStackTrace();
                }
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s.equals("A")) {
                new writeDB(parseType, to).execute();
            } else {
                showDia(2);
            }
        }
    }

    private class writeDB extends AsyncTask<Void, String, String> {

        private File writeFile;
        private String parseType;
        private int to;

        private writeDB(String _parseType, int _to) {
            this.parseType = _parseType;
            this.to = _to;
        }

        @Override
        protected String doInBackground(Void... voids) {
            File sdFile = Constant.checkFolder(Constant.folder_name);
            JsonFactory f = new JsonFactory();
            try {
                writeFile = new File(sdFile, writeFilename);
                JsonParser jp = f.createJsonParser(writeFile);
                if (parseType.equals("SizeNDesign")) {
                    parseSizeNDesign(jp, to);
                } else if (parseType.equals("StockInfo")) {
                    db.deleteTable(DBHandler.Table_StockInfo);
                    parseStockInfo(jp);
                }
                return "";
            } catch (Exception e) {
                try {
                    FileWriter writer = new FileWriter(new File(sdFile, "Log.txt"));
                    writer.append(e.getMessage());
                    writer.flush();
                    writer.close();
                } catch (Exception e1) {
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
            if (s != null) {
                if (s.equals("")) {
                    sndpd.setProgress((to * 100) / maxProdId);
                    Constant.showLog("s is blank:");
                    if (writeFile.delete()) {
                        Constant.showLog("Write Delete");
                        if (to == maxProdId) {
                            sndpd.dismiss();
                            prefname = getString(R.string.pref_autoSizeNDesign);
                            showDia(1);
                        } else {
                            int from = to + 1;
                            to = to + 100;
                            Constant.showLog("From-" + from + "-To-" + to);
                            if (to > maxProdId) {
                                to = maxProdId;
                            }
                            loadSizeNDesignMaster(from, to);
                        }
                    }
                } else {
                    showDia(2);
                }
            } else {
                showDia(2);
            }
        }
    }

    private class getCustomerMaster extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            return Post.POST(strings[0]);
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);
            if (response != null && !response.equals("")) {
                response = response.replace("\\\\r\\\\n", "");
                response = response.substring(1, response.length() - 1);
                constant.showPD();
                new readCustJSON(response, "CustMast").execute();
            } else {
                writeLog("getCustomerMaster_response_null");
            }
        }
    }

    private class readCustJSON extends AsyncTask<Void, Void, String> {
        private String result, parseType;
        private ProgressDialog pd;

        private readCustJSON(String _result, String _parseType) {
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
                String search = "\\\\", replace = "";
                File writeFile = new File(sdFile, writeFilename);
                writer = new FileWriter(writeFile);
                int size = result.length();
                if (size > 2) {
                    Log.d("Log", "Replacing");
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
            } catch (IOException | OutOfMemoryError e) {
                pd.dismiss();
                writeLog("readCustJSON_" + e.getMessage());
                try {
                    writer = new FileWriter(new File(sdFile, "Log.txt"));
                    writer.append(e.getMessage());
                    writer.flush();
                    writer.close();
                } catch (Exception e1) {
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
            if (s.equals("A")) {
                new writeCustDB(parseType).execute();
            } else {
                showDia(2);
            }
        }
    }

    private class writeCustDB extends AsyncTask<Void, String, String> {
        private File writeFile;
        private String parseType;

        private writeCustDB(String _parseType) {
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
            pd1.setMessage("It will take app. 4-5 min");
            pd1.show();
        }

        @Override
        protected String doInBackground(Void... voids) {
            File sdFile = Constant.checkFolder(Constant.folder_name);
            JsonFactory f = new JsonFactory();
            try {
                writeFile = new File(sdFile, writeFilename);
                JsonParser jp = f.createJsonParser(writeFile);
                db.deleteTable(DBHandler.Table_Customermaster);
                parseCustMaster(jp);
                return "";
            } catch (Exception e) {
                pd1.dismiss();
                writeLog("writeCustDB_" + e.getMessage());
                try {
                    FileWriter writer = new FileWriter(new File(sdFile, "Log.txt"));
                    writer.append(e.getMessage());
                    writer.flush();
                    writer.close();
                } catch (Exception e1) {
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
            if (s.equals("")) {
                if (writeFile.delete()) {
                    Constant.showLog("Write Delete");
                    prefname = getString(R.string.pref_autoCustomer);
                    showDia(1);
                }
            } else {
                showDia(2);
            }
        }
    }

    private void parseSizeNDesign(JsonParser jp, int to) {
        try {
            int count = 0;
            List<SizeNDesignClass> list = new ArrayList<>();
            while (jp.nextToken() != JsonToken.END_ARRAY) {
                count++;
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
                    } else if (DBHandler.ARSD_GSTGroup.equals(token)) {
                        jp.nextToken();
                        sizeNDesignClass.setGSTGroup(jp.getText());
                    } else if (DBHandler.ARSD_InOutType.equals(token)) {
                        jp.nextToken();
                        sizeNDesignClass.setInOutType(jp.getText());
                    } else if (DBHandler.ARSD_Total.equals(token)) {
                        jp.nextToken();
                        sizeNDesignClass.setTotal(jp.getValueAsInt());
                    } else if (DBHandler.ARSD_HashCode.equals(token)) {
                        jp.nextToken();
                        sizeNDesignClass.setHashCode(jp.getText());
                    } else if (DBHandler.ARSD_ImageName.equals(token)) {
                        jp.nextToken();
                        sizeNDesignClass.setImageName(jp.getText());
                    } /* else if (DBHandler.ARSD_Cat1.equals(token)) {
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
            }
            db.addSizeNDesignMaster(list);
            Constant.showLog("" + count);
        } catch (Exception e) {
            writeLog("parseSizeNDesign_" + e.getMessage());
            e.printStackTrace();
            showDia(2);
        }
    }

    private class getBankBranchMaster extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            return Post.POST(strings[0]);
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);
            if (response != null && !response.equals("")) {
                response = response.substring(1, response.length() - 1);
                constant.showPD();
                new readBBJSON(response, "BankBranchMast").execute();
            } else {
                writeLog("getBankBranchMaster_response_null");
            }
        }
    }

    private class readBBJSON extends AsyncTask<Void, Void, String> {
        private String result, parseType;
        private ProgressDialog pd;

        readBBJSON(String _result, String _parseType) {
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
                String search = "\\\\", replace = "";
                File writeFile = new File(sdFile, writeFilename);
                writer = new FileWriter(writeFile);
                int size = result.length();
                if (size > 2) {
                    Log.d("Log", "Replacing");
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
            } catch (IOException | OutOfMemoryError e) {
                pd.dismiss();
                writeLog("readBBJSON_" + e.getMessage());
                try {
                    writer = new FileWriter(new File(sdFile, "Log.txt"));
                    writer.append(e.getMessage());
                    writer.flush();
                    writer.close();
                } catch (Exception e1) {
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
            if (s.equals("A")) {
                new writeBBDB(parseType).execute();
            } else {
                showDia(2);
            }
        }
    }

    private class writeBBDB extends AsyncTask<Void, String, String> {
        private File writeFile;
        private String parseType;

        private writeBBDB(String _parseType) {
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
            pd1.setMessage("It will take app. 4-5 min");
            pd1.show();
        }

        @Override
        protected String doInBackground(Void... voids) {
            File sdFile = Constant.checkFolder(Constant.folder_name);
            JsonFactory f = new JsonFactory();
            try {
                writeFile = new File(sdFile, writeFilename);
                JsonParser jp = f.createJsonParser(writeFile);
                db.deleteTable(DBHandler.Table_BankBranchMaster);
                parseBankBranchMaster(jp);
                return "";
            } catch (Exception e) {
                pd1.dismiss();
                writeLog("writeBBDB_" + e.getMessage());
                try {
                    FileWriter writer = new FileWriter(new File(sdFile, "Log.txt"));
                    writer.append(e.getMessage());
                    writer.flush();
                    writer.close();
                } catch (Exception e1) {
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
            if (s.equals("")) {
                if (writeFile.delete()) {
                    Constant.showLog("Write Delete");
                    prefname = getString(R.string.pref_autoBankBranch);
                    showDia(1);
                }
            } else {
                showDia(2);
            }
        }
    }

    private void parseSizeNDesign(JsonParser jp, ProgressDialog pd) {
        try {
            int count = 0;
            List<SizeNDesignClass> list = new ArrayList<>();
            while (jp.nextToken() != JsonToken.END_ARRAY) {
                count++;
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
                    } else if (DBHandler.ARSD_GSTGroup.equals(token)) {
                        jp.nextToken();
                        sizeNDesignClass.setGSTGroup(jp.getText());
                    } else if (DBHandler.ARSD_InOutType.equals(token)) {
                        jp.nextToken();
                        sizeNDesignClass.setInOutType(jp.getText());
                    } else if (DBHandler.ARSD_Total.equals(token)) {
                        jp.nextToken();
                        sizeNDesignClass.setTotal(jp.getValueAsInt());
                    } else if (DBHandler.ARSD_HashCode.equals(token)) {
                        jp.nextToken();
                        sizeNDesignClass.setHashCode(jp.getText());
                    } else if (DBHandler.ARSD_ImageName.equals(token)) {
                        jp.nextToken();
                        sizeNDesignClass.setImageName(jp.getText());
                    } /* else if (DBHandler.ARSD_Cat1.equals(token)) {
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
            }
            db.addSizeNDesignMaster(list);
            Constant.showLog("" + count);
        } catch (Exception e) {
            e.printStackTrace();
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
            pd1.dismiss();
            // constant.showPD();
            showDia(2);
        }
    }

    private void parseCustMaster(JsonParser jp) {
        try {
            ArrayList<CustomerDetailClass> list = new ArrayList<>();
            while (jp.nextToken() != JsonToken.END_ARRAY) {
                CustomerDetailClass custClass = new CustomerDetailClass();
                while (jp.nextToken() != JsonToken.END_OBJECT) {
                    String token = jp.getCurrentName();
                    if ("retailCustID".equals(token)) {
                        jp.nextToken();
                        custClass.setCustID(jp.getIntValue());
                    } else if ("name".equals(token)) {
                        jp.nextToken();
                        custClass.setName(jp.getText());
                    } else if ("address".equals(token)) {
                        jp.nextToken();
                        custClass.setAddress(jp.getText());
                    } else if ("mobile".equals(token)) {
                        jp.nextToken();
                        custClass.setMobile(jp.getText());
                    } else if ("email".equals(token)) {
                        jp.nextToken();
                        custClass.setEmail(jp.getText());
                    } else if ("Panno".equals(token)) {
                        jp.nextToken();
                        custClass.setPANno(jp.getText());
                    } else if ("PartyName".equals(token)) {
                        jp.nextToken();
                        custClass.setPartyName(jp.getText());
                    } else if ("GSTNo".equals(token)) {
                        jp.nextToken();
                        custClass.setGSTNo(jp.getText());
                    } else if ("status".equals(token)) {
                        jp.nextToken();
                        custClass.setStatus(jp.getText());
                    } else if ("ImagePath".equals(token)) {
                        jp.nextToken();
                        custClass.setImagePath(jp.getText());
                    } else if ("Discount".equals(token)) {
                        jp.nextToken();
                        custClass.setDiscount(jp.getFloatValue());
                    } else if ("branchId".equals(token)) {
                        jp.nextToken();
                        custClass.setBranchId(jp.getIntValue());
                    } else if ("District".equals(token)) {
                        jp.nextToken();
                        custClass.setDistrict(jp.getText());
                    } else if ("Taluka".equals(token)) {
                        jp.nextToken();
                        custClass.setTaluka(jp.getText());
                    } else if ("cityId".equals(token)) {
                        jp.nextToken();
                        custClass.setCityId(jp.getIntValue());
                    } else if ("areaId".equals(token)) {
                        jp.nextToken();
                        custClass.setAreaId(jp.getIntValue());
                    } else if ("HoCode".equals(token)) {
                        jp.nextToken();
                        custClass.setHOCode(jp.getIntValue());
                    } else if ("AadharNo".equals(token)) {
                        jp.nextToken();
                        custClass.setAadharNo(jp.getText());
                    }
                }
                list.add(custClass);
            }
            db.addCustomerDetail(list);
        } catch (Exception e) {
            writeLog("parseCustMaster_" + e.getMessage());
            e.printStackTrace();
            constant.showPD();
            showDia(2);
        }
    }

    private void parseBankBranchMaster(JsonParser jp) {
        try {
            ArrayList<BankBranchMasterClass> list = new ArrayList<>();
            while (jp.nextToken() != JsonToken.END_ARRAY) {
                BankBranchMasterClass bbClass = new BankBranchMasterClass();
                while (jp.nextToken() != JsonToken.END_OBJECT) {
                    String token = jp.getCurrentName();
                    if ("Autoid".equals(token)) {
                        jp.nextToken();
                        bbClass.setAutoid(jp.getText());
                    } else if ("id".equals(token)) {
                        jp.nextToken();
                        bbClass.setId(jp.getText());
                    } else if ("Branch".equals(token)) {
                        jp.nextToken();
                        bbClass.setBranch(jp.getText());
                    } else if ("Custid".equals(token)) {
                        jp.nextToken();
                        bbClass.setCustid(jp.getText());
                    } else if ("AccountNo".equals(token)) {
                        jp.nextToken();
                        bbClass.setAccountNo(jp.getText());
                    } else if ("CBranch".equals(token)) {
                        jp.nextToken();
                        bbClass.setcBranch(jp.getText());
                    } else if ("CBankid".equals(token)) {
                        jp.nextToken();
                        bbClass.setcBankid(jp.getText());
                    }
                }
                list.add(bbClass);
            }
            db.addBankBranchMaster(list);
        } catch (Exception e) {
            writeLog("parseBankBranchMaster_" + e.getMessage());
            e.printStackTrace();
            constant.showPD();
            showDia(2);
        }
    }

    private class getSizeDesignMastDet extends AsyncTask<String, Void, String> {
        int to;

        private getSizeDesignMastDet(int _to) {
            this.to = _to;
        }

        @Override
        protected String doInBackground(String... strings) {
            return Post.POST(strings[0]);
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);
            if(response!=null && !response.equals("")) {
                response = response.substring(1, response.length() - 1);
                new readJSONSDMD(response, "SDMD", to).execute();
            }else{
                if(sndpd!=null){
                    sndpd.dismiss();
                }
                toast.setText("Connection Timeout...Please Tyr After Sometime");
                toast.show();
                writeLog("getSizeDesignMastDet_response_null");
            }

        }
    }

    private class readJSONSDMD extends AsyncTask<Void, Void, String> {
        private int to;
        private File writeFile;
        private String result, parseType;

        private readJSONSDMD(String _result, String _parseType, int _to) {
            this.result = _result;
            this.parseType = _parseType;
            this.to = _to;
        }

        @Override
        protected String doInBackground(Void... voids) {
            String retValue = "B";
            File sdFile = Constant.checkFolder(Constant.folder_name);
            FileWriter writer;
            try {
                String search = "\\\\", replace = "";
                writeFile = new File(sdFile, writeFilename);
                writer = new FileWriter(writeFile);
                int size = result.length();
                if (size > 2) {
                    Log.d("Log", "Replacing");
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
            } catch (IOException | OutOfMemoryError e) {
                db.deleteTable(DBHandler.Table_SizeDesignMastDet);
                try {
                    writer = new FileWriter(new File(sdFile, "Log.txt"));
                    writer.append(e.getMessage());
                    writer.flush();
                    writer.close();
                } catch (Exception e1) {
                    e.printStackTrace();
                }
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s.equals("A")) {
                new writeDBSDMD(parseType, to).execute();
            }else if (s.equals("B")) {
                nextValue(to,writeFile);
            } else {
                showDia(2);
            }
        }
    }

    private class writeDBSDMD extends AsyncTask<Void, String, String> {

        private File writeFile;
        private String parseType;
        private int to;

        private writeDBSDMD(String _parseType, int _to) {
            this.parseType = _parseType;
            this.to = _to;
        }

        @Override
        protected String doInBackground(Void... voids) {
            File sdFile = Constant.checkFolder(Constant.folder_name);
            JsonFactory f = new JsonFactory();
            try {
                writeFile = new File(sdFile, writeFilename);
                JsonParser jp = f.createJsonParser(writeFile);
                parseSDMD(jp, to);
                return "";
            } catch (Exception e) {
                db.deleteTable(DBHandler.Table_SizeDesignMastDet);
                try {
                    FileWriter writer = new FileWriter(new File(sdFile, "Log.txt"));
                    writer.append(e.getMessage());
                    writer.flush();
                    writer.close();
                } catch (Exception e1) {
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
            if (s != null) {
                if (s.equals("")) {
                    nextValue(to,writeFile);
                } else {
                    showDia(2);
                }
            } else {
                showDia(2);
            }
        }
    }

    private void nextValue(int to,File writeFile){
        sndpd.setProgress((to * 100) / maxSDMDAuto);
        Constant.showLog("s is blank:");
        if (writeFile.delete()) {
            Constant.showLog("Write Delete");
            if (to == maxSDMDAuto) {
                sndpd.dismiss();
                prefname = getString(R.string.pref_autoSizeDetail);
                showDia(1);
            } else {
                int from = to + 1;
                to = to + 100;
                Constant.showLog("From-" + from + "-To-" + to);
                if (to > maxSDMDAuto) {
                    to = maxSDMDAuto;
                }
                loadSDMD(from,to);
            }
        }
    }

    private void parseSDMD(JsonParser jp, int to) {
        try {
            int count = 0;
            List<SizeDesignMastDetClass> list = new ArrayList<>();
            while (jp.nextToken() != JsonToken.END_ARRAY) {
                count++;
                SizeDesignMastDetClass sizeNDesignClass = new SizeDesignMastDetClass();
                while (jp.nextToken() != JsonToken.END_OBJECT) {
                    String token = jp.getCurrentName();
                    if (DBHandler.SDMD_Auto.equals(token)) {
                        jp.nextToken();
                        sizeNDesignClass.setAuto(jp.getValueAsInt());
                    } else if (DBHandler.SDMD_ProductId.equals(token)) {
                        jp.nextToken();
                        sizeNDesignClass.setProductid(jp.getValueAsInt());
                    } else if (DBHandler.SDMD_DesignNo.equals(token)) {
                        jp.nextToken();
                        String dn = jp.getText();
                        sizeNDesignClass.setDesignNo(dn);
                    } else if (DBHandler.SDMD_SizeGroupFrom.equals(token)) {
                        jp.nextToken();
                        sizeNDesignClass.setSizeGroupFrom(jp.getValueAsInt());
                    } else if (DBHandler.SDMD_SizeGroupTo.equals(token)) {
                        jp.nextToken();
                        sizeNDesignClass.setSizeGroupTo(jp.getValueAsInt());
                    }else if (DBHandler.SDMD_Total.equals(token)) {
                        jp.nextToken();
                        sizeNDesignClass.setTotal(jp.getValueAsInt());
                    } else if (DBHandler.SDMD_SizeGroup.equals(token)) {
                        jp.nextToken();
                        sizeNDesignClass.setSizeGroup(jp.getText());
                    } else if (DBHandler.SDMD_Colour.equals(token)) {
                        jp.nextToken();
                        String col = jp.getText();
                        sizeNDesignClass.setColour(col);
                    } else if (DBHandler.SDMD_Size.equals(token)) {
                        jp.nextToken();
                        sizeNDesignClass.setSize(jp.getValueAsInt());
                    } else if (DBHandler.SDMD_Qty.equals(token)) {
                        jp.nextToken();
                        sizeNDesignClass.setQty(jp.getValueAsInt());
                    }
                }
                list.add(sizeNDesignClass);
            }
            db.addSizeDesignMastDet(list);
            Constant.showLog("" + count);
        } catch (Exception e) {
            writeLog("sdmd_" + e.getMessage());
            e.printStackTrace();
            db.deleteTable(DBHandler.Table_SizeDesignMastDet);
            showDia(2);
        }
    }

    private void updateSharedPref(String prefname, String value){
        writeLog(prefname+"_"+value);
        FirstActivity.pref = getSharedPreferences(FirstActivity.PREF_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = FirstActivity.pref.edit();
        String str = getDateTime()+"-"+value+"-"+getTime();
        Constant.showLog(prefname+"-"+str);
        editor.putString(prefname, getTime());
        editor.apply();
        //setSyncDate();
    }

    private void getMaxAuto(final int type) {
        String url = Constant.ipaddress + "/GetMaxAuto?type="+type;
        constant = new Constant(DataRefreshActivity.this);
        constant.showPD();
        Constant.showLog(url);
        writeLog("getMaxAuto_" + url);
        VolleyRequests requests = new VolleyRequests(getApplicationContext());
        requests.getMaxAuto(url, new ServerCallback() {
            @Override
            public void onSuccess(String result) {
                constant.showPD();
                Constant.showLog(result);
                if(type==1){
                    maxProdId = Integer.valueOf(result);
                    loadProductMaster(0, 100);
                }/*else if(type==2){
                    maxProdId = Integer.valueOf(result);
                    loadCustomerMaster(0, 100);
                }else if(type==3){
                    maxProdId = Integer.valueOf(result);
                    loadBankBranchMaster(0, 100);
                }*/
            }

            @Override
            public void onFailure(String result) {
                constant.showPD();
                Constant.showLog(result);
                if(type==1){
                    maxProdId = db.getMaxProdId();
                    loadProductMaster(0, 100);
                }/*else if(type==2){
                    maxProdId = db.getCustMax();
                    loadCustomerMaster(0, 100);
                }else if(type==3){
                    maxProdId = db.getBankBranchMax();
                    loadBankBranchMaster(0, 100);
                }*/
            }
        });
    }

    private String getDateTime() {
        String str = "";
        try{
            str = new SimpleDateFormat("dd/MMM/yyyy", Locale.ENGLISH).format(new Date());
        }catch (Exception e){
            e.printStackTrace();
        }
        return str;
    }

    private String getTime() {
        String str = "";
        try{
            str = new SimpleDateFormat("dd/MMM/yyyy HH:mm", Locale.ENGLISH).format(new Date());
        }catch (Exception e){
            e.printStackTrace();
        }
        return str;
    }

    private void writeLog(String _data) {
        new WriteLog().writeLog(getApplicationContext(), "DataRefreshActivity_" + _data);
    }
}