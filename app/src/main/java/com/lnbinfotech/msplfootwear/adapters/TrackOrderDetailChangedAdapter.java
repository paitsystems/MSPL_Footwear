package com.lnbinfotech.msplfootwear.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lnbinfotech.msplfootwear.R;
import com.lnbinfotech.msplfootwear.constant.Constant;
import com.lnbinfotech.msplfootwear.model.TrackOrderDetailChangedClass;

import java.text.DecimalFormat;
import java.util.List;

//Created by ANUP on 12/28/2017.

public class TrackOrderDetailChangedAdapter extends BaseAdapter{

    private List<TrackOrderDetailChangedClass> orderList;
    private Context context;
    private LayoutInflater inflater;
    private DecimalFormat flt_price;

    public TrackOrderDetailChangedAdapter(List<TrackOrderDetailChangedClass> _orderList, Context _context) {
        this.orderList = _orderList;
        this.context = _context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        flt_price = new DecimalFormat();
        flt_price.setMaximumFractionDigits(2);
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
            convertview = inflater.inflate(R.layout.list_item_track_order_detail_changed, viewGroup, false);
            holder.tv_prod_id = (TextView) convertview.findViewById(R.id.tv_prod_id);
            holder.tv_size_group = (TextView) convertview.findViewById(R.id.tv_size_group);
            holder.tv_color = (TextView) convertview.findViewById(R.id.tv_color);
            holder.tv_loose_qty = (TextView) convertview.findViewById(R.id.tv_loose_qty);
            holder.tv_wsp = (TextView) convertview.findViewById(R.id.tv_wsp);
            holder.tv_mr = (TextView) convertview.findViewById(R.id.tv_mr);
            holder.tv_amt = (TextView) convertview.findViewById(R.id.tv_amt);
            holder.tv_gst_per = (TextView) convertview.findViewById(R.id.tv_gst_per);
            holder.tv_invqty = (TextView) convertview.findViewById(R.id.tv_invqty);
            holder.tv_canqty = (TextView) convertview.findViewById(R.id.tv_cancleqty);
            convertview.setTag(holder);
        } else {
            holder = (ViewHolder) convertview.getTag();
        }

        TrackOrderDetailChangedClass order = (TrackOrderDetailChangedClass) getItem(position);
        holder.tv_prod_id.setText(order.getProdid());
        holder.tv_size_group.setText(order.getSize_group());
        holder.tv_color.setText(order.getColor());
        String hashCode = order.getHashcode();
        if (hashCode.equalsIgnoreCase("#FFFFFF")) {
            holder.tv_color.setTextColor(Color.parseColor("#000000"));
            holder.tv_color.setBackgroundResource(R.drawable.black_border_draw);
        } else {
            holder.tv_color.setTextColor(Color.parseColor("#FFFFFF"));
            holder.tv_color.setBackgroundColor(Color.parseColor(hashCode));
        }
        holder.tv_loose_qty.setText(String.valueOf(order.getOrderqty()));
        holder.tv_mr.setText(order.getMrp());
        holder.tv_invqty.setText(String.valueOf(order.getInvqty()));
        holder.tv_canqty.setText(String.valueOf(order.getCanqty()));
        return convertview;
    }

    private class ViewHolder {
        public TextView tv_prod_id, tv_size_group, tv_color, tv_loose_qty, tv_wsp, tv_mr, tv_amt, tv_gst_per,
                        tv_invqty, tv_canqty;
    }

}
