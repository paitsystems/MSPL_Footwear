package com.pait.exec;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.crash.FirebaseCrash;
import com.pait.exec.adapters.CustomerDetailListAdapter;
import com.pait.exec.connectivity.ConnectivityTest;
import com.pait.exec.constant.AppSingleton;
import com.pait.exec.constant.Constant;
import com.pait.exec.db.DBHandler;
import com.pait.exec.interfaces.ServerCallback;
import com.pait.exec.location.LocationProvider;
import com.pait.exec.log.WriteLog;
import com.pait.exec.model.UserClass;
import com.pait.exec.parse.ParseJSON;
import com.pait.exec.volleyrequests.VolleyRequests;

import java.util.ArrayList;

public class CustomerDetailsActivity extends AppCompatActivity
        implements View.OnClickListener,
        LocationProvider.LocationCallback1{

    private ListView listView;
    private CustomerDetailListAdapter adapter;
    private DBHandler db;
    private Toast toast;
    private Constant constant;
    private String version = "", mobNo = "", id = "0";
    private TextView tv_version;
    private LocationProvider provider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(Constant.liveTestFlag==1) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }

        setContentView(R.layout.activity_customer_details);

        init();

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }
        provider = new LocationProvider(CustomerDetailsActivity.this,CustomerDetailsActivity.this,CustomerDetailsActivity.this);

        if(FirstActivity.pref.contains(getString(R.string.pref_imeino2))) {
            checkIsActive();
        }else{
            ArrayList<UserClass> userList = db.getUserDetail();
            if(!userList.isEmpty()){
                UserClass user = userList.get(0);
                mobNo = user.getMobile();
                id = String.valueOf(user.getCustID());
            }
            registerIMEINo2();
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                UserClass custClass = (UserClass) listView.getAdapter().getItem(i);
                Intent intent = new Intent(getApplicationContext(),CustomerLoginActivity.class);
                intent.putExtra("cust",custClass);
                startActivity(intent);
                overridePendingTransition(R.anim.enter,R.anim.exit);
            }
        });

        try {
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
            version = pInfo.versionCode+"."+pInfo.versionName;
            tv_version.setText("Version : "+pInfo.versionName);
            Constant.showLog("App Version " + version);
            writeLog("MainActivity_Version_"+version);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            writeLog("MainActivity_"+e.getMessage());
        }

        /*Button crashButton = new Button(this);
        crashButton.setText("Crash!");
        crashButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Crashlytics.getInstance().crash(); // Force a crash
            }
        });
        addContentView(crashButton,
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));*/
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
        switch (view.getId()) {
            case 0:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        showDia(0);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                showDia(0);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case LocationProvider.REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        Constant.showLog("Success");
                        break;
                    case Activity.RESULT_CANCELED:
                        provider.checkLocationAvailability();
                        Constant.showLog("Cancelled Success");
                        break;
                }
                break;
        }
    }

    private void init() {
        listView = (ListView) findViewById(R.id.listView);
        db = new DBHandler(getApplicationContext());
        toast = Toast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        constant = new Constant(CustomerDetailsActivity.this);
        tv_version = (TextView) findViewById(R.id.tv_version);
        FirstActivity.pref = getSharedPreferences(FirstActivity.PREF_NAME,MODE_PRIVATE);
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
        }else if (a == 1) {
            builder.setMessage("You Are Not An Active Employee?");
            builder.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    toast.cancel();
                    new Constant(CustomerDetailsActivity.this).doFinish();
                }
            });
        }else if (a == 2) {
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
        }else if (a == 3) {
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
        }else if(a==4) {
            builder.setMessage("Error While Loading Data");
            builder.setPositiveButton("Try Again", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    new Constant();
                    checkVersion();
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        }else if(a==5) {
            builder.setMessage("This Device Is Not Registered");
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    new Constant(CustomerDetailsActivity.this).doFinish();
                }
            });
        }else if(a==8) {
            builder.setTitle("Update App");
            builder.setMessage("MSPL Executive New Version Is Available");
            builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    new Constant(CustomerDetailsActivity.this).doFinish();
                    final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                    }
                    catch (android.content.ActivityNotFoundException anfe) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + appPackageName)));
                    }
                    dialog.dismiss();
                }
            });
        }else if (a == 9) {
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
        }
        builder.create().show();
    }

    private void registerIMEINo2(){
        constant = new Constant(CustomerDetailsActivity.this);
        if(ConnectivityTest.getNetStat(getApplicationContext())) {
            int id = FirstActivity.pref.getInt(getString(R.string.pref_retailCustId), 0);
            String __imeino = "",__imeino1 = "",__imeino2 = "";
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                __imeino = new Constant(getApplicationContext()).getIMEINo1();
                String[] arr = __imeino.split("\\^");
                if(arr.length>1) {
                    __imeino1 = arr[0];
                    __imeino2 = arr[1];
                }else{
                    __imeino1 = __imeino;
                    __imeino2 = __imeino;
                }
            }else{
                __imeino = new Constant(getApplicationContext()).getIMEINo();
                __imeino1 = __imeino;
                __imeino2 = __imeino;
            }
            final String imeino = __imeino;
            String url = Constant.ipaddress + "/UpdateIMEINo?mobileno="+mobNo+"&IMEINo1="
                    +__imeino1+"&IMEINo2="+__imeino2+"&type=E&id="+id;
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
                        editor.putBoolean(getString(R.string.pref_imeino2),true);
                        editor.apply();
                        checkIsActive();
                        writeLog("registerIMEINo2_Success_" + response);
                    } else  {
                        showDia(9);
                    }
                }
                @Override
                public void onFailure(String result) {
                    constant.showPD();
                    showDia(9);
                }
            });
        }else{
            showDia(3);
        }
    }

    private void checkIsActive(){
        constant = new Constant(CustomerDetailsActivity.this);
        if(ConnectivityTest.getNetStat(getApplicationContext())) {
            int id = FirstActivity.pref.getInt(getString(R.string.pref_retailCustId), 0);
            String __imeino = "",__imeino1 = "",__imeino2 = "";
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                __imeino = new Constant(getApplicationContext()).getIMEINo1();
                String[] arr = __imeino.split("\\^");
                if(arr.length>1) {
                    __imeino1 = arr[0];
                    __imeino2 = arr[1];
                }else{
                    __imeino1 = __imeino;
                    __imeino2 = __imeino;
                }
            }else{
                __imeino = new Constant(getApplicationContext()).getIMEINo();
                __imeino1 = __imeino;
                __imeino2 = __imeino;
            }
            final String imeino = __imeino,imeino1 = __imeino1,imeino2 = __imeino2;
            //String url = Constant.ipaddress + "/GetActiveStatusV5?id=" + id + "&type=E&imeino="+imeino;
            String url = Constant.ipaddress + "/GetActiveStatusV6?id=" + id + "&type=E&IMEINo1="+imeino1
                    +"&IMEINo2="+imeino2;
            Constant.showLog(url);
            writeLog("checkIsActive_" + url);
            constant.showPD();
            VolleyRequests requests = new VolleyRequests(CustomerDetailsActivity.this);
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
        }else{
            showDia(3);
        }
    }

    private void checkVersion(){
        constant = new Constant(CustomerDetailsActivity.this);
        constant.showPD();
        String url1 = Constant.ipaddress+"/GetVersionV5?type=E";
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
                            if (currVersion>dataVersion) {
                                SharedPreferences.Editor editor = FirstActivity.pref.edit();
                                editor.putString(getString(R.string.pref_version), _data);
                                editor.apply();
                                loadData();
                            }else if (currVersion<dataVersion){
                                showDia(8);
                            }else{
                                loadData();
                            }
                        } else if (_data == null) {
                                showDia(4);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        writeLog("MainActivity_loadData_versionRequest_"+ error.getMessage());
                        FirebaseCrash.log("MainActivity_loadData_versionRequest_"+ error.getMessage());
                            constant.showPD();
                            showDia(4);
                    }
                }
        );
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(versionRequest,"ABC");

    }

    private void loadData(){
        ArrayList<UserClass> list = db.getUserDetail();
        adapter = new CustomerDetailListAdapter(CustomerDetailsActivity.this,list);
        listView.setAdapter(adapter);
    }

    private void writeLog(String _data){
        new WriteLog().writeLog(getApplicationContext(),"CustomerDetailsActivity_"+_data);
    }

    @Override
    public void handleNewLocation(Location location,String address) {
        try {
            Constant.showLog("handleNewLocation");
            Constant.showLog("CustomerDetailsActivity_"+address);
        }catch (Exception e){
            e.printStackTrace();
            writeLog("handleNewLocation_"+e.getMessage());
        }
    }

    @Override
    public void locationAvailable() {
        Constant.showLog("Location Available");
    }

}
