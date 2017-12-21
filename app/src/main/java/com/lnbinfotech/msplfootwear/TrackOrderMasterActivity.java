package com.lnbinfotech.msplfootwear;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.view.Gravity;

import com.lnbinfotech.msplfootwear.adapters.TrackOrderMasterAdapter;
import com.lnbinfotech.msplfootwear.constant.Constant;
import com.lnbinfotech.msplfootwear.interfaces.ServerCallbackList;
import com.lnbinfotech.msplfootwear.log.WriteLog;
import com.lnbinfotech.msplfootwear.model.TrackOrderMasterClass;
import com.lnbinfotech.msplfootwear.volleyrequests.VolleyRequests;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class TrackOrderMasterActivity extends AppCompatActivity implements View.OnClickListener {

    private Toast toast;
    private ListView listView;
    private Constant constant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_order_master);

        init();

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        loadOrderDetails();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(listView.getWindowToken(), 0);
                TrackOrderMasterClass trackOrderClass = (TrackOrderMasterClass) listView.getItemAtPosition(i);
                Intent intent = new Intent(TrackOrderMasterActivity.this,TrackOrderDetailActivityChanged.class);
                intent.putExtra("trackorderclass", trackOrderClass);
                startActivity(intent);
                overridePendingTransition(R.anim.enter,R.anim.exit);
            }
        });
    }

    private void init() {
        constant = new Constant(TrackOrderMasterActivity.this);
        toast = Toast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        listView = (ListView) findViewById(R.id.listView);
    }

    @Override
    public void onBackPressed() {
        //showDia(0);
        new Constant(TrackOrderMasterActivity.this).doFinish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //showDia(0);
                new Constant(TrackOrderMasterActivity.this).doFinish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void loadOrderDetails() {
        String url = Constant.ipaddress + "/GetTrackOrderMaster?custId=" + FirstActivity.pref.getInt(getString(R.string.pref_retailCustId), 0);
        Constant.showLog(url);
        writeLog("loadOrderDetails_" + url);
        constant.showPD();
        VolleyRequests requests = new VolleyRequests(TrackOrderMasterActivity.this);
        requests.loadTrackOrder(url, new ServerCallbackList() {
            @Override
            public void onSuccess(Object result) {
                constant.showPD();
                List<TrackOrderMasterClass> list = (List<TrackOrderMasterClass>) result;
                if (list.size() != 0) {
                    TrackOrderMasterAdapter adapter = new TrackOrderMasterAdapter(list, getApplicationContext());
                    listView.setAdapter(adapter);
                } else {
                    showPopup(1);
                }
            }
            @Override
            public void onFailure(Object result) {
                constant.showPD();
                showPopup(2);
            }
        });
    }

    private void showPopup(int id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if (id == 0) {
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
        } else if (id == 2) {
            builder.setMessage("Error While Loading Data?");
            builder.setPositiveButton("Try again", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    loadOrderDetails();
                }
            });
        } else if (id == 3) {
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

    private void writeLog(String _data) {
        new WriteLog().writeLog(getApplicationContext(), "TrackOrderActivity_" + _data);
    }

}
