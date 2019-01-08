package com.lnbinfotech.msplfootwearex;

import android.app.DatePickerDialog;
import android.content.Context;
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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.Gravity;

import com.lnbinfotech.msplfootwearex.adapters.ChequeDetailChangedAdapter;
import com.lnbinfotech.msplfootwearex.constant.Constant;
import com.lnbinfotech.msplfootwearex.interfaces.TestInterface;
import com.lnbinfotech.msplfootwearex.log.WriteLog;
import com.lnbinfotech.msplfootwearex.model.ChequeDetailsClass;
import com.lnbinfotech.msplfootwearex.model.SelectAutoItemClass;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ChequeDetailsActivityChanged extends AppCompatActivity implements View.OnClickListener, TestInterface{

    private ListView listView;
    private EditText ed_branch, ed_bank, ed_chq_no, ed_chq_serial;
    private AppCompatButton btn_submit;
    private Toast toast;
    private String auto_type,imagePath,custId = "";
    private Menu menu;
    public static SelectAutoItemClass selectAuto;
    private List<ChequeDetailsClass> list;
    private ChequeDetailChangedAdapter adapter;
    private int requestCode = 0, mYear, mMonth, mDay;
    public ChequeDetailsClass chequeDetails;
    public static String chequeNo = "0";
    private TextView tv_amntTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(Constant.liveTestFlag==1) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }

        setContentView(R.layout.activity_cheque_details_changed);

        init();

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.payment);
        }

        try {
            custId = getIntent().getExtras().getString("custid");
        } catch (Exception e) {
            e.printStackTrace();
            writeLog("onCreate_" + e.getMessage());
        }

        Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        ed_bank.setOnClickListener(this);
        ed_branch.setOnClickListener(this);
        btn_submit.setOnClickListener(this);
        ed_chq_no.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String str1 = ed_chq_serial.getText().toString();
                if(!str1.equals("")) {
                    String str = ed_chq_no.getText().toString();
                    if (!str.equals("")) {
                        setData();
                    } else {
                        list.clear();
                        listView.setAdapter(null);
                    }
                }else{
                    toast.setText("First Enter Cheque Serial Number");
                    toast.show();
                }
            }
        });
    }

    @Override
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
            case R.id.ed_bank:
                Intent k = new Intent(ChequeDetailsActivityChanged.this, SelectAutoItemActivity.class);
                auto_type = "bank";
                k.putExtra("Auto_type", auto_type);
                startActivity(k);
                overridePendingTransition(R.anim.enter, R.anim.exit);
                writeLog("goes to SelectAutoItemActivity");
                break;
            case R.id.ed_branch:
                Intent i = new Intent(ChequeDetailsActivityChanged.this, SelectAutoItemActivity.class);
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
        showDia(1);
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
                showDia(1);
                break;
            case R.id.chq_save:
                showDia(2);
                break;
            case R.id.chq_clear:
                showDia(3);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResumeFragment(String data1, String data2, Context context) {
        ((InputMethodManager)getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(btn_submit.getWindowToken(),0);
        new DatePickerDialog(ChequeDetailsActivityChanged.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
                    SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MMM/yyyy", Locale.ENGLISH);
                    Date select_date = sdf.parse(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                    String dt = sdf1.format(select_date);
                    Constant.showLog(dt);
                    adapter.returnDate(dt);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, mYear, mMonth, mDay).show();
    }

    @Override
    public void onPauseFragment(String data1, String data2, Context context) {
        chequeNo = data1;
        /*Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File f = Constant.checkFolder(Constant.folder_name);
        f = new File(f.getAbsolutePath(), "temp.jpg");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
        startActivityForResult(intent, requestCode);
        overridePendingTransition(R.anim.enter, R.anim.exit);*/

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File f = Constant.checkFolder(Constant.folder_name);
        f = new File(f.getAbsolutePath(),"temp.jpg");
        Uri photoURI = FileProvider.getUriForFile(getApplicationContext(), getApplicationContext().getPackageName()
                + ".provider", f);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityForResult(intent,requestCode);
        overridePendingTransition(R.anim.enter, R.anim.exit);
    }

    @Override
    public void onAmountChange(int amnt) {
        tv_amntTotal.setText(String.valueOf(amnt));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (this.requestCode == requestCode && resultCode == RESULT_OK) {
            try {
                long datetime = System.currentTimeMillis();
                SimpleDateFormat sdf = new SimpleDateFormat("dd_MMM_yyyy_HH_mm_ss", Locale.ENGLISH);
                Date resultdate = new Date(datetime);
                imagePath = "C_" + custId + "_Cheque_" + chequeNo + "_" + sdf.format(resultdate) + ".jpg";
                Constant.showLog(imagePath);
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
                    adapter.returnImage(imagePath);
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

    private void init() {
        toast = Toast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        selectAuto = new SelectAutoItemClass();
        ed_bank = (EditText) findViewById(R.id.ed_bank);
        ed_branch = (EditText) findViewById(R.id.ed_branch);
        ed_chq_no = (EditText) findViewById(R.id.ed_total_chq);
        btn_submit = (AppCompatButton) findViewById(R.id.btn_submt);
        listView = (ListView) findViewById(R.id.listView);
        tv_amntTotal = (TextView) findViewById(R.id.tv_amntTotal);
        ed_chq_serial = (EditText) findViewById(R.id.ed_chqSerial);
        list = new ArrayList<>();
        FirstActivity.pref = getSharedPreferences(FirstActivity.PREF_NAME,MODE_PRIVATE);
    }

    private void get_auto_banklist() {
        ed_bank.setText(selectAuto.getChq_auto_bank());
        Constant.showLog("ed_bank: " + selectAuto.getChq_auto_bank());
    }

    private void get_auto_branchlist() {
        ed_branch.setText(selectAuto.getChq_auto_branch());
        Constant.showLog("ed_branch: " + selectAuto.getChq_auto_branch());
    }

    private void setData(){
        list.clear();
        listView.setAdapter(null);
        int tot = Integer.parseInt(ed_chq_no.getText().toString());
        String str = ed_chq_serial.getText().toString();
        if(str.equals("")){
           str = "0";
        }
        int chqNo = Integer.parseInt(str);
        for(int i=1;i<=tot;i++){
            ChequeDetailsClass cheque = new ChequeDetailsClass();
            cheque.setSrNo(i);
            cheque.setChq_det_amt("0");
            cheque.setChq_det_number(String.valueOf(chqNo));
            chqNo = chqNo + 1;
            cheque.setChq_det_date(new Constant(getApplicationContext()).getDate());
            list.add(cheque);
        }
        adapter = new ChequeDetailChangedAdapter(getApplicationContext(), list);
        adapter.initInterface(ChequeDetailsActivityChanged.this);
        listView.setAdapter(adapter);
    }

    private void showDia(int a) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ChequeDetailsActivityChanged.this);
        builder.setCancelable(false);
        if(a==1) {
            builder.setMessage("Do You Want To Go Back ?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    VisitPaymentFormActivity.ls.clear();
                    new Constant(ChequeDetailsActivityChanged.this).doFinish();
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
                    new Constant(ChequeDetailsActivityChanged.this).doFinish();
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

    private void validations() {
        if (ed_bank.getText().toString().equals("")) {
            toast.setText("Please Select Bank Name");
            toast.show();
        } else if (ed_branch.getText().toString().equals("")) {
            toast.setText("Please Select Branch Name");
            toast.show();
        } else {
            get_data();
        }
    }

    private void get_data() {
        try {
            for(int i=0;i<list.size();i++) {
                chequeDetails = (ChequeDetailsClass) listView.getItemAtPosition(i);

                String bank = ed_bank.getText().toString();
                chequeDetails.setChq_det_bank(bank);

                String branch = ed_branch.getText().toString();
                chequeDetails.setChq_det_branch(branch);

                String bankName = ed_bank.getText().toString();
                String bankBranch = ed_branch.getText().toString();
                chequeDetails.setCustBankName(bankName);
                chequeDetails.setCustBankBranch(bankBranch);

                String amount = chequeDetails.getChq_det_amt();
                int tot = Integer.parseInt(amount);
                VisitPaymentFormActivity.total = VisitPaymentFormActivity.total + tot;

                VisitPaymentFormActivity.ls.add(chequeDetails);
            }
            Constant.showLog(""+VisitPaymentFormActivity.total);
            VisitPaymentFormActivity.isChequeDataSaved = 1;
            new Constant(ChequeDetailsActivityChanged.this).doFinish();
        }catch (Exception e){
            e.printStackTrace();
            writeLog("ChequeDetailsActivity_get_Data_"+e.getMessage());
            toast.setText("Something went wrong");
            toast.show();
        }
        //finish();
        //new Constant(ChequeDetailsActivity.this).doFinish();
    }

    private void writeLog(String _data) {
        new WriteLog().writeLog(getApplicationContext(), "ChequeDetailsActivityChanged_" + _data);
    }
}
