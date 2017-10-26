package com.lnbinfotech.msplfootwearex;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.lnbinfotech.msplfootwearex.constant.Constant;
import com.lnbinfotech.msplfootwearex.db.DBHandler;
import com.lnbinfotech.msplfootwearex.interfaces.ServerCallback;
import com.lnbinfotech.msplfootwearex.log.WriteLog;
import com.lnbinfotech.msplfootwearex.services.UploadImageService;
import com.lnbinfotech.msplfootwearex.volleyrequests.VolleyRequests;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URLEncoder;

public class NewCustomerEntryDetailFormActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText ed_cus_name, ed_mobile_no, ed_email_id, ed_address, ed_gstno, ed_panno;
    private Spinner spinner_addproof, spinner_idproof;
    private String[] add_proof = {"Aadhaar", "Light bill", "Aggreement copy", "Index 0"};
    private String[] id_proof = {"Aadhaar", "Pan card", "Driving license", "Election card"};
    private ArrayAdapter<String> adapter_address;
    private ArrayAdapter<String> adapter_id;
    private RadioButton rdo_gst, rdo_pan;
    private LinearLayout gst_lay, pan_lay;
    private AppCompatButton bt_save;
    private ImageView imageView_edit, imageView_cus_edit, imageView_address_edit, imageView_id_edit, imageView_gstpan_edit, imageView_cus_image, imageView_addproof, imageView_idproof, imageView_pan_img, imageView_gst_img;
    private Constant constant;
    private DBHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_customer_entry_detail_form);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.customerentrydata);
        }
        init();
        set_value_newCusEntry();
        set_value_attachCusImage();
        set_value_attachAddressProof();
        set_value_attachAddressProofImage();
        set_value_attachIdProof();
        set_value_attachIdProofImage();
        set_value_attachgstpan_no();
        //set_value_attachGstProofImage();
        //set_value_attachPanProofImage();
    }

    private void init() {
        db = new DBHandler(NewCustomerEntryDetailFormActivity.this);
        ed_cus_name = (EditText) findViewById(R.id.ed_cus_name);
        ed_mobile_no = (EditText) findViewById(R.id.ed_mobile_no);
        ed_email_id = (EditText) findViewById(R.id.ed_emailid);
        ed_address = (EditText) findViewById(R.id.ed_address);
        ed_gstno = (EditText) findViewById(R.id.ed_gstno);
        ed_panno = (EditText) findViewById(R.id.ed_panno);

        rdo_gst = (RadioButton) findViewById(R.id.rdo_gstno);
        rdo_pan = (RadioButton) findViewById(R.id.rdo_panno);

        gst_lay = (LinearLayout) findViewById(R.id.gst_lay);
        pan_lay = (LinearLayout) findViewById(R.id.pan_lay);
        bt_save = (AppCompatButton) findViewById(R.id.btn_save);

        imageView_gstpan_edit = (ImageView) findViewById(R.id.imageView_gstpan_edit);
        imageView_id_edit = (ImageView) findViewById(R.id.imageView_id_edit);
        imageView_address_edit = (ImageView) findViewById(R.id.imageView_address_edit);
        imageView_cus_edit = (ImageView) findViewById(R.id.imageView_cus_edit);
        imageView_edit = (ImageView) findViewById(R.id.imageView_edit);
        imageView_cus_image = (ImageView) findViewById(R.id.imageView_cus_image);
        imageView_addproof = (ImageView) findViewById(R.id.imageView_addproof);
        imageView_idproof = (ImageView) findViewById(R.id.imageView_idproof);
        imageView_gst_img = (ImageView) findViewById(R.id.imageView_gst_img);
        imageView_pan_img = (ImageView) findViewById(R.id.imageView_pan_img);

        spinner_addproof = (Spinner) findViewById(R.id.spinner_addproof);
        spinner_idproof = (Spinner) findViewById(R.id.spinner_idproof);

        adapter_address = new ArrayAdapter<String>(this, R.layout.address_list, add_proof);
        spinner_addproof.setAdapter(adapter_address);
        spinner_addproof.setEnabled(false);

        adapter_id = new ArrayAdapter<String>(this, R.layout.id_list, id_proof);
        spinner_idproof.setAdapter(adapter_id);
        spinner_idproof.setEnabled(false);

        bt_save.setOnClickListener(this);
        imageView_edit.setOnClickListener(this);
        imageView_cus_edit.setOnClickListener(this);
        imageView_address_edit.setOnClickListener(this);
        imageView_id_edit.setOnClickListener(this);
        imageView_gstpan_edit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_save:
                showPopup();
                writeLog("all data succcessfully saved from NewCustomerEntryDetailFormActivity:");
                break;

            case R.id.imageView_edit:
                Intent i = new Intent(NewCustomerEntryDetailFormActivity.this, NewCustomerEntryActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.enter, R.anim.exit);
                writeLog("activity goes to NewCustomerEntryActivity :");
                finish();
                break;

            case R.id.imageView_cus_edit:
                Intent in = new Intent(NewCustomerEntryDetailFormActivity.this, AttachCustomerImage.class);
                startActivity(in);
                overridePendingTransition(R.anim.enter, R.anim.exit);
                writeLog("activity goes to AttachCustomerImage :");
                finish();
                break;

            case R.id.imageView_address_edit:
                Intent intent_ = new Intent(NewCustomerEntryDetailFormActivity.this, AttachAddressProofImage.class);
                startActivity(intent_);
                overridePendingTransition(R.anim.enter, R.anim.exit);
                writeLog("activity goes to AttachAddressProofImage :");
                finish();
                break;

            case R.id.imageView_id_edit:
                Intent k = new Intent(NewCustomerEntryDetailFormActivity.this, AttachIdProofImageActivity.class);
                startActivity(k);
                overridePendingTransition(R.anim.enter, R.anim.exit);
                writeLog("activity goes to AttachIdProofImageActivity :");
                finish();
                break;

            case R.id.imageView_gstpan_edit:
                Intent intent = new Intent(NewCustomerEntryDetailFormActivity.this, AttachGSTnoPANnoImageActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.enter, R.anim.exit);
                writeLog("activity goes to AttachGSTnoPANnoImageActivity:");
                finish();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        showPopup();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //new Constant(NewCustomerEntryDetailFormActivity.this).doFinish();
                showPopup();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void set_value_newCusEntry() {
        NewCustomerEntryActivity.flag = 0;
        Constant.showLog("cus_name: " + OptionsActivity.new_cus.getCust_name());
        ed_cus_name.setText(OptionsActivity.new_cus.getCust_name());
        ed_mobile_no.setText(OptionsActivity.new_cus.getMobile_no());
        ed_email_id.setText(OptionsActivity.new_cus.getEmail_id());
        ed_address.setText(OptionsActivity.new_cus.getAddress());
    }

    private void set_value_attachCusImage() {
        AttachCustomerImage.flag = 0;
        String filename = OptionsActivity.new_cus.getCus_image();
        Constant.showLog("filename: " + OptionsActivity.new_cus.getCus_image());

        File file = Constant.checkFolder(Constant.folder_name);
        File fileArray[] = file.listFiles();

        if (fileArray.length != 0) {
            for (File f : fileArray) {
                if (f.getName().equals(filename)) {
                    if (f.length() != 0) {
                        String _imagePath = getRealPathFromURI(Environment.getExternalStorageDirectory() + File.separator + Constant.folder_name + File.separator + filename);
                        imageView_cus_image.setImageBitmap(scaleBitmap(_imagePath));
                        writeLog("imageView_cus_image is disply to form activity:");
                    }
                    break;
                }
            }
        }
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
        try {
            int inWidth, inHeight;
            InputStream in;
            in = new FileInputStream(imagePath);
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

        }
        return resizedBitmap;
    }

    private void set_value_attachAddressProof() {
        try {
            String address_proof = OptionsActivity.new_cus.getAddress_proof();
            int position = Integer.parseInt(address_proof);
            Constant.showLog("val: " + position);
            spinner_addproof.setSelection(position);
            spinner_addproof.setSelection(Integer.parseInt(OptionsActivity.new_cus.getAddress_proof()));
        } catch (NumberFormatException e) {
            e.printStackTrace();
            writeLog("NumberFormatException" + e);
            Toast toast = Toast.makeText(getApplicationContext(), "NumberFormatException", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
    }

    private void set_value_attachAddressProofImage() {
        AttachAddressProofImage.flag = 0;
        String filename = OptionsActivity.new_cus.getAddress_proof_image();
        Constant.showLog("filename: " + OptionsActivity.new_cus.getAddress_proof_image());

        File file = Constant.checkFolder(Constant.folder_name);
        File fileArray[] = file.listFiles();

        if (fileArray.length != 0) {
            for (File f : fileArray) {
                if (f.getName().equals(filename)) {
                    if (f.length() != 0) {
                        String _imagePath = getRealPathFromURI(Environment.getExternalStorageDirectory() + File.separator + Constant.folder_name + File.separator + filename);
                        imageView_addproof.setImageBitmap(scaleBitmap(_imagePath));
                        writeLog("imageView_addproof is disply to form activity:");
                    }
                    break;
                }
            }
        }
    }

    private void set_value_attachIdProof() {
        String id_proof = OptionsActivity.new_cus.getId_proof();
        int position = Integer.parseInt(id_proof);
        Constant.showLog("val: " + position);
        spinner_idproof.setSelection(position);
    }

    private void set_value_attachIdProofImage() {
        AttachIdProofImageActivity.flag = 0;
        String filename = OptionsActivity.new_cus.getId_proof_image();
        Constant.showLog("filename: " + OptionsActivity.new_cus.getId_proof_image());

        File file = Constant.checkFolder(Constant.folder_name);
        File fileArray[] = file.listFiles();

        if (fileArray.length != 0) {
            for (File f : fileArray) {
                if (f.getName().equals(filename)) {
                    if (f.length() != 0) {
                        String _imagePath = getRealPathFromURI(Environment.getExternalStorageDirectory() + File.separator + Constant.folder_name + File.separator + filename);
                        imageView_idproof.setImageBitmap(scaleBitmap(_imagePath));
                        writeLog("imageView_idproof is disply to form activity:");
                    }
                    break;
                }
            }
        }
    }

    private void set_value_attachGstProofImage() {
        //AttachIdProofImageActivity.flag = 0;
        String filename = OptionsActivity.new_cus.getGst_no_image();
        Constant.showLog("filename: " + filename);

        File file = Constant.checkFolder(Constant.folder_name);
        File fileArray[] = file.listFiles();

        if (fileArray.length != 0) {
            for (File f : fileArray) {
                if (f.getName().equals(filename)) {
                    if (f.length() != 0) {
                        String _imagePath = getRealPathFromURI(Environment.getExternalStorageDirectory() + File.separator + Constant.folder_name + File.separator + filename);
                        imageView_gst_img.setImageBitmap(scaleBitmap(_imagePath));
                        writeLog("imageView_gst_img is disply to form activity:");
                    }
                    break;
                }
            }
        }
    }

    private void set_value_attachPanProofImage() {
        //AttachIdProofImageActivity.flag = 0;
        String filename = OptionsActivity.new_cus.getPan_no_image();
        Constant.showLog("filename: " + filename);
        writeLog("filename:" + filename);


        File file = Constant.checkFolder(Constant.folder_name);
        File fileArray[] = file.listFiles();

        if (fileArray.length != 0) {
            for (File f : fileArray) {
                if (f.getName().equals(filename)) {
                    if (f.length() != 0) {
                        String _imagePath = getRealPathFromURI(Environment.getExternalStorageDirectory() + File.separator + Constant.folder_name + File.separator + filename);
                        imageView_pan_img.setImageBitmap(scaleBitmap(_imagePath));
                        writeLog("imageView_pan_img is disply to form activity:");
                    }
                    break;
                }
            }
        }
    }

    private void set_value_attachgstpan_no() {
        AttachGSTnoPANnoImageActivity.flag = 0;
        if (AttachGSTnoPANnoImageActivity.radio_flag == 1) {

            gst_lay.setVisibility(View.VISIBLE);
            pan_lay.setVisibility(View.GONE);
            rdo_gst.setChecked(true);
            rdo_pan.setChecked(false);
            String gst = OptionsActivity.new_cus.getGst_no();
            Constant.showLog("gst: " + gst);
            ed_gstno.setText(gst);
            set_value_attachGstProofImage();
        } else if (AttachGSTnoPANnoImageActivity.radio_flag == 2) {
            gst_lay.setVisibility(View.GONE);
            pan_lay.setVisibility(View.VISIBLE);
            rdo_pan.setChecked(true);
            rdo_gst.setChecked(false);
            String pan = OptionsActivity.new_cus.getPan_no();
            Constant.showLog("pan_no: " + pan);
            ed_panno.setText(pan);
            set_value_attachPanProofImage();
        }
    }

    private void saveData() {
        try {
            String url = "";
            constant = new Constant(NewCustomerEntryDetailFormActivity.this);
            constant.showPD();
            String _cust_name = URLEncoder.encode(OptionsActivity.new_cus.getCust_name(), "UTF-8");
            String _mob_no = URLEncoder.encode(OptionsActivity.new_cus.getMobile_no(), "UTF-8");
            String _email_id = URLEncoder.encode(OptionsActivity.new_cus.getEmail_id(), "UTF-8");
            String _address = URLEncoder.encode(OptionsActivity.new_cus.getAddress(), "UTF-8");
            String _cust_img = URLEncoder.encode(OptionsActivity.new_cus.getCus_image(), "UTF-8");
            String _address_proof = URLEncoder.encode(OptionsActivity.new_cus.getAddress_proof(), "UTF-8");
            String _address_proof_img = URLEncoder.encode(OptionsActivity.new_cus.getAddress_proof_image(), "UTF-8");
            String _id_proof = URLEncoder.encode(OptionsActivity.new_cus.getId_proof(), "UTF-8");
            String _id_proof_img = URLEncoder.encode(OptionsActivity.new_cus.getId_proof_image(), "UTF-8");
            String _gst_no = URLEncoder.encode(OptionsActivity.new_cus.getGst_no(), "UTF-8");
            String _gstno_img = URLEncoder.encode(OptionsActivity.new_cus.getGst_no_image(), "UTF-8");
            String _pan_no = URLEncoder.encode(OptionsActivity.new_cus.getPan_no(), "UTF-8");
            String _panno_img = URLEncoder.encode(OptionsActivity.new_cus.getPan_no_image(), "UTF-8");

            String custId = "", BranchId = "", District = "", Taluka = "", CityId = "", AreaId = "", HOCode = "";
            Cursor cursor = db.getUserDetails();
            if (cursor.moveToFirst()) {
                do {
                    custId = cursor.getString(cursor.getColumnIndex("CustId"));
                    BranchId = cursor.getString(cursor.getColumnIndex("BranchId"));
                    District = cursor.getString(cursor.getColumnIndex("District"));
                    Taluka = cursor.getString(cursor.getColumnIndex("Taluka"));
                    CityId = cursor.getString(cursor.getColumnIndex("CityId"));
                    AreaId = cursor.getString(cursor.getColumnIndex("AreaId"));
                    HOCode = cursor.getString(cursor.getColumnIndex("HOCode"));
                } while (cursor.moveToNext());
            }
            cursor.close();
            String _custId = URLEncoder.encode(custId, "UTF-8");
            String _BranchId = URLEncoder.encode(BranchId, "UTF-8");
            String _District = URLEncoder.encode(District, "UTF-8");
            String _Taluka = URLEncoder.encode(Taluka, "UTF-8");
            String _CityId = URLEncoder.encode(CityId, "UTF-8");
            String _AreaId = URLEncoder.encode(AreaId, "UTF-8");
            String _HOCode = URLEncoder.encode(HOCode, "UTF-8");
            String data = OptionsActivity.new_cus.getId_addressproof()+"-"+OptionsActivity.new_cus.getAddress_proof_image()+","+ OptionsActivity.new_cus.getId_idproof()+"-"+OptionsActivity.new_cus.getId_proof_image()+","+OptionsActivity.new_cus.getId_gstpan_proof()+"-"+OptionsActivity.new_cus.getGstpan_img();

            /*if(AttachGSTnoPANnoImageActivity.radio_flag == 1) {
                url = Constant.ipaddress + "/SaveCustomerDetail?custname="+_cust_name+"&mobno="+_mob_no+"&email="+_email_id+"&address="+_address+"&custimg="+_cust_img+"&addressproof="+_address_proof+"&addressproofimg="+_address_proof_img+"&idproof="+_id_proof+"&idproofimg="+_id_proof_img+"&GSTINNo="+_gst_no+"&GSTINimg="+_gstno_img;
                Constant.showLog(url);
                writeLog("saveData():url called" + url);
            }else if(AttachGSTnoPANnoImageActivity.radio_flag == 2) {
                url = Constant.ipaddress + "/SaveCustomerDetail?custname="+_cust_name+"&mobno="+_mob_no+"&email="+_email_id+"&address="+_address+"&custimg="+_cust_img+"&addressproof="+_address_proof+"&addressproofimg="+_address_proof_img+"&idproof="+_id_proof+"&idproofimg="+_id_proof_img+"&PANNo="+_pan_no+"&PANimg="+_panno_img;
                Constant.showLog(url);
                writeLog("saveData():url called" + url);
            }*/


            url = Constant.ipaddress + "/SaveCustomerDetail?custname=" + _cust_name + "&mobno=" + _mob_no + "&email=" + _email_id + "&address=" + _address + "&custimg="
                    + _cust_img + "&addressproof=" + _address_proof + "&addressproofimg=" + _address_proof_img + "&idproof=" + _id_proof + "&idproofimg=" + _id_proof_img + "&GSTINNo=" + _gst_no + "&GSTINimg=" + _gstno_img + "&PANNo=" + _pan_no + "&PANimg=" + _panno_img
                    + "&custid=" + _custId + "&Branchid=" + _BranchId + "&district=" + _District + "&taluka=" + _Taluka + "&cityid=" + _CityId + "&areaid=" + _AreaId + "&HOCode=" + _HOCode + "&data="+data;
            Constant.showLog(url);
            writeLog("saveData():url called" + url);

            VolleyRequests requests = new VolleyRequests(NewCustomerEntryDetailFormActivity.this);
            requests.saveCustomerDetail(url, new ServerCallback() {
                @Override
                public void onSuccess(String result) {
                    constant.showPD();
                    Constant.showLog("Volly request success");
                    writeLog("saveData():Volley_success");
                }

                @Override
                public void onFailure(String result) {
                    constant.showPD();
                    writeLog("saveData():Volley_error");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            writeLog("saveData():customer data save exception");
        }
    }

    public void showPopup() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you want to save data?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                saveData();
                Toast toast = Toast.makeText(getApplicationContext(), "Data saved successfully..", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();

                Intent intent1 = new Intent("test");//UploadImageService.BROADCAST
                sendBroadcast(intent1);
                writeLog("UploadImageService_onHandleIntent_broadcastSend");

                NewCustomerEntryActivity.flag = 1;
                AttachCustomerImage.flag = 2;
                AttachAddressProofImage.flag = 3;
                AttachIdProofImageActivity.flag = 4;
                AttachGSTnoPANnoImageActivity.flag = 5;
                Intent in = new Intent(NewCustomerEntryDetailFormActivity.this, OptionsActivity.class);
                startActivity(in);
                new Constant(NewCustomerEntryDetailFormActivity.this).doFinish();
                //finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        builder.create().show();
    }

    private void writeLog(String _data) {
        new WriteLog().writeLog(getApplicationContext(), "NewCustomerEntryDetailFormActivity_" + _data);
    }


}


