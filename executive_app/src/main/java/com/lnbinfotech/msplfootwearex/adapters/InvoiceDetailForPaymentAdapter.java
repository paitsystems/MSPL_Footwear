package com.lnbinfotech.msplfootwearex.adapters;

//Created by ANUP on 1/18/2018.

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.lnbinfotech.msplfootwearex.R;
import com.lnbinfotech.msplfootwearex.VisitPaymentFormActivity;
import com.lnbinfotech.msplfootwearex.constant.Constant;
import com.lnbinfotech.msplfootwearex.interfaces.PaymentTotalInterface;
import com.lnbinfotech.msplfootwearex.model.OuststandingReportClass;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;

public class InvoiceDetailForPaymentAdapter extends BaseAdapter{

    private List<OuststandingReportClass> out_List;
    private Context context;
    private LayoutInflater inflater;
    private DecimalFormat flt_price;
    private HashMap<Integer,Integer> map;
    private int total = 0;
    private PaymentTotalInterface paymentInt;

    public InvoiceDetailForPaymentAdapter(List<OuststandingReportClass> _super_sellList, Context _context) {
        this.out_List = _super_sellList;
        this.context = _context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        flt_price = new DecimalFormat();
        flt_price.setMaximumFractionDigits(2);
        map = new HashMap<>();
        total = VisitPaymentFormActivity.total;
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
            convertview = inflater.inflate(R.layout.list_item_invoice, viewGroup, false);
            holder.tv_date = (TextView) convertview.findViewById(R.id.tv_date);
            holder.tv_type = (TextView) convertview.findViewById(R.id.tv_type);
            holder.tv_dcno = (TextView) convertview.findViewById(R.id.tv_dcno);
            holder.tv_total = (TextView) convertview.findViewById(R.id.tv_total);
            holder.tv_paidAmnt = (TextView) convertview.findViewById(R.id.tv_paidamnt);
            holder.tv_outstdAmnt = (TextView) convertview.findViewById(R.id.tv_outstndamnt);
            holder.cb = (CheckBox) convertview.findViewById(R.id.cb);
            convertview.setTag(holder);
        } else {
            holder = (ViewHolder) convertview.getTag();
        }
        final OuststandingReportClass outClass = (OuststandingReportClass) getItem(position);
        holder.tv_date.setText(outClass.getDate());
        holder.tv_type.setText(outClass.getType());
        holder.tv_dcno.setText(outClass.getDcno());
        holder.tv_total.setText(flt_price.format(outClass.getTotal()));
        holder.tv_paidAmnt.setText(flt_price.format(outClass.getPaidAmnt()));
        holder.tv_outstdAmnt.setText(flt_price.format(outClass.getOutAmnt()));
        holder.cb.setOnCheckedChangeListener(null);
        holder.cb.setChecked(outClass.isChecked());
        holder.cb.setTag(position);
        holder.cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckBox cb = (CheckBox) view;
                int pos = (int) view.getTag();
                if (!map.isEmpty()) {
                    if (cb.isChecked()) {
                        map.put(pos, 1);
                    } else {
                        map.put(pos, 0);
                    }
                } else {
                    if (cb.isChecked()) {
                        map.put(pos, 1);
                    } else {
                        map.put(pos, 0);
                    }
                }
                int a = map.get(pos);
                OuststandingReportClass out = out_List.get(pos);
                double tot, paidAmnt, outAmnt;
                tot = out.getTotal();
                paidAmnt = out.getPaidAmnt();
                outAmnt = out.getOutAmnt();
                if (a == 1 && total != 0) {
                    out.setChecked(true);
                    if (total > tot) {
                        paidAmnt = tot;
                        outAmnt = 0;
                        total = (int) (total - tot);
                    } else if (total <= tot) {
                        paidAmnt = total;
                        outAmnt = (int) (tot - total);
                        total = 0;
                    }
                    VisitPaymentFormActivity.totBal = (int) (VisitPaymentFormActivity.totBal + tot);
                    VisitPaymentFormActivity.totPaid = (int) (VisitPaymentFormActivity.totPaid + paidAmnt);
                    out.setPaidAmnt(paidAmnt);
                    out.setOutAmnt(outAmnt);
                    out_List.set(pos, out);
                    notifyDataSetChanged();
                    paymentInt.totalsCalculation("");
                } else if (a == 1) {
                    notifyDataSetChanged();
                    paymentInt.totalsCalculation("0");
                } else if (a == 0) {
                    out.setChecked(false);
                    VisitPaymentFormActivity.totBal = (int) (VisitPaymentFormActivity.totBal - tot);
                    VisitPaymentFormActivity.totPaid = (int) (VisitPaymentFormActivity.totPaid - paidAmnt);
                    paidAmnt = 0;
                    outAmnt = tot;
                    total = (int) (total + out.getPaidAmnt());

                    out.setPaidAmnt(paidAmnt);
                    out.setOutAmnt(outAmnt);
                    out_List.set(pos, out);
                    notifyDataSetChanged();
                    paymentInt.totalsCalculation("");
                }

                //Constant.showLog("" + out.getTotal());
            }
        });
        return convertview;
    }

    private class ViewHolder {
        private TextView tv_date,tv_type,tv_dcno,tv_total,tv_paidAmnt,tv_outstdAmnt;
        private CheckBox cb;
    }

    public void initInterface(PaymentTotalInterface _paymentInt){
        this.paymentInt = _paymentInt;
    }
}
