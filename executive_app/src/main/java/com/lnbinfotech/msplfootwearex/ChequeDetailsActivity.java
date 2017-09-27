package com.lnbinfotech.msplfootwearex;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.lnbinfotech.msplfootwearex.constant.Constant;
import com.lnbinfotech.msplfootwearex.log.WriteLog;
import com.lnbinfotech.msplfootwearex.model.ChequeDetailsGetterSetter;
import com.lnbinfotech.msplfootwearex.model.SelectAutoItemGetterSetter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class ChequeDetailsActivity extends AppCompatActivity {
    private EditText ed_branch,ed_bank,ed_chq_date,ed_chq_no,ed_chq_amt,ed_chq_ref;
    private AppCompatButton btn_submit;
    private ImageView imageView_cheque_img;
    private String auto_type, current_time, file_name;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
    private Calendar cal = Calendar.getInstance();
    final int requestCode = 21;
    static SelectAutoItemGetterSetter selectAuto;
    static ChequeDetailsGetterSetter chequeDetails;
    int day,month,year;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheque_details);
        init();
    }

    private void init(){

        selectAuto = new SelectAutoItemGetterSetter();
        chequeDetails = new ChequeDetailsGetterSetter();
        day = cal.get(Calendar.DAY_OF_MONTH);
        month = cal.get(Calendar.MONTH);
        year = cal.get(Calendar.YEAR);

        ed_bank = (EditText) findViewById(R.id.ed_bank);
        ed_branch = (EditText) findViewById(R.id.ed_branch);
        ed_chq_date = (EditText) findViewById(R.id.ed_chq_date);
        ed_chq_no = (EditText) findViewById(R.id.ed_chq_no);
        ed_chq_amt = (EditText) findViewById(R.id.ed_chq_amt);
        ed_chq_ref  = (EditText) findViewById(R.id.ed_chq_ref);
        imageView_cheque_img = (ImageView) findViewById(R.id.imageView_cheque_img);
        btn_submit = (AppCompatButton) findViewById(R.id.btn_submt);

        ed_bank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //VisitPaymentFormGetterSetter getterSetter = new VisitPaymentFormGetterSetter();

                Intent intent = new Intent(ChequeDetailsActivity.this,SelectAutoItemActivity.class);
                auto_type = "bank";
                intent.putExtra("Auto_type",auto_type);
                startActivity(intent);
                writeLog("goes to SelectAutoItemActivity");
            }
        });

        ed_branch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChequeDetailsActivity.this,SelectAutoItemActivity.class);
                auto_type = "branch";
                intent.putExtra("Auto_type",auto_type);
                startActivity(intent);
                writeLog("goes to SelectAutoItemActivity");
            }
        });

        ed_chq_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(1);
            }
        });

        imageView_cheque_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              capture_image();
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validations();
            }
        });

    }
    protected void onResume() {
        super.onResume();
        get_auto_branchlist();
        get_auto_banklist();

    }

    public void get_data(){
       // ChequeDetailsGetterSetter chequeDetails = new ChequeDetailsGetterSetter();
        String bank = ed_bank.getText().toString();
        chequeDetails.setChq_det_bank(bank);

        String branch = ed_branch.getText().toString();
        chequeDetails.setChq_det_branch(branch);

        String date = ed_chq_date.getText().toString();
        //VisitPaymentFormActivity.cheque.setChq_det_date(date);
        chequeDetails.setChq_det_date(date);

        String number = ed_chq_no.getText().toString();
        //VisitPaymentFormActivity.cheque.setChq_det_number(number);
        chequeDetails.setChq_det_number(number);

        String amount = ed_chq_amt.getText().toString();
        //VisitPaymentFormActivity.cheque.setChq_det_amt(amount);
        chequeDetails.setChq_det_amt(amount);

        String ref = ed_chq_ref.getText().toString();
        //VisitPaymentFormActivity.cheque.setChq_det_ref(ref);
        chequeDetails.setChq_det_ref(ref);

        chequeDetails.setChq_det_image(file_name);
        Log.d("Log","file_name: "+file_name);
        VisitPaymentFormActivity.ls.add(chequeDetails);
        finish();
    }

    private void get_auto_banklist(){
        // get_bank = getIntent().getStringExtra("Bank_name");
         ed_bank.setText(selectAuto.getChq_auto_bank());
        Log.d("Log","ed_bank: "+selectAuto.getChq_auto_bank());
        //ed_amount.setText(visit.getCheque_amount());
    }
    private void get_auto_branchlist(){
        // get_branch = getIntent().getStringExtra("Branch_name");
        ed_branch.setText(selectAuto.getChq_auto_branch());
        Log.d("Log","ed_branch: "+selectAuto.getChq_auto_branch());
        //ed_amount.setText(visit.getCheque_amount());

    }
    private void validations() {
        if (ed_bank.getText().toString().equals("")) {
            Toast toast = Toast.makeText(getApplicationContext(), "Please enter bank name", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        } else if (ed_branch.getText().toString().equals("")) {
            Toast toast = Toast.makeText(getApplicationContext(), "Please enter branch name", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        } else if (ed_chq_date.getText().toString().equals("")) {
            Toast toast = Toast.makeText(getApplicationContext(), "Please,enter cheque date", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        } else if (ed_chq_no.getText().toString().equals("")) {
            Toast toast = Toast.makeText(getApplicationContext(), "Please,enter cheque number", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        } else if (ed_chq_amt.getText().toString().equals("")) {
            Toast toast = Toast.makeText(getApplicationContext(), "Please,enter cheque amount", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        } else if (ed_chq_ref.getText().toString().equals("")) {
            Toast toast = Toast.makeText(getApplicationContext(), "Please,enter cheque reference", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        } else {
            get_data();

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (this.requestCode == requestCode && resultCode == RESULT_OK ) {

            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            Constant.checkFolder(Constant.captured_images_folder);
            String dateformat = currentDateFormat();
             file_name = "img_" + dateformat + ".jpg";
            store_CameraPhoto_InSdCard(bitmap, dateformat);
            Bitmap mbitmap = get_Image_from_sd_card(file_name);
            Log.d("Log", "imgename:" + mbitmap);
            imageView_cheque_img.setImageBitmap(mbitmap);
        }else {
            writeLog("Image cputure canceled from camera..");
        }
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
        } catch (FileNotFoundException f){
            f.printStackTrace();
        }catch (IOException io){
            io.printStackTrace();
        }catch (NullPointerException w){
            w.printStackTrace();
        }/*catch (Exception e){
            e.printStackTrace();
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
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HH_mm");
        current_time = sdf.format(new Date());
        return current_time;
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        return new DatePickerDialog(this,chq_date,year,month,day);
    }

    DatePickerDialog.OnDateSetListener chq_date = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
              try {
                  Date select_date = sdf.parse(dayOfMonth + "/" + (monthOfYear +1) + "/" + year);
                  ed_chq_date.setText(sdf.format(select_date));
              }catch (Exception e){
                  e.printStackTrace();
              }
        }
    };

    private void capture_image(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, requestCode);
    }

    private void showPopup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setMessage("Do you want to attach image?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    finish();
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

    private void writeLog(String _data){
        new WriteLog().writeLog(getApplicationContext(),"VisitPaymentFormActivity_" +_data);
    }

}
