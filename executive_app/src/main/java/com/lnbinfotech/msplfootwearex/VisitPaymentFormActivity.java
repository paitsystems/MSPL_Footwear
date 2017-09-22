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
import android.widget.Toast;

import com.lnbinfotech.msplfootwearex.adapter.ShowChqDetailAdapter;
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

    private EditText ed_amount;
    private TextInputEditText ed_cus_name,ed_branch,ed_bank;
    private RadioButton rdo_cash,rdo_cheque;
    private LinearLayout cheque_lay;
    private ListView lv_show_chq_detail;
    String auto_type, current_time;
    ImageView imageView_cheque_img;
    AppCompatButton btn_cheque_details,btn_save;
    Bitmap bmp;
    List<ChequeDetailsGetterSetter> ls;
    static ChequeDetailsGetterSetter cheque;
    static VisitPaymentFormGetterSetter visit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visit_payment_form);

        init();
        visit = new VisitPaymentFormGetterSetter();
        cheque = new ChequeDetailsGetterSetter();
        setAmount();


    }

    void init(){
        ed_amount = (EditText) findViewById(R.id.ed_amount);

        ed_cus_name = (TextInputEditText) findViewById(R.id.ed_cus_name);
        ed_bank = (TextInputEditText) findViewById(R.id.ed_bank);
        ed_branch = (TextInputEditText) findViewById(R.id.ed_branch);

        btn_cheque_details = (AppCompatButton) findViewById(R.id.btn_cheque_details);
        btn_save = (AppCompatButton) findViewById(R.id.btn_save);
        lv_show_chq_detail = (ListView) findViewById(R.id.lv_show_chq_detail);

        rdo_cash = (RadioButton) findViewById(R.id.rdo_cash);
        rdo_cheque = (RadioButton) findViewById(R.id.rdo_cheque);
        cheque_lay = (LinearLayout) findViewById(R.id.cheque_lay);
        imageView_cheque_img = (ImageView) findViewById(R.id.imageView_cheque_img);


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



        ed_bank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //VisitPaymentFormGetterSetter getterSetter = new VisitPaymentFormGetterSetter();
                String amt =  ed_amount.getText().toString();
                visit.setCheque_amount(amt);
                Intent intent = new Intent(VisitPaymentFormActivity.this,SelectAutoItemActivity.class);
                auto_type = "bank";
                intent.putExtra("Auto_type",auto_type);
                startActivity(intent);
                writeLog("goes to SelectAutoItemActivity");
            }
        });

        ed_branch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VisitPaymentFormActivity.this,SelectAutoItemActivity.class);
                auto_type = "branch";
                intent.putExtra("Auto_type",auto_type);
                startActivity(intent);
                writeLog("goes to SelectAutoItemActivity");
            }
        });

        btn_cheque_details.setOnClickListener(new View.OnClickListener() {
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

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        get_auto_cuslist();
        get_auto_banklist();
        get_auto_branchlist();
        show_chq_adapter();

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
            case 2:
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
                break;
        }

    }

    private void get_auto_cuslist(){
       //VisitPaymentFormGetterSetter getterSetter = new VisitPaymentFormGetterSetter();
       // get_cust = getIntent().getStringExtra("Customer_name");
        Log.d("Log","cus: "+visit.getCustomer_name());
         ed_cus_name.setText(visit.getCustomer_name());
    }
    private void get_auto_banklist(){
        // get_bank = getIntent().getStringExtra("Bank_name");
        ed_bank.setText(visit.getCheque_bank());
        ed_amount.setText(visit.getCheque_amount());
    }
    private void get_auto_branchlist(){
        // get_branch = getIntent().getStringExtra("Branch_name");
        ed_branch.setText(visit.getCheque_branch());
        ed_amount.setText(visit.getCheque_amount());

    }

    private void setAmount(){

    }

    private void show_chq_adapter(){
        ls = new ArrayList<>();
        //TODO create function in database getting all valuse of cheque and iterate cursor.and then set ls to adapter
        ShowChqDetailAdapter adapter = new ShowChqDetailAdapter(this,ls);
        lv_show_chq_detail.setAdapter(adapter);
        /*Toast toast  = Toast.makeText(getApplicationContext(),"Cheque details added successfully",Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.show();*/
    }



    public void store_CameraPhoto_InSdCard(Bitmap bitmap, String currentdate){
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
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HH_mm", Locale.ENGLISH);
         current_time = sdf.format(new Date());
        return current_time;
    }

    private void showPopup(int id){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if(id == 0) {
            builder.setMessage("Do you want to attach image?");
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
        }else if(id == 1){
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
        }
        builder.create().show();
    }

    private void writeLog(String _data){
        new WriteLog().writeLog(getApplicationContext(),"VisitPaymentFormActivity_" +_data);
    }

}
