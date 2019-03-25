package com.lnbinfotech.msplfootwear.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.lnbinfotech.msplfootwear.fragments.Fragment_SchemeFullImage;

import java.util.ArrayList;
import java.util.List;

public class SchemeFullImageAdapter extends FragmentPagerAdapter {

    private List<String> mFragmentImageList = new ArrayList<>();

    public SchemeFullImageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getCount() {
        return mFragmentImageList.size();
    }

    @Override
    public Fragment getItem(int position) {
        return Fragment_SchemeFullImage.newInstance(mFragmentImageList.get(position));
    }

    public void getImageTitle(List<String> _mFragmentImageList) {
        this.mFragmentImageList = _mFragmentImageList;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "";
    }
}
