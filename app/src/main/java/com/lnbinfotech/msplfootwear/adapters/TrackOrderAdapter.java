package com.lnbinfotech.msplfootwear.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lnbinfotech.msplfootwear.model.TrackOrderClass;
import com.lnbinfotech.msplfootwearex.R;
import com.lnbinfotech.msplfootwearex.model.TrackOrderClass;

import java.util.List;

/**
 * Created by SNEHA on 10/26/2017.
 */
public class TrackOrderAdapter extends BaseAdapter {
    private List<TrackOrderClass> orderList;
    private Context context;
    private LayoutInflater inflater;

    public TrackOrderAdapter(List<TrackOrderClass> _orderList, Context _context) {
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
            convertview = inflater.inflate(R.layout.order_list_item, viewGroup, false);
            holder.tv_date = (TextView) convertview.findViewById(R.id.tv_date);
            holder.tv_orderno = (TextView) convertview.findViewById(R.id.tv_orderno);
            holder.tv_orderqty = (TextView) convertview.findViewById(R.id.tv_orderqty);
            holder.tv_orderamt = (TextView) convertview.findViewById(R.id.tv_orderamt);
            convertview.setTag(holder);
        } else {
            holder = (ViewHolder) convertview.getTag();
        }
        TrackOrderClass trackOrderClass = new TrackOrderClass();
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
