package com.lnbinfotech.msplfootwearex;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.lnbinfotech.msplfootwearex.adapters.ShowChqDetailAdapter;
import com.lnbinfotech.msplfootwearex.constant.Constant;
import com.lnbinfotech.msplfootwearex.log.WriteLog;
import com.lnbinfotech.msplfootwearex.model.ChequeDetailsGetterSetter;
import com.lnbinfotech.msplfootwearex.model.VisitPaymentFormGetterSetter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class VisitPaymentFormActivity extends AppCompatActivity {

    private EditText ed_amount,ed_cus_name;
    private RadioButton rdo_cash,rdo_cheque;
    private LinearLayout cheque_lay,add_more_lay,chqbtn_det_lay;
    private ListView lv_show_chq_detail;
    private TextView tv_cheque_details;
    private String auto_type, current_time;
    private ImageView imageView_cheque_img;
    private AppCompatButton btn_save;
    private Bitmap bmp;
    static List<ChequeDetailsGetterSetter> ls;
    private ChequeDetailsGetterSetter cheque;
    static VisitPaymentFormGetterSetter visit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visit_payment_form);

        init();

    }

    void init(){
        ed_amount = (EditText) findViewById(R.id.ed_amount);

        ed_cus_name = (EditText) findViewById(R.id.ed_cus_name);


        tv_cheque_details = (TextView) findViewById(R.id.tv_cheque_details);
        btn_save = (AppCompatButton) findViewById(R.id.btn_save);
        lv_show_chq_detail = (ListView) findViewById(R.id.lv_show_chq_detail);

        rdo_cash = (RadioButton) findViewById(R.id.rdo_cash);
        rdo_cheque = (RadioButton) findViewById(R.id.rdo_cheque);
        cheque_lay = (LinearLayout) findViewById(R.id.cheque_lay);
        chqbtn_det_lay= (LinearLayout) findViewById(R.id.chqbtn_det_lay);
        add_more_lay = (LinearLayout) findViewById(R.id.add_more_lay);
        imageView_cheque_img = (ImageView) findViewById(R.id.imageView_cheque_img);

        /*if(ls.size() != 0){
            add_more_lay.setVisibility(View.VISIBLE);
        }*/

        ed_cus_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VisitPaymentFormActivity.this,SelectAutoItemActivity.class);
                auto_type = "cus";
                intent.putExtra("Auto_type",auto_type);
                startActivity(intent);
                writeLog("goes to SelectAutoItemActivity");
            }
        });

        rdo_cash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rdo_cash.setChecked(true);
                rdo_cheque.setChecked(false);
                cheque_lay.setVisibility(View.GONE);
            }
        });

        rdo_cheque.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rdo_cash.setChecked(false);
                rdo_cheque.setChecked(true);
                cheque_lay.setVisibility(View.VISIBLE);
            }
        });

        tv_cheque_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VisitPaymentFormActivity.this,ChequeDetailsActivity.class);
                startActivity(intent);
                writeLog("goes to ChequeDetailsActivity");
            }
        });

        add_more_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VisitPaymentFormActivity.this,ChequeDetailsActivity.class);
                startActivity(intent);
                writeLog("goes to ChequeDetailsActivity");
            }
        });

        imageView_cheque_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopup(0);

            }
        });
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ed_cus_name.getText().toString().equals("")){
                    Toast toast  = Toast.makeText(getApplicationContext(),"Please enter customer name",Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER,0,0);
                    toast.show();
                }
                if(rdo_cheque.isChecked()){
                    validation();
                }

            }
        });

        visit = new VisitPaymentFormGetterSetter();
        cheque = new ChequeDetailsGetterSetter();
        ls = new ArrayList<>();
        setAmount();

    }

    @Override
    protected void onResume() {
        super.onResume();
        get_auto_cuslist();
        //if(!lv_show_chq_detail.getAdapter().equals(null)) {
         show_chq_adapter();
         if(ls.size() != 0){
             add_more_lay.setVisibility(View.VISIBLE);
             chqbtn_det_lay.setEnabled(false);
         }else {
             add_more_lay.setVisibility(View.GONE);
             chqbtn_det_lay.setEnabled(true);
         }
        //add_more_lay.setVisibility(View.VISIBLE);
       // }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 1:
                if(resultCode == RESULT_OK){
                    Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                    Constant.checkFolder(Constant.captured_images_folder);
                    String dateformat = currentDateFormat();
                    String file_name = "img_" + dateformat + ".jpg";
                    store_CameraPhoto_InSdCard(bitmap, dateformat);
                    Bitmap mbitmap = get_Image_from_sd_card(file_name);
                    Log.d("Log", "imgename:" + mbitmap);
                    imageView_cheque_img.setImageBitmap(mbitmap);
                }else {
                    writeLog("Image cputure canceled from camera..");
                }
                break;
            /*case 2:
                if(data != null && resultCode == RESULT_OK ){
                    Cursor cursor;

                    Uri selectedImage = data.getData();

                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                     cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                     cursor.moveToFirst() ;
                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);

                        String filePath = cursor.getString(columnIndex);

                    cursor.close();

                    if(bmp != null && !bmp.isRecycled())
                    {
                        bmp = null;
                    }
                    bmp = BitmapFactory.decodeFile(filePath);
                    imageView_cheque_img.setImageBitmap(bmp);
                }else {
                writeLog("Photopicker canceled from gallery..");
            }
                break;*/
        }

    }

    private void get_auto_cuslist(){
       //VisitPaymentFormGetterSetter getterSetter = new VisitPaymentFormGetterSetter();
       // get_cust = getIntent().getStringExtra("Customer_name");
         Log.d("Log","cus: "+visit.getCustomer_name());
         ed_cus_name.setText(visit.getCustomer_name());
    }


    private void setAmount(){
        String amt =  ed_amount.getText().toString();
        VisitPaymentFormActivity.visit.setCheque_amount(amt);
    }

    private void show_chq_adapter(){
        //ls = new ArrayList<>();
        lv_show_chq_detail.setAdapter(null);

        //ls.add(cheque);
        //TODO create function in database getting all valuse of cheque and iterate cursor.and then set ls to adapter
        //for(int i = 0; i <= ls.size(); i++) {
            ShowChqDetailAdapter adapter = new ShowChqDetailAdapter(this, ls);
            Log.d("Log", "listchq: " + ls.size());
            lv_show_chq_detail.setAdapter(adapter);
        //}
       // add_more_lay.setVisibility(View.VISIBLE);
        /*Toast toast  = Toast.makeText(getApplicationContext(),"Cheque details added successfully",Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.show();*/
    }



    private void store_CameraPhoto_InSdCard(Bitmap bitmap, String currentdate){
        File file = new File(Environment.getExternalStorageDirectory() + File.separator + Constant.captured_images_folder+File.separator + "img_"+currentdate+".jpg");
        //File file = new File(Environment.getExternalStorageDirectory() + "img_"+currentdate+".jpeg");

        Log.d("Log","File path:"+file);
        try{

            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 15, fos);
            fos.flush();
            fos.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        /*catch (FileNotFoundException f){
            f.printStackTrace();
        }catch (IOException io){
            io.printStackTrace();
        }catch (NullPointerException w){
            w.printStackTrace();
        }*/
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
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HH_mm", Locale.ENGLISH);
         current_time = sdf.format(new Date());
        return current_time;
    }

    private void validation(){
         if(ed_amount.getText().toString().equals("")){
            Toast toast  = Toast.makeText(getApplicationContext(),"Please enter amount",Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER,0,0);
            toast.show();
         }else if(lv_show_chq_detail.getAdapter().getCount() == 0){
             Log.d("Log","list_sie:"+lv_show_chq_detail.getAdapter().getCount());
         //}else if(ls.size() == 0){
            Toast toast  = Toast.makeText(getApplicationContext(),"Please enter cheque details",Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER,0,0);
            toast.show();
        }

    }




    private void showPopup(int id){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if(id == 0) {
            builder.setMessage("Do you want to attach image?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    //showPopup(1);
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent,1);
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
        }/*else if(id == 1){
            builder.setMessage("Attach image");
            builder.setPositiveButton("Camera", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                       Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                       startActivityForResult(intent,1);
                }
            });
            builder.setNegativeButton("Gallery", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent1 = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent1,2);
                }
            });
        }*/
        builder.create().show();
    }

    private void writeLog(String _data){
        new WriteLog().writeLog(getApplicationContext(),"VisitPaymentFormActivity_" +_data);
    }

}
