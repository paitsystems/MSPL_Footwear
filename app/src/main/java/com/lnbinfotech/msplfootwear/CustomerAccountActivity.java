package com.lnbinfotech.msplfootwear;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.view.Gravity;

import com.lnbinfotech.msplfootwear.constant.Constant;

public class CustomerAccountActivity extends AppCompatActivity implements View.OnClickListener {

    private Constant constant, constant1;
    private Toast toast;
    private CardView card_bills,card_gst, card_ledger,card_out;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_account);

        init();
        card_ledger.setOnClickListener(this);
        card_out.setOnClickListener(this);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case  R.id.card_ledger:
                startActivity(new Intent(getApplicationContext(), LedgerReportActivity.class));
                overridePendingTransition(R.anim.enter, R.anim.exit);
                break;
            case  R.id.card_out:
                startActivity(new Intent(getApplicationContext(), OutstandingBillReportActivity.class));
                overridePendingTransition(R.anim.enter, R.anim.exit);
                break;
            case  R.id.card_gst:
                toast.setText("Under Development");
                toast.show();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        //showDia(0);
        new Constant(CustomerAccountActivity.this).doFinish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //showDia(0);
                new Constant(CustomerAccountActivity.this).doFinish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void init() {
        constant = new Constant(CustomerAccountActivity.this);
        constant1 = new Constant(getApplicationContext());
        toast = Toast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        card_bills = (CardView) findViewById(R.id.card_bills);
        card_gst= (CardView) findViewById(R.id.card_gst);
        card_ledger = (CardView) findViewById(R.id.card_ledger);
        card_out = (CardView) findViewById(R.id.card_out);
    }

    private void showDia(int a) {
        AlertDialog.Builder builder = new AlertDialog.Builder(CustomerAccountActivity.this);
        builder.setCancelable(false);
        if (a == 0) {
            builder.setMessage("Do You Want To Exit App?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    new Constant(CustomerAccountActivity.this).doFinish();
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
