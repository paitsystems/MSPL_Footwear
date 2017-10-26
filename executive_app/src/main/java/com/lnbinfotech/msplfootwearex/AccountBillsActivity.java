package com.lnbinfotech.msplfootwearex;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.lnbinfotech.msplfootwearex.log.WriteLog;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class AccountBillsActivity extends AppCompatActivity implements View.OnClickListener{
    private SimpleDateFormat sdf;
    private Toast toast;
    private RadioButton rd_all_bills,rd_outs_bills;
    private EditText ed_search;
    private ListView listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_default);
        init();
    }
    private void init(){
        sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        toast = Toast.makeText(getApplicationContext(),"",Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER,0,0);
        rd_all_bills = (RadioButton) findViewById(R.id.rd_all_bills);
        rd_outs_bills = (RadioButton) findViewById(R.id.rd_outs_bills);
        ed_search = (EditText) findViewById(R.id.ed_search);
        listView = (ListView) findViewById(R.id.listView);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
    private void showPopup(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("Do you want to rxit?");
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

    @Override
    public void onClick(View view) {

    }
    private void writeLog(String _data){
        new WriteLog().writeLog(getApplicationContext(),"AccountBillsActivity_" +_data);
    }
}
