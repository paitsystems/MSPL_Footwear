package com.lnbinfotech.msplfootwearex;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.MenuItem;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.lnbinfotech.msplfootwearex.constant.Constant;
import com.lnbinfotech.msplfootwearex.db.DBHandler;

import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.Gravity;

public class VisitOptionsActivity extends AppCompatActivity implements View.OnClickListener {

    private Constant constant;
    private Toast toast;
    private TextView tv_arealine,tv_area, tv_shopname_display, tv_address, tv_phone1, tv_custname_display;
    private CardView card_take_order, card_payment, card_ledger_report, card_feedback,card_track_order;
    private int custId = 0;
    private String custName = "", imgName= "NA.jpg";
    private ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(Constant.liveTestFlag==1) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }

        //setContentView(R.layout.activity_visit_option);
        setContentView(R.layout.visit_option_test1);
        //setContentView(R.layout.visitoptionsdemo);

        init();

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.visitoption);
        }
        try {
            String arealine = getIntent().getExtras().getString("area_line");
            String area = getIntent().getExtras().getString("area_name");
            custName = getIntent().getExtras().getString("child_selected");
            custId = Integer.parseInt(getIntent().getExtras().getString("cust_id"));
            DisplayCustListActivity.custId = custId;
            tv_arealine.setText(arealine);
            tv_area.setText(area);
            tv_shopname_display.setText(custName);
            setCustInfo();
        }catch (Exception e){
            e.printStackTrace();
        }
        card_take_order.setOnClickListener(this);
        card_payment.setOnClickListener(this);
        card_ledger_report.setOnClickListener(this);
        card_feedback.setOnClickListener(this);
        card_track_order.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.card_take_order:
                //showDia(1);
                //startActivity(new Intent(getApplicationContext(),DisplayCustListAreawiseActivity.class));
                startActivity(new Intent(getApplicationContext(), CutsizeSetwiseOrderActivity.class));
                overridePendingTransition(R.anim.enter, R.anim.exit);
                break;
            case R.id.card_payment:
                /*toast.setText("Under Development");
                toast.show();*/
                Intent intent = new Intent(getApplicationContext(), VisitPaymentFormActivity.class);
                intent.putExtra("cust_id", String.valueOf(custId));
                intent.putExtra("child_selected", custName);
                startActivity(intent);
                overridePendingTransition(R.anim.enter, R.anim.exit);
                break;
            case R.id.card_ledger_report:
                Intent in = new Intent(getApplicationContext(), LedgerReportActivity.class);
                in.putExtra("cust_id", String.valueOf(custId));
                in.putExtra("child_selected", custName);
                startActivity(in);
                overridePendingTransition(R.anim.enter, R.anim.exit);
                break;
            case R.id.card_feedback:
                startActivity(new Intent(getApplicationContext(), FeedbackActivity.class));
                overridePendingTransition(R.anim.enter, R.anim.exit);
                break;
            case R.id.tv_phone1:
                String str = tv_phone1.getText().toString();
                if(!str.equals("") && !str.equals("0")) {
                    Intent phoneIntent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", str, null));
                    startActivity(phoneIntent);
                    overridePendingTransition(R.anim.enter, R.anim.exit);
                }
                break;
            case R.id.card_track_order:
                //toast.setText("Under Development");
                //toast.show();
                startActivity(new Intent(getApplicationContext(), TrackOrderMasterActivity.class));
                overridePendingTransition(R.anim.enter, R.anim.exit);
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

    private void setCustInfo(){
        Cursor res = new DBHandler(getApplicationContext()).getCustInfo(custId);
        if(res.moveToFirst()){
            do{
                tv_custname_display.setText(res.getString(res.getColumnIndex(DBHandler.CM_Name)));
                tv_address.setText(res.getString(res.getColumnIndex(DBHandler.CM_Address)));
                tv_phone1.setText(res.getString(res.getColumnIndex(DBHandler.CM_MobileNo)));
                imgName= res.getString(res.getColumnIndex(DBHandler.CM_ImagePath));
                Glide.with(getApplicationContext()).load(Constant.custimgUrl+imgName)
                        .thumbnail(0.5f)
                        .crossFade()
                        .placeholder(R.drawable.user32)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(img);
            }while (res.moveToNext());
        }
        res.close();
    }

    private void init() {
        tv_shopname_display = (TextView) findViewById(R.id.tv_shopname_display);
        tv_arealine = (TextView) findViewById(R.id.tv_arealine);
        card_take_order = (CardView) findViewById(R.id.card_take_order);
        card_payment = (CardView) findViewById(R.id.card_payment);
        card_ledger_report = (CardView) findViewById(R.id.card_ledger_report);
        card_feedback = (CardView) findViewById(R.id.card_feedback);
        toast = Toast.makeText(getApplicationContext(),"",Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER,0,0);
        tv_address = (TextView) findViewById(R.id.tv_address);
        tv_phone1 = (TextView) findViewById(R.id.tv_phone1);
        tv_phone1.setOnClickListener(this);
        img = (ImageView) findViewById(R.id.img);
        card_track_order = (CardView) findViewById(R.id.card_track_order);
        tv_custname_display = (TextView) findViewById(R.id.tv_custname_display);
        tv_area = (TextView) findViewById(R.id.tv_area);
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
