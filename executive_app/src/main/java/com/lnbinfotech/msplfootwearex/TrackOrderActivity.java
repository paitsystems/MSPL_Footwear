package com.lnbinfotech.msplfootwearex;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.lnbinfotech.msplfootwearex.adapters.TrackOrderAdapter;
import com.lnbinfotech.msplfootwearex.constant.Constant;
import com.lnbinfotech.msplfootwearex.interfaces.ServerCallback;
import com.lnbinfotech.msplfootwearex.interfaces.ServerCallbackList;
import com.lnbinfotech.msplfootwearex.log.WriteLog;
import com.lnbinfotech.msplfootwearex.model.TrackOrderClass;
import com.lnbinfotech.msplfootwearex.volleyrequests.VolleyRequests;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TrackOrderActivity extends AppCompatActivity implements View.OnClickListener {
    private SimpleDateFormat sdf;
    private Toast toast;
    private ListView listView;
    private Constant constant;
    //public static List<String> order_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_default);
        init();
        loadOrderDetails();
    }
    private void init(){
        //order_list = new ArrayList<>();
        constant = new Constant(TrackOrderActivity.this);
        sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        toast = Toast.makeText(getApplicationContext(),"",Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER,0,0);
        listView = (ListView) findViewById(R.id.listView);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void loadOrderDetails(){
        String url = Constant.ipaddress+"/GetTrackOrderDetail?custId="+FirstActivity.pref.getInt(getString(R.string.pref_retailCustId),0);
        Constant.showLog(url);
        writeLog("loadOrderDetails_"+url);
        constant.showPD();
        VolleyRequests requests = new VolleyRequests(TrackOrderActivity.this);
        requests.loadTrackOrederDetail(url, new ServerCallbackList() {

            @Override
            public void onSuccess(Object result) {
                constant.showPD();
                List<TrackOrderClass> list = (List<TrackOrderClass>) result;
                if(list.size() != 0){
                    TrackOrderAdapter adapter = new TrackOrderAdapter(list,getApplicationContext());
                     listView.setAdapter(adapter);
                }else {
                    showPopup(2);
                }

            }

            @Override
            public void onFailure(Object result) {
                constant.showPD();
                showPopup(2);
            }
        });
    }

    private void showPopup(int id){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
       if(id == 0) {
           builder.setMessage("Do you want to exit?");
           builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
               @Override
               public void onClick(DialogInterface dialogInterface, int i) {
                   finish();
               }
           });
           builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
               @Override
               public void onClick(DialogInterface dialogInterface, int i) {
                   dialogInterface.dismiss();
               }
           });
       } else if (id == 1) {
           builder.setMessage("Data Loaded Successfully");
           builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
               @Override
               public void onClick(DialogInterface dialog, int which) {
                   dialog.dismiss();
               }
           });
       }else if (id == 2) {
           builder.setMessage("Error While Loading Data?");
           builder.setPositiveButton("Try again", new DialogInterface.OnClickListener() {
               @Override
               public void onClick(DialogInterface dialog, int which) {
                   loadOrderDetails();
               }
           });
       }else if (id == 3) {
           builder.setMessage("");
           builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
               @Override
               public void onClick(DialogInterface dialog, int which) {
                   dialog.dismiss();
               }
           });
       }

        builder.create().show();
    }

    @Override
    public void onClick(View view) {

    }
    private void writeLog(String _data){
        new WriteLog().writeLog(getApplicationContext(),"TrackOrderActivity_" +_data);
    }
}