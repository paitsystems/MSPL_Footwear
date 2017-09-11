package com.lnbinfotech.msplfootwear.model;

// Created by lnb on 8/16/2017.

import android.content.Context;

import com.lnbinfotech.msplfootwear.constant.Constant;

import java.io.File;
import java.util.concurrent.atomic.AtomicInteger;

public class ImageUploadClass {

    String FileNM = null;
    File f;
    Context context;
    AtomicInteger atomicInteger;
    Constant constant;

    public ImageUploadClass(String _fileNM, Context _context, AtomicInteger _atomicInteger,Constant _constant) {
        this.FileNM = _fileNM;
        this.context = _context;
        this.atomicInteger = _atomicInteger;
        this.constant = _constant;
    }

    public void uploadImage() {
        /*try {
            f = new File(Environment.getExternalStorageDirectory() + File.separator + Constant.folder_name,FileNM);
            FTPClient client = new FTPClient();
            client.connect(Constant.ftp_adress, 21);
            client.login(Constant.ftp_username, Constant.ftp_password);
            client.setFileType(FTP.BINARY_FILE_TYPE);
            client.enterLocalPassiveMode();
            if (f != null) {
                FileInputStream ifile = new FileInputStream(f);
                client.cwd(FirstActivity.pref.getString(context.getString(R.string.pref_FTPImgFolder),""));
                client.storeFile(FileNM, ifile);
            }
            client.disconnect();
            if(atomicInteger.decrementAndGet()==0){
                constant.showPD();
            }
        } catch (Exception e) {
            e.printStackTrace();
            if(atomicInteger.decrementAndGet()==0){
                constant.showPD();
            }
        }*/
    }
}
