package com.lnbinfotech.msplfootwearex;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.MenuItem;
import android.view.View;

import com.lnbinfotech.msplfootwearex.constant.Constant;

import android.widget.TextView;
import android.widget.Toast;
import android.view.Gravity;

public class VisitOptionsActivity extends AppCompatActivity implements View.OnClickListener {

    private Constant constant;
    private Toast toast;
    private TextView tv_arealine_display,tv_custname_display;
    private CardView card_take_order, card_payment, card_ledger_report, card_feedback;
    private int custId = 0;
    private String custName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_visit_option);
        setContentView(R.layout.visit_option_test1);
        //setContentView(R.layout.visitoptionsdemo);

        init();

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.visitoption);
        }
        String arealine = "Selected Arealine-"+getIntent().getExtras().getString("area_name");
        //Constant.showLog(arealine);
        custName = getIntent().getExtras().getString("child_selected");
        custId = Integer.parseInt(getIntent().getExtras().getString("cust_id"));
        DisplayCustListActivity.custId = custId;
        //Constant.showLog(custName);
        //tv_arealine.setText("Selected Arealine-" +arealine);
        tv_arealine_display.setText(arealine);
        tv_custname_display.setText("CustomerName-"+custName);
        card_take_order.setOnClickListener(this);
        card_payment.setOnClickListener(this);
        card_ledger_report.setOnClickListener(this);
        card_feedback.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.card_take_order:
                //showDia(1);
                //startActivity(new Intent(getApplicationContext(),DisplayCustListAreawiseActivity.class));
                startActivity(new Intent(getApplicationContext(),CutsizeSetwiseOrderActivity.class));
                overridePendingTransition(R.anim.enter, R.anim.exit);
                break;
            case R.id.card_payment:
                toast.setText("Under Development");
                toast.show();
                /*Intent intent =  new Intent(getApplicationContext(),VisitPaymentFormActivity.class);
                intent.putExtra("cust_id",String.valueOf(custId));
                intent.putExtra("child_selected",custName);
                startActivity(intent);
                overridePendingTransition(R.anim.enter,R.anim.exit);*/
                break;
            case R.id.card_ledger_report:
                Intent in =  new Intent(getApplicationContext(),LedgerReportActivity.class);
                in.putExtra("cust_id",String.valueOf(custId));
                in.putExtra("child_selected",custName);
                startActivity(in);
                overridePendingTransition(R.anim.enter,R.anim.exit);
                break;
            case R.id.card_feedback:
                startActivity(new Intent(getApplicationContext(),FeedbackActivity.class));
                overridePendingTransition(R.anim.enter,R.anim.exit);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        //showDia(0);
        new Constant(VisitOptionsActivity.this).doFinish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //showDia(0);
                new Constant(VisitOptionsActivity.this).doFinish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void init() {
        tv_custname_display = (TextView) findViewById(R.id.tv_custname_display);
        tv_arealine_display = (TextView) findViewById(R.id.tv_arealine_display);
        card_take_order = (CardView) findViewById(R.id.card_take_order);
        card_payment = (CardView) findViewById(R.id.card_payment);
        card_ledger_report = (CardView) findViewById(R.id.card_ledger_report);
        card_feedback = (CardView) findViewById(R.id.card_feedback);
        toast = Toast.makeText(getApplicationContext(),"",Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER,0,0);

    }

    private void showDia(int a) {
        AlertDialog.Builder builder = new AlertDialog.Builder(VisitOptionsActivity.this);
        builder.setCancelable(false);
        if (a == 0) {
            builder.setMessage("Do You Want To Exit App?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    new Constant(VisitOptionsActivity.this).doFinish();
                    dialog.dismiss();
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        }if (a == 1) {
            builder.setTitle("Take Order");
            builder.setMessage("How do you want to take order?");
            builder.setPositiveButton("Imagewise", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startActivity(new Intent(getApplicationContext(),ImagewiseSetwiseOrderActivity.class));
                    overridePendingTransition(R.anim.enter,R.anim.exit);
                    dialog.dismiss();
                }
            });
            builder.setNegativeButton("Cutsize", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                   // startActivity(new Intent(getApplicationContext(),CutsizeSetwiseOrderActivity.class));
                    startActivity(new Intent(getApplicationContext(),DisplayCustListAreawiseActivity.class));
                    overridePendingTransition(R.anim.enter,R.anim.exit);
                    dialog.dismiss();
                }
            });
            builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        }
        builder.create().show();
    }
}
