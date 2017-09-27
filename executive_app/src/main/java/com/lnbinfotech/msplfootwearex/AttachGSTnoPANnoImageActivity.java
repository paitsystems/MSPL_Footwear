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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import com.lnbinfotech.msplfootwearex.constant.Constant;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AttachGSTnoPANnoImageActivity extends AppCompatActivity {
    private RadioButton rdo_gst,rdo_pan;
    private LinearLayout gst_lay,pan_lay;
    private Button bt_next;
    private ImageView imageView_pan_img,imageView_gst_img;
    private EditText ed_gstno,ed_panno;
    private Bitmap bmp;
    private int _flag;
    static  int radio_flag = 1;
    static int flag = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attach_gst_pan_image);
        init();

    }
    private void init(){
        rdo_gst = (RadioButton) findViewById(R.id.rdo_gstno);
        rdo_pan = (RadioButton) findViewById(R.id.rdo_panno);
        gst_lay = (LinearLayout) findViewById(R.id.gst_lay);
        pan_lay = (LinearLayout) findViewById(R.id.pan_lay);
        bt_next = (Button) findViewById(R.id.btn_next);
        ed_gstno = (EditText) findViewById(R.id.ed_gstno);
        ed_panno = (EditText) findViewById(R.id.ed_panno);
        imageView_gst_img = (ImageView) findViewById(R.id.imageView_gst_img);
        imageView_pan_img = (ImageView) findViewById(R.id.imageView_pan_img);

        if(flag == 0) {
            set_value_attachgstpan_no();
        }

            rdo_gst.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    radio_flag = 1;
                    rdo_pan.setChecked(false);
                    gst_lay.setVisibility(View.VISIBLE);
                    pan_lay.setVisibility(View.GONE);
                }
            });
            rdo_pan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    radio_flag = 2;
                    rdo_gst.setChecked(false);
                    gst_lay.setVisibility(View.GONE);
                    pan_lay.setVisibility(View.VISIBLE);
                }
            });


            imageView_pan_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    _flag = 0;
                    // imageView_gst_img.setVisibility(View.GONE);
                    showPopup(0);
                }
            });

            imageView_gst_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    _flag = 1;
                    showPopup(0);

                }
            });

            bt_next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    gstpan_no_value();
                    Intent i = new Intent(AttachGSTnoPANnoImageActivity.this, NewCustomerEntryDetailFormActivity.class);
                    startActivity(i);
                    finish();
                }
            });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                    Constant.checkFolder(Constant.captured_images_folder);
                    String dateformat = currentDateFormat();
                    String file_name = "gp_img_" + dateformat + ".jpg";
                    store_CameraPhoto_InSdCard(bitmap, dateformat);
                    Bitmap mbitmap = get_Image_from_sd_card(file_name);
                    Log.d("Log", "imgename:" + mbitmap);
                    if(_flag == 0){
                        imageView_pan_img.setImageBitmap(mbitmap);
                        OptionsActivity.new_cus.setPan_no_image(file_name);
                    }else if (_flag == 1){
                        imageView_gst_img.setImageBitmap(mbitmap);
                        OptionsActivity.new_cus.setGst_no_image(file_name);
                    }
                }
                break;
            case 2:
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

                    //imgv_img1.setBackgroundResource(0);

                    if(_flag == 0){
                        imageView_pan_img.setImageBitmap(bmp);
                    }else if (_flag == 1){
                        imageView_gst_img.setImageBitmap(bmp);
                    }

                } else {
                    Log.d("Status:", "Photopicker canceled");
                }
                break;


        }
    }

    private void store_CameraPhoto_InSdCard(Bitmap bitmap,String currentdate){
        File file = new File(Environment.getExternalStorageDirectory() + File.separator + Constant.captured_images_folder+File.separator + "gp_img_"+currentdate+".jpg");
        //File file = new File(Environment.getExternalStorageDirectory() + "img_"+currentdate+".jpeg");

        Log.d("Log","File path:"+file);
        try{

            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 15, fos);
            fos.flush();
            fos.close();
        }catch (FileNotFoundException f){
            f.printStackTrace();
        }catch (IOException io){
            io.printStackTrace();
        }catch (NullPointerException w){
            w.printStackTrace();
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
        }
        return bitmap;
    }

    private String currentDateFormat(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HH_mm");
        String current_time = sdf.format(new Date());
        return current_time;
    }

    private void gstpan_no_value(){
        if(radio_flag == 1){
            String gst_no = ed_gstno.getText().toString();
            Log.d("Log","gst_no: "+gst_no);
            OptionsActivity.new_cus.setGst_no(gst_no);
        }else if(radio_flag == 2){
            String pan_no = ed_panno.getText().toString();
            Log.d("Log","pan_no: "+pan_no);
            OptionsActivity.new_cus.setPan_no(pan_no);
        }
    }

    private void set_value_attachgstpan_no(){


        if(radio_flag == 1){

            gst_lay.setVisibility(View.VISIBLE);
            pan_lay.setVisibility(View.GONE);
            rdo_gst.setChecked(true);
            rdo_pan.setChecked(false);
            String gst = OptionsActivity.new_cus.getGst_no();
            Log.d("Log","gst: "+gst);
            ed_gstno.setText(gst);
            set_value_attachGstProofImage();
        }else if(radio_flag == 2){

            gst_lay.setVisibility(View.GONE);
            pan_lay.setVisibility(View.VISIBLE);
            rdo_pan.setChecked(true);
            rdo_gst.setChecked(false);
            String pan = OptionsActivity.new_cus.getPan_no();
            Log.d("Log","pan_no: "+pan);
            ed_panno.setText(pan);
            set_value_attachPanProofImage();
        }
    }

    private void set_value_attachGstProofImage(){
        //AttachIdProofImageActivity.flag = 0;
        String filename = OptionsActivity.new_cus.getGst_no_image();
        Log.d("Log","filename: "+filename);

        File file = Constant.checkFolder(Constant.captured_images_folder);
        File fileArray[] = file.listFiles();

        if (fileArray.length != 0) {
            for (File f : fileArray) {
                if (f.getName().equals(filename)) {
                    if (f.length() != 0) {
                        String _imagePath = getRealPathFromURI( Environment.getExternalStorageDirectory() + File.separator + Constant.captured_images_folder+File.separator + filename);
                        imageView_gst_img.setImageBitmap(scaleBitmap(_imagePath));
                    }
                    break;
                }
            }
        }
    }

    private void set_value_attachPanProofImage(){
        //AttachIdProofImageActivity.flag = 0;
        String filename = OptionsActivity.new_cus.getPan_no_image();
        Log.d("Log","filename: "+filename);

        File file = Constant.checkFolder(Constant.captured_images_folder);
        File fileArray[] = file.listFiles();

        if (fileArray.length != 0) {
            for (File f : fileArray) {
                if (f.getName().equals(filename)) {
                    if (f.length() != 0) {
                        String _imagePath = getRealPathFromURI( Environment.getExternalStorageDirectory() + File.separator + Constant.captured_images_folder+File.separator + filename);
                        imageView_pan_img.setImageBitmap(scaleBitmap(_imagePath));
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

    public Bitmap scaleBitmap(String imagePath) {
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

    private void showPopup(int id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you want to attach image?");
        if (id == 0) {
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    showPopup(1);
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
        }else if (id == 1) {
            builder.setMessage("Attach image");
            builder.setPositiveButton("Camera", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent1 = new Intent( MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent1,1);

                }
            });
            builder.setNegativeButton("Gallery", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent2 = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent2,2);
                }
            });
        }
        builder.create().show();
    }
}


