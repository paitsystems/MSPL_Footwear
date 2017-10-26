package com.lnbinfotech.msplfootwearex;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;
import android.view.Gravity;

import com.lnbinfotech.msplfootwearex.constant.Constant;
import com.lnbinfotech.msplfootwearex.log.WriteLog;

public class AddToCartActivity extends AppCompatActivity implements View.OnClickListener {

    private Constant constant, constant1;
    private Toast toast;
    private EditText ed_prod_search;
    private RadioButton rdo_pack, rdo_unpack;
    private String cat2, cat9;
    public static String selProd = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_cart);

        init();

        cat9 = getIntent().getExtras().getString("cat9");
        cat2 = getIntent().getExtras().getString("cat2");

        ed_prod_search.setOnClickListener(this);
        rdo_pack.setOnClickListener(this);
        rdo_unpack.setOnClickListener(this);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(selProd!=null){
            ed_prod_search.setText(selProd);
        }
        constant = new Constant(AddToCartActivity.this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ed_prod_search:
                Intent intent = new Intent(getApplicationContext(), ProductSearchActivity.class);
                intent.putExtra("cat9",cat9);
                intent.putExtra("cat2",cat2);
                startActivity(intent);
                overridePendingTransition(R.anim.enter,R.anim.exit);
                break;
            case R.id.rdo_pack:
                rdo_pack.setChecked(true);
                rdo_unpack.setChecked(false);
                break;
            case R.id.rdo_unpack:
                rdo_pack.setChecked(false);
                rdo_unpack.setChecked(true);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        //showDia(0);
        selProd = null;
        new Constant(AddToCartActivity.this).doFinish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //showDia(0);
                selProd = null;
                new Constant(AddToCartActivity.this).doFinish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void init() {
        constant = new Constant(AddToCartActivity.this);
        constant1 = new Constant(getApplicationContext());
        toast = Toast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        ed_prod_search = (EditText) findViewById(R.id.ed_prod_search);
        rdo_pack = (RadioButton) findViewById(R.id.rdo_pack);
        rdo_unpack = (RadioButton) findViewById(R.id.rdo_unpack);
    }

    private void showDia(int a) {
        AlertDialog.Builder builder = new AlertDialog.Builder(AddToCartActivity.this);
        builder.setCancelable(false);
        if (a == 0) {
            builder.setMessage("Do You Want To Exit App?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    new Constant(AddToCartActivity.this).doFinish();
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

    private void writeLog(String _data) {
        new WriteLog().writeLog(getApplicationContext(), "AddToCartActivity_" + _data);
    }
}
