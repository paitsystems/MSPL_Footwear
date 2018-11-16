package com.pait.cust.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.pait.cust.R;
import com.pait.cust.model.GentsCategoryClass;

import java.util.List;

//Created by ANUP on 11/2/2017.

public class LadiesNBoysCategoryGridAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private List<GentsCategoryClass> gents;
    private int[] drawId;

    public LadiesNBoysCategoryGridAdapter(Context _context,List<GentsCategoryClass> _gents, int[] _drawId){
        this.context = _context;
        this.gents = _gents;
        this.drawId = _drawId;
        inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return gents.size();
    }

    @Override
    public Object getItem(int i) {
        return gents.get(i);
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
            view = inflater.inflate(R.layout.grid_item_gents_category, viewGroup, false);
            holder.tv_catname = (TextView) view.findViewById(R.id.tv_catename);
            holder.img = (ImageView) view.findViewById(R.id.image);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        GentsCategoryClass gentsClass = (GentsCategoryClass) getItem(i);
        holder.tv_catname.setText(gentsClass.getCategoryName());
        holder.img.setImageResource(drawId[i]);
        return view;
    }

    private class ViewHolder {
        public TextView tv_catname;
        public ImageView img;
    }

}

