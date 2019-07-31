package com.lnbinfotech.msplfootwear.services;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;

import com.lnbinfotech.msplfootwear.FirstActivity;
import com.lnbinfotech.msplfootwear.R;
import com.lnbinfotech.msplfootwear.connectivity.ConnectivityTest;
import com.lnbinfotech.msplfootwear.constant.Constant;
import com.lnbinfotech.msplfootwear.db.DBHandler;
import com.lnbinfotech.msplfootwear.interfaces.RetrofitApiInterface;
import com.lnbinfotech.msplfootwear.log.WriteLog;
import com.lnbinfotech.msplfootwear.model.CustomerOrderClass;
import com.lnbinfotech.msplfootwear.model.ProductMasterClass;
import com.lnbinfotech.msplfootwear.model.SizeDesignMastDetClass;
import com.lnbinfotech.msplfootwear.model.SizeNDesignClass;
import com.lnbinfotech.msplfootwear.model.UserClass;
import com.lnbinfotech.msplfootwear.post.RequestResponseClass;
import com.lnbinfotech.msplfootwear.utility.RetrofitApiBuilder;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//Created by Sneha on 22-06-2018.

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class ScheduledJobServicePL extends JobService {

    private DBHandler db;
    private ArrayList<UserClass> userList;
    private ArrayList<CustomerOrderClass> custList;
    private File DBFileName,DBSDFileName;
    private String DBFilePath, DBSDFilePath, DBSDZipFilePath;
    private File SDDBZipFileName, SDDBUnzipFileName, SDDBFileName;
    private String SDDBZipFilePath, SDDBUnzipFilePath, SDDBFilePath;

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        Constant.showLog("onStartJob");
        int hour = Integer.parseInt(getTime());
        Constant.showLog("AutoSync_"+hour);

        //Intent intent2 = new Intent(getApplicationContext(), UploadImageService.class);
        //getApplicationContext().startService(intent2);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            getApplicationContext().startForegroundService(new Intent(getApplicationContext(), UploadImageService.class));
        } else {
            getApplicationContext().startService(new Intent(getApplicationContext(), UploadImageService.class));
        }

        if (ConnectivityTest.getNetStat(getApplicationContext())) {
            //startSync();
            if (isAppIsInBackground(getApplicationContext())) {
                Constant.showLog("App_IS_InBackground");
                writeLog("onStartJob_" + hour + "App_IS_InBackground");
                if (isSynced(getString(R.string.pref_lastSync)) || !FirstActivity.pref.contains(getString(R.string.pref_newDB))) {
                    downloadDB();
                    writeLog("onStartJob_" + hour + "_Online_Started");
                } else {
                    Constant.showLog("App_IS_InBackground");
                    writeLog("onStartJob_" + hour + "App_IS_InBackground");
                }
            } else {
                Constant.showLog("App_IS_NOT_InBackground");
                writeLog("onStartJob_" + hour + "App_IS_NOT_InBackground");
            }
        } else {
            writeLog("onStartJob_" + hour + "_Offline");
        }
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        Constant.showLog("onStopJob");
        writeLog("onStopJob");
        return false;
    }

    private void startSync(){
        db = new DBHandler(getApplicationContext());
        RequestResponseClass reqResp = new RequestResponseClass(getApplicationContext());

        if(getDateDifference(getString(R.string.pref_autoCompany))) {
            reqResp.loadCompanyMaster();
        }else{
            if(isSynced(getString(R.string.pref_autoCompany))) {
                String str = "select max(" + DBHandler.Company_Id + ") from " + DBHandler.Table_CompanyMaster;
                int max = db.getMaxAutoId(str);
                reqResp.loadCompanyMaster(max);
            }
        }

        if(isSynced(getString(R.string.pref_autoCustomer))) {
            reqResp.loadCustomerMaster();
        }

        /*if(getDateDifference(getString(R.string.pref_autoDocument))) {
            reqResp.loadDocumentMaster();
        }else{
            String str = "select max("+DBHandler.Document_Id+") from "+DBHandler.Table_DocumentMaster;
            int max = db.getMaxAutoId(str);
            reqResp.loadDocumentMaster(max);
        }

        if(getDateDifference(getString(R.string.pref_autoEmployee))) {
            reqResp.loadEmployeeMaster();
        }else{
            String str = "select max("+DBHandler.EMP_EmpId+") from "+DBHandler.Table_Employee;
            int max = db.getMaxAutoId(str);
            reqResp.loadEmployeeMaster(max);
        }*/

        if(getDateDifference(getString(R.string.pref_autoGST))) {
            reqResp.loadGSTMaster();
        }else{
            if(isSynced(getString(R.string.pref_autoGST))) {
                String str = "select max(" + DBHandler.GST_Auto + ") from " + DBHandler.Table_GSTMASTER;
                int max = db.getMaxAutoId(str);
                reqResp.loadGSTMaster(max);
            }
        }

        if(getDateDifference(getString(R.string.pref_autoHO))) {
            reqResp.loadHOMaster();
        }else{
            if(isSynced(getString(R.string.pref_autoHO))) {
                String str = "select max(" + DBHandler.HO_Auto + ") from " + DBHandler.Table_HOMaster;
                int max = db.getMaxAutoId(str);
                reqResp.loadHOMaster(max);
            }
        }

        /*if(getDateDifference(getString(R.string.pref_autoProduct))) {
            reqResp.getMaxAuto(1);
        }else{
            if(isSynced(getString(R.string.pref_autoProduct))) {
                String str = "select max(" + DBHandler.PM_ProductID + ") from " + DBHandler.Table_ProductMaster;
                int max = db.getMaxAutoId(str);
                reqResp.loadProductMaster(max);
            }
        }*/
        if(isSynced(getString(R.string.pref_autoProduct))) {
            getProductMasterV6();
        }
    }

    private boolean isSynced(String prefName){
        boolean ret = false;
        try {
            FirstActivity.pref = getApplicationContext().getSharedPreferences(FirstActivity.PREF_NAME, Context.MODE_PRIVATE);
            String str = FirstActivity.pref.getString(prefName, "01/Jan/1900");
            String strArr[] = str.split("\\-");
            String savedDate = "";
            if (strArr.length > 1) {
                savedDate = strArr[0];
            }else{
                savedDate = str;
            }
            String currDate = getDateTime();
            Date sDate = new SimpleDateFormat("dd/MMM/yyyy", Locale.ENGLISH).parse(savedDate);
            Date cDate = new SimpleDateFormat("dd/MMM/yyyy",Locale.ENGLISH).parse(currDate);
            if(cDate.compareTo(sDate)!=0){
                ret = true;
                String str1 = prefName+"-"+savedDate+"-"+currDate+"-"+ret;
                Constant.showLog(str1);
                writeLog("isSynced_"+str1);
            }else{
                ret = false;
                String str1 = prefName+"-"+savedDate+"-"+currDate+"-"+ret;
                Constant.showLog(str1);
                writeLog("isSynced_"+str1);
            }
        }catch (Exception e){
            e.printStackTrace();
            writeLog("isSynced_"+e.getMessage());
            ret = false;
        }
        return ret;
    }

    private boolean getDateDifference(String prefName){
        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        boolean ret = false;
        try {
            FirstActivity.pref = getApplicationContext().getSharedPreferences(FirstActivity.PREF_NAME, Context.MODE_PRIVATE);
            String str = FirstActivity.pref.getString(prefName, "01/Jan/1900");
            String strArr[] = str.split("\\-");
            String savedDate = "";
            if (strArr.length > 1) {
                savedDate = strArr[0];
            }else{
                savedDate = str;
            }
            String currDate = getDateTime();
            Date sDate = new SimpleDateFormat("dd/MMM/yyyy",Locale.ENGLISH).parse(savedDate);
            Date cDate = new SimpleDateFormat("dd/MMM/yyyy",Locale.ENGLISH).parse(currDate);

            long different = cDate.getTime() - sDate.getTime();
            long elapsedDays = different / daysInMilli;
            Constant.showLog("elapsedDays - "+elapsedDays);
            if(elapsedDays>15){
                ret = true;
                String str1 = prefName+"-"+savedDate+"-"+currDate+"-"+ret;
                Constant.showLog(str1);
                writeLog("ElapsedDays_"+elapsedDays+"_isSynced_"+str1);
            }else{
                ret = false;
                String str1 = prefName+"-"+savedDate+"-"+currDate+"-"+ret;
                Constant.showLog(str1);
                writeLog("ElapsedDays_"+elapsedDays+"_isSynced_"+str1);
            }
        }catch (Exception e){
            e.printStackTrace();
            writeLog("isSynced_"+e.getMessage());
            ret = false;
        }
        return ret;
    }

    private String getDateTime() {
        String str = "";
        try{
            str = new SimpleDateFormat("dd/MMM/yyyy", Locale.ENGLISH).format(new Date());
        }catch (Exception e){
            e.printStackTrace();
            writeLog("getDateTime_"+e.getMessage());
        }
        return str;
    }

    private void writeLog(String data) {
        new WriteLog().writeLog(getBaseContext(), "ScheduledJobServicePL_" + data);
    }

    private String getTime() {
        String str = "";
        try{
            str = new SimpleDateFormat("HH", Locale.ENGLISH).format(new Date());
        }catch (Exception e){
            e.printStackTrace();
        }
        return str;
    }

    private String getTime1() {
        String str = "";
        try{
            str = new SimpleDateFormat("dd/MMM/yyyy HH:mm", Locale.ENGLISH).format(new Date());
        }catch (Exception e){
            e.printStackTrace();
        }
        return str;
    }

    private void getProductMasterV6() {
        try {
            final DBHandler db = new DBHandler(getApplicationContext());
            String url = 0 + "|" + 10000 + "|" + "E";
            writeLog("getProductMasterV6_"+url);
            final JSONObject jsonBody = new JSONObject();
            jsonBody.put("details", url);
            RequestBody body = RequestBody.create(okhttp3.MediaType.
                    parse("application/json; charset=utf-8"), (jsonBody).toString());
            Constant.showLog(jsonBody.toString());

            Call<List<ProductMasterClass>> call = new RetrofitApiBuilder().getApiBuilder().
                    create(RetrofitApiInterface.class).
                    getProductMasterV6(body);
            call.enqueue(new Callback<List<ProductMasterClass>>() {
                @Override
                public void onResponse(Call<List<ProductMasterClass>> call, Response<List<ProductMasterClass>> response) {
                    Constant.showLog("onResponse");
                    List<ProductMasterClass> list = response.body();
                    if (list != null) {
                        if (list.size()!=0) {
                            db.deleteTable(DBHandler.Table_ProductMaster);
                        }
                        db.addProductMaster(list);
                        Constant.showLog(list.size() + "_getProductMasterV6");
                        writeLog("getProductMasterV6_onResponse_" + list.size());
                        getAllSizeDesignMastDetV6();
                    } else {
                        Constant.showLog("onResponse_list_null");
                        writeLog("getProductMasterV6_onResponse_list_null");
                    }
                }

                @Override
                public void onFailure(Call<List<ProductMasterClass>> call, Throwable t) {
                    Constant.showLog("onFailure");
                    if (!call.isCanceled()) {
                        call.cancel();
                    }
                    t.printStackTrace();
                    writeLog("getProductMasterV6_onFailure_" + t.getMessage());

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            writeLog("getProductMasterV6_" + e.getMessage());
        }
    }

    private void getAllSizeDesignMastDetV6() {
        try {
            final DBHandler db = new DBHandler(getApplicationContext());
            String url = 0 + "|" + 10000;
            writeLog("getAllSizeDesignMastDetV6_"+url);
            final JSONObject jsonBody = new JSONObject();
            jsonBody.put("details", url);
            RequestBody body = RequestBody.create(okhttp3.MediaType.
                    parse("application/json; charset=utf-8"), (jsonBody).toString());
            Constant.showLog(jsonBody.toString());

            Call<List<SizeNDesignClass>> call = new RetrofitApiBuilder().getApiBuilder().
                    create(RetrofitApiInterface.class).
                    getAllSizeDesignMastDetV6(body);
            call.enqueue(new Callback<List<SizeNDesignClass>>() {
                @Override
                public void onResponse(Call<List<SizeNDesignClass>> call, Response<List<SizeNDesignClass>> response) {
                    Constant.showLog("onResponse");
                    List<SizeNDesignClass> list = response.body();
                    if (list != null) {
                        if (list.size()!=0) {
                            db.deleteTable(DBHandler.Table_AllRequiredSizesDesigns);
                        }
                        db.addSizeNDesignMaster(list);
                        Constant.showLog(list.size() + "_getAllSizeDesignMastDetV6");
                        FirstActivity.pref = getSharedPreferences(FirstActivity.PREF_NAME,Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = FirstActivity.pref.edit();
                        String str = getDateTime()+"-"+"True"+"-"+getTime1();
                        Constant.showLog(getString(R.string.pref_autoSizeNDesign)+"-"+str);
                        editor.putString(getString(R.string.pref_autoSizeNDesign), getTime1());
                        editor.apply();
                        writeLog("getAllSizeDesignMastDetV6_onResponse_" + list.size() + "_" + str);
                        getSizeDesignMastDetV6();
                    } else {
                        Constant.showLog("onResponse_list_null");
                        writeLog("getAllSizeDesignMastDetV6_onResponse_list_null");
                    }
                }

                @Override
                public void onFailure(Call<List<SizeNDesignClass>> call, Throwable t) {
                    Constant.showLog("onFailure");
                    if (!call.isCanceled()) {
                        call.cancel();
                    }
                    t.printStackTrace();
                    writeLog("getAllSizeDesignMastDetV6_onFailure_" + t.getMessage());

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            writeLog("getAllSizeDesignMastDetV6_" + e.getMessage());
        }
    }

    private void getSizeDesignMastDetV6() {
        try {
            final DBHandler db = new DBHandler(getApplicationContext());
            String url = 0 + "|" + 10000;
            writeLog("getSizeDesignMastDetV6_"+url);
            final JSONObject jsonBody = new JSONObject();
            jsonBody.put("details", url);
            RequestBody body = RequestBody.create(okhttp3.MediaType.
                    parse("application/json; charset=utf-8"), (jsonBody).toString());
            Constant.showLog(jsonBody.toString());

            Call<List<SizeDesignMastDetClass>> call = new RetrofitApiBuilder().getApiBuilder().
                    create(RetrofitApiInterface.class).
                    getSizeDesignMastDetV6(body);
            call.enqueue(new Callback<List<SizeDesignMastDetClass>>() {
                @Override
                public void onResponse(Call<List<SizeDesignMastDetClass>> call, Response<List<SizeDesignMastDetClass>> response) {
                    Constant.showLog("onResponse");
                    List<SizeDesignMastDetClass> list = response.body();
                    if (list != null) {
                        if (list.size()!=0) {
                            db.deleteTable(DBHandler.Table_SizeDesignMastDet);
                        }
                        db.addSizeDesignMastDet(list);
                        Constant.showLog(list.size() + "_getSizeDesignMastDetV6");
                        FirstActivity.pref = getSharedPreferences(FirstActivity.PREF_NAME,Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor1 = FirstActivity.pref.edit();
                        String str1 = getDateTime()+"-"+"True"+"-"+getTime1();
                        Constant.showLog(getString(R.string.pref_autoSizeDetail)+"-"+str1);
                        editor1.putString(getString(R.string.pref_autoSizeDetail), getTime1());
                        editor1.apply();
                        writeLog("getSizeDesignMastDetV6_onResponse_" + list.size() + "_" + str1);

                        FirstActivity.pref = getSharedPreferences(FirstActivity.PREF_NAME,Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = FirstActivity.pref.edit();
                        String str = getDateTime()+"-"+"True"+"-"+getTime1();
                        Constant.showLog(getString(R.string.pref_autoProduct)+"-"+str);
                        editor.putString(getString(R.string.pref_autoProduct), getTime1());
                        editor.apply();
                        writeLog("getProductMasterV6_onResponse_" + str);

                        FirstActivity.pref = getSharedPreferences(FirstActivity.PREF_NAME,Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor2 = FirstActivity.pref.edit();
                        String str2 = getDateTime()+"-"+"True"+"-"+getTime1();
                        Constant.showLog(getString(R.string.pref_lastSync)+"-"+str2);
                        editor2.putString(getString(R.string.pref_lastSync), getTime1());
                        editor2.apply();

                    } else {
                        Constant.showLog("onResponse_list_null");
                        writeLog("getSizeDesignMastDetV6_onResponse_list_null");
                    }
                }

                @Override
                public void onFailure(Call<List<SizeDesignMastDetClass>> call, Throwable t) {
                    Constant.showLog("onFailure");
                    if (!call.isCanceled()) {
                        call.cancel();
                    }
                    t.printStackTrace();
                    writeLog("getSizeDesignMastDetV6_onFailure_" + t.getMessage());

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            writeLog("getSizeDesignMastDetV6_" + e.getMessage());
        }
    }

    private void downloadDB(){
        getData();
        writeLog("----- In downloadDB -----");
        Constant.showLog("----- In downloadDB -----");
        String file_url = Constant.imgUrl+Constant.zip_file;
        Constant.showLog(file_url);
        new DownloadFileFromURL().execute(file_url);
        writeLog("----- End downloadDB -----");
    }

    private void getData() {
        Constant.showLog("----- In getData ------");
        writeLog("----- In getData ------");
        DBHandler db = new DBHandler(getApplicationContext());
        userList = db.getUserDetail();
        custList = db.getCustOrder();
        Constant.showLog("userList-"+userList.size()+"-custList-"+custList.size());
        writeLog("userList-"+userList.size()+"-custList-"+custList.size());
        Constant.showLog("----- End getData ------");
        writeLog("----- End getData ------");
        db.close();
    }

    private class DownloadFileFromURL extends AsyncTask<String, String, String> {

        private FTPClient client = null;

        @Override
        protected String doInBackground(String... f_url) {
            try {
                writeLog("----- In DownloadFileFromURL ------");
                Constant.showLog("----- In DownloadFileFromURL ------");
                client = new FTPClient();
                try {
                    client.connect(Constant.ftp_adress1, 21);
                } catch (Exception e) {
                    e.printStackTrace();
                    writeLog("DownloadFileFromURL_doInBackground_"+e.getMessage());
                    client.connect(Constant.ftp_adress2, 21);
                }
                client.login(Constant.ftp_username, Constant.ftp_password);
                client.setFileType(FTP.BINARY_FILE_TYPE);
                client.enterLocalPassiveMode();
                if (client.changeWorkingDirectory(Constant.dir_data_sync)) {
                    SDDBZipFilePath = Environment.getExternalStorageDirectory() + File.separator
                            + Constant.folder_name  + File.separator + Constant.unzipFolderName;
                    SDDBZipFileName = new File(SDDBZipFilePath, Constant.zip_file);
                    if(SDDBZipFileName.exists()){
                        SDDBZipFileName.delete();
                        Constant.showLog(SDDBZipFileName.getAbsolutePath() + " Deleted ");
                        writeLog(SDDBZipFileName.getAbsolutePath() + " Deleted ");
                    }
                    Constant.showLog("SDDBZipFilePath - "+SDDBZipFilePath +"\n" +
                            "SDDBZipFileName - "+SDDBZipFileName.getAbsolutePath());
                    OutputStream outstream = new BufferedOutputStream(new FileOutputStream(SDDBZipFileName));
                    client.retrieveFile(Constant.zip_file, outstream);
                    outstream.close();
                    client.logout();
                    client.disconnect();
                    writeLog("File Downloaded Successfully");
                } else {
                    writeLog("Error While changeWorkingDirectory");
                }
            } catch (Exception e) {
                e.printStackTrace();
                writeLog("Exception "+e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String file_url) {
            Constant.showLog("----- End DownloadFileFromURL ------");
            writeLog("----- End DownloadFileFromURL ------");
            if (SDDBZipFilePath != null) {
                unzip();
            }
        }
    }

    public void unzip() {
        try {
            Constant.showLog("----- In unZip File ------");
            writeLog("----- In unZip File ------");

            SDDBUnzipFilePath = android.os.Environment.getExternalStorageDirectory() + File.separator +
                    Constant.folder_name + File.separator + Constant.unzipFolderName;

            Constant.showLog("SDDBUnzipFilePath - "+SDDBUnzipFilePath);

            FileInputStream fin = new FileInputStream(SDDBZipFileName);
            ZipInputStream zin = new ZipInputStream(fin);
            ZipEntry ze = null;
            FileOutputStream fout = null;
            BufferedInputStream in = null;
            BufferedOutputStream out = null;
            while ((ze = zin.getNextEntry()) != null) {
                SDDBUnzipFileName = new File(SDDBUnzipFilePath + "/" + ze.getName());
                if(SDDBUnzipFileName.exists()){
                    SDDBUnzipFileName.delete();
                    Constant.showLog(SDDBUnzipFileName.getAbsolutePath() + " Deleted ");
                    writeLog(SDDBUnzipFileName.getAbsolutePath() + " Deleted ");
                }
                Constant.showLog("SDDBUnzipFileName - "+SDDBUnzipFileName.getAbsolutePath());
                fout = new FileOutputStream(SDDBUnzipFileName);
                in = new BufferedInputStream(zin);
                out = new BufferedOutputStream(fout);
                byte b[] = new byte[1024];
                int n;
                while ((n = in.read(b, 0, 1024)) >= 0) {
                    out.write(b, 0, n);
                    //Constant.showLog("n "+n);
                }
                if(SDDBZipFileName.exists()) {
                    SDDBZipFileName.delete();
                }
                Constant.showLog("Write Complete");
                Constant.showLog("----- End unZip File ------");
                writeLog("----- End unZip File ------");
            }
            if(out!=null) {
                out.close();
            }
            if(fout!=null) {
                fout.close();
            }
            if(in!=null)
                in.close();

            CopySDTODB();
        } catch (Exception e) {
            e.printStackTrace();
            writeLog("unzip_"+e.getMessage());
        }
    }

    private void CopySDTODB() {
        try {
            Constant.showLog("----- In CopySDTODB ------");
            writeLog("----- In CopySDTODB ------");
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
            //SDDBFilePath = pInfo.applicationInfo.dataDir+"/databases/";
            SDDBFilePath = "/data/data/"+pInfo.packageName+"/databases/";

            SDDBUnzipFilePath = SDDBUnzipFileName.getAbsolutePath();

            File currentDB = new File(SDDBUnzipFilePath);
            File backupDB = new File(SDDBFilePath, DBHandler.Database_Name);
            /*if(backupDB.exists()){
                backupDB.delete();
                Constant.showLog(backupDB.getAbsolutePath()+" deleted");
                writeLog(backupDB.getAbsolutePath()+" deleted");
            }*/
            Constant.showLog("SDDBUnzipFileName - "+SDDBUnzipFileName +"\n" +
                    "SDDBUnzipFilePath - "+SDDBUnzipFilePath +"\n" +
                    "SDDBFilePath - "+SDDBFilePath +"\n" +
                    "currentDB - "+"\n" +
                    "backupDB - ");

            /*FileChannel source = new FileInputStream(SDDBUnzipFileName).getChannel();
            FileChannel destination = new FileOutputStream(backupDB).getChannel();
            destination.transferFrom(source, 0, source.size());
            destination.close();
            source.close();*/

            DBHandler db = new DBHandler(getApplicationContext(),SDDBUnzipFilePath);
            db.deleteTable(DBHandler.Table_CustomerOrder);
            db.deleteTable(DBHandler.Table_Usermaster);
            db.deleteTable(DBHandler.Table_TrackCustomerOrder);
            int count = 0;
            for(int i=0;i<userList.size();i++) {
                count++;
                db.addUserDetail(userList.get(i));
            }
            Constant.showLog(count+"");
            writeLog("userList "+count+" Added");
            count=0;
            for(int i=0;i<custList.size();i++) {
                count++;
                db.addCustomerOrder(custList.get(i));
            }
            Constant.showLog(count+"");
            writeLog("custList "+count+" Added");

            InputStream mInput = new FileInputStream(SDDBUnzipFileName);
            String outFileName = SDDBFilePath + DBHandler.Database_Name;
            Constant.showLog("outFileName - "+outFileName);
            OutputStream mOutput = new FileOutputStream(outFileName);
            byte[] mBuffer = new byte[1024];
            int mLength;
            while ((mLength = mInput.read(mBuffer))>0) {
                //Constant.showLog("mLength "+mLength);
                mOutput.write(mBuffer, 0, mLength);
            }
            mOutput.flush();
            mOutput.close();
            mInput.close();
            writeLog("outFileName - "+outFileName+" wrote ");

            db = new DBHandler(getApplicationContext());
            db.deleteTable(DBHandler.Table_TrackCustomerOrder);

            SharedPreferences.Editor editor = FirstActivity.pref.edit();
            String str = getTime1();
            Constant.showLog("Last Sync - " + str);
            writeLog("CopySDTODB_Last Sync_" + str);
            editor.putString(getString(R.string.pref_lastSync), str);
            editor.putBoolean(getString(R.string.pref_newDB),true);
            editor.apply();

            Constant.showLog("----- End CopySDTODB ------");
            writeLog("----- End CopySDTODB ------");
            new DBHandler(getApplicationContext(),SDDBUnzipFilePath).deleteTable(DBHandler.Table_TrackCustomerOrder);

            /*String arr[] = {getString(R.string.pref_autoArealine),getString(R.string.pref_autoArea),
                    getString(R.string.pref_autoBank), getString(R.string.pref_autoBankBranch),
                    getString(R.string.pref_autoCity), getString(R.string.pref_autoCompany),
                    getString(R.string.pref_autoCustomer), getString(R.string.pref_autoCurrency),
                    getString(R.string.pref_autoDocument), getString(R.string.pref_autoEmployee),
                    getString(R.string.pref_autoGST), getString(R.string.pref_autoHO),
                    getString(R.string.pref_autoProduct), getString(R.string.pref_autoSizeNDesign),
                    getString(R.string.pref_autoSizeDetail),getString(R.string.pref_lastSync)};

            for(String pref : arr) {
                updateSharedPref(pref,"Y");
            }*/

        } catch (Exception e) {
            e.printStackTrace();
            writeLog("CopySDTODB_"+e.getMessage());
        }
    }

    private boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                        }
                    }
                }
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
            }
        }
        return isInBackground;
    }

}
