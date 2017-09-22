package com.lnbinfotech.msplfootwearex;


import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import com.lnbinfotech.msplfootwearex.adapters.SetAutoItemAdapter;
import java.util.ArrayList;
import java.util.List;

public class SelectAutoItemActivity extends AppCompatActivity {
   private TextInputEditText ed_cus_name,ed_branch,ed_bank;
   private List<String> cus_list,bank_list,branch_list ;

    private LinearLayout cus_lay,bank_lay,branch_lay;
   private ListView lv_cus,lv_bank,lv_branch;

    //String[] cus_name = {"sneha","pooja","rohini","poonam","nikita","karan","neha","nita"};
    //String[] bank_name = {"SBI","BOM","BOB","BOI","ICC Bank","kotak","RBI"};
    //String[] branch_name = {"pune","mumbai","satara","nashik","karad","mahabaleshwar","dhule"};
   // int flag;
    String get_type="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_auto_item);
        init();


    }

    void init(){
        ed_cus_name = (TextInputEditText) findViewById(R.id.ed_cus_name);
        ed_bank = (TextInputEditText) findViewById(R.id.ed_bank);
        ed_branch = (TextInputEditText) findViewById(R.id.ed_branch);

        cus_lay = (LinearLayout) findViewById(R.id.cus_lay);
        bank_lay = (LinearLayout) findViewById(R.id.bank_lay);
        branch_lay = (LinearLayout) findViewById(R.id.branch_lay);



        lv_cus = (ListView) findViewById(R.id.lv_cus);
        lv_bank = (ListView) findViewById(R.id.lv_bank);
        lv_branch = (ListView) findViewById(R.id.lv_branch);
        cus_list = new ArrayList<>();
        bank_list = new ArrayList<>();
        branch_list = new ArrayList<>();

        cus_list.add(0,"sneha");
        cus_list.add(1,"pooja");
        cus_list.add(2,"kiran");
        cus_list.add(3,"aniket");
        cus_list.add(4,"poonam");
        cus_list.add(5,"neha");

        bank_list.add(0,"BOB");
        bank_list.add(1,"BOM");

        branch_list.add(0,"Pune");
        branch_list.add(1,"Mumbai");

        get_type = getIntent().getStringExtra("Auto_type");
        get_auto_type();


        ed_cus_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setCusList();
            }
        });
        ed_bank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setBankList();
            }
        });
        ed_branch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setBranchList();

            }
        });

        lv_cus.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                //if(get_type.equals("cus")) {

                    String select_item = cus_list.get(position);
                    VisitPaymentFormActivity.visit.setCustomer_name(select_item);
                    ed_cus_name.setText(select_item);

                    Log.d("Log", "selcted_item: " + VisitPaymentFormActivity.visit.getCustomer_name());
                    finish();
                    //Intent intent1 = new Intent(SelectAutoItemActivity.this, VisitPaymentFormActivity.class);
                    //intent1.putExtra("Customer_name", select_item);
                    //startActivity(intent1);
               // }
                /*if(get_type.equals("bank")) {
                    String select_item = bank_list.get(position);
                    ed_bank.setText(select_item);
                    Log.d("Log", "selcted_item: " + select_item);
                    Intent intent2 = new Intent(SelectAutoItemActivity.this, VisitPaymentFormActivity.class);
                    intent2.putExtra("Bank_name", select_item);
                    startActivity(intent2);
                }
                if(get_type.equals("branch")) {
                    String select_item = branch_list.get(position);
                    ed_branch.setText(select_item);
                    Log.d("Log", "selcted_item: " + select_item);
                    Intent intent3 = new Intent(SelectAutoItemActivity.this, VisitPaymentFormActivity.class);
                    intent3.putExtra("Branch_name", select_item);
                    startActivity(intent3);
                }*/


            }
        });
        lv_bank.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {


                String select_item = bank_list.get(position);
                VisitPaymentFormActivity.visit.setCheque_bank(select_item);
                ed_bank.setText(select_item);
                Log.d("Log", "selcted_item: " + select_item);
                finish();
                /*Intent intent2 = new Intent(SelectAutoItemActivity.this, VisitPaymentFormActivity.class);
                intent2.putExtra("Bank_name", select_item);
                startActivity(intent2);*/
            }
        });
        lv_branch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {


                String select_item = branch_list.get(position);
                VisitPaymentFormActivity.visit.setCheque_branch(select_item);
                ed_branch.setText(select_item);
                Log.d("Log", "selcted_item: " + select_item);
                finish();
                /*Intent intent1 = new Intent(SelectAutoItemActivity.this, VisitPaymentFormActivity.class);
                intent1.putExtra("Branch_name", select_item);
                startActivity(intent1);*/
            }
        });



    }

    public void setCusList(){
        //TODO create db.getCustomerList()to database containing query "select customer_name from table";
     /*TODO   Cursor cur = db.getCustomerList();
        while(cur.moveToNext()){
            cus_list.add(cur.getString(cur.getColumnIndex("Name")));
        }
        cur.close;*/
        lv_cus.setAdapter(new SetAutoItemAdapter(this,cus_list));

    }
    public void setBankList(){
        //TODO create db.getBankList()to database containing query "select bank_name from table";
     /*TODO   Cursor cur = db.getBankList();
        while(cur.moveToNext()){
            bank_list.add(cur.getString(cur.getColumnIndex("Bank_name")));
        }
        cur.close;*/

        lv_bank.setAdapter(new SetAutoItemAdapter(this,bank_list));

    }
    public void setBranchList(){
        //TODO create db.getBranchList()to database containing query "select branch_name from table";
     /*TODO   Cursor cur = db.getBranchList();
        while(cur.moveToNext()){
            branch_list.add(cur.getString(cur.getColumnIndex("Branch_name")));
        }
        cur.close;*/
        lv_branch.setAdapter(new SetAutoItemAdapter(this,branch_list));

    }
    void get_auto_type(){
        if(get_type.equals("cus")) {

            cus_lay.setVisibility(View.VISIBLE);
            bank_lay.setVisibility(View.GONE);
            branch_lay.setVisibility(View.GONE);

        }
        if(get_type.equals("bank")) {

            cus_lay.setVisibility(View.GONE);
            bank_lay.setVisibility(View.VISIBLE);
            branch_lay.setVisibility(View.GONE);

        }
        if(get_type.equals("branch")) {

            cus_lay.setVisibility(View.GONE);
            bank_lay.setVisibility(View.GONE);
            branch_lay.setVisibility(View.VISIBLE);

        }

    }
}
