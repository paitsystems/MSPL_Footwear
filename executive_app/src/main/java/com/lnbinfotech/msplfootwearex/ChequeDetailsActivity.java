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
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lnbinfotech.msplfootwearex.adapters.ShowChqDetailAdapter;
import com.lnbinfotech.msplfootwearex.constant.Constant;
import com.lnbinfotech.msplfootwearex.log.WriteLog;
import com.lnbinfotech.msplfootwearex.model.ChequeDetailsClass;
import com.lnbinfotech.msplfootwearex.model.SelectAutoItemClass;

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

    private ListView lv_show_chq_detail;
    private LinearLayout lay_img, lay_list;
    private EditText ed_branch, ed_bank, ed_chq_no, ed_chq_amt, ed_chq_ref, ed_total_chq, ed_chq_date;
    private AppCompatButton btn_submit;
    private TextView tv_chq_date, tv_total;
    private ImageView imageView_cheque_img;
    private String auto_type, imagePath;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
    private Calendar cal = Calendar.getInstance();
    private final int requestCode = 21;
    public static SelectAutoItemClass selectAuto;
    public ChequeDetailsClass chequeDetails;
    private Date today_date = Calendar.getInstance().getTime();
    private int day, month, year;
    private Toast toast;
    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(Constant.liveTestFlag==1) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }

        setContentView(R.layout.activity_cheque_details);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.payment);
        }
        init();
        if(!VisitPaymentFormActivity.ls.isEmpty()){
            lay_list.setVisibility(View.VISIBLE);
            show_chq_adapter();
        }
    }

    private void init() {
        toast = Toast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        selectAuto = new SelectAutoItemClass();
        day = cal.get(Calendar.DAY_OF_MONTH);
        month = cal.get(Calendar.MONTH);
        year = cal.get(Calendar.YEAR);
        FirstActivity.pref = getSharedPreferences(FirstActivity.PREF_NAME,MODE_PRIVATE);
        ed_bank = (EditText) findViewById(R.id.ed_bank);
        ed_branch = (EditText) findViewById(R.id.ed_branch);
        tv_chq_date = (TextView) findViewById(R.id.tv_chq_date);
        tv_total = (TextView) findViewById(R.id.tv_total);
        ed_chq_no = (EditText) findViewById(R.id.ed_chq_no);
        ed_chq_amt = (EditText) findViewById(R.id.ed_chq_amt);
        ed_chq_ref = (EditText) findViewById(R.id.ed_chq_ref);
        ed_total_chq = (EditText) findViewById(R.id.ed_total_chq);
        ed_chq_date = (EditText) findViewById(R.id.ed_chq_date);
        imageView_cheque_img = (ImageView) findViewById(R.id.imageView_cheque_img);
        btn_submit = (AppCompatButton) findViewById(R.id.btn_submt);
        lv_show_chq_detail = (ListView) findViewById(R.id.lv_show_chq_detail);
        lay_img = (LinearLayout) findViewById(R.id.lay_img);
        lay_list = (LinearLayout) findViewById(R.id.list_lay);
        tv_chq_date.setText(sdf.format(today_date));

        ed_bank.setOnClickListener(this);
        ed_branch.setOnClickListener(this);
        ed_chq_date.setOnClickListener(this);
        tv_chq_date.setOnClickListener(this);
        lay_img.setOnClickListener(this);
        btn_submit.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_submt:
                validations();
                break;
            case R.id.lay_img:
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                File f = Constant.checkFolder(Constant.folder_name);
                f = new File(f.getAbsolutePath(), "temp.jpg");
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                startActivityForResult(intent, requestCode);
                overridePendingTransition(R.anim.enter, R.anim.exit);
                //capture_image();
                break;
            case R.id.tv_chq_date:
                showDialog(1);
                break;
            case R.id.ed_chq_date:
                showDialog(1);
                break;
            case R.id.ed_bank:
                Intent k = new Intent(ChequeDetailsActivity.this, SelectAutoItemActivity.class);
                auto_type = "bank";
                k.putExtra("Auto_type", auto_type);
                startActivity(k);
                overridePendingTransition(R.anim.enter, R.anim.exit);
                writeLog("goes to SelectAutoItemActivity");
                break;
            case R.id.ed_branch:
                Intent i = new Intent(ChequeDetailsActivity.this, SelectAutoItemActivity.class);
                auto_type = "branch";
                i.putExtra("Auto_type", auto_type);
                startActivity(i);
                overridePendingTransition(R.anim.enter, R.anim.exit);
                writeLog("goes to SelectAutoItemActivity");
                break;
        }
    }

    @Override
    public void onBackPressed() {
        showPopup(1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        if(VisitPaymentFormActivity.ls.size()>=1) {
            getMenuInflater().inflate(R.menu.chequedetail_menu, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //new Constant(ChequeDetailsActivity.this).doFinish();
                showPopup(1);
                break;
            case R.id.chq_save:
                showPopup(2);
                break;
            case R.id.chq_clear:
                showPopup(3);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void onResume() {
        super.onResume();
        get_auto_branchlist();
        get_auto_banklist();
        if (VisitPaymentFormActivity.ls.size() != 0) {
            show_chq_adapter();
        }
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
            toast.setText("Please Select Bank Name");
            toast.show();
        } else if (ed_branch.getText().toString().equals("")) {
            toast.setText("Please Select Branch Name");
            toast.show();
        } else if (tv_chq_date.getText().toString().equals("")) {
            toast.setText("Please Select Cheque Date");
            toast.show();
        } else if (ed_total_chq.getText().toString().equals("")) {
            toast.setText("Please Enter Number of Cheques");
            toast.show();
        } else if (ed_chq_no.getText().toString().equals("")) {
            toast.setText("Please Enter Cheque Number");
            toast.show();
        } else if (ed_chq_amt.getText().toString().equals("")) {
            toast.setText("Please Enter Cheque Amount");
            toast.show();
        } /*else if (ed_chq_ref.getText().toString().equals("")) {
            toast.setText("Please,enter cheque reference");
            toast.show();
        } */ else {
            get_data();
        }
    }

    private void get_data() {
        try {
            chequeDetails = new ChequeDetailsClass();
            String bank = ed_bank.getText().toString();
            chequeDetails.setChq_det_bank(bank);

            String branch = ed_branch.getText().toString();
            chequeDetails.setChq_det_branch(branch);

            String tot_chq = ed_total_chq.getText().toString();
            chequeDetails.setNo_of_chq(tot_chq);

            String date = tv_chq_date.getText().toString();
            date = new SimpleDateFormat("dd/MMM/yyyy", Locale.ENGLISH).format(new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH).parse(date));
            chequeDetails.setChq_det_date(date);

            String number = ed_chq_no.getText().toString();
            chequeDetails.setChq_det_number(number);

            String amount = ed_chq_amt.getText().toString();
            chequeDetails.setChq_det_amt(amount);
            int tot = Integer.parseInt(amount);
            VisitPaymentFormActivity.total = VisitPaymentFormActivity.total + tot;
            tv_total.setText(String.valueOf(VisitPaymentFormActivity.total));

            String ref = ed_chq_ref.getText().toString();
            chequeDetails.setChq_det_ref(ref);

            chequeDetails.setChq_det_image(imagePath);
            Constant.showLog("file_name: " + imagePath);

            String bankName = ed_bank.getText().toString();
            String bankBranch = ed_branch.getText().toString();

            chequeDetails.setCustBankName(bankName);
            chequeDetails.setCustBankBranch(bankBranch);

            VisitPaymentFormActivity.ls.add(chequeDetails);
            clearFields();
        }catch (Exception e){
            e.printStackTrace();
            writeLog("ChequeDetailsActivity_get_Data_"+e.getMessage());
            toast.setText("Something went wrong");
            toast.show();
        }
        //finish();
        //new Constant(ChequeDetailsActivity.this).doFinish();
    }

    private void clearFields() {
        ed_chq_no.setText(null);
        ed_chq_amt.setText(null);
        ed_chq_no.requestFocus();
        imageView_cheque_img.setImageResource(R.drawable.camera);
        show_chq_adapter();
    }

    private void show_chq_adapter() {
        if(VisitPaymentFormActivity.ls.size()==1){
            lay_list.setVisibility(View.VISIBLE);
            if(menu!=null) {
                onCreateOptionsMenu(this.menu);
            }
        }
        lv_show_chq_detail.setAdapter(null);
        ShowChqDetailAdapter adapter = new ShowChqDetailAdapter(this, VisitPaymentFormActivity.ls);
        Constant.showLog("listchq: " + VisitPaymentFormActivity.ls.size());
        lv_show_chq_detail.setAdapter(adapter);
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
                    e.printStackTrace();
                }
            } catch (Exception e) {
                writeLog("onActivityResult():Exception:" + e);
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
                ed_chq_date.setText(sdf.format(select_date));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    private void showPopup(int a) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        if(a==1) {
            builder.setMessage("Do You Want To Go Back ?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    VisitPaymentFormActivity.ls.clear();
                    new Constant(ChequeDetailsActivity.this).doFinish();
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
        }else if(a==2){
            builder.setMessage("Save Cheque Details ?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    VisitPaymentFormActivity.isChequeDataSaved = 1;
                    new Constant(ChequeDetailsActivity.this).doFinish();
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
        }else if(a==3){
            builder.setMessage("Do You Want To Clear All Cheque Details ?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    VisitPaymentFormActivity.ls.clear();
                    VisitPaymentFormActivity.isChequeDataSaved = 0;
                    show_chq_adapter();
                    dialogInterface.dismiss();
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

    private void writeLog(String _data) {
        new WriteLog().writeLog(getApplicationContext(), "VisitPaymentFormActivity_" + _data);
    }
}
