package com.lnbinfotech.msplfootwear.adapters;

//Created by Anup on 10/17/2018.

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.lnbinfotech.msplfootwear.ImageWiseAddToCartActivity;
import com.lnbinfotech.msplfootwear.R;
import com.lnbinfotech.msplfootwear.constant.Constant;
import com.lnbinfotech.msplfootwear.interfaces.RecyclerViewToActivityInterface;
import com.lnbinfotech.msplfootwear.model.ImagewiseAddToCartClass;

import java.util.ArrayList;
import java.util.List;

public class ImageWiseAddToCartRecyclerAdapter extends RecyclerView.Adapter<ImageWiseAddToCartRecyclerAdapter.ProductViewHolder> {

    private Context context;
    private List<ImagewiseAddToCartClass> prodList;
    private RecyclerViewToActivityInterface listener;
    private int flag = 0, pos = -1;
    private List<String> whiteHashCodeList = new ArrayList<>();

    public ImageWiseAddToCartRecyclerAdapter(Context _context, List<ImagewiseAddToCartClass> _prodList) {
        this.context = _context;
        this.prodList = _prodList;
        addToList();
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_imagewise_addtocart, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        ImagewiseAddToCartClass prod = prodList.get(position);
        holder.textView.setText(prod.getColour());
        holder.textView.setTag(position);
        String hashCode = prod.getHashCode();
        if (whiteHashCodeList.contains(hashCode)) {
            holder.textView.setTextColor(Color.parseColor("#000000"));
            holder.lay.setBackgroundResource(R.drawable.green_boarder_draw);
        } else {
            holder.textView.setTextColor(Color.parseColor("#FFFFFF"));
            holder.lay.setBackgroundColor(Color.parseColor(hashCode));
        }

        String imageName = "NA";
        imageName = prod.getImageName();
        String arr[] = imageName.split("\\,");
        if (arr.length > 1) {
            imageName = arr[0];
        }
        imageName = imageName.replace(" ", "%20");
        imageName = Constant.imgUrl + imageName + ".jpg";
        Constant.showLog(imageName);
        Glide.with(context).load(imageName)
                .thumbnail(0.5f)
                .placeholder(R.drawable.placehoder)
                .error(R.drawable.placehoder)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.img);

        if(!ImageWiseAddToCartActivity.map.isEmpty()){
            if(ImageWiseAddToCartActivity.map.containsKey(position)){
                int a = ImageWiseAddToCartActivity.map.get(position);
                if(a==1){
                    holder.selImg.setVisibility(View.VISIBLE);
                }else{
                    holder.selImg.setVisibility(View.INVISIBLE);
                }
            }else{
                holder.selImg.setVisibility(View.INVISIBLE);
            }
        }else{
            holder.selImg.setVisibility(View.INVISIBLE);
        }

        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flag = 1;
                pos = (int) view.getTag();
                if (ImageWiseAddToCartActivity.map.isEmpty()) {
                    ImageWiseAddToCartActivity.map.put(pos, 1);
                } else {
                    if (ImageWiseAddToCartActivity.map.containsKey(pos)) {
                        if (ImageWiseAddToCartActivity.map.get(pos) == 0) {
                            ImageWiseAddToCartActivity.map.put(pos, 1);
                        } else {
                            ImageWiseAddToCartActivity.map.put(pos, 0);
                        }
                    } else {
                        ImageWiseAddToCartActivity.map.put(pos, 1);
                    }
                }
                notifyDataSetChanged();

            }
        });

        Constant.showLog("POS - "+position);
        if(pos==position){
            if(flag==1){
                listener.onItemClick("A");
                flag = 0;
            }
        }
    }

    @Override
    public int getItemCount() {
        return prodList.size();
    }

    public void setOnClickListener1(RecyclerViewToActivityInterface _listener) {
        this.listener = _listener;
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView img,selImg;
        private TextView textView;
        private LinearLayout lay;

        public ProductViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.textView);
            selImg = (ImageView) itemView.findViewById(R.id.selImg);
            lay = (LinearLayout) itemView.findViewById(R.id.lay);
            img = (ImageView) itemView.findViewById(R.id.img);
            img.setOnClickListener(this);
            lay.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int value = getAdapterPosition();
            ImagewiseAddToCartClass prod = prodList.get(value);
            if(view.getId()==R.id.img) {
                listener.onImageClick(prod);
            }else {
                listener.onItemClick("A");
            }
            notifyDataSetChanged();
        }
    }

    private void addToList() {
        whiteHashCodeList.clear();
        whiteHashCodeList.add("#FFFFF0");
        whiteHashCodeList.add("#FFFFF1");
        whiteHashCodeList.add("#FFFFF2");
        whiteHashCodeList.add("#FFFFF3");
        whiteHashCodeList.add("#FFFFF4");
        whiteHashCodeList.add("#FFFFF5");
        whiteHashCodeList.add("#FFFFF6");
        whiteHashCodeList.add("#FFFFF7");
        whiteHashCodeList.add("#FFFFF8");
        whiteHashCodeList.add("#FFFFF9");
        whiteHashCodeList.add("#FFFFFA");
        whiteHashCodeList.add("#FFFFFF");
    }
}
