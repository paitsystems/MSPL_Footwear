package com.pait.cust.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.pait.cust.R;
import com.pait.cust.constant.Constant;
import com.pait.cust.model.GentsCategoryClass;

import java.util.List;

//Created by lnb on 9/26/2017.

public class GentsCategoryGridAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private List<GentsCategoryClass> gents;
    private String allImageName = "";
    private int pos=0;
    private String img[];

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
    public View getView(final int i, View view, ViewGroup viewGroup) {
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
        String artSize = gentsClass.getCategoryName() +" - " + gentsClass.getCat3();
        holder.tv_catname.setText(artSize);
        holder.tv_mrp.setText(roundDecimals(gentsClass.getMrp()));
        holder.tv_markup.setText(gentsClass.getMarkup());
        holder.tv_markdown.setText(gentsClass.getMarkdown());
        //holder.img.setImageResource(drawId[i]);
        allImageName = gentsClass.getImgName();
        img = allImageName.split(",");
        if(img.length>1) {
            pos = 0;
            loadImage(holder, img[0]);
        }else{
            loadImage(holder, allImageName);
        }
        return view;
    }

    private class ViewHolder {
        public TextView tv_catname, tv_mrp, tv_markup, tv_markdown;
        public ImageView img;
    }

    private void loadImage(final ViewHolder holder,String imageName){
        Glide.with(context).load(imageName)
                .thumbnail(0.5f)
                .crossFade()
                .placeholder(R.drawable.placehoder)
                .error(R.drawable.placehoder)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        if(pos<img.length) {
                            loadImage(holder, img[pos]);
                            pos++;
                        }
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        pos = img.length;
                        Constant.showLog("onResourceReady");
                        return false;
                    }
                })
                .into(holder.img);
    }

    private String roundDecimals(String d) {
        return String.format("%.2f", Double.parseDouble(d));
    }

}
