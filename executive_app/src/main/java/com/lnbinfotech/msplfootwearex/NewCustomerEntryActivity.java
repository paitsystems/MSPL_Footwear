package com.lnbinfotech.msplfootwearex;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NewCustomerEntryActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    EditText ed_cus_name,ed_mobile_no,ed_email_id,ed_address;
    Spinner spinner_addproof,spinner_idproof;
    String[] add_proof = {"Aadhaar","Light bill","Aggreement copy","Index 0"};
    String[] id_proof = {"Aadhaar","Pan card","Driving license","Election card"};
    ArrayAdapter<String> adapter_address;
    ArrayAdapter<String> adapter_id;
    RadioButton rdo_gst,rdo_pan;
    LinearLayout gst_lay,pan_lay;
    Button bt_save;
    ImageView imageView_cus_image,imageView_addproof,imageView_idproof,imageView_pan_img,imageView_gst_img;
    Bitmap bmp;
    int flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_customer_entry);
        init();
    }
    void init(){
        ed_cus_name = (EditText) findViewById(R.id.ed_cus_name);
        ed_mobile_no = (EditText) findViewById(R.id.ed_mobile_no);
        ed_email_id = (EditText) findViewById(R.id.ed_emailid);
        ed_address = (EditText) findViewById(R.id.ed_address);

        rdo_gst = (RadioButton) findViewById(R.id.rdo_gstno);
        rdo_pan = (RadioButton) findViewById(R.id.rdo_panno);

        gst_lay = (LinearLayout) findViewById(R.id.gst_lay);
        pan_lay = (LinearLayout) findViewById(R.id.pan_lay);
        bt_save = (Button) findViewById(R.id.btn_save);

        imageView_cus_image = (ImageView) findViewById(R.id.imageView_cus_image);
        imageView_addproof = (ImageView) findViewById(R.id.imageView_addproof);
        imageView_idproof = (ImageView) findViewById(R.id.imageView_idproof);
        imageView_gst_img = (ImageView) findViewById(R.id.imageView_gst_img);
        imageView_pan_img = (ImageView) findViewById(R.id.imageView_pan_img);

        spinner_addproof = (Spinner) findViewById(R.id.spinner_addproof);
        spinner_idproof = (Spinner) findViewById(R.id.spinner_idproof);

        adapter_address = new ArrayAdapter<>(this,R.layout.address_list,add_proof);
        spinner_addproof.setAdapter(adapter_address);
        spinner_addproof.setOnItemSelectedListener(this);
        adapter_id = new ArrayAdapter<>(this,R.layout.id_list,id_proof);
        spinner_idproof.setAdapter(adapter_id);
        spinner_idproof.setOnItemSelectedListener(this);

        rdo_gst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                rdo_pan.setChecked(false);
                gst_lay.setVisibility(View.VISIBLE);
                pan_lay.setVisibility(View.GONE);
            }
        });
        rdo_pan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rdo_gst.setChecked(false);
                gst_lay.setVisibility(View.GONE);
                pan_lay.setVisibility(View.VISIBLE);

            }
        });

        imageView_cus_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flag = 0;
                showPopup(0);
            }
        });

        imageView_addproof.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flag = 1;
               showPopup(0);

            }
        });

        imageView_idproof.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flag = 2;
                showPopup(0);

            }
        });
        imageView_pan_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flag = 3;
               // imageView_gst_img.setVisibility(View.GONE);
                showPopup(0);

            }
        });

        imageView_gst_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flag = 4;

                showPopup(0);

            }
        });

        bt_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ed_cus_name.getText().toString().equals("") & ed_mobile_no.getText().equals("")
                        & ed_email_id.getText().toString().equals("") & ed_address.getText().toString().equals("")){
                    Toast toast  = Toast.makeText(getApplicationContext(),"Please,fill all the fields",Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                }else
                 if(ed_cus_name.getText().toString().equals("")){
                    Toast toast  = Toast.makeText(getApplicationContext(),"Please,enter customer name",Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                }else
                 if(ed_mobile_no.getText().equals("")){
                    Toast toast  = Toast.makeText(getApplicationContext(),"Please,enter mobile number",Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                }else
                 if(ed_email_id.getText().toString().equals("")){
                    Toast toast  = Toast.makeText(getApplicationContext(),"Please,enter email id",Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                }else
                 if(ed_address.getText().toString().equals("")){
                    Toast toast  = Toast.makeText(getApplicationContext(),"Please,enter address",Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                }
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 1:
                if (resultCode == RESULT_OK) {
                    Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                    Constant.checkFolder(Constant.captured_images_folder);
                    String dateformat = currentDateFormat();
                    String file_name = "img_" + dateformat + ".jpg";
                    store_CameraPhoto_InSdCard(bitmap, dateformat);
                    Bitmap mbitmap = get_Image_from_sd_card(file_name);
                    Log.d("Log", "imgename:" + mbitmap);
                    if(flag == 0){
                        imageView_cus_image.setImageBitmap(mbitmap);
                    }else if (flag == 1){
                        imageView_addproof.setImageBitmap(mbitmap);
                     }else if(flag == 2){
                        imageView_idproof.setImageBitmap(mbitmap);
                      }else  if(flag == 3){
                        imageView_pan_img.setImageBitmap(mbitmap);
                      }else if(flag == 4){
                        imageView_gst_img.setImageBitmap(mbitmap);
                      }

                }
                break;
            case 2:
                if (data != null && resultCode == RESULT_OK)
                {

                    Uri selectedImage = data.getData();

                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String filePath = cursor.getString(columnIndex);
                    cursor.close();

                    if(bmp != null && !bmp.isRecycled())
                    {
                        bmp = null;
                    }
                    bmp = BitmapFactory.decodeFile(filePath);

                    //imgv_img1.setBackgroundResource(0);
                    if(flag == 0){
                        imageView_cus_image.setImageBitmap(bmp);
                    }else if(flag == 1) {
                        imageView_addproof.setImageBitmap(bmp);
                    }else  if(flag == 2){
                        imageView_idproof.setImageBitmap(bmp);
                    }else if(flag == 3){
                        imageView_pan_img.setImageBitmap(bmp);
                    }else if(flag ==  4){
                        imageView_gst_img.setImageBitmap(bmp);
                    }
                }
                else
                {
                    Log.d("Status:", "Photopicker canceled");
                }
                break;


        }
    }


    public void store_CameraPhoto_InSdCard(Bitmap bitmap,String currentdate){
        File file = new File(Environment.getExternalStorageDirectory() + File.separator + Constant.captured_images_folder+File.separator + "img_"+currentdate+".jpg");
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



    public Bitmap get_Image_from_sd_card(String filename){
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

    public String currentDateFormat(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HH_mm");
        String current_time = sdf.format(new Date());
        return current_time;
    }



    public void showPopup(int id) {

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
