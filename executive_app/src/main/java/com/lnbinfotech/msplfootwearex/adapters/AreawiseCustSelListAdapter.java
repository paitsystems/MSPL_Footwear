package com.lnbinfotech.msplfootwearex.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lnbinfotech.msplfootwearex.R;
import com.lnbinfotech.msplfootwearex.model.AreawiseCustomerSelectionClass;

import java.util.List;

// Created by lnb on 9/23/2017.

public class AreawiseCustSelListAdapter extends BaseAdapter {

    Context context;
    LayoutInflater inflater;
    List<AreawiseCustomerSelectionClass> areaList;

    public AreawiseCustSelListAdapter(Context _context, List<AreawiseCustomerSelectionClass> _areaList){
        this.context = _context;
        this.areaList = _areaList;
        this.inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return areaList.size();
    }

    @Override
    public Object getItem(int i) {
        return areaList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if(view == null){
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.list_item_areawise_customer_list,viewGroup,false);
            holder.tv_arename = (TextView) view.findViewById(R.id.tv_areaname);
            view.setTag(holder);
        }else{
            holder = (ViewHolder) view.getTag();
        }
        AreawiseCustomerSelectionClass areaClass = (AreawiseCustomerSelectionClass) getItem(i);
        holder.tv_arename.setText(areaClass.getAreaname());
        return view;
    }

    private class ViewHolder{
        private TextView tv_arename;
    }
}
