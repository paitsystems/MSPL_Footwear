package com.pait.cust.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.pait.cust.R;
import com.pait.cust.model.GentsCategoryClass;

import java.util.List;

//Created by lnb on 10/10/2017.

public class GentsCategoryListAdapter extends BaseAdapter {

    private Context context;
    private List<GentsCategoryClass> gentCatList;
    private LayoutInflater inflater;

    public GentsCategoryListAdapter(Context _context, List<GentsCategoryClass> _gentCatList){
        this.context = _context;
        this.gentCatList = _gentCatList;
        inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return gentCatList.size();
    }

    @Override
    public Object getItem(int i) {
        return gentCatList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final ViewHolder holder;
        if(view==null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.list_item_sub_category_male, viewGroup, false);
            holder.tv_catname = (TextView) view.findViewById(R.id.TextView);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        GentsCategoryClass gentsClass = (GentsCategoryClass) getItem(i);
        holder.tv_catname.setText(gentsClass.getCategoryName());
        return view;
    }

    private class ViewHolder {
        public TextView tv_catname;
    }
}
