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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.lnbinfotech.msplfootwearex.constant.Constant;
import com.lnbinfotech.msplfootwearex.log.WriteLog;
import com.lnbinfotech.msplfootwearex.model.ChequeDetailsClass;
import com.lnbinfotech.msplfootwearex.model.SelectAutoItemClass;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class OtherDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    private Constant constant, constant1;
    private ListView lv_show_chq_detail;
    private LinearLayout lay_img, lay_list;
    private EditText ed_branch, ed_bank, ed_mode_type, ed_remark, ed_amnt, ed_total_chq, ed_chq_date;
    private AppCompatButton btn_submit;
    private TextView tv_chq_date, tv_total;
    private ImageView imageView_cheque_img;
    private String auto_type, imagePath, custId = "0";
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
    private Calendar cal = Calendar.getInstance();
    private final int requestCode = 21, REQUEST_IMAGE_PICK_UP = 2;
    public ChequeDetailsClass chequeDetails;
    private Date today_date = Calendar.getInstance().getTime();
    private int day, month, year;
    private Toast toast;
    private List<ChequeDetailsClass> ls;
    private Menu menu;
    private Spinner sp_sizeGroup;
    List<String> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(Constant.liveTestFlag==1) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }

        setContentView(R.layout.activity_other_details);

        init();

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        try {
            custId = getIntent().getExtras().getString("custid");
        } catch (Exception e) {
            e.printStackTrace();
            writeLog("onCreate_" + e.getMessage());
        }
    }

    protected void onResume() {
        super.onResume();
        get_auto_branchlist();
        get_auto_banklist();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_submt:
                validations();
                break;
            case R.id.lay_img:
                showDia(1);
                break;
            case R.id.tv_chq_date:
                showDialog(1);
                break;
            case R.id.ed_chq_date:
                showDialog(1);
                break;
            case R.id.ed_bank:
                Intent k = new Intent(OtherDetailsActivity.this, SelectAutoItemActivity.class);
                auto_type = "bank1";
                k.putExtra("Auto_type", auto_type);
                startActivity(k);
                overridePendingTransition(R.anim.enter, R.anim.exit);
                writeLog("goes to SelectAutoItemActivity");
                break;
            case R.id.ed_branch:
                Intent i = new Intent(OtherDetailsActivity.this, SelectAutoItemActivity.class);
                auto_type = "branch1";
                i.putExtra("Auto_type", auto_type);
                startActivity(i);
                overridePendingTransition(R.anim.enter, R.anim.exit);
                writeLog("goes to SelectAutoItemActivity");
                break;
        }
    }

    @Override
    public void onBackPressed() {
        //showDia(0);
        new Constant(OtherDetailsActivity.this).doFinish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //showDia(0);
                new Constant(OtherDetailsActivity.this).doFinish();
                break;
        }
        return super.onOptionsItemSelected(item);
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
                String type = list.get(sp_sizeGroup.getSelectedItemPosition());
                imagePath = "C_"+custId+"_Other_"+type+"_" + sdf.format(resultdate) + ".jpg";
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
        }else if (requestCode == REQUEST_IMAGE_PICK_UP && resultCode == RESULT_OK && data!=null) {
            try {
                Uri selectedImage = data.getData();
                String[] filepathcoloum = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(selectedImage, filepathcoloum, null, null, null);
                if(cursor!=null) {
                    if (cursor.moveToFirst()) {
                        int columnindex = cursor.getColumnIndex(filepathcoloum[0]);
                        String imgDecodedString = cursor.getString(columnindex);
                        long datetime = System.currentTimeMillis();
                        SimpleDateFormat sdf = new SimpleDateFormat("dd_MMM_yyyy_HH_mm_ss", Locale.ENGLISH);
                        Date resultdate = new Date(datetime);
                        String fname = "S_" + sdf.format(resultdate);
                        File sourcefile = new File(imgDecodedString);
                        File destinationfile = new File(Environment.getExternalStorageDirectory() + File.separator + Constant.folder_name + "/" + fname + ".jpg");
                        copyImage(sourcefile, destinationfile);
                        cursor.close();
                    }
                }else{
                    toast.setText("Please Try Again");
                    toast.show();
                }
            }catch (Exception e){
                e.printStackTrace();
                writeLog("onActivityResult_REQUEST_IMAGE_PICK_UP_"+e.getMessage());
                toast.setText("Something Went Wrong");
                toast.show();
            }
        }
    }

    private void init() {
        toast = Toast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        ChequeDetailsActivityChanged.selectAuto = new SelectAutoItemClass();
        day = cal.get(Calendar.DAY_OF_MONTH);
        month = cal.get(Calendar.MONTH);
        year = cal.get(Calendar.YEAR);
        FirstActivity.pref = getSharedPreferences(FirstActivity.PREF_NAME,MODE_PRIVATE);
        ed_bank = (EditText) findViewById(R.id.ed_bank);
        ed_branch = (EditText) findViewById(R.id.ed_branch);
        tv_chq_date = (TextView) findViewById(R.id.tv_chq_date);
        tv_total = (TextView) findViewById(R.id.tv_total);
        ed_mode_type = (EditText) findViewById(R.id.ed_mode_type);
        ed_remark = (EditText) findViewById(R.id.ed_remark);
        ed_amnt = (EditText) findViewById(R.id.ed_amnt);
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
        ls = new ArrayList<>();
        sp_sizeGroup = (Spinner) findViewById(R.id.sp_mode_type);
        list = new ArrayList<>();
        list.add("Cash");list.add("RTGS");list.add("NEFT");list.add("IMPS");
        list.add("Direct Deposit");
        sp_sizeGroup.setAdapter(new ArrayAdapter<>(getApplicationContext(), R.layout.sizegroup_spinner_row, list));

    }

    private void showDia(int a) {
        AlertDialog.Builder builder = new AlertDialog.Builder(OtherDetailsActivity.this);
        builder.setCancelable(false);
        if (a == 0) {
            builder.setMessage("Do You Want To Exit App?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    new Constant(OtherDetailsActivity.this).doFinish();
                    dialog.dismiss();
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        }else if(a==1) {
            builder.setMessage("Select Attachment From...");
            builder.setPositiveButton("Gallery", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    openGallery();
                }
            });
            builder.setNegativeButton("Camera", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    takeimage();
                }
            });
            builder.setNeutralButton("Cancel",new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        }
        builder.create().show();
    }

    private void takeimage(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File f = Constant.checkFolder(Constant.folder_name);
        f = new File(f.getAbsolutePath(), "temp.jpg");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
        startActivityForResult(intent, requestCode);
        overridePendingTransition(R.anim.enter, R.anim.exit);
    }

    private void openGallery(){
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(gallery,REQUEST_IMAGE_PICK_UP);
    }

    private void copyImage(File source, File destination){
        try {
            FileChannel sourcechannel, destinationchannel;
            sourcechannel = new FileInputStream(source).getChannel();
            destinationchannel = new FileOutputStream(destination).getChannel();
            if(sourcechannel!=null){
                destinationchannel.transferFrom(sourcechannel,0,sourcechannel.size());
            }
            if(sourcechannel!=null){
                sourcechannel.close();
            }
            destinationchannel.close();
            setImage(destination, 0);
        }catch (Exception e){
            e.printStackTrace();
            writeLog("copyImage_"+e.getMessage());
        }
    }

    private void setImage(File f, int i){
        OutputStream outFile;
        try {
            imageView_cheque_img.setVisibility(View.VISIBLE);
            Bitmap bitmap;
            BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
            bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(), bitmapOptions);
            Bitmap bmp = scaleBitmap(f.getAbsolutePath());
            imageView_cheque_img.setImageBitmap(bmp);
            File file;
            if(i == 0) {
                String OImgPath = Environment.getExternalStorageDirectory() + File.separator + Constant.folder_name+"/";
                if (f.delete()) {
                   Constant.showLog("Delete");
                }
                long datetime = System.currentTimeMillis();
                SimpleDateFormat sdf = new SimpleDateFormat("dd_MMM_yyyy_HH_mm_ss", Locale.ENGLISH);
                Date resultdate = new Date(datetime);
                String type = list.get(sp_sizeGroup.getSelectedItemPosition());
                imagePath = "C_"+custId+"_Other_"+type+"_" + sdf.format(resultdate) + ".jpg";
                file = new File(OImgPath, "/" +imagePath);
                //String fname = "IMG_" + sdf.format(resultdate);
                //file = new File( fname + ".jpg");
                //imagePath = fname + ".jpg";
            }else{
                file = f;
                imagePath = f.getAbsolutePath();
                imagePath = f.getName();
            }
            try {
                outFile = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 15, outFile);
                outFile.flush();
                outFile.close();
            } catch (Exception e) {
                e.printStackTrace();
                writeLog("setImage_1_"+e.getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
            writeLog("setImage_2_"+e.getMessage());
        }
    }

    private void validations() {
        if (ed_bank.getText().toString().equals("")) {
            toast.setText("Please Select Bank Name");
            toast.show();
        } else if (ed_branch.getText().toString().equals("")) {
            toast.setText("Please Select Branch Name");
            toast.show();
        } /*else if (tv_chq_date.getText().toString().equals("")) {
            toast.setText("Please Select Cheque Date");
            toast.show();
        } else if (ed_total_chq.getText().toString().equals("")) {
            toast.setText("Please Enter Number of Cheques");
            toast.show();
        } else if (ed_mode_type.getText().toString().equals("")) {
            toast.setText("Please Enter Cheque Number");
            toast.show();
        } else if (ed_remark.getText().toString().equals("")) {
            toast.setText("Please Enter Cheque Amount");
            toast.show();
        }*/ else if (ed_amnt.getText().toString().equals("")) {
            toast.setText("Please,Enter Amount");
            toast.show();
        } else {
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

            //String number = ed_mode_type.getText().toString();
            String number = list.get(sp_sizeGroup.getSelectedItemPosition());
            chequeDetails.setChq_det_number(number);

            String amount = ed_amnt.getText().toString();
            int tot = Integer.parseInt(amount);
            VisitPaymentFormActivity.total = VisitPaymentFormActivity.total + tot;
            chequeDetails.setChq_det_amt(amount);

            String ref = ed_remark.getText().toString();
            chequeDetails.setChq_det_ref(ref);

            chequeDetails.setChq_det_image(imagePath);
            Constant.showLog("file_name: " + imagePath);

            String bankName = ed_bank.getText().toString();
            String bankBranch = ed_branch.getText().toString();

            chequeDetails.setCustBankName(bankName);
            chequeDetails.setCustBankBranch(bankBranch);

            VisitPaymentFormActivity.ls.add(chequeDetails);
            //clearFields();
            VisitPaymentFormActivity.isChequeDataSaved = 2;
            new Constant(OtherDetailsActivity.this).doFinish();
        }catch (Exception e){
            e.printStackTrace();
            writeLog("ChequeDetailsActivity_get_Data_"+e.getMessage());
            toast.setText("Something went wrong");
            toast.show();
        }
        //finish();
        //new Constant(ChequeDetailsActivityChanged.this).doFinish();
    }

    private void get_auto_banklist() {
        ed_bank.setText(ChequeDetailsActivityChanged.selectAuto.getChq_auto_bank());
        Constant.showLog("ed_bank: " + ChequeDetailsActivityChanged.selectAuto.getChq_auto_bank());
    }

    private void get_auto_branchlist() {
        ed_branch.setText(ChequeDetailsActivityChanged.selectAuto.getChq_auto_branch());
        Constant.showLog("ed_branch: " + ChequeDetailsActivityChanged.selectAuto.getChq_auto_branch());
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

    private void writeLog(String _data) {
        new WriteLog().writeLog(getApplicationContext(), "OtherDetailsActivity_" + _data);
    }
}

