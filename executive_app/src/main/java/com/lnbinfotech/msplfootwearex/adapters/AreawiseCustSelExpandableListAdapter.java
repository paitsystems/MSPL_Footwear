package com.lnbinfotech.msplfootwearex.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.lnbinfotech.msplfootwearex.R;

import java.util.HashMap;
import java.util.List;


// Created by SNEHA on 12/14/2017.

public class AreawiseCustSelExpandableListAdapter extends BaseExpandableListAdapter{
    Context context;
    HashMap<Integer,List<String>> area_map;
    List<Integer> areaid_list;
    HashMap<Integer,List<Integer>> areaid_partyId_map;
    HashMap<Integer,List<String>> party_map;
    List<Integer> partyid_list;
    HashMap<Integer,Integer> area_party_map;


    public AreawiseCustSelExpandableListAdapter(Context _context,HashMap<Integer,List<String>> _area_map,List<Integer> _areaid_list,HashMap<Integer,List<String>> _party_map,
            List<Integer> _partyid_list,HashMap<Integer,Integer> _area_party_map,HashMap<Integer,List<Integer>> _areaid_partyId_map){
        this.context = _context;
        this.area_map = _area_map;
        this.party_map = _party_map;
        this.areaid_list = _areaid_list;
        this.partyid_list = _partyid_list;
        this.area_party_map = _area_party_map;
        this.areaid_partyId_map = _areaid_partyId_map;
    }

    @Override
    public int getGroupCount() {
        return areaid_list.size();
    }

    @Override
    public Object getGroup(int group_position) {
        /*int group = areaid_list.get(group_position);
        String s = String.valueOf(area_map.get(group));
        return s;*/
        return area_map.get(areaid_list.get(group_position));
    }

    @Override
    public long getGroupId(int group_position) {
        return group_position;
    }

    @Override
    public View getGroupView(int group_position, boolean b, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.explist_parent_item,null);
        TextView tv_area_name = (TextView) view.findViewById(R.id.tv_area_name);
        tv_area_name.setText(String.valueOf(getGroup(group_position)));
        return view;
    }

    @Override
    public int getChildrenCount(int group_position) {
        return partyid_list.size();
    }

    /*public Object getChild(int groupPosition, int childPosition) {
        int a = clint_id_list.get(groupPosition);
        List<List<String>> b =  map.get(a);
        List<String> list = b.get(childPosition);
        return list;
    }*/


    @Override
    public Object getChild(int group_position, int child_position) {

     //   int areaid_pos =  areaid_list.get(group_position);
        int partyid_pos = partyid_list.get(child_position);

        return party_map.get(partyid_pos);
    }

    @Override
    public long getChildId(int group_position, int child_position) {
        return child_position;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getChildView(int group_position, int i1, boolean b, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.explist_chlid_item,null);
        TextView tv_party_name = (TextView) view.findViewById(R.id.party_name);
       // tv_party_name.setText();
        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }
}
