package com.pait.dispatch_app.adapters;

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
import com.pait.dispatch_app.R;
import com.pait.dispatch_app.constant.Constant;
import com.pait.dispatch_app.interfaces.TestInterface;
import com.pait.dispatch_app.interfaces.TestInterface1;
import com.pait.dispatch_app.model.ChequeDetailsClass;
import com.pait.dispatch_app.model.DispatchDetailClass;

import java.io.File;
import java.util.List;

//Created by ANUP on 3/27/2018.

public class DispatchDetailAdapter extends BaseAdapter
        implements TestInterface1 {

    private Context context;
    private List<DispatchDetailClass> dDlist;
    private int dtPos;
    private TestInterface testInterface;

    public DispatchDetailAdapter(Context _context, List<DispatchDetailClass> _dDlist) {
        this.context = _context;
        this.dDlist = _dDlist;
    }

    public void initInterface(TestInterface _testInterface){
        this.testInterface = _testInterface;
    }

    @Override
    public int getCount() {
        return dDlist.size();
    }

    @Override
    public DispatchDetailClass getItem(int position) {
        return dDlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup viewGroup) {
        View v = convertView;
        final ViewHolder holder;

        DispatchDetailClass cd1 = getItem(position);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            if(cd1.getCBL().equals("C")) {
                v = inflater.inflate(R.layout.list_item_cheque_detail_changed, null);
            } else if(cd1.getCBL().equals("L")) {
                v = inflater.inflate(R.layout.list_item_cheque_detail_changed_l, null);
            } else {
                v = inflater.inflate(R.layout.list_item_cheque_detail_changed_b, null);
            }
            holder = new ViewHolder();
            holder.tv_srno = v.findViewById(R.id.tv_srno);
            holder.tv_cbl = v.findViewById(R.id.tv_cbl);
            holder.ed_noOfCarton = v.findViewById(R.id.ed_noOfCarton);
            holder.img_carton = v.findViewById(R.id.img_carton);

            holder.ed_noOfCarton.addTextChangedListener(new GenericTextWatcher(holder.ed_noOfCarton,position));
            
            holder.img_carton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dtPos = position;
                    DispatchDetailClass cd = getItem(position);
                    testInterface.onPauseFragment(cd.getCBL(),"",context);
                }
            });
            v.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        DispatchDetailClass cd = getItem(position);
        holder.tv_srno.setText(String.valueOf(cd.getSrNo()));
        holder.tv_cbl.setText(cd.getCBL());
        holder.ed_noOfCarton.setText(cd.getNoOfCartons());

        String completePath = Environment.getExternalStorageDirectory() + File.separator +
                Constant.folder_name + File.separator + Constant.image_folder + File.separator + cd.getImgName();
        File file = new File(completePath);
        Uri imageUri = Uri.fromFile(file);
        Glide.with(context).load(imageUri)
                .thumbnail(0.5f)
                .crossFade()
                .placeholder(R.drawable.ic_party_mode_black_24dp)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.img_carton);

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
    }

    @Override
    public void returnImage(String img) {
        DispatchDetailClass chq = dDlist.get(dtPos);
        chq.setImgName(img);
        dDlist.set(dtPos,chq);
        notifyDataSetChanged();
    }

    private class ViewHolder {
        private TextView tv_srno, tv_cbl;
        private EditText ed_noOfCarton;
        private ImageView img_carton;
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
                case R.id.ed_noOfCarton:
                    DispatchDetailClass cheque1 = dDlist.get(pos);
                    if(str.equals("")) {
                       str = "0";
                    }
                    cheque1.setNoOfCartons(str);
                    dDlist.set(pos,cheque1);
                    int amnt = 0;
                    for(int i=0;i<dDlist.size();i++) {
                        DispatchDetailClass cheque2 = dDlist.get(i);
                        amnt = amnt + Integer.parseInt(cheque2.getNoOfCartons());
                    }
                    testInterface.onAmountChange(amnt);
                    Constant.showLog(str);
                    break;
            }
        }
    }

}
