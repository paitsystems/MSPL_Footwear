package com.lnbinfotech.msplfootwearex;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
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

import com.crashlytics.android.Crashlytics;
import com.lnbinfotech.msplfootwearex.adapters.CustomerDetailListAdapter;
import com.lnbinfotech.msplfootwearex.connectivity.ConnectivityTest;
import com.lnbinfotech.msplfootwearex.constant.Constant;
import com.lnbinfotech.msplfootwearex.db.DBHandler;
import com.lnbinfotech.msplfootwearex.interfaces.ServerCallback;
import com.lnbinfotech.msplfootwearex.location.LocationProvider;
import com.lnbinfotech.msplfootwearex.log.WriteLog;
import com.lnbinfotech.msplfootwearex.model.CustomerDetailClass;
import com.lnbinfotech.msplfootwearex.model.UserClass;
import com.lnbinfotech.msplfootwearex.volleyrequests.VolleyRequests;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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

        provider = new LocationProvider(CustomerDetailsActivity.this,CustomerDetailsActivity.this);

        checkIsActive();
        loadData();

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
            tv_version.setText("Version : "+version);
            Constant.showLog("App Version " + version);
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

    private void init() {
        listView = (ListView) findViewById(R.id.listView);
        db = new DBHandler(getApplicationContext());
        toast = Toast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        constant = new Constant(CustomerDetailsActivity.this);
        tv_version = (TextView) findViewById(R.id.tv_version);
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
        }
        builder.create().show();
    }

    private void checkIsActive(){
        constant = new Constant(CustomerDetailsActivity.this);
        if(ConnectivityTest.getNetStat(getApplicationContext())) {
            int id = FirstActivity.pref.getInt(getString(R.string.pref_retailCustId), 0);
            String url = Constant.ipaddress + "/GetActiveStatus?id=" + id + "&type=E";
            Constant.showLog(url);
            writeLog("checkIsActive_" + url);
            constant.showPD();
            VolleyRequests requests = new VolleyRequests(CustomerDetailsActivity.this);
            requests.getActiveStatus(url, new ServerCallback() {
                @Override
                public void onSuccess(String result) {
                    constant.showPD();
                    if (result.equals("A")) {
                        loadData();
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

    private void loadData(){
        ArrayList<UserClass> list = db.getUserDetail();
        adapter = new CustomerDetailListAdapter(CustomerDetailsActivity.this,list);
        listView.setAdapter(adapter);
    }

    private void writeLog(String _data){
        new WriteLog().writeLog(getApplicationContext(),"CustomerDetailsActivity_"+_data);
    }

    @Override
    public void handleNewLocation(Location location) {
        Constant.showLog("handleNewLocation");
        double lat = location.getLatitude();
        double lon = location.getLongitude();
        Constant.showLog(lat+"-"+lon);
        toast.setText(lat+"-"+lon);
        toast.show();
        /*Geocoder geo = new Geocoder(CustomerDetailsActivity.this, Locale.ENGLISH);
        try {
            List<Address> address = geo.getFromLocation(lat, lon, 1);
            if (address != null) {
                Address returnedAddress = address.get(0);
                StringBuilder strReturnedAddress = new StringBuilder();
                for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                Constant.showLog(strReturnedAddress.toString());
            } else {
                Toast.makeText(getApplicationContext(), "Location Not Updated", Toast.LENGTH_LONG).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Location Not Updated", Toast.LENGTH_LONG).show();
        }*/
    }
}
