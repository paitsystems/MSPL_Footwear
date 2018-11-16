package com.pait.cust.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pait.cust.R;
import com.pait.cust.interfaces.RecyclerViewToActivityInterface;
import com.pait.cust.model.CheckAvailStockClass;

import java.util.ArrayList;
import java.util.List;

//Created by ANUP on 12/30/2017.

public class CheckAvailStockAdapter extends RecyclerView.Adapter<CheckAvailStockAdapter.CheckAvailStockViewHolder> {

    private RecyclerViewToActivityInterface recyclerViewToActivityInterface;
    private List<CheckAvailStockClass> stockList;
    private Context context;
    private int var = -1;
    private int[] intArr;
    //private List<Integer> selColorList = new ArrayList<>();
    private List<String> whiteHashCodeList = new ArrayList<>();

    public CheckAvailStockAdapter(List<CheckAvailStockClass> _list, Context _context) {
        this.stockList = _list;
        this.context = _context;
        this.intArr = new int[_list.size()];
        addToList();
    }

    @Override
    public CheckAvailStockViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_stock_info, parent, false);
        return new CheckAvailStockViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CheckAvailStockViewHolder holder, int position) {
        CheckAvailStockClass stock = stockList.get(position);
        String stockStr = stock.getSizegroup()+"-"+stock.getStat();
        holder.textView.setText(stockStr);
        holder.textView.setTag(position);
        String hashCode = stock.getHashcode();
        if (whiteHashCodeList.contains(hashCode)) {
            holder.textView.setTextColor(Color.parseColor("#000000"));
            holder.textView.setBackgroundResource(R.drawable.green_boarder_draw);
        } else {
            holder.textView.setTextColor(Color.parseColor("#FFFFFF"));
            holder.textView.setBackgroundColor(Color.parseColor(hashCode));
        }
/*
        if(!AddToCartActivity.map.isEmpty()){
            if(AddToCartActivity.map.containsKey(position)){
                int a = AddToCartActivity.map.get(position);
                if(a==1){
                    holder.lay.setBackgroundResource(R.drawable.green_boarder_draw);
                }else{
                    holder.lay.setBackgroundResource(0);
                }
            }else{
                holder.lay.setBackgroundResource(0);
            }
        }else{
            holder.lay.setBackgroundResource(0);
        }

        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = (int) view.getTag();
                if(AddToCartActivity.map.isEmpty()){
                    AddToCartActivity.map.put(pos,1);
                }else{
                    if(AddToCartActivity.map.containsKey(pos)){
                        if(AddToCartActivity.map.get(pos)==0){
                            AddToCartActivity.map.put(pos,1);
                        }else{
                            AddToCartActivity.map.put(pos,0);
                        }
                    }else{
                        AddToCartActivity.map.put(pos,1);
                    }
                }
                notifyDataSetChanged();
            }
        });
*/
    }

    @Override
    public int getItemCount() {
        return stockList.size();
    }

    public void setOnClickListener1(RecyclerViewToActivityInterface _recyclerViewToActivityInterface) {
        this.recyclerViewToActivityInterface = _recyclerViewToActivityInterface;
    }

    public class CheckAvailStockViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView textView;
        private LinearLayout lay;

        public CheckAvailStockViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.textView);
            lay = (LinearLayout) itemView.findViewById(R.id.lay);
            //textView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            //Constant.showLog(textView.getText().toString());
            // recyclerViewToActivityInterface.onItemClick(textView.getText().toString());
            var = getAdapterPosition();
        }
    }

    private void addToList() {
        whiteHashCodeList.clear();
        whiteHashCodeList.add("#FFFFF0");
        whiteHashCodeList.add("#FFFFF1");
        whiteHashCodeList.add("#FFFFF2");
        whiteHashCodeList.add("#FFFFF3");
        whiteHashCodeList.add("#FFFFF4");
        whiteHashCodeList.add("#FFFFF5");
        whiteHashCodeList.add("#FFFFF6");
        whiteHashCodeList.add("#FFFFF7");
        whiteHashCodeList.add("#FFFFF8");
        whiteHashCodeList.add("#FFFFF9");
        whiteHashCodeList.add("#FFFFFA");
        whiteHashCodeList.add("#FFFFFF");
    }
}
