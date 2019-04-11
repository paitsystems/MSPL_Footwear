package com.lnbinfotech.msplfootwear;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
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

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.lnbinfotech.msplfootwear.connectivity.ConnectivityTest;
import com.lnbinfotech.msplfootwear.constant.Constant;
import com.lnbinfotech.msplfootwear.constant.Utitlity;
import com.lnbinfotech.msplfootwear.db.DBHandler;
import com.lnbinfotech.msplfootwear.interfaces.RetrofitApiInterface;
import com.lnbinfotech.msplfootwear.interfaces.ServerCallback;
import com.lnbinfotech.msplfootwear.log.CopyLog;
import com.lnbinfotech.msplfootwear.log.WriteLog;
import com.lnbinfotech.msplfootwear.mail.GMailSender;
import com.lnbinfotech.msplfootwear.model.ImagewiseAddToCartClass;
import com.lnbinfotech.msplfootwear.model.ProductMasterClass;
import com.lnbinfotech.msplfootwear.model.SchemeMasterClass;
import com.lnbinfotech.msplfootwear.utility.RetrofitApiBuilder;
import com.lnbinfotech.msplfootwear.volleyrequests.VolleyRequests;

import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OptionsActivity extends AppCompatActivity implements View.OnClickListener,
        BaseSliderView.OnSliderClickListener,
        ViewPagerEx.OnPageChangeListener{

    private CardView card_bank_details,card_give_order, card_account, card_track_order, card_profile, card_scheme, card_whats_new, card_feedback;
    public static float custDisc = 0;
    private Menu mMenu;
    private TextView actionbar_noti_tv, tv_address, tv_phone1,tv_phone2,tv_mobile1, tv_mobile2, tv_email, tv_lastSync;
    private DBHandler db;
    private Toast toast;
    private SliderLayout sliderLayout;
    private HashMap<String, String> scImgHashMap;
    private Constant constant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.optionstest);

        if(Constant.liveTestFlag==1) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }

        setContentView(R.layout.test);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        init();

       /* if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }*/

        card_give_order.setOnClickListener(this);
        card_account.setOnClickListener(this);
        card_track_order.setOnClickListener(this);
        card_profile.setOnClickListener(this);
        card_scheme.setOnClickListener(this);
        card_whats_new.setOnClickListener(this);
        card_feedback.setOnClickListener(this);
        card_bank_details.setOnClickListener(this);
        tv_phone1.setOnClickListener(this);
        tv_phone2.setOnClickListener(this);
        tv_mobile1.setOnClickListener(this);
        tv_mobile2.setOnClickListener(this);

        //TODO: JobScheduled
        Utitlity.scheduledJob(getApplicationContext());

        setContactUs();

        AddImagesUrlOnline();

        for(String name : scImgHashMap.keySet()){
            TextSliderView textSliderView = new TextSliderView(OptionsActivity.this);
            textSliderView
                    .description(name)
                    .image(scImgHashMap.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(this);

            textSliderView.bundle(new Bundle());
            textSliderView.getBundle().putString("extra",name);
            sliderLayout.addSlider(textSliderView);
        }
        sliderLayout.setPresetTransformer(SliderLayout.Transformer.DepthPage);
        sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        sliderLayout.setCustomAnimation(new DescriptionAnimation());
        sliderLayout.setDuration(3000);
        sliderLayout.addOnPageChangeListener(OptionsActivity.this);
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
            case R.id.card_give_order:
                showDia(3);
                /*startActivity(new Intent(getApplicationContext(), CutsizeSetwiseOrderActivity.class));
                overridePendingTransition(R.anim.enter, R.anim.exit);*/
                break;
            case R.id.card_account:
                startActivity(new Intent(getApplicationContext(), CustomerAccountActivity.class));
                overridePendingTransition(R.anim.enter, R.anim.exit);
                break;
            case R.id.card_track_order:
                startActivity(new Intent(getApplicationContext(), TrackOrderMasterActivity.class));
                overridePendingTransition(R.anim.enter, R.anim.exit);
                break;
            case R.id.card_profile:
                startActivity(new Intent(getApplicationContext(), ProfileViewActivity.class));
                overridePendingTransition(R.anim.enter, R.anim.exit);
                break;
            case R.id.card_scheme:
                ImagewiseAddToCartClass prod = new ImagewiseAddToCartClass();
                prod.setImageName("SchoolShoe,");
                Intent sintent = new Intent(getApplicationContext(), SchemeFullImageActivity.class);
                sintent.putExtra("data",prod);
                sintent.putExtra("pos","0");
                startActivity(sintent);
                overridePendingTransition(R.anim.enter, R.anim.exit);
                break;
            case R.id.card_whatsnew:
               /* toast.setText("Under Development");
                toast.show();*/
                Intent intent = new Intent(getApplicationContext(), MainImagewiseSetwiseOrderActivity.class);
                intent.putExtra("from","WhatsNew");
                startActivity(intent);
                overridePendingTransition(R.anim.enter, R.anim.exit);
                break;
            case R.id.card_feedback:
                startActivity(new Intent(getApplicationContext(), FeedbackActivity.class));
                overridePendingTransition(R.anim.enter, R.anim.exit);
                break;
            case R.id.card_bank_details:
                startActivity(new Intent(getApplicationContext(), OurBankDetailsActivity.class));
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

        //int count = new DBHandler(getApplicationContext()).getCartCount();
        int count = db.getCartCount();
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

    @Override
    public void onSliderClick(BaseSliderView slider) {
        //Toast.makeText(this,slider.getBundle().get("extra") + "", Toast.LENGTH_SHORT).show();
        ImagewiseAddToCartClass prod = new ImagewiseAddToCartClass();
        prod.setImageName("SchoolShoe,");
        Intent sintent = new Intent(getApplicationContext(), SchemeFullImageActivity.class);
        sintent.putExtra("data",prod);
        sintent.putExtra("pos","0");
        startActivity(sintent);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
       // Constant.showLog("Page Changed: " + position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private void init() {
        toast = Toast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        db = new DBHandler(this);
        FirstActivity.pref = getSharedPreferences(FirstActivity.PREF_NAME,MODE_PRIVATE);
        card_give_order = findViewById(R.id.card_give_order);
        card_account = findViewById(R.id.card_account);
        card_track_order = findViewById(R.id.card_track_order);
        card_profile = findViewById(R.id.card_profile);
        card_scheme = findViewById(R.id.card_scheme);
        card_whats_new = findViewById(R.id.card_whatsnew);
        card_feedback = findViewById(R.id.card_feedback);
        card_bank_details = findViewById(R.id.card_bank_details);
        tv_address = findViewById(R.id.tv_address);
        tv_phone1 = findViewById(R.id.tv_phone1);
        tv_phone2 = findViewById(R.id.tv_phone2);
        tv_mobile1 = findViewById(R.id.tv_mobile1);
        tv_mobile2 = findViewById(R.id.tv_mobile2);
        tv_email = findViewById(R.id.tv_email);
        tv_lastSync = findViewById(R.id.tv_lastSync);

        sliderLayout = findViewById(R.id.slider);
    }

    private void showDia(int a) {
        AlertDialog.Builder builder = new AlertDialog.Builder(OptionsActivity.this);
        builder.setCancelable(false);
        if (a == 0) {
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
            builder.setPositiveButton("Imagewise", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startActivity(new Intent(getApplicationContext(), ImagewiseSetwiseOrderActivity.class));
                    overridePendingTransition(R.anim.enter, R.anim.exit);
                    dialog.dismiss();
                }
            });
            builder.setNegativeButton("Cutsize", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    int custId = FirstActivity.pref.getInt(getString(R.string.pref_retailCustId), 0);
                    custDisc = new DBHandler(getApplicationContext()).getCustDiscount(custId);
                    startActivity(new Intent(getApplicationContext(), CutsizeSetwiseOrderActivity.class));
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
        } else if (a == 2) {
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
        } else if (a == 3) {
            builder.setTitle("Take Order");
            builder.setMessage("How do you want to take order?");
            builder.setPositiveButton("With Photos", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(getApplicationContext(), MainImagewiseSetwiseOrderActivity.class);
                    intent.putExtra("from", "Option");
                    startActivity(intent);
                    overridePendingTransition(R.anim.enter, R.anim.exit);
                    dialog.dismiss();
                }
            });
            builder.setNegativeButton("Without Photos", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startActivity(new Intent(getApplicationContext(), CutsizeSetwiseOrderActivity.class));
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
        } else if (a == 7) {
            builder.setMessage("Do You Want To Clear Saved Order?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    db.deleteTable(DBHandler.Table_CustomerOrder);
                    if (mMenu != null) {
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
        } else if (a == 8) {
            builder.setMessage("Please Try Again");
            builder.setPositiveButton("Try Again", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    new Constant();
                    getSaleExe();
                }
            });
        }
        builder.create().show();
    }

    public void AddImagesUrlOnline(){
        scImgHashMap = new HashMap<>();
        scImgHashMap.put("School Shoes", "http://103.109.13.200:24086/IMAGES/Scheme/SchoolShoes.jpg");
        /*scImgHashMap.put("Donut", "http://103.109.13.200:24086/IMAGES/2702FC_Foo%20Kids_Red_P1.jpg");
        scImgHashMap.put("Eclair", "http://103.109.13.200:24086/IMAGES/2901_Aaram_Black_P1.jpg");
        scImgHashMap.put("Froyo", "http://103.109.13.200:24086/IMAGES/F196C_Foo%20Kids_Blue_P1.jpg");
        scImgHashMap.put("GingerBread", "http://103.109.13.200:24086/IMAGES/2902_Aaram_Black_P1.jpg");*/
    }

    private void getSchemeData() {
        constant = new Constant(OptionsActivity.this);
        constant.showPD();
        try {
            final DBHandler db = new DBHandler(getApplicationContext());
            //CustId+"|"+HOCode+"|"+BranchId+"|"+ArealineId+"|"+AreaId+"|"+Cat
            String url = 1176 + "|" + 1 + "|" + 1 +"|"+ 1 +"|" + 1 + "|" + "School";
            writeLog("getSchemeData_"+url);
            final JSONObject jsonBody = new JSONObject();
            jsonBody.put("details", url);
            RequestBody body = RequestBody.create(okhttp3.MediaType.
                    parse("application/json; charset=utf-8"), (jsonBody).toString());
            Constant.showLog(jsonBody.toString());

            Call<List<SchemeMasterClass>> call = new RetrofitApiBuilder().getApiBuilder().
                    create(RetrofitApiInterface.class).
                    getSchemeData(body);
            call.enqueue(new Callback<List<SchemeMasterClass>>() {
                @Override
                public void onResponse(Call<List<SchemeMasterClass>> call, Response<List<SchemeMasterClass>> response) {
                    Constant.showLog("onResponse");
                    List<SchemeMasterClass> list = response.body();
                    if (list != null) {
                        if (list.size()!=0) {

                        }
                    } else {
                        Constant.showLog("onResponse_list_null");
                        writeLog("getSchemeData_onResponse_list_null");
                    }
                    constant.showPD();
                }

                @Override
                public void onFailure(Call<List<SchemeMasterClass>> call, Throwable t) {
                    Constant.showLog("onFailure");
                    if (!call.isCanceled()) {
                        call.cancel();
                    }
                    t.printStackTrace();
                    writeLog("getSchemeData_onFailure_" + t.getMessage());
                    constant.showPD();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            writeLog("getSchemeData_" + e.getMessage());
            constant.showPD();
            showDia(2);
        }
    }

    private void getSaleExe() {
        final Constant constant = new Constant(OptionsActivity.this);
        if (ConnectivityTest.getNetStat(getApplicationContext())) {
            int id = FirstActivity.pref.getInt(getString(R.string.pref_retailCustId), 0);
            String url = Constant.ipaddress + "/GetSalesExe?id=" + id + "&type=C&IMEINo1=0&IMEINo2=0";
            Constant.showLog(url);
            writeLog("getSaleExe_" + url);
            constant.showPD();
            VolleyRequests requests = new VolleyRequests(OptionsActivity.this);
            requests.getActiveStatus(url, new ServerCallback() {
                @Override
                public void onSuccess(String result) {
                    constant.showPD();
                    String arr[] = result.split("\\^");
                    if(arr.length>1){

                    }
                }

                @Override
                public void onFailure(String result) {
                    constant.showPD();
                    showDia(8);
                }
            });
        } else {
            showDia(3);
        }
    }

    private void setContactUs(){
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
        tv_lastSync.setText("Data Last Sync On - "+lastSync);
    }

    private void makeCall(String number){
        Intent phoneIntent = new Intent(Intent.ACTION_DIAL, Uri.fromParts(
                "tel", number, null));
        startActivity(phoneIntent);
        overridePendingTransition(R.anim.enter,R.anim.exit);
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

    private void writeLog(String _data) {
        new WriteLog().writeLog(getApplicationContext(), _data);
    }

}
