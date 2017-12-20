package com.lnbinfotech.msplfootwear.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lnbinfotech.msplfootwear.R;
import com.lnbinfotech.msplfootwear.model.OurBankDetailsClass;
import com.lnbinfotech.msplfootwear.model.OuststandingReportClass;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by SNEHA on 12/16/2017.
 */
public class OurBankDetailsAdapter extends BaseAdapter {
    private List<OurBankDetailsClass> bank_List;
    private Context context;
    private LayoutInflater inflater;
    private DecimalFormat flt_price;


    public OurBankDetailsAdapter(List<OurBankDetailsClass> _bank_List, Context _context) {
        this.bank_List = _bank_List;
        this.context = _context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        flt_price = new DecimalFormat();
        flt_price.setMaximumFractionDigits(2);

    }

    @Override
    public int getCount() {
        return bank_List.size();
    }

    @Override
    public Object getItem(int position) {
        return bank_List.get(position);
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
            convertview = inflater.inflate(R.layout.bank_details_list_item, viewGroup, false);
            holder.tv_bank_name = (TextView) convertview.findViewById(R.id.tv_bank_name);
            holder.tv_acc_no = (TextView) convertview.findViewById(R.id.tv_acc_no);
            holder.tv_ifsc = (TextView) convertview.findViewById(R.id.tv_ifsc);
            holder.tv_micr = (TextView) convertview.findViewById(R.id.tv_micr);
            convertview.setTag(holder);
        } else {
            holder = (ViewHolder) convertview.getTag();
        }
        OurBankDetailsClass bankClass = (OurBankDetailsClass) getItem(position);
        holder.tv_bank_name.setText(bankClass.getBankName());
        holder.tv_acc_no.setText(bankClass.getAccNo());
        holder.tv_ifsc.setText(bankClass.getIFSC());
        holder.tv_micr.setText(bankClass.getMICR());

        return convertview;
    }

    private class ViewHolder {
        public TextView tv_bank_name,tv_acc_no,tv_ifsc,tv_micr;
    }
}

