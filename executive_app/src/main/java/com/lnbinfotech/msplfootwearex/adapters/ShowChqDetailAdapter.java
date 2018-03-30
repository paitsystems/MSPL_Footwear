package com.lnbinfotech.msplfootwearex.adapters;// Created by anup on 9/21/2017.

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lnbinfotech.msplfootwearex.R;
import com.lnbinfotech.msplfootwearex.model.ChequeDetailsClass;

import java.util.List;

public class ShowChqDetailAdapter extends BaseAdapter{

    Activity activity;
    List<ChequeDetailsClass> cheque_list;

    public ShowChqDetailAdapter(Activity _activity,List<ChequeDetailsClass> _cheque_list){
        this.activity = _activity;
        this.cheque_list = _cheque_list;
    }

    @Override
    public int getCount() {
        return cheque_list.size();
    }

    @Override
    public ChequeDetailsClass getItem(int position) {
        return cheque_list.get(position);
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
            v = inflater.inflate(R.layout.chq_detail_show_list,null);
            holder = new ViewHolder();
            holder.tv_date = (TextView) v.findViewById(R.id.tv_date);
            holder.tv_number = (TextView) v.findViewById(R.id.tv_number);
            holder.tv_amount = (TextView) v.findViewById(R.id.tv_amount);
            holder.tv_ref = (TextView) v.findViewById(R.id.tv_ref);
            v.setTag(holder);

        }else {
            holder = (ViewHolder) convertView.getTag();
        }
           ChequeDetailsClass cd = getItem(position);
           holder.tv_date.setText(cd.getChq_det_date());
           Log.d("Log","tv_date: "+cd.getChq_det_date());
           holder.tv_number.setText(cd.getChq_det_number());
           Log.d("Log","tv_number: "+cd.getChq_det_number());
           holder.tv_amount.setText(cd.getChq_det_amt());
           Log.d("Log","tv_amount: "+cd.getChq_det_amt());
           holder.tv_ref.setText(cd.getChq_det_ref());
           Log.d("Log","tv_ref: "+cd.getChq_det_ref());

        return v;
    }
    private class ViewHolder{
        public TextView tv_date,tv_number,tv_amount,tv_ref;
    }
}
