package com.pait.dispatch_app;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
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
import com.pait.dispatch_app.log.WriteLog;
import com.pait.dispatch_app.parse.ParseJSON;
import com.pait.dispatch_app.parse.UserClass;
import com.pait.dispatch_app.volleyrequests.VolleyRequests;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CustomerDetailsActivity extends AppCompatActivity
        implements View.OnClickListener,
        LocationProvider.LocationCallback1 {

    private ListView listView;
    private CustomerDetailListAdapter adapter;
    private DBHandler db;
    private Toast toast;
    private Constant constant;
    private Button btn_save;
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
                dpId = dpMap.get(br);
                //userClass.setDpId(id);
                Constant.showLog(br + " " + dpId);
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
        switch (view.getId()) {
            case R.id.btn_save:
                startNewActivity();
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

    /*@Override
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
    }*/

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
        btn_save.setOnClickListener(this);
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
            //TODO: Check App Name
            builder.setMessage("MSPL Executive New Version Is Available");
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
        String url1 = Constant.ipaddress + "/GetVersionV5?type=E";
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
                                loadCompanyMaster();
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
                showDia(2);
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
        int hoCode = FirstActivity.pref.getInt(getString(R.string.pref_branchid), 0);
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

    private void startNewActivity() {
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
        finish();
        //TODO; Check
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("cust", userClass);
        startActivity(intent);
        overridePendingTransition(R.anim.enter, R.anim.exit);
    }


    private void writeLog(String _data) {
        new WriteLog().writeLog(getApplicationContext(), "CustomerDetailsActivity_" + _data);
    }

}
