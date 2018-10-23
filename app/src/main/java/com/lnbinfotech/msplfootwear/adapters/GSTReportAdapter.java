package com.lnbinfotech.msplfootwear.adapters;

//Created by Anup on 10/17/2018.

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lnbinfotech.msplfootwear.R;
import com.lnbinfotech.msplfootwear.model.GSTDetailClass;
import com.lnbinfotech.msplfootwear.model.GSTDetailClass;

import java.text.DecimalFormat;
import java.util.List;

public class GSTReportAdapter extends BaseAdapter {

    private List<GSTDetailClass> gstDetList;
    private Context context;
    private LayoutInflater inflater;
    private DecimalFormat flt_price;

    public GSTReportAdapter(List<GSTDetailClass> _super_sellList, Context _context) {
        this.gstDetList = _super_sellList;
        this.context = _context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        flt_price = new DecimalFormat();
        flt_price.setMaximumFractionDigits(2);

    }

    @Override
    public int getCount() {
        return gstDetList.size();
    }

    @Override
    public Object getItem(int position) {
        return gstDetList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        final ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.list_item_gst_report, viewGroup, false);
            holder.tv_invDate = (TextView) convertView.findViewById(R.id.tv_invDate);
            holder.tv_invNo = (TextView) convertView.findViewById(R.id.tv_invNo);
            holder.tv_shopName = (TextView) convertView.findViewById(R.id.tv_shopName);
            holder.tv_gstNo = (TextView) convertView.findViewById(R.id.tv_gstNo);
            holder.tv_hsnCode = (TextView) convertView.findViewById(R.id.tv_hsnCode);
            holder.tv_taxPer = (TextView) convertView.findViewById(R.id.tv_taxPer);
            holder.tv_qty = (TextView) convertView.findViewById(R.id.tv_qty);
            holder.tv_uom = (TextView) convertView.findViewById(R.id.tv_uom);
            holder.tv_assValue = (TextView) convertView.findViewById(R.id.tv_assValue);
            holder.tv_cgstAmnt = (TextView) convertView.findViewById(R.id.tv_cgstAmnt);
            holder.tv_sgstAmnt = (TextView) convertView.findViewById(R.id.tv_sgstAmnt);
            holder.tv_igstAmnt = (TextView) convertView.findViewById(R.id.tv_igstAmnt);
            holder.tv_roundOff = (TextView) convertView.findViewById(R.id.tv_roundOff);
            holder.tv_netSale = (TextView) convertView.findViewById(R.id.tv_netSale);
            holder.tv_type = (TextView) convertView.findViewById(R.id.tv_type);
            holder.tv_invoiceNo = (TextView) convertView.findViewById(R.id.tv_invoiceNo);
            holder.tv_invoiceDate = (TextView) convertView.findViewById(R.id.tv_invoiceDate);
            holder.lay = (LinearLayout) convertView.findViewById(R.id.lay);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        GSTDetailClass ledgerReportClass = (GSTDetailClass) getItem(position);
        holder.tv_invDate.setText(ledgerReportClass.getInvDate());
        holder.tv_invNo.setText(ledgerReportClass.getInvNo());
        holder.tv_shopName.setText(ledgerReportClass.getShopName());
        holder.tv_gstNo.setText(ledgerReportClass.getGSTNo());
        holder.tv_hsnCode.setText(ledgerReportClass.getHSNCode());
        holder.tv_taxPer.setText(ledgerReportClass.getTax());
        holder.tv_qty.setText(ledgerReportClass.getQty());
        holder.tv_uom.setText(ledgerReportClass.getUOM());
        holder.tv_assValue.setText(ledgerReportClass.getAssesible_value());
        holder.tv_cgstAmnt.setText(ledgerReportClass.getCGSTAmt());
        holder.tv_sgstAmnt.setText(ledgerReportClass.getSGSTAmt());
        holder.tv_igstAmnt.setText(ledgerReportClass.getIGSTAmt());
        holder.tv_roundOff.setText(ledgerReportClass.getRoundOff());
        holder.tv_netSale.setText(ledgerReportClass.getNetSale());
        holder.tv_type.setText(ledgerReportClass.getType());
        holder.tv_invoiceNo.setText(ledgerReportClass.getInvoiceNo());
        holder.tv_invoiceDate.setText(ledgerReportClass.getInvoiceDate());
        if (ledgerReportClass.getType().equals("Return")) {
            holder.lay.setBackgroundColor(context.getResources().getColor(R.color.blued));
        } else {
            holder.lay.setBackgroundColor(context.getResources().getColor(R.color.white));
        }
        return convertView;
    }

    private class ViewHolder {
        private TextView tv_invDate,tv_invNo,tv_shopName,tv_gstNo, tv_hsnCode, tv_taxPer,tv_qty,tv_uom,tv_assValue,
                tv_cgstAmnt,tv_sgstAmnt,tv_igstAmnt,tv_roundOff,tv_netSale,tv_type,tv_invoiceNo,tv_invoiceDate;
        private LinearLayout lay;
    }
}
