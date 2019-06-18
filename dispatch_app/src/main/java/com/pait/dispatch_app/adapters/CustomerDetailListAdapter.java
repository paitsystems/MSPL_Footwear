package com.pait.dispatch_app.adapters;

//Created by lnb on 8/15/2017.

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.pait.dispatch_app.R;
import com.pait.dispatch_app.constant.Constant;
import com.pait.dispatch_app.parse.UserClass;

import java.util.ArrayList;

public class CustomerDetailListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<UserClass> list;

    public CustomerDetailListAdapter(Context _context, ArrayList<UserClass> _list) {
        this.context = _context;
        this.list = _list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
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
            view = inflater.inflate(R.layout.list_item_customer_details, viewGroup,false);
            holder.tv_custname = view.findViewById(R.id.tv_custname);
            holder.tv_custaddress = view.findViewById(R.id.tv_custaddress);
            holder.tv_custmobile = view.findViewById(R.id.tv_custmobile);
            holder.tv_custemail = view.findViewById(R.id.tv_custemail);
            holder.img_cust = view.findViewById(R.id.img_cust);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        UserClass userClass = (UserClass) getItem(i);
        holder.tv_custname.setText(userClass.getName());
        holder.tv_custaddress.setText(userClass.getAddress());
        holder.tv_custmobile.setText(userClass.getMobile());
        holder.tv_custemail.setText(userClass.getEmail());
        String img = userClass.getImagePath();
        Constant.showLog(Constant.custimgUrl+img);
        Glide.with(context).load(Constant.custimgUrl+userClass.getImagePath())
                .thumbnail(0.5f)
                .crossFade()
                .placeholder(R.drawable.ic_male)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.img_cust);
        return view;
    }

    private class ViewHolder {
        private TextView tv_custname, tv_custaddress, tv_custmobile, tv_custemail;
        private ImageView img_cust;
    }
}