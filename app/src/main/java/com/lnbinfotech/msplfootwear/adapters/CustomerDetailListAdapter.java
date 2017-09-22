package com.lnbinfotech.msplfootwear.adapters;

//Created by lnb on 8/15/2017.

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lnbinfotech.msplfootwear.R;
import com.lnbinfotech.msplfootwear.model.CustomerDetailClass;

import java.util.ArrayList;

public class CustomerDetailListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<CustomerDetailClass> list;

    public CustomerDetailListAdapter(Context _context, ArrayList<CustomerDetailClass> _list) {
        this.context = _context;
        this.list = _list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_item_customer_details, viewGroup,false);
            holder.tv_custname = (TextView) view.findViewById(R.id.tv_custname);
            holder.tv_custaddress = (TextView) view.findViewById(R.id.tv_custaddress);
            holder.tv_custmobile = (TextView) view.findViewById(R.id.tv_custmobile);
            holder.tv_custemail = (TextView) view.findViewById(R.id.tv_custemail);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        CustomerDetailClass custClass = (CustomerDetailClass) getItem(i);
        holder.tv_custname.setText(custClass.getName());
        holder.tv_custaddress.setText(custClass.getAddress());
        holder.tv_custmobile.setText(custClass.getMobile());
        holder.tv_custemail.setText(custClass.getEmail());
        return view;
    }

    private class ViewHolder {
        TextView tv_custname, tv_custaddress, tv_custmobile, tv_custemail;
    }
}
