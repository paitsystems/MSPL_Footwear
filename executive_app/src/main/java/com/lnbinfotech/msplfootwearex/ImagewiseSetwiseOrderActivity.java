package com.lnbinfotech.msplfootwearex;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.lnbinfotech.msplfootwearex.adapters.ViewPagerAdapter;
import com.lnbinfotech.msplfootwearex.constant.Constant;
import com.lnbinfotech.msplfootwearex.fragments.IGentsCategoryFragment;
import com.lnbinfotech.msplfootwearex.fragments.IHawaiNEvaCategoryFragment;
import com.lnbinfotech.msplfootwearex.fragments.ILadiesNBoysCategoryFragment;
import com.lnbinfotech.msplfootwearex.fragments.ISchoolShoesCategoryFragment;

public class ImagewiseSetwiseOrderActivity extends AppCompatActivity implements View.OnClickListener {

    private Constant constant;
    private Toast toast;
    private ViewPagerAdapter adapter;
    private TabLayout tabLayout;
    private ViewPager pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(Constant.liveTestFlag==1) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }

        setContentView(R.layout.activity_imagewise_setwise_order);

        init();

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.takeorder);
        }

        setViewPager();
        tabLayout.setupWithViewPager(pager);
        createTabIcons();

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
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new IGentsCategoryFragment(),"GENTS");
        adapter.addFragment(new ILadiesNBoysCategoryFragment(),"LADIES & BOYS");
        adapter.addFragment(new IHawaiNEvaCategoryFragment(),"HAWAI & EVA");
        adapter.addFragment(new ISchoolShoesCategoryFragment(),"SCHOOL SHOES");
        pager.setAdapter(adapter);
    }

    private void init() {
        toast = Toast.makeText(getApplicationContext(),"",Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER,0,0);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        pager = (ViewPager) findViewById(R.id.pager);
        FirstActivity.pref = getSharedPreferences(FirstActivity.PREF_NAME,MODE_PRIVATE);
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
