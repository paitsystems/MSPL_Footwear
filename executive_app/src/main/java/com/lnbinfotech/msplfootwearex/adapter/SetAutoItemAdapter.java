package com.lnbinfotech.msplfootwearex.adapter;// Created by anup on 9/20/2017.

import android.app.Activity;
import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lnbinfotech.msplfootwearex.R;

import java.util.List;


public class SetAutoItemAdapter extends BaseAdapter {
    Activity activity;
    List<String> item_list;
    LayoutInflater layoutInflater;

    public SetAutoItemAdapter(Activity _activity,List<String> _item_list){
        this.activity = _activity;
        this.item_list = _item_list;
       // layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return item_list.size();
    }

    @Override
    public Object getItem(int position) {
        return item_list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        View v = convertView;
        final ViewHolder holder;
        LayoutInflater inflater = activity.getLayoutInflater();
        if(convertView == null){
            v = inflater.inflate(R.layout.item_list,null);
            holder = new ViewHolder();
            holder.tv_item = (TextView) v.findViewById(R.id.tv_item);
            v.setTag(holder);

        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        String obj = item_list.get(position);
        holder.tv_item.setText(obj);

        return v;
    }
    private class ViewHolder{
        public TextView tv_item;
    }
}
