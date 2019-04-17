package com.pait.dispatch_app;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.pait.dispatch_app.adapters.ChequeDetailChangedAdapter;
import com.pait.dispatch_app.constant.Constant;
import com.pait.dispatch_app.db.DBHandler;
import com.pait.dispatch_app.interfaces.RetrofitApiInterface;
import com.pait.dispatch_app.interfaces.ServerCallback;
import com.pait.dispatch_app.interfaces.TestInterface;
import com.pait.dispatch_app.log.WriteLog;
import com.pait.dispatch_app.model.ChequeDetailsClass;
import com.pait.dispatch_app.model.DispatchMasterClass;
import com.pait.dispatch_app.utility.RetrofitApiBuilder;
import com.pait.dispatch_app.volleyrequests.VolleyRequests;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,TestInterface {

    private EditText ed_custName, ed_poNo, ed_dispatchBy, ed_cartons, ed_bundles;
    private TextView tv_poQty, tv_qty_Total, tv_transporter;
    private Button btn_submit;
    private NonScrollListView listView;
    private ImageView img_slip;
    private ChequeDetailChangedAdapter adapter;
    private Toast toast;
    private List<ChequeDetailsClass> list;
    private int requestCode = 1, requestCode2 = 2, edCustCode = 3, edPOCode = 4, edDPBy = 5, hoCode, dpID, empId;
    private String imagePath = "";
    private DBHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(Constant.liveTestFlag==1) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }
        setContentView(R.layout.activity_main);

        init();


        empId = FirstActivity.pref.getInt(getString(R.string.pref_retailCustId),0);
        hoCode = FirstActivity.pref.getInt(getString(R.string.pref_hocode),0);
        dpID = FirstActivity.pref.getInt(getString(R.string.pref_dpId),0);

        ed_custName.setOnClickListener(this);
        ed_poNo.setOnClickListener(this);
        ed_dispatchBy.setOnClickListener(this);
        btn_submit.setOnClickListener(this);
        img_slip.setOnClickListener(this);

        ed_cartons.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String str = ed_cartons.getText().toString();
                if (!str.equals("")) {
                    setData();
                } else {
                    list.clear();
                    listView.setAdapter(null);
                }
            }
        });

        ed_bundles.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String str = ed_bundles.getText().toString();
                if (!str.equals("")) {
                    setData();
                } else {
                    list.clear();
                    listView.setAdapter(null);
                }
            }
        });

        loadEmployeeMaster();
    }

    @Override
    public void onBackPressed() {
        showDia(1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.chequedetail_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.chq_save:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_slip:
                takeImage(requestCode2);
                break;
            case R.id.ed_custName:
                Intent intent1 = new Intent(getApplicationContext(), ProductSearchActivity.class);
                intent1.putExtra("from","1");
                intent1.putExtra("hoCode",String.valueOf(hoCode));
                startActivityForResult(intent1, edCustCode);
                overridePendingTransition(R.anim.enter, R.anim.exit);
                break;
            case R.id.ed_poNo:
                if (ProductSearchActivity.partyName != null) {
                    Intent intent = new Intent(getApplicationContext(), ProductSearchActivity.class);
                    intent.putExtra("from","2");
                    intent.putExtra("hoCode",String.valueOf(hoCode));
                    startActivityForResult(intent, edPOCode);
                    overridePendingTransition(R.anim.enter, R.anim.exit);
                } else {
                    toast.setText("First Select PartyName");
                    toast.show();
                }
                break;
            case R.id.ed_dispatchBy:
                if (ProductSearchActivity.partyName != null) {
                    Intent intent = new Intent(getApplicationContext(), ProductSearchActivity.class);
                    intent.putExtra("from","3");
                    intent.putExtra("hoCode",String.valueOf(hoCode));
                    startActivityForResult(intent, edDPBy);
                    overridePendingTransition(R.anim.enter, R.anim.exit);
                } else {
                    toast.setText("First Select PartyName");
                    toast.show();
                }
                break;
        }
    }

    @Override
    public void onResumeFragment(String data1, String data2, Context context) {
    }

    @Override
    public void onPauseFragment(String data1, String data2, Context context) {
        takeImage(requestCode);
    }

    @Override
    public void onAmountChange(int amnt) {
        tv_qty_Total.setText(String.valueOf(amnt));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (this.requestCode == requestCode && resultCode == RESULT_OK) {
            try {
                long datetime = System.currentTimeMillis();
                SimpleDateFormat sdf = new SimpleDateFormat("dd_MMM_yyyy_HH_mm_ss", Locale.ENGLISH);
                Date resultdate = new Date(datetime);
                //imagePath = "C_" + custId + "_Cheque_" + chequeNo + "_" + sdf.format(resultdate) + ".jpg";
                imagePath = "C_" + sdf.format(resultdate) + ".jpg";
                Constant.showLog(imagePath);
                File f = new File(Environment.getExternalStorageDirectory() + File.separator +
                        Constant.folder_name + File.separator + Constant.image_folder);
                for (File temp : f.listFiles()) {
                    if (temp.getName().equals("temp.jpg")) {
                        f = temp;
                        break;
                    }
                }
                OutputStream outFile;
                Bitmap bitmap;
                BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(), bitmapOptions);

                File file = new File(Environment.getExternalStorageDirectory() + File.separator +
                        Constant.folder_name + File.separator + Constant.image_folder, imagePath);
                try {
                    outFile = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 15, outFile);
                    outFile.flush();
                    outFile.close();
                    adapter.returnImage(imagePath);
                } catch (Exception e) {
                    writeLog("onActivityResult():FileNotFoundException:" + e);
                    e.printStackTrace();
                }
            } catch (Exception e) {
                writeLog("onActivityResult():Exception:" + e);
                e.printStackTrace();
            }
        } else if (this.requestCode2 == requestCode && resultCode == RESULT_OK) {
            try {
                long datetime = System.currentTimeMillis();
                SimpleDateFormat sdf = new SimpleDateFormat("dd_MMM_yyyy_HH_mm_ss", Locale.ENGLISH);
                Date resultdate = new Date(datetime);
                //imagePath = "C_" + custId + "_Cheque_" + chequeNo + "_" + sdf.format(resultdate) + ".jpg";
                imagePath = "C_" + sdf.format(resultdate) + ".jpg";
                Constant.showLog(imagePath);
                File f = new File(Environment.getExternalStorageDirectory() + File.separator +
                        Constant.folder_name + File.separator + Constant.image_folder);
                for (File temp : f.listFiles()) {
                    if (temp.getName().equals("temp.jpg")) {
                        f = temp;
                        break;
                    }
                }
                OutputStream outFile;
                Bitmap bitmap;
                BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(), bitmapOptions);

                File file = new File(Environment.getExternalStorageDirectory() + File.separator +
                        Constant.folder_name + File.separator + Constant.image_folder, imagePath);
                try {
                    outFile = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 15, outFile);
                    outFile.flush();
                    outFile.close();
                    img_slip.setImageBitmap(bitmap);
                } catch (Exception e) {
                    writeLog("onActivityResult():FileNotFoundException:" + e);
                    e.printStackTrace();
                }
            } catch (Exception e) {
                writeLog("onActivityResult():Exception:" + e);
                e.printStackTrace();
            }
        } else if (this.edCustCode == requestCode && resultCode == RESULT_OK) {
            DispatchMasterClass dm = (DispatchMasterClass) data.getSerializableExtra("result");
            ed_custName.setText(dm.getPartyName());
            ed_poNo.setText(null);
            tv_poQty.setText("0");
            tv_transporter.setText("");
            ed_dispatchBy.setText(null);
        } else if (this.edPOCode == requestCode && resultCode == RESULT_OK) {
            DispatchMasterClass dm = (DispatchMasterClass) data.getSerializableExtra("result");
            ed_poNo.setText(dm.getPONO());
            tv_poQty.setText(dm.getTotalQty());
            tv_transporter.setText(dm.getTransporter());
            ed_dispatchBy.setText(dm.getEmp_Name());
        } else if (this.edDPBy == requestCode && resultCode == RESULT_OK) {
            DispatchMasterClass dm = (DispatchMasterClass) data.getSerializableExtra("result");
            ed_dispatchBy.setText(dm.getEmp_Name());
        }
    }

    private void takeImage(int requestCode){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File f = Constant.checkFolder(Constant.folder_name + File.separator + Constant.image_folder);
        f = new File(f.getAbsolutePath(),"temp.jpg");
        Uri photoURI = FileProvider.getUriForFile(getApplicationContext(), getApplicationContext().getPackageName()
                + ".provider", f);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityForResult(intent,requestCode);
        overridePendingTransition(R.anim.enter, R.anim.exit);
    }

    private void setData(){
        list.clear();
        listView.setAdapter(null);
        int tot = Integer.parseInt(ed_cartons.getText().toString());
        String str = "1";
        int chqNo = Integer.parseInt(str);
        for(int i=1;i<=tot;i++){
            ChequeDetailsClass cheque = new ChequeDetailsClass();
            cheque.setSrNo(i);
            cheque.setCBL("C");
            cheque.setChq_det_amt("0");
            cheque.setChq_det_number(String.valueOf(chqNo));
            chqNo = chqNo + 1;
            cheque.setChq_det_date(new Constant(getApplicationContext()).getDate());
            list.add(cheque);
        }
        ChequeDetailsClass cheque = new ChequeDetailsClass();
        cheque.setSrNo(tot+1);
        cheque.setCBL("L");
        cheque.setChq_det_amt("0");
        cheque.setChq_det_number(String.valueOf(chqNo));
        chqNo = chqNo + 1;
        cheque.setChq_det_date(new Constant(getApplicationContext()).getDate());
        list.add(cheque);
        int tot1 = Integer.parseInt(ed_bundles.getText().toString());
        if(tot1!=0){
            cheque = new ChequeDetailsClass();
            cheque.setSrNo(tot+2);
            cheque.setCBL("B");
            cheque.setChq_det_amt("0");
            cheque.setChq_det_number(String.valueOf(chqNo));
            chqNo = chqNo + 1;
            cheque.setChq_det_date(new Constant(getApplicationContext()).getDate());
            list.add(cheque);
        }
        adapter = new ChequeDetailChangedAdapter(getApplicationContext(), list);
        adapter.initInterface(MainActivity.this);
        listView.setAdapter(adapter);
    }

    private void loadEmployeeMaster() {
        int max = db.getMaxEmpId();
        String url = Constant.ipaddress + "/GetEmployeeMaster?Id="+max;
        Constant.showLog(url);
        writeLog("loadEmployeeMaster_" + url);
        final Constant constant = new Constant(MainActivity.this);
        constant.showPD();
        VolleyRequests requests = new VolleyRequests(MainActivity.this);
        requests.refreshEmployeeMaster(url, new ServerCallback() {
            @Override
            public void onSuccess(String result) {
                constant.showPD();
                getDispatchMaster();
            }

            @Override
            public void onFailure(String result) {
                constant.showPD();
                showDia(2);
            }
        },0);
    }

    private void getDispatchMaster() {
        final Constant constant = new Constant(MainActivity.this);
        constant.showPD();
        try {
            int maxAuto = db.getMaxAuto();
            //Auto +"|"+ CustId +"|"+ HOCode +"|"+ discpatchId +"|"+ empId
            String url = maxAuto + "|" + 0 + "|" + hoCode + "|" + dpID + "|" + empId;
            writeLog("getDispatchMaster_"+url);
            final JSONObject jsonBody = new JSONObject();
            jsonBody.put("details", url);
            RequestBody body = RequestBody.create(okhttp3.MediaType.
                    parse("application/json; charset=utf-8"), (jsonBody).toString());
            Constant.showLog(jsonBody.toString());

            Call<List<DispatchMasterClass>> call = new RetrofitApiBuilder().getApiBuilder().
                    create(RetrofitApiInterface.class).
                    getDispatchMaster(body);
            call.enqueue(new Callback<List<DispatchMasterClass>>() {
                @Override
                public void onResponse(Call<List<DispatchMasterClass>> call, Response<List<DispatchMasterClass>> response) {
                    Constant.showLog("onResponse");
                    List<DispatchMasterClass> list = response.body();
                    if (list != null) {
                        db.addDispatchMaster(list);
                        Constant.showLog(list.size() + "_getDispatchMaster");
                    } else {
                        Constant.showLog("onResponse_list_null");
                        writeLog("getDispatchMaster_onResponse_list_null");
                    }
                    constant.showPD();
                }

                @Override
                public void onFailure(Call<List<DispatchMasterClass>> call, Throwable t) {
                    Constant.showLog("onFailure");
                    if (!call.isCanceled()) {
                        call.cancel();
                    }
                    t.printStackTrace();
                    writeLog("getDispatchMaster_onFailure_" + t.getMessage());
                    constant.showPD();
                    showDia(2);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            writeLog("getDispatchMaster_" + e.getMessage());
            constant.showPD();
            showDia(2);
        }
    }

    private void init(){
        toast = Toast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        ed_custName = findViewById(R.id.ed_custName);
        ed_poNo = findViewById(R.id.ed_poNo);
        ed_dispatchBy = findViewById(R.id.ed_dispatchBy);
        ed_cartons = findViewById(R.id.ed_cartons);
        ed_bundles = findViewById(R.id.ed_bundles);
        tv_poQty = findViewById(R.id.tv_poQty);
        tv_qty_Total = findViewById(R.id.tv_qtyTotal);
        tv_transporter = findViewById(R.id.tv_transporter);
        btn_submit = findViewById(R.id.btn_submit);
        listView = findViewById(R.id.listView);
        img_slip = findViewById(R.id.img_slip);
        list = new ArrayList<>();
        db = new DBHandler(getApplicationContext());
        FirstActivity.pref = getSharedPreferences(FirstActivity.PREF_NAME,MODE_PRIVATE);
    }

    private void showDia(int a) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setCancelable(false);
        if(a==1) {
            builder.setMessage("Do You Want To Go Back ?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    new Constant(MainActivity.this).doFinish();
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
        } else if (a == 2) {
            builder.setMessage("Error While Loading Data");
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    new Constant();
                    dialog.dismiss();
                }
            });
        }
        builder.create().show();
    }

    private void validations() {
        if (ed_custName.getText().toString().equals("")) {
            toast.setText("Please Select Bank Name");
            toast.show();
        } else if (ed_poNo.getText().toString().equals("")) {
            toast.setText("Please Select Branch Name");
            toast.show();
        } else {
            get_data();
        }
    }

    private void get_data(){

    }

    private void writeLog(String _data) {
        new WriteLog().writeLog(getApplicationContext(), "MainActivity_" + _data);
    }
}
