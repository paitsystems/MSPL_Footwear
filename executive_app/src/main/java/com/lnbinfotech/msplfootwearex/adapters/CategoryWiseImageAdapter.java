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
import com.lnbinfotech.msplfootwearex.constant.Constant;
import com.lnbinfotech.msplfootwearex.model.GentsCategoryClass;

import java.util.List;

//Created by Anup on 21-08-2018.

public class CategoryWiseImageAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private List<GentsCategoryClass> gents;

    public CategoryWiseImageAdapter(Context _context, List<GentsCategoryClass> _gents) {
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
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.grid_item_categorywise_images, viewGroup, false);
            holder.img = (ImageView) view.findViewById(R.id.image);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        GentsCategoryClass gentsClass = (GentsCategoryClass) getItem(i);
        String imageName = gentsClass.getImgName();
        imageName = imageName.replace(" ", "%20");
        imageName = Constant.imgUrl+imageName+".jpg";
        Constant.showLog(imageName);
        Glide.with(context).load(imageName)
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

