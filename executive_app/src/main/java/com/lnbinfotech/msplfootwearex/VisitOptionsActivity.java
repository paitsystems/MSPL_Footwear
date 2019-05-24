package com.lnbinfotech.msplfootwearex;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.lnbinfotech.msplfootwearex.constant.Constant;
import com.lnbinfotech.msplfootwearex.db.DBHandler;
import com.lnbinfotech.msplfootwearex.location.LocationProvider;
import com.lnbinfotech.msplfootwearex.log.WriteLog;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONObject;
import org.json.JSONStringer;

public class VisitOptionsActivity extends AppCompatActivity implements
        View.OnClickListener,LocationProvider.LocationCallback1 {

    private Constant constant;
    private Toast toast;
    private TextView tv_arealine,tv_area, tv_shopname_display, tv_address, tv_phone1, tv_custname_display;
    private CardView card_take_order, card_payment, card_ledger_report, card_feedback,card_track_order;
    private int custId = 0;
    private String custName = "", imgName= "NA.jpg";
    private ImageView img;
    private LocationProvider provider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(Constant.liveTestFlag==1) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }

        //setContentView(R.layout.activity_visit_option);
        setContentView(R.layout.visit_option_test1);
        //setContentView(R.layout.visitoptionsdemo);

        init();

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.visitoption);
        }
        try {
            String arealine = getIntent().getExtras().getString("area_line");
            String area = getIntent().getExtras().getString("area_name");
            custName = getIntent().getExtras().getString("child_selected");
            custId = Integer.parseInt(getIntent().getExtras().getString("cust_id"));
            DisplayCustListActivity.custId = custId;
            tv_arealine.setText(arealine);
            tv_area.setText("- " +area);
            tv_shopname_display.setText(custName);
            setCustInfo();
        }catch (Exception e){
            e.printStackTrace();
        }
        card_take_order.setOnClickListener(this);
        card_payment.setOnClickListener(this);
        card_ledger_report.setOnClickListener(this);
        card_feedback.setOnClickListener(this);
        card_track_order.setOnClickListener(this);

    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            if (provider != null) {
                provider.disconnect();
            }
        }catch (Exception e){
            e.printStackTrace();
            writeLog("onPause_"+e.getMessage());
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.card_take_order:
                showDia(1);
                break;
            case R.id.card_payment:
                Intent intent = new Intent(getApplicationContext(), VisitPaymentFormActivity.class);
                intent.putExtra("cust_id", String.valueOf(custId));
                intent.putExtra("child_selected", custName);
                startActivity(intent);
                overridePendingTransition(R.anim.enter, R.anim.exit);
                break;
            case R.id.card_ledger_report:
                Intent in = new Intent(getApplicationContext(), LedgerReportActivity.class);
                in.putExtra("cust_id", String.valueOf(custId));
                in.putExtra("child_selected", custName);
                startActivity(in);
                overridePendingTransition(R.anim.enter, R.anim.exit);
                break;
            case R.id.card_feedback:
                startActivity(new Intent(getApplicationContext(), FeedbackActivity.class));
                overridePendingTransition(R.anim.enter, R.anim.exit);
                break;
            case R.id.tv_phone1:
                String str = tv_phone1.getText().toString();
                if (!str.equals("") && !str.equals("0")) {
                    Intent phoneIntent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", str, null));
                    startActivity(phoneIntent);
                    overridePendingTransition(R.anim.enter, R.anim.exit);
                }
                break;
            case R.id.card_track_order:
                SharedPreferences.Editor editor = FirstActivity.pref.edit();
                editor.putInt(getString(R.string.pref_selcustid), custId);
                editor.putString(getString(R.string.pref_selcustname), custName);
                editor.apply();
                startActivity(new Intent(getApplicationContext(), TrackOrderMasterActivity.class));
                overridePendingTransition(R.anim.enter, R.anim.exit);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        new Constant(VisitOptionsActivity.this).doFinish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.visitoptionactivity_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                new Constant(VisitOptionsActivity.this).doFinish();
                break;
            case R.id.custLoc:
                showDia(4);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setCustInfo(){
        Cursor res = new DBHandler(getApplicationContext()).getCustInfo(custId);
        if(res.moveToFirst()){
            do{
                tv_custname_display.setText(res.getString(res.getColumnIndex(DBHandler.CM_Name)));
                tv_address.setText(res.getString(res.getColumnIndex(DBHandler.CM_Address)));
                tv_phone1.setText(res.getString(res.getColumnIndex(DBHandler.CM_MobileNo)));
                imgName= res.getString(res.getColumnIndex(DBHandler.CM_ImagePath));
                Glide.with(getApplicationContext()).load(Constant.custimgUrl+imgName)
                        .thumbnail(0.5f)
                        .crossFade()
                        .placeholder(R.drawable.ic_male)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(img);
            }while (res.moveToNext());
        }
        res.close();
    }

    private void catchCustLoc(){
        try {
            constant = new Constant(VisitOptionsActivity.this);
            constant.showPD();
            provider.connect();
        }catch (Exception e){
            e.printStackTrace();
            writeLog("catchCustLoc_"+e.getMessage());
            toast.setText("Please Try Again...");
            toast.show();
        }
    }

    private void init() {
        tv_shopname_display = (TextView) findViewById(R.id.tv_shopname_display);
        tv_arealine = (TextView) findViewById(R.id.tv_arealine);
        card_take_order = (CardView) findViewById(R.id.card_take_order);
        card_payment = (CardView) findViewById(R.id.card_payment);
        card_ledger_report = (CardView) findViewById(R.id.card_ledger_report);
        card_feedback = (CardView) findViewById(R.id.card_feedback);
        toast = Toast.makeText(getApplicationContext(),"",Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER,0,0);
        tv_address = (TextView) findViewById(R.id.tv_address);
        tv_phone1 = (TextView) findViewById(R.id.tv_phone1);
        tv_phone1.setOnClickListener(this);
        img = (ImageView) findViewById(R.id.img);
        card_track_order = (CardView) findViewById(R.id.card_track_order);
        tv_custname_display = (TextView) findViewById(R.id.tv_custname_display);
        tv_area = (TextView) findViewById(R.id.tv_area);
        FirstActivity.pref = getSharedPreferences(FirstActivity.PREF_NAME,MODE_PRIVATE);
        provider = new LocationProvider(VisitOptionsActivity.this,VisitOptionsActivity.this,VisitOptionsActivity.this);
    }

    private void showDia(int a) {
        AlertDialog.Builder builder = new AlertDialog.Builder(VisitOptionsActivity.this);
        builder.setCancelable(false);
        if (a == 0) {
            builder.setMessage("Do You Want To Exit App?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    new Constant(VisitOptionsActivity.this).doFinish();
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
            builder.setTitle("Take Order");
            builder.setMessage("How do you want to take order?");
            builder.setPositiveButton("With Photos", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startActivity(new Intent(getApplicationContext(),MainImagewiseSetwiseOrderActivity.class));
                    overridePendingTransition(R.anim.enter,R.anim.exit);
                    dialog.dismiss();
                }
            });
            builder.setNegativeButton("Without Photos", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startActivity(new Intent(getApplicationContext(),CutsizeSetwiseOrderActivity.class));
                    overridePendingTransition(R.anim.enter,R.anim.exit);
                    dialog.dismiss();
                }
            });
            builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        }else if (a == 2) {
            builder.setMessage("Location Updated Successfully");
            builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        }else if (a == 3) {
            builder.setMessage("Error While Updating Customer Location");
            builder.setPositiveButton("Try Again", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    new Constant();
                    catchCustLoc();
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        }else if (a == 4) {
            builder.setMessage("Do You Want To Update Customer Location?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    catchCustLoc();
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        }
        builder.create().show();
    }

    private void writeLog(String _data){
        new WriteLog().writeLog(getApplicationContext(),"VisitOptionsActivity_"+_data);
    }

    @Override
    public void handleNewLocation(Location location, String address) {
        constant.showPD();
        try {
            provider.disconnect();
            double lat = location.getLatitude();
            double lon = location.getLongitude();
            String data = custId + "|" + lat + "|" + lon + "|" + address + "|" + "C";
            new updateCustLoc().execute(data);
        }catch (Exception e){
            e.printStackTrace();
            toast.setText("Please Try Again...");
            toast.show();
        }
    }

    private class updateCustLoc extends AsyncTask<String, Void, String> {

        private ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(VisitOptionsActivity.this);
            pd.setMessage("Please Wait...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... url) {
            String value = "";
            DefaultHttpClient httpClient = null;
            HttpPost request = new HttpPost(Constant.ipaddress + "/SaveCustomerLoc");
            request.setHeader("Accept", "application/json");
            request.setHeader("Content-type", "application/json");
            try {

                JSONStringer vehicle = new JSONStringer().object().key("rData").object().key("details").value(url[0]).endObject().endObject();
                Constant.showLog(vehicle.toString());
                writeLog("updateCustLoc_"+vehicle.toString());
                StringEntity entity = new StringEntity(vehicle.toString());
                request.setEntity(entity);
                HttpParams httpParams = new BasicHttpParams();
                HttpConnectionParams.setConnectionTimeout(httpParams,Constant.TIMEOUT_CON);
                HttpConnectionParams.setSoTimeout(httpParams, Constant.TIMEOUT_SO);
                httpClient = new DefaultHttpClient(httpParams);
                HttpResponse response = httpClient.execute(request);
                Constant.showLog("Saving : " + response.getStatusLine().getStatusCode());
                value = new BasicResponseHandler().handleResponse(response);
            } catch (Exception e) {
                e.printStackTrace();
                writeLog("updateCustLoc_result_" + e.getMessage());
            }
            finally {
                try{
                    if(httpClient!=null) {
                        httpClient.getConnectionManager().shutdown();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    writeLog("updateCustLoc_finally_"+e.getMessage());
                }
            }
            return value;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                Constant.showLog(result);
                String str = new JSONObject(result).getString("SaveCustomerLocResult");
                str = str.replace("\"", "");
                Constant.showLog(str);
                pd.dismiss();
                writeLog("updateCustLoc_result_" + str + "_" + result);
                if (!str.equals("0")) {
                    writeLog("updateCustLoc_result_" + str);
                    showDia(2);
                } else {
                    showDia(3);
                }
            } catch (Exception e) {
                writeLog("updateCustLoc_" + e.getMessage());
                e.printStackTrace();
                showDia(3);
                pd.dismiss();
            }
        }
    }

    @Override
    public void locationAvailable() {

    }
}
