package com.lnbinfotech.msplfootwearex;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;

import com.lnbinfotech.msplfootwearex.constant.Constant;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AttachIdProofImageActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    private Spinner spinner_idproof;
    private String[] id_proof = {"Aadhaar","Pan card","Driving license","Election card"};
    private ArrayAdapter<String> adapter_id;
    private ImageView imageView_idproof;
    private Bitmap bmp;
    private Button bt_next;
    static int flag = 4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attach_id_proof_image);
        init();
    }

    private void init(){
        imageView_idproof = (ImageView) findViewById(R.id.imageView_idproof);
        bt_next = (Button) findViewById(R.id.btn_next);

        spinner_idproof = (Spinner) findViewById(R.id.spinner_idproof);
        adapter_id = new ArrayAdapter<>(this,R.layout.id_list,id_proof);
        spinner_idproof.setAdapter(adapter_id);

        if(flag == 0){

            set_value_attachIdProof();
            set_value_attachIdProofImage();

            spinner_idproof.setOnItemSelectedListener(this);

            imageView_idproof.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    showPopup(0);

                }
            });

            bt_next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(AttachIdProofImageActivity.this,NewCustomerEntryDetailFormActivity.class);
                    startActivity(i);
                    finish();
                }
            });

        }else {

            spinner_idproof.setOnItemSelectedListener(this);

            imageView_idproof.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    showPopup(0);

                }
            });

            bt_next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(AttachIdProofImageActivity.this, AttachGSTnoPANnoImageActivity.class);
                    startActivity(i);
                    finish();
                }
            });
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String item_id = spinner_idproof.getSelectedItem().toString();
        int position = spinner_idproof.getSelectedItemPosition();
        Log.d("Log","positon:"+spinner_idproof.getSelectedItemPosition());
        OptionsActivity.new_cus.setId_proof(String.valueOf(position));
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                    Constant.checkFolder(Constant.captured_images_folder);
                    String dateformat = currentDateFormat();
                    String file_name = "img_" + dateformat + ".jpg";
                    store_CameraPhoto_InSdCard(bitmap, dateformat);
                    Bitmap mbitmap = get_Image_from_sd_card(file_name);
                    Log.d("Log", "imgename:" + mbitmap);
                    imageView_idproof.setImageBitmap(mbitmap);
                    OptionsActivity.new_cus.setId_proof_image(file_name);

                }
                break;
            case 2:
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

                    //imgv_img1.setBackgroundResource(0);

                    imageView_idproof.setImageBitmap(bmp);

                } else {
                    Log.d("Status:", "Photopicker canceled");
                }
                break;


        }
    }

    private void store_CameraPhoto_InSdCard(Bitmap bitmap,String currentdate){
        File file = new File(Environment.getExternalStorageDirectory() + File.separator + Constant.captured_images_folder+File.separator + "img_"+currentdate+".jpg");
        //File file = new File(Environment.getExternalStorageDirectory() + "img_"+currentdate+".jpeg");

        Log.d("Log","File path:"+file);
        try{

            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 15, fos);
            fos.flush();
            fos.close();
        }catch (FileNotFoundException f){
            f.printStackTrace();
        }catch (IOException io){
            io.printStackTrace();
        }catch (NullPointerException w){
            w.printStackTrace();
        }
    }



    private Bitmap get_Image_from_sd_card(String filename){
        Bitmap bitmap = null;
        File imgfile = new File(Environment.getExternalStorageDirectory() + File.separator + Constant.captured_images_folder+File.separator + filename);


        try {
            FileInputStream fis = new FileInputStream(imgfile);
            bitmap  = BitmapFactory.decodeStream(fis);
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }
        return bitmap;
    }

    private String currentDateFormat(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HH_mm");
        String current_time = sdf.format(new Date());
        return current_time;
    }

    private void set_value_attachIdProof(){

        String address_proof = OptionsActivity.new_cus.getId_proof();
        int  position = Integer.parseInt(address_proof);
        Log.d("Log","val: "+position);
        spinner_idproof.setSelection(position);
    }

    private void set_value_attachIdProofImage(){

        String filename = OptionsActivity.new_cus.getId_proof_image();
        Log.d("Log","filename: "+OptionsActivity.new_cus.getId_proof_image());


        File file = Constant.checkFolder(Constant.captured_images_folder);
        File fileArray[] = file.listFiles();

        if (fileArray.length != 0) {
            for (File f : fileArray) {
                if (f.getName().equals(filename)) {
                    if (f.length() != 0) {
                        //String _imagePath = getRealPathFromURI(Environment.getExternalStorageDirectory() + File.separator + Constant.folder_name + File.separator + filename);
                        String _imagePath = getRealPathFromURI( Environment.getExternalStorageDirectory() + File.separator + Constant.captured_images_folder+File.separator + filename);
                        imageView_idproof.setImageBitmap(scaleBitmap(_imagePath));
                    }
                    break;
                }
            }
        }
    }

    private String getRealPathFromURI(String contentURI) {
        Uri contentUri = Uri.parse(contentURI);
        Cursor cursor = getContentResolver().query(contentUri, null, null, null, null);
        if (cursor == null) {
            return contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            String s = cursor.getString(idx);
            cursor.close();
            return s;
        }
    }

    public Bitmap scaleBitmap(String imagePath) {
        Bitmap resizedBitmap = null;
        try {
            int inWidth, inHeight;
            InputStream in;
            in = new FileInputStream(imagePath);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(in, null, options);
            in.close();
            //in = null;
            inWidth = options.outWidth;
            inHeight = options.outHeight;
            in = new FileInputStream(imagePath);
            options = new BitmapFactory.Options();
            options.inSampleSize = Math.max(inWidth / 300, inHeight / 300);
            Bitmap roughBitmap = BitmapFactory.decodeStream(in, null, options);

            Matrix m = new Matrix();
            RectF inRect = new RectF(0, 0, roughBitmap.getWidth(), roughBitmap.getHeight());
            RectF outRect = new RectF(0, 0, roughBitmap.getWidth(), roughBitmap.getHeight());
            m.setRectToRect(inRect, outRect, Matrix.ScaleToFit.CENTER);
            float[] values = new float[9];
            m.getValues(values);
            resizedBitmap = Bitmap.createScaledBitmap(roughBitmap, (int) (roughBitmap.getWidth() * values[0]), (int) (roughBitmap.getHeight() * values[4]), true);
        } catch (Exception e) {
            e.printStackTrace();
            //pb.setVisibility(View.GONE);
            //img.setImageResource(R.drawable.bg);
            // toast.show();
        }
        return resizedBitmap;
    }

    private void showPopup(int id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you want to attach image?");
        if (id == 0) {
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    showPopup(1);
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
        }else if (id == 1) {
            builder.setMessage("Attach image");
            builder.setPositiveButton("Camera", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent1 = new Intent( MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent1,1);

                }
            });
            builder.setNegativeButton("Gallery", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent2 = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent2,2);
                }
            });
        }
        builder.create().show();
    }


}


