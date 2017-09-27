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
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;
import com.lnbinfotech.msplfootwearex.constant.Constant;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class NewCustomerEntryDetailFormActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    EditText ed_cus_name,ed_mobile_no,ed_email_id,ed_address,ed_gstno,ed_panno;
    Spinner spinner_addproof,spinner_idproof;
    String[] add_proof = {"Aadhaar","Light bill","Aggreement copy","Index 0"};
    String[] id_proof = {"Aadhaar","Pan card","Driving license","Election card"};
    ArrayAdapter<String> adapter_address;
    ArrayAdapter<String> adapter_id;
    RadioButton rdo_gst,rdo_pan;
    LinearLayout gst_lay,pan_lay;
    Button bt_save;
    ImageView imageView_edit,imageView_cus_edit,imageView_address_edit,imageView_id_edit,imageView_gstpan_edit,imageView_cus_image,imageView_addproof,imageView_idproof,imageView_pan_img,imageView_gst_img;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_customer_entry_detail_form);
        init();

        set_value_newCusEntry();
        set_value_attachCusImage();
        set_value_attachAddressProof();
        set_value_attachAddressProofImage();
        set_value_attachIdProof();
        set_value_attachIdProofImage();
        set_value_attachgstpan_no();
    }
    void init(){
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
        bt_save = (Button) findViewById(R.id.btn_save);

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

        adapter_address = new ArrayAdapter<String>(this,R.layout.address_list,add_proof);
        spinner_addproof.setAdapter(adapter_address);
        spinner_addproof.setEnabled(false);

        adapter_id = new ArrayAdapter<String>(this,R.layout.id_list,id_proof);
        spinner_idproof.setAdapter(adapter_id);
        spinner_idproof.setEnabled(false);


        imageView_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(NewCustomerEntryDetailFormActivity.this,NewCustomerEntryActivity.class);
                startActivity(i);
                finish();
            }
        });

        imageView_cus_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(NewCustomerEntryDetailFormActivity.this,AttachCustomerImage.class);
                startActivity(i);
                finish();
            }
        });

        imageView_address_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(NewCustomerEntryDetailFormActivity.this,AttachAddressProofImage.class);
                startActivity(i);
                finish();
            }
        });

        imageView_id_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(NewCustomerEntryDetailFormActivity.this,AttachIdProofImageActivity.class);
                startActivity(i);
                finish();
            }
        });

        imageView_gstpan_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(NewCustomerEntryDetailFormActivity.this,AttachGSTnoPANnoImageActivity.class);
                startActivity(i);
                finish();
            }
        });

        bt_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              showPopup();
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String item_add = spinner_addproof.getSelectedItem().toString();
        String item_id = spinner_idproof.getSelectedItem().toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void  set_value_newCusEntry(){
        NewCustomerEntryActivity.flag = 0;
        Log.d("Log","cus_name: "+OptionsActivity.new_cus.getCust_name());
        ed_cus_name.setText(OptionsActivity.new_cus.getCust_name());
        ed_mobile_no.setText(OptionsActivity.new_cus.getMobile_no());
        ed_email_id.setText(OptionsActivity.new_cus.getEmail_id());
        ed_address.setText(OptionsActivity.new_cus.getAddress());
    }

    private void set_value_attachCusImage() {
        AttachCustomerImage.flag = 0;
        String filename = OptionsActivity.new_cus.getCus_image();
        Log.d("Log","filename: "+OptionsActivity.new_cus.getCus_image());

        File file = Constant.checkFolder(Constant.captured_images_folder);
        File fileArray[] = file.listFiles();

        if (fileArray.length != 0) {
            for (File f : fileArray) {
                if (f.getName().equals(filename)) {
                    if (f.length() != 0) {
                        String _imagePath = getRealPathFromURI( Environment.getExternalStorageDirectory() + File.separator + Constant.captured_images_folder+File.separator + filename);
                        imageView_cus_image.setImageBitmap(scaleBitmap(_imagePath));
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

    private void set_value_attachAddressProof(){
        String address_proof = OptionsActivity.new_cus.getAddress_proof();
        int position = Integer.parseInt(address_proof);
        Log.d("Log","val: "+position);
        spinner_addproof.setSelection(position);
        spinner_addproof.setSelection(Integer.parseInt(OptionsActivity.new_cus.getAddress_proof()));
    }

    private void set_value_attachAddressProofImage(){
        AttachAddressProofImage.flag = 0;
        String filename = OptionsActivity.new_cus.getAddress_proof_image();
        Log.d("Log","filename: "+OptionsActivity.new_cus.getAddress_proof_image());

        File file = Constant.checkFolder(Constant.captured_images_folder);
        File fileArray[] = file.listFiles();

        if (fileArray.length != 0) {
            for (File f : fileArray) {
                if (f.getName().equals(filename)) {
                    if (f.length() != 0) {
                        String _imagePath = getRealPathFromURI( Environment.getExternalStorageDirectory() + File.separator + Constant.captured_images_folder+File.separator + filename);
                        imageView_addproof.setImageBitmap(scaleBitmap(_imagePath));
                    }
                    break;
                }
            }
        }
    }

    private void set_value_attachIdProof(){
        String id_proof = OptionsActivity.new_cus.getId_proof();
        int  position = Integer.parseInt(id_proof);
        Log.d("Log","val: "+position);
        spinner_idproof.setSelection(position);
    }

    private void set_value_attachIdProofImage(){
        AttachIdProofImageActivity.flag = 0;
        String filename = OptionsActivity.new_cus.getId_proof_image();
        Log.d("Log","filename: "+OptionsActivity.new_cus.getId_proof_image());

        File file = Constant.checkFolder(Constant.captured_images_folder);
        File fileArray[] = file.listFiles();

        if (fileArray.length != 0) {
            for (File f : fileArray) {
                if (f.getName().equals(filename)) {
                    if (f.length() != 0) {
                        String _imagePath = getRealPathFromURI( Environment.getExternalStorageDirectory() + File.separator + Constant.captured_images_folder+File.separator + filename);
                        imageView_idproof.setImageBitmap(scaleBitmap(_imagePath));
                    }
                    break;
                }
            }
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

    private void set_value_attachgstpan_no(){
        AttachGSTnoPANnoImageActivity.flag = 0;

        if(AttachGSTnoPANnoImageActivity.radio_flag == 1){

            gst_lay.setVisibility(View.VISIBLE);
            pan_lay.setVisibility(View.GONE);
            rdo_gst.setChecked(true);
            rdo_pan.setChecked(false);
            String gst = OptionsActivity.new_cus.getGst_no();
            Log.d("Log","gst: "+gst);
            ed_gstno.setText(gst);
            set_value_attachGstProofImage();
        }else if(AttachGSTnoPANnoImageActivity.radio_flag == 2){

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

    public void showPopup() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you want to save data?");

            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Toast toast = Toast.makeText(getApplicationContext(), "Data saved successfully..", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
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

}


