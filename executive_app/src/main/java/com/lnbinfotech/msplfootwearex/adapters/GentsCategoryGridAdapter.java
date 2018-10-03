package com.lnbinfotech.msplfootwearex.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.lnbinfotech.msplfootwearex.R;
import com.lnbinfotech.msplfootwearex.model.GentsCategoryClass;

import java.util.List;

//Created by lnb on 9/26/2017.

public class GentsCategoryGridAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private List<GentsCategoryClass> gents;

    public GentsCategoryGridAdapter(Context _context,List<GentsCategoryClass> _gents){
        this.context = _context;
        this.gents = _gents;
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
            holder.tv_mrp = (TextView) view.findViewById(R.id.tv_mrp);
            holder.tv_markup = (TextView) view.findViewById(R.id.tv_markup);
            holder.tv_markdown = (TextView) view.findViewById(R.id.tv_markdown);
            holder.img = (ImageView) view.findViewById(R.id.image);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        GentsCategoryClass gentsClass = (GentsCategoryClass) getItem(i);
        holder.tv_catname.setText(gentsClass.getCategoryName());
        holder.tv_mrp.setText(gentsClass.getMrp());
        holder.tv_markup.setText(gentsClass.getMarkup());
        holder.tv_markdown.setText(gentsClass.getMarkdown());
        //holder.img.setImageResource(drawId[i]);

        Glide.with(context).load(gentsClass.getImgName())
                .thumbnail(0.5f)
                .crossFade()
                .placeholder(R.drawable.placehoder)
                .error(R.drawable.placehoder)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.img);
        return view;
    }

    private class ViewHolder {
        public TextView tv_catname, tv_mrp, tv_markup, tv_markdown;
        public ImageView img;
    }

}

