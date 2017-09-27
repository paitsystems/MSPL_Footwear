package com.lnbinfotech.msplfootwearex;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.Toast;

import com.lnbinfotech.msplfootwearex.adapters.AreawiseCustSelListAdapter;
import com.lnbinfotech.msplfootwearex.constant.Constant;
import com.lnbinfotech.msplfootwearex.model.AreawiseCustomerSelectionClass;

import java.util.ArrayList;
import java.util.List;

public class AreawiseCustomerSelectionActivity extends AppCompatActivity implements View.OnClickListener {

    private Constant constant, constant1;
    private Toast toast;
    private ListView listView;
    private List<AreawiseCustomerSelectionClass> areaList;
    private AreawiseCustSelListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_areawise_customer_selection);

        init();

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                AreawiseCustomerSelectionClass areaClass = (AreawiseCustomerSelectionClass) listView.getItemAtPosition(i);
                Constant.showLog(areaClass.getAreaname());
                startActivity(new Intent(getApplicationContext(),VisitOptionsActivity.class));
                overridePendingTransition(R.anim.enter,R.anim.exit);
            }
        });

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
        new Constant(AreawiseCustomerSelectionActivity.this).doFinish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //showDia(0);
                new Constant(AreawiseCustomerSelectionActivity.this).doFinish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void init() {
        constant = new Constant(AreawiseCustomerSelectionActivity.this);
        constant1 = new Constant(getApplicationContext());
        toast = Toast.makeText(getApplicationContext(),"",Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER,0,0);
        listView = (ListView) findViewById(R.id.listView);
        areaList = new ArrayList<>();
        AreawiseCustomerSelectionClass areaClass = new AreawiseCustomerSelectionClass();
        areaClass.setAreaname("Aundh Line");
        areaList.add(areaClass);
        AreawiseCustomerSelectionClass areaClass1 = new AreawiseCustomerSelectionClass();
        areaClass1.setAreaname("Dhayri Line");
        areaList.add(areaClass1);
        AreawiseCustomerSelectionClass areaClass2 = new AreawiseCustomerSelectionClass();
        areaClass2.setAreaname("Hadapsar Line");
        areaList.add(areaClass2);
        AreawiseCustomerSelectionClass areaClass3 = new AreawiseCustomerSelectionClass();
        areaClass3.setAreaname("Kondhwa Line");
        areaList.add(areaClass3);
        AreawiseCustomerSelectionClass areaClass4 = new AreawiseCustomerSelectionClass();
        areaClass4.setAreaname("Pune City Line");
        areaList.add(areaClass4);
        AreawiseCustomerSelectionClass areaClass5 = new AreawiseCustomerSelectionClass();
        areaClass5.setAreaname("Shirur Line");
        areaList.add(areaClass5);
        adapter = new AreawiseCustSelListAdapter(getApplicationContext(),areaList);
        listView.setAdapter(adapter);
    }

    private void showDia(int a) {
        AlertDialog.Builder builder = new AlertDialog.Builder(AreawiseCustomerSelectionActivity.this);
        builder.setCancelable(false);
        if (a == 0) {
            builder.setMessage("Do You Want To Exit App?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    new Constant(AreawiseCustomerSelectionActivity.this).doFinish();
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
