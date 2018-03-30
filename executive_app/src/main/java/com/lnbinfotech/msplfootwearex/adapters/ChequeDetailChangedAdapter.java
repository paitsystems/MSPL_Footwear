package com.lnbinfotech.msplfootwearex.adapters;

import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.lnbinfotech.msplfootwearex.R;
import com.lnbinfotech.msplfootwearex.constant.Constant;
import com.lnbinfotech.msplfootwearex.model.ChequeDetailsClass;

import java.util.List;

//Created by ANUP on 3/27/2018.

public class ChequeDetailChangedAdapter extends BaseAdapter {

    private Activity activity;
    private List<ChequeDetailsClass> cheque_list;

    public ChequeDetailChangedAdapter(Activity _activity, List<ChequeDetailsClass> _cheque_list) {
        this.activity = _activity;
        this.cheque_list = _cheque_list;
    }

    @Override
    public int getCount() {
        return cheque_list.size();
    }

    @Override
    public ChequeDetailsClass getItem(int position) {
        return cheque_list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup viewGroup) {
        View v = convertView;
        final ViewHolder holder;
        LayoutInflater inflater = activity.getLayoutInflater();
        if (convertView == null) {
            v = inflater.inflate(R.layout.list_item_cheque_detail_changed, null);
            holder = new ViewHolder();
            holder.tv_srno = (TextView) v.findViewById(R.id.tv_srno);
            holder.tv_cqdate = (TextView) v.findViewById(R.id.tv_cqdate);
            holder.ed_cqno = (EditText) v.findViewById(R.id.ed_cqno);
            holder.ed_amnt = (EditText) v.findViewById(R.id.ed_amnt);
            holder.tv_img = (TextView) v.findViewById(R.id.tv_img);

            holder.ed_cqno.addTextChangedListener(new GenericTextWatcher(holder.ed_cqno,position));
            holder.ed_amnt.addTextChangedListener(new GenericTextWatcher(holder.ed_amnt,position));

            holder.tv_cqdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Constant.showLog(holder.tv_cqdate.getText().toString());
                }
            });

            holder.tv_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Constant.showLog(holder.tv_img.getText().toString());
                }
            });
            v.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ChequeDetailsClass cd = cheque_list.get(position);
        holder.tv_srno.setText(String.valueOf(cd.getSrNo()));
        holder.tv_cqdate.setText(cd.getChq_det_date());
        holder.ed_cqno.setText(cd.getChq_det_number());
        holder.ed_amnt.setText(cd.getChq_det_amt());
        //holder.tv_img.setText(cd.getChq_det_ref());
        return v;
    }

    private class ViewHolder {
        private TextView tv_srno, tv_cqdate, tv_img;
        private EditText ed_cqno, ed_amnt;
    }

    private class GenericTextWatcher implements TextWatcher {

        private View view;
        private int pos;

        private GenericTextWatcher(View _view, int _pos){
            this.view = _view;
            this.pos = _pos;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            String str = editable.toString();
            switch (view.getId()){
                case R.id.ed_cqno:
                    ChequeDetailsClass cheque = cheque_list.get(pos);
                    cheque.setChq_det_number(str);
                    cheque_list.set(pos,cheque);
                    Constant.showLog(str);
                    break;
                case R.id.ed_amnt:
                    ChequeDetailsClass cheque1 = cheque_list.get(pos);
                    cheque1.setChq_det_amt(str);
                    cheque_list.set(pos,cheque1);
                    Constant.showLog(str);
                    break;
            }
        }
    }
}
