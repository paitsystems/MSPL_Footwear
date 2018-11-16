package com.pait.exec.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.pait.exec.R;
import com.pait.exec.adapters.ViewPagerAdapter;
import com.pait.exec.constant.Constant;

//Created by ANUP on 3/5/2018.

public class TabParentFragment extends Fragment {

    private Constant constant;
    private Toast toast;
    private ViewPagerAdapter adapter;
    private TabLayout tabLayout;
    private ViewPager pager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab_parent,container,false);

        tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);
        pager = (ViewPager) view.findViewById(R.id.pager);

        setViewPager();
        tabLayout.setupWithViewPager(pager);
        createTabIcons();

        return view;
    }

    private void createTabIcons(){
        TextView tab1 = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.custom_tab, null);
        tab1.setText("GENTS");
        //tab1.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.user32, 0, 0);
        tabLayout.getTabAt(0).setCustomView(tab1);

        TextView tab2 = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.custom_tab, null);
        tab2.setText("LADIES-N-BOYS");
        //tab2.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ladies32, 0, 0);
        tabLayout.getTabAt(1).setCustomView(tab2);

        TextView tab3 = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.custom_tab, null);
        tab3.setText("HAWAI-N-EVA");
        //tab3.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.hawaib32, 0, 0);
        tabLayout.getTabAt(2).setCustomView(tab3);

        TextView tab4 = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.custom_tab, null);
        tab4.setText("SCHOOL SHOES");
        //tab4.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.schoolb32, 0, 0);
        tabLayout.getTabAt(3).setCustomView(tab4);
    }

    private void setViewPager(){
        adapter = new ViewPagerAdapter(getFragmentManager());
        adapter.addFragment(new IGentsCategoryFragment(),"GENTS");
        adapter.addFragment(new ILadiesNBoysCategoryFragment(),"LADIES & BOYS");
        adapter.addFragment(new IHawaiNEvaCategoryFragment(),"HAWAI & EVA");
        adapter.addFragment(new ISchoolShoesCategoryFragment(),"SCHOOL SHOES");
        pager.setAdapter(adapter);
    }

}
