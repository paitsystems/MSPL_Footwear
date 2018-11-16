package com.pait.exec.adapters;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.pait.exec.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SNEHA on 12/5/2017.
 */
public class DisplayAreawiseCustListAdapter extends BaseAdapter implements Filterable {
    Activity activity;
    List<String> item_list;
    List<String> filter_list;
    ValueFilter valueFilter;

    public DisplayAreawiseCustListAdapter(Activity _activity, List<String> _item_list) {
        this.activity = _activity;
        this.item_list = _item_list;
        this.filter_list = _item_list;
        getFilter();
    }

    @Override
    public int getCount() {
        return item_list.size();
    }

    @Override
    public Object getItem(int position) {
        return item_list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        View v = convertView;
        final ViewHolder holder;
        LayoutInflater inflater = activity.getLayoutInflater();
        if (convertView == null) {
            v = inflater.inflate(R.layout.item_list, null);
            holder = new ViewHolder();
            holder.tv_item = (TextView) v.findViewById(R.id.tv_item);
            v.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        String obj = item_list.get(position);
        holder.tv_item.setText(obj);

        return v;
    }

    private class ViewHolder {
        public TextView tv_item;
    }

    @Override
    public Filter getFilter() {
        if (valueFilter == null) {
            valueFilter = new ValueFilter();
        }
        return valueFilter;
    }

    private class ValueFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            FilterResults results = new FilterResults();
            ArrayList<String> filter_ls = new ArrayList<>();

            if (charSequence == null && charSequence.length() == 0) {
                results.count = filter_list.size();
                Log.d("Log", "filter_list:" + filter_list.size());
                results.values = filter_list;
            } else {
                charSequence = charSequence.toString().toLowerCase();
                for (int i = 0; i < filter_list.size(); i++) {
                    String data = filter_list.get(i);
                    if (data.toLowerCase().contains(charSequence.toString())) {
                        filter_ls.add(data);
                    }
                }
                results.count = filter_ls.size();
                Log.d("Log", "filter_ls:" + filter_ls.size());
                results.values = filter_ls;
                Log.d("Log", "filter_ls value:" + filter_ls);
            }
            /*if (charSequence != null && charSequence.length() > 0) {
                for (int i = 0; i < filter_list.size(); i++) {
                    if (filter_list.get(i).contains(charSequence)) {
                        filter_ls.add(filter_list.get(i));
                    }
                }
                results.count = filter_ls.size();
                results.values = filter_ls;
            } else {
                results.count = filter_list.size();
                results.values = filter_list;
            }*/
            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            if (filterResults.count == 0) {
                return;
            }
            item_list = (List<String>) filterResults.values;
            Log.d("Log", "filter values publish:" + filterResults.values);
            notifyDataSetChanged();
            //removes all values from adapter list.
            /*item_list.clear();
             for(String item:item_list)
                item_list.add(item);

            //adapter list contains only filtered list.
           notifyDataSetChanged();*/
        }
    }
}