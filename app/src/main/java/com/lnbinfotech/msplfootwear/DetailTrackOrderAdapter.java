package com.lnbinfotech.msplfootwear;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;

import com.lnbinfotech.msplfootwear.constant.Constant;
import com.lnbinfotech.msplfootwear.model.DetailOrderClass;

import java.util.HashMap;
import java.util.List;

/**
 * Created by SNEHA on 10/27/2017.
 */
public class DetailTrackOrderAdapter extends BaseExpandableListAdapter {
    HashMap<Integer,List<DetailOrderClass>> map_parent;
    HashMap<Integer,String> map_child;
    Context context;

    public DetailTrackOrderAdapter(HashMap<Integer,List<DetailOrderClass>> _map_parent,HashMap<Integer,String> _map_child,Context _context){
        this.context = _context;
        this.map_parent = _map_parent;
        this.map_child = _map_child;
    }

    @Override
    public int getGroupCount() {
        return 0;
    }

    @Override
    public int getChildrenCount(int i) {
        return 0;
    }

    @Override
    public Object getGroup(int i) {
        return null;
    }

    @Override
    public Object getChild(int i, int i1) {
        return null;
    }

    @Override
    public long getGroupId(int i) {
        return 0;
    }

    @Override
    public long getChildId(int i, int i1) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        return null;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        return null;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }
}
