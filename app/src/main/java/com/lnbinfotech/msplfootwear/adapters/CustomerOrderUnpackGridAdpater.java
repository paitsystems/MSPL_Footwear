package com.lnbinfotech.msplfootwear.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.lnbinfotech.msplfootwear.AddToCartActivity;
import com.lnbinfotech.msplfootwear.R;
import com.lnbinfotech.msplfootwear.constant.Constant;

import java.util.List;

//Created by ANUP on 11/26/2017.

public class CustomerOrderUnpackGridAdpater extends BaseAdapter {

    private Context context;
    private List<String> sizeList;
    private int requestFocus = 0;

    public CustomerOrderUnpackGridAdpater(Context _context, List<String> _sizeList){
        this.context = _context;
        this.sizeList = _sizeList;
    }

    @Override
    public int getCount() {
        return sizeList.size();
    }

    @Override
    public String getItem(int i) {
        return sizeList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int pos, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view=inflater.inflate(R.layout.grid_item_cust_order_unpack, viewGroup, false);

        final EditText tv = (EditText) view.findViewById(R.id.ed_looseqty);
        String _colourHashCode = sizeList.get(pos);
        String[] colourHashCode = _colourHashCode.split("\\-");
        if(colourHashCode.length>1) {
            String hashCode = colourHashCode[1];
            if (hashCode.equalsIgnoreCase("#FFFFFF")) {
                tv.setTextColor(Color.parseColor("#000000"));
                GradientDrawable background = (GradientDrawable) tv.getBackground();
                background.setColor(Color.parseColor("#FFFFFF"));
                tv.setBackground(background);
            } else {
                tv.setTextColor(Color.parseColor("#FFFFFF"));
                GradientDrawable background = (GradientDrawable) tv.getBackground();
                background.setColor(Color.parseColor(hashCode));
                tv.setBackground(background);
            }
            tv.setText(colourHashCode[0]);
            tv.setOnClickListener(null);
            tv.setFocusable(false);
            tv.addTextChangedListener(null);
        }else if(colourHashCode[0].equals("+2") || colourHashCode[0].equals("+1")){
            tv.setText(colourHashCode[0]);
            tv.setBackgroundDrawable(null);
            tv.setVisibility(View.INVISIBLE);
        }else{
            tv.setText(colourHashCode[0]);
            if(requestFocus==pos){
                tv.requestFocus();
                tv.setSelection(tv.getText().length());
            }
            GradientDrawable background = (GradientDrawable) tv.getBackground();
            background.setColor(Color.parseColor("#FFFFFF"));
            tv.setBackground(background);
            if(AddToCartActivity.unClickablePositionsList.contains(pos)){
                tv.setOnClickListener(null);
                tv.setFocusable(false);
                tv.addTextChangedListener(null);
            }else {
                tv.setFocusable(true);
                tv.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        /*if(tv.getText().toString().equals("")){
                            tv.setText("0");
                            tv.setSelected(true);
                        }*/
                        if(AddToCartActivity.allSizeChangeList.contains(pos)) {
                            String allSize = tv.getText().toString();
                            sizeList.set(pos, allSize);
                            int changeValuePos = pos;
                            for(int a=0;a<AddToCartActivity.sizeGroup_list.size();a++){
                                changeValuePos = changeValuePos + AddToCartActivity.colour_list.size() + 1;
                                sizeList.set(changeValuePos, allSize);
                            }
                            requestFocus = pos;
                            notifyDataSetChanged();
                        }else{
                            String allSize = tv.getText().toString();
                            sizeList.set(pos, allSize);
                            int changeValuePos = pos;
                            for(int a=0;a<AddToCartActivity.sizeGroup_list.size();a++){
                                changeValuePos = changeValuePos - AddToCartActivity.colour_list.size() - 1;
                                if(AddToCartActivity.allSizeChangeList.contains(changeValuePos)) {
                                    sizeList.set(changeValuePos, "0");
                                    break;
                                }
                            }
                            requestFocus = pos;
                            notifyDataSetChanged();
                        }
                    }
                });
            }
        }
        return view;
    }

    private class ViewHolder {
        //private TextView tv;
        private EditText tv;
    }
}
