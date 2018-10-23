package com.lnbinfotech.msplfootwear.fragments;

//Created by Anup on 10/17/2018.

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.lnbinfotech.msplfootwear.R;
import com.lnbinfotech.msplfootwear.constant.Constant;
import com.lnbinfotech.msplfootwear.db.DBHandler;

import java.util.ArrayList;
import java.util.List;

public class IGentsCategoryFragment1 extends Fragment {

    private LinearLayout mainLayout;
    private TabLayout tabs;
    private ViewPager pager;
    private String cat = "", cat1 = "";
    private Context context;

    public static IGentsCategoryFragment1 newInstance(String _cat){
        IGentsCategoryFragment1 frag = new IGentsCategoryFragment1();
        Bundle args = new Bundle();
        args.putString("cat", _cat);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cat = getArguments().getString("cat");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mainLayout = (LinearLayout) inflater.inflate(R.layout.fragment_scrollable_tab, container, false);
        tabs = (TabLayout) mainLayout.findViewById(R.id.tabScrollable);
        pager = (ViewPager) mainLayout.findViewById(R.id.vpNews);

        setUpPager();
        Constant.showLog(cat+"-onCreateView()");
        return mainLayout;
    }

    private void setUpPager() {
        final ViewPagerAdapter adp = new ViewPagerAdapter(getChildFragmentManager());
        DBHandler db = new DBHandler(getContext());
        if(cat.equals("GENTS")){
            cat1 = "Gents";
        }else if(cat.equals("LADIES-N-BOYS")){
            cat1 = "Ladies & Kids";
        }else if(cat.equals("HAWAI-N-EVA")){
            cat1 = "Hawai & Eva";
        }else if(cat.equals("SCHOOL SHOES")){
            cat1 = "School Shoes";
        }
        Cursor res = db.getImageSubCategory(cat1);
        if(res.moveToFirst()){
            do {
                adp.addFrag(new IGentsCategoryFragment(), res.getString(res.getColumnIndex(DBHandler.PM_Cat2)));
            }while (res.moveToNext());
        }
        res.close();
        tabs.setTabMode(TabLayout.MODE_SCROLLABLE);
        pager.setAdapter(adp);
        //pager.setOffscreenPageLimit(0);
        tabs.setupWithViewPager(pager);
    }

    private class ViewPagerAdapter extends FragmentStatePagerAdapter {

        List<Fragment> fragList = new ArrayList<>();
        List<String> titleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public void addFrag(Fragment f, String title) {
            fragList.add(f);
            titleList.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            //return fragList.get(position);
            return IGentsCategoryFragment.newInstance(cat1,titleList.get(position));
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

