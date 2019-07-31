package com.lnbinfotech.msplfootwear;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
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
import com.lnbinfotech.msplfootwear.model.SchemeMasterClass;
import com.lnbinfotech.msplfootwear.utility.RetrofitApiBuilder;
import com.lnbinfotech.msplfootwear.volleyrequests.VolleyRequests;

import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.List;
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
    private TextView actionbar_noti_tv, tv_address, tv_phone1,tv_phone2,tv_mobile1,
            tv_mobile2, tv_email, tv_lastSync, tv_custname, tv_custaddress, tv_custmobile, tv_custemail;
    private DBHandler db;
    private Toast toast;
    private SliderLayout sliderLayout;
    private HashMap<String, String> scImgHashMap;
    private ImageView img_cust;
    private String imgName = "NA.jpg";

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
        tv_custmobile.setOnClickListener(this);
        img_cust.setOnClickListener(this);
        tv_email.setOnClickListener(this);

        //TODO: JobScheduled
        Utitlity.scheduledJob(getApplicationContext());

        setContactUs();

        getSchemeData();

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
                /*ImagewiseAddToCartClass prod = new ImagewiseAddToCartClass();
                prod.setImageName("SchoolShoes,VERTEXSCHEMEEIDSPECIALMAY2019");
                Intent sintent = new Intent(getApplicationContext(), SchemeFullImageActivity.class);
                sintent.putExtra("data",prod);
                sintent.putExtra("pos","0");
                startActivity(sintent);
                overridePendingTransition(R.anim.enter, R.anim.exit);*/
                showFullImage();
                break;
            case R.id.card_whatsnew:
               /* toast.setText("Under Development");
                toast.show();*/
                Intent intent = new Intent(getApplicationContext(), MainImagewiseSetwiseOrderActivity.class);
                intent.putExtra("from", "WhatsNew");
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
                if (!phone1.equals("") && !phone1.equals("0")) {
                    makeCall(phone1);
                }
                break;
            case R.id.tv_phone2:
                String phone2 = tv_phone2.getText().toString();
                if (!phone2.equals("") && !phone2.equals("0")) {
                    makeCall(phone2);
                }
                break;
            case R.id.tv_mobile1:
                String mob1 = tv_mobile1.getText().toString();
                if (!mob1.equals("") && !mob1.equals("0")) {
                    //makeCall(mob1);
                    sendWhatsapp("Hi", mob1);
                }
                break;
            case R.id.tv_mobile2:
                String mob2 = tv_mobile2.getText().toString();
                if (!mob2.equals("") && !mob2.equals("0")) {
                    makeCall(mob2);
                }
                break;
            case R.id.tv_custmobile:
                String mob3 = tv_custmobile.getText().toString();
                if (!mob3.equals("") && !mob3.equals("0")) {
                    showDia(10);
                }
                break;
            case R.id.img_cust:
                showPic();
                break;
            case R.id.tv_email:
                sendGmail(tv_email.getText().toString());
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
        showFullImage();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
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
        scImgHashMap = new HashMap<>();
        tv_custname = findViewById(R.id.tv_custname);
        tv_custaddress = findViewById(R.id.tv_custaddress);
        tv_custmobile = findViewById(R.id.tv_custmobile);
        tv_custemail = findViewById(R.id.tv_custemail);
        img_cust = findViewById(R.id.img_cust);
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
        }  else if (a == 9) {
            builder.setMessage("Your Sales Executive is Changed Today.");
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        } else if (a == 10) {
            builder.setMessage("What Do You Want To Do?");
            builder.setPositiveButton("Call", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    makeCall(tv_custmobile.getText().toString());
                }
            });
            builder.setNegativeButton("WhatsApp", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    sendWhatsapp("Hi",tv_custmobile.getText().toString());
                }
            });
            builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        }
        builder.create().show();
    }

    private void getSchemeData() {
        final Constant constant = new Constant(OptionsActivity.this);
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
                    scImgHashMap = new HashMap<>();
                    if (list != null) {
                        if (list.size()!=0) {
                            for(SchemeMasterClass mast : list){
                                scImgHashMap.put(mast.getCat(),Constant.imgUrl+"/Scheme/"+mast.getImgName());
                            }
                        } else {
                            sliderLayout.setVisibility(View.GONE);
                        }
                        setScheme();
                    } else {
                        sliderLayout.setVisibility(View.GONE);
                        Constant.showLog("onResponse_list_null");
                        writeLog("getSchemeData_onResponse_list_null");
                    }
                    constant.showPD();
                    getSaleExe();
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
                    getSaleExe();
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
            requests.getSalesExe(url, new ServerCallback() {
                @Override
                public void onSuccess(String result) {
                    constant.showPD();
                    String arr[] = result.split("\\^");
                    if(arr.length>1){
                        tv_custname.setText(arr[1]);
                        tv_custmobile.setText(arr[2]);
                        imgName = arr[3];

                        if(FirstActivity.pref.contains(getString(R.string.pref_execId))) {
                            int prevId = FirstActivity.pref.getInt(getString(R.string.pref_execId), 0);
                            if (prevId != Integer.parseInt(arr[0])) {
                                showDia(9);
                            }
                        }

                        SharedPreferences.Editor editor = FirstActivity.pref.edit();
                        editor.putInt(getString(R.string.pref_execId),Integer.parseInt(arr[0]));
                        editor.putString(getString(R.string.pref_execName),(arr[1]));
                        editor.apply();

                        Constant.showLog(Constant.custimgUrl+imgName);
                        Glide.with(getApplicationContext()).load(Constant.custimgUrl+imgName)
                                .thumbnail(0.5f)
                                .crossFade()
                                .placeholder(R.drawable.ic_male)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(img_cust);
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

    private void sendWhatsapp(String message, String mobNo){
        String url = "https://api.whatsapp.com/send?phone=+91" + mobNo;
        try {
            PackageManager pm = getPackageManager();
            pm.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES);
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        } catch (Exception e) {
            toast.setText("Whatsapp app not installed in your phone");
            toast.show();
            e.printStackTrace();
            writeLog("sendWhatsapp_"+e.getMessage());
        }
    }

    private void sendGmail(String mailId) {
        try {
            Intent eintent = new Intent(Intent.ACTION_SEND);
            eintent.putExtra(Intent.EXTRA_EMAIL, new String[]{mailId});
            //eintent.putExtra(Intent.EXTRA_SUBJECT, "Subject text here...");
            //eintent.putExtra(Intent.EXTRA_TEXT, "Body of the content here...");
            //eintent.putExtra(Intent.EXTRA_CC, "mailcc@gmail.com");
            eintent.setType("text/html");
            //eintent.setPackage("com.google.android.gm");
            startActivity(Intent.createChooser(eintent, "Send mail"));
        } catch (Exception e) {
            toast.setText("Gmail app not installed in your phone");
            toast.show();
            e.printStackTrace();
            writeLog("setGmail_" + e.getMessage());
        }
    }

    private void showPic() {
        AlertDialog.Builder builder = new AlertDialog.Builder(OptionsActivity.this);
        builder.setCancelable(true);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.pic_dialog, null);
        ImageView _img = view.findViewById(R.id.dia_img);
        Glide.with(getApplicationContext()).load(Constant.custimgUrl + imgName)
                .thumbnail(1f)
                .crossFade()
                .placeholder(R.drawable.ic_male)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .fitCenter()
                .into(_img);
        builder.setView(view);
        builder.create().show();
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

    private void showFullImage() {
        ImagewiseAddToCartClass prod = new ImagewiseAddToCartClass();
        String str =  "";
        for (Object value : scImgHashMap.values()) {
            str = str + value + ",";
        }
        if(str.length()>1){
            str = str.substring(0,str.length()-1);
        }
        prod.setImageName(str);
        Intent sintent = new Intent(getApplicationContext(), SchemeFullImageActivity.class);
        sintent.putExtra("data",prod);
        sintent.putExtra("pos","0");
        if(scImgHashMap.size()>0) {
            startActivity(sintent);
        } else {
            toast.setText("No Scheme Available");
            toast.show();
        }
    }

    private void setScheme(){
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

    private void writeLog(String _data) {
        new WriteLog().writeLog(getApplicationContext(), _data);
    }

}
