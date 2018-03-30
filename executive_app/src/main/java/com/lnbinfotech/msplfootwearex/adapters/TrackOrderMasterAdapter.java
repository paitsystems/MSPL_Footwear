package com.lnbinfotech.msplfootwearex.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lnbinfotech.msplfootwearex.model.TrackOrderMasterClass;
import com.lnbinfotech.msplfootwearex.R;

import java.util.List;

//Created by SNEHA on 3/26/2017.

public class TrackOrderMasterAdapter extends BaseAdapter {
    private List<TrackOrderMasterClass> orderList;
    private Context context;
    private LayoutInflater inflater;

    public TrackOrderMasterAdapter(List<TrackOrderMasterClass> _orderList, Context _context) {
        this.orderList = _orderList;
        this.context = _context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return orderList.size();
    }

    @Override
    public Object getItem(int position) {
        return orderList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertview, ViewGroup viewGroup) {
        final ViewHolder holder;
        if (convertview == null) {
            holder = new ViewHolder();
            convertview = inflater.inflate(R.layout.list_item_track_order_master, viewGroup, false);
            holder.tv_date = (TextView) convertview.findViewById(R.id.tv_date);
            holder.tv_orderno = (TextView) convertview.findViewById(R.id.tv_orderno);
            holder.tv_orderqty = (TextView) convertview.findViewById(R.id.tv_orderqty);
            holder.tv_orderamt = (TextView) convertview.findViewById(R.id.tv_orderamt);
            convertview.setTag(holder);
        } else {
            holder = (ViewHolder) convertview.getTag();
        }
        TrackOrderMasterClass trackOrderClass = (TrackOrderMasterClass) getItem(position);
        holder.tv_date.setText(trackOrderClass.getPODate());
        holder.tv_orderno.setText(trackOrderClass.getPono());
        holder.tv_orderqty.setText(trackOrderClass.getLooseQty());
        holder.tv_orderamt.setText(trackOrderClass.getNetAmt());
        return convertview;
    }

    private class ViewHolder {
        public TextView tv_date, tv_orderno, tv_orderqty, tv_orderamt;
    }
}
