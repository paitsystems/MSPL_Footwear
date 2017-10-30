package com.lnbinfotech.msplfootwear;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.lnbinfotech.msplfootwear.adapters.CutsizewiseViewPagerAdapter;
import com.lnbinfotech.msplfootwear.constant.Constant;
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //showDia(0);
                new Constant(CutsizeSetwiseOrderActivity.this).doFinish();
                break;
        }
        return super.onOptionsItemSelected(item);
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
