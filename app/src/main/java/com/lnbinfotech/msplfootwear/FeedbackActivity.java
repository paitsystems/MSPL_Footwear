package com.lnbinfotech.msplfootwear;

import android.app.Dialog;
import android.app.Notification;
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
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.lnbinfotech.msplfootwear.constant.Constant;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FeedbackActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Spinner fedback_spinner;
    ArrayAdapter<String> feedbk_type;
    String[] arr = {"Invoice","Order","Packing","Damage Goods","Service","Team","Others"};
    EditText ed_description;
    AppCompatButton bt_send;
    ImageView imgv_img1,imgv_img2,imgv_img3;
    LinearLayout packing_order_inv_lay;
    CardView damaged_goods_cardlay,service_or_team_cardlay;
    //final int requestCode = 21 ;
    ByteArrayOutputStream byteArray;
    Bitmap bmp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        init();
    }
    void init(){
        fedback_spinner = (Spinner) findViewById(R.id.fedback_spinner);
        ed_description = (EditText) findViewById(R.id.ed_description);
        bt_send = (AppCompatButton) findViewById(R.id.bt_send);

        imgv_img1 = (ImageView) findViewById(R.id.imgv_img1);
        imgv_img2 = (ImageView) findViewById(R.id.imgv_img2);
        imgv_img3 = (ImageView) findViewById(R.id.imgv_img3);

        //  imgv_img1 = null;
        //imgv_img2 = null;
        //imgv_img3 = null;

        packing_order_inv_lay = (LinearLayout) findViewById(R.id.packing_order_inv_lay);
        damaged_goods_cardlay = (CardView) findViewById(R.id.damaged_goods_cardlay);
        service_or_team_cardlay = (CardView) findViewById(R.id.service_or_team_cardlay);

        feedbk_type = new ArrayAdapter<String>(this,R.layout.feedbk_type_list,arr);
        fedback_spinner.setAdapter(feedbk_type);
        fedback_spinner.setOnItemSelectedListener(this);

        /*bt_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             show_popup(0);
            }
        });*/

        imgv_img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                show_popup(0);
            }
        });

        imgv_img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                show_popup(1);
            }
        });

        imgv_img3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                show_popup(2);
            }
        });


        bt_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        String item = fedback_spinner.getSelectedItem().toString();
        fedback_spinner.setSelection(position);

        if (item.equals("Packing") | item.equals("Order") | item.equals("Invoice")){
            packing_order_inv_lay.setVisibility(View.VISIBLE);
            damaged_goods_cardlay.setVisibility(View.GONE);
            service_or_team_cardlay.setVisibility(View.GONE);
        }else if(item.equals("Damage Goods")){
            packing_order_inv_lay.setVisibility(View.GONE);
            damaged_goods_cardlay.setVisibility(View.VISIBLE);
            service_or_team_cardlay.setVisibility(View.GONE);
        }else if (item.equals("Service") | item.equals("Team")){
            packing_order_inv_lay.setVisibility(View.GONE);
            damaged_goods_cardlay.setVisibility(View.GONE);
            service_or_team_cardlay.setVisibility(View.VISIBLE);
        }else if(item.equals("Others")){
            packing_order_inv_lay.setVisibility(View.GONE);
            damaged_goods_cardlay.setVisibility(View.GONE);
            service_or_team_cardlay.setVisibility(View.GONE);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public  void show_popup(int id){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if(id == 0) {
            builder.setMessage("Do you want to attach image?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    show_popup(3);

                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    dialogInterface.dismiss();
                }
            });
        }else if(id == 1) {
            builder.setMessage("Do you want to attach image?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    show_popup(4);

                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    dialogInterface.dismiss();
                }
            });
        }else if(id == 2) {
            builder.setMessage("Do you want to attach image?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    show_popup(5);

                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    dialogInterface.dismiss();
                }
            });
        }else  if(id == 3) {
            builder.setMessage("Attach image:");
            builder.setPositiveButton("Camera", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    Intent intent1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent1, 1);

                }
            });
            builder.setNegativeButton("Gallery", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent4 = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent4, 4);

                }
            });
        } else if(id == 4) {
            builder.setMessage("Attach image:");
            builder.setPositiveButton("Camera", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    Intent intent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent2, 2);

                }
            });
            builder.setNegativeButton("Gallery", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent5 = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent5, 5);

                }
            });
        }else if(id == 5){
            builder.setMessage("Attach image:");
            builder.setPositiveButton("Camera", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    Intent intent3 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent3, 3);

                }
            });
            builder.setNegativeButton("Gallery", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent6 = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent6, 6);


                }
            });
        }else if(id == 6) {
            builder.setMessage("You can attach only three images do you want to delete it?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    imgv_img3.setImageBitmap(null);

                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();

                }
            });
        }
        builder.create().show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //if (this.requestCode == requestCode && resultCode == RESULT_OK ) {
        //imgv_img2.setImageBitmap(bitmap);
        // byteArray = new ByteArrayOutputStream();
        //String folder_name = "Paragon";
        //String imagename = getIntent().getExtras().getString("imgname");





        switch (requestCode){
            case 1:
                if (resultCode == RESULT_OK ) {

                    Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                    Constant.checkFolder(Constant.image_folder);
                    String dateformat = currentDateFormat();
                    String file_name = "img_" + dateformat + ".jpg";
                    store_CameraPhoto_InSdCard(bitmap, dateformat);
                    Bitmap mbitmap = get_Image_from_sd_card(file_name);
                    imgv_img1.setImageBitmap(mbitmap);
                    Log.d("Log","imgename:"+mbitmap);
                    imgv_img2.setVisibility(View.VISIBLE);
                }
                break;
            case 2:
                if (resultCode == RESULT_OK ) {

                    Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                    Constant.checkFolder(Constant.image_folder);
                    String dateformat = currentDateFormat();
                    String file_name = "img_" + dateformat + ".jpg";
                    store_CameraPhoto_InSdCard(bitmap, dateformat);
                    Bitmap mbitmap = get_Image_from_sd_card(file_name);
                    imgv_img3.setVisibility(View.VISIBLE);
                    imgv_img2.setImageBitmap(mbitmap);
                    Log.d("Log", "imgename:" + mbitmap);
                }
                break;

            case 3:
                if (resultCode == RESULT_OK ) {

                    Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                    Constant.checkFolder(Constant.image_folder);
                    String dateformat = currentDateFormat();
                    String file_name = "img_" + dateformat + ".jpg";
                    store_CameraPhoto_InSdCard(bitmap, dateformat);
                    Bitmap mbitmap = get_Image_from_sd_card(file_name);
                    imgv_img3.setImageBitmap(mbitmap);
                    Log.d("Log","imgename:"+mbitmap);

                }
                break;

            case 4:
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
                    imgv_img1.setImageBitmap(bmp);
                    imgv_img2.setVisibility(View.VISIBLE);
                }
                else
                {
                    Log.d("Status:", "Photopicker canceled");
                }


                break;
            case 5:
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

                    // imgv_img2.setBackgroundResource(0);
                    imgv_img2.setImageBitmap(bmp);
                    imgv_img3.setVisibility(View.VISIBLE);
                }
                else
                {
                    Log.d("Status:", "Photopicker canceled");
                }
                break;
            case 6:
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

                    //imgv_img3.setBackgroundResource(0);
                    imgv_img3.setImageBitmap(bmp);
                }
                else
                {
                    Log.d("Status:", "Photopicker canceled");
                }
                break;
        }


         /*File file = new File(Environment.getExternalStorageDirectory() + File.separator + Constant.image_folder+File.separator + "img_"+currentDateFormat()+".png");
            Log.d("Log","File path:"+file);
            try {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArray);
                //file.createNewFile();
                FileOutputStream fos = new FileOutputStream(file);
                fos.write(byteArray.toByteArray());
                fos.close();
            }catch (NullPointerException e){
                e.printStackTrace();
            }catch (IOException  i){
                i.printStackTrace();
            }*/


    }

    public void store_CameraPhoto_InSdCard(Bitmap bitmap,String currentdate){
        File file = new File(Environment.getExternalStorageDirectory() + File.separator + Constant.image_folder+File.separator + "img_"+currentdate+".jpg");
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
        File imgfile = new File(Environment.getExternalStorageDirectory() + File.separator + Constant.image_folder+File.separator + filename);
        //File imgfile = new File(Environment.getExternalStorageDirectory() +  filename);

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

    public void get_Intent(){
        Intent intent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        startActivityForResult(intent2, 1);

    }
}
