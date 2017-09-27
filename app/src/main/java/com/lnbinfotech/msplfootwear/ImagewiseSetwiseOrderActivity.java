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

import com.lnbinfotech.msplfootwear.adapters.ViewPagerAdapter;
import com.lnbinfotech.msplfootwear.constant.Constant;
import com.lnbinfotech.msplfootwear.fragments.GentsCategoryFragment;
import com.lnbinfotech.msplfootwear.fragments.HawaiNEvaCategoryFragment;
import com.lnbinfotech.msplfootwear.fragments.LadiesNBoysCategoryFragment;
import com.lnbinfotech.msplfootwear.fragments.SchoolShoesCategoryFragment;

public class ImagewiseSetwiseOrderActivity extends AppCompatActivity implements View.OnClickListener {

    private Constant constant;
    private Toast toast;
    private ViewPagerAdapter adapter;
    private TabLayout tabLayout;
    private ViewPager pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imagewise_setwise_order);

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
        new Constant(ImagewiseSetwiseOrderActivity.this).doFinish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //showDia(0);
                new Constant(ImagewiseSetwiseOrderActivity.this).doFinish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setViewPager(){
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new GentsCategoryFragment(),"GENTS");
        adapter.addFragment(new LadiesNBoysCategoryFragment(),"LADIES & BOYS");
        adapter.addFragment(new HawaiNEvaCategoryFragment(),"HAWAI & EVA");
        adapter.addFragment(new SchoolShoesCategoryFragment(),"SCHOOL SHOES");
        pager.setAdapter(adapter);
    }

    private void init() {
        toast = Toast.makeText(getApplicationContext(),"",Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER,0,0);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        pager = (ViewPager) findViewById(R.id.pager);
    }

    private void showDia(int a) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ImagewiseSetwiseOrderActivity.this);
        builder.setCancelable(false);
        if (a == 0) {
            builder.setMessage("Do You Want To Exit App?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    new Constant(ImagewiseSetwiseOrderActivity.this).doFinish();
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
