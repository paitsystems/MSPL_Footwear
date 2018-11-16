package com.pait.exec.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.pait.exec.R;
import com.pait.exec.ViewCustomerOrderActiviy;
import com.pait.exec.interfaces.RecyclerViewToActivityInterface;
import com.pait.exec.model.CompanyMasterClass;

import java.util.List;

//Created by ANUP on 12/13/2017.

public class DispatchCenterListAdapter extends RecyclerView.Adapter<DispatchCenterListAdapter.DispatchCenterViewHolder> {

    private RecyclerViewToActivityInterface recyclerViewToActivityInterface;
    private List<CompanyMasterClass> dispatchcenter_list;
    private Context context;
    private int var = -1;

    public DispatchCenterListAdapter(List<CompanyMasterClass> _list,Context _context){
        this.dispatchcenter_list = _list;
        this.context = _context;
        //setMapData();
    }

    @Override
    public DispatchCenterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_dispatch_center,parent,false);
        return new DispatchCenterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final DispatchCenterViewHolder holder, final int position) {
        final CompanyMasterClass comClass = dispatchcenter_list.get(position);
        holder.tv_dispatchcenter.setText(comClass.getCompanyInitial());
        int a = ViewCustomerOrderActiviy.cbMap.get(Integer.parseInt(comClass.companyId));
        if(a==0){
            holder.cb_dispatchCenter.setChecked(false);
        }else{
            holder.cb_dispatchCenter.setChecked(true);
        }
        holder.cb_dispatchCenter.setOnCheckedChangeListener(null);
        holder.cb_dispatchCenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int a = ViewCustomerOrderActiviy.cbMap.get(Integer.parseInt(comClass.companyId));
                if(a==0){
                    ViewCustomerOrderActiviy.cbMap.put(Integer.parseInt(comClass.companyId),1);
                }else{
                    ViewCustomerOrderActiviy.cbMap.put(Integer.parseInt(comClass.companyId),0);
                }
                recyclerViewToActivityInterface.onItemClick(comClass.companyId);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return dispatchcenter_list.size();
    }

    public void setOnClickListener1(RecyclerViewToActivityInterface _recyclerViewToActivityInterface){
        this.recyclerViewToActivityInterface = _recyclerViewToActivityInterface;
    }

    public class DispatchCenterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private CheckBox cb_dispatchCenter;
        private TextView tv_dispatchcenter;

        public DispatchCenterViewHolder(View itemView) {
            super(itemView);
            tv_dispatchcenter = (TextView) itemView.findViewById(R.id.tv_dispatchname);
            cb_dispatchCenter = (CheckBox) itemView.findViewById(R.id.cb_dispatchcenter);
            //cb_dispatchCenter.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            var = getAdapterPosition();
            //notifyDataSetChanged();
            //recyclerViewToActivityInterface.onItemClick(cb_dispatchCenter.getText().toString());
        }
    }

    /*private void setMapData(){
        cbMap.clear();
        for(int i=0;i<dispatchcenter_list.size();i++){
            cbMap.put(i,0);
        }
    }*/
}
