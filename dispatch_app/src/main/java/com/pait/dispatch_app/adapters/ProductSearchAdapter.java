package com.pait.dispatch_app.adapters;

//Created by lnb on 8/15/2017.

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.pait.dispatch_app.R;
import com.pait.dispatch_app.model.DispatchMasterClass;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ProductSearchAdapter extends BaseAdapter {

    private List<DispatchMasterClass> prodList;
    private List<DispatchMasterClass> _prodList;
    private Context context;
    private String from;

    public ProductSearchAdapter(List<DispatchMasterClass> __prodList, Context _context, String _from) {
        this.prodList = __prodList;
        this._prodList = new ArrayList<>();
        this._prodList.addAll(this.prodList);
        this.context = _context;
        this.from = _from;
    }

    @Override
    public int getCount() {
        return prodList.size();
    }

    @Override
    public Object getItem(int i) {
        return prodList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_item_client_search, null);
            holder.tv_branch = view.findViewById(R.id.tv_branch);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        DispatchMasterClass prodClass = (DispatchMasterClass) getItem(i);
        if(from.equals("1")) {
            holder.tv_branch.setText(prodClass.getPartyName());
        } else if(from.equals("2")) {
            holder.tv_branch.setText(prodClass.getPONO());
        } else if(from.equals("3")) {
            holder.tv_branch.setText(prodClass.getEmp_Name());
        }
        return view;
    }

    private class ViewHolder {
        TextView tv_branch;
    }

    public void filter(String searchText) {
        if (_prodList.size() != 0 && prodList.size() != 0) {
            searchText = searchText.toLowerCase().toLowerCase(Locale.getDefault());
            prodList.clear();
            if (searchText.length() == 0) {
                prodList.addAll(_prodList);
            } else {
                for (DispatchMasterClass prodClass : _prodList) {
                    if (from.equals("1")) {
                        if (prodClass.getPartyName().toLowerCase(Locale.getDefault()).contains(searchText)) {
                            prodList.add(prodClass);
                        }
                    } else if (from.equals("2")) {
                        if (prodClass.getPONO().toLowerCase(Locale.getDefault()).contains(searchText)) {
                            prodList.add(prodClass);
                        }
                    }else if (from.equals("3")) {
                        if (prodClass.getEmp_Name().toLowerCase(Locale.getDefault()).contains(searchText)) {
                            prodList.add(prodClass);
                        }
                    }
                }
            }
            notifyDataSetChanged();
        } else if (_prodList.size() != 0) {
            prodList.addAll(_prodList);
        }
    }
}