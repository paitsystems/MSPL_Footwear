package com.lnbinfotech.paragaon.adapter;

//Created by lnb on 8/15/2017.

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lnbinfotech.paragaon.R;
import com.lnbinfotech.paragaon.model.TicketDetailClass;

import java.util.ArrayList;

public class ReplyResponseListAdapter extends BaseAdapter {

    ArrayList<TicketDetailClass> ticketDetailClassList;
    Context context;

    public ReplyResponseListAdapter(ArrayList<TicketDetailClass> _ticketDetailClassList, Context _context) {
        this.ticketDetailClassList = _ticketDetailClassList;
        this.context = _context;
    }

    @Override
    public int getCount() {
        return ticketDetailClassList.size();
    }

    @Override
    public Object getItem(int i) {
        return ticketDetailClassList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final ViewHolder holder;
        //TicketDetailClass detClass = ticketDetailClassList.get(i);
        TicketDetailClass detClass = (TicketDetailClass) getItem(i);
        holder = new ViewHolder();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(detClass.getType().equals("C")) {
            view = inflater.inflate(R.layout.list_item_ticket_detail_reply, null);
        }else{
            view = inflater.inflate(R.layout.list_item_ticket_detail_response, null);
        }
        holder.tv_datetime = (TextView) view.findViewById(R.id.tv_datetime);
        holder.tv_response = (TextView) view.findViewById(R.id.tv_response);
        String _datetime = detClass.getCrDate() + " " + detClass.getCrTime();
        holder.tv_datetime.setText(_datetime);
        holder.tv_response.setText(detClass.getDesc());
        return view;
    }

    private class ViewHolder {
        TextView tv_datetime, tv_response;
    }
}
