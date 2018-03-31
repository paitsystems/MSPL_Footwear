package com.lnbinfotech.msplfootwearex;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lnbinfotech.msplfootwearex.adapters.AreawiseCustSelExpandableListAdapter;
import com.lnbinfotech.msplfootwearex.adapters.AreawiseCustSelListAdapter;
import com.lnbinfotech.msplfootwearex.constant.Constant;
import com.lnbinfotech.msplfootwearex.db.DBHandler;
import com.lnbinfotech.msplfootwearex.model.ArealineMasterClass;
import com.lnbinfotech.msplfootwearex.model.AreawiseCustomerSelectionClass;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AreawiseCustomerSelectionActivity extends AppCompatActivity implements View.OnClickListener {

    private Constant constant, constant1;
    private Toast toast;
    private ListView listView;
    private ExpandableListView exp_listView;
    private List<AreawiseCustomerSelectionClass> areaList;
    public static HashMap<String ,String> hashmap;
    private AreawiseCustSelListAdapter adapter;
    private AreawiseCustSelExpandableListAdapter adapter1;
    private DBHandler db;
    private HashMap<Integer,List<String>> area_map;
    private List<Integer> areaid_list;
    private HashMap<Integer,List<Integer>> areaid_partyId_map;
    private HashMap<Integer,String> party_map;
    private List<Integer> partyid_list;
    private HashMap<Integer,Integer> area_party_map;
    private HashMap<Integer,List<String>> childls;
    private int areaId = 0,custId = 0;
    private String area_name = "", child_sel = "";
    private ImageView img_parent;
    private int cust_id = 0;
    private ArealineMasterClass areaLineClass;
    private TextView tv_arealine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(Constant.liveTestFlag==1) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }

        setContentView(R.layout.activity_areawise_customer_selection);
        FirstActivity.pref = getSharedPreferences(FirstActivity.PREF_NAME,MODE_PRIVATE);

        init();

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.visit);
        }

        try {
            areaLineClass = (ArealineMasterClass) getIntent().getSerializableExtra("arealine");
            tv_arealine.setText(areaLineClass.getArea());
        }catch (Exception e){
            e.printStackTrace();
            toast.setText("Something Went Wrong");
            toast.show();
        }
       /* listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
                        SharedPreferences.Editor editor = FirstActivity.pref.edit();
                        editor.putString(getString(R.string.areaid),id);
                        editor.apply();
                        Constant.showLog("id:"+id);
                        hashmap.put(area_name,id);
                    }while (cursor.moveToNext());
                }
                cursor.close();
                startActivity(new Intent(getApplicationContext(),VisitOptionsActivity.class).putExtra("area_name",area_name));
                overridePendingTransition(R.anim.enter,R.anim.exit);
            }
        });*/

       /* exp_listView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int group_postion, long l) {
                img_parent.setImageDrawable(getResources().getDrawable(R.drawable.expand_16));
               *//* area_name = String.valueOf(area_map.get(areaid_list.get(group_postion)));
                Constant.showLog("area_name:"+area_name);*//*
                return true;
            }
        });
*/
        exp_listView.setGroupIndicator(null);

        exp_listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int group_postion, int child_position, long l) {
                //String sel_child = (String) adapter1.getChild(group_postion,child_position);
                String a = String.valueOf(area_map.get(areaid_list.get(group_postion)));
                area_name = a.replace("[","").replace("]","");
                //Constant.showLog("area_name:"+area_name);

                child_sel =  party_map.get(areaid_partyId_map.get(areaid_list.get(group_postion)).get(child_position));
                //Constant.showLog("child_selected:"+child_sel);

                cust_id =  db.getCustid(child_sel);
                //Constant.showLog("cust_id:"+cust_id);

                int selCustId = FirstActivity.pref.getInt(getString(R.string.pref_selcustid), 0);
                if(selCustId!=0){
                    if(selCustId!=cust_id){
                        int count = db.getCustOrderDetail();
                        if(count!=0) {
                            showDia(1);
                        }else{
                            saveNCountinue();
                        }
                    }else{
                        saveNCountinue();
                    }
                }else{
                    saveNCountinue();
                }
                return true;
            }
        });

        areaName();

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
        exp_listView = (ExpandableListView) findViewById(R.id.exp_listView);
        tv_arealine = (TextView) findViewById(R.id.tv_arealine);
        areaList = new ArrayList<>();

        area_map = new HashMap<>();
        party_map = new HashMap<>();
        area_party_map = new HashMap<>();
        areaid_list = new ArrayList<>();
        partyid_list = new ArrayList<>();
        areaid_partyId_map = new HashMap<>();
        childls = new HashMap<>();

        adapter1 = new AreawiseCustSelExpandableListAdapter(getApplicationContext(),area_map, areaid_list, party_map, partyid_list, area_party_map, areaid_partyId_map,childls);
        exp_listView.setAdapter(adapter1);
    }

    private void areaName(){
        String areaIds = "";
        Cursor res = db.getAreaIdAreaLineWise(areaLineClass.getArea());
        if(res.moveToFirst()){
            do {
                areaIds = areaIds + res.getString(res.getColumnIndex(DBHandler.AL_AreaId)) +",";
            }while (res.moveToNext());
        }
        if(!areaIds.equals("")) {
            areaIds = areaIds.substring(0, areaIds.length() - 1);
        }
        res.close();
        if(!areaIds.equals("")) {
            Cursor cursor = db.getExpListData(areaIds);
            //Cursor cursor =  db.getExpListData();
            if (cursor.moveToFirst()) {
                do {
                    String area_name = "", cust_name = "";
                    // AreawiseCustomerSelectionClass  areaclass = new AreawiseCustomerSelectionClass();
                    areaId = cursor.getInt(cursor.getColumnIndex(DBHandler.Area_Id));
                    area_name = cursor.getString(cursor.getColumnIndex(DBHandler.Area_Area));
                    custId = cursor.getInt(cursor.getColumnIndex(DBHandler.CM_RetailCustID));
                    cust_name = cursor.getString(cursor.getColumnIndex(DBHandler.CM_PartyName));

                    if (!areaid_list.contains(areaId)) {
                        areaid_list.add(areaId);
                    }
                    //Constant.showLog("areaid_list:"+areaid_list.size());

                    if (!partyid_list.contains(custId)) {
                        partyid_list.add(custId);
                    }
                    //Constant.showLog("partyid_list:"+partyid_list.size());

                    if (area_map.isEmpty()) {
                        List<String> ar_NameLs = new ArrayList<>();
                        ar_NameLs.add(area_name);
                        area_map.put(areaId, ar_NameLs);
                    } else {
                        if (area_map.containsKey(areaId)) {
                            List<String> ar_NameLs1 = area_map.get(areaId);
                            //List<String> ar_NameLs1  = new ArrayList<>();
                            if (!ar_NameLs1.contains(area_name)) {
                                ar_NameLs1.add(area_name);
                            }

                            area_map.put(areaId, ar_NameLs1);
                        } else {
                            List<String> ar_NameLs = new ArrayList<>();
                            ar_NameLs.add(area_name);
                            area_map.put(areaId, ar_NameLs);
                        }
                    }
                    //Constant.showLog("area_map:"+area_map.size());

                /*if(party_map.isEmpty()) {
                    List<String> cus_NameLs = new ArrayList<>();
                    cus_NameLs.add(cust_name);
                    party_map.put(custId,cus_NameLs);
                }else {
                    if(party_map.containsKey(custId)){
                        List<String> cus_NameLs1 = party_map.get(custId);
                        cus_NameLs1.add(cust_name);
                        party_map.put(custId,cus_NameLs1);
                    }else{
                        List<String> cus_NameLs = new ArrayList<>();
                        cus_NameLs.add(cust_name);
                        party_map.put(custId,cus_NameLs);
                    }
                }
                Constant.showLog("party_map:"+party_map.size());*/

                    if (party_map.isEmpty()) {
                        party_map.put(custId, cust_name);
                    } else {
                        if (party_map.containsKey(custId)) {
                            String s = party_map.get(custId);
                            if (!s.equals(cust_name)) {
                                s = cust_name;
                            }
                            party_map.put(custId, s);
                        } else {
                            party_map.put(custId, cust_name);
                        }
                    }


                    area_party_map.put(areaId, custId);
                    //Constant.showLog("area_party_map:"+area_party_map.size());

               /* if(area_party_map.isEmpty()) {
                    area_party_map.put(areaId,custId);
                }else {
                    if(party_map.containsKey(areaId)){
                        area_party_map.put(areaId,custId);
                    }else{
                        area_party_map.put(areaId,custId);
                    }
                }*/

                    // areaid_partyId_map.put(areaId,)
                    if (areaid_partyId_map.isEmpty()) {
                        List<Integer> cus_IdLs = new ArrayList<>();
                        cus_IdLs.add(custId);
                        areaid_partyId_map.put(areaId, cus_IdLs);
                    } else {
                        if (areaid_partyId_map.containsKey(areaId)) {
                            List<Integer> cus_IdLs1 = areaid_partyId_map.get(areaId);
                            cus_IdLs1.add(custId);
                            areaid_partyId_map.put(areaId, cus_IdLs1);
                        } else {
                            List<Integer> cus_IdLs = new ArrayList<>();
                            cus_IdLs.add(custId);
                            areaid_partyId_map.put(areaId, cus_IdLs);
                        }
                    }
                    //Constant.showLog("areaid_partyId_map:"+areaid_partyId_map.size());

                    if (childls.isEmpty()) {
                        List<String> child_NameLs = new ArrayList<>();
                        child_NameLs.add(cust_name);
                        childls.put(areaId, child_NameLs);
                    } else {
                        if (childls.containsKey(areaId)) {
                            List<String> child_NameLs1 = childls.get(areaId);
                            //List<String> ar_NameLs1  = new ArrayList<>();
                            if (!child_NameLs1.contains(cust_name)) {
                                child_NameLs1.add(cust_name);
                            }
                            childls.put(areaId, child_NameLs1);
                        } else {
                            List<String> child_NameLs = new ArrayList<>();
                            child_NameLs.add(cust_name);
                            childls.put(areaId, child_NameLs);
                        }
                    }
                    //Constant.showLog("childls:"+childls.size());
                } while (cursor.moveToNext());
            }
            cursor.close();
        }else{
            toast.setText("No Record Available");
            toast.show();
        }
    }

    private void saveNCountinue(){
        SharedPreferences.Editor editor = FirstActivity.pref.edit();
        editor.putInt(getString(R.string.pref_selcustid),cust_id);
        editor.putString(getString(R.string.pref_selcustname),child_sel);
        editor.apply();
        Intent in = new Intent(getApplicationContext(),VisitOptionsActivity.class);
        in.putExtra("area_line",areaLineClass.getArea());
        //in.putExtra("area_line","NA");
        in.putExtra("area_name",area_name);
        in.putExtra("child_selected",child_sel);
        in.putExtra("cust_id",String.valueOf(cust_id));
        //finish();
        startActivity(in);
        overridePendingTransition(R.anim.enter,R.anim.exit);
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
        }else if (a == 1) {
            String custName = FirstActivity.pref.getString(getString(R.string.pref_selcustname),"");
            builder.setMessage("There Is Already Pending Order For Customer - "+custName);
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.setNeutralButton("Clear Last Order", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    SharedPreferences.Editor editor = FirstActivity.pref.edit();
                    editor.putInt(getString(R.string.pref_selcustid),0);
                    editor.putString(getString(R.string.pref_selcustname),"");
                    editor.putString("totalNetAmnt","0");
                    DisplayCustOutstandingActivity.outClass = null;
                    editor.apply();
                    db.deleteTable(DBHandler.Table_CustomerOrder);
                    dialog.dismiss();
                    showDia(2);
                }
            });
        }else if (a == 2) {
            builder.setMessage("Previous Order Cleared Successfully");
            builder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    saveNCountinue();
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
