package com.lnbinfotech.paragaon;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.lnbinfotech.paragaon.adapter.ShortDescListAdapter;
import com.lnbinfotech.paragaon.connectivity.ConnectivityTest;
import com.lnbinfotech.paragaon.constant.Constant;
import com.lnbinfotech.paragaon.log.CopyLog;
import com.lnbinfotech.paragaon.log.WriteLog;
import com.lnbinfotech.paragaon.mail.GMailSender;
import com.lnbinfotech.paragaon.model.TicketMasterClass;
import com.lnbinfotech.paragaon.parse.ParseJSON;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

// Created by lnb on 8/11/2016.

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    TextView tv_total, tv_complete, tv_pending;
    Button btn_view_all, btn_add;
    AutoCompleteTextView auto;
    ImageView img_add_new, img_view_all;
    Constant constant;
    Toast toast;
    ListView listView;
    public static int isUpdate = 0;

    AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_main);

        init();

        new Constant(getApplicationContext()).setRecurringAlarm();

        Drawable drawable = ContextCompat.getDrawable(getApplicationContext(),R.drawable.add_new_ticket_draw);
        drawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTint(drawable, ContextCompat.getColor(getApplicationContext(),R.color.darkgreen));

        btn_add.setBackground(drawable);
        btn_view_all.setBackground(drawable);

        btn_add.setOnClickListener(this);
        btn_view_all.setOnClickListener(this);
        mAdView = (AdView) findViewById(R.id.adView);

        AdRequest adRequest;
        if(Constant.liveTestFlag==1) {
            adRequest = new AdRequest.Builder().build();
        }else {
            adRequest = new AdRequest.Builder()
                    .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                    .addTestDevice("0558B791C50AB34B5650C3C48C9BD15E")
                    .build();
        }
        mAdView.loadAd(adRequest);

        loadData();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TicketMasterClass pendingTicketClass = (TicketMasterClass) listView.getItemAtPosition(i);
                Intent intent = new Intent(getApplicationContext(),UpdateTicketActivity.class);
                intent.putExtra("data",pendingTicketClass);
                startActivity(intent);
                overridePendingTransition(R.anim.enter,R.anim.exit);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(isUpdate==1){
            writeLog("MainActivity_onResume_loadData");
            isUpdate = 0;
            constant = new Constant(MainActivity.this);
            loadData();
        }
        if(mAdView!=null){
            mAdView.resume();
        }
    }

    @Override
    protected void onPause() {
        if(mAdView!=null){
            mAdView.pause();
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        if(mAdView!=null){
            mAdView.destroy();
        }
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        showDia(2);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_add:
                startActivity(new Intent(getApplicationContext(),AddNewTicketActivity.class));
                overridePendingTransition(R.anim.enter,R.anim.exit);
                break;
            case R.id.btn_view_all:
                startActivity(new Intent(getApplicationContext(),AllTicketListActivity.class));
                overridePendingTransition(R.anim.enter,R.anim.exit);
                break;
            case R.id.img_add_new:
                startActivity(new Intent(getApplicationContext(),AddNewTicketActivity.class));
                overridePendingTransition(R.anim.enter,R.anim.exit);
                break;
            case R.id.img_view_all:
                startActivity(new Intent(getApplicationContext(),AllTicketListActivity.class));
                overridePendingTransition(R.anim.enter,R.anim.exit);
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mainactivity_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId() == R.id.logout){
            showDia(0);
        }else if(item.getItemId() == R.id.refresh){
            showDia(1);
        }else if(item.getItemId() == R.id.report_error){
            showDia(6);
        }
        return super.onOptionsItemSelected(item);
    }

    void init(){
        auto = (AutoCompleteTextView) findViewById(R.id.auto);
        tv_total = (TextView) findViewById(R.id.tv_total);
        tv_complete = (TextView) findViewById(R.id.tv_complete);
        tv_pending = (TextView) findViewById(R.id.tv_pending);
        btn_view_all = (Button) findViewById(R.id.btn_view_all);
        btn_add = (Button) findViewById(R.id.btn_add);

        img_view_all = (ImageView) findViewById(R.id.img_view_all);
        img_add_new = (ImageView) findViewById(R.id.img_add_new);
        img_view_all.setOnClickListener(this);
        img_add_new.setOnClickListener(this);
        listView = (ListView) findViewById(R.id.listView);
        toast = Toast.makeText(getApplicationContext(),"",Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER,0,0);
        constant = new Constant(MainActivity.this);
    }

    void refreshUserData(){
        constant = new Constant(MainActivity.this);
        constant.showPD();
        try {
            String user = FirstActivity.pref.getString(getString(R.string.pref_username), "");
            String pass = FirstActivity.pref.getString(getString(R.string.pref_password), "");
            String url = Constant.ipaddress + "/getEmpValid?UserName=" + user + "&Password=" + pass;
            writeLog("MainActivity_refreshUserData_"+url);
            Constant.showLog(url);
            StringRequest request = new StringRequest(url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String result) {
                            Constant.showLog(result);
                            result = result.replace("\\", "");
                            result = result.replace("''", "");
                            result = result.substring(1, result.length() - 1);
                            if(new ParseJSON(result,getApplicationContext()).parseUserData() == 1){
                                showDia(4);
                                writeLog("MainActivity_refreshUserData_Success");
                            }else{
                                writeLog("MainActivity_refreshUserData_UnSuccess");
                                showDia(5);
                            }
                            constant.showPD();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            writeLog("MainActivity_refreshUserData_volley_"+ error.getMessage());
                            error.printStackTrace();
                            constant.showPD();
                            showDia(5);
                        }
                    }
            );

            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            queue.add(request);
        }catch (Exception e){
            writeLog("MainActivity_refreshUserData_"+e.getMessage());
            e.printStackTrace();
            toast.setText("Something Went Wrong");
            toast.show();
        }

    }

    void loadData(){
        constant.showPD();
        final AtomicInteger atomicInteger = new AtomicInteger(2);
        String url1 = Constant.ipaddress+"/GetCount?clientAuto="+FirstActivity.pref.getInt(getString(R.string.pref_auto),0);
        String url2 = Constant.ipaddress+"/GetTicketMaster?clientAuto="+FirstActivity.pref.getInt(getString(R.string.pref_auto),0)+"&type="+FirstActivity.pref.getString(getString(R.string.pref_emptype),"");
        Constant.showLog(url1);
        Constant.showLog(url2);
        StringRequest countRequest = new StringRequest(url1,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String result) {
                        Constant.showLog(result);
                        result = result.replace("\\", "");
                        result = result.replace("''", "");
                        result = result.substring(1,result.length()-1);
                        String _data = new ParseJSON(result).parseGetCountData();
                        int taskLeft = atomicInteger.decrementAndGet();
                        if(taskLeft==0) {
                            constant.showPD();
                        }
                        SharedPreferences.Editor editor = FirstActivity.pref.edit();
                        int total = 0;
                        if(_data!=null && !_data.equals("0")){
                            String[] data = _data.split("\\^");
                            total = Integer.parseInt(data[0]);
                            tv_total.setText(data[0]);
                            tv_complete.setText(data[1]);
                            tv_pending.setText(data[2]);
                        }else if(_data== null){
                            showDia(3);
                        }
                        editor.putInt(getString(R.string.pref_ticketTotal),total);
                        editor.apply();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        writeLog("MainActivity_loadData_countRequest_"+ error.getMessage());
                        SharedPreferences.Editor editor = FirstActivity.pref.edit();
                        editor.putInt(getString(R.string.pref_ticketTotal),0);
                        editor.apply();
                        error.printStackTrace();
                        int taskLeft = atomicInteger.decrementAndGet();
                        if(taskLeft==0) {
                            constant.showPD();
                        }
                        showDia(3);
                    }
                }
        );

        StringRequest descRequest = new StringRequest(url2,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String result) {
                        Constant.showLog(result);
                        result = result.replace("\\", "");
                        result = result.replace("''", "");
                        result = result.substring(1,result.length()-1);
                        /*ArrayList<ShortDescClass> descList = new ParseJSON(result).parseShortDesc();
                        int taskLeft = atomicInteger.decrementAndGet();
                        if(taskLeft==0) {
                            constant.showPD();
                        }
                        if(descList.size()!=0){
                            listView.setAdapter(null);
                            listView.setAdapter(new ShortDescListAdapter(descList,getApplicationContext()));
                        }else{
                            showDia(3);
                        }*/
                        ArrayList<TicketMasterClass> pendingTicketClassList = new ParseJSON(result).parseAllTicket();
                        int taskLeft = atomicInteger.decrementAndGet();
                        if(taskLeft==0) {
                            constant.showPD();
                        }
                        if(pendingTicketClassList!=null) {
                            if (pendingTicketClassList.size() != 0) {
                                listView.setAdapter(null);
                                listView.setAdapter(new ShortDescListAdapter(pendingTicketClassList, getApplicationContext()));
                            }
                        }else{
                            showDia(3);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        writeLog("MainActivity_loadData_descRequest_"+ error.getMessage());
                        error.printStackTrace();
                        int taskLeft = atomicInteger.decrementAndGet();
                        if(taskLeft==0) {
                            constant.showPD();
                        }
                        showDia(3);
                    }
                }
        );

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(countRequest);
        queue.add(descRequest);
    }

    void showDia(int i){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setCancelable(false);
        if(i==0){
            builder.setMessage("Do You Want To Logout App?");
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    SharedPreferences.Editor editor = FirstActivity.pref.edit();
                    editor.putBoolean(getResources().getString(R.string.pref_logged),false);
                    editor.apply();
                    dialogInterface.dismiss();
                    Constant.deleteLogFile();
                    new Constant(MainActivity.this).doFinish();
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
        }else if(i==1){
            builder.setMessage("Do You Want To Refresh Data?");
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    refreshUserData();
                    dialogInterface.dismiss();
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
        }else if(i==2){
            builder.setMessage("Do You Want To Exit App?");
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Constant.deleteLogFile();
                    new Constant(MainActivity.this).doFinish();
                    dialogInterface.dismiss();
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
        }else if(i==3) {
            builder.setMessage("Error While Loading Data");
            builder.setPositiveButton("Try Again", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    loadData();
                    dialog.dismiss();
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        }else if(i==4) {
            builder.setMessage("Data Updated Successfully");
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        }else if(i==5) {
            builder.setMessage("Error While Updating Data");
            builder.setPositiveButton("Try Again", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    refreshUserData();
                    dialog.dismiss();
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        }else if(i==6) {
            builder.setMessage("Do You Want To Report Error?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if(ConnectivityTest.getNetStat(getApplicationContext())) {
                        exportfile();
                    }else{
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
        }
        builder.create().show();
    }

    void exportfile(){
        if(new CopyLog().copyLog(getApplicationContext())) {
            writeLog("MainActivity_exportfile_Log_File_Exported");
            sendMail1();
        }else{
            writeLog("MainActivity_exportfile_Error_While_Log_File_Exporting");
        }
    }

    void sendMail1(){
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
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private class sendMail extends AsyncTask<String, Void, String> {
        private final AtomicInteger workCounter;

        ProgressDialog pd;
        String respMailId;
        GMailSender sender;

        sendMail(AtomicInteger workCounter, String _respMailId,GMailSender _sender){
            respMailId = _respMailId;
            sender = _sender;
            this.workCounter = workCounter;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(MainActivity.this);
            pd.setCancelable(false);
            pd.setMessage("Please Wait...");
            pd.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                String res = respMailId;
                sender.sendMail(Constant.mail_subject,Constant.mail_body,Constant.automailID,res);
                return "1";
            } catch (Exception e) {
                writeLog("MainActivity_sendMailClass_"+e.getMessage());
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
                if(result.equals("1")) {
                    if (tasksLeft == 0) {
                        writeLog("MainActivity_sendMailClass_Mail_Send_Successfully");
                        Constant.showLog("sendMail END MULTI THREAD");
                        Constant.showLog("sendMail Work Counter END " + tasksLeft);
                        toast.setText("File Exported Successfully");
                    } else {
                        writeLog("MainActivity_sendMailClass_Mail_Send_UnSuccessfull1");
                        toast.setText("Error While Sending Mail");
                    }
                }else{
                    toast.setText("Error While Exporting Log File");
                    writeLog("MainActivity_sendMailClass_Mail_Send_UnSuccessfull");
                }
                toast.show();
                pd.dismiss();
            }catch (Exception e){
                writeLog("MainActivity_sendMailClass_"+e.getMessage());
                e.printStackTrace();
                pd.dismiss();
            }
        }
    }

    private void writeLog(String _data){
        new WriteLog().writeLog(getApplicationContext(),_data);
    }

}
