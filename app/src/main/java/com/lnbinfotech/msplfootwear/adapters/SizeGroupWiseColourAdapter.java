package com.lnbinfotech.msplfootwear.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lnbinfotech.msplfootwear.AddToCartActivity;
import com.lnbinfotech.msplfootwear.R;
import com.lnbinfotech.msplfootwear.interfaces.RecyclerViewToActivityInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

//Created by ANUP on 11/13/2017.

public class SizeGroupWiseColourAdapter extends RecyclerView.Adapter<SizeGroupWiseColourAdapter.SizeGroupWiseColourViewHolder> {

    private RecyclerViewToActivityInterface recyclerViewToActivityInterface;
    private List<String> colourList;
    private Context context;
    private int var = -1;
    private int[] intArr;
    //private List<Integer> selColorList = new ArrayList<>();

    public SizeGroupWiseColourAdapter(List<String> _list, Context _context) {
        this.colourList = _list;
        this.context = _context;
        this.intArr = new int[_list.size()];

    }

    @Override
    public SizeGroupWiseColourViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_sizegroup_colour, parent, false);
        return new SizeGroupWiseColourViewHolder(view);
    }


    @Override
    public void onBindViewHolder(SizeGroupWiseColourViewHolder holder, int position) {
        String _colourHashCode = colourList.get(position);
        String[] colourHashCode = _colourHashCode.split("\\-");
        holder.textView.setText(colourHashCode[0]);
        holder.textView.setTag(position);
        String hashCode = colourHashCode[1];
        if (hashCode.equalsIgnoreCase("#FFFFFF")) {
            holder.textView.setTextColor(Color.parseColor("#000000"));
            holder.textView.setBackgroundResource(R.drawable.green_boarder_draw);
        } else {
            holder.textView.setTextColor(Color.parseColor("#FFFFFF"));
            holder.textView.setBackgroundColor(Color.parseColor(hashCode));
        }
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
    }

    @Override
    public int getItemCount() {
        return colourList.size();
    }

    public void setOnClickListener1(RecyclerViewToActivityInterface _recyclerViewToActivityInterface) {
        this.recyclerViewToActivityInterface = _recyclerViewToActivityInterface;
    }

    public class SizeGroupWiseColourViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView textView;
        private LinearLayout lay;

        public SizeGroupWiseColourViewHolder(View itemView) {
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
}

