package com.lnbinfotech.msplfootwearex;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.lnbinfotech.msplfootwearex.constant.Constant;
import com.lnbinfotech.msplfootwearex.db.DBHandler;
import com.lnbinfotech.msplfootwearex.log.WriteLog;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AttachAddressProofImage extends AppCompatActivity implements View.OnClickListener {
    private ImageView imageView_addproof;
    //private Bitmap bmp;
    //private int position;
    //private String file_name;
    private String imagePath;
    private AppCompatButton bt_next, bt_update, bt_cancel;
    private LinearLayout save_lay, update_lay;
    private Spinner spinner_addproof;
    private ArrayAdapter<String> adapter_address;
    public static int flag = 3;
    private Toast toast;
    private final int requestCode = 1;
    private DBHandler db;
    private List<String> doc_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(Constant.liveTestFlag==1) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }

        setContentView(R.layout.activity_attach_address_proof_image);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setTitle(R.string.newcustomerentry);
        }
        init();
    }

    private void init() {
        doc_list = new ArrayList<>();
        db = new DBHandler(AttachAddressProofImage.this);
        toast = Toast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        imageView_addproof = (ImageView) findViewById(R.id.imageView_addproof);
        bt_next = (AppCompatButton) findViewById(R.id.btn_next);
        bt_update = (AppCompatButton) findViewById(R.id.btn_update);
        bt_cancel = (AppCompatButton) findViewById(R.id.btn_cancel);
        save_lay = (LinearLayout) findViewById(R.id.save_lay);
        update_lay = (LinearLayout) findViewById(R.id.update_lay);
        spinner_addproof = (Spinner) findViewById(R.id.spinner_addproof);
        FirstActivity.pref = getSharedPreferences(FirstActivity.PREF_NAME,MODE_PRIVATE);
        setDocList();
        adapter_address = new ArrayAdapter<>(this, R.layout.address_list, doc_list);
        spinner_addproof.setAdapter(adapter_address);

        if (flag == 0) {
            save_lay.setVisibility(View.GONE);
            update_lay.setVisibility(View.VISIBLE);

            set_value_attachAddressProof();
            set_value_attachAddressProofImage();

            imageView_addproof.setOnClickListener(this);
            bt_update.setOnClickListener(this);
            bt_cancel.setOnClickListener(this);
        } else {
            save_lay.setVisibility(View.VISIBLE);
            update_lay.setVisibility(View.GONE);

            imageView_addproof.setOnClickListener(this);
            bt_next.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_next:
                int position = spinner_addproof.getSelectedItemPosition();
                Constant.showLog("positon:" + spinner_addproof.getSelectedItemPosition());
                OptionsActivity.new_cus.setAddress_proof(String.valueOf(position));
                setIdSpinnerValue();

                OptionsActivity.new_cus.setAddress_proof_image(imagePath);
                String filename = OptionsActivity.new_cus.getAddress_proof_image();
                if (filename == null) {
                    toast.setText("Please, attach address proof image.");
                    toast.show();
                } else {
                    Intent i = new Intent(AttachAddressProofImage.this, AttachIdProofImageActivity.class);
                    startActivity(i);
                    overridePendingTransition(R.anim.enter, R.anim.exit);
                    finish();
                }
                break;
            case R.id.btn_update:
                OptionsActivity.new_cus.setAddress_proof_image(imagePath);

                int position_ = spinner_addproof.getSelectedItemPosition();
                Constant.showLog("positon:" + spinner_addproof.getSelectedItemPosition());
                OptionsActivity.new_cus.setAddress_proof(String.valueOf(position_));
                setIdSpinnerValue();

                Intent intent = new Intent(AttachAddressProofImage.this, NewCustomerEntryDetailFormActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.enter, R.anim.exit);
                finish();
                break;
            case R.id.btn_cancel:
                Intent j = new Intent(AttachAddressProofImage.this, NewCustomerEntryDetailFormActivity.class);
                startActivity(j);
                overridePendingTransition(R.anim.enter, R.anim.exit);
                finish();
                break;
            case R.id.imageView_addproof:
                /*Intent intent_ = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                File f = Constant.checkFolder(Constant.folder_name);
                f = new File(f.getAbsolutePath(), "temp.jpg");
                intent_.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                startActivityForResult(intent_, requestCode);*/

                Intent intent_ = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                File f = Constant.checkFolder(Constant.folder_name);
                f = new File(f.getAbsolutePath(),"temp.jpg");
                Uri photoURI = FileProvider.getUriForFile(getApplicationContext(), getApplicationContext().getPackageName()
                        + ".provider", f);
                intent_.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                intent_.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivityForResult(intent_,requestCode);
                overridePendingTransition(R.anim.enter, R.anim.exit);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        showPopup();
       /* super.onBackPressed();
        Intent j = new Intent(AttachAddressProofImage.this, NewCustomerEntryDetailFormActivity.class);
        startActivity(j);
        writeLog("onBackPressed():data canceled and goes to DetailFormActivity ");
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
                //new Constant(AttachAddressProofImage.this).doFinish();
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
                imageView_addproof.setVisibility(View.VISIBLE);
                String _imagePath = getRealPathFromURI(Environment.getExternalStorageDirectory() + File.separator + Constant.folder_name + File.separator + "temp.jpg");
                imageView_addproof.setImageBitmap(scaleBitmap(_imagePath));
                long datetime = System.currentTimeMillis();
                SimpleDateFormat sdf = new SimpleDateFormat("dd_MMM_yyyy_HH_mm_ss", Locale.ENGLISH);
                Date resultdate = new Date(datetime);

                imagePath = "Address_Img_" + sdf.format(resultdate) + ".jpg";

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
                    writeLog("Exception1_" + e.getMessage());
                    e.printStackTrace();
                }
            } catch (Exception e) {
                writeLog("Exception_" + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    /*private void store_CameraPhoto_InSdCard(Bitmap bitmap,String currentdate){
        File file = new File(Environment.getExternalStorageDirectory() + File.separator + Constant.captured_images_folder+File.separator + "address_img_"+currentdate+".jpg");
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

    private void setDocList(){
        Cursor cursor = db.getDocName();
        if(cursor.moveToFirst()){
            do{
                doc_list.add(cursor.getString(cursor.getColumnIndex(DBHandler.Document_DocName)));
            }while (cursor.moveToNext());
        }
        cursor.close();
    }

    private void setIdSpinnerValue(){
        String value = spinner_addproof.getSelectedItem().toString();
        Constant.showLog("setIdSpinnerValue():spinner_addproofvalue"+value);
        Cursor cursor =  db.getIdOfDocType(value);

        if(cursor.moveToFirst()){
            do{
               OptionsActivity.new_cus.setId_addressproof(cursor.getString(cursor.getColumnIndex(DBHandler.Document_Id)));
            }while (cursor.moveToNext());
        }
        cursor.close();
    }

    private void set_value_attachAddressProof() {
        String address_proof = OptionsActivity.new_cus.getAddress_proof();
        int position = Integer.parseInt(address_proof);
        Log.d("Log", "val: " + position);
        spinner_addproof.setSelection(position);
    }

    private void set_value_attachAddressProofImage() {
        String filename = OptionsActivity.new_cus.getAddress_proof_image();
        Log.d("Log", "filename: " + OptionsActivity.new_cus.getAddress_proof_image());
        File file = Constant.checkFolder(Constant.folder_name);
        File fileArray[] = file.listFiles();

        if (fileArray.length != 0) {
            for (File f : fileArray) {
                if (f.getName().equals(filename)) {
                    if (f.length() != 0) {
                        String _imagePath = getRealPathFromURI(Environment.getExternalStorageDirectory() + File.separator + Constant.folder_name + File.separator + filename);
                        imageView_addproof.setImageBitmap(scaleBitmap(_imagePath));
                        writeLog("set_value_attachCusImage():imageView_addproof is attched:");
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
            writeLog("FileNotFoundException and IOException found:" + e);
        }
        return resizedBitmap;
    }

    private void showPopup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you want to clear this data");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent in = new Intent(AttachAddressProofImage.this, OptionsActivity.class);
                OptionsActivity.new_cus = null;
                startActivity(in);
                new Constant(AttachAddressProofImage.this).doFinish();
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
        new WriteLog().writeLog(getApplicationContext(), "AttachAddressProofImage_" + _data);
    }
}


