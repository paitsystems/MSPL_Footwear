package com.lnbinfotech.msplfootwearex;

import android.app.DatePickerDialog;
import android.app.Dialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lnbinfotech.msplfootwearex.constant.Constant;
import com.lnbinfotech.msplfootwearex.log.WriteLog;
import com.lnbinfotech.msplfootwearex.model.ChequeDetailsGetterSetter;
import com.lnbinfotech.msplfootwearex.model.SelectAutoItemGetterSetter;

import java.io.File;
import java.io.FileInputStream;

import java.io.FileOutputStream;

import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class ChequeDetailsActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText ed_branch, ed_bank, ed_chq_no, ed_chq_amt, ed_chq_ref;
    private AppCompatButton btn_submit;
    private TextView tv_chq_date;
    private ImageView imageView_cheque_img;
    private String auto_type, current_time, imagePath;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
    private Calendar cal = Calendar.getInstance();
    private final int requestCode = 21;
    public static SelectAutoItemGetterSetter selectAuto;
    public static ChequeDetailsGetterSetter chequeDetails;
    private Date today_date = Calendar.getInstance().getTime();
    private int day, month, year;
    private Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheque_details);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.payment);
        }
        init();
    }

    private void init() {

        toast = Toast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        selectAuto = new SelectAutoItemGetterSetter();
        chequeDetails = new ChequeDetailsGetterSetter();
        day = cal.get(Calendar.DAY_OF_MONTH);
        month = cal.get(Calendar.MONTH);
        year = cal.get(Calendar.YEAR);

        ed_bank = (EditText) findViewById(R.id.ed_bank);
        ed_branch = (EditText) findViewById(R.id.ed_branch);
        tv_chq_date = (TextView) findViewById(R.id.tv_chq_date);
        ed_chq_no = (EditText) findViewById(R.id.ed_chq_no);
        ed_chq_amt = (EditText) findViewById(R.id.ed_chq_amt);
        ed_chq_ref = (EditText) findViewById(R.id.ed_chq_ref);
        imageView_cheque_img = (ImageView) findViewById(R.id.imageView_cheque_img);
        btn_submit = (AppCompatButton) findViewById(R.id.btn_submt);

        tv_chq_date.setText(sdf.format(today_date));

        ed_bank.setOnClickListener(this);
        ed_branch.setOnClickListener(this);
        tv_chq_date.setOnClickListener(this);
        imageView_cheque_img.setOnClickListener(this);
        btn_submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_submt:
                validations();
                break;
            case R.id.imageView_cheque_img:
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                File f = Constant.checkFolder(Constant.folder_name);
                f = new File(f.getAbsolutePath(), "temp.jpg");
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                startActivityForResult(intent, requestCode);
                //capture_image();
                break;
            case R.id.tv_chq_date:
                showDialog(1);
                break;
            case R.id.ed_bank:
                Intent k = new Intent(ChequeDetailsActivity.this, SelectAutoItemActivity.class);
                auto_type = "bank";
                k.putExtra("Auto_type", auto_type);
                startActivity(k);
                writeLog("goes to SelectAutoItemActivity");
                break;
            case R.id.ed_branch:
                Intent i = new Intent(ChequeDetailsActivity.this, SelectAutoItemActivity.class);
                auto_type = "branch";
                i.putExtra("Auto_type", auto_type);
                startActivity(i);
                writeLog("goes to SelectAutoItemActivity");
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
                //new Constant(ChequeDetailsActivity.this).doFinish();
                showPopup();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void onResume() {
        super.onResume();
        get_auto_branchlist();
        get_auto_banklist();
    }

    private void get_data() {
        String bank = ed_bank.getText().toString();
        chequeDetails.setChq_det_bank(bank);

        String branch = ed_branch.getText().toString();
        chequeDetails.setChq_det_branch(branch);

        String date = tv_chq_date.getText().toString();
        chequeDetails.setChq_det_date(date);

        String number = ed_chq_no.getText().toString();
        chequeDetails.setChq_det_number(number);

        String amount = ed_chq_amt.getText().toString();
        chequeDetails.setChq_det_amt(amount);

        String ref = ed_chq_ref.getText().toString();
        chequeDetails.setChq_det_ref(ref);

        chequeDetails.setChq_det_image(imagePath);
        Constant.showLog("file_name: " + imagePath);
        VisitPaymentFormActivity.ls.add(chequeDetails);
        //finish();
        new Constant(ChequeDetailsActivity.this).doFinish();
    }

    private void get_auto_banklist() {
        ed_bank.setText(selectAuto.getChq_auto_bank());
        Constant.showLog("ed_bank: " + selectAuto.getChq_auto_bank());
    }

    private void get_auto_branchlist() {
        ed_branch.setText(selectAuto.getChq_auto_branch());
        Constant.showLog("ed_branch: " + selectAuto.getChq_auto_branch());
    }

    private void validations() {
        if (ed_bank.getText().toString().equals("")) {
            toast.setText("Please enter bank name");
            toast.show();
        } else if (ed_branch.getText().toString().equals("")) {
            toast.setText("Please enter branch name");
            toast.show();
        } else if (tv_chq_date.getText().toString().equals("")) {
            toast.setText("Please,enter cheque date");
            toast.show();
        } else if (ed_chq_no.getText().toString().equals("")) {
            toast.setText("Please,enter cheque number");
            toast.show();
        } else if (ed_chq_amt.getText().toString().equals("")) {
            toast.setText("Please,enter cheque amount");
            toast.show();
        } else if (ed_chq_ref.getText().toString().equals("")) {
            toast.setText("Please,enter cheque reference");
            toast.show();
        } else {
            get_data();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (this.requestCode == requestCode && resultCode == RESULT_OK) {
            try {
                imageView_cheque_img.setVisibility(View.VISIBLE);
                String _imagePath = getRealPathFromURI(Environment.getExternalStorageDirectory() + File.separator + Constant.folder_name + File.separator + "temp.jpg");
                imageView_cheque_img.setImageBitmap(scaleBitmap(_imagePath));
                long datetime = System.currentTimeMillis();
                SimpleDateFormat sdf = new SimpleDateFormat("dd_MMM_yyyy_HH_mm_ss", Locale.ENGLISH);
                Date resultdate = new Date(datetime);

                imagePath = "Cheque_Img_" + sdf.format(resultdate) + ".jpg";

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

    @Override
    protected Dialog onCreateDialog(int id) {
        return new DatePickerDialog(this, chq_date, year, month, day);
    }

    DatePickerDialog.OnDateSetListener chq_date = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
            try {
                Date select_date = sdf.parse(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                tv_chq_date.setText(sdf.format(select_date));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    private void showPopup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage("Do you want to clear cheque details");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                new Constant(ChequeDetailsActivity.this).doFinish();
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
        new WriteLog().writeLog(getApplicationContext(), "VisitPaymentFormActivity_" + _data);
    }


}
