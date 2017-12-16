package com.lnbinfotech.msplfootwear;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.lnbinfotech.msplfootwear.adapters.CutsizewiseViewPagerAdapter;
import com.lnbinfotech.msplfootwear.constant.Constant;
import com.lnbinfotech.msplfootwear.db.DBHandler;
import com.lnbinfotech.msplfootwear.fragments.CGentsCategoryFragment;
import com.lnbinfotech.msplfootwear.fragments.CHawaiNEvaCategoryFragment;
import com.lnbinfotech.msplfootwear.fragments.CLadiesNBoysCategoryFragment;
import com.lnbinfotech.msplfootwear.fragments.CSchoolShoesCategoryFragment;

public class CutsizeSetwiseOrderActivity extends AppCompatActivity implements View.OnClickListener {

    private TabLayout tabLayout;
    private ViewPager pager;
    private CutsizewiseViewPagerAdapter adapter;
    private Constant constant;
    private Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cutsize_sizewise_order);

        init();

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        setViewPager();
        tabLayout.setupWithViewPager(pager);
        createTabIcons();

        FirstActivity.pref = getSharedPreferences(FirstActivity.PREF_NAME,MODE_PRIVATE);
        int custId = FirstActivity.pref.getInt(getString(R.string.pref_retailCustId),0);
        OptionsActivity.custDisc = new DBHandler(getApplicationContext()).getCustDiscount(custId);
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
