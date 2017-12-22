package com.lnbinfotech.msplfootwearex;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.Gravity;

import com.lnbinfotech.msplfootwearex.adapters.CutsizewiseViewPagerAdapter;
import com.lnbinfotech.msplfootwearex.constant.Constant;
import com.lnbinfotech.msplfootwearex.db.DBHandler;
import com.lnbinfotech.msplfootwearex.fragments.CGentsCategoryFragment;
import com.lnbinfotech.msplfootwearex.fragments.CHawaiNEvaCategoryFragment;
import com.lnbinfotech.msplfootwearex.fragments.CLadiesNBoysCategoryFragment;
import com.lnbinfotech.msplfootwearex.fragments.CSchoolShoesCategoryFragment;

public class CutsizeSetwiseOrderActivity extends AppCompatActivity implements View.OnClickListener {

    private TabLayout tabLayout;
    private ViewPager pager;
    private CutsizewiseViewPagerAdapter adapter;
    private Constant constant;
    private Toast toast;
    private TextView tv_custname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cutsize_sizewise_order);

        DisplayCustOutstandingActivity.outClass = null;

        init();

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.takeorder);
        }

        setViewPager();
        tabLayout.setupWithViewPager(pager);
        createTabIcons();

        OptionsActivity.custDisc = new DBHandler(getApplicationContext()).getCustDiscount(DisplayCustListActivity.custId);

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
        new Constant(CutsizeSetwiseOrderActivity.this).doFinish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.cutsize_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //showDia(0);
                new Constant(CutsizeSetwiseOrderActivity.this).doFinish();
                break;
            case R.id.prod_search:
                Intent intent = new Intent(getApplicationContext(), ProductSearchActivity.class);
                intent.putExtra("cat9", "0");
                intent.putExtra("cat2", "0");
                intent.putExtra("from", "cutsize");
                startActivity(intent);
                overridePendingTransition(R.anim.enter, R.anim.exit);
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    private void createTabIcons(){
        TextView tab1 = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tab1.setText("GENTS");
        tab1.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.user32, 0, 0);
        tabLayout.getTabAt(0).setCustomView(tab1);

        TextView tab2 = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tab2.setText("LADIES-N-BOYS");
        tab2.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ladies32, 0, 0);
        tabLayout.getTabAt(1).setCustomView(tab2);

        TextView tab3 = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tab3.setText("HAWAI-N-EVA");
        tab3.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.hawaib32, 0, 0);
        tabLayout.getTabAt(2).setCustomView(tab3);

        TextView tab4 = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tab4.setText("SCHOOL SHOES");
        tab4.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.schoolb32, 0, 0);
        tabLayout.getTabAt(3).setCustomView(tab4);

    }

    private void setViewPager(){
        adapter = new CutsizewiseViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new CGentsCategoryFragment(),"GENTS");
        adapter.addFragment(new CLadiesNBoysCategoryFragment(),"LADIES & BOYS");
        adapter.addFragment(new CHawaiNEvaCategoryFragment(),"HAWAI & EVA");
        adapter.addFragment(new CSchoolShoesCategoryFragment(),"SCHOOL SHOES");
        pager.setAdapter(adapter);
    }

    private void init() {
        toast = Toast.makeText(getApplicationContext(),"",Toast.LENGTH_LONG);
        tv_custname = (TextView) findViewById(R.id.tv_custname1);
        tv_custname.setText(FirstActivity.pref.getString(getString(R.string.pref_selcustname),""));
        toast.setGravity(Gravity.CENTER,0,0);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        pager = (ViewPager) findViewById(R.id.pager);
    }

    private void showDia(int a) {
        AlertDialog.Builder builder = new AlertDialog.Builder(CutsizeSetwiseOrderActivity.this);
        if (a == 0) {
            builder.setMessage("Do You Want To Exit App?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    new Constant(CutsizeSetwiseOrderActivity.this).doFinish();
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
