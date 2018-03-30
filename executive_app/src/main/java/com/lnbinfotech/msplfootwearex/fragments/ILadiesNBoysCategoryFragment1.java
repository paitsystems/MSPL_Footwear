package com.lnbinfotech.msplfootwearex.fragments;

//Created by ANUP on 3/6/2018.

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.lnbinfotech.msplfootwearex.R;
import com.lnbinfotech.msplfootwearex.db.DBHandler;

import java.util.ArrayList;
import java.util.List;

public class ILadiesNBoysCategoryFragment1 extends Fragment {

    private LinearLayout mainLayout;
    private TabLayout tabs;
    private ViewPager vpNews;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mainLayout = (LinearLayout) inflater.inflate(R.layout.fragment_scrollable_tab, container, false);
        tabs = (TabLayout) mainLayout.findViewById(R.id.tabScrollable);
        vpNews = (ViewPager) mainLayout.findViewById(R.id.vpNews);
        setUpPager();
        return mainLayout;
    }

    private void setUpPager() {
        NewsPagerAdapter adp = new NewsPagerAdapter(getFragmentManager());
        DBHandler db = new DBHandler(getContext());
        Cursor res = db.getImageSubCategory("Ladies & Kids");
        if(res.moveToFirst()){
            do {
                adp.addFrag(new ILadiesNBoysCategoryFragment(), res.getString(res.getColumnIndex(DBHandler.PM_Cat2)));
            }while (res.moveToNext());
        }
        res.close();
        tabs.setTabMode(TabLayout.MODE_SCROLLABLE);
        vpNews.setAdapter(adp);
        vpNews.setOffscreenPageLimit(12);
        tabs.setupWithViewPager(vpNews);
    }

    private class NewsPagerAdapter extends FragmentPagerAdapter {

        List<Fragment> fragList = new ArrayList<>();
        List<String> titleList = new ArrayList<>();

        public NewsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public void addFrag(Fragment f, String title) {
            fragList.add(f);
            titleList.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return fragList.get(position);
        }

        @Override
        public int getCount() {
            return fragList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titleList.get(position);
        }
    }
}

