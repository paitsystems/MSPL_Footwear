package com.lnbinfotech.msplfootwear.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lnbinfotech.msplfootwear.R;

import com.lnbinfotech.msplfootwear.model.OuststandingReportClass;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by SNEHA on 12/11/2017.
 */
public class OuststandingReportAdapter extends BaseAdapter {
    private List<OuststandingReportClass> out_List;
    private Context context;
    private LayoutInflater inflater;
    private DecimalFormat flt_price;


    public OuststandingReportAdapter(List<OuststandingReportClass> _super_sellList, Context _context) {
        this.out_List = _super_sellList;
        this.context = _context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        flt_price = new DecimalFormat();
        flt_price.setMaximumFractionDigits(2);

    }

    @Override
    public int getCount() {
        return out_List.size();
    }

    @Override
    public Object getItem(int position) {
        return out_List.get(position);
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
            convertview = inflater.inflate(R.layout.outstanding_item_list, viewGroup, false);
            holder.tv_date = (TextView) convertview.findViewById(R.id.tv_date);
            holder.tv_type = (TextView) convertview.findViewById(R.id.tv_type);
            holder.tv_dcno = (TextView) convertview.findViewById(R.id.tv_dcno);
            holder.tv_total = (TextView) convertview.findViewById(R.id.tv_total);
            convertview.setTag(holder);
        } else {
            holder = (ViewHolder) convertview.getTag();
        }
        OuststandingReportClass outClass = (OuststandingReportClass) getItem(position);
        holder.tv_date.setText(outClass.getDate());
        holder.tv_type.setText(outClass.getType());
        holder.tv_dcno.setText(outClass.getDcno());
        holder.tv_total.setText(flt_price.format(outClass.getTotal()));

        return convertview;
    }

    private class ViewHolder {
        public TextView tv_date,tv_type,tv_dcno,tv_total;
    }
}
