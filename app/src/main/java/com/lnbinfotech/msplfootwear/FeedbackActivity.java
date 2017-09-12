package com.lnbinfotech.msplfootwear;

import android.app.Dialog;
import android.app.Notification;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import java.io.File;
import java.net.URI;

public class FeedbackActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Spinner fedback_spinner;
    ArrayAdapter<String> feedbk_type;
    String[] arr = {"Invoice","Order","Packing","Damage Goods","Service","Team","Others"};
    EditText ed_description;
    AppCompatButton bt_camera,bt_send;
    ImageView imgv_img1,imgv_img2,imgv_img3;
    LinearLayout packing_order_inv_lay,damaged_goods_lay,service_or_team_lay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        init();
    }
    void init(){
        fedback_spinner = (Spinner) findViewById(R.id.fedback_spinner);
        ed_description = (EditText) findViewById(R.id.ed_description);
        bt_camera = (AppCompatButton) findViewById(R.id.bt_camera);
        bt_send = (AppCompatButton) findViewById(R.id.bt_send);

        imgv_img1 = (ImageView) findViewById(R.id.imgv_img1);
        imgv_img2 = (ImageView) findViewById(R.id.imgv_img2);
        imgv_img3 = (ImageView) findViewById(R.id.imgv_img3);

        packing_order_inv_lay = (LinearLayout) findViewById(R.id.packing_order_inv_lay);
        damaged_goods_lay = (LinearLayout) findViewById(R.id.damaged_goods_lay);
        service_or_team_lay = (LinearLayout) findViewById(R.id.service_or_team_lay);

        feedbk_type = new ArrayAdapter<String>(this,R.layout.feedbk_type_list,arr);
        fedback_spinner.setAdapter(feedbk_type);
        fedback_spinner.setOnItemSelectedListener(this);

        bt_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             show_popup(0);
            }
        });

        bt_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        String item = fedback_spinner.getSelectedItem().toString();
        fedback_spinner.setSelection(position);

        if (item.equals("Packing") | item.equals("Order") | item.equals("Invoice")){
            packing_order_inv_lay.setVisibility(View.VISIBLE);
            damaged_goods_lay.setVisibility(View.GONE);
            service_or_team_lay.setVisibility(View.GONE);
        }else if(item.equals("Damage Goods")){
            packing_order_inv_lay.setVisibility(View.GONE);
            damaged_goods_lay.setVisibility(View.VISIBLE);
            service_or_team_lay.setVisibility(View.GONE);
        }else if (item.equals("Service") | item.equals("Team")){
            packing_order_inv_lay.setVisibility(View.GONE);
            damaged_goods_lay.setVisibility(View.GONE);
            service_or_team_lay.setVisibility(View.VISIBLE);
        }else if(item.equals("Others")){
            packing_order_inv_lay.setVisibility(View.GONE);
            damaged_goods_lay.setVisibility(View.GONE);
            service_or_team_lay.setVisibility(View.GONE);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public  void show_popup(int id){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if(id == 0) {
            builder.setMessage("Do you want to attach image?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File file = new File(Environment.getExternalStorageDirectory().getPath());
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                    startActivityForResult(intent,1);
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    dialogInterface.dismiss();
                }
            });
        }
        builder.create().show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //if (requestCode == this.requestCode, )
    }
}
