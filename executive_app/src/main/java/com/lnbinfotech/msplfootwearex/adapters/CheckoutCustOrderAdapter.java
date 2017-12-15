package com.lnbinfotech.msplfootwearex.adapters;


 //Created by SNEHA on 12/1/2017.

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lnbinfotech.msplfootwearex.R;
import com.lnbinfotech.msplfootwearex.model.CheckoutCustOrderClass;
import com.lnbinfotech.msplfootwearex.model.CustomerOrderClass;

import java.text.DecimalFormat;
import java.util.List;

public class CheckoutCustOrderAdapter extends BaseAdapter {

    private List<CustomerOrderClass> checkList;
    private Context context;
    private LayoutInflater inflater;
    private DecimalFormat flt_price;

    public CheckoutCustOrderAdapter(List<CustomerOrderClass> _checkList, Context _context) {
        this.checkList = _checkList;
        this.context = _context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        flt_price = new DecimalFormat();
        flt_price.setMaximumFractionDigits(2);

    }

    @Override
    public int getCount() {
        return checkList.size();
    }

    @Override
    public Object getItem(int position) {
        return checkList.get(position);
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
            convertview = inflater.inflate(R.layout.checkout_custorder_list_item, viewGroup, false);
            holder.tv_prod_id = (TextView) convertview.findViewById(R.id.tv_prod_id);
            holder.tv_size_group = (TextView) convertview.findViewById(R.id.tv_size_group);
            holder.tv_color = (TextView) convertview.findViewById(R.id.tv_color);
            holder.tv_loose_qty = (TextView) convertview.findViewById(R.id.tv_loose_qty);
            holder.tv_aqty = (TextView) convertview.findViewById(R.id.tv_aqty);
            convertview.setTag(holder);
        } else {
            holder = (ViewHolder) convertview.getTag();
        }

        CustomerOrderClass order  = (CustomerOrderClass) getItem(position);
        holder.tv_prod_id.setText(order.getProdId());
        holder.tv_size_group.setText(order.getSizeGroup());
        holder.tv_color.setText(order.getColor());
        String hashCode = order.getHashCode();
        if (hashCode.equalsIgnoreCase("#FFFFFF")) {
            holder.tv_color.setTextColor(Color.parseColor("#000000"));
            holder.tv_color.setBackgroundResource(R.drawable.black_border_draw);
        } else {
            holder.tv_color.setTextColor(Color.parseColor("#FFFFFF"));
            holder.tv_color.setBackgroundColor(Color.parseColor(hashCode));
        }
        holder.tv_loose_qty.setText(String.valueOf(order.getLooseQty()));
        int a = order.getAvailQty();
        if(a!=0) {
            holder.tv_aqty.setText("Y");
            holder.tv_aqty.setBackgroundColor(Color.parseColor("#025F1E"));
        }else{
            holder.tv_aqty.setText("N");
            holder.tv_aqty.setBackgroundColor(Color.parseColor("#FF0000"));
        }
        holder.tv_aqty.setTextColor(Color.parseColor("#FFFFFF"));
        return convertview;
    }

    private class ViewHolder {
        public TextView tv_prod_id,tv_size_group,tv_color,tv_loose_qty,tv_aqty;
    }
}


