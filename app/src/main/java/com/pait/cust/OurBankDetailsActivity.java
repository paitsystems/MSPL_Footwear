package com.pait.cust;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.Toast;
import android.view.Gravity;

import com.pait.cust.adapters.OurBankDetailsAdapter;
import com.pait.cust.connectivity.ConnectivityTest;
import com.pait.cust.constant.Constant;
import com.pait.cust.interfaces.ServerCallbackList;
import com.pait.cust.log.WriteLog;
import com.pait.cust.model.OurBankDetailsClass;
import com.pait.cust.volleyrequests.VolleyRequests;

import java.util.List;

public class OurBankDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    private Constant constant, constant1;
    private Toast toast;
    private ListView lv_account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(Constant.liveTestFlag==1) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }

        setContentView(R.layout.activity_our_bank_details);

        init();
        showBankDetails();

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

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
        new Constant(OurBankDetailsActivity.this).doFinish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //showDia(0);
                new Constant(OurBankDetailsActivity.this).doFinish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void init() {
        constant = new Constant(OurBankDetailsActivity.this);
        constant1 = new Constant(getApplicationContext());
        toast = Toast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        lv_account = (ListView) findViewById(R.id.lv_account);
        FirstActivity.pref = getSharedPreferences(FirstActivity.PREF_NAME,MODE_PRIVATE);
    }

    private void showBankDetails() {
        if (ConnectivityTest.getNetStat(OurBankDetailsActivity.this)) {
            try {
                lv_account.setAdapter(null);

                int hocode = FirstActivity.pref.getInt(getString(R.string.pref_hocode),0) ;

                String url = Constant.ipaddress + "/GetBankDetails?custType=O&status=A&hocode="+hocode;
                Constant.showLog(url);
                writeLog("showBankDetails()" + url);
                constant.showPD();
                VolleyRequests requests = new VolleyRequests(OurBankDetailsActivity.this);
                requests.loadBankDetails(url, new ServerCallbackList() {
                    @Override
                    public void onSuccess(Object result) {
                        constant.showPD();
                        List<OurBankDetailsClass> list = (List<OurBankDetailsClass>) result;
                        if (list.size() != 0) {
                            OurBankDetailsAdapter adapter = new OurBankDetailsAdapter(list, getApplicationContext());
                            lv_account.setAdapter(adapter);
                            //setTotal(list);
                        } else {
                            toast.setText("No Record Available");
                            toast.show();
                        }
                    }

                    @Override
                    public void onFailure(Object result) {
                        constant.showPD();
                        showPopup(2);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                constant.showPD();
                showPopup(2);
                writeLog("showOutstandingReport_" + e.getMessage());
            }
        } else {
            toast.setText("You Are Offline");
            toast.show();
        }
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
            builder.setMessage("No Record Available");
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    showBankDetails();
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
        } else if (id == 4) {
            builder.setMessage("select month type:");
            builder.setPositiveButton("3 Month", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            builder.setNegativeButton("6 Month", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
        }
    }

    private void writeLog(String _data) {
        new WriteLog().writeLog(getApplicationContext(), "OurBankDetailsActivity_" + _data);
    }
}
