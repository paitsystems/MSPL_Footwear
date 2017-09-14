package com.lnbinfotech.msplfootwearex.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lnbinfotech.msplfootwearex.R;
import com.lnbinfotech.msplfootwearex.model.TicketMasterClass;

import java.util.ArrayList;
import java.util.Locale;

//Created by lnb on 8/12/2017.

public class AllTicketListAdapter extends BaseAdapter {

    Context context;
    ArrayList<TicketMasterClass> pendingTicketClassList;
    ArrayList<TicketMasterClass> _pendingTicketClassList;

    public AllTicketListAdapter(Context _context, ArrayList<TicketMasterClass> _pendingTicketClassList){
        this.context = _context;
        this.pendingTicketClassList = _pendingTicketClassList;
        this._pendingTicketClassList = new ArrayList<>();
        this._pendingTicketClassList.addAll(this.pendingTicketClassList);
    }

    @Override
    public int getCount() {
        return pendingTicketClassList.size();
    }

    @Override
    public Object getItem(int i) {
        return pendingTicketClassList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if(view==null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_item_all_ticket_list, null);
            holder = new ViewHolder();

            holder.tv_date = (TextView) view.findViewById(R.id.tv_date);
            holder.tv_time = (TextView) view.findViewById(R.id.tv_time);
            holder.tv_subject = (TextView) view.findViewById(R.id.tv_subject);
            holder.tv_desc = (TextView) view.findViewById(R.id.tv_desc);
            holder.tv_status = (TextView) view.findViewById(R.id.tv_status);
            holder.tv_comments = (TextView) view.findViewById(R.id.tv_comments);
            holder.tv_ticket_no = (TextView) view.findViewById(R.id.tv_ticket_no);
            holder.tv_assignto = (TextView) view.findViewById(R.id.tv_assignto);
            //holder.status_lay = (LinearLayout) view.findViewById(R.id.status_lay);
            view.setTag(holder);
        }else{
            holder = (ViewHolder) view.getTag();
        }
        TicketMasterClass pendingTicketClass = (TicketMasterClass) getItem(i);
        holder.tv_date.setText(pendingTicketClass.getCrDate());
        holder.tv_time.setText(pendingTicketClass.getCrTime());
        holder.tv_subject.setText(pendingTicketClass.getSubject());
        String desc = pendingTicketClass.getParticular();
        if(desc.length()>100){
            desc = desc.substring(0,99)+"...";
        }
        holder.tv_desc.setText(desc);
        String status = pendingTicketClass.getStatus();
        if(status.equals("Closed")){
            holder.tv_status.setBackground(context.getResources().getDrawable(R.drawable.status_btn_draw_green));
        }else{
            holder.tv_status.setBackground(context.getResources().getDrawable(R.drawable.status_btn_draw_red));
        }
        holder.tv_status.setText(status);
        holder.tv_ticket_no.setText(pendingTicketClass.getTicketNo());
        holder.tv_assignto.setText(pendingTicketClass.getAssignTO());
        return view;
    }

    private class ViewHolder{
        TextView tv_date, tv_time, tv_subject, tv_desc, tv_status, tv_comments, tv_ticket_no, tv_assignto;
    }

    public void filter(String searchText){
        if(_pendingTicketClassList.size()!=0 && pendingTicketClassList.size()!=0) {
            searchText = searchText.toLowerCase().toLowerCase(Locale.getDefault());
            pendingTicketClassList.clear();
            if (searchText.length() == 0) {
                pendingTicketClassList.addAll(_pendingTicketClassList);
            } else {
                for (TicketMasterClass ticketMasterClass : _pendingTicketClassList) {
                    if (ticketMasterClass.getCrDate().toLowerCase(Locale.getDefault()).contains(searchText)) {
                        pendingTicketClassList.add(ticketMasterClass);
                    } else if (ticketMasterClass.getCrTime().toLowerCase(Locale.getDefault()).contains(searchText)) {
                        pendingTicketClassList.add(ticketMasterClass);
                    } else if (ticketMasterClass.getTicketNo().toLowerCase(Locale.getDefault()).contains(searchText)) {
                        pendingTicketClassList.add(ticketMasterClass);
                    } else if (ticketMasterClass.getStatus().toLowerCase(Locale.getDefault()).contains(searchText)) {
                        pendingTicketClassList.add(ticketMasterClass);
                    } else if (ticketMasterClass.getSubject().toLowerCase(Locale.getDefault()).contains(searchText)) {
                        pendingTicketClassList.add(ticketMasterClass);
                    } else if (ticketMasterClass.getParticular().toLowerCase(Locale.getDefault()).contains(searchText)) {
                        pendingTicketClassList.add(ticketMasterClass);
                    }else if (ticketMasterClass.getAssignTO().toLowerCase(Locale.getDefault()).contains(searchText)) {
                        pendingTicketClassList.add(ticketMasterClass);
                    }
                }
            }
            notifyDataSetChanged();
        }
    }
}
