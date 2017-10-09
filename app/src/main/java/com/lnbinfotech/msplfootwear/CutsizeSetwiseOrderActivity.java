package com.lnbinfotech.msplfootwear;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.lnbinfotech.msplfootwear.constant.Constant;

public class CutsizeSetwiseOrderActivity extends AppCompatActivity implements View.OnClickListener {

    private Constant constant;
    private Toast toast;
    Button btn_gents, btn_ladiesnboys, btn_hawaineva, btn_schoolshoe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cutsize_sizewise_order);

        init();

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        btn_gents.setOnClickListener(this);
        btn_ladiesnboys.setOnClickListener(this);
        btn_hawaineva.setOnClickListener(this);
        btn_schoolshoe.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case 0:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        //showDia(0);
        new Constant(CutsizeSetwiseOrderActivity.this).doFinish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //showDia(0);
                new Constant(CutsizeSetwiseOrderActivity.this).doFinish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void init() {
        toast = Toast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        btn_gents = (Button) findViewById(R.id.btn_gents);
        btn_ladiesnboys = (Button) findViewById(R.id.btn_ladiesnboys);
        btn_hawaineva = (Button) findViewById(R.id.btn_hawaineva);
        btn_schoolshoe = (Button) findViewById(R.id.btn_schoolshoe);
    }

    private void showDia(int a) {
        AlertDialog.Builder builder = new AlertDialog.Builder(CutsizeSetwiseOrderActivity.this);
        if (a == 0) {
            builder.setMessage("Do You Want To Exit App?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    new Constant(CutsizeSetwiseOrderActivity.this).doFinish();
                    dialog.dismiss();
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        }
        builder.create().show();
    }

}
