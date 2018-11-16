package com.pait.exec.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.pait.exec.R;
import com.pait.exec.model.ArealineMasterClass;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

//Created by ANUP on 1/16/2018.

public class ArealineSelectionAdapter extends BaseAdapter {

    private Context context;
    private List<ArealineMasterClass> areaLineList, _areaLineList;

    public ArealineSelectionAdapter(Context _context, List<ArealineMasterClass> __areaLine){
        this.context = _context;
        this.areaLineList = __areaLine;
        this._areaLineList = new ArrayList<>();
        this._areaLineList.addAll(this.areaLineList);
    }

    @Override
    public int getCount() {
        return areaLineList.size();
    }

    @Override
    public Object getItem(int i) {
        return areaLineList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if(view==null){
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_item_arealine_item,null);
            view.setTag(holder);
        }else{
            holder = (ViewHolder) view.getTag();
        }
        holder.tv_arealine = (TextView) view.findViewById(R.id.TextView);
        ArealineMasterClass areaLineList = (ArealineMasterClass) getItem(i);
        holder.tv_arealine.setText(areaLineList.getArea());
        return view;
    }

    private class  ViewHolder{
        private TextView tv_arealine;
    }

    public void filter(String searchText){
        if(_areaLineList.size()!=0 && areaLineList.size()!=0) {
            searchText = searchText.toLowerCase().toLowerCase(Locale.getDefault());
            areaLineList.clear();
            if (searchText.length() == 0) {
                areaLineList.addAll(_areaLineList);
            } else {
                for (ArealineMasterClass areaClass : _areaLineList) {
                    if (areaClass.getArea().toLowerCase(Locale.getDefault()).contains(searchText)) {
                        areaLineList.add(areaClass);
                    }
                }
            }
            notifyDataSetChanged();
        }else if(_areaLineList.size()!=0 && areaLineList.size()==0) {
            areaLineList.addAll(_areaLineList);
        }
    }
}
