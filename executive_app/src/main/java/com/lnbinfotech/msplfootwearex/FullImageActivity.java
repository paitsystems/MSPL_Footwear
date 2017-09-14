package com.lnbinfotech.msplfootwearex;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.Toast;

import com.lnbinfotech.msplfootwearex.constant.Constant;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class FullImageActivity extends AppCompatActivity {

    TouchImageView imageView;
    Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_image);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        imageView = (TouchImageView) findViewById(R.id.touch_imageview);
        toast = Toast.makeText(getApplicationContext(),"File Not Found",Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER,0,0);

        String imageName = getIntent().getExtras().getString("imagename");
        Constant.checkFolder(Constant.folder_name);
        File f = new File(Environment.getExternalStorageDirectory() + File.separator + Constant.folder_name + File.separator + imageName);
        if(f.length()!=0) {
            String _imagePath = getRealPathFromURI(Environment.getExternalStorageDirectory() + File.separator + Constant.folder_name + File.separator + imageName);
            imageView.setImageBitmap(scaleBitmap(_imagePath));
        }else{
            Toast toast = Toast.makeText(getApplicationContext(),"File Not Found",Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER,0,0);
            toast.show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                new Constant(FullImageActivity.this).doFinish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        new Constant(FullImageActivity.this).doFinish();
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
        }
        return resizedBitmap;
    }

}
