package com.lnbinfotech.msplfootwear.adapters;

//Created by lnb on 8/15/2017.

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lnbinfotech.msplfootwear.R;
import com.lnbinfotech.msplfootwear.model.ProductMasterClass;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ProductSearchAdapter extends BaseAdapter {

    List<ProductMasterClass> prodList;
    List<ProductMasterClass> _prodList;
    Context context;

    public ProductSearchAdapter(List<ProductMasterClass> __prodList, Context _context) {
        this.prodList = __prodList;
        this._prodList = new ArrayList<>();
        this._prodList.addAll(this.prodList);
        this.context = _context;
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
            holder.tv_branch = (TextView) view.findViewById(R.id.tv_branch);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        ProductMasterClass prodClass = (ProductMasterClass) getItem(i);
        holder.tv_branch.setText(prodClass.getFinal_prod());
        return view;
    }

    private class ViewHolder {
        TextView tv_branch;
    }

    public void filter(String searchText){
        if(_prodList.size()!=0 && prodList.size()!=0) {
            searchText = searchText.toLowerCase().toLowerCase(Locale.getDefault());
            prodList.clear();
            if (searchText.length() == 0) {
                prodList.addAll(_prodList);
            } else {
                for (ProductMasterClass prodClass : _prodList) {
                    if (prodClass.getFinal_prod().toLowerCase(Locale.getDefault()).contains(searchText)) {
                        prodList.add(prodClass);
                    }
                }
            }
            notifyDataSetChanged();
        }else if(_prodList.size()!=0 && prodList.size()==0) {
            prodList.addAll(_prodList);
        }
    }
}
