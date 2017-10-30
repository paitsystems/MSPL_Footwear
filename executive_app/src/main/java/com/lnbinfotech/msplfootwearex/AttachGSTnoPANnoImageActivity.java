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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;

import com.lnbinfotech.msplfootwearex.constant.Constant;
import com.lnbinfotech.msplfootwearex.db.DBHandler;
import com.lnbinfotech.msplfootwearex.log.WriteLog;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AttachGSTnoPANnoImageActivity extends AppCompatActivity implements View.OnClickListener {
    private RadioButton rdo_gst, rdo_pan;
    private LinearLayout gst_lay, pan_lay;
    private AppCompatButton bt_next, bt_update, bt_cancel;
    private LinearLayout save_lay, update_lay;
    private ImageView imageView_pan_img, imageView_gst_img;
    private EditText ed_gstno, ed_panno;
    //private Bitmap bmp;
    private String imagePath;
    private int _flag = 1;
    public static int radio_flag = 1;
    public static int flag = 5;
    private Toast toast;
    private final int requestCode = 1;
    private DBHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attach_gst_pan_image);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.newcustomerentry);
        }
        init();
    }

    private void init() {
        db = new DBHandler(AttachGSTnoPANnoImageActivity.this);
        toast = Toast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        rdo_gst = (RadioButton) findViewById(R.id.rdo_gstno);
        rdo_pan = (RadioButton) findViewById(R.id.rdo_panno);
        gst_lay = (LinearLayout) findViewById(R.id.gst_lay);
        pan_lay = (LinearLayout) findViewById(R.id.pan_lay);
        bt_next = (AppCompatButton) findViewById(R.id.btn_next);
        ed_gstno = (EditText) findViewById(R.id.ed_gstno);
        ed_panno = (EditText) findViewById(R.id.ed_panno);
        bt_cancel = (AppCompatButton) findViewById(R.id.btn_cancel);
        bt_update = (AppCompatButton) findViewById(R.id.btn_update);
        save_lay = (LinearLayout) findViewById(R.id.save_lay);
        update_lay = (LinearLayout) findViewById(R.id.update_lay);
        imageView_gst_img = (ImageView) findViewById(R.id.imageView_gst_img);
        imageView_pan_img = (ImageView) findViewById(R.id.imageView_pan_img);

        if (flag == 0) {
            save_lay.setVisibility(View.GONE);
            update_lay.setVisibility(View.VISIBLE);
            set_value_attachgstpan_no();
        } else {
            save_lay.setVisibility(View.VISIBLE);
            update_lay.setVisibility(View.GONE);
        }
        bt_update.setOnClickListener(this);
        bt_cancel.setOnClickListener(this);

        rdo_gst.setOnClickListener(this);
        rdo_pan.setOnClickListener(this);

        imageView_pan_img.setOnClickListener(this);
        imageView_gst_img.setOnClickListener(this);
        bt_next.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_next:
                gstpan_no_value();
                setIdValue();
                String filename = "";
                if (_flag == 0) {
                    OptionsActivity.new_cus.setPan_no_image(imagePath);
                    OptionsActivity.new_cus.setGst_no_image("NA");
                    OptionsActivity.new_cus.setGstpan_img(imagePath);
                    filename = OptionsActivity.new_cus.getPan_no_image();
                } else if (_flag == 1) {
                    OptionsActivity.new_cus.setPan_no_image("NA");
                    OptionsActivity.new_cus.setGst_no_image(imagePath);
                    OptionsActivity.new_cus.setGstpan_img(imagePath);
                    filename = OptionsActivity.new_cus.getGst_no_image();
                }

                if (filename == null) {
                    toast.setText("Please, attach image.");
                    toast.show();
                } else {
                    Intent k = new Intent(AttachGSTnoPANnoImageActivity.this, NewCustomerEntryDetailFormActivity.class);
                    startActivity(k);
                    overridePendingTransition(R.anim.enter, R.anim.exit);
                    writeLog("Next button of onclick():data saved and goes to DetailFormActivity ");
                    finish();
                }
                break;
            case R.id.btn_update:
                gstpan_no_value();
                setIdValue();
                if (_flag == 0) {
                    OptionsActivity.new_cus.setPan_no_image(imagePath);
                    OptionsActivity.new_cus.setGstpan_img(imagePath);
                    //OptionsActivity.new_cus.setGst_no_image(gst);
                    Log.d("Log","imagePath:"+imagePath);
                } else if (_flag == 1) {
                    OptionsActivity.new_cus.setGst_no_image(imagePath);
                    OptionsActivity.new_cus.setGstpan_img(imagePath);
                   // OptionsActivity.new_cus.setPan_no_image(pan);
                    Log.d("Log","imagePath:"+imagePath);
                }
                Intent intent = new Intent(AttachGSTnoPANnoImageActivity.this, NewCustomerEntryDetailFormActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.enter, R.anim.exit);
                writeLog("Update button of onclick():data updated and goes to DetailFormActivity ");
                finish();
                break;
            case R.id.btn_cancel:
                Intent j = new Intent(AttachGSTnoPANnoImageActivity.this, NewCustomerEntryDetailFormActivity.class);
                startActivity(j);
                overridePendingTransition(R.anim.enter, R.anim.exit);
                writeLog("Cancel button of onclick():data canceled and goes to DetailFormActivity ");
                finish();
                break;
            case R.id.imageView_gst_img:
                Intent intent_ = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                File f = Constant.checkFolder(Constant.folder_name);
                f = new File(f.getAbsolutePath(), "temp.jpg");
                intent_.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                startActivityForResult(intent_, requestCode);
                break;
            case R.id.imageView_pan_img:
                Intent in = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                File file = Constant.checkFolder(Constant.folder_name);
                f = new File(file.getAbsolutePath(), "temp.jpg");
                in.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                startActivityForResult(in, requestCode);
                break;
            case R.id.rdo_gstno:
                radio_flag = 1;
                _flag = 1;
                rdo_pan.setChecked(false);
                gst_lay.setVisibility(View.VISIBLE);
                pan_lay.setVisibility(View.GONE);
                break;
            case R.id.rdo_panno:
                radio_flag = 2;
                _flag = 0;
                rdo_gst.setChecked(false);
                gst_lay.setVisibility(View.GONE);
                pan_lay.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        showPopup();
        /*Intent j = new Intent(AttachGSTnoPANnoImageActivity.this, NewCustomerEntryDetailFormActivity.class);
        startActivity(j);
        writeLog("Cancel button of onclick():data canceled and goes to DetailFormActivity ");
        finish();*/

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //new Constant(AttachGSTnoPANnoImageActivity.this).doFinish();
                showPopup();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (this.requestCode == requestCode && resultCode == RESULT_OK) {
            try {
                String _imagePath = getRealPathFromURI(Environment.getExternalStorageDirectory() + File.separator + Constant.folder_name + File.separator + "temp.jpg");
               if(flag == 0) {
                   if (_flag == 1) {
                       imageView_gst_img.setVisibility(View.VISIBLE);
                       //OptionsActivity.new_cus.setGst_no_image(null);
                       imageView_gst_img.setImageBitmap(scaleBitmap(_imagePath));
                   } else if (_flag == 0) {
                       imageView_pan_img.setVisibility(View.VISIBLE);
                       //OptionsActivity.new_cus.setPan_no_image(null);
                       imageView_pan_img.setImageBitmap(scaleBitmap(_imagePath));
                   }
               }else {
                    if (_flag == 1) {
                        imageView_gst_img.setVisibility(View.VISIBLE);
                        imageView_gst_img.setImageBitmap(scaleBitmap(_imagePath));
                    } else if (_flag == 0) {
                        imageView_pan_img.setVisibility(View.VISIBLE);
                        imageView_pan_img.setImageBitmap(scaleBitmap(_imagePath));
                    }
                }
                long datetime = System.currentTimeMillis();
                SimpleDateFormat sdf = new SimpleDateFormat("dd_MMM_yyyy_HH_mm_ss", Locale.ENGLISH);
                Date resultdate = new Date(datetime);

                imagePath = "GP_Img_" + sdf.format(resultdate) + ".jpg";

                File f = new File(Environment.getExternalStorageDirectory() + File.separator + Constant.folder_name);
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
                File file = new File(Environment.getExternalStorageDirectory() + File.separator + Constant.folder_name, imagePath);
                try {
                    outFile = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 15, outFile);
                    outFile.flush();
                    outFile.close();
                } catch (Exception e) {
                    writeLog("onActivityResult():FileNotFoundException:" + e);
                    //writeLog("AddNewTicketActivity_onActivityResult_outFile_"+e.getMessage());
                    e.printStackTrace();
                }
            } catch (Exception e) {
                writeLog("onActivityResult():Exception:" + e);
                //writeLog("AddNewTicketActivity_onActivityResult_"+e.getMessage());
                e.printStackTrace();
            }
        }

    }

    /*private void store_CameraPhoto_InSdCard(Bitmap bitmap,String currentdate){
        File file = new File(Environment.getExternalStorageDirectory() + File.separator + Constant.captured_images_folder+File.separator + "gp_img_"+currentdate+".jpg");
        //File file = new File(Environment.getExternalStorageDirectory() + "img_"+currentdate+".jpeg");

        Log.d("Log","File path:"+file);
        try{

            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 15, fos);
            fos.flush();
            fos.close();
        }catch (Exception f){
            f.printStackTrace();
            writeLog("FileNotFoundException and IOException found:"+f);
        }
    }
    private Bitmap get_Image_from_sd_card(String filename){
        Bitmap bitmap = null;
        File imgfile = new File(Environment.getExternalStorageDirectory() + File.separator + Constant.captured_images_folder+File.separator + filename);

        try {
            FileInputStream fis = new FileInputStream(imgfile);
            bitmap  = BitmapFactory.decodeStream(fis);
        }catch (FileNotFoundException e){
            e.printStackTrace();
            writeLog("FileNotFoundException:"+e);
        }
        return bitmap;
    }

    private String currentDateFormat(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HH_mm");
        String current_time = sdf.format(new Date());
        return current_time;
    }*/

    private void setIdValue(){
        String value = "";
        if (_flag == 0) {
            value = "PAN CARD";
            Constant.showLog("setIdValue():panvalue:"+value);
        } else if (_flag == 1) {
            value = "GSTIN";
            Constant.showLog("setIdValue():gstvalue:"+value);
        }
        Cursor cursor =  db.getIdOfDocType(value);
        if(cursor.moveToFirst()){
            do{
                OptionsActivity.new_cus.setId_gstpan_proof(cursor.getString(cursor.getColumnIndex(DBHandler.Document_Id)));
            }while (cursor.moveToNext());
        }
        cursor.close();
    }

    private void gstpan_no_value() {
        if (radio_flag == 1) {
            String gst_no = ed_gstno.getText().toString();
            Constant.showLog("gst_no: " + gst_no);
            OptionsActivity.new_cus.setGst_no(gst_no);
            OptionsActivity.new_cus.setPan_no("NA");
        } else if (radio_flag == 2) {
            String pan_no = ed_panno.getText().toString();
            Constant.showLog("pan_no: " + pan_no);
            OptionsActivity.new_cus.setGst_no("NA");
            OptionsActivity.new_cus.setPan_no(pan_no);
        }
    }

    private void set_value_attachgstpan_no() {
        if (radio_flag == 1) {
            gst_lay.setVisibility(View.VISIBLE);
            pan_lay.setVisibility(View.GONE);
            rdo_gst.setChecked(true);
            rdo_pan.setChecked(false);
            String gst = OptionsActivity.new_cus.getGst_no();
            Constant.showLog("gst: " + gst);
            ed_gstno.setText(gst);
            set_value_attachGstProofImage();
        } else if (radio_flag == 2) {
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
                        writeLog("set_value_attachGstProofImage():imageView_gst_img is display's to form activity:");
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

        File file = Constant.checkFolder(Constant.folder_name);
        File fileArray[] = file.listFiles();

        if (fileArray.length != 0) {
            for (File f : fileArray) {
                if (f.getName().equals(filename)) {
                    if (f.length() != 0) {
                        String _imagePath = getRealPathFromURI(Environment.getExternalStorageDirectory() + File.separator + Constant.folder_name + File.separator + filename);
                        imageView_pan_img.setImageBitmap(scaleBitmap(_imagePath));
                        writeLog("set_value_attachPanProofImage():imageView_pan_img is display's to form activity:");
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
            writeLog("scaleBitmap():FileNotFoundException and IOException found:" + e);
        }
        return resizedBitmap;
    }

    private void showPopup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you want to clear this data");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent in = new Intent(AttachGSTnoPANnoImageActivity.this, OptionsActivity.class);
                OptionsActivity.new_cus = null;
                startActivity(in);
                new Constant(AttachGSTnoPANnoImageActivity.this).doFinish();
                // finish();
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
        new WriteLog().writeLog(getApplicationContext(), "AttachGSTnoPANnoImageActivity_" + _data);
    }


}


