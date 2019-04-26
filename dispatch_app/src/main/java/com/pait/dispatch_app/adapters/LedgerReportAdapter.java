package com.pait.dispatch_app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.pait.dispatch_app.R;
import com.pait.dispatch_app.model.DispatchMasterClass;

import java.text.DecimalFormat;
import java.util.List;

public class LedgerReportAdapter extends BaseAdapter {

    private List<DispatchMasterClass> ledger_List;
    private Context context;
    private LayoutInflater inflater;
    private DecimalFormat flt_price;

    public LedgerReportAdapter(List<DispatchMasterClass> _super_sellList, Context _context) {
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
            convertview = inflater.inflate(R.layout.list_item_dispatch_report, viewGroup, false);
            holder.tv_date = convertview.findViewById(R.id.tv_date);
            holder.tv_partyName = convertview.findViewById(R.id.tv_partyName);
            holder.tv_pono = convertview.findViewById(R.id.tv_pono);
            holder.tv_qty = convertview.findViewById(R.id.tv_qty);
            holder.tv_dispatchBy = convertview.findViewById(R.id.tv_dispatchBy);
            holder.tv_transporter = convertview.findViewById(R.id.tv_transporter);
            convertview.setTag(holder);
        } else {
            holder = (ViewHolder) convertview.getTag();
        }
        DispatchMasterClass ledgerReportClass = (DispatchMasterClass) getItem(position);
        holder.tv_date.setText(ledgerReportClass.getDispatchDate());
        holder.tv_partyName.setText(ledgerReportClass.getPartyName());
        holder.tv_pono.setText(ledgerReportClass.getPONO());
        holder.tv_qty.setText(ledgerReportClass.getTotalQty());
        holder.tv_transporter.setText(ledgerReportClass.getTransporter());
        holder.tv_dispatchBy.setText(ledgerReportClass.getEmp_Name());

        return convertview;
    }

    private class ViewHolder {
        private TextView tv_date, tv_partyName, tv_pono, tv_qty, tv_dispatchBy, tv_transporter;
    }
}

