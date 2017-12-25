package com.lnbinfotech.msplfootwear.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lnbinfotech.msplfootwear.R;
import com.lnbinfotech.msplfootwear.constant.Constant;
import com.lnbinfotech.msplfootwear.model.WarehouseDetailsClass;

import java.text.DecimalFormat;
import java.util.List;


 //Created by SNEHA on 12/22/2017.

public class WarehousesDetailAdapter extends BaseAdapter {
    Context context;
    List<WarehouseDetailsClass> wlist;
    LayoutInflater inflater;
    private DecimalFormat dc;

    public WarehousesDetailAdapter(Context _context, List<WarehouseDetailsClass> _wlist){
        this.context = _context;
        this.wlist = _wlist;
        inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        dc = new DecimalFormat();
        dc.setMaximumFractionDigits(2);
    }

    @Override
    public int getCount() {
        return wlist.size();
    }

    @Override
    public Object getItem(int position) {
        return wlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final ViewHolder holder;
        if(view == null){
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.warehouse_detail_list_item,viewGroup,false);
            holder.tv_warehouse = (TextView) view.findViewById(R.id.tv_warehouse);
            holder.tv_qty = (TextView) view.findViewById(R.id.tv_qty);
            holder.tv_amt = (TextView) view.findViewById(R.id.tv_amt);
             view.setTag(holder);
        }else {
            holder = (ViewHolder) view.getTag();
        }

        WarehouseDetailsClass wclass = (WarehouseDetailsClass) getItem(i);
        holder.tv_warehouse.setText(wclass.getWarehouse());
        holder.tv_qty.setText(dc.format(wclass.getQty()));
        holder.tv_amt.setText(dc.format(wclass.getAmt()));

        return view;
    }

    public class ViewHolder{
        public TextView tv_warehouse,tv_qty,tv_amt;
    }
}
