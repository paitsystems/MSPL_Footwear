package com.lnbinfotech.msplfootwearex;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
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
import com.lnbinfotech.msplfootwearex.db.DBHandler;
import com.lnbinfotech.msplfootwearex.model.AreawiseCustomerSelectionClass;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AreawiseCustomerSelectionActivity extends AppCompatActivity implements View.OnClickListener {

    private Constant constant, constant1;
    private Toast toast;
    private ListView listView;
    private List<AreawiseCustomerSelectionClass> areaList;
    public static HashMap<String ,String> hashmap;
    private AreawiseCustSelListAdapter adapter;
    private DBHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_areawise_customer_selection);

        init();

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.visit);
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                AreawiseCustomerSelectionClass areaClass = (AreawiseCustomerSelectionClass) listView.getItemAtPosition(i);
                Constant.showLog(areaClass.getAreaname());
                String area_name = areaClass.getAreaname();
                Cursor cursor = db.getAreaId(area_name);
                if(cursor.moveToFirst()){
                    do{
                       // AreawiseCustomerSelectionClass areaClass_ = new AreawiseCustomerSelectionClass();
                        String id = cursor.getString(cursor.getColumnIndex(DBHandler.Area_Id));
                        Constant.showLog("id:"+id);
                        hashmap.put(area_name,id);
                    }while (cursor.moveToNext());
                }
                cursor.close();
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
        hashmap = new HashMap<>();
        db = new DBHandler(AreawiseCustomerSelectionActivity.this);
        constant = new Constant(AreawiseCustomerSelectionActivity.this);
        constant1 = new Constant(getApplicationContext());
        toast = Toast.makeText(getApplicationContext(),"",Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER,0,0);
        listView = (ListView) findViewById(R.id.listView);
        areaList = new ArrayList<>();


        areaName();

        /*AreawiseCustomerSelectionClass areaClass = new AreawiseCustomerSelectionClass();
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
        AreawiseCustomerSelectionClass areaClass6 = new AreawiseCustomerSelectionClass();
        areaClass5.setAreaname("Area Line");
        areaList.add(areaClass6);*/
        adapter = new AreawiseCustSelListAdapter(getApplicationContext(),areaList);
        listView.setAdapter(adapter);
    }

    private  void areaName(){
        Cursor cursor =  db.getAreaName(FirstActivity.pref.getInt(getString(R.string.pref_cityid),0));
        if(cursor.moveToFirst()){
            do{
                AreawiseCustomerSelectionClass  areaclass = new AreawiseCustomerSelectionClass();
                areaclass.setAreaname(cursor.getString(cursor.getColumnIndex(DBHandler.Area_Area)));
                areaList.add(areaclass);
                Constant.showLog("arealist:"+areaList.size());
            }while (cursor.moveToNext());
        }
        cursor.close();
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
