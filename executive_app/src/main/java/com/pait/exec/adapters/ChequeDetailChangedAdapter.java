package com.pait.exec.adapters;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.pait.exec.ChequeDetailsActivityChanged;
import com.pait.exec.R;
import com.pait.exec.constant.Constant;
import com.pait.exec.interfaces.TestInterface;
import com.pait.exec.interfaces.TestInterface1;
import com.pait.exec.model.ChequeDetailsClass;

import java.io.File;
import java.util.List;

//Created by ANUP on 3/27/2018.

public class ChequeDetailChangedAdapter extends BaseAdapter implements TestInterface1{

    private Context context;
    private List<ChequeDetailsClass> cheque_list;
    private int dtPos;
    private TestInterface testInterface;

    public ChequeDetailChangedAdapter(Context _context, List<ChequeDetailsClass> _cheque_list) {
        this.context = _context;
        this.cheque_list = _cheque_list;
    }

    public void initInterface(TestInterface _testInterface){
        this.testInterface = _testInterface;
    }

    @Override
    public int getCount() {
        return cheque_list.size();
    }

    @Override
    public ChequeDetailsClass getItem(int position) {
        return cheque_list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup viewGroup) {
        View v = convertView;
        final ViewHolder holder;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            v = inflater.inflate(R.layout.list_item_cheque_detail_changed, null);
            holder = new ViewHolder();
            holder.tv_srno = (TextView) v.findViewById(R.id.tv_srno);
            holder.tv_cqdate = (TextView) v.findViewById(R.id.tv_cqdate);
            holder.ed_cqno = (EditText) v.findViewById(R.id.ed_cqno);
            holder.ed_amnt = (EditText) v.findViewById(R.id.ed_amnt);
            holder.tv_img = (TextView) v.findViewById(R.id.tv_img);
            holder.img_chq = (ImageView) v.findViewById(R.id.img_cq);

            holder.ed_cqno.addTextChangedListener(new GenericTextWatcher(holder.ed_cqno,position));
            holder.ed_amnt.addTextChangedListener(new GenericTextWatcher(holder.ed_amnt,position));

            holder.tv_cqdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dtPos = position;
                    testInterface.onResumeFragment("","",context);
                }
            });

            holder.img_chq.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dtPos = position;
                    ChequeDetailsClass cd = getItem(position);
                    testInterface.onPauseFragment(cd.getChq_det_number(),"",context);
                }
            });
            v.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ChequeDetailsClass cd = getItem(position);
        holder.tv_srno.setText(String.valueOf(cd.getSrNo()));
        holder.tv_cqdate.setText(cd.getChq_det_date());
        holder.ed_cqno.setText(cd.getChq_det_number());
        holder.ed_amnt.setText(cd.getChq_det_amt());

        String completePath = Environment.getExternalStorageDirectory() + File.separator + Constant.folder_name + "/" + cd.getChq_det_image();
        File file = new File(completePath);
        Uri imageUri = Uri.fromFile(file);
        Glide.with(context).load(imageUri)
                .thumbnail(0.5f)
                .crossFade()
                .placeholder(R.drawable.ic_party_mode_black_24dp)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.img_chq);

        return v;
    }

    @Override
    public int getViewTypeCount() {
        return getCount();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void returnDate(String dt) {
        ChequeDetailsClass chq = cheque_list.get(dtPos);
        chq.setChq_det_date(dt);
        cheque_list.set(dtPos,chq);
        notifyDataSetChanged();
    }

    @Override
    public void returnImage(String img) {
        ChequeDetailsClass chq = cheque_list.get(dtPos);
        chq.setChq_det_image(img);
        cheque_list.set(dtPos,chq);
        notifyDataSetChanged();
    }

    private class ViewHolder {
        private TextView tv_srno, tv_cqdate, tv_img;
        private EditText ed_cqno, ed_amnt;
        private ImageView img_chq;
    }

    private class GenericTextWatcher implements TextWatcher {

        private View view;
        private int pos;

        private GenericTextWatcher(View _view, int _pos){
            this.view = _view;
            this.pos = _pos;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            String str = editable.toString();
            switch (view.getId()){
                case R.id.ed_cqno:
                    ChequeDetailsClass cheque = cheque_list.get(pos);
                    cheque.setChq_det_number(str);
                    cheque_list.set(pos,cheque);
                    Constant.showLog(str);
                    ChequeDetailsActivityChanged.chequeNo = str;
                    /*if(pos==0){
                        for(int i=0;i<cheque_list.size();i++){
                            ChequeDetailsClass cheque = cheque_list.get(i);
                            if(i==0){
                                cheque.setChq_det_number(str);
                                cheque_list.set(i,cheque);
                            }else{
                                if(!str.equals("")) {
                                    int _str = Integer.parseInt(str);
                                    _str = _str + 1;
                                    str = String.valueOf(_str);
                                    cheque.setChq_det_number(String.valueOf(_str));
                                    cheque_list.set(i,cheque);
                                }
                            }
                        }
                        notifyDataSetChanged();
                    }else{
                        ChequeDetailsClass cheque = cheque_list.get(pos);
                        cheque.setChq_det_number(str);
                        cheque_list.set(pos,cheque);
                    }
                    Constant.showLog(str);
                    ChequeDetailsActivityChanged.chequeNo = str;*/
                    break;
                case R.id.ed_amnt:
                    ChequeDetailsClass cheque1 = cheque_list.get(pos);
                    if(str.equals("")) {
                       str = "0";
                    }
                    cheque1.setChq_det_amt(str);
                    cheque_list.set(pos,cheque1);
                    int amnt = 0;
                    for(int i=0;i<cheque_list.size();i++) {
                        ChequeDetailsClass cheque2 = cheque_list.get(i);
                        amnt = amnt + Integer.parseInt(cheque2.getChq_det_amt());
                    }
                    testInterface.onAmountChange(amnt);
                    Constant.showLog(str);
                    break;
            }
        }
    }

}
