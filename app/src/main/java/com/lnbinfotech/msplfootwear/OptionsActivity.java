package com.lnbinfotech.msplfootwear;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.lnbinfotech.msplfootwear.constant.Constant;

public class OptionsActivity extends AppCompatActivity implements View.OnClickListener {

    Button btn_give_order, btn_account, btn_track_order, btn_profile, btn_scheme, btn_whats_new, btn_feedback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        init();

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        btn_give_order.setOnClickListener(this);
        btn_account.setOnClickListener(this);
        btn_track_order.setOnClickListener(this);
        btn_profile.setOnClickListener(this);
        btn_scheme.setOnClickListener(this);
        btn_whats_new.setOnClickListener(this);
        btn_feedback.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_give_order:
                showDia(1);
                break;
            case R.id.btn_account:
                startActivity(new Intent(getApplicationContext(),CustomerAccountActivity.class));
                overridePendingTransition(R.anim.enter,R.anim.exit);
                break;
            case R.id.btn_track_order:
                startActivity(new Intent(getApplicationContext(),TrackOrderActivity.class));
                overridePendingTransition(R.anim.enter,R.anim.exit);
                break;
            case R.id.btn_profile:
                startActivity(new Intent(getApplicationContext(),UpdateProfileActivity.class));
                overridePendingTransition(R.anim.enter,R.anim.exit);
                break;
            case R.id.btn_scheme:
                break;
            case R.id.btn_whats_new:
                break;
            case R.id.btn_feedback:
                startActivity(new Intent(getApplicationContext(),FeedbackActivity.class));
                overridePendingTransition(R.anim.enter,R.anim.exit);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        showDia(0);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                showDia(0);
                break;
            case R.id.refresh:
                startActivity(new Intent(getApplicationContext(), DataRefreshActivity.class));
                overridePendingTransition(R.anim.enter, R.anim.exit);
                break;
            case R.id.logout:
                break;
            case R.id.report_error:
                showDia(6);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void init() {
        btn_give_order = (Button) findViewById(R.id.btn_give_order);
        btn_account = (Button) findViewById(R.id.btn_account);
        btn_track_order = (Button) findViewById(R.id.btn_track_order);
        btn_profile = (Button) findViewById(R.id.btn_profile);
        btn_scheme = (Button) findViewById(R.id.btn_scheme);
        btn_whats_new = (Button) findViewById(R.id.btn_whats_new);
        btn_feedback = (Button) findViewById(R.id.btn_feedback);
    }

    private void showDia(int a) {
        AlertDialog.Builder builder = new AlertDialog.Builder(OptionsActivity.this);
        builder.setCancelable(false);
        if (a == 0) {
            builder.setMessage("Do You Want To Exit App?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    new Constant(OptionsActivity.this).doFinish();
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
                    startActivity(new Intent(getApplicationContext(),CutsizeSetwiseOrderActivity.class));
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
