package com.pait.dispatch_app;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.crash.FirebaseCrash;
import com.pait.dispatch_app.adapters.CustomerDetailListAdapter;
import com.pait.dispatch_app.connectivity.ConnectivityTest;
import com.pait.dispatch_app.constant.AppSingleton;
import com.pait.dispatch_app.constant.Constant;
import com.pait.dispatch_app.db.DBHandler;
import com.pait.dispatch_app.interfaces.ServerCallback;
import com.pait.dispatch_app.location.LocationProvider;
import com.pait.dispatch_app.log.CopyLog;
import com.pait.dispatch_app.log.WriteLog;
import com.pait.dispatch_app.mail.GMailSender;
import com.pait.dispatch_app.parse.ParseJSON;
import com.pait.dispatch_app.parse.UserClass;
import com.pait.dispatch_app.services.UploadImageService;
import com.pait.dispatch_app.volleyrequests.VolleyRequests;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class CustomerDetailsActivity extends AppCompatActivity
        implements View.OnClickListener,
        LocationProvider.LocationCallback1 {

    private ListView listView;
    private CustomerDetailListAdapter adapter;
    private DBHandler db;
    private Toast toast;
    private Constant constant;
    private Button btn_save, btn_order, btn_report;
    private String version = "", mobNo = "", id = "0";
    private TextView tv_version;
    private LocationProvider provider;
    private Spinner sp_dpCenter;
    private List<String> dpList;
    private HashMap<String, Integer> dpMap;
    private UserClass userClass;
    private int dpId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Constant.liveTestFlag == 1) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }

        setContentView(R.layout.activity_customer_details);

        init();

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }

        provider = new LocationProvider(CustomerDetailsActivity.this, CustomerDetailsActivity.this, CustomerDetailsActivity.this);

        if (FirstActivity.pref.contains(getString(R.string.pref_imeino2))) {
            checkIsActive();
        } else {
            ArrayList<UserClass> userList = db.getUserDetail();
            if (!userList.isEmpty()) {
                UserClass user = userList.get(0);
                mobNo = user.getMobile();
                id = String.valueOf(user.getCustID());
            }
            registerIMEINo2();
        }

        /*listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                userClass = (UserClass) listView.getAdapter().getItem(i);
                Intent intent = new Intent(getApplicationContext(),CustomerLoginActivity.class);
                intent.putExtra("cust",userClass);
                startActivity(intent);
                overridePendingTransition(R.anim.enter,R.anim.exit);
            }
        });*/

        sp_dpCenter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String br = (String) adapterView.getItemAtPosition(i);
                int id = dpMap.get(br);
                if(id !=0) {
                    dpId = dpMap.get(br);
                    Constant.showLog(br + " " + dpId);
                } else {
                    toast.setText("Select Dispatch Center First");
                    toast.show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        try {
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
            version = pInfo.versionCode + "." + pInfo.versionName;
            tv_version.setText("Version : " + pInfo.versionName);
            Constant.showLog("App Version " + version);
            writeLog("MainActivity_Version_" + version);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            writeLog("MainActivity_" + e.getMessage());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Constant.showLog("onResume");
        provider.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Constant.showLog("onPause");
        provider.disconnect();
    }

    @Override
    public void onClick(View view) {
        int id = dpMap.get(dpList.get(sp_dpCenter.getSelectedItemPosition()));
        switch (view.getId()) {
            case R.id.btn_save:
                startNewActivity(0);
                break;
            case R.id.btn_order:
                if(id !=0) {
                    startNewActivity(0);
                } else {
                    toast.setText("Select Dispatch Center First");
                    toast.show();
                }
                break;
            case R.id.btn_report:
                if(id !=0) {
                    startNewActivity(1);
                } else {
                    toast.setText("Select Dispatch Center First");
                    toast.show();
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        showDia(0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.custdetail_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh:
                showDia(10);
                break;
            case R.id.report_error:
                showDia(11);
                break;
            case R.id.sync_image:
                writeLog("Sync_Images_Started");
                Intent intent = new Intent(CustomerDetailsActivity.this, UploadImageService.class);
                startService(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void handleNewLocation(Location location, String address) {
        try {
            Constant.showLog("handleNewLocation");
            Constant.showLog("CustomerDetailsActivity_" + address);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void locationAvailable() {
        Constant.showLog("Location Available");
    }

    private void init() {
        FirstActivity.pref = getSharedPreferences(FirstActivity.PREF_NAME, MODE_PRIVATE);
        listView = findViewById(R.id.listView);
        db = new DBHandler(getApplicationContext());
        toast = Toast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        constant = new Constant(CustomerDetailsActivity.this);
        tv_version = findViewById(R.id.tv_version);
        sp_dpCenter = findViewById(R.id.sp_dpcenter);
        dpList = new ArrayList<>();
        dpMap = new HashMap<>();
        btn_save = findViewById(R.id.btn_save);
        btn_order = findViewById(R.id.btn_order);
        btn_report = findViewById(R.id.btn_report);
        btn_save.setOnClickListener(this);
        btn_order.setOnClickListener(this);
        btn_report.setOnClickListener(this);
    }

    private void showDia(int a) {
        AlertDialog.Builder builder = new AlertDialog.Builder(CustomerDetailsActivity.this);
        builder.setCancelable(false);
        if (a == 0) {
            builder.setMessage("Do You Want To Exit App?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    new Constant(CustomerDetailsActivity.this).doFinish();
                    toast.cancel();
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
            builder.setMessage("You Are Not An Active Customer?");
            builder.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    toast.cancel();
                    new Constant(CustomerDetailsActivity.this).doFinish();
                }
            });
        } else if (a == 2) {
            builder.setMessage("Please Try Again");
            builder.setPositiveButton("Try Again", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    new Constant();
                    checkIsActive();
                }
            });
            builder.setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    toast.cancel();
                    new Constant(CustomerDetailsActivity.this).doFinish();
                }
            });
        } else if (a == 3) {
            builder.setTitle("You Are Offline");
            builder.setMessage("Please Connect To Network?");
            builder.setPositiveButton("Try Again", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    checkIsActive();
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    new Constant(CustomerDetailsActivity.this).doFinish();

                }
            });
        } else if (a == 4) {
            builder.setMessage("Error While Loading Data");
            builder.setPositiveButton("Try Again", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    checkVersion();
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        } else if (a == 5) {
            builder.setMessage("This Device Is Not Registered");
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    new Constant(CustomerDetailsActivity.this).doFinish();
                }
            });
        } else if (a == 8) {
            builder.setTitle("Update App");
            builder.setMessage("New App Version Is Available");
            builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    new Constant(CustomerDetailsActivity.this).doFinish();
                    final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                    } catch (android.content.ActivityNotFoundException anfe) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + appPackageName)));
                    }
                    dialog.dismiss();
                }
            });
        } else if (a == 9) {
            builder.setMessage("Please Try Again");
            builder.setPositiveButton("Try Again", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    new Constant();
                    registerIMEINo2();
                }
            });
            builder.setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    toast.cancel();
                    new Constant(CustomerDetailsActivity.this).doFinish();
                }
            });
        }  else if (a == 10) {
            builder.setMessage("Do You Want To Refresh Data?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    db.deleteTable(DBHandler.Table_Employee);
                    db.deleteTable(DBHandler.Table_CompanyMaster);
                    loadEmployeeMaster();
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        } else if (a == 11) {
            builder.setMessage("Do You Want To Report An Issue?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (ConnectivityTest.getNetStat(getApplicationContext())) {
                        exportFile();
                    } else {
                        toast.setText("You Are Offline");
                        toast.show();
                    }
                    dialog.dismiss();
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        } else if (a == 12) {
            builder.setMessage("Error While Loading Data");
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    new Constant();
                    dialog.dismiss();
                }
            });
        }
        builder.create().show();
    }

    private void registerIMEINo2() {
        constant = new Constant(CustomerDetailsActivity.this);
        if (ConnectivityTest.getNetStat(getApplicationContext())) {
            int id = FirstActivity.pref.getInt(getString(R.string.pref_retailCustId), 0);
            String __imeino = "", __imeino1 = "", __imeino2 = "";
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                __imeino = new Constant(getApplicationContext()).getIMEINo1();
                String[] arr = __imeino.split("\\^");
                if (arr.length > 1) {
                    __imeino1 = arr[0];
                    __imeino2 = arr[1];
                } else {
                    __imeino1 = __imeino;
                    __imeino2 = __imeino;
                }
            } else {
                __imeino = new Constant(getApplicationContext()).getIMEINo();
                __imeino1 = __imeino;
                __imeino2 = __imeino;
            }
            final String imeino = __imeino;
            String url = Constant.ipaddress + "/UpdateIMEINo?mobileno=" + mobNo + "&IMEINo1="
                    + __imeino1 + "&IMEINo2=" + __imeino2 + "&type=E&id=" + id;
            Constant.showLog(url);
            writeLog("registerIMEINo2_" + url);
            constant.showPD();
            VolleyRequests requests = new VolleyRequests(CustomerDetailsActivity.this);
            requests.updateIMEINo(url, new ServerCallback() {
                @Override
                public void onSuccess(String response) {
                    constant.showPD();
                    if (!response.equals("0") && !response.equals("-1") && !response.equals("-2")) {
                        SharedPreferences.Editor editor = FirstActivity.pref.edit();
                        editor.putBoolean(getString(R.string.pref_imeino2), true);
                        editor.apply();
                        checkIsActive();
                        writeLog("registerIMEINo2_Success_" + response);
                    } else {
                        showDia(9);
                    }
                }

                @Override
                public void onFailure(String result) {
                    constant.showPD();
                    showDia(9);
                }
            });
        } else {
            showDia(3);
        }
    }

    private void checkIsActive() {
        constant = new Constant(CustomerDetailsActivity.this);
        if (ConnectivityTest.getNetStat(getApplicationContext())) {
            int id = FirstActivity.pref.getInt(getString(R.string.pref_retailCustId), 0);
            String __imeino = "", __imeino1 = "", __imeino2 = "";
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                __imeino = new Constant(getApplicationContext()).getIMEINo1();
                String[] arr = __imeino.split("\\^");
                if (arr.length > 1) {
                    __imeino1 = arr[0];
                    __imeino2 = arr[1];
                } else {
                    __imeino1 = __imeino;
                    __imeino2 = __imeino;
                }
            } else {
                __imeino = new Constant(getApplicationContext()).getIMEINo();
                __imeino1 = __imeino;
                __imeino2 = __imeino;
            }
            final String imeino = __imeino, imeino1 = __imeino1, imeino2 = __imeino2;
            //String url = Constant.ipaddress + "/GetActiveStatusV5?id=" + id + "&type=E&imeino="+imeino;
            String url = Constant.ipaddress + "/GetActiveStatusV6?id=" + id + "&type=E&IMEINo1=" + imeino1
                    + "&IMEINo2=" + imeino2;
            Constant.showLog(url);
            writeLog("checkIsActive_" + url);
            constant.showPD();
            final VolleyRequests requests = new VolleyRequests(CustomerDetailsActivity.this);
            requests.getActiveStatus(url, new ServerCallback() {
                @Override
                public void onSuccess(String result) {
                    constant.showPD();
                    if (result.equals("A")) {
                        checkVersion();
                    } else if (result.equals("I")) {
                        showDia(5);
                    } else {
                        showDia(1);
                    }
                }

                @Override
                public void onFailure(String result) {
                    constant.showPD();
                    showDia(2);

                }
            });
        } else {
            showDia(3);
        }
    }

    private void checkVersion() {
        constant = new Constant(CustomerDetailsActivity.this);
        constant.showPD();
        String url1 = Constant.ipaddress + "/GetVersionV5?type=D";
        Constant.showLog(url1);
        StringRequest versionRequest = new StringRequest(url1,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String result) {
                        Constant.showLog(result);
                        result = result.replace("\\", "");
                        result = result.replace("''", "");
                        result = result.substring(1, result.length() - 1);
                        String _data = new ParseJSON(result).parseVersion();
                        constant.showPD();
                        if (_data != null && !_data.equals("0")) {
                            String versionArr[] = version.split("\\.");
                            String dataArr[] = _data.split("\\.");
                            int currVersion = Integer.parseInt(versionArr[0]);
                            int dataVersion = Integer.parseInt(dataArr[0]);
                            if (currVersion > dataVersion) {
                                SharedPreferences.Editor editor = FirstActivity.pref.edit();
                                editor.putString(getString(R.string.pref_version), _data);
                                editor.apply();
                                loadEmployeeMaster();
                            } else if (currVersion < dataVersion) {
                                showDia(8);
                            } else {
                                loadCompanyMaster();
                            }
                        } else if (_data == null) {
                            showDia(4);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        writeLog("MainActivity_loadData_versionRequest_" + error.getMessage());
                        FirebaseCrash.log("MainActivity_loadData_versionRequest_" + error.getMessage());
                        constant.showPD();
                        showDia(4);
                    }
                }
        );
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(versionRequest, "ABC");

    }

    private void loadCompanyMaster() {
        int max = db.getMaxCompId();
        String url = Constant.ipaddress + "/GetCompanyMaster?Id=" + max;
        Constant.showLog(url);
        writeLog("loadCompanyMaster_" + url);
        constant.showPD();
        VolleyRequests requests = new VolleyRequests(CustomerDetailsActivity.this);
        requests.refreshCompanyMaster(url, new ServerCallback() {
            @Override
            public void onSuccess(String result) {
                constant.showPD();
                loadData();
            }

            @Override
            public void onFailure(String result) {
                constant.showPD();
                showDia(12);
            }
        }, 0);
    }

    private void loadEmployeeMaster() {
        int max = db.getMaxEmpId();
        String url = Constant.ipaddress + "/GetEmployeeMaster?Id=" + max;
        Constant.showLog(url);
        writeLog("loadEmployeeMaster_" + url);
        final Constant constant = new Constant(CustomerDetailsActivity.this);
        constant.showPD();
        VolleyRequests requests = new VolleyRequests(CustomerDetailsActivity.this);
        requests.refreshEmployeeMaster(url, new ServerCallback() {
            @Override
            public void onSuccess(String result) {
                constant.showPD();
                loadCompanyMaster();
            }

            @Override
            public void onFailure(String result) {
                constant.showPD();
                showDia(12);
            }
        }, 0);
    }

    private void loadData() {
        ArrayList<UserClass> list = db.getUserDetail();
        adapter = new CustomerDetailListAdapter(CustomerDetailsActivity.this, list);
        listView.setAdapter(adapter);
        setDPCenter();
    }

    private void setDPCenter() {
        FirstActivity.pref = getSharedPreferences(FirstActivity.PREF_NAME, MODE_PRIVATE);
        int hoCode = FirstActivity.pref.getInt(getString(R.string.pref_hocode), 0);
        dpList.add("Select Dispatch Center");
        dpMap.put("Select Dispatch Center",0);
        Cursor res = db.getDPCenter(hoCode);
        if (res.moveToFirst()) {
            do {
                dpList.add(res.getString(res.getColumnIndex(DBHandler.Company_DisplayCmp)));
                dpMap.put(res.getString(res.getColumnIndex(DBHandler.Company_DisplayCmp)),
                        res.getInt(res.getColumnIndex(DBHandler.Company_Id)));
            } while (res.moveToNext());
        }
        res.close();
        sp_dpCenter.setAdapter(new ArrayAdapter<>(getApplicationContext(), R.layout.sizegroup_spinner_row, dpList));

    }

    private void startNewActivity(int id) {
        userClass = (UserClass) listView.getAdapter().getItem(0);
        userClass.setDpId(dpId);
        if (FirstActivity.pref.contains(getString(R.string.pref_dpId))) {
            int prevId = FirstActivity.pref.getInt(getString(R.string.pref_dpId), 0);
            if (prevId != userClass.getDpId()) {
                db.deleteTable(DBHandler.Table_DispatchMaster);
            }
        }
        String pin = userClass.getCustID() + "-" + "1234";
        SharedPreferences.Editor editor = FirstActivity.pref.edit();
        editor.putString(getString(R.string.pref_savedpin), pin);
        editor.putInt(getString(R.string.pref_retailCustId), userClass.getCustID());
        editor.putInt(getString(R.string.pref_branchid), userClass.getBranchId());
        editor.putInt(getString(R.string.pref_cityid), userClass.getCityId());
        editor.putInt(getString(R.string.pref_hocode), userClass.getHOCode());
        editor.putString(getString(R.string.pref_mobno), userClass.getMobile());
        editor.putInt(getString(R.string.pref_dpId), userClass.getDpId());
        editor.apply();
        //TODO; Check
        Intent intent;
        if (id==0){
            intent = new Intent(getApplicationContext(), MainActivity.class);
        } else {
            intent = new Intent(getApplicationContext(), ReportActivity.class);
        }
        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("cust", userClass);
        startActivity(intent);
        overridePendingTransition(R.anim.enter, R.anim.exit);
    }

    private void exportFile() {
        if (new CopyLog().copyLog(getApplicationContext())) {
            writeLog("MainActivity_exportfile_Log_File_Exported");
            sendMail1();
        } else {
            writeLog("MainActivity_exportfile_Error_While_Log_File_Exporting");
        }
    }

    private void sendMail1() {
        try {
            File sdFile = Constant.checkFolder(Constant.folder_name);
            File writeFile = new File(sdFile, Constant.log_file_name);
            GMailSender sender = new GMailSender(Constant.automailID, Constant.autoamilPass);
            Constant.showLog("Attached Log File :- " + writeFile.getAbsolutePath());
            sender.addAttachment(sdFile.getAbsolutePath() + File.separator + Constant.log_file_name, Constant.log_file_name, Constant.mail_body);
            String resp[] = {Constant.mailReceipient};
            AtomicInteger workCounter = new AtomicInteger(resp.length);
            for (String aResp : resp) {
                if (!aResp.equals("")) {
                    Constant.showLog("send Mail Recp :- " + aResp);
                    new sendMail(workCounter, aResp, sender).execute("");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class sendMail extends AsyncTask<String, Void, String> {
        private final AtomicInteger workCounter;

        ProgressDialog pd;
        String respMailId;
        GMailSender sender;

        sendMail(AtomicInteger workCounter, String _respMailId, GMailSender _sender) {
            respMailId = _respMailId;
            sender = _sender;
            this.workCounter = workCounter;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(CustomerDetailsActivity.this);
            pd.setCancelable(false);
            pd.setMessage("Please Wait...");
            pd.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                String res = respMailId;
                String mob = FirstActivity.pref.getString(getString(R.string.pref_mobno), "0");
                String subject = Constant.mail_subject + "_" + mob;
                sender.sendMail(subject, Constant.mail_body, Constant.automailID, res);
                return "1";
            } catch (Exception e) {
                writeLog("MainActivity_sendMailClass_" + e.getMessage());
                e.printStackTrace();
                return "0";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                int tasksLeft = this.workCounter.decrementAndGet();
                Constant.showLog("sendMail Work Counter " + tasksLeft);
                if (result.equals("1")) {
                    if (tasksLeft == 0) {
                        writeLog("MainActivity_sendMailClass_Mail_Send_Successfully");
                        Constant.showLog("sendMail END MULTI THREAD");
                        Constant.showLog("sendMail Work Counter END " + tasksLeft);
                        toast.setText("File Exported Successfully");
                    } else {
                        writeLog("MainActivity_sendMailClass_Mail_Send_UnSuccessfull1");
                        toast.setText("Error While Sending Mail");
                    }
                } else {
                    toast.setText("Error While Exporting Log File");
                    writeLog("MainActivity_sendMailClass_Mail_Send_UnSuccessfull");
                }
                toast.show();
                pd.dismiss();
            } catch (Exception e) {
                writeLog("MainActivity_sendMailClass_" + e.getMessage());
                e.printStackTrace();
                pd.dismiss();
            }
        }
    }

    private void writeLog(String _data) {
        new WriteLog().writeLog(getApplicationContext(), "CustomerDetailsActivity_" + _data);
    }

}
