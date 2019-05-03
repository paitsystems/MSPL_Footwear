package com.pait.dispatch_app;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.pait.dispatch_app.adapters.DispatchDetailAdapter;
import com.pait.dispatch_app.constant.Constant;
import com.pait.dispatch_app.db.DBHandler;
import com.pait.dispatch_app.interfaces.RetrofitApiInterface;
import com.pait.dispatch_app.interfaces.TestInterface;
import com.pait.dispatch_app.log.WriteLog;
import com.pait.dispatch_app.model.DispatchDetailClass;
import com.pait.dispatch_app.model.DispatchMasterClass;
import com.pait.dispatch_app.model.EmployeeMasterClass;
import com.pait.dispatch_app.parse.UserClass;
import com.pait.dispatch_app.services.UploadImageService;
import com.pait.dispatch_app.utility.RetrofitApiBuilder;

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

public class MainActivity_KRD extends AppCompatActivity implements View.OnClickListener, TestInterface {

    private EditText ed_custName, ed_poNo, ed_dispatchBy, ed_cartons, ed_bundles, ed_total;
    private TextView tv_poQty, tv_qty_Total, tv_transporter;
    private Button btn_submit;
    private NonScrollListView listView;
    private ImageView img_slip;
    private DispatchDetailAdapter adapter;
    private Toast toast;
    private List<DispatchDetailClass> list;
    private int requestCode = 1, requestCode2 = 2, edCustCode = 3, edPOCode = 4, edDPBy = 5, hoCode,
            dpID, empId, custCode = 0, flag = 0;
    private String imagePath = "NA", psImagePath = "", imgType, pono = "", empName = "NA", userType;
    private DBHandler db;
    private DispatchMasterClass dm;
    private EmployeeMasterClass em;
    private UserClass userClass;
    private LinearLayout lay_car, lay_header;
    private TextView tv_car;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Constant.liveTestFlag == 1) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }
        setContentView(R.layout.activity_main__krd);

        init();

        userClass = (UserClass) getIntent().getExtras().get("cust");
        userType = getIntent().getExtras().getString("type");

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(userClass.getName());
        }

        if (userType.equals("1")) {
            lay_car.setVisibility(View.GONE);
            lay_header.setVisibility(View.GONE);
            tv_car.setVisibility(View.GONE);
            listView.setVisibility(View.GONE);
        } else {
            ed_total.setFocusable(false);
            ed_total.setClickable(false);
            ed_total.setEnabled(false);
            ed_dispatchBy.setFocusable(false);
            ed_dispatchBy.setClickable(false);
            ed_dispatchBy.setEnabled(false);
            //img_slip.setFocusable(false);
            //img_slip.setClickable(false);
            //img_slip.setEnabled(false);
        }

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
                    ed_total.setText("0");
                    list.clear();
                    listView.setAdapter(null);
                }
            }
        });

        if (userType.equals("1")) {
            getDispatchMaster(1);
        } else {
            getDispatchMaster(3);
        }
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
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_slip:
                if (userType.equals("1")) {
                    takeImage(requestCode2);
                } else {
                    showPic();
                }
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
        ed_total.setText(String.valueOf(amnt));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (this.requestCode == requestCode && resultCode == RESULT_OK) {
            try {
                long datetime = System.currentTimeMillis();
                SimpleDateFormat sdf = new SimpleDateFormat("dd_MMM_yyyy_HH_mm_ss", Locale.ENGLISH);
                Date resultdate = new Date(datetime);
                imagePath = pono + "_" + imgType + "_" + custCode + "_" + empName + "_" + sdf.format(resultdate) + ".jpg";
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
                    writeLog("onActivityResult():FileNotFoundException:_" + e);
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
                psImagePath = pono + "_PS_" + custCode + "_" + empName + "_" + sdf.format(resultdate) + ".jpg";
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
            pono = arr[2] + "_" + arr[0];
            if (userType.equals("2")) {
                tv_poQty.setText(dm.getDPTotal());
                ed_total.setText(dm.getDPTotal());
                psImagePath = dm.getPSImage();
                loadImage();
            } else {
                tv_poQty.setText(dm.getTotalQty());
            }
            tv_transporter.setText(dm.getTransporter());
            ed_dispatchBy.setText(dm.getEmp_Name());
            String str = dm.getEmp_Name();
            Constant.showLog(str);
            try {
                String arr1[] = str.split("\\s+");
                if (arr1.length > 1) {
                    empName = arr1[0];
                } else {
                    empName = str;
                }
            } catch (Exception e) {
                e.printStackTrace();
                writeLog(e.getMessage());
            }
            em = new EmployeeMasterClass();
            em.setEmp_Id(Integer.parseInt(dm.getEmp_Id()));
            em.setName(dm.getEmp_Name());
            list.clear();
            listView.setAdapter(null);
        } else if (this.edDPBy == requestCode && resultCode == RESULT_OK) {
            em = (EmployeeMasterClass) data.getSerializableExtra("result");
            ed_dispatchBy.setText(em.getName());
            String str = dm.getEmp_Name();
            Constant.showLog(str);
            try {
                String arr1[] = str.split("\\s+");
                if (arr1.length > 1) {
                    empName = arr1[0];
                } else {
                    empName = str;
                }
            } catch (Exception e) {
                e.printStackTrace();
                writeLog(e.getMessage());
            }
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
            adapter.initInterface(MainActivity_KRD.this);
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
        ed_total.setText("0");
        tv_transporter.setText("");
        ed_dispatchBy.setText(null);
        ed_bundles.setText("0");
        ed_cartons.setText("0");
        ed_cartons.setSelected(true);
        img_slip.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_camera_alt_black_24dp));
        psImagePath = "";
        flag = 0;
        imagePath = "NA";
        empName = "NA";
    }

    private void getDispatchMaster(int type) {
        final Constant constant = new Constant(MainActivity_KRD.this);
        constant.showPD();
        try {
            int maxAuto = db.getMaxAuto();
            //Auto + "|"+ CustId + "|"+ HOCode + "|"+ dispatchId + "|"+ empId + "|"+ type
            String url = maxAuto + "|" + 0 + "|" + hoCode + "|" + dpID + "|" + empId + "|" + type;
            writeLog("getDispatchMaster_" + url);
            if (pono != null && !pono.equals("")) {
                db.deleteOrderTableAfterSave(pono);
            }
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

    private void validations() {
        int flag = 0;
        int qty1 = Integer.parseInt(tv_poQty.getText().toString());
        int qty2 = Integer.parseInt(ed_total.getText().toString());
        if (ed_custName.getText().toString().equals("")) {
            toast.setText("Please Select Party Name");
            toast.show();
            flag = 1;
        } else if (ed_poNo.getText().toString().equals("")) {
            toast.setText("Please Select PO Number");
            toast.show();
            flag = 1;
        } else if (psImagePath.equals("")) {
            toast.setText("Please Capture Packing Slip Image");
            toast.show();
            flag = 1;
        }
        if (userType.equals("2")) {
            if (qty1 != qty2) {
                toast.setText("Total Quantity Mismatch");
                toast.show();
                flag = 1;
            }
        }
        if (flag == 0) {
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
        }
        Constant.showLog(dm.getPartyName() + "\n" +
                dm.getPONO() + "\n" +
                dm.getDcNo() + "\n" +
                dm.getDCdate() + "\n" +
                dm.getTotalQty() + "\n" +
                dm.getTransporter());

        Constant.showLog(em.getEmp_Id() + "\n" + em.getName());
        if (notOfCartoon.length() > 1) {
            notOfCartoon = notOfCartoon.substring(0, notOfCartoon.length() - 1);
        }
        if (imageNames.length() > 1) {
            imageNames = imageNames.substring(0, imageNames.length() - 1);
        }
        Constant.showLog(notOfCartoon);
        Constant.showLog(imageNames);

        //DCNO,PONO,DispatchBy,NoOfCartoon,DispatchPerson,CheckedPerson,Carton,Bundle,ImagePath,DTotal
        int DTotal = Integer.parseInt(ed_total.getText().toString());

        String data = dm.getDcNo() + "|" + dm.getPONO() + "|" + dm.getEmp_Id() + "|" + notOfCartoon + "|" +
                empId + "|" + empId + "|" + str1 + "|" + str + "|" + imageNames + "|" + psImagePath + "|" + DTotal;

        Constant.showLog(data);

        if (_flag == 0) {
            new saveDispatchMaster(dm.getPONO()).execute(data);
        } else {
            toast.setText("Please Capture All Images");
            toast.show();
        }
    }

    private class saveDispatchMaster extends AsyncTask<String, Void, String> {
        private String pono1 = "";
        private ProgressDialog pd;

        private saveDispatchMaster(String _pono) {
            this.pono1 = _pono;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(MainActivity_KRD.this);
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
                        if (retAutoBranchId[1].equals(String.valueOf(pono1))) {
                            pono = this.pono1;
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

    private void loadImage() {
        try {
            String img = psImagePath;
            String arr[] = img.split("_");
            String month = arr[6];
            String day = arr[5];
            String dpCenter = arr[1];
            String path = Constant.dpAppUrl + month + "/" + day + "/" + dpCenter + "/";
            //"http://103.109.13.200:24086/DPApp/May/03/UGNT/2731_UGNT_C_4723_ANIL_03_May_2019_12_56_19.jpg"
            Constant.showLog(path + psImagePath);
            Glide.with(getApplicationContext()).load(path + psImagePath)
                    .thumbnail(0.5f)
                    .crossFade()
                    .placeholder(R.drawable.ic_camera_alt_black_24dp)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(img_slip);
        } catch (Exception e) {
            e.printStackTrace();
            writeLog("loadImage_" + e.getMessage());
        }
    }

    private void showPic() {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity_KRD.this);
            builder.setCancelable(true);
            LayoutInflater inflater = getLayoutInflater();
            View view = inflater.inflate(R.layout.pic_dialog, null);
            ImageView _img = view.findViewById(R.id.dia_img);
            String img = psImagePath;
            String arr[] = img.split("_");
            String month = arr[6];
            String day = arr[5];
            String dpCenter = arr[1];
            String path = Constant.dpAppUrl + month + "/" + day + "/" + dpCenter + "/";
            //"http://103.109.13.200:24086/DPApp/May/03/UGNT/2731_UGNT_C_4723_ANIL_03_May_2019_12_56_19.jpg"
            Constant.showLog(path + psImagePath);
            Glide.with(getApplicationContext()).load(path + psImagePath)
                    .thumbnail(1f)
                    .crossFade()
                    .placeholder(R.drawable.ic_male)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .fitCenter()
                    .into(_img);
            builder.setView(view);
            builder.create().show();
        } catch (Exception e) {
            e.printStackTrace();
            writeLog("showPic_" + e.getMessage());
        }
    }

    private void init() {
        toast = Toast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        ed_custName = findViewById(R.id.ed_custName);
        ed_poNo = findViewById(R.id.ed_poNo);
        ed_dispatchBy = findViewById(R.id.ed_dispatchBy);
        ed_cartons = findViewById(R.id.ed_cartons);
        ed_total = findViewById(R.id.ed_total);
        ed_bundles = findViewById(R.id.ed_bundles);
        tv_poQty = findViewById(R.id.tv_poQty);
        tv_qty_Total = findViewById(R.id.tv_qtyTotal);
        tv_transporter = findViewById(R.id.tv_transporter);
        btn_submit = findViewById(R.id.btn_submit);
        listView = findViewById(R.id.listView);
        img_slip = findViewById(R.id.img_slip);

        lay_car = findViewById(R.id.lay_car);
        lay_header = findViewById(R.id.lay_header);
        tv_car = findViewById(R.id.tv_car);

        list = new ArrayList<>();
        db = new DBHandler(getApplicationContext());
        FirstActivity.pref = getSharedPreferences(FirstActivity.PREF_NAME, MODE_PRIVATE);
    }

    private void showDia(int a) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity_KRD.this);
        builder.setCancelable(false);
        if (a == 1) {
            builder.setMessage("Do You Want To Go Back ?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    new Constant(MainActivity_KRD.this).doFinish();
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
                    Intent intent = new Intent(MainActivity_KRD.this, UploadImageService.class);
                    startService(intent);
                    writeLog("UploadImageService_onHandleIntent_broadcastSend");
                    if (userType.equals("1")) {
                        getDispatchMaster(1);
                    } else {
                        getDispatchMaster(3);
                    }
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
                    db.deleteTable(DBHandler.Table_DispatchMaster);
                    flag = 0;
                    flag = 0;
                    if (userType.equals("1")) {
                        getDispatchMaster(1);
                    } else {
                        getDispatchMaster(3);
                    }
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
        }
        builder.create().show();
    }

    private void writeLog(String _data) {
        new WriteLog().writeLog(getApplicationContext(), "MainActivityKRD_" + _data);
    }
}

