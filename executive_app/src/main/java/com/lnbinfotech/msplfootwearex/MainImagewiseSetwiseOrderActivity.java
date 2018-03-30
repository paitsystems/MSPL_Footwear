package com.lnbinfotech.msplfootwearex;

//Created by ANUP on 3/5/2018.

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.lnbinfotech.msplfootwearex.constant.Constant;
import com.lnbinfotech.msplfootwearex.fragments.IGentsCategoryFragment1;
import java.util.ArrayList;
import java.util.List;

public class MainImagewiseSetwiseOrderActivity extends AppCompatActivity {

    private NonScrollableVP pager;
    private TabLayout tabs;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(Constant.liveTestFlag==1) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }

        setContentView(R.layout.activity_main_imagewisesetwiseorder);
        pager = (NonScrollableVP)findViewById(R.id.pagerMain);
        tabs = (TabLayout) findViewById(R.id.tabs);
        setUpPager();
    }

    private void setUpPager() {
        final ViewPagerClass adapter = new ViewPagerClass(getSupportFragmentManager());
        String arr[] = new String[]{"GENTS", "LADIES-N-BOYS", "HAWAI-N-EVA", "SCHOOL SHOES"};
        for (String str : arr) {
            adapter.addFragment(new IGentsCategoryFragment1(), str);
        }
        pager.setAdapter(adapter);
        tabs.setupWithViewPager(pager);
    }

    private class ViewPagerClass extends FragmentStatePagerAdapter {

        List<Fragment> mFragments = new ArrayList<>();
        List<String> mFragmentsTitle = new ArrayList<>();

        public ViewPagerClass(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment f, String s) {
            mFragments.add(f);
            mFragmentsTitle.add(s);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentsTitle.get(position);
        }

        @Override
        public Fragment getItem(int position) {
            //return mFragments.get(position);
            return IGentsCategoryFragment1.newInstance(mFragmentsTitle.get(position));
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }
    }
}

