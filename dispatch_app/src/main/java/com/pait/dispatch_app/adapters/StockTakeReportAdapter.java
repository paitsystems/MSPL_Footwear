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
import com.pait.dispatch_app.interfaces.TestInterface;
import com.pait.dispatch_app.interfaces.TestInterface1;
import com.pait.dispatch_app.model.StockTakeClass;

import java.util.List;

public class StockTakeReportAdapter extends BaseAdapter
        implements TestInterface1 {

    private Context context;
    private List<StockTakeClass> dDlist;
    private TestInterface testInterface;
    private int checkStock;

    public StockTakeReportAdapter(Context _context, List<StockTakeClass> _dDlist, int _checkStock) {
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
            v = inflater.inflate(R.layout.list_item_stock_take_report_adapter, null);
            holder = new ViewHolder();
            holder.tv_articleno = v.findViewById(R.id.tv_articleno);
            holder.tv_totalQty = v.findViewById(R.id.tv_totalQty);
            holder.tv_stockQty = v.findViewById(R.id.tv_stockQty);
            holder.ed_packQty = v.findViewById(R.id.ed_packQty);
            holder.ed_looseQty = v.findViewById(R.id.ed_looseQty);
            holder.tv_articleno.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    testInterface.onResumeFragment(holder.tv_articleno.getText().toString(),"",context);
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
    }

    public List<StockTakeClass> getDataSet(){
        return dDlist;
    }

    private class ViewHolder {
        private TextView tv_articleno, tv_totalQty, tv_stockQty;
        private EditText ed_packQty, ed_looseQty;
    }

}


