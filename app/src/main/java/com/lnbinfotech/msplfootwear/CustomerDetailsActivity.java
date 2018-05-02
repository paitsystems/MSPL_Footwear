package com.lnbinfotech.msplfootwear;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.crashlytics.android.Crashlytics;
import com.google.firebase.crash.FirebaseCrash;
import com.lnbinfotech.msplfootwear.adapters.CustomerDetailListAdapter;
import com.lnbinfotech.msplfootwear.connectivity.ConnectivityTest;
import com.lnbinfotech.msplfootwear.constant.AppSingleton;
import com.lnbinfotech.msplfootwear.constant.Constant;
import com.lnbinfotech.msplfootwear.db.DBHandler;
import com.lnbinfotech.msplfootwear.interfaces.ServerCallback;
import com.lnbinfotech.msplfootwear.location.LocationProvider;
import com.lnbinfotech.msplfootwear.log.WriteLog;
import com.lnbinfotech.msplfootwear.model.CustomerDetailClass;
import com.lnbinfotech.msplfootwear.model.UserClass;
import com.lnbinfotech.msplfootwear.parse.ParseJSON;
import com.lnbinfotech.msplfootwear.volleyrequests.VolleyRequests;

import java.util.ArrayList;

public class CustomerDetailsActivity extends AppCompatActivity
        implements View.OnClickListener,
        LocationProvider.LocationCallback1{

    private ListView listView;
    private CustomerDetailListAdapter adapter;
    private DBHandler db;
    private Toast toast;
    private Constant constant;
    private String version = "";
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

        checkIsActive();

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
            builder.setMessage("You Are Not An Active Customer?");
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
                    checkVersion();
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        }else if(a==8) {
            builder.setTitle("Update App");
            builder.setMessage("Smart Ticket New Version Is Available");
            builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //FirstActivity.pref.edit().clear().commit();
                    //db.deleteTabel(DBHandler.Ticket_Master_Table);
                    //db.deleteTabel(DBHandler.SMLMAST_Table);
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
            /*builder.setNegativeButton("Later", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });*/
        }
        builder.create().show();
    }

    private void checkIsActive() {
        constant = new Constant(CustomerDetailsActivity.this);
        if (ConnectivityTest.getNetStat(getApplicationContext())) {
            int id = FirstActivity.pref.getInt(getString(R.string.pref_retailCustId), 0);
            String url = Constant.ipaddress + "/GetActiveStatus?id=" + id + "&type=C";
            Constant.showLog(url);
            writeLog("checkIsActive_" + url);
            constant.showPD();
            VolleyRequests requests = new VolleyRequests(CustomerDetailsActivity.this);
            requests.getActiveStatus(url, new ServerCallback() {
                @Override
                public void onSuccess(String result) {
                    constant.showPD();
                    if (result.equals("A")) {
                        //loadData();
                        checkVersion();
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

    private void checkVersion(){
        constant.showPD();
        String url1 = Constant.ipaddress+"/GetVersion";
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
            final double lat = location.getLatitude();
            final double lon = location.getLongitude();
            Constant.showLog(lat + "-" + lon);
            toast.setText(lat + "-" + lon);
            //toast.show();
            /*Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(this, Locale.getDefault());
            addresses = geocoder.getFromLocation(lat, lon, 1);
            String address = addresses.get(0).getAddressLine(0);
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            String knownName = addresses.get(0).getFeatureName();
            Constant.showLog(address+"-"+city+"-"+state+"-"+country+"-"+postalCode+"-"+knownName);*/
            Constant.showLog(address);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void locationAvailable() {
        Constant.showLog("Location Available");
    }

}
