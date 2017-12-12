package com.lnbinfotech.msplfootwearex.adapters;

//Created by lnb on 8/15/2017.

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lnbinfotech.msplfootwearex.R;

import java.util.HashMap;
import java.util.List;

public class ViewAddedToCardItemAdapter extends BaseAdapter {

    private HashMap<String, List<String>> orderMap;
    private Context context;
    private List<String> sizeGroupList;

    public ViewAddedToCardItemAdapter(HashMap<String, List<String>> _orderMap, Context _context, List<String> _sizeGroupList) {
        this.orderMap = _orderMap;
        this.context = _context;
        this.sizeGroupList = _sizeGroupList;
    }

    @Override
    public int getCount() {
        return sizeGroupList.size();
    }

    @Override
    public Object getItem(int i) {
        return sizeGroupList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        holder = new ViewHolder();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.list_item_view_add_to_card, null);
        holder.lay_horizontal = (LinearLayout) view.findViewById(R.id.lay_horizontal);
        view.setTag(holder);

        String key = (String) getItem(i);

        TextView tv = new TextView(context);
        tv.setText(key);
        tv.setTextColor(Color.parseColor("#000000"));
        //tv.setLayoutParams(new TableLayout.LayoutParams(80, 50, 0.5f));
        //tv.setPadding(2,1,2,1);
        //LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(80,40);
        params.setMargins(2,1,2,1);
        tv.setLayoutParams(params);
        holder.lay_horizontal.addView(tv);

        List<String> strList = orderMap.get(key);
        for (String str : strList) {
            String strArr[] = str.split("\\-");
            tv = new TextView(context);
            tv.setText(strArr[4]);
            tv.setGravity(Gravity.CENTER);
            String hashCode = strArr[2];
            if (hashCode.equalsIgnoreCase("#FFFFFF")) {
                tv.setTextColor(Color.parseColor("#000000"));
                tv.setBackgroundResource(R.drawable.black_border_draw);
            } else {
                tv.setTextColor(Color.parseColor("#FFFFFF"));
                tv.setBackgroundColor(Color.parseColor(hashCode));
            }
            //tv.setPadding(2,1,2,1);
            //tv.setLayoutParams(new TableLayout.LayoutParams(80, 50,0.5f));
            tv.setLayoutParams(params);
            holder.lay_horizontal.addView(tv);
        }
        return view;
    }

    private class ViewHolder {
        public LinearLayout lay_horizontal;
    }
}
