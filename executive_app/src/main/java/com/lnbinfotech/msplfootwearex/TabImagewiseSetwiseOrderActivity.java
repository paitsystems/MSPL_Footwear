package com.lnbinfotech.msplfootwearex;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TabHost;
import android.widget.Toast;
import android.view.Gravity;

import com.lnbinfotech.msplfootwearex.constant.Constant;
import com.lnbinfotech.msplfootwearex.fragments.IGentsCategoryFragment;
import com.lnbinfotech.msplfootwearex.fragments.TabChildFragment;
import com.lnbinfotech.msplfootwearex.fragments.TabParentFragment;
import com.lnbinfotech.msplfootwearex.log.WriteLog;

public class TabImagewiseSetwiseOrderActivity extends FragmentActivity implements View.OnClickListener {

    private Constant constant, constant1;
    private Toast toast;
    private FragmentTabHost tabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(Constant.liveTestFlag==1) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }

        setContentView(R.layout.activity_tab_imagewise_setwise_order);

        init();

        /*if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }*/

        tabHost = (FragmentTabHost) findViewById(R.id.tabHost);
        assert tabHost != null;
        tabHost.setup(this,getSupportFragmentManager(),R.id.tabcontent);
        String arr[] = new String[]{"GENTS","LADIES-N-BOYS","HAWAI-N-EVA","SCHOOL SHOES"};
        for(String str:arr){
            tabHost.addTab(tabHost.newTabSpec("gents").setIndicator(str), TabParentFragment.class,null);
        }
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
        new Constant(TabImagewiseSetwiseOrderActivity.this).doFinish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //showDia(0);
                new Constant(TabImagewiseSetwiseOrderActivity.this).doFinish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void init() {
        constant = new Constant(TabImagewiseSetwiseOrderActivity.this);
        constant1 = new Constant(getApplicationContext());
        toast = Toast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
    }

    private void showDia(int a) {
        AlertDialog.Builder builder = new AlertDialog.Builder(TabImagewiseSetwiseOrderActivity.this);
        builder.setCancelable(false);
        if (a == 0) {
            builder.setMessage("Do You Want To Exit App?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    new Constant(TabImagewiseSetwiseOrderActivity.this).doFinish();
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
        new WriteLog().writeLog(getApplicationContext(), "TabImagewiseSetwiseOrderActivity_" + _data);
    }
}
