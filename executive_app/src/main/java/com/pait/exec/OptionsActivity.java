package com.pait.exec;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.pait.exec.connectivity.ConnectivityTest;
import com.pait.exec.constant.Constant;
import com.pait.exec.constant.Utility;
import com.pait.exec.db.DBHandler;
import com.pait.exec.interfaces.DatabaseUpdateInterface;
import com.pait.exec.log.CopyLog;
import com.pait.exec.log.WriteLog;
import com.pait.exec.mail.GMailSender;
import com.pait.exec.model.NewCustomerEntryClass;

import java.io.File;
import java.util.concurrent.atomic.AtomicInteger;

public class OptionsActivity extends AppCompatActivity implements View.OnClickListener, DatabaseUpdateInterface {

    public static NewCustomerEntryClass new_cus;
    private CardView card_take_order, card_visit, card_report, card_new_cust_entry, card_track_order;
    public static float custDisc = 0;
    private Toast toast;
    private Menu mMenu;
    private TextView actionbar_noti_tv, tv_address, tv_phone1,tv_phone2,tv_mobile1, tv_mobile2, tv_email, tv_lastSync;
    private DBHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(Constant.liveTestFlag==1) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }

        //setContentView(R.layout.activity_options);
        // setContentView(R.layout.optionstext);
        setContentView(R.layout.test);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        init();

        /*if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }*/

        card_take_order.setOnClickListener(this);
        card_track_order.setOnClickListener(this);
        card_visit.setOnClickListener(this);
        card_report.setOnClickListener(this);
        card_new_cust_entry.setOnClickListener(this);
        tv_phone1.setOnClickListener(this);
        tv_phone2.setOnClickListener(this);
        tv_mobile1.setOnClickListener(this);
        tv_mobile2.setOnClickListener(this);

        //TODO: JobScheduled
        Utility.scheduledJob(getApplicationContext());
        /*Intent intent1 = new Intent(getApplicationContext(),AutoSyncService.class);
        startService(intent1);*/

        setContactUs();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mMenu!=null){
            onCreateOptionsMenu(mMenu);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.card_take_order:
                Intent in = new Intent(getApplicationContext(), DisplayCustListActivity.class);
                in.putExtra("from","order");
                startActivity(in);
                overridePendingTransition(R.anim.enter, R.anim.exit);
                break;
            case R.id.card_visit:
                startActivity(new Intent(getApplicationContext(), ArealinewiseAreaSelectionActivity.class));
                //startActivity(new Intent(getApplicationContext(), AreawiseCustomerSelectionActivity.class));
                overridePendingTransition(R.anim.enter, R.anim.exit);
                break;
            case R.id.card_reports:
                toast.setText("Under Development");
                toast.show();
                break;
            case R.id.card_new_cust_entry:
                finish();
                startActivity(new Intent(getApplicationContext(), NewCustomerEntryActivity.class));
                overridePendingTransition(R.anim.enter, R.anim.exit);
                break;
            case R.id.card_track_order:
                Intent in1 = new Intent(getApplicationContext(), DisplayCustListActivity.class);
                in1.putExtra("from","trackorder");
                startActivity(in1);
                overridePendingTransition(R.anim.enter, R.anim.exit);
                break;
            case R.id.tv_phone1:
                String phone1 = tv_phone1.getText().toString();
                if(!phone1.equals("")&& !phone1.equals("0")){
                    makeCall(phone1);
                }
                break;
            case R.id.tv_phone2:
                String phone2 = tv_phone2.getText().toString();
                if(!phone2.equals("")&& !phone2.equals("0")){
                    makeCall(phone2);
                }
                break;
            case R.id.tv_mobile1:
                String mob1 = tv_mobile1.getText().toString();
                if(!mob1.equals("")&& !mob1.equals("0")){
                    makeCall(mob1);
                }
                break;
            case R.id.tv_mobile2:
                String mob2 = tv_mobile2.getText().toString();
                if(!mob2.equals("")&& !mob2.equals("0")){
                    makeCall(mob2);
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
        mMenu = menu;
        menu.clear();
        getMenuInflater().inflate(R.menu.mainactivity_menu, menu);
        final MenuItem item = menu.findItem(R.id.cart);
        MenuItemCompat.setActionView(item, R.layout.actionbaar_badge_layout);
        View view = MenuItemCompat.getActionView(item);
        TextView actionbar_noti_tv = (TextView)view.findViewById(R.id.actionbar_noti_tv);
        actionbar_noti_tv.setText("0");

        int count = new DBHandler(getApplicationContext()).getCartCount();
        Constant.showLog("cart_count:"+count);
        actionbar_noti_tv.setText(String.valueOf(count));

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onOptionsItemSelected(item);
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                showDia(0);
                break;
            case R.id.refresh:
                startActivity(new Intent(getApplicationContext(), DataRefreshActivity.class));
                overridePendingTransition(R.anim.enter, R.anim.exit);
                break;
            /*case R.id.logout:
                break;*/
            case R.id.report_error:
                showDia(6);
                break;
            case R.id.cart:
                Intent intent = new Intent(getApplicationContext(), ViewCustomerOrderActiviy.class);
                intent.putExtra("from","option");
                startActivity(intent);
                overridePendingTransition(R.anim.enter, R.anim.exit);
                break;
            case R.id.clear:
                showDia(7);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void init() {
        toast = Toast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        db = new DBHandler(this);
        FirstActivity.pref = getSharedPreferences(FirstActivity.PREF_NAME,MODE_PRIVATE);
        //db.initInterface(OptionsActivity.this);
        new_cus = new NewCustomerEntryClass();
        card_take_order = (CardView) findViewById(R.id.card_take_order);
        card_visit = (CardView) findViewById(R.id.card_visit);
        card_report = (CardView) findViewById(R.id.card_reports);
        card_new_cust_entry = (CardView) findViewById(R.id.card_new_cust_entry);
        tv_address = (TextView) findViewById(R.id.tv_address);
        tv_phone1 = (TextView) findViewById(R.id.tv_phone1);
        tv_phone2 = (TextView) findViewById(R.id.tv_phone2);
        tv_mobile1 = (TextView) findViewById(R.id.tv_mobile1);
        tv_mobile2 = (TextView) findViewById(R.id.tv_mobile2);
        tv_email = (TextView) findViewById(R.id.tv_email);
        card_track_order = (CardView) findViewById(R.id.card_track_order);
        tv_lastSync = (TextView) findViewById(R.id.tv_lastSync);
    }

    private void showDia(int a) {
        AlertDialog.Builder builder = new AlertDialog.Builder(OptionsActivity.this);
        builder.setCancelable(false);
        if (a == -2) {
            builder.setMessage("Please Update Currency Master");
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        }else if (a == 0) {
            builder.setMessage("Do You Want To Exit App?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    new Constant(OptionsActivity.this).doFinish();
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
            builder.setTitle("Take Order");
            builder.setMessage("How do you want to take order?");
            builder.setPositiveButton("With Photos", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //startActivity(new Intent(getApplicationContext(), ImagewiseSetwiseOrderActivity.class));
                    //startActivity(new Intent(getApplicationContext(), TabImagewiseSetwiseOrderActivity.class));
                    startActivity(new Intent(getApplicationContext(), MainImagewiseSetwiseOrderActivity.class));
                    overridePendingTransition(R.anim.enter, R.anim.exit);
                    dialog.dismiss();
                }
            });
            builder.setNegativeButton("Without Photos", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                   // startActivity(new Intent(getApplicationContext(), CutsizeSetwiseOrderActivity.class));
                    Intent in = new Intent(getApplicationContext(), DisplayCustListActivity.class);
                    in.putExtra("from","order");
                    startActivity(in);
                    overridePendingTransition(R.anim.enter, R.anim.exit);
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
            builder.setMessage("Do You Want To Logout From App?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    new Constant(OptionsActivity.this).doFinish();
                    dialog.dismiss();
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        } else if (a == 6) {
            builder.setMessage("Do You Want To Report An Issue?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (ConnectivityTest.getNetStat(getApplicationContext())) {
                        exportfile();
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
        }else if (a == 7) {
            builder.setMessage("Do You Want To Clear Saved Order?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    db.deleteTable(DBHandler.Table_CustomerOrder);
                    if(mMenu!=null){
                        onCreateOptionsMenu(mMenu);
                    }
                    toast.setText("Order Cleared");
                    toast.show();
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

    private void setContactUs(){
        //int hocode = FirstActivity.pref.getInt(getString(R.string.pref_branchid),0);
        int hocode = FirstActivity.pref.getInt(getString(R.string.pref_hocode),0);
        Cursor res = db.getContactUsData(hocode);
        if(res.moveToFirst()){
            do{
                tv_address.setText(res.getString(res.getColumnIndex(DBHandler.Company_Company_Add)));
                tv_phone1.setText(res.getString(res.getColumnIndex(DBHandler.Company_Company_Phno)));
                tv_phone2.setText(res.getString(res.getColumnIndex(DBHandler.Company_Company_Phone2)));
                tv_mobile1.setText(res.getString(res.getColumnIndex(DBHandler.Company_MobileNo)));
                tv_mobile2.setText(res.getString(res.getColumnIndex(DBHandler.Company_Mobileno2)));
                tv_email.setText(res.getString(res.getColumnIndex(DBHandler.Company_Company_Email)));
            }while (res.moveToNext());
        }
        res.close();
        String lastSync = FirstActivity.pref.getString(getString(R.string.pref_lastSync),"");
        tv_lastSync.setText("Last Data Sync On - "+lastSync);
    }

    private void makeCall(String number){
        Intent phoneIntent = new Intent(Intent.ACTION_DIAL, Uri.fromParts(
                "tel", number, null));
        startActivity(phoneIntent);
        overridePendingTransition(R.anim.enter,R.anim.exit);
    }

    private void writeLog(String _data) {
        new WriteLog().writeLog(getApplicationContext(), _data);
    }

    private void exportfile() {
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

    @Override
    public void dbUpdated() {
        showDia(-2);
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
            pd = new ProgressDialog(OptionsActivity.this);
            pd.setCancelable(false);
            pd.setMessage("Please Wait...");
            pd.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                String res = respMailId;
                String mob = FirstActivity.pref.getString(getString(R.string.pref_mobno),"0");
                String subject = Constant.mail_subject+"_"+mob;
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
}
