package com.pait.exec.adapters;

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

import com.pait.exec.R;
import com.pait.exec.interfaces.CurrencyInterface;
import com.pait.exec.model.CurrencyMasterClass;

import java.util.ArrayList;

public class SaleExeCurrenyAdapter extends BaseAdapter {

    private ArrayList<CurrencyMasterClass> seCurrList;
    private Context context;
    private CurrencyInterface currInterface;

    public SaleExeCurrenyAdapter(ArrayList<CurrencyMasterClass> _seCurrList, Context _context) {
        this.seCurrList = _seCurrList;
        this.context = _context;
    }

    @Override
    public int getCount() {
        return seCurrList.size();
    }

    @Override
    public Object getItem(int i) {
        return seCurrList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_item_se_currency, null);
            holder.tv_curr = (TextView) view.findViewById(R.id.tv_se_curr);
            holder.tv_tot = (TextView) view.findViewById(R.id.tv_se_curr_total);
            holder.ed_value = (EditText) view.findViewById(R.id.ed_se_curr);
            holder.ed_value.addTextChangedListener(new GenericTextWatcher(holder.ed_value,i,holder));
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        CurrencyMasterClass descClass = (CurrencyMasterClass) getItem(i);
        holder.tv_curr.setText(descClass.getCurrency());
        holder.tv_tot.setText(descClass.getTotal());
        return view;
    }

    @Override
    public int getViewTypeCount() {
        return getCount();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    private class ViewHolder {
        private TextView tv_curr, tv_tot;
        private EditText ed_value;
    }

    public void initInterface(CurrencyInterface _interface){
        currInterface = _interface;
    }

    private class GenericTextWatcher implements TextWatcher {

        private View view;
        private int pos;
        private ViewHolder holder;

        private GenericTextWatcher(View _view, int _pos, ViewHolder _holder){
            this.view = _view;
            this.pos = _pos;
            this.holder = _holder;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            switch (view.getId()){
                case R.id.ed_se_curr:
                    String value = editable.toString();
                    if(!value.equals("")){
                        CurrencyMasterClass descClass = seCurrList.get(pos);
                        String cur = descClass.getCurrency();
                        int tot = Integer.parseInt(cur)*Integer.parseInt(value);
                        descClass.setValue(value);
                        descClass.setTotal(String.valueOf(tot));
                        holder.tv_tot.setText(String.valueOf(tot));
                        seCurrList.set(pos,descClass);
                    }else{
                        CurrencyMasterClass descClass = seCurrList.get(pos);
                        descClass.setValue("0");
                        descClass.setTotal("0");
                        holder.tv_tot.setText("0");
                        seCurrList.set(pos,descClass);
                    }
                    //notifyDataSetChanged();
                    currInterface.seCurrency(String.valueOf(""));
                    break;
            }
        }
    }

}
