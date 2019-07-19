package com.lnbinfotech.msplfootwearex;

import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;

import com.lnbinfotech.msplfootwearex.constant.Constant;
import com.lnbinfotech.msplfootwearex.db.DBHandler;
import com.lnbinfotech.msplfootwearex.log.WriteLog;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static com.lnbinfotech.msplfootwearex.constant.Constant.currentDateFormat;

public class AttachGSTnoPANnoImageActivity extends AppCompatActivity implements View.OnClickListener {

    private RadioButton rdo_gst, rdo_pan;
    private LinearLayout gst_lay, pan_lay;
    private AppCompatButton bt_next, bt_update, bt_cancel;
    private LinearLayout save_lay, update_lay, lay_img, lay_img1;
    private ImageView imageView_pan_img, imageView_gst_img;
    private EditText ed_gstno, ed_panno;
    private String imagePath;
    private int _flag = 1;
    public static int radio_flag = 1;
    public static int flag = 5;
    private Toast toast;
    private final int requestCode = 1;
    private DBHandler db;
    private String value = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(Constant.liveTestFlag==1) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }

        setContentView(R.layout.activity_attach_gst_pan_image);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setTitle(R.string.newcustomerentry);
        }
        init();

        value = "VAT Certificate Copy";
        Constant.showLog("setIdValue():gstvalue:"+value);
        Cursor cursor =  db.getIdOfDocType(value);
        if(cursor.moveToFirst()){
            OptionsActivity.new_cus.setId_gstpan_proof(cursor.getString(cursor.getColumnIndex(DBHandler.Document_Id)));
        }
        Constant.showLog("Documentid:"+OptionsActivity.new_cus.getId_gstpan_proof());
        cursor.close();

        if (flag == 0) {
            save_lay.setVisibility(View.GONE);
            update_lay.setVisibility(View.VISIBLE);
            set_value_attachgstpan_no();
        } else {
            save_lay.setVisibility(View.VISIBLE);
            update_lay.setVisibility(View.GONE);
        }

        //setIdValue();

        bt_update.setOnClickListener(this);
        bt_cancel.setOnClickListener(this);

        rdo_gst.setOnClickListener(this);
        rdo_pan.setOnClickListener(this);

        imageView_pan_img.setOnClickListener(this);
        imageView_gst_img.setOnClickListener(this);
        bt_next.setOnClickListener(this);
    }

    private void init() {
        db = new DBHandler(AttachGSTnoPANnoImageActivity.this);
        toast = Toast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        rdo_gst = findViewById(R.id.rdo_gstno);
        rdo_pan = findViewById(R.id.rdo_panno);
        gst_lay = findViewById(R.id.gst_lay);
        pan_lay = findViewById(R.id.pan_lay);
        bt_next = findViewById(R.id.btn_next);
        ed_gstno = findViewById(R.id.ed_gstno);
        ed_panno = findViewById(R.id.ed_panno);
        bt_cancel = findViewById(R.id.btn_cancel);
        bt_update = findViewById(R.id.btn_update);
        save_lay = findViewById(R.id.save_lay);
        update_lay = findViewById(R.id.update_lay);
        lay_img = findViewById(R.id.lay_img);
        lay_img1 = findViewById(R.id.lay_img1);
        imageView_gst_img = findViewById(R.id.imageView_gst_img);
        imageView_pan_img = findViewById(R.id.imageView_pan_img);
        lay_img.setOnClickListener(this);
        lay_img1.setOnClickListener(this);
        FirstActivity.pref = getSharedPreferences(FirstActivity.PREF_NAME,MODE_PRIVATE);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_next:
                gstpan_no_value();
                setIdValue();
                String filename = "";
                if (_flag == 0) {
                    OptionsActivity.new_cus.setPan_no_image(imagePath);
                    OptionsActivity.new_cus.setGst_no_image("NA");
                    OptionsActivity.new_cus.setGstpan_img(imagePath);
                    filename = OptionsActivity.new_cus.getPan_no_image();
                } else if (_flag == 1) {
                    OptionsActivity.new_cus.setPan_no_image("NA");
                    OptionsActivity.new_cus.setGst_no_image(imagePath);
                    OptionsActivity.new_cus.setGstpan_img(imagePath);
                    filename = OptionsActivity.new_cus.getGst_no_image();
                }

                if (filename == null) {
                    toast.setText("Please, attach image.");
                    toast.show();
                } else {
                    Intent k = new Intent(AttachGSTnoPANnoImageActivity.this, NewCustomerEntryDetailFormActivity.class);
                    startActivity(k);
                    overridePendingTransition(R.anim.enter, R.anim.exit);
                    finish();
                }
                break;
            case R.id.btn_update:
                gstpan_no_value();
                setIdValue();
                if (_flag == 0) {
                    OptionsActivity.new_cus.setPan_no_image(imagePath);
                    OptionsActivity.new_cus.setGstpan_img(imagePath);
                    //OptionsActivity.new_cus.setGst_no_image(gst);
                    Log.d("Log","imagePath:"+imagePath);
                } else if (_flag == 1) {
                    OptionsActivity.new_cus.setGst_no_image(imagePath);
                    OptionsActivity.new_cus.setGstpan_img(imagePath);
                   // OptionsActivity.new_cus.setPan_no_image(pan);
                    Log.d("Log","imagePath:"+imagePath);
                }
                finish();
                Intent intent = new Intent(AttachGSTnoPANnoImageActivity.this, NewCustomerEntryDetailFormActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.enter, R.anim.exit);
                break;
            case R.id.btn_cancel:
                finish();
                Intent j = new Intent(AttachGSTnoPANnoImageActivity.this, NewCustomerEntryDetailFormActivity.class);
                startActivity(j);
                overridePendingTransition(R.anim.enter, R.anim.exit);
                break;
            case R.id.imageView_gst_img:
                /*Intent intent_ = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                File f = Constant.checkFolder(Constant.folder_name + File.separator + Constant.image_folder);
                f = new File(f.getAbsolutePath(),"temp.jpg");
                Uri photoURI = FileProvider.getUriForFile(getApplicationContext(), getApplicationContext().getPackageName()
                        + ".provider", f);
                intent_.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                intent_.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivityForResult(intent_,requestCode);
                overridePendingTransition(R.anim.enter, R.anim.exit);*/
                break;
            case R.id.imageView_pan_img:
                /*Intent in = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                File f1 = Constant.checkFolder(Constant.folder_name + File.separator + Constant.image_folder);
                f1 = new File(f1.getAbsolutePath(),"temp.jpg");
                Uri photoURI1 = FileProvider.getUriForFile(getApplicationContext(), getApplicationContext().getPackageName()
                        + ".provider", f1);
                in.putExtra(MediaStore.EXTRA_OUTPUT, photoURI1);
                in.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivityForResult(in,requestCode);
                overridePendingTransition(R.anim.enter, R.anim.exit);*/
                break;
            case R.id.rdo_gstno:
                radio_flag = 1;
                _flag = 1;
                rdo_pan.setChecked(false);
                gst_lay.setVisibility(View.VISIBLE);
                pan_lay.setVisibility(View.GONE);
                break;
            case R.id.rdo_panno:
                radio_flag = 2;
                _flag = 0;
                rdo_gst.setChecked(false);
                gst_lay.setVisibility(View.GONE);
                pan_lay.setVisibility(View.VISIBLE);
                break;
            case R.id.lay_img:
                showPopup(2);
                break;
            case R.id.lay_img1:
                showPopup(2);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        showPopup(1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                showPopup(1);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (this.requestCode == requestCode && resultCode == RESULT_OK) {
            try {
                String _imagePath = getRealPathFromURI(Environment.getExternalStorageDirectory() +
                        File.separator + Constant.folder_name + File.separator + Constant.image_folder + File.separator
                        + "temp.jpg");
                if (flag == 0) {
                    if (_flag == 1) {
                        imageView_gst_img.setVisibility(View.VISIBLE);
                        imageView_gst_img.setImageBitmap(scaleBitmap(_imagePath));
                    } else if (_flag == 0) {
                        imageView_pan_img.setVisibility(View.VISIBLE);
                        imageView_pan_img.setImageBitmap(scaleBitmap(_imagePath));
                    }
                } else {
                    if (_flag == 1) {
                        imageView_gst_img.setVisibility(View.VISIBLE);
                        imageView_gst_img.setImageBitmap(scaleBitmap(_imagePath));
                    } else if (_flag == 0) {
                        imageView_pan_img.setVisibility(View.VISIBLE);
                        imageView_pan_img.setImageBitmap(scaleBitmap(_imagePath));
                    }
                }

                imagePath = "C_GP_Img_" + currentDateFormat() + ".jpg";

                File f = new File(Environment.getExternalStorageDirectory() + File.separator +
                        Constant.folder_name + File.separator + Constant.image_folder);
                for (File temp : f.listFiles()) {
                    if (temp.getName().equals("temp.jpg")) {
                        f = temp;
                        break;
                    }
                }

                OutputStream outFile;
                Bitmap bitmap;
                BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(), bitmapOptions);
                File file = new File(Environment.getExternalStorageDirectory()
                        + File.separator + Constant.folder_name + File.separator + Constant.image_folder, imagePath);
                try {
                    outFile = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 15, outFile);
                    outFile.flush();
                    outFile.close();
                } catch (Exception e) {
                    writeLog("Exception1_" + e.getMessage());
                    e.printStackTrace();
                }
            } catch (Exception e) {
                writeLog("Exception_" + e.getMessage());
                e.printStackTrace();
            }
        } else if (resultCode == RESULT_OK) {
            try {
                Uri uri = data.getData();
                String filepath = getPath(getApplicationContext(),uri);
                imagePath = "C_GP_Img_" + currentDateFormat() + ".jpg";
                File sourceFile = new File(filepath);
                File destinationFile = new File(Environment.getExternalStorageDirectory() +
                        File.separator + Constant.folder_name + File.separator +
                        Constant.image_folder + File.separator + imagePath);
                copyImage(sourceFile, destinationFile,4);
            } catch (Exception e) {
                e.printStackTrace();
                toast.setText("Something Went Wrong");
                toast.show();
            }
        }
    }

    private void setIdValue(){

        if (radio_flag == 2) {
            value = "PAN CARD";
            Constant.showLog("setIdValue():panvalue:"+value);
        } else if (radio_flag == 1) {
            value = "GSTIN";
            Constant.showLog("setIdValue():gstvalue:"+value);
        }
        Cursor cursor =  db.getIdOfDocType(value);
        if(cursor.moveToFirst()){
            OptionsActivity.new_cus.setId_gstpan_proof(cursor.getString(cursor.getColumnIndex(DBHandler.Document_Id)));
        }
        Constant.showLog("Documentid:"+OptionsActivity.new_cus.getId_gstpan_proof());
        cursor.close();
    }

    private void gstpan_no_value() {
        if (radio_flag == 1) {
            String gst_no = ed_gstno.getText().toString();
            Constant.showLog("gst_no: " + gst_no);
            OptionsActivity.new_cus.setGst_no(gst_no);
            OptionsActivity.new_cus.setPan_no("NA");
        } else if (radio_flag == 2) {
            String pan_no = ed_panno.getText().toString();
            Constant.showLog("pan_no: " + pan_no);
            OptionsActivity.new_cus.setGst_no("NA");
            OptionsActivity.new_cus.setPan_no(pan_no);
        }
    }

    private void takeImage(int requestCode) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File f = Constant.checkFolder(Constant.folder_name + File.separator + Constant.image_folder);
        f = new File(f.getAbsolutePath(),"temp.jpg");
        Uri photoURI = FileProvider.getUriForFile(getApplicationContext(), getApplicationContext().getPackageName()
                + ".provider", f);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityForResult(intent,requestCode);
        overridePendingTransition(R.anim.enter, R.anim.exit);
    }

    private void openGallery(int requestCode) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, requestCode);
    }

    private String getPath(Context context, Uri uri) {
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else
            if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {split[1]};
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    private static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = { column };
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    private static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    private static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    private static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    private static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    private void copyImage(File source, File destination, int a) {
        try {
            FileChannel sourceChannel, destinationChannel;
            sourceChannel = new FileInputStream(source).getChannel();
            destinationChannel = new FileOutputStream(destination).getChannel();
            if (sourceChannel != null) {
                destinationChannel.transferFrom(sourceChannel, 0, sourceChannel.size());
            }
            if (sourceChannel != null) {
                sourceChannel.close();
            }
            destinationChannel.close();
            setImage(destination, a);
        } catch (Exception e) {
            e.printStackTrace();
            writeLog("copyImage_"+e.getMessage());
        }
    }

    private void setImage(File f, int a) {
        try {
            BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
            Bitmap bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(), bitmapOptions);
            Constant.showLog(f.getName() + "-" + f.getAbsolutePath());
            if (flag == 0) {
                if (_flag == 1) {
                    imageView_gst_img.setVisibility(View.VISIBLE);
                    imageView_gst_img.setImageBitmap(bitmap);
                } else if (_flag == 0) {
                    imageView_pan_img.setVisibility(View.VISIBLE);
                    imageView_pan_img.setImageBitmap(bitmap);
                }
            } else {
                if (_flag == 1) {
                    imageView_gst_img.setVisibility(View.VISIBLE);
                    imageView_gst_img.setImageBitmap(bitmap);
                } else if (_flag == 0) {
                    imageView_pan_img.setVisibility(View.VISIBLE);
                    imageView_pan_img.setImageBitmap(bitmap);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            writeLog("setImage_"+e.getMessage());
        }
    }

    private void set_value_attachgstpan_no() {
        if (radio_flag == 1) {
            gst_lay.setVisibility(View.VISIBLE);
            pan_lay.setVisibility(View.GONE);
            rdo_gst.setChecked(true);
            rdo_pan.setChecked(false);
            String gst = OptionsActivity.new_cus.getGst_no();
            Constant.showLog("gst: " + gst);
            ed_gstno.setText(gst);
            set_value_attachGstProofImage();
        } else if (radio_flag == 2) {
            gst_lay.setVisibility(View.GONE);
            pan_lay.setVisibility(View.VISIBLE);
            rdo_pan.setChecked(true);
            rdo_gst.setChecked(false);
            String pan = OptionsActivity.new_cus.getPan_no();
            Constant.showLog("pan_no: " + pan);
            ed_panno.setText(pan);
            set_value_attachPanProofImage();
        }
    }

    private void set_value_attachGstProofImage() {
        //AttachIdProofImageActivity.flag = 0;
        String filename = OptionsActivity.new_cus.getGst_no_image();
        Constant.showLog("filename: " + filename);

        File file = Constant.checkFolder(Constant.folder_name + File.separator + Constant.image_folder);
        File fileArray[] = file.listFiles();

        if (fileArray.length != 0) {
            for (File f : fileArray) {
                if (f.getName().equals(filename)) {
                    if (f.length() != 0) {
                        String _imagePath = getRealPathFromURI(Environment.getExternalStorageDirectory() + File.separator + Constant.folder_name
                                + File.separator + Constant.image_folder+ File.separator + filename);
                        imageView_gst_img.setImageBitmap(scaleBitmap(_imagePath));
                    }
                    break;
                }
            }
        }
    }

    private void set_value_attachPanProofImage() {
        //AttachIdProofImageActivity.flag = 0;
        String filename = OptionsActivity.new_cus.getPan_no_image();
        Constant.showLog("filename: " + filename);

        File file = Constant.checkFolder(Constant.folder_name +
                File.separator + Constant.image_folder + File.separator + Constant.image_folder);
        File fileArray[] = file.listFiles();

        if (fileArray.length != 0) {
            for (File f : fileArray) {
                if (f.getName().equals(filename)) {
                    if (f.length() != 0) {
                        String _imagePath = getRealPathFromURI(Environment.getExternalStorageDirectory() + File.separator + Constant.folder_name
                                + File.separator + Constant.image_folder+ File.separator + filename);
                        imageView_pan_img.setImageBitmap(scaleBitmap(_imagePath));
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

    private Bitmap scaleBitmap(String imagePath) {
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
        }
        return resizedBitmap;
    }

    private void showPopup(int id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you want to clear this data?");
        if(id == 1) {
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent in = new Intent(AttachGSTnoPANnoImageActivity.this, OptionsActivity.class);
                OptionsActivity.new_cus = null;
                startActivity(in);
                new Constant(AttachGSTnoPANnoImageActivity.this).doFinish();
                // finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        }else if (id == 2) {
            builder.setMessage("Select Attachment From...");
            builder.setPositiveButton("Gallery", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    openGallery(2);
                }
            });
            builder.setNegativeButton("Camera", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    takeImage(1);
                }
            });
            builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        }

        builder.create().show();
    }

    private void writeLog(String _data) {
        new WriteLog().writeLog(getApplicationContext(), "AttachGSTnoPANnoImageActivity_" + _data);
    }


}


