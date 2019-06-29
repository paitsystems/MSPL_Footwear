package com.lnbinfotech.msplfootwear;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.lnbinfotech.msplfootwear.adapters.SizeGroupWiseColourAdapter;
import com.lnbinfotech.msplfootwear.constant.Constant;
import com.lnbinfotech.msplfootwear.db.DBHandler;
import com.lnbinfotech.msplfootwear.interfaces.RetrofitApiInterface;
import com.lnbinfotech.msplfootwear.interfaces.ServerCallback;
import com.lnbinfotech.msplfootwear.log.WriteLog;
import com.lnbinfotech.msplfootwear.model.FeedbackClass;
import com.lnbinfotech.msplfootwear.model.InvoiceNumberClass;
import com.lnbinfotech.msplfootwear.services.UploadImageService;
import com.lnbinfotech.msplfootwear.utility.RetrofitApiBuilder;
import com.lnbinfotech.msplfootwear.volleyrequests.VolleyRequests;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FeedbackActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private Spinner feedback_spinner, sp_sizeGroup, sp_color;
    //private String imagePath;
    private ArrayAdapter<String> feedbk_type, artNoAdapter, invNoAdapter, invOrderNoAdapter, staffAdapter;
    private String[] arr = {"Select Type", "Damage Goods", "Invoice", "Order", "Packing", "Service", "Others"};
    private EditText ed_description, ed_qty, ed_salesman;
    private AutoCompleteTextView auto_invoice_no, auto_article_no, auto_invOrderNo, auto_staff;
    private AppCompatButton bt_send;
    private ImageView imgv_img1, imgv_img2, imgv_img3;
    private LinearLayout packing_order_inv_lay, lay_img1, lay_img2, lay_img3;
    private CardView damaged_goods_cardlay, service_or_team_cardlay;
    private final int requestCode = 21;
    private ByteArrayOutputStream byteArray;
    private Bitmap bmp;
    private int flag;
    private FeedbackClass feedbackClass;
    private Constant constant;
    private Toast toast;
    private List<String> listArtNo, listInvNo, listStaffName, listOrderNo, sizeGroup_list, color_list;
    private DBHandler db;
    private RadioButton rdo_salesman, rdo_office;
    private int hocode, custId;
    private String name, seName;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Constant.liveTestFlag == 1) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }

        setContentView(R.layout.activity_feedback);

        init();
        setArticleNo();
        getInvoices(1, "I");

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        imgv_img1.setOnClickListener(this);
        imgv_img2.setOnClickListener(this);
        imgv_img3.setOnClickListener(this);

        rdo_office.setOnClickListener(this);
        rdo_salesman.setOnClickListener(this);

        bt_send.setOnClickListener(this);
        feedback_spinner.setOnItemSelectedListener(this);

        auto_article_no.setOnTouchListener((view, motionEvent) -> {
            final int action = motionEvent.getAction();
            if (action == MotionEvent.ACTION_DOWN) {
                auto_article_no.showDropDown();
                auto_article_no.setThreshold(0);
            }
            return false;
        });

        auto_invoice_no.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                final int action = motionEvent.getAction();
                if (action == MotionEvent.ACTION_DOWN) {
                    auto_invoice_no.showDropDown();
                    auto_invoice_no.setThreshold(0);
                }
                return false;
            }
        });

        auto_invOrderNo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                final int action = motionEvent.getAction();
                if (action == MotionEvent.ACTION_DOWN) {
                    auto_invOrderNo.showDropDown();
                    auto_invOrderNo.setThreshold(0);
                }
                return false;
            }
        });

        auto_staff.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                final int action = motionEvent.getAction();
                if (action == MotionEvent.ACTION_DOWN) {
                    auto_staff.showDropDown();
                    auto_staff.setThreshold(0);
                }
                return false;
            }
        });

        auto_article_no.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String prodName = listArtNo.get(i);
                AddToCartActivity.selProdId = db.getProdId(prodName);
                sizeGroup_list.clear();
                Cursor res1 = db.getDistinctSizeGroup("D");
                if (res1.moveToFirst()) {
                    do {
                        sizeGroup_list.add(res1.getString(res1.getColumnIndex(DBHandler.ARSD_SizeGroup)));
                    } while (res1.moveToNext());
                } else {
                    sizeGroup_list.add("NA");
                }
                res1.close();
                sp_sizeGroup.setAdapter(new ArrayAdapter<>(getApplicationContext(), R.layout.sizegroup_spinner_row, sizeGroup_list));
            }
        });

        sp_sizeGroup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selSizeGroup = (String) adapterView.getItemAtPosition(i);
                Constant.showLog(selSizeGroup);
                color_list.clear();
                Cursor res1 = db.getDistinctColour(selSizeGroup,"D",1);
                if (res1.moveToFirst()) {
                    do {
                        String colourHashcode = res1.getString(res1.getColumnIndex(DBHandler.ARSD_Colour));
                        color_list.add(colourHashcode);
                    } while (res1.moveToNext());
                } else {
                    color_list.add("NA");
                }
                res1.close();
                if (color_list.size() != 0) {
                    sp_color.setAdapter(new ArrayAdapter<>(getApplicationContext(), R.layout.sizegroup_spinner_row, color_list));
                } else {
                    toast.setText("No Colour Available");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgv_img1:
                //flag = 0;
                show_popup(0);
                break;
            case R.id.imgv_img2:
                //flag = 1;
                show_popup(1);
                break;
            case R.id.imgv_img3:
                //flag = 2;
                show_popup(2);
                break;
            case R.id.bt_send:
                setValue();
                break;
            case R.id.rdo_salesman:
                rdo_salesman.setChecked(true);
                rdo_office.setChecked(false);
                ed_salesman.setVisibility(View.VISIBLE);
                ed_salesman.setText(seName);
                auto_staff.setVisibility(View.GONE);
                break;
            case R.id.rdo_office:
                rdo_salesman.setChecked(false);
                rdo_office.setChecked(true);
                ed_salesman.setVisibility(View.GONE);
                auto_staff.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        String item = feedback_spinner.getSelectedItem().toString();
        Constant.showLog(item);
        feedbackClass.setFeedbk_type(item);
        feedback_spinner.setSelection(position);

        if (item.equals("Packing")) {
            name = "PK";
            packing_order_inv_lay.setVisibility(View.VISIBLE);
            damaged_goods_cardlay.setVisibility(View.GONE);
            service_or_team_cardlay.setVisibility(View.GONE);

            invNoAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.custom_spinner, listOrderNo);
            invNoAdapter.setDropDownViewResource(R.layout.custom_spinner);
            auto_invOrderNo.setAdapter(invNoAdapter);

        } else if (item.equals("Order")) {
            name = "OR";
            packing_order_inv_lay.setVisibility(View.VISIBLE);
            damaged_goods_cardlay.setVisibility(View.GONE);
            service_or_team_cardlay.setVisibility(View.GONE);

            invNoAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.custom_spinner, listOrderNo);
            invNoAdapter.setDropDownViewResource(R.layout.custom_spinner);
            auto_invOrderNo.setAdapter(invNoAdapter);

        } else if (item.equals("Invoice")) {
            name = "IN";
            packing_order_inv_lay.setVisibility(View.VISIBLE);
            damaged_goods_cardlay.setVisibility(View.GONE);
            service_or_team_cardlay.setVisibility(View.GONE);

            invNoAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.custom_spinner, listInvNo);
            invNoAdapter.setDropDownViewResource(R.layout.custom_spinner);
            auto_invOrderNo.setAdapter(invNoAdapter);

        } else if (item.equals("Damage Goods")) {
            name = "DG";
            packing_order_inv_lay.setVisibility(View.GONE);
            damaged_goods_cardlay.setVisibility(View.VISIBLE);
            service_or_team_cardlay.setVisibility(View.GONE);

            invNoAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.custom_spinner, listOrderNo);
            invNoAdapter.setDropDownViewResource(R.layout.custom_spinner);
            auto_invoice_no.setAdapter(invNoAdapter);

        } else if (item.equals("Service")) {
            name = "SR";
            packing_order_inv_lay.setVisibility(View.GONE);
            damaged_goods_cardlay.setVisibility(View.GONE);
            service_or_team_cardlay.setVisibility(View.VISIBLE);
        } else if (item.equals("Others")) {
            name = "OT";
            packing_order_inv_lay.setVisibility(View.GONE);
            damaged_goods_cardlay.setVisibility(View.GONE);
            service_or_team_cardlay.setVisibility(View.GONE);
        } else if (item.equals("Select Type") || item.equals("Team")) {
            packing_order_inv_lay.setVisibility(View.GONE);
            damaged_goods_cardlay.setVisibility(View.GONE);
            service_or_team_cardlay.setVisibility(View.GONE);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onBackPressed() {
        //showDia(0);
        new Constant(FeedbackActivity.this).doFinish();
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //showDia(0);
                new Constant(FeedbackActivity.this).doFinish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    //Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                    Constant.checkFolder(Constant.folder_name + File.separator + Constant.image_folder);
                    String _imagePath = getRealPathFromURI(Environment.getExternalStorageDirectory() +
                            File.separator + Constant.folder_name + File.separator +
                            Constant.image_folder + File.separator + "temp.jpg");
                    Bitmap bitmap = scaleBitmap(_imagePath);
                    String dateformat = currentDateFormat();
                    String file_name = "F_" + name + "_" + custId + "_" + hocode + "_" + dateformat + ".jpg";
                    store_CameraPhoto_InSdCard(bitmap, file_name);
                    Bitmap mbitmap = get_Image_from_sd_card(file_name);
                    imgv_img1.setImageBitmap(mbitmap);
                    Log.d("Log", "imgename:" + mbitmap);
                    //imgv_img2.setVisibility(View.VISIBLE);
                    lay_img2.setVisibility(View.VISIBLE);
                    feedbackClass.setFeed_img1(file_name);
                    Constant.showLog("file_name" + file_name);
                    Constant.showLog("" + mbitmap);
                    // feedbackClass.setFeed_img1(String.valueOf(mbitmap));
                }
                break;
            case 2:
                if (resultCode == RESULT_OK) {
                    //Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                    String _imagePath = getRealPathFromURI(Environment.getExternalStorageDirectory() +
                            File.separator + Constant.folder_name + File.separator +
                            Constant.image_folder + File.separator + "temp.jpg");
                    Bitmap bitmap = scaleBitmap(_imagePath);
                    Constant.checkFolder(Constant.image_folder);
                    String dateformat = currentDateFormat();
                    String file_name = "F_" + name + "_" + custId + "_" + hocode + "_" + dateformat + ".jpg";
                    store_CameraPhoto_InSdCard(bitmap, file_name);
                    Bitmap mbitmap = get_Image_from_sd_card(file_name);
                    // imgv_img3.setVisibility(View.VISIBLE);
                    imgv_img2.setImageBitmap(mbitmap);
                    lay_img3.setVisibility(View.VISIBLE);
                    feedbackClass.setFeed_img2(file_name);
                    Constant.showLog("file_name" + file_name);
                    //feedbackClass.setFeed_img2(String.valueOf(mbitmap));
                    Log.d("Log", "imgename:" + mbitmap);
                }
                break;
            case 3:
                if (resultCode == RESULT_OK) {
                    //Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                    String _imagePath = getRealPathFromURI(Environment.getExternalStorageDirectory() +
                            File.separator + Constant.folder_name + File.separator +
                            Constant.image_folder + File.separator + "temp.jpg");
                    Bitmap bitmap = scaleBitmap(_imagePath);
                    Constant.checkFolder(Constant.image_folder);
                    String dateformat = currentDateFormat();
                    String file_name = "F_" + name + "_" + custId + "_" + hocode + "_" + dateformat + ".jpg";
                    store_CameraPhoto_InSdCard(bitmap, file_name);
                    Bitmap mbitmap = get_Image_from_sd_card(file_name);
                    imgv_img3.setImageBitmap(mbitmap);
                    Log.d("Log", "imgename:" + mbitmap);
                    feedbackClass.setFeed_img3(file_name);
                    Constant.showLog("file_name" + file_name);
                    //feedbackClass.setFeed_img3(String.valueOf(mbitmap));
                }
                break;
            case 4:
                if (data != null && resultCode == RESULT_OK) {
                    try {
                        String dateformat = currentDateFormat();
                        String file_name = "feedbkimg_" + dateformat + ".jpg";
                        File destFile = new File((Environment.getExternalStorageDirectory() + File.separator +
                                Constant.folder_name + File.separator +
                                Constant.image_folder + File.separator + file_name));
                        copyFile(new File(getPath(data.getData())), destFile);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                break;
            case 5:
                if (data != null && resultCode == RESULT_OK) {
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String filePath = cursor.getString(columnIndex);
                    cursor.close();

                    if (bmp != null && !bmp.isRecycled()) {
                        bmp = null;
                    }
                    bmp = BitmapFactory.decodeFile(filePath);
                    imgv_img2.setImageBitmap(bmp);
                    imgv_img3.setVisibility(View.VISIBLE);
                    feedbackClass.setFeed_img2(String.valueOf(bmp));
                } else {
                    writeLog("Statusimg2:Photopicker canceled");
                }
                break;
            case 6:
                if (data != null && resultCode == RESULT_OK) {
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String filePath = cursor.getString(columnIndex);
                    cursor.close();

                    if (bmp != null && !bmp.isRecycled()) {
                        bmp = null;
                    }
                    bmp = BitmapFactory.decodeFile(filePath);//imgv_img3.setBackgroundResource(0);
                    imgv_img3.setImageBitmap(bmp);
                    feedbackClass.setFeed_img3(String.valueOf(bmp));
                } else {
                    writeLog("Statusimg3:Photopicker canceled");
                }
                break;
        }
    }

    private void getInvoices(int type, String orderType) {
        constant = new Constant(FeedbackActivity.this);
        constant.showPD();
        try {
            //int maxAuto = db.getMaxAuto();
            //CustId + "|" + branchId + "|"+ seId + "|"+ hocode + "|" + type + "|" orderType
            int CustId = FirstActivity.pref.getInt(getString(R.string.pref_retailCustId),0);
            int branchId = FirstActivity.pref.getInt(getString(R.string.pref_branchid),0);
            int seId = 0;
            int hocode = FirstActivity.pref.getInt(getString(R.string.pref_hocode),0);

            String url = CustId + "|" + branchId + "|" + seId + "|" + hocode + "|" + "C" + "|" + orderType;
            writeLog("getInvoices_" + url);
            final JSONObject jsonBody = new JSONObject();
            jsonBody.put("details", url);
            RequestBody body = RequestBody.create(okhttp3.MediaType.
                    parse("application/json; charset=utf-8"), (jsonBody).toString());
            Constant.showLog(jsonBody.toString());

            Call<List<InvoiceNumberClass>> call = new RetrofitApiBuilder().getApiBuilder().
                    create(RetrofitApiInterface.class).
                    getInvoiceNumber(body);
            call.enqueue(new Callback<List<InvoiceNumberClass>>() {
                @Override
                public void onResponse(Call<List<InvoiceNumberClass>> call, Response<List<InvoiceNumberClass>> response) {
                    Constant.showLog("onResponse");
                    List<InvoiceNumberClass> list = response.body();
                    constant.showPD();
                    if (list != null) {
                        if (type == 1) {
                            listInvNo.clear();
                            for (InvoiceNumberClass inv : list)
                                listInvNo.add(inv.getInvNo());

                            getInvoices(2, "O");
                        } else if (type == 2) {
                            listOrderNo.clear();
                            for (InvoiceNumberClass inv : list)
                                listOrderNo.add(inv.getInvNo());

                            getInvoices(3, "E");
                        } else if (type == 3) {
                            listStaffName.clear();
                            for (InvoiceNumberClass inv : list)
                                listStaffName.add(inv.getInvNo());

                            staffAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.custom_spinner, listStaffName);
                            staffAdapter.setDropDownViewResource(R.layout.custom_spinner);
                            auto_staff.setAdapter(staffAdapter);
                        }
                        Constant.showLog(list.size() + "_getInvoices");
                    } else {
                        Constant.showLog("onResponse_list_null");
                        writeLog("getInvoices_onResponse_list_null");
                    }
                }

                @Override
                public void onFailure(Call<List<InvoiceNumberClass>> call, Throwable t) {
                    Constant.showLog("onFailure");
                    if (!call.isCanceled()) {
                        call.cancel();
                    }
                    t.printStackTrace();
                    writeLog("getInvoices_onFailure_" + t.getMessage());
                    constant.showPD();
                    show_popup(10);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            writeLog("getInvoices_" + e.getMessage());
            constant.showPD();
            show_popup(10);
        }
    }

    private void setArticleNo() {
        listArtNo = db.getArticleNo();
        Constant.showLog("" + listArtNo.size());
        artNoAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.custom_spinner, listArtNo);
        artNoAdapter.setDropDownViewResource(R.layout.custom_spinner);
        auto_article_no.setAdapter(artNoAdapter);
    }

    private void setValue() {
        String description = ed_description.getText().toString();
        String item = feedback_spinner.getSelectedItem().toString();
        if (!item.equals("Select Type")) {
            if (!description.equals("")) {
                String article_no = auto_article_no.getText().toString();
                String invoice_no = "0", sizeGroup = "NA", color = "NA";

                if (item.equals("Damage Goods")) {
                    invoice_no = auto_invoice_no.getText().toString();
                    sizeGroup = sizeGroup_list.get(sp_sizeGroup.getSelectedItemPosition());
                    color = color_list.get(sp_color.getSelectedItemPosition());
                } else if (item.equals("Invoice") || item.equals("Order") || item.equals("Packing")) {
                    invoice_no = auto_invOrderNo.getText().toString();
                }

                String qty = ed_qty.getText().toString();
                String salesman = "0";
                String front_office = "NA";
                if (rdo_office.isChecked()) {
                    front_office = "Front Office";
                    salesman = auto_staff.getText().toString();
                } else if (rdo_salesman.isChecked()) {
                    front_office = "Salesman";
                    salesman = ed_salesman.getText().toString();
                }
                feedbackClass.setArticle_no(article_no);
                feedbackClass.setInvoice_no(invoice_no);
                feedbackClass.setQty(qty);
                feedbackClass.setSalesman_id(salesman);
                feedbackClass.setFront_office(front_office);
                feedbackClass.setDescription(description);
                feedbackClass.setSizeGroup(sizeGroup);
                feedbackClass.setColor(color);
                if (feedbackClass.getFeed_img1().equals("")) {
                    feedbackClass.setFeed_img1("NA");
                }
                if (feedbackClass.getFeed_img2().equals("")) {
                    feedbackClass.setFeed_img2("NA");
                }
                if (feedbackClass.getFeed_img3().equals("")) {
                    feedbackClass.setFeed_img3("NA");
                }
                if(!feedbackClass.getFeed_img1().equals("NA") || !feedbackClass.getFeed_img2().equals("NA")
                        || !feedbackClass.getFeed_img3().equals("NA")) {
                    new uploadImage().execute();
                } else {
                    saveFeedbackdetail();
                }
            } else {
                toast.setText("Please Enter Description");
                toast.show();
            }
        } else {
            toast.setText("Please Select Feedback Type");
            toast.show();
        }
    }

    private void resetValue() {
        feedback_spinner.setSelection(0);
        auto_invOrderNo.setText("");
        auto_staff.setText("");
        auto_invoice_no.setText("");
        auto_article_no.setText("");
        ed_description.setText("");
        ed_qty.setText("");

        rdo_salesman.setChecked(false);
        rdo_office.setChecked(false);

        imgv_img1.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_photo_camera));
        imgv_img2.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_photo_camera));
        imgv_img3.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_photo_camera));

        lay_img2.setVisibility(View.GONE);
        lay_img3.setVisibility(View.GONE);

        packing_order_inv_lay.setVisibility(View.GONE);
        damaged_goods_cardlay.setVisibility(View.GONE);
        service_or_team_cardlay.setVisibility(View.GONE);
    }

    private void init() {
        FirstActivity.pref = getSharedPreferences(FirstActivity.PREF_NAME, MODE_PRIVATE);
        feedbackClass = new FeedbackClass();
        feedbackClass.setFeed_img1("NA");
        feedbackClass.setFeed_img2("NA");
        feedbackClass.setFeed_img3("NA");
        feedback_spinner = findViewById(R.id.feedback_spinner);
        sp_sizeGroup = findViewById(R.id.sp_sizegroup);
        sp_color = findViewById(R.id.sp_color);
        ed_description = findViewById(R.id.ed_description);
        auto_article_no = findViewById(R.id.auto_article_no);
        ed_qty = findViewById(R.id.ed_qty);
        ed_salesman = findViewById(R.id.ed_salesman);
        auto_invoice_no = findViewById(R.id.auto_invoice_no);
        auto_invOrderNo = findViewById(R.id.auto_invOrderNo);
        auto_staff = findViewById(R.id.auto_staff);
        bt_send = findViewById(R.id.bt_send);
        imgv_img1 = findViewById(R.id.imgv_img1);
        imgv_img2 = findViewById(R.id.imgv_img2);
        imgv_img3 = findViewById(R.id.imgv_img3);
        packing_order_inv_lay = findViewById(R.id.packing_order_inv_lay);
        lay_img1 = findViewById(R.id.lay_img1);
        lay_img2 = findViewById(R.id.lay_img2);
        lay_img3 = findViewById(R.id.lay_img3);
        rdo_salesman = findViewById(R.id.rdo_salesman);
        rdo_office = findViewById(R.id.rdo_office);
        damaged_goods_cardlay = findViewById(R.id.damaged_goods_cardlay);
        service_or_team_cardlay = findViewById(R.id.service_or_team_cardlay);
        feedbk_type = new ArrayAdapter<>(this, R.layout.feedbk_type_list, arr);
        feedback_spinner.setAdapter(feedbk_type);
        toast = Toast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        Constant.checkFolder(Constant.folder_name + File.separator + Constant.image_folder);
        listArtNo = new ArrayList<>();
        listInvNo = new ArrayList<>();
        listOrderNo = new ArrayList<>();
        sizeGroup_list = new ArrayList<>();
        color_list = new ArrayList<>();
        listStaffName = new ArrayList<>();
        db = new DBHandler(getApplicationContext());
        hocode = FirstActivity.pref.getInt(getString(R.string.pref_hocode),0);
        custId = FirstActivity.pref.getInt(getString(R.string.pref_retailCustId),0);
        seName = FirstActivity.pref.getString(getString(R.string.pref_execName),"");

    }

    private void show_popup(int id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        if (id == 0) {
            builder.setMessage("Do you want to attach image?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();

                    Intent intent1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File f = Constant.checkFolder(Constant.folder_name + File.separator + Constant.image_folder);
                    f = new File(f.getAbsolutePath(), "temp.jpg");
                    Uri photoURI = FileProvider.getUriForFile(getApplicationContext(), getApplicationContext().getPackageName()
                            + ".provider", f);
                    intent1.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    intent1.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    startActivityForResult(intent1, 1);
                    overridePendingTransition(R.anim.enter, R.anim.exit);
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
        } else if (id == 1) {
            builder.setMessage("Do you want to attach image?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();

                    Intent intent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File f = Constant.checkFolder(Constant.folder_name + File.separator + Constant.image_folder);
                    f = new File(f.getAbsolutePath(), "temp.jpg");
                    Uri photoURI = FileProvider.getUriForFile(getApplicationContext(), getApplicationContext().getPackageName()
                            + ".provider", f);
                    intent2.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    intent2.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    startActivityForResult(intent2, 2);
                    overridePendingTransition(R.anim.enter, R.anim.exit);
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
        } else if (id == 2) {
            builder.setMessage("Do you want to attach image?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();

                    Intent intent3 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File f = Constant.checkFolder(Constant.folder_name + File.separator + Constant.image_folder);
                    f = new File(f.getAbsolutePath(), "temp.jpg");
                    Uri photoURI = FileProvider.getUriForFile(getApplicationContext(), getApplicationContext().getPackageName()
                            + ".provider", f);
                    intent3.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    intent3.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    startActivityForResult(intent3, 3);
                    overridePendingTransition(R.anim.enter, R.anim.exit);
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
        } else if (id == 3) {
            builder.setMessage("Attach image:");
            builder.setPositiveButton("Camera", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    dialogInterface.dismiss();

                    Intent intent1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File f = Constant.checkFolder(Constant.folder_name + File.separator + Constant.image_folder);
                    f = new File(f.getAbsolutePath(), "temp.jpg");
                    Uri photoURI = FileProvider.getUriForFile(getApplicationContext(), getApplicationContext().getPackageName()
                            + ".provider", f);
                    intent1.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    intent1.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    startActivityForResult(intent1, 1);
                    overridePendingTransition(R.anim.enter, R.anim.exit);
                }
            });
            builder.setNegativeButton("Gallery", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    dialogInterface.dismiss();

                    Intent intent1 = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    File f = Constant.checkFolder(Constant.folder_name + File.separator + Constant.image_folder);
                    f = new File(f.getAbsolutePath(), "temp.jpg");
                    Uri photoURI = FileProvider.getUriForFile(getApplicationContext(), getApplicationContext().getPackageName()
                            + ".provider", f);
                    intent1.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    intent1.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    startActivityForResult(intent1, 4);
                    overridePendingTransition(R.anim.enter, R.anim.exit);
                }
            });
        } else if (id == 4) {
            builder.setMessage("Attach image:");
            builder.setPositiveButton("Camera", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    dialogInterface.dismiss();

                    Intent intent1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File f = Constant.checkFolder(Constant.folder_name + File.separator + Constant.image_folder);
                    f = new File(f.getAbsolutePath(), "temp.jpg");
                    Uri photoURI = FileProvider.getUriForFile(getApplicationContext(), getApplicationContext().getPackageName()
                            + ".provider", f);
                    intent1.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    intent1.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    startActivityForResult(intent1, 2);
                    overridePendingTransition(R.anim.enter, R.anim.exit);
                }
            });
            builder.setNegativeButton("Gallery", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    dialogInterface.dismiss();

                    Intent intent1 = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    File f = Constant.checkFolder(Constant.folder_name + File.separator + Constant.image_folder);
                    f = new File(f.getAbsolutePath(), "temp.jpg");
                    Uri photoURI = FileProvider.getUriForFile(getApplicationContext(), getApplicationContext().getPackageName()
                            + ".provider", f);
                    intent1.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    intent1.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    startActivityForResult(intent1, 5);
                    overridePendingTransition(R.anim.enter, R.anim.exit);
                }
            });
        } else if (id == 5) {
            builder.setMessage("Attach image:");
            builder.setPositiveButton("Camera", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    dialogInterface.dismiss();

                    Intent intent1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File f = Constant.checkFolder(Constant.folder_name + File.separator + Constant.image_folder);
                    f = new File(f.getAbsolutePath(), "temp.jpg");
                    Uri photoURI = FileProvider.getUriForFile(getApplicationContext(), getApplicationContext().getPackageName()
                            + ".provider", f);
                    intent1.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    intent1.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    startActivityForResult(intent1, 3);
                    overridePendingTransition(R.anim.enter, R.anim.exit);
                }
            });
            builder.setNegativeButton("Gallery", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    dialogInterface.dismiss();

                    Intent intent1 = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    File f = Constant.checkFolder(Constant.folder_name + File.separator + Constant.image_folder);
                    f = new File(f.getAbsolutePath(), "temp.jpg");
                    Uri photoURI = FileProvider.getUriForFile(getApplicationContext(), getApplicationContext().getPackageName()
                            + ".provider", f);
                    intent1.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    intent1.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    startActivityForResult(intent1, 6);
                    overridePendingTransition(R.anim.enter, R.anim.exit);
                }
            });
        } else if (id == 6) {
            builder.setMessage("You can attach only three images do you want to delete it?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    imgv_img3.setImageBitmap(null);
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
        } else if (id == 7) {
            builder.setMessage("Feedback Saved Successfully");
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    show_popup(11);
                }
            });
        } else if (id == 8) {
            builder.setMessage("Error While Saving Data");
            builder.setPositiveButton("Try Again", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    saveFeedbackdetail();
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
        } else if (id == 10) {
            builder.setMessage("Error While Loading Data");
            builder.setPositiveButton("Try Again", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    getInvoices(0, "I");
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
        } else if (id == 11) {
            builder.setMessage("Do You Want Give Another Feedback?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    resetValue();
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    Intent intent = new Intent(FeedbackActivity.this, UploadImageService.class);
                    startService(intent);
                    writeLog("UploadImageService_onHandleIntent_broadcastSend");
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    new Constant(FeedbackActivity.this).doFinish();
                }
            });
        }
        builder.create().show();
    }

    private String getRealPathFromURI(String contentURI) {
        Uri contentUri = Uri.parse(contentURI);
        Cursor cursor = getContentResolver().query(contentUri, null, null, null, null);
        if (cursor == null) {
            return contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            String s = cursor.getString(idx);
            cursor.close();
            return s;
        }
    }

    private Bitmap scaleBitmap(String imagePath) {
        Bitmap resizedBitmap = null;
        /*try {
            int inWidth, inHeight;
            InputStream in = new FileInputStream(imagePath);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(in, null, options);
            in.close();
            //in = null;
            inWidth = options.outWidth;
            inHeight = options.outHeight;
            in = new FileInputStream(imagePath);
            options = new BitmapFactory.Options();
            options.inSampleSize = Math.max(inWidth / 300, inHeight / 300);
            Bitmap roughBitmap = BitmapFactory.decodeStream(in, null, options);

            Matrix m = new Matrix();
            RectF inRect = new RectF(0, 0, roughBitmap.getWidth(), roughBitmap.getHeight());
            RectF outRect = new RectF(0, 0, roughBitmap.getWidth(), roughBitmap.getHeight());
            m.setRectToRect(inRect, outRect, Matrix.ScaleToFit.CENTER);
            float[] values = new float[9];
            m.getValues(values);
            resizedBitmap = Bitmap.createScaledBitmap(roughBitmap, (int) (roughBitmap.getWidth() * values[0]), (int) (roughBitmap.getHeight() * values[4]), true);
        } catch (Exception e) {
            e.printStackTrace();
            writeLog("FileNotFoundException and IOException found:" + e);
        }*/
        return resizedBitmap;
    }

    private String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        startManagingCursor(cursor);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    private void copyFile(File sourceFile, File destFile) throws IOException {
        if (!sourceFile.exists()) {
            return;
        }
        FileChannel source = null;
        FileChannel destination = null;
        source = new FileInputStream(sourceFile).getChannel();
        destination = new FileOutputStream(destFile).getChannel();
        if (destination != null && source != null) {
            destination.transferFrom(source, 0, source.size());
        }
        if (source != null) {
            source.close();
        }
        if (destination != null) {
            destination.close();
        }
    }

    private void store_CameraPhoto_InSdCard(Bitmap _bitmap, String file_name) {
        File file = new File(Environment.getExternalStorageDirectory() + File.separator +
                Constant.folder_name + File.separator +
                Constant.image_folder, file_name);
        Log.d("Log", "File path:" + file);

        File f = new File(Environment.getExternalStorageDirectory() + File.separator +
                Constant.folder_name + File.separator + Constant.image_folder);
        for (File temp : f.listFiles()) {
            if (temp.getName().equals("temp.jpg")) {
                f = temp;
                break;
            }
        }

        Bitmap bitmap;
        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(), bitmapOptions);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 15, fos);
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Bitmap get_Image_from_sd_card(String filename) {
        Bitmap bitmap = null;
        File imgfile = new File(Environment.getExternalStorageDirectory() + File.separator +
                Constant.folder_name + File.separator +
                Constant.image_folder + File.separator + filename);
        try {
            FileInputStream fis = new FileInputStream(imgfile);
            bitmap = BitmapFactory.decodeStream(fis);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    private String currentDateFormat() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd_MMM_yyyy_HH_mm_ss", Locale.ENGLISH);
        return sdf.format(new Date());
    }

    private void saveFeedbackdetail() {
        try {
            //constant = new Constant(FeedbackActivity.this);
            String url = "";
            //constant.showPD();
            String feedtype = feedbackClass.getFeedbk_type();
            String articleno = feedbackClass.getArticle_no();
            String invoiceno = feedbackClass.getInvoice_no();
            String qty = feedbackClass.getQty();
            String salesmanid = feedbackClass.getSalesman_id();
            String officetype = feedbackClass.getFront_office();
            String description = feedbackClass.getDescription();
            String img1 = feedbackClass.getFeed_img1();

            String img2 = feedbackClass.getFeed_img2();
            String img3 = feedbackClass.getFeed_img3();
            String crby = String.valueOf(FirstActivity.pref.getInt(getString(R.string.pref_retailCustId), 0));
            Constant.showLog("crby" + crby);

            if (feedtype.equals("Damage Goods")) {
                salesmanid = "NA";
                officetype = "NA";
            } else if (feedtype.equals("Service") || feedtype.equals("Team")) {
                articleno = "0";
                qty = "0";
                invoiceno = "0";
            } else if (feedtype.equals("Invoice") || feedtype.equals("Order") || feedtype.equals("Packing")) {
                salesmanid = "NA";
                officetype = "NA";
                articleno = "0";
                qty = "0";
            } else if (feedtype.equals("Others")) {
                salesmanid = "NA";
                officetype = "NA";
                articleno = "0";
                qty = "0";
                invoiceno = "0";
            }

            int branchId = getBranchId(invoiceno);

            /*String _feedtype = URLEncoder.encode(feedtype, "UTF-8");
            String _articleno = URLEncoder.encode(articleno, "UTF-8");
            String _invoiceno = URLEncoder.encode(invoiceno, "UTF-8");
            String _qty = URLEncoder.encode(qty, "UTF-8");
            String _salesmanid = URLEncoder.encode(salesmanid, "UTF-8");
            String _officetype = URLEncoder.encode(officetype, "UTF-8");
            String _description = URLEncoder.encode(description, "UTF-8");
            String _img1 = URLEncoder.encode(img1, "UTF-8");
            String _img2 = URLEncoder.encode(img2, "UTF-8");
            String _img3 = URLEncoder.encode(img3, "UTF-8");
            String _crby = URLEncoder.encode(crby, "UTF-8");

            url = Constant.ipaddress + "/SaveFeedbackDetail?feedbk_type=" + _feedtype + "&article_no=" +
                    _articleno + "&invoice_no=" + _invoiceno + "&qty=" + _qty + "&salesman_id=" +
                    _salesmanid + "&office_type=" + _officetype + "&description=" + _description + "&img1=" +
                    _img1 + "&img2=" + _img2 + "&img3=" + _img3 + "&crby=" + _crby + "&user_type=C";*/

            url = feedtype + "|" + articleno + "|" + invoiceno + "|" + qty + "|" + "0" + "|" +
                    officetype + "|" + description + "|" + img1 + "|" + img2 + "|" + img3 + "|" +
                    crby + "|" + "C" + "|" + branchId + "|" + salesmanid + "|" +
                    feedbackClass.getSizeGroup() + "|" + feedbackClass.getColor();

            Constant.showLog(url);
            writeLog("savefeedback_url called_" + url);

            new saveFeedBack("").execute(url);

            /*VolleyRequests requests = new VolleyRequests(FeedbackActivity.this);
            requests.saveFeedbackDetail(url, new ServerCallback() {
                @Override
                public void onSuccess(String result) {
                    constant.showPD();
                    show_popup(7);
                    Constant.showLog("Volly request success");
                    writeLog("saveFeedbackdetail():Volley_success");
                }

                @Override
                public void onFailure(String result) {
                    constant.showPD();
                    show_popup(8);
                    writeLog("saveFeedbackdetail_" + result);
                }
            });*/
        } catch (Exception e) {
            constant.showPD();
            show_popup(7);
            e.printStackTrace();
            writeLog("saveFeedbackdetail_" + e.getMessage());
        }
    }

    private int getBranchId(String invNo){
        int branchId = 0;
        if(!invNo.equals("0")){
            String arr [] = invNo.split("/");
            if(arr.length>1){
                branchId = db.getBranchId(arr[0]);
            }
        } else {
            branchId = hocode;
        }
        return branchId;
    }

    private class saveFeedBack extends AsyncTask<String, Void, String> {
        private String pono1 = "";
        private ProgressDialog pd;

        private saveFeedBack(String _pono) {
            this.pono1 = _pono;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(FeedbackActivity.this);
            pd.setMessage("Please Wait...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... url) {
            String value = "";
            DefaultHttpClient httpClient = null;
            HttpPost request = new HttpPost(Constant.ipaddress + "/saveFeedbackDetails");
            request.setHeader("Accept", "application/json");
            request.setHeader("Content-type", "application/json");
            try {
                JSONStringer vehicle = new JSONStringer().object().key("rData").object().key("details").value(url[0]).endObject().endObject();
                StringEntity entity = new StringEntity(vehicle.toString());
                Constant.showLog(vehicle.toString());
                writeLog("saveFeedBack_" + vehicle.toString());
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
                writeLog("saveFeedBack_result_" + e.getMessage());
            } finally {
                try {
                    if (httpClient != null) {
                        httpClient.getConnectionManager().shutdown();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    writeLog("saveFeedBack_finally_" + e.getMessage());
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
                String str = new JSONObject(result).getString("SaveFeedbackDetailsResult");
                str = str.replace("\"", "");
                Constant.showLog(str);
                pd.dismiss();
                writeLog("saveFeedBack_result_" + str + "_" + result);
                String[] retAutoBranchId = str.split("\\-");
                if (retAutoBranchId.length > 1) {
                    if (!retAutoBranchId[0].equals("0") && !retAutoBranchId[0].equals("+2") && !retAutoBranchId[0].equals("+3")) {
                        show_popup(7);
                    } else {
                        show_popup(8);
                    }
                } else {
                    show_popup(8);
                }
            } catch (Exception e) {
                writeLog("saveFeedBack_" + e.getMessage());
                e.printStackTrace();
                show_popup(8);
                pd.dismiss();
            }
        }
    }

    private class uploadImage extends AsyncTask<Void, Void, String> {

        private ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(FeedbackActivity.this);
            pd.setMessage("Uploading Images...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                File f;
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                    f = new File(Environment.getExternalStorageDirectory() + File.separator +
                            Constant.folder_name + File.separator + Constant.image_folder);
                } else {
                    f = getApplicationContext().getFilesDir();
                }
                FTPClient client = new FTPClient();
                client.connect(Constant.ftp_adress, 21);
                client.login(Constant.ftp_username, Constant.ftp_password);
                client.setFileType(FTP.BINARY_FILE_TYPE);
                client.enterLocalPassiveMode();
                if (f != null) {
                    if (f.exists()) {
                        Constant.showLog(f.getPath());
                        for (File file : f.listFiles()) {
                            if (file != null && !file.isDirectory()) {
                                FileInputStream ifile = new FileInputStream(file);
                                String str = file.getName();
                                String arr[] = str.split("_");
                                try {
                                    String imgType = arr[0];
                                    if (imgType.equals("F")) {
                                        client.changeToParentDirectory();
                                        Constant.showLog(client.printWorkingDirectory());
                                        String feedtype = arr[1];
                                        String month = arr[5];
                                        String day = arr[4];
                                        String hoCode = "HKHO";
                                        String folderType = "";
                                        client.cwd(Constant.dir_Feed_Back);
                                        Constant.showLog(client.printWorkingDirectory());
                                        if (arr[3].equals("01") || arr[3].equals("1")) {
                                            hoCode = "HKHO";
                                        } else if (arr[3].equals("12")) {
                                            hoCode = "HKRD";
                                        } else if (arr[3].equals("13")) {
                                            hoCode = "HANR";
                                        }

                                        if (feedtype.equals("DG")) {
                                            folderType = "Damage Goods";
                                        } else if (feedtype.equals("SR")){
                                            folderType = "Service";
                                        } else if(feedtype.equals("IN")){
                                            folderType = "Invoice";
                                        } else if (feedtype.equals("OT")) {
                                            folderType = "Others";
                                        } else if(feedtype.equals("OR")) {
                                            folderType = "Order";
                                        } else if(feedtype.equals("PK")) {
                                            folderType = "Packing";
                                        }
                                        Constant.showLog("F/" + hoCode + "/" + month + "/" + folderType);
                                        client.cwd(hoCode + "/" + month + "/" + folderType);
                                        Constant.showLog(client.printWorkingDirectory());
                                        if (client.storeFile(file.getName(), ifile)) {
                                            if (!client.printWorkingDirectory().equals(Constant.dir_Feed_Back)) {
                                                client.changeToParentDirectory();
                                                Constant.showLog(client.printWorkingDirectory());
                                                if (!client.printWorkingDirectory().equals(Constant.dir_Feed_Back)) {
                                                    client.changeToParentDirectory();
                                                    Constant.showLog(client.printWorkingDirectory());
                                                    if (!client.printWorkingDirectory().equals(Constant.dir_Feed_Back)) {
                                                        client.changeToParentDirectory();
                                                        Constant.showLog(client.printWorkingDirectory());
                                                    }
                                                }
                                            }
                                            file.delete();
                                            Constant.showLog("Feedback Image deleted.." + file.getName());
                                        } else {
                                            writeLog("onHandleIntent_Error_While_Storing_Feedback_File");
                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    writeLog("onHandleIntent_" + e.getMessage());
                                }
                            }
                        }
                    }
                }
                client.disconnect();
                Constant.showLog("disconnected..");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pd.dismiss();
            saveFeedbackdetail();
        }
    }

    private void writeLog(String _data) {
        new WriteLog().writeLog(getApplicationContext(), "FeedbackActivity_" + _data);
    }
}
