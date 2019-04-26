package com.pait.dispatch_app;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
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

import com.pait.dispatch_app.adapters.DispatchDetailAdapter;
import com.pait.dispatch_app.connectivity.ConnectivityTest;
import com.pait.dispatch_app.constant.Constant;
import com.pait.dispatch_app.db.DBHandler;
import com.pait.dispatch_app.interfaces.RetrofitApiInterface;
import com.pait.dispatch_app.interfaces.ServerCallback;
import com.pait.dispatch_app.interfaces.TestInterface;
import com.pait.dispatch_app.log.CopyLog;
import com.pait.dispatch_app.log.WriteLog;
import com.pait.dispatch_app.mail.GMailSender;
import com.pait.dispatch_app.model.DispatchDetailClass;
import com.pait.dispatch_app.model.DispatchMasterClass;
import com.pait.dispatch_app.model.EmployeeMasterClass;
import com.pait.dispatch_app.parse.UserClass;
import com.pait.dispatch_app.services.UploadImageService;
import com.pait.dispatch_app.utility.RetrofitApiBuilder;
import com.pait.dispatch_app.volleyrequests.VolleyRequests;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;

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
    private DispatchDetailAdapter adapter;
    private Toast toast;
    private List<DispatchDetailClass> list;
    private List<DispatchMasterClass> exportList;
    private int requestCode = 1, requestCode2 = 2, edCustCode = 3, edPOCode = 4, edDPBy = 5, hoCode,
            dpID, empId, custCode = 0, totQty = 0, flag = 0;
    private String imagePath = "NA", psImagePath = "", imgType, pono, exportFileName;
    private DBHandler db;
    private DispatchMasterClass dm;
    private EmployeeMasterClass em;
    private UserClass userClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Constant.liveTestFlag == 1) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }
        setContentView(R.layout.activity_main);

        init();

        userClass = (UserClass) getIntent().getExtras().get("cust");

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(userClass.getName());
        }

        /*empId = FirstActivity.pref.getInt(getString(R.string.pref_retailCustId), 0);
        hoCode = FirstActivity.pref.getInt(getString(R.string.pref_hocode), 0);
        dpID = FirstActivity.pref.getInt(getString(R.string.pref_dpId), 0);*/

        empId = userClass.getCustID();
        hoCode = userClass.getHOCode();
        dpID = userClass.getDpId();

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
                    tv_qty_Total.setText("0");
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
            case R.id.dm_save:
                validations();
                break;
            case R.id.refresh:
                showDia(5);
                break;
            case R.id.report_error:
                showDia(6);
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
                intent1.putExtra("from", "1");
                intent1.putExtra("hoCode", String.valueOf(hoCode));
                startActivityForResult(intent1, edCustCode);
                overridePendingTransition(R.anim.enter, R.anim.exit);
                break;
            case R.id.ed_poNo:
                if (ProductSearchActivity.partyName != null) {
                    Intent intent = new Intent(getApplicationContext(), ProductSearchActivity.class);
                    intent.putExtra("from", "2");
                    intent.putExtra("hoCode", String.valueOf(hoCode));
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
                    intent.putExtra("from", "3");
                    intent.putExtra("hoCode", String.valueOf(hoCode));
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
        imgType = data1;
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
                imagePath = custCode + "_" + imgType + "_" + pono + "_" + sdf.format(resultdate) + ".jpg";
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
                    imagePath = "NA";
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
                psImagePath = custCode + "_PS_" + pono + "_" + sdf.format(resultdate) + ".jpg";
                Constant.showLog(psImagePath);
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
                        Constant.folder_name + File.separator + Constant.image_folder, psImagePath);
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
            custCode = Integer.parseInt(dm.getCustId());
            ed_custName.setText(dm.getPartyName());
            ed_poNo.setText(null);
            tv_poQty.setText("0");
            tv_qty_Total.setText("0");
            tv_transporter.setText("");
            ed_dispatchBy.setText(null);
            list.clear();
            listView.setAdapter(null);
        } else if (this.edPOCode == requestCode && resultCode == RESULT_OK) {
            dm = (DispatchMasterClass) data.getSerializableExtra("result");
            ed_poNo.setText(dm.getPONO());
            String[] arr = dm.getPONO().split("\\/");
            pono = arr[0] + "_" + arr[2];
            tv_poQty.setText(dm.getTotalQty());
            tv_qty_Total.setText("0");
            tv_transporter.setText(dm.getTransporter());
            ed_dispatchBy.setText(dm.getEmp_Name());
            em = new EmployeeMasterClass();
            em.setEmp_Id(Integer.parseInt(dm.getEmp_Id()));
            em.setName(dm.getEmp_Name());
            list.clear();
            listView.setAdapter(null);
        } else if (this.edDPBy == requestCode && resultCode == RESULT_OK) {
            em = (EmployeeMasterClass) data.getSerializableExtra("result");
            ed_dispatchBy.setText(em.getName());
        }
    }

    private void takeImage(int requestCode) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File f = Constant.checkFolder(Constant.folder_name + File.separator + Constant.image_folder);
        f = new File(f.getAbsolutePath(), "temp.jpg");
        Uri photoURI = FileProvider.getUriForFile(getApplicationContext(), getApplicationContext().getPackageName()
                + ".provider", f);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityForResult(intent, requestCode);
        overridePendingTransition(R.anim.enter, R.anim.exit);
    }

    private void setData() {
        list.clear();
        listView.setAdapter(null);
        String str1 = ed_cartons.getText().toString();
        if (str1.equals("")) {
            str1 = "0";
        }
        int tot = Integer.parseInt(str1);
        for (int i = 1; i <= tot; i++) {
            DispatchDetailClass cheque = new DispatchDetailClass();
            cheque.setSrNo(i);
            cheque.setCBL("C");
            cheque.setNoOfCartons("0");
            cheque.setImgName("NA");
            list.add(cheque);
        }
        if (!str1.equals("0")) {
            DispatchDetailClass cheque = new DispatchDetailClass();
            cheque.setSrNo(++tot);
            cheque.setCBL("L");
            cheque.setNoOfCartons("0");
            cheque.setImgName("NA");
            list.add(cheque);
        }
        String str = ed_bundles.getText().toString();
        if (str.equals("")) {
            str = "0";
        }
        int tot1 = Integer.parseInt(str);
        if (tot1 != 0) {
            DispatchDetailClass cheque = new DispatchDetailClass();
            cheque.setSrNo(++tot);
            cheque.setCBL("B");
            cheque.setNoOfCartons(str);
            cheque.setImgName("NA");
            list.add(cheque);
        }
        if (!list.isEmpty()) {
            adapter = new DispatchDetailAdapter(getApplicationContext(), list);
            adapter.initInterface(MainActivity.this);
            listView.setAdapter(adapter);
        }
    }

    private void clearFields() {
        list.clear();
        listView.setAdapter(null);
        ed_custName.setText(null);
        ed_poNo.setText(null);
        tv_poQty.setText("0");
        tv_qty_Total.setText("0");
        tv_transporter.setText("");
        ed_dispatchBy.setText(null);
        ed_bundles.setText("0");
        ed_cartons.setText("0");
        ed_cartons.setSelected(true);
        img_slip.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_camera_alt_black_24dp));
        psImagePath = "";
        flag = 0;
        imagePath = "NA";
    }

    private void loadEmployeeMaster() {
        int max = db.getMaxEmpId();
        String url = Constant.ipaddress + "/GetEmployeeMaster?Id=" + max;
        Constant.showLog(url);
        writeLog("loadEmployeeMaster_" + url);
        final Constant constant = new Constant(MainActivity.this);
        constant.showPD();
        VolleyRequests requests = new VolleyRequests(MainActivity.this);
        requests.refreshEmployeeMaster(url, new ServerCallback() {
            @Override
            public void onSuccess(String result) {
                constant.showPD();
                flag = 0;
                getDispatchMaster(1);
            }

            @Override
            public void onFailure(String result) {
                constant.showPD();
                showDia(2);
            }
        }, 0);
    }

    private void getDispatchMaster(int type) {
        final Constant constant = new Constant(MainActivity.this);
        constant.showPD();
        try {
            int maxAuto = db.getMaxAuto();
            //Auto + "|"+ CustId + "|"+ HOCode + "|"+ dispatchId + "|"+ empId + "|"+ type
            String url = maxAuto + "|" + 0 + "|" + hoCode + "|" + dpID + "|" + empId + "|" + type;
            writeLog("getDispatchMaster_" + url);
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
                        if (flag == 0) {
                            db.addDispatchMaster(list);
                            Constant.showLog(list.size() + "_getDispatchMaster");
                        } else if (flag == 1) {
                            exportList = response.body();
                            exportToExcel();
                        }
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

    private void loadCompanyMaster() {
        int max = db.getMaxCompId();
        String url = Constant.ipaddress + "/GetCompanyMaster?Id=" + max;
        Constant.showLog(url);
        writeLog("loadCompanyMaster_" + url);
        final Constant constant = new Constant(MainActivity.this);
        constant.showPD();
        VolleyRequests requests = new VolleyRequests(MainActivity.this);
        requests.refreshCompanyMaster(url, new ServerCallback() {
            @Override
            public void onSuccess(String result) {
                constant.showPD();
                loadEmployeeMaster();
            }

            @Override
            public void onFailure(String result) {
                constant.showPD();
                showDia(2);
            }
        }, 0);
    }

    private void init() {
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
        FirstActivity.pref = getSharedPreferences(FirstActivity.PREF_NAME, MODE_PRIVATE);
    }

    private void showDia(int a) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setCancelable(false);
        if (a == 1) {
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
        } else if (a == 3) {
            builder.setMessage("Data Saved Successfully");
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    clearFields();
                    flag = 0;
                    Intent intent = new Intent(MainActivity.this, UploadImageService.class);
                    startService(intent);
                    writeLog("UploadImageService_onHandleIntent_broadcastSend");
                    getDispatchMaster(1);
                }
            });
        } else if (a == 4) {
            builder.setMessage("Error While Saving Order");
            builder.setPositiveButton("Try Again", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    new Constant();
                    dialog.dismiss();
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        } else if (a == 5) {
            builder.setMessage("Do You Want To Refresh Data?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    db.deleteTable(DBHandler.Table_Employee);
                    db.deleteTable(DBHandler.Table_CompanyMaster);
                    db.deleteTable(DBHandler.Table_DispatchMaster);
                    flag = 0;
                    loadCompanyMaster();
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
                        exportFile();
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
            builder.setMessage("Do You Want To Save Data?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    getData();
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        } else if (a == 8) {
            builder.setMessage("Do You Want To Send Report?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    flag = 1;
                    getDispatchMaster(2);
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

    private void validations() {
        int qty1 = Integer.parseInt(tv_poQty.getText().toString());
        int qty2 = Integer.parseInt(tv_qty_Total.getText().toString());
        if (ed_custName.getText().toString().equals("")) {
            toast.setText("Please Select Party Name");
            toast.show();
        } else if (ed_poNo.getText().toString().equals("")) {
            toast.setText("Please Select PO Number");
            toast.show();
        } else if (psImagePath.equals("")) {
            toast.setText("Please Capture Packing Slip Image");
            toast.show();
        } else if(qty1!=qty2){
            toast.setText("Total Quantity Mismatch");
            toast.show();
        } else {
            showDia(7);
        }
    }

    private void getData() {
        int _flag = 0;
        String str1 = ed_cartons.getText().toString();
        if (str1.equals(""))
            str1 = "0";

        String str = ed_bundles.getText().toString();
        if (str.equals(""))
            str = "0";

        String notOfCartoon = "", imageNames = "";
        for (DispatchDetailClass cq : list) {
            Constant.showLog(cq.getSrNo() + "\n" +
                    cq.getCBL() + "\n" +
                    cq.getNoOfCartons() + "\n" +
                    cq.getImgName());

            if (cq.getCBL().equals("L") || cq.getCBL().equals("B")) {
                if (!cq.getNoOfCartons().equals("0") && !cq.getNoOfCartons().equals("")) {
                    notOfCartoon = notOfCartoon + cq.getNoOfCartons() + ".";
                    imageNames = imageNames + cq.getImgName() + ",";
                    if (cq.getImgName().equals("NA")) {
                        _flag = 1;
                        break;
                    }
                }
            } else {
                notOfCartoon = notOfCartoon + cq.getNoOfCartons() + ".";
                imageNames = imageNames + cq.getImgName() + ",";
                if (cq.getImgName().equals("NA")) {
                    _flag = 1;
                    break;
                }
            }

            /*if (cq.getCBL().equals("L") || cq.getCBL().equals("B")) {
                if (!cq.getNoOfCartons().equals("0") && !cq.getNoOfCartons().equals("")) {
                    if (cq.getImgName().equals("NA")) {
                        _flag = 1;
                        break;
                    }
                }
            } else if (cq.getImgName().equals("NA")) {
                _flag = 1;
                break;
            }*/
        }
        imageNames = imageNames + psImagePath;
        Constant.showLog(dm.getPartyName() + "\n" +
                dm.getPONO() + "\n" +
                dm.getDcNo() + "\n" +
                dm.getDCdate() + "\n" +
                dm.getTotalQty() + "\n" +
                dm.getTransporter());

        Constant.showLog(em.getEmp_Id() + "\n" + em.getName());
        notOfCartoon = notOfCartoon.substring(0, notOfCartoon.length() - 1);
        Constant.showLog(notOfCartoon);
        Constant.showLog(imageNames);

        //DCNO,PONO,DispatchBy,NoOfCartoon,DispatchPerson,CheckedPerson,Carton,Bundle,ImagePath,

        String data = dm.getDcNo() + "|" + dm.getPONO() + "|" + dm.getEmp_Id() + "|" + notOfCartoon + "|" +
                empId + "|" + empId + "|" + str1 + "|" + str + "|" + imageNames;

        Constant.showLog(data);

        if (_flag == 0) {
            new saveDispatchMaster(dm.getPONO()).execute(data);
        } else {
            toast.setText("Please Capture All Images");
            toast.show();
        }
    }

    private class saveDispatchMaster extends AsyncTask<String, Void, String> {
        private String pono = "";
        private ProgressDialog pd;

        private saveDispatchMaster(String _pono) {
            this.pono = _pono;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(MainActivity.this);
            pd.setMessage("Please Wait...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... url) {
            String value = "";
            DefaultHttpClient httpClient = null;
            HttpPost request = new HttpPost(Constant.ipaddress + "/saveDispatchData");
            request.setHeader("Accept", "application/json");
            request.setHeader("Content-type", "application/json");
            try {
                JSONStringer vehicle = new JSONStringer().object().key("rData").object().key("details").value(url[0]).endObject().endObject();
                StringEntity entity = new StringEntity(vehicle.toString());
                Constant.showLog(vehicle.toString());
                writeLog("saveDispatchMaster_" + vehicle.toString());
                request.setEntity(entity);
                HttpParams httpParams = new BasicHttpParams();
                HttpConnectionParams.setConnectionTimeout(httpParams, Constant.TIMEOUT_CON);
                HttpConnectionParams.setSoTimeout(httpParams, Constant.TIMEOUT_SO);
                httpClient = new DefaultHttpClient(httpParams);
                //DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpResponse response = httpClient.execute(request);
                Constant.showLog("Saving : " + response.getStatusLine().getStatusCode());
                value = new BasicResponseHandler().handleResponse(response);
                //return Post.POST(url[0]);
            } catch (Exception e) {
                e.printStackTrace();
                writeLog("saveDispatchMaster_result_" + e.getMessage());
            } finally {
                try {
                    if (httpClient != null) {
                        httpClient.getConnectionManager().shutdown();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    writeLog("saveDispatchMaster_finally_" + e.getMessage());
                }
            }
            return value;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                Constant.showLog(result);
                //String str = new JSONObject(result).getString("SaveCustOrderMasterResult");
                String str = new JSONObject(result).getString("SaveDispatchDataResult");
                str = str.replace("\"", "");
                Constant.showLog(str);
                pd.dismiss();
                writeLog("saveDispatchMaster_result_" + str + "_" + result);
                String[] retAutoBranchId = str.split("\\-");
                if (retAutoBranchId.length > 1) {
                    if (!retAutoBranchId[0].equals("0") && !retAutoBranchId[0].equals("+2") && !retAutoBranchId[0].equals("+3")) {
                        if (retAutoBranchId[1].equals(String.valueOf(pono))) {
                            db.deleteOrderTableAfterSave(pono);
                            showDia(3);
                        } else {
                            showDia(4);
                        }
                    } else {
                        showDia(4);
                    }
                } else {
                    showDia(4);
                }
                //counter++;
                //saveCustOrder();
            } catch (Exception e) {
                writeLog("saveDispatchMaster_" + e.getMessage());
                e.printStackTrace();
                showDia(4);
                pd.dismiss();
            }
        }
    }

    private void exportFile() {
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
            pd = new ProgressDialog(MainActivity.this);
            pd.setCancelable(false);
            pd.setMessage("Please Wait...");
            pd.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                String res = respMailId;
                String mob = FirstActivity.pref.getString(getString(R.string.pref_mobno), "0");
                String subject = Constant.mail_subject + "_" + mob;
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

    private void exportToExcel() {
        long datetime = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("dd_MMM_yyyy_hh_mm_ss", Locale.ENGLISH);
        Date resultdate = new Date(datetime);
        String cur_date = sdf.format(resultdate);
        exportFileName = "Dispatch_Allotment_Report_" + cur_date + ".xls";
        writeLog("exportToExcel_" + exportFileName);
        new writeFile().execute(exportFileName);
    }

    private class writeFile extends AsyncTask<String, Void, String> {

        private ProgressDialog pd1;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd1 = new ProgressDialog(MainActivity.this);
            pd1.setMessage("Exporting Data Please Wait...");
            pd1.setCancelable(false);
            pd1.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String status = "";
            try {
                File directory = Constant.checkFolder(Constant.folder_name + "/" + Constant.gstFolderName);
                if (!directory.exists()) {
                    if (directory.mkdirs()) {
                        Constant.showLog("TAG");
                    }
                }
                File outputFile = new File(directory, strings[0]);
                Workbook wb = new HSSFWorkbook();
                CellStyle cs = wb.createCellStyle();
                Font font = wb.createFont();
                font.setBoldweight(Font.BOLDWEIGHT_BOLD);
                cs.setFont(font);
                writeLog("writeFile_exportReport_called");
                exportDispatchReport(wb, cs);
                FileOutputStream os = new FileOutputStream(outputFile);
                wb.write(os);
                os.close();
                writeLog("writeFile_Success");
            } catch (Exception e) {
                status = null;
                e.printStackTrace();
                writeLog("writeFile_" + e.getMessage());
            }
            return status;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd1.dismiss();
            if (result != null) {
                shareFile();
                flag = 0;
                Constant.showLog("Report Exported Successfully");
                toast.setText("Report Exported Successfully");
                toast.show();
            } else {
                writeLog("Error_While_Exporting_Data");
                toast.setText("Error While Exporting Data. Please Try Again");
                toast.show();
            }
        }
    }

    private void shareFile() {
        try {
            Intent intentShareFile = new Intent(Intent.ACTION_SEND);
            String myFilePath = Constant.checkFolder(Constant.folder_name).getAbsolutePath() + File.separator +
                    Constant.gstFolderName + File.separator + exportFileName;
            Constant.showLog("file path:" + myFilePath);

            File f = new File(myFilePath);
            Uri photoURI = FileProvider.getUriForFile(getApplicationContext(), getApplicationContext().getPackageName()
                    + ".provider", f);
            intentShareFile.setType("application/vnd.ms-excel");
            intentShareFile.putExtra(Intent.EXTRA_STREAM, photoURI);
            //intentShareFile.putExtra(Intent.EXTRA_SUBJECT, partyName + " - " + fromdate + " - " + todate + " GST Report");
            intentShareFile.putExtra(Intent.EXTRA_SUBJECT, " Order Dispatch Allotment Report");
            startActivity(Intent.createChooser(intentShareFile, "Share File"));
        } catch (Exception e) {
            e.printStackTrace();
            writeLog("shareFile_" + e.getMessage());
            toast.setText("Something Went Wrong");
            toast.show();
        }
    }

    private void exportDispatchReport(Workbook wb, CellStyle cs) {

        Constant.showLog("exportGSTReport");
        Sheet sheet = wb.createSheet("Order Dispatch Allotment Report");

        String arr[] = new String[]{"PartyName", "Order No", "Dispatch Date", "Order Qty", "Transporter", "Dispatch By",};

        String compName = "";
        int hocode = FirstActivity.pref.getInt(getString(R.string.pref_hocode), 0);
        if (hocode == 1) {
            compName = "MADHUR SALES PRIVATE LIMITED - PUNE DIVISION";
        } else if (hocode == 12) {
            compName = "MADHUR SALES PRIVATE LIMITED - KARAD DIVISION";
        } else if (hocode == 13) {
            compName = "MADHUR SALES PRIVATE LIMITED - AHMEDNAGAR DIVISION";
        }

        CellStyle style = wb.createCellStyle();
        Font font = wb.createFont();
        font.setColor(HSSFColor.BLUE.index);
        style.setFont(font);

        Row rowHeader = sheet.createRow(0);
        Cell cellHeader = rowHeader.createCell(1);
        cellHeader.setCellValue(compName);
        cellHeader.setCellStyle(style);

        /*sheet.addMergedRegion(new CellRangeAddress(
                0, //first row (0-based)
                0, //last row  (0-based)
                1, //first column (0-based)
                6  //last column  (0-based)
        ));

        cellHeader = rowHeader.createCell(8);
        cellHeader.setCellValue("GSTNO");
        cellHeader.setCellStyle(style);

        sheet.addMergedRegion(new CellRangeAddress(
                0, //first row (0-based)
                0, //last row  (0-based)
                8, //first column (0-based)
                8  //last column  (0-based)
        ));

        cellHeader = rowHeader.createCell(9);
        cellHeader.setCellValue("27AAJCM4570B1ZK");
        cellHeader.setCellStyle(style);*/

        sheet.addMergedRegion(new CellRangeAddress(
                0, //first row (0-based)
                0, //last row  (0-based)
                9, //first column (0-based)
                10  //last column  (0-based)
        ));

        Row row = sheet.createRow(1);
        for (int i = 0; i < arr.length; i++) {
            Cell c = row.createCell(i);
            c.setCellValue(arr[i]);
            c.setCellStyle(cs);
        }

        int count = 1;

        for (int i = 0; i <= exportList.size(); i++) {
            count++;
            Row row1 = sheet.createRow(count);
            if (i < exportList.size()) {
                DispatchMasterClass gstDet = exportList.get(i);
                DataFormat format = wb.createDataFormat();
                for (int j = 0; j < arr.length; j++) {
                    Cell cell = row1.createCell(j);
                    if (j == 0) {
                        cell.setCellValue(gstDet.getPartyName());
                    } else if (j == 1) {
                        cell.setCellValue(gstDet.getPONO());
                    } else if (j == 2) {
                        cell.setCellValue(gstDet.getDispatchDate());
                    } else if (j == 3) {
                        cell.setCellValue(Double.parseDouble(gstDet.getTotalQty()));
                        totQty = totQty + Integer.parseInt(gstDet.getTotalQty());
                    } else if (j == 4) {
                        cell.setCellValue(gstDet.getTransporter());
                    } else {
                        cell.setCellValue(gstDet.getEmp_Name());
                    }
                }
            } else {
                for (int j = 0; j < arr.length; j++) {
                    Cell cell = row1.createCell(j);
                    if (j == 3) {
                        cell.setCellType(Cell.CELL_TYPE_NUMERIC);
                        cell.setCellValue(Double.parseDouble(String.valueOf(totQty)));
                    }
                }
            }
        }
        Constant.showLog("" + count);
        writeLog("writeFile_exportGSTReport_called");
    }

    private void writeLog(String _data) {
        new WriteLog().writeLog(getApplicationContext(), "MainActivity_" + _data);
    }
}
