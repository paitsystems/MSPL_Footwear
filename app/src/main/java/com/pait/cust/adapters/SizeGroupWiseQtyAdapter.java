package com.pait.cust.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pait.cust.AddToCartActivity;
import com.pait.cust.R;
import com.pait.cust.interfaces.RecyclerViewToActivityInterface;

import java.util.List;

//Created by ANUP on 11/9/2017.

public class SizeGroupWiseQtyAdapter extends RecyclerView.Adapter<SizeGroupWiseQtyAdapter.SizeGroupWiseQtyViewHolder> {

    private RecyclerViewToActivityInterface recyclerViewToActivityInterface;
    private List<String> sizeList;
    private Context context;
    private int var = -1, selPos = 0;
    private String listType;

    public SizeGroupWiseQtyAdapter(List<String> _list,Context _context,String listType, int selPos){
        this.sizeList = _list;
        this.context = _context;
        this.listType = listType;
        this.selPos = selPos;
    }

    @Override
    public SizeGroupWiseQtyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_sizegroup_qty,parent,false);
        if(listType.equals("U")){
            var = selPos;
        }
        return new SizeGroupWiseQtyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SizeGroupWiseQtyViewHolder holder, int position) {
        holder.textView.setText(sizeList.get(position));
        if(position==sizeList.size()-1){
            if(var!=-1) {
                if(var==position) {
                    holder.textView.setTextColor(context.getResources().getColor(R.color.lightyellow));
                    holder.textView.setBackgroundResource(R.drawable.add_select_company_pack_draw);
                }else{
                    holder.textView.setTextColor(context.getResources().getColor(R.color.red));
                    holder.textView.setBackgroundResource(R.drawable.add_company_pack_draw);
                }
            }else{
                holder.textView.setTextColor(context.getResources().getColor(R.color.red));
                holder.textView.setBackgroundResource(R.drawable.add_company_pack_draw);
            }
        }else{
            if(var!=-1){
                if(var==position) {
                    holder.textView.setTextColor(context.getResources().getColor(R.color.white));
                    holder.textView.setBackgroundResource(R.drawable.add_selected_qty_draw);
                }else{
                    holder.textView.setTextColor(context.getResources().getColor(R.color.black));
                    holder.textView.setBackgroundResource(R.drawable.black_border_draw);
                }
            }else{
                holder.textView.setTextColor(context.getResources().getColor(R.color.black));
                holder.textView.setBackgroundResource(R.drawable.black_border_draw);
            }
        }
    }

    @Override
    public int getItemCount() {
        return sizeList.size();
    }

    public void setOnClickListener1(RecyclerViewToActivityInterface _recyclerViewToActivityInterface){
        this.recyclerViewToActivityInterface = _recyclerViewToActivityInterface;
    }

    public class SizeGroupWiseQtyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView textView;

        public SizeGroupWiseQtyViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.textView);
            textView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            //Constant.showLog(textView.getText().toString());
            var = getAdapterPosition();
            listType = "A";
            AddToCartActivity.selQty = var;
            notifyDataSetChanged();
            recyclerViewToActivityInterface.onItemClick(textView.getText().toString());
        }
    }
}
