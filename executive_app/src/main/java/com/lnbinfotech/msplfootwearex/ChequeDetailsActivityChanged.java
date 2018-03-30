package com.lnbinfotech.msplfootwearex;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.view.Gravity;

import com.lnbinfotech.msplfootwearex.adapters.ChequeDetailChangedAdapter;
import com.lnbinfotech.msplfootwearex.constant.Constant;
import com.lnbinfotech.msplfootwearex.log.WriteLog;
import com.lnbinfotech.msplfootwearex.model.ChequeDetailsClass;
import com.lnbinfotech.msplfootwearex.model.SelectAutoItemClass;

import java.util.ArrayList;
import java.util.List;

public class ChequeDetailsActivityChanged extends AppCompatActivity implements View.OnClickListener{

    private ListView listView;
    private EditText ed_branch, ed_bank, ed_chq_no;
    private AppCompatButton btn_submit;
    private Toast toast;
    private String auto_type;
    private Menu menu;
    public static SelectAutoItemClass selectAuto;
    private List<ChequeDetailsClass> list;

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
                String str = ed_chq_no.getText().toString();
                if(!str.equals("")){
                    setData();
                }else{
                    list.clear();
                    listView.setAdapter(null);
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

    private void init() {
        toast = Toast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        selectAuto = new SelectAutoItemClass();
        ed_bank = (EditText) findViewById(R.id.ed_bank);
        ed_branch = (EditText) findViewById(R.id.ed_branch);
        ed_chq_no = (EditText) findViewById(R.id.ed_total_chq);
        btn_submit = (AppCompatButton) findViewById(R.id.btn_submt);
        listView = (ListView) findViewById(R.id.listView);
        list = new ArrayList<>();
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
        for(int i=1;i<=tot;i++){
            ChequeDetailsClass cheque = new ChequeDetailsClass();
            cheque.setSrNo(i);
            cheque.setChq_det_amt("0");
            cheque.setChq_det_number("0");
            cheque.setChq_det_date("27/Mar/2018");
            list.add(cheque);
        }
        listView.setAdapter(new ChequeDetailChangedAdapter(ChequeDetailsActivityChanged.this, list));
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

    private void writeLog(String _data) {
        new WriteLog().writeLog(getApplicationContext(), "ChequeDetailsActivityChanged_" + _data);
    }
}
