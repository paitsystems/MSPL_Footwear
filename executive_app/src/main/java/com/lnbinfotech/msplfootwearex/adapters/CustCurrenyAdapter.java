package com.lnbinfotech.msplfootwearex.adapters;

//Created by lnb on 8/15/2017.

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;
import com.lnbinfotech.msplfootwearex.R;
import com.lnbinfotech.msplfootwearex.constant.Constant;
import com.lnbinfotech.msplfootwearex.interfaces.CurrencyInterface;
import com.lnbinfotech.msplfootwearex.model.CurrencyMasterClass;

import java.util.ArrayList;

public class CustCurrenyAdapter extends BaseAdapter {

    private ArrayList<CurrencyMasterClass> currClassList;
    private Context context;
    private CurrencyInterface currInterface;

    public CustCurrenyAdapter(ArrayList<CurrencyMasterClass> _currClassList, Context _context) {
        this.currClassList = _currClassList;
        this.context = _context;
    }

    @Override
    public int getCount() {
        return currClassList.size();
    }

    @Override
    public Object getItem(int i) {
        return currClassList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        final ViewHolder holder;
        holder = new ViewHolder();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.list_item_cust_currency, null);
        holder.tv_curr = (TextView) view.findViewById(R.id.tv_cust_curr);
        holder.ed_value = (EditText) view.findViewById(R.id.ed_cust_curr);
        if(i==0){
            holder.ed_value.requestFocus();
        }
        /*if (view == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_item_cust_currency, null);
            holder.tv_curr = (TextView) view.findViewById(R.id.tv_cust_curr);
            holder.ed_value = (EditText) view.findViewById(R.id.ed_cust_curr);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }*/
        final CurrencyMasterClass descClass = (CurrencyMasterClass) getItem(i);
        holder.tv_curr.setText(descClass.getCurrency());
        holder.ed_value.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String value = holder.ed_value.getText().toString();
                if(!value.equals("")){
                    descClass.setValue(value);
                }else{
                    descClass.setValue("0");
                }
                currClassList.set(i,descClass);
                //notifyDataSetChanged();
                currInterface.custCurrency(String.valueOf(""));
            }
        });
        return view;
    }

    private class ViewHolder {
        private TextView tv_curr;
        private EditText ed_value;
    }

    public void initInterface(CurrencyInterface _interface){
        currInterface = _interface;
    }
}
