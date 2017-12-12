package com.lnbinfotech.msplfootwear.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lnbinfotech.msplfootwear.R;
import com.lnbinfotech.msplfootwear.model.LedgerReportClass;

import java.text.DecimalFormat;
import java.util.List;


 // Created by SNEHA on 12/11/2017.

public class LedgerReportAdapter extends BaseAdapter {
    private List<LedgerReportClass> ledger_List;
    private Context context;
    private LayoutInflater inflater;
    private DecimalFormat flt_price;


    public LedgerReportAdapter(List<LedgerReportClass> _super_sellList, Context _context) {
        this.ledger_List = _super_sellList;
        this.context = _context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        flt_price = new DecimalFormat();
        flt_price.setMaximumFractionDigits(2);

    }

    @Override
    public int getCount() {
        return ledger_List.size();
    }

    @Override
    public Object getItem(int position) {
        return ledger_List.get(position);
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
            convertview = inflater.inflate(R.layout.ledger_item_list, viewGroup, false);
            holder.tv_date = (TextView) convertview.findViewById(R.id.tv_date);
            holder.tv_against = (TextView) convertview.findViewById(R.id.tv_agnst);
            holder.tv_opnbal = (TextView) convertview.findViewById(R.id.tv_ob);
            holder.tv_clbal = (TextView) convertview.findViewById(R.id.tv_clb);
            holder.tv_debit = (TextView) convertview.findViewById(R.id.tv_db);
            holder.tv_credit = (TextView) convertview.findViewById(R.id.tv_cd);
            holder.tv_transno = (TextView) convertview.findViewById(R.id.tv_tno);
            convertview.setTag(holder);
        } else {
            holder = (ViewHolder) convertview.getTag();
        }
        LedgerReportClass ledgerReportClass = (LedgerReportClass) getItem(position);
        holder.tv_date.setText(ledgerReportClass.getDate());
        holder.tv_against.setText(ledgerReportClass.getAgainst());
        holder.tv_transno.setText(ledgerReportClass.getTransno());
        holder.tv_opnbal.setText(flt_price.format(ledgerReportClass.getOpnbal()));
        holder.tv_clbal.setText(flt_price.format(ledgerReportClass.getClsbal()));
        holder.tv_debit.setText(flt_price.format(ledgerReportClass.getDebit()));
        holder.tv_credit.setText(flt_price.format(ledgerReportClass.getCredit()));

        return convertview;
    }

    private class ViewHolder {
        public TextView tv_date,tv_against,tv_opnbal,tv_clbal, tv_debit, tv_credit,tv_transno;
    }
}
