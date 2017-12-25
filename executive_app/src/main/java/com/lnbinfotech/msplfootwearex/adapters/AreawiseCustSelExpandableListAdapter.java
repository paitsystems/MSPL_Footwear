package com.lnbinfotech.msplfootwearex.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lnbinfotech.msplfootwearex.R;
import com.lnbinfotech.msplfootwearex.constant.Constant;

import java.util.HashMap;
import java.util.List;


// Created by SNEHA on 12/14/2017.

public class AreawiseCustSelExpandableListAdapter extends BaseExpandableListAdapter{
    Context context;
    HashMap<Integer,List<String>> area_map;
    List<Integer> areaid_list;
    HashMap<Integer,List<Integer>> areaid_partyId_map;
    //HashMap<Integer,List<String>> party_map;
    HashMap<Integer,String> party_map;
    List<Integer> partyid_list;
    HashMap<Integer,Integer> area_party_map;
    HashMap<Integer,List<String>> childls;




    public AreawiseCustSelExpandableListAdapter(Context _context,HashMap<Integer,List<String>> _area_map,List<Integer> _areaid_list,HashMap<Integer,String> _party_map,
            List<Integer> _partyid_list,HashMap<Integer,Integer> _area_party_map,HashMap<Integer,List<Integer>> _areaid_partyId_map,HashMap<Integer,List<String>> _childls){
        this.context = _context;
        this.area_map = _area_map;
        this.party_map = _party_map;
        this.areaid_list = _areaid_list;
        this.partyid_list = _partyid_list;
        this.area_party_map = _area_party_map;
        this.areaid_partyId_map = _areaid_partyId_map;
        this.childls = _childls;

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

        Constant.showLog("getgroup:"+area_map.get(areaid_list.get(group_position)));
        String a = String.valueOf(area_map.get(areaid_list.get(group_position)));
        String fin = a.replace("[","").replace("]","");
        Constant.showLog("fin:"+fin);
        return fin;

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
        TextView tv_child_count = (TextView) view.findViewById(R.id.tv_child_count);
        ImageView img_parent = (ImageView) view.findViewById(R.id.img_parent);
        if(b) {
            img_parent.setImageResource(R.drawable.expand_16);
        }else {
            img_parent.setImageResource(R.drawable.compress_16);
        }
        if(group_position % 2 == 1){
            //view.setBackgroundColor(Color.parseColor("#29B6F6"));  //blue
             view.setBackgroundColor(Color.parseColor("#8BC34A"));   //green
        }else {
            //view.setBackgroundColor(Color.parseColor("#FF8F00"));    //orange
            view.setBackgroundColor(Color.parseColor("#D500F9"));      //purple
        }

        String count = "["+String.valueOf(areaid_partyId_map.get(areaid_list.get(group_position)).size())+"]";
        tv_area_name.setText(String.valueOf(getGroup(group_position)));
        tv_child_count.setText(count);
        Constant.showLog("group name:"+String.valueOf(getGroup(group_position)));
        return view;
    }

    @Override
    public int getChildrenCount(int group_position) {
        return areaid_partyId_map.get(areaid_list.get(group_position)).size();
    }


    @Override
    public Object getChild(int group_position, int child_position) {

        Constant.showLog("mp:"+party_map.get(areaid_partyId_map.get(areaid_list.get(group_position)).get(child_position)));
        return party_map.get(areaid_partyId_map.get(areaid_list.get(group_position)).get(child_position));


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
    public View getChildView(int group_position, int child_position, boolean b, View view, ViewGroup viewGroup) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.explist_chlid_item,null);
        TextView tv_party_name = (TextView) view.findViewById(R.id.party_name);
        TextView tv_party_no = (TextView) view.findViewById(R.id.party_no);

        if(group_position % 2 == 1){
            tv_party_name.setTextColor(Color.parseColor("#3F51B5"));
            tv_party_no.setTextColor(Color.parseColor("#3F51B5"));             //blue
        }else {
            tv_party_name.setTextColor(Color.parseColor("#fa6c04"));
            tv_party_no.setTextColor(Color.parseColor("#fa6c04"));               ///orange
        }

        tv_party_name.setText(String.valueOf(getChild(group_position,child_position)));
        tv_party_no.setText(String.valueOf(child_position + 1));

        /*for (int i = child_position;i<=getChildrenCount(group_position);i++){
            Constant.showLog("child_position>>>>>>>>>>:"+i);
            tv_party_no.setText(String.valueOf(i));
        }*/

        return view;
    }

    @Override
    public boolean isChildSelectable(int group_position, int child_position) {
        return true;
    }
}
