package com.pait.dispatch_app.adapters;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pait.dispatch_app.R;
import com.pait.dispatch_app.constant.Constant;
import com.pait.dispatch_app.interfaces.TestInterface;
import com.pait.dispatch_app.interfaces.TestInterface1;
import com.pait.dispatch_app.model.StockTakeClass;

import java.util.List;

public class StockTakeAdapter  extends BaseAdapter
        implements TestInterface1 {

    private Context context;
    private List<StockTakeClass> dDlist;
    private TestInterface testInterface;
    private int checkStock;

    public StockTakeAdapter(Context _context, List<StockTakeClass> _dDlist, int _checkStock) {
        this.context = _context;
        this.dDlist = _dDlist;
        this.checkStock = _checkStock;
    }

    public void initInterface(TestInterface _testInterface) {
        this.testInterface = _testInterface;
    }

    @Override
    public int getCount() {
        return dDlist.size();
    }

    @Override
    public StockTakeClass getItem(int position) {
        return dDlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup viewGroup) {
        View v = convertView;
        final ViewHolder holder;
        StockTakeClass st = getItem(position);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            v = inflater.inflate(R.layout.list_item_stock_take_adapter, null);
            holder = new ViewHolder();
            holder.tv_articleno = v.findViewById(R.id.tv_articleno);
            holder.tv_totalQty = v.findViewById(R.id.tv_totalQty);
            holder.tv_stockQty = v.findViewById(R.id.tv_stockQty);
            holder.ed_packQty = v.findViewById(R.id.ed_packQty);
            holder.ed_looseQty = v.findViewById(R.id.ed_looseQty);
            holder.lay_totQty = v.findViewById(R.id.lay_totQty);

            holder.ed_packQty.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    //StockTakeClass cheque2 = dDlist.get(pos);
                    String str = editable.toString();
                    if (str.equals("")) {
                        str = "0";
                    }
                    String lQty = st.getLooseQty();
                    if (lQty.equals("")) {
                        lQty = "0";
                    }
                    int noOfPiece = Integer.parseInt(st.getNoOfPices());
                    int pQty = Integer.parseInt(str);
                    pQty = pQty * noOfPiece;
                    int totQty = pQty + Integer.parseInt(lQty);
                    st.setTotalQty(String.valueOf(totQty));
                    st.setPackQty(str);
                    holder.tv_totalQty.setText(String.valueOf(totQty));

                }
            });

            holder.ed_looseQty.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    String str = editable.toString();
                    if (str.equals("")) {
                        str = "0";
                    }
                    String pQty = st.getPackQty();
                    if (pQty.equals("")) {
                        pQty = "0";
                    }
                    int noOfPiece = Integer.parseInt(st.getNoOfPices());
                    int pQtyInt = Integer.parseInt(pQty);
                    pQtyInt = pQtyInt * noOfPiece;
                    int totQty = Integer.parseInt(str) + pQtyInt;
                    st.setTotalQty(String.valueOf(totQty));
                    st.setLooseQty(str);
                    holder.tv_totalQty.setText(String.valueOf(totQty));
                }
            });

            v.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_articleno.setText(st.getProductId());
        holder.tv_stockQty.setText(st.getStockQty());
        holder.ed_packQty.setText(st.getPackQty());
        holder.ed_looseQty.setText(st.getLooseQty());
        //Constant.showLog(st.getProductId() + "-" + st.getTotalQty() + "-" + st.getStockQty() + "-" + st.getPackQty() + "-" + st.getLooseQty());
        int totQty = Integer.parseInt(st.getTotalQty());
        if (checkStock == 0) {
            holder.tv_totalQty.setText(String.valueOf(totQty));
        } else {
            int stockQty = Integer.parseInt(st.getStockQty());
            if (totQty == stockQty) {
                holder.tv_totalQty.setText(String.valueOf(totQty));
                holder.tv_totalQty.setTextColor(context.getResources().getColor(R.color.white));
                holder.tv_totalQty.setBackgroundColor(context.getResources().getColor(R.color.darkgreen));
            } else {
                holder.tv_totalQty.setText(String.valueOf(totQty));
                holder.tv_totalQty.setTextColor(context.getResources().getColor(R.color.white));
                holder.tv_totalQty.setBackgroundColor(context.getResources().getColor(R.color.red));
            }
        }

        return v;
    }

    @Override
    public int getViewTypeCount() {
        return getCount();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void returnDate(String dt) {
    }

    @Override
    public void returnImage(String img) {
        notifyDataSetChanged();
    }

    public List<StockTakeClass> getDataSet(){
        return dDlist;
    }

    private class ViewHolder {
        private TextView tv_articleno, tv_totalQty, tv_stockQty;
        private EditText ed_packQty, ed_looseQty;
        private LinearLayout lay_totQty;
    }

    private class GenericTextWatcher implements TextWatcher {

        private View view;
        private int pos;

        private GenericTextWatcher(View _view, int _pos) {
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
            switch (view.getId()) {
                case R.id.ed_packQty:
                    StockTakeClass cheque2 = dDlist.get(pos);
                    if (str.equals("")) {
                        str = "0";
                    }
                    String lQty = cheque2.getLooseQty();
                    if(lQty.equals("")){
                        lQty = "0";
                    }
                    int c = Integer.parseInt(str) + Integer.parseInt(lQty);
                    cheque2.setTotalQty(String.valueOf(c));
                    cheque2.setPackQty(str);
                    dDlist.set(pos, cheque2);
                    break;
                case R.id.ed_looseQty:
                    StockTakeClass cheque1 = dDlist.get(pos);
                    if (str.equals("")) {
                        str = "0";
                    }
                    String pQty = cheque1.getPackQty();
                    if(pQty.equals("")){
                        pQty = "0";
                    }
                    int d = Integer.parseInt(str) + Integer.parseInt(pQty);
                    cheque1.setTotalQty(String.valueOf(d));
                    cheque1.setLooseQty(str);
                    dDlist.set(pos, cheque1);
                    notifyDataSetChanged();
                    break;
            }
        }
    }
}

