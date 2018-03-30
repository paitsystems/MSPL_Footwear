package com.lnbinfotech.msplfootwearex;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.lnbinfotech.msplfootwearex.constant.Constant;
import com.lnbinfotech.msplfootwearex.interfaces.ServerCallback;
import com.lnbinfotech.msplfootwearex.log.WriteLog;
import com.lnbinfotech.msplfootwearex.model.FeedbackClass;
import com.lnbinfotech.msplfootwearex.volleyrequests.VolleyRequests;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class FeedbackActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    private Spinner fedback_spinner;
    //private String imagePath;
    private ArrayAdapter<String> feedbk_type;
    private String[] arr = {"Invoice", "Order", "Packing", "Damage Goods", "Service", "Team", "Others"};
    private EditText ed_description, ed_article_no, ed_qty, ed_salesman, ed_front_office;
    private AutoCompleteTextView auto_invoice_no;
    private AppCompatButton bt_send;
    private ImageView imgv_img1, imgv_img2, imgv_img3;
    private LinearLayout packing_order_inv_lay, lay_img1, lay_img2, lay_img3;
    private CardView damaged_goods_cardlay, service_or_team_cardlay;
    private final int requestCode = 21;
    private ByteArrayOutputStream byteArray;
    private Bitmap bmp;
    private int flag;
    private FeedbackClass feedbackClass;
    private Constant constant;
    private Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(Constant.liveTestFlag==1) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }

        setContentView(R.layout.activity_feedback);

        init();

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        //lay_img1.setOnClickListener(this);
        //lay_img2.setOnClickListener(this);
        // lay_img3.setOnClickListener(this);
        imgv_img1.setOnClickListener(this);
        imgv_img2.setOnClickListener(this);
        imgv_img3.setOnClickListener(this);

        bt_send.setOnClickListener(this);
        fedback_spinner.setOnItemSelectedListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgv_img1:
                //flag = 0;
                show_popup(0);
                break;
            case R.id.imgv_img2:
                //flag = 1;
                show_popup(1);
                break;
            case R.id.imgv_img3:
                //flag = 2;
                show_popup(2);
                break;
            case R.id.bt_send:
                setValue();

                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        String item = fedback_spinner.getSelectedItem().toString();
        Constant.showLog(item);
        feedbackClass.setFeedbk_type(item);
        fedback_spinner.setSelection(position);

        if (item.equals("Packing") | item.equals("Order") | item.equals("Invoice")) {
            packing_order_inv_lay.setVisibility(View.VISIBLE);
            damaged_goods_cardlay.setVisibility(View.GONE);
            service_or_team_cardlay.setVisibility(View.GONE);
        } else if (item.equals("Damage Goods")) {
            packing_order_inv_lay.setVisibility(View.GONE);
            damaged_goods_cardlay.setVisibility(View.VISIBLE);
            service_or_team_cardlay.setVisibility(View.GONE);
        } else if (item.equals("Service") | item.equals("Team")) {
            packing_order_inv_lay.setVisibility(View.GONE);
            damaged_goods_cardlay.setVisibility(View.GONE);
            service_or_team_cardlay.setVisibility(View.VISIBLE);
        } else if (item.equals("Others")) {
            packing_order_inv_lay.setVisibility(View.GONE);
            damaged_goods_cardlay.setVisibility(View.GONE);
            service_or_team_cardlay.setVisibility(View.GONE);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onBackPressed() {
        //showDia(0);
        new Constant(FeedbackActivity.this).doFinish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //showDia(0);
                new Constant(FeedbackActivity.this).doFinish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                    Constant.checkFolder(Constant.image_folder);
                    String dateformat = currentDateFormat();
                    String file_name = "img_" + dateformat + ".jpg";
                    store_CameraPhoto_InSdCard(bitmap, dateformat);
                    Bitmap mbitmap = get_Image_from_sd_card(file_name);
                    imgv_img1.setImageBitmap(mbitmap);
                    Log.d("Log", "imgename:" + mbitmap);
                    //imgv_img2.setVisibility(View.VISIBLE);
                    lay_img2.setVisibility(View.VISIBLE);
                    feedbackClass.setFeed_img1(file_name);
                    Constant.showLog("file_name"+file_name);
                    Constant.showLog(""+mbitmap);
                    // feedbackClass.setFeed_img1(String.valueOf(mbitmap));
                }
                break;
            case 2:
                if (resultCode == RESULT_OK) {
                    Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                    Constant.checkFolder(Constant.image_folder);
                    String dateformat = currentDateFormat();
                    String file_name = "img_" + dateformat + ".jpg";
                    store_CameraPhoto_InSdCard(bitmap, dateformat);
                    Bitmap mbitmap = get_Image_from_sd_card(file_name);
                    // imgv_img3.setVisibility(View.VISIBLE);
                    imgv_img2.setImageBitmap(mbitmap);
                    lay_img3.setVisibility(View.VISIBLE);
                    feedbackClass.setFeed_img2(file_name);
                    Constant.showLog("file_name"+file_name);
                    //feedbackClass.setFeed_img2(String.valueOf(mbitmap));
                    Log.d("Log", "imgename:" + mbitmap);
                }
                break;

            case 3:
                if (resultCode == RESULT_OK) {
                    Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                    Constant.checkFolder(Constant.image_folder);
                    String dateformat = currentDateFormat();
                    String file_name = "img_" + dateformat + ".jpg";
                    store_CameraPhoto_InSdCard(bitmap, dateformat);
                    Bitmap mbitmap = get_Image_from_sd_card(file_name);
                    imgv_img3.setImageBitmap(mbitmap);
                    Log.d("Log", "imgename:" + mbitmap);
                    feedbackClass.setFeed_img3(file_name);
                    Constant.showLog("file_name"+file_name);
                    //feedbackClass.setFeed_img3(String.valueOf(mbitmap));
                }
                break;

            case 4:
                if (data != null && resultCode == RESULT_OK) {
                   /* Uri selectedImage = data.getData();

                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String filePath = cursor.getString(columnIndex);
                    cursor.close();

                    if (bmp != null && !bmp.isRecycled()) {
                        bmp = null;
                    }
                    Constant.showLog("gallery imgpath:"+filePath);
                    Constant.showLog("gallery bmp:"+bmp);
                    bmp = BitmapFactory.decodeFile(filePath);

                    //imgv_img1.setBackgroundResource(0);
                    imgv_img1.setImageBitmap(bmp);
                    imgv_img2.setVisibility(View.VISIBLE);
                    feedbackClass.setFeed_img1(String.valueOf(bmp));
                } else {
                    writeLog("Statusimg1:Photopicker canceled");
                }*/
                    try {
                        String dateformat = currentDateFormat();
                        String file_name = "feedbkimg_" + dateformat + ".jpg";
                        File destFile = new File((Environment.getExternalStorageDirectory() + File.separator + Constant.image_folder + File.separator + file_name));
                        copyFile(new File(getPath(data.getData())), destFile);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                break;
            case 5:
                if (data != null && resultCode == RESULT_OK) {

                    Uri selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String filePath = cursor.getString(columnIndex);
                    cursor.close();

                    if (bmp != null && !bmp.isRecycled()) {
                        bmp = null;
                    }
                    bmp = BitmapFactory.decodeFile(filePath);

                    // imgv_img2.setBackgroundResource(0);
                    imgv_img2.setImageBitmap(bmp);
                    imgv_img3.setVisibility(View.VISIBLE);
                    feedbackClass.setFeed_img2(String.valueOf(bmp));
                } else {
                    writeLog("Statusimg2:Photopicker canceled");
                }
                break;
            case 6:
                if (data != null && resultCode == RESULT_OK) {
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String filePath = cursor.getString(columnIndex);
                    cursor.close();

                    if (bmp != null && !bmp.isRecycled()) {
                        bmp = null;
                    }
                    bmp = BitmapFactory.decodeFile(filePath);//imgv_img3.setBackgroundResource(0);
                    imgv_img3.setImageBitmap(bmp);
                    feedbackClass.setFeed_img3(String.valueOf(bmp));
                } else {
                    writeLog("Statusimg3:Photopicker canceled");
                }
                break;
        }
         /*File file = new File(Environment.getExternalStorageDirectory() + File.separator + Constant.image_folder+File.separator + "img_"+currentDateFormat()+".png");
            Log.d("Log","File path:"+file);
            try {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArray);
                //file.createNewFile();
                FileOutputStream fos = new FileOutputStream(file);
                fos.write(byteArray.toByteArray());
                fos.close();
            }catch (NullPointerException e){
                e.printStackTrace();
            }catch (IOException i){
                i.printStackTrace();
            }*/
    }

    private void init() {
        toast = Toast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        feedbackClass = new FeedbackClass();
        feedbackClass.setFeed_img1("NA");
        feedbackClass.setFeed_img2("NA");
        feedbackClass.setFeed_img3("NA");
        fedback_spinner = (Spinner) findViewById(R.id.fedback_spinner);
        ed_description = (EditText) findViewById(R.id.ed_description);
        ed_article_no = (EditText) findViewById(R.id.ed_article_no);
        ed_qty = (EditText) findViewById(R.id.ed_qty);
        ed_salesman = (EditText) findViewById(R.id.ed_salesman);
        ed_front_office = (EditText) findViewById(R.id.ed_frontoffice);
        auto_invoice_no = (AutoCompleteTextView) findViewById(R.id.auto_invoice_no);
        bt_send = (AppCompatButton) findViewById(R.id.bt_send);
        imgv_img1 = (ImageView) findViewById(R.id.imgv_img1);
        imgv_img2 = (ImageView) findViewById(R.id.imgv_img2);
        imgv_img3 = (ImageView) findViewById(R.id.imgv_img3);
        packing_order_inv_lay = (LinearLayout) findViewById(R.id.packing_order_inv_lay);
        lay_img1 = (LinearLayout) findViewById(R.id.lay_img1);
        lay_img2 = (LinearLayout) findViewById(R.id.lay_img2);
        lay_img3 = (LinearLayout) findViewById(R.id.lay_img3);
        damaged_goods_cardlay = (CardView) findViewById(R.id.damaged_goods_cardlay);
        service_or_team_cardlay = (CardView) findViewById(R.id.service_or_team_cardlay);
        feedbk_type = new ArrayAdapter<>(this, R.layout.feedbk_type_list, arr);
        fedback_spinner.setAdapter(feedbk_type);
    }

    private void setValue() {
        String article_no = ed_article_no.getText().toString();
        String invoice_no = auto_invoice_no.getText().toString();
        String qty = ed_qty.getText().toString();
        String salesman = ed_salesman.getText().toString();
        String front_office = ed_front_office.getText().toString();
        String description = ed_description.getText().toString();
        feedbackClass.setArticle_no(article_no);
        feedbackClass.setInvoice_no(invoice_no);
        feedbackClass.setQty(qty);
        feedbackClass.setSalesman_id(salesman);
        feedbackClass.setFront_office(front_office);
        feedbackClass.setDescription(description);
        if(feedbackClass.getFeed_img1().equals("")){
            feedbackClass.setFeed_img1("NA");
        }

        if(feedbackClass.getFeed_img2().equals("")){
            feedbackClass.setFeed_img2("NA");
        }
        if(feedbackClass.getFeed_img3().equals("")){
            feedbackClass.setFeed_img3("NA");
        }
        saveFeedbackdetail();

    }

    private void show_popup(int id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        if (id == 0) {
            builder.setMessage("Do you want to attach image?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    //show_popup(3);
                    Intent intent1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent1, 1);
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
        } else if (id == 1) {
            builder.setMessage("Do you want to attach image?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    // show_popup(4);
                    Intent intent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent2, 2);
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
        } else if (id == 2) {
            builder.setMessage("Do you want to attach image?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    //show_popup(5);
                    Intent intent3 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent3, 3);
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
        } else if (id == 3) {
            builder.setMessage("Attach image:");
            builder.setPositiveButton("Camera", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent1, 1);
                }
            });
            builder.setNegativeButton("Gallery", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent4 = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent4, 4);
                }
            });
        } else if (id == 4) {
            builder.setMessage("Attach image:");
            builder.setPositiveButton("Camera", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent2, 2);
                }
            });
            builder.setNegativeButton("Gallery", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent5 = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent5, 5);
                }
            });
        } else if (id == 5) {
            builder.setMessage("Attach image:");
            builder.setPositiveButton("Camera", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent3 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent3, 3);
                }
            });
            builder.setNegativeButton("Gallery", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent6 = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent6, 6);
                }
            });
        } else if (id == 6) {
            builder.setMessage("You can attach only three images do you want to delete it?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    imgv_img3.setImageBitmap(null);

                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
        } else if (id == 7) {
            builder.setMessage("Feedback Saved Successfully");
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent1 = new Intent("test");//UploadImageService.BROADCAST
                    sendBroadcast(intent1);
                    writeLog("UploadImageService_onHandleIntent_broadcastSend");
                    new Constant(FeedbackActivity.this).doFinish();
                }
            });
        } else if (id == 8) {
            builder.setMessage("Error While Saving Data");
            builder.setPositiveButton("Try Again", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    saveFeedbackdetail();
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
        }
        builder.create().show();
    }


    private String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        startManagingCursor(cursor);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    private void copyFile(File sourceFile, File destFile) throws IOException {
        if (!sourceFile.exists()) {
            return;
        }
        FileChannel source = null;
        FileChannel destination = null;
        source = new FileInputStream(sourceFile).getChannel();
        destination = new FileOutputStream(destFile).getChannel();
        if (destination != null && source != null) {
            destination.transferFrom(source, 0, source.size());
        }
        if (source != null) {
            source.close();
        }
        if (destination != null) {
            destination.close();
        }
    }

    private void store_CameraPhoto_InSdCard(Bitmap bitmap, String currentdate) {
        File file = new File(Environment.getExternalStorageDirectory() + File.separator + Constant.image_folder + File.separator + "img_" + currentdate + ".jpg");
        Log.d("Log", "File path:" + file);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 15, fos);
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Bitmap get_Image_from_sd_card(String filename) {
        Bitmap bitmap = null;
        File imgfile = new File(Environment.getExternalStorageDirectory() + File.separator + Constant.image_folder + File.separator + filename);
        try {
            FileInputStream fis = new FileInputStream(imgfile);
            bitmap = BitmapFactory.decodeStream(fis);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    private String currentDateFormat() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd_MMM_yyyy_HH_mm_ss", Locale.ENGLISH);
        return sdf.format(new Date());
    }

    private void saveFeedbackdetail() {
        try {
            constant = new Constant(FeedbackActivity.this);
            String url = "";
            constant.showPD();
            String feedtype = feedbackClass.getFeedbk_type();
            String articleno = feedbackClass.getArticle_no();
            String invoiceno = feedbackClass.getInvoice_no();
            String qty = feedbackClass.getQty();
            String salesmanid = feedbackClass.getSalesman_id();
            String officetype = feedbackClass.getFront_office();
            String description = feedbackClass.getDescription();
            String img1 = feedbackClass.getFeed_img1();


          /*  if( feedbackClass.getFeed_img1().equals("null")){
                feedbackClass.setFeed_img1("NA");

            }*/
            String img2 = feedbackClass.getFeed_img2();

          /*  if(img2.equals("null")){
                img2 = "NA";
            }*/
            String img3 = feedbackClass.getFeed_img3();

           /* if( img3.equals("null")){
                img3 = "NA";
            }*/
            String crby = String.valueOf(FirstActivity.pref.getInt(getString(R.string.pref_retailCustId),0));
            Constant.showLog("crby"+crby);
            //String crdate = feedbackClass.getCrdate();
            //String crtime = feedbackClass.getCrtime();
            // String usertype = feedbackClass.getUser_type();

           /* if(img1.equals("null") || img2.equals("null") || img3.equals("null")){
                img1 = "NA";
                img2 = "NA";
                img3 = "NA";
            }*/

            if(feedtype.equals("Damage Goods")){
                salesmanid = "0";
                officetype = "NA";
            }else if(feedtype.equals("Service") || feedtype.equals("Team")){
                articleno = "0";
                qty = "0";
                invoiceno = "0";
            }else{
                salesmanid = "0";
                officetype = "NA";
                articleno = "0";
                qty = "0";
                invoiceno = "0";
            }


            String _feedtype = URLEncoder.encode(feedtype, "UTF-8");
            String _articleno = URLEncoder.encode(articleno, "UTF-8");
            String _invoiceno = URLEncoder.encode(invoiceno, "UTF-8");
            String _qty = URLEncoder.encode(qty, "UTF-8");
            String _salesmanid = URLEncoder.encode(salesmanid, "UTF-8");
            String _officetype = URLEncoder.encode(officetype, "UTF-8");
            String _description = URLEncoder.encode(description, "UTF-8");
            String _img1 = URLEncoder.encode(img1, "UTF-8");
            String _img2 = URLEncoder.encode(img2, "UTF-8");
            String _img3 = URLEncoder.encode(img3, "UTF-8");
            String _crby = URLEncoder.encode(crby, "UTF-8");
            //String _crdate = URLEncoder.encode(crdate, "UTF-8");
            //String _crtime = URLEncoder.encode(crtime, "UTF-8");
            //String _usertype = URLEncoder.encode(usertype, "UTF-8");


            url = Constant.ipaddress +"/SaveFeedbackDetail?feedbk_type=" + _feedtype + "&article_no=" + _articleno + "&invoice_no=" + _invoiceno + "&qty=" + _qty + "&salesman_id=" + _salesmanid + "&office_type=" + _officetype + "&description=" + _description + "&img1=" + _img1 + "&img2=" + _img2 + "&img3=" + _img3 + "&crby="+_crby+"&user_type=E"; //+ _usertype;
            Constant.showLog(url);
            writeLog("savefeedback():url called:" + url);

            VolleyRequests requests = new VolleyRequests(FeedbackActivity.this);
            requests.saveFeedbackDetail(url, new ServerCallback() {
                @Override
                public void onSuccess(String result) {
                    //constant.showPD();
                    show_popup(7);
                    Constant.showLog("Volly request success");
                    writeLog("saveFeedbackdetail():Volley_success");
                }

                @Override
                public void onFailure(String result) {
                    //constant.showPD();
                    show_popup(8);
                    writeLog("saveFeedbackdetail_" + result);
                }
            });
        } catch (Exception e) {
            show_popup(7);
            e.printStackTrace();
            writeLog("saveFeedbackdetail_" + e.getMessage());
        }
    }

    private void writeLog(String _data) {
        new WriteLog().writeLog(getApplicationContext(), "FeedbackActivity_" + _data);
    }
}

