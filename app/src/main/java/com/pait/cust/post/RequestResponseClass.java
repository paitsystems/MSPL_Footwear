package com.pait.cust.post;

//Created by ANUP on 3/9/2018.

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.pait.cust.FirstActivity;
import com.pait.cust.R;
import com.pait.cust.constant.Constant;
import com.pait.cust.db.DBHandler;
import com.pait.cust.interfaces.ServerCallback;
import com.pait.cust.log.WriteLog;
import com.pait.cust.model.BankBranchMasterClass;
import com.pait.cust.model.CustomerDetailClass;
import com.pait.cust.model.SizeDesignMastDetClass;
import com.pait.cust.model.SizeNDesignClass;
import com.pait.cust.volleyrequests.VolleyRequests;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class RequestResponseClass {

    private Context context;
    private String writeFilename = "Write.txt", custfile = "custfile.txt", bankfile="bankfile.txt", sdmdfile="sdmd.txt", sndfile="sndfile.txt";
    private int maxProdId = 0, maxSDMDAuto = 0;

    public RequestResponseClass(Context _context){
        this.context = _context;
    }

    public void loadAreaMaster() {
        String url = Constant.ipaddress + "/GetAreaMaster?Id=0";
        Constant.showLog(url);
        writeLog("loadAreaMaster_" + url);
        VolleyRequests requests = new VolleyRequests(context);
        requests.refreshAreaMaster(url, new ServerCallback() {
            @Override
            public void onSuccess(String result) {
                updateSharedPref(context.getString(R.string.pref_autoArea),"Y");
            }

            @Override
            public void onFailure(String result) {
                updateSharedPref(context.getString(R.string.pref_autoArea),"N");
            }
        });
    }

    public void loadCityMaster() {
        String url = Constant.ipaddress + "/GetCityMaster?Id=0";
        Constant.showLog(url);
        writeLog("loadCityMaster_" + url);
        VolleyRequests requests = new VolleyRequests(context);
        requests.refreshCityMaster(url, new ServerCallback() {
            @Override
            public void onSuccess(String result) {
                updateSharedPref(context.getString(R.string.pref_autoCity),"Y");
            }

            @Override
            public void onFailure(String result) {
                updateSharedPref(context.getString(R.string.pref_autoCity),"N");
            }
        });
    }

    public void loadHOMaster() {
        String url = Constant.ipaddress + "/GetHOMaster?Id=0";
        Constant.showLog(url);
        writeLog("loadHOMaster_" + url);
        VolleyRequests requests = new VolleyRequests(context);
        requests.refreshHOMaster(url, new ServerCallback() {
            @Override
            public void onSuccess(String result) {
                updateSharedPref(context.getString(R.string.pref_autoHO),"Y");
            }

            @Override
            public void onFailure(String result) {
                updateSharedPref(context.getString(R.string.pref_autoHO),"N");
            }
        },0);
    }

    public void loadEmployeeMaster() {
        String url = Constant.ipaddress + "/GetEmployeeMaster?Id=0";
        Constant.showLog(url);
        writeLog("loadEmployeeMaster_" + url);
        VolleyRequests requests = new VolleyRequests(context);
        requests.refreshEmployeeMaster(url, new ServerCallback() {
            @Override
            public void onSuccess(String result) {
                updateSharedPref(context.getString(R.string.pref_autoEmployee),"Y");
            }

            @Override
            public void onFailure(String result) {
                updateSharedPref(context.getString(R.string.pref_autoEmployee),"N");
            }
        });
    }

    public void loadCompanyMaster() {
        String url = Constant.ipaddress + "/GetCompanyMaster?Id=0";
        Constant.showLog(url);
        writeLog("loadCompanyMaster_" + url);
        VolleyRequests requests = new VolleyRequests(context);
        requests.refreshCompanyMaster(url, new ServerCallback() {
            @Override
            public void onSuccess(String result) {
                updateSharedPref(context.getString(R.string.pref_autoCompany),"Y");
            }

            @Override
            public void onFailure(String result) {
                updateSharedPref(context.getString(R.string.pref_autoCompany),"N");
            }
        },0);
    }

    public void loadBankMaster() {
        String url = Constant.ipaddress + "/GetBankMaster?Id=0";
        Constant.showLog(url);
        writeLog("loadBankMaster_" + url);
        VolleyRequests requests = new VolleyRequests(context);
        requests.refreshBankMaster(url, new ServerCallback() {
            @Override
            public void onSuccess(String result) {
                updateSharedPref(context.getString(R.string.pref_autoBank),"Y");
            }

            @Override
            public void onFailure(String result) {
                updateSharedPref(context.getString(R.string.pref_autoBank),"N");
            }
        });
    }

    public void loadDocumentMaster() {
        String url = Constant.ipaddress + "/GetDocumentMaster?Id=0";
        Constant.showLog(url);
        writeLog("loadDocumentMaster_" + url);
        VolleyRequests requests = new VolleyRequests(context);
        requests.refreshDocumentMaster(url, new ServerCallback() {
            @Override
            public void onSuccess(String result) {
                updateSharedPref(context.getString(R.string.pref_autoDocument),"Y");
            }

            @Override
            public void onFailure(String result) {
                updateSharedPref(context.getString(R.string.pref_autoDocument),"N");
            }
        });
    }

    public void loadGSTMaster() {
        String url = Constant.ipaddress + "/GetGSTMaster?Id=0";
        Constant.showLog(url);
        writeLog("loadGSTMaster_" + url);
        VolleyRequests requests = new VolleyRequests(context);
        requests.refreshGSTMaster(url, new ServerCallback() {
            @Override
            public void onSuccess(String result) {
                updateSharedPref(context.getString(R.string.pref_autoGST),"Y");
            }

            @Override
            public void onFailure(String result) {
                updateSharedPref(context.getString(R.string.pref_autoGST),"N");
            }
        },0);
    }

    public void loadHOMaster(int max) {
        String url = Constant.ipaddress + "/GetHOMaster?Id="+max;
        Constant.showLog(url);
        writeLog("loadHOMaster_" + url);
        VolleyRequests requests = new VolleyRequests(context);
        requests.refreshHOMaster(url, new ServerCallback() {
            @Override
            public void onSuccess(String result) {
                updateSharedPref(context.getString(R.string.pref_autoHO),"Y");
            }

            @Override
            public void onFailure(String result) {
                updateSharedPref(context.getString(R.string.pref_autoHO),"N");
            }
        },max);
    }

    public void loadCompanyMaster(int max) {
        String url = Constant.ipaddress + "/GetCompanyMaster?Id="+max;
        Constant.showLog(url);
        writeLog("loadCompanyMaster_" + url);
        VolleyRequests requests = new VolleyRequests(context);
        requests.refreshCompanyMaster(url, new ServerCallback() {
            @Override
            public void onSuccess(String result) {
                updateSharedPref(context.getString(R.string.pref_autoCompany),"Y");
            }

            @Override
            public void onFailure(String result) {
                updateSharedPref(context.getString(R.string.pref_autoCompany),"N");
            }
        },max);
    }

    public void loadGSTMaster(int max) {
        String url = Constant.ipaddress + "/GetGSTMaster?Id="+max;
        Constant.showLog(url);
        writeLog("loadGSTMaster_" + url);
        VolleyRequests requests = new VolleyRequests(context);
        requests.refreshGSTMaster(url, new ServerCallback() {
            @Override
            public void onSuccess(String result) {
                updateSharedPref(context.getString(R.string.pref_autoGST),"Y");
            }

            @Override
            public void onFailure(String result) {
                updateSharedPref(context.getString(R.string.pref_autoGST),"N");
            }
        },max);
    }

    public void loadProductMaster(final int max) {
        maxProdId = 1000000;
        String url = Constant.ipaddress + "/GetProductMaster?Id="+max;
        Constant.showLog(url);
        writeLog("loadProductMaster_" + url);
        VolleyRequests requests = new VolleyRequests(context);
        requests.refreshProductMaster(url, new ServerCallback() {
            @Override
            public void onSuccess(String result) {
                loadSizeNDesignMaster(max, 1000000);
            }

            @Override
            public void onFailure(String result) {
                updateSharedPref(context.getString(R.string.pref_autoProduct),"N");
                updateSharedPref(context.getString(R.string.pref_autoSizeDetail),"N");
                updateSharedPref(context.getString(R.string.pref_autoSizeNDesign),"N");
                updateSharedPref(context.getString(R.string.pref_lastSync),"N");
            }
        },max,1000000);
    }

    public void loadProductMaster() {
        String url = Constant.ipaddress + "/GetProductMaster?Id=0";
        Constant.showLog(url);
        writeLog("loadProductMaster_" + url);
        VolleyRequests requests = new VolleyRequests(context);
        requests.refreshProductMaster(url, new ServerCallback() {
            @Override
            public void onSuccess(String result) {
                loadSizeNDesignMaster();
            }

            @Override
            public void onFailure(String result) {
                updateSharedPref(context.getString(R.string.pref_autoProduct),"N");
            }
        },0,0);
    }

    private void loadProductMaster(final int from, final int to) {
        String url = Constant.ipaddress + "/GetProductMasterV5?from="+from+"&to="+to+"&type=C";
        Constant.showLog(url);
        writeLog("loadProductMasterV5_" + url);
        VolleyRequests requests = new VolleyRequests(context);
        requests.refreshProductMaster(url, new ServerCallback() {
            @Override
            public void onSuccess(String result) {
                if (to == maxProdId) {
                    loadSizeNDesignMaster();
                } else {
                    int from = to + 1;
                    int _to = to + 500;
                    Constant.showLog("From-" + from + "-To-" + _to);
                    if (_to > maxProdId) {
                        _to = maxProdId;
                    }
                    loadProductMaster(from, _to);
                }
            }
            @Override
            public void onFailure(String result) {
                updateSharedPref(context.getString(R.string.pref_autoProduct),"N");
            }
        },from,to);
    }

    private void loadSizeNDesignMaster(){
        maxProdId = new DBHandler(context).getMaxProdId();
        if (maxProdId != 0) {
            //new DBHandler(context).deleteTable(DBHandler.Table_AllRequiredSizesDesigns);
            loadSizeNDesignMaster(0, 100);
        }
    }

    private void loadSizeNDesignMaster(int from, int to) {
        String url = Constant.ipaddress + "/GetAllRequiredSizesdesigns?Id=" + from + "&ToId=" + to;
        Constant.showLog(url);
        writeLog("loadSizeNDesignMaster_" + url);
        new getSizeNDesignMaster(from,to).execute(url);
    }

    private class getSizeNDesignMaster extends AsyncTask<String, Void, String> {
        private int from, to;

        private getSizeNDesignMaster(int _from, int _to) {
            this.from = _from;
            this.to = _to;
        }

        @Override
        protected String doInBackground(String... strings) {
            return Post.POST(strings[0]);
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);
            if(response!=null && !response.equals("")) {
                response = response.substring(1, response.length() - 1);
                new readJSON(response, "SizeNDesign", from, to).execute();
            }else{
                writeLog("getSizeNDesignMaster_response_null");
            }
        }
    }

    private class readJSON extends AsyncTask<Void, Void, String> {
        private int from, to;
        private String result, parseType;

        private readJSON(String _result, String _parseType, int _from, int _to) {
            this.result = _result;
            this.parseType = _parseType;
            this.from = _from;
            this.to = _to;
        }

        @Override
        protected String doInBackground(Void... voids) {
            String retValue = "A";
            File sdFile = Constant.checkFolder(Constant.folder_name);
            File writeFile = new File(sdFile, sndfile);
            FileWriter writer;
            try {
                String search = "\\\\", replace = "";
                writer = new FileWriter(writeFile);
                int size = result.length();
                if (size > 2) {
                    Log.d("Log", "Replacing");
                    int b = 50000;
                    for (int i = 0; i < size; i++) {
                        if (b >= size) {
                            b = size;
                        }
                        String q = result.substring(i, b);
                        String g = q.replaceAll(search, replace);
                        System.gc();
                        writer.append(g);
                        i = b - 1;
                        b = b + 50000;
                    }
                    retValue = "A";
                }
                writer.flush();
                writer.close();
                return retValue;
            } catch (IOException | OutOfMemoryError e) {
                writeLog("readJSON_"+e.getMessage());
                updateSharedPref(context.getString(R.string.pref_autoSizeNDesign),"N");
                writeFile.delete();
                try {
                    writer = new FileWriter(new File(sdFile, "Log.txt"));
                    writer.append(e.getMessage());
                    writer.flush();
                    writer.close();
                } catch (Exception e1) {
                    e.printStackTrace();
                }
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(s!=null) {
                if (s.equals("A")) {
                    new writeDB(parseType, from, to).execute();
                } else {
                    updateSharedPref(context.getString(R.string.pref_autoSizeNDesign), "N");
                }
            }else{
                updateSharedPref(context.getString(R.string.pref_autoSizeNDesign), "N");

            }
        }
    }

    private class writeDB extends AsyncTask<Void, String, String> {

        private File writeFile;
        private String parseType;
        private int from, to;

        private writeDB(String _parseType, int _from, int _to) {
            this.parseType = _parseType;
            this.from = _from;
            this.to = _to;
        }

        @Override
        protected String doInBackground(Void... voids) {
            File sdFile = Constant.checkFolder(Constant.folder_name);
            JsonFactory f = new JsonFactory();
            try {
                writeFile = new File(sdFile, sndfile);
                JsonParser jp = f.createJsonParser(writeFile);
                if (parseType.equals("SizeNDesign")) {
                    parseSizeNDesign(jp, from, to);
                }
                return "";
            } catch (Exception e) {
                writeLog("writeDB_"+e.getMessage());
                writeFile.delete();
                try {
                    FileWriter writer = new FileWriter(new File(sdFile, "Log.txt"));
                    writer.append(e.getMessage());
                    writer.flush();
                    writer.close();
                } catch (Exception e1) {
                    e.printStackTrace();
                    return null;
                }
                updateSharedPref(context.getString(R.string.pref_autoSizeNDesign),"N");
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try{
                if (s.equals("")) {
                    Constant.showLog("s is blank:");
                    if (writeFile.delete()) {
                        Constant.showLog("Write Delete");
                        if (to == maxProdId) {
                            updateSharedPref(context.getString(R.string.pref_autoSizeNDesign),"Y");
                            if(maxProdId==1000000){
                                loadSDMD(from);
                            }else {
                                loadSDMD();
                            }
                        } else {
                            int from = to + 1;
                            to = to + 100;
                            Constant.showLog("From-" + from + "-To-" + to);
                            if (to > maxProdId) {
                                to = maxProdId;
                            }
                            loadSizeNDesignMaster(from, to);
                        }
                    }
                } else {
                    updateSharedPref(context.getString(R.string.pref_autoSizeNDesign),"N");
                }
            }catch (Exception e){
                writeLog("onPostExecute_"+e.getMessage());
                updateSharedPref(context.getString(R.string.pref_autoSizeNDesign),"N");
            }
        }
    }

    private void parseSizeNDesign(JsonParser jp, int from, int to) {
        try {
            int count = 0;
            List<SizeNDesignClass> list = new ArrayList<>();
            while (jp.nextToken() != JsonToken.END_ARRAY) {
                count++;
                SizeNDesignClass sizeNDesignClass = new SizeNDesignClass();
                while (jp.nextToken() != JsonToken.END_OBJECT) {
                    String token = jp.getCurrentName();
                    if (DBHandler.ARSD_Productid.equals(token)) {
                        jp.nextToken();
                        sizeNDesignClass.setProductid(jp.getValueAsInt());
                    } else if (DBHandler.ARSD_DesignNo.equals(token)) {
                        jp.nextToken();
                        sizeNDesignClass.setDesignNo(jp.getText());
                    } else if (DBHandler.ARSD_Colour.equals(token)) {
                        jp.nextToken();
                        sizeNDesignClass.setColour(jp.getText());
                    } else if (DBHandler.ARSD_SizeGroup.equals(token)) {
                        jp.nextToken();
                        sizeNDesignClass.setSizeGroup(jp.getText());
                    } else if (DBHandler.ARSD_typ.equals(token)) {
                        jp.nextToken();
                        sizeNDesignClass.setTyp(jp.getText());
                    } else if (DBHandler.ARSD_SizeFrom.equals(token)) {
                        jp.nextToken();
                        sizeNDesignClass.setSizeFrom(jp.getValueAsInt());
                    } else if (DBHandler.ARSD_SizeTo.equals(token)) {
                        jp.nextToken();
                        sizeNDesignClass.setSizeTo(jp.getValueAsInt());
                    } else if (DBHandler.ARSD_GSTGroup.equals(token)) {
                        jp.nextToken();
                        sizeNDesignClass.setGSTGroup(jp.getText());
                    } else if (DBHandler.ARSD_InOutType.equals(token)) {
                        jp.nextToken();
                        sizeNDesignClass.setInOutType(jp.getText());
                    } else if (DBHandler.ARSD_Total.equals(token)) {
                        jp.nextToken();
                        sizeNDesignClass.setTotal(jp.getValueAsInt());
                    } else if (DBHandler.ARSD_HashCode.equals(token)) {
                        jp.nextToken();
                        sizeNDesignClass.setHashCode(jp.getText());
                    } else if (DBHandler.ARSD_ImageName.equals(token)) {
                        jp.nextToken();
                        sizeNDesignClass.setImageName(jp.getText());
                    } /* else if (DBHandler.ARSD_Cat1.equals(token)) {
                        jp.nextToken();
                        sizeNDesignClass.setCat1(jp.getText());
                    } else if (DBHandler.ARSD_Cat2.equals(token)) {
                        jp.nextToken();
                        sizeNDesignClass.setCat2(jp.getText());
                    } else if (DBHandler.ARSD_Cat3.equals(token)) {
                        jp.nextToken();
                        sizeNDesignClass.setCat3(jp.getText());
                    } else if (DBHandler.ARSD_Cat4.equals(token)) {
                        jp.nextToken();
                        sizeNDesignClass.setCat4(jp.getText());
                    } else if (DBHandler.ARSD_Cat5.equals(token)) {
                        jp.nextToken();
                        sizeNDesignClass.setCat5(jp.getText());
                    } else if (DBHandler.ARSD_Cat6.equals(token)) {
                        jp.nextToken();
                        sizeNDesignClass.setCat6(jp.getText());
                    } else if (DBHandler.ARSD_Final_prod.equals(token)) {
                        jp.nextToken();
                        sizeNDesignClass.setFinal_prod(jp.getText());
                    } else if (DBHandler.ARSD_Uom.equals(token)) {
                        jp.nextToken();
                        sizeNDesignClass.setUom(jp.getText());
                    } else if (DBHandler.ARSD_Vat.equals(token)) {
                        jp.nextToken();
                        sizeNDesignClass.setVat(jp.getText());
                    }else if (DBHandler.ARSD_ActualInw.equals(token)) {
                        jp.nextToken();
                        sizeNDesignClass.setActualInw(jp.getText());
                    }*/
                }
                list.add(sizeNDesignClass);
            }
            new DBHandler(context).deleteTableFromToRange(DBHandler.Table_AllRequiredSizesDesigns, from, to);
            new DBHandler(context).addSizeNDesignMaster(list);
            Constant.showLog("" + count);
        } catch (Exception e) {
            writeLog("parseSizeNDesign_" + e.getMessage());
            e.printStackTrace();
            updateSharedPref(context.getString(R.string.pref_autoSizeNDesign),"N");
        }
    }

    public void loadCustomerMaster() {
        int from = FirstActivity.pref.getInt(context.getString(R.string.pref_retailCustId), 0);
        String url = Constant.ipaddress + "/GetCustomerMasterV5?from="+from+"&to=0&type=C";
        Constant.showLog(url);
        writeLog("loadCustomerMaster_" + url);
        new getCustomerMaster().execute(url);
    }

    private class getCustomerMaster extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            return Post.POST(strings[0]);
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);
            if(response!=null && !response.equals("")) {
                response = response.replace("\\\\r\\\\n", "");
                response = response.substring(1, response.length() - 1);
                new readCustJSON(response, "CustMast").execute();
            }else{
                writeLog("getCustomerMaster_response_null");
            }
        }
    }

    private class readCustJSON extends AsyncTask<Void, Void, String> {
        private String result, parseType;

        private readCustJSON(String _result, String _parseType) {
            this.result = _result;
            this.parseType = _parseType;
        }

        @Override
        protected String doInBackground(Void... voids) {
            String retValue = "A";
            File sdFile = Constant.checkFolder(Constant.folder_name);
            File writeFile = new File(sdFile, custfile);
            FileWriter writer;
            try {
                String search = "\\\\", replace = "";
                writer = new FileWriter(writeFile);
                int size = result.length();
                if (size > 2) {
                    Log.d("Log", "Replacing");
                    int b = 50000;
                    for (int i = 0; i < size; i++) {
                        if (b >= size) {
                            b = size;
                        }
                        String q = result.substring(i, b);
                        String g = q.replaceAll(search, replace);
                        System.gc();
                        writer.append(g);
                        i = b - 1;
                        b = b + 50000;
                    }
                    retValue = "A";
                }
                writer.flush();
                writer.close();
                return retValue;
            } catch (IOException | OutOfMemoryError e) {
                updateSharedPref(context.getString(R.string.pref_autoCustomer),"N");
                writeFile.delete();
                writeLog("readCustJSON_" + e.getMessage());
                try {
                    writer = new FileWriter(new File(sdFile, "Log.txt"));
                    writer.append(e.getMessage());
                    writer.flush();
                    writer.close();
                } catch (Exception e1) {
                    e.printStackTrace();
                }
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(s!=null) {
                if (s.equals("A")) {
                    new writeCustDB(parseType).execute();
                } else {
                    updateSharedPref(context.getString(R.string.pref_autoCustomer), "N");
                }
            }
        }
    }

    private class writeCustDB extends AsyncTask<Void, String, String> {
        private File writeFile;
        private String parseType;

        private writeCustDB(String _parseType) {
            this.parseType = _parseType;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... voids) {
            File sdFile = Constant.checkFolder(Constant.folder_name);
            JsonFactory f = new JsonFactory();
            try {
                writeFile = new File(sdFile, custfile);
                JsonParser jp = f.createJsonParser(writeFile);
                parseCustMaster(jp);
                return "";
            } catch (Exception e) {
                writeFile.delete();
                updateSharedPref(context.getString(R.string.pref_autoCustomer),"N");
                writeLog("writeCustDB_" + e.getMessage());
                try {
                    FileWriter writer = new FileWriter(new File(sdFile, "Log.txt"));
                    writer.append(e.getMessage());
                    writer.flush();
                    writer.close();
                } catch (Exception e1) {
                    e.printStackTrace();
                    return null;
                }
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s != null) {
                if (s.equals("")) {
                    if (writeFile.delete()) {
                        updateSharedPref(context.getString(R.string.pref_autoCustomer), "Y");
                        Constant.showLog("Write Delete");
                    }
                } else {
                    updateSharedPref(context.getString(R.string.pref_autoCustomer), "N");
                }
            } else {
                updateSharedPref(context.getString(R.string.pref_autoCustomer), "N");
            }
        }
    }

    private void parseCustMaster(JsonParser jp) {
        try {
            ArrayList<CustomerDetailClass> list = new ArrayList<>();
            while (jp.nextToken() != JsonToken.END_ARRAY) {
                CustomerDetailClass custClass = new CustomerDetailClass();
                while (jp.nextToken() != JsonToken.END_OBJECT) {
                    String token = jp.getCurrentName();
                    if ("retailCustID".equals(token)) {
                        jp.nextToken();
                        custClass.setCustID(jp.getIntValue());
                    } else if ("name".equals(token)) {
                        jp.nextToken();
                        custClass.setName(jp.getText());
                    } else if ("address".equals(token)) {
                        jp.nextToken();
                        custClass.setAddress(jp.getText());
                    } else if ("mobile".equals(token)) {
                        jp.nextToken();
                        custClass.setMobile(jp.getText());
                    } else if ("email".equals(token)) {
                        jp.nextToken();
                        custClass.setEmail(jp.getText());
                    } else if ("Panno".equals(token)) {
                        jp.nextToken();
                        custClass.setPANno(jp.getText());
                    } else if ("PartyName".equals(token)) {
                        jp.nextToken();
                        custClass.setPartyName(jp.getText());
                    } else if ("GSTNo".equals(token)) {
                        jp.nextToken();
                        custClass.setGSTNo(jp.getText());
                    } else if ("status".equals(token)) {
                        jp.nextToken();
                        custClass.setStatus(jp.getText());
                    } else if ("ImagePath".equals(token)) {
                        jp.nextToken();
                        custClass.setImagePath(jp.getText());
                    } else if ("Discount".equals(token)) {
                        jp.nextToken();
                        custClass.setDiscount(jp.getFloatValue());
                    } else if ("branchId".equals(token)) {
                        jp.nextToken();
                        custClass.setBranchId(jp.getIntValue());
                    } else if ("District".equals(token)) {
                        jp.nextToken();
                        custClass.setDistrict(jp.getText());
                    } else if ("Taluka".equals(token)) {
                        jp.nextToken();
                        custClass.setTaluka(jp.getText());
                    } else if ("cityId".equals(token)) {
                        jp.nextToken();
                        custClass.setCityId(jp.getIntValue());
                    } else if ("areaId".equals(token)) {
                        jp.nextToken();
                        custClass.setAreaId(jp.getIntValue());
                    } else if ("HoCode".equals(token)) {
                        jp.nextToken();
                        custClass.setHOCode(jp.getIntValue());
                    } else if ("AadharNo".equals(token)) {
                        jp.nextToken();
                        custClass.setAadharNo(jp.getText());
                    }
                }
                list.add(custClass);
            }
            new DBHandler(context).deleteTable(DBHandler.Table_Customermaster);
            new DBHandler(context).addCustomerDetail(list);
        } catch (Exception e) {
            updateSharedPref(context.getString(R.string.pref_autoCustomer),"N");
            writeLog("parseCustMaster_" + e.getMessage());
            e.printStackTrace();
        }
    }

    public void loadBankBranchMaster() {
        String url = Constant.ipaddress + "/GetBankBranchMaster?Id=0";
        Constant.showLog(url);
        writeLog("loadBankBranchMaster_" + url);
        new getBankBranchMaster().execute(url);
    }

    private class getBankBranchMaster extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            return Post.POST(strings[0]);
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);
            if(response!=null && !response.equals("")) {
                response = response.substring(1, response.length() - 1);
                new readBBJSON(response, "BankBranchMast").execute();
            }else{
                writeLog("getBankBranchMaster_response_null");
            }
        }
    }

    private class readBBJSON extends AsyncTask<Void, Void, String> {
        private String result, parseType;

        private readBBJSON(String _result, String _parseType) {
            this.result = _result;
            this.parseType = _parseType;
        }

        @Override
        protected String doInBackground(Void... voids) {
            String retValue = "A";
            File sdFile = Constant.checkFolder(Constant.folder_name);
            File writeFile = new File(sdFile, bankfile);
            FileWriter writer;
            try {
                String search = "\\\\", replace = "";
                writer = new FileWriter(writeFile);
                int size = result.length();
                if (size > 2) {
                    Log.d("Log", "Replacing");
                    int b = 50000;
                    for (int i = 0; i < size; i++) {
                        if (b >= size) {
                            b = size;
                        }
                        String q = result.substring(i, b);
                        String g = q.replaceAll(search, replace);
                        System.gc();
                        writer.append(g);
                        i = b - 1;
                        b = b + 50000;
                    }
                    retValue = "A";
                }
                writer.flush();
                writer.close();
                return retValue;
            } catch (IOException | OutOfMemoryError e) {
                updateSharedPref(context.getString(R.string.pref_autoBankBranch),"N");
                writeFile.delete();
                writeLog("readBBJSON_" + e.getMessage());
                try {
                    writer = new FileWriter(new File(sdFile, "Log.txt"));
                    writer.append(e.getMessage());
                    writer.flush();
                    writer.close();
                } catch (Exception e1) {
                    e.printStackTrace();
                }
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(s!=null) {
                if (s.equals("A")) {
                    new writeBBDB(parseType).execute();
                } else {
                    updateSharedPref(context.getString(R.string.pref_autoBankBranch), "N");
                }
            }else {
                updateSharedPref(context.getString(R.string.pref_autoBankBranch), "N");
            }
        }
    }

    private class writeBBDB extends AsyncTask<Void, String, String> {
        private File writeFile;
        private String parseType;

        private writeBBDB(String _parseType) {
            this.parseType = _parseType;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... voids) {
            File sdFile = Constant.checkFolder(Constant.folder_name);
            JsonFactory f = new JsonFactory();
            try {
                writeFile = new File(sdFile, bankfile);
                JsonParser jp = f.createJsonParser(writeFile);
                parseBankBranchMaster(jp);
                return "";
            } catch (Exception e) {
                writeFile.delete();
                updateSharedPref(context.getString(R.string.pref_autoBankBranch),"N");
                writeLog("writeBBDB_" + e.getMessage());
                try {
                    FileWriter writer = new FileWriter(new File(sdFile, "Log.txt"));
                    writer.append(e.getMessage());
                    writer.flush();
                    writer.close();
                } catch (Exception e1) {
                    e.printStackTrace();
                    return null;
                }
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s != null) {
                if (s.equals("")) {
                    if (writeFile.delete()) {
                        Constant.showLog("Write Delete");
                    }
                    updateSharedPref(context.getString(R.string.pref_autoBankBranch), "Y");
                } else {
                    updateSharedPref(context.getString(R.string.pref_autoBankBranch), "N");
                }
            }else {
                updateSharedPref(context.getString(R.string.pref_autoBankBranch), "Y");
            }
        }
    }

    private void parseBankBranchMaster(JsonParser jp) {
        try {
            ArrayList<BankBranchMasterClass> list = new ArrayList<>();
            while (jp.nextToken() != JsonToken.END_ARRAY) {
                BankBranchMasterClass bbClass = new BankBranchMasterClass();
                while (jp.nextToken() != JsonToken.END_OBJECT) {
                    String token = jp.getCurrentName();
                    if ("Autoid".equals(token)) {
                        jp.nextToken();
                        bbClass.setAutoid(jp.getText());
                    } else if ("id".equals(token)) {
                        jp.nextToken();
                        bbClass.setId(jp.getText());
                    } else if ("Branch".equals(token)) {
                        jp.nextToken();
                        bbClass.setBranch(jp.getText());
                    } else if ("Custid".equals(token)) {
                        jp.nextToken();
                        bbClass.setCustid(jp.getText());
                    } else if ("AccountNo".equals(token)) {
                        jp.nextToken();
                        bbClass.setAccountNo(jp.getText());
                    } else if ("CBranch".equals(token)) {
                        jp.nextToken();
                        bbClass.setcBranch(jp.getText());
                    } else if ("CBankid".equals(token)) {
                        jp.nextToken();
                        bbClass.setcBankid(jp.getText());
                    }
                }
                list.add(bbClass);
            }
            new DBHandler(context).deleteTable(DBHandler.Table_BankBranchMaster);
            new DBHandler(context).addBankBranchMaster(list);
        } catch (Exception e) {
            writeLog("parseBankBranchMaster_" + e.getMessage());
            e.printStackTrace();
            updateSharedPref(context.getString(R.string.pref_autoBankBranch),"N");
        }
    }

    private void loadSDMD(int max) {
        loadSDMD(max, 1000000);
    }

    private void loadSDMD(){
        maxSDMDAuto = new DBHandler(context).getMaxProdId();
        if (maxSDMDAuto != 0) {
            Constant.showLog("maxSDMDAuto :- "+maxSDMDAuto);
            loadSDMD(0,100);
        }
    }

    private void loadSDMD(int from, int to) {
        String url = Constant.ipaddress + "/GetAllSizeDesignMastDet?Id=" + from + "&ToId=" + to;
        Constant.showLog(url);
        writeLog("loadSDMD_" + url);
        new getSizeDesignMastDet(from,to).execute(url);
    }

    private class getSizeDesignMastDet extends AsyncTask<String, Void, String> {
        private int from,to;

        private getSizeDesignMastDet(int _from,int _to) {
            this.from = _from;
            this.to = _to;
        }

        @Override
        protected String doInBackground(String... strings) {
            return Post.POST(strings[0]);
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);
            if(response!=null && !response.equals("")) {
                response = response.substring(1, response.length() - 1);
                new readJSONSDMD(response, "SDMD", from, to).execute();
            }else{
                writeLog("getSizeDesignMastDet_response_null");
            }
        }
    }

    private class readJSONSDMD extends AsyncTask<Void, Void, String> {
        private int from,to;
        private File writeFile;
        private String result, parseType;

        private readJSONSDMD(String _result, String _parseType, int _from, int _to) {
            this.result = _result;
            this.parseType = _parseType;
            this.from = _from;
            this.to = _to;
        }

        @Override
        protected String doInBackground(Void... voids) {
            String retValue = "B";
            File sdFile = Constant.checkFolder(Constant.folder_name);
            writeFile = new File(sdFile, sdmdfile);
            FileWriter writer;
            try {
                String search = "\\\\", replace = "";
                writer = new FileWriter(writeFile);
                int size = result.length();
                if (size > 2) {
                    Log.d("Log", "Replacing");
                    int b = 50000;
                    for (int i = 0; i < size; i++) {
                        if (b >= size) {
                            b = size;
                        }
                        String q = result.substring(i, b);
                        String g = q.replaceAll(search, replace);
                        System.gc();
                        writer.append(g);
                        i = b - 1;
                        b = b + 50000;
                    }
                    retValue = "A";
                }
                writer.flush();
                writer.close();
                return retValue;
            } catch (IOException | OutOfMemoryError e) {
                writeFile.delete();
                writeLog("readJSONSDMD_"+e.getMessage());
                try {
                    writer = new FileWriter(new File(sdFile, "Log.txt"));
                    writer.append(e.getMessage());
                    writer.flush();
                    writer.close();
                } catch (Exception e1) {
                    e.printStackTrace();
                }
                updateSharedPref(context.getString(R.string.pref_autoSizeDetail),"N");
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s.equals("A")) {
                new writeDBSDMD(parseType, from,to).execute();
            }else if (s.equals("B")) {
                nextValue(to,writeFile);
            }
        }
    }

    private class writeDBSDMD extends AsyncTask<Void, String, String> {

        private File writeFile;
        private String parseType;
        private int from, to;

        private writeDBSDMD(String _parseType, int _from, int _to) {
            this.parseType = _parseType;
            this.from = _from;
            this.to = _to;
        }

        @Override
        protected String doInBackground(Void... voids) {
            File sdFile = Constant.checkFolder(Constant.folder_name);
            JsonFactory f = new JsonFactory();
            try {
                writeFile = new File(sdFile, sdmdfile);
                JsonParser jp = f.createJsonParser(writeFile);
                parseSDMD(jp, from,to);
                return "";
            } catch (Exception e) {
                writeFile.delete();
                writeLog("writeDBSDMD_"+e.getMessage());
                updateSharedPref(context.getString(R.string.pref_autoSizeDetail),"N");
                try {
                    FileWriter writer = new FileWriter(new File(sdFile, "Log.txt"));
                    writer.append(e.getMessage());
                    writer.flush();
                    writer.close();
                } catch (Exception e1) {
                    e.printStackTrace();
                    return null;
                }
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                if (s != null) {
                    if (s.equals("")) {
                        nextValue(to, writeFile);
                    } else {
                        updateSharedPref(context.getString(R.string.pref_autoSizeDetail), "N");
                    }
                } else {
                    updateSharedPref(context.getString(R.string.pref_autoSizeDetail), "N");
                }
            }catch (Exception e){
                e.printStackTrace();
                writeLog("writeDBSDMD_"+e.getMessage());
                updateSharedPref(context.getString(R.string.pref_autoSizeDetail), "N");
            }
        }
    }

    private void nextValue(int to,File writeFile){
        Constant.showLog("s is blank:");
        if (writeFile.delete()) {
            Constant.showLog("Write Delete");
            if (to == maxSDMDAuto) {
                updateSharedPref(context.getString(R.string.pref_autoProduct),"Y");
                updateSharedPref(context.getString(R.string.pref_autoSizeDetail),"Y");
                updateSharedPref(context.getString(R.string.pref_lastSync),"Y");
            } else {
                int from = to + 1;
                to = to + 100;
                Constant.showLog("From-" + from + "-To-" + to);
                if (to > maxSDMDAuto) {
                    to = maxSDMDAuto;
                }
                loadSDMD(from,to);
            }
        }
    }

    private void parseSDMD(JsonParser jp, int from, int to) {
        try {
            int count = 0;
            List<SizeDesignMastDetClass> list = new ArrayList<>();
            while (jp.nextToken() != JsonToken.END_ARRAY) {
                count++;
                SizeDesignMastDetClass sizeNDesignClass = new SizeDesignMastDetClass();
                while (jp.nextToken() != JsonToken.END_OBJECT) {
                    String token = jp.getCurrentName();
                    if (DBHandler.SDMD_Auto.equals(token)) {
                        jp.nextToken();
                        sizeNDesignClass.setAuto(jp.getValueAsInt());
                    } else if (DBHandler.SDMD_ProductId.equals(token)) {
                        jp.nextToken();
                        sizeNDesignClass.setProductid(jp.getValueAsInt());
                    } else if (DBHandler.SDMD_DesignNo.equals(token)) {
                        jp.nextToken();
                        String dn = jp.getText();
                        sizeNDesignClass.setDesignNo(dn);
                    } else if (DBHandler.SDMD_SizeGroupFrom.equals(token)) {
                        jp.nextToken();
                        sizeNDesignClass.setSizeGroupFrom(jp.getValueAsInt());
                    } else if (DBHandler.SDMD_SizeGroupTo.equals(token)) {
                        jp.nextToken();
                        sizeNDesignClass.setSizeGroupTo(jp.getValueAsInt());
                    }else if (DBHandler.SDMD_Total.equals(token)) {
                        jp.nextToken();
                        sizeNDesignClass.setTotal(jp.getValueAsInt());
                    } else if (DBHandler.SDMD_SizeGroup.equals(token)) {
                        jp.nextToken();
                        sizeNDesignClass.setSizeGroup(jp.getText());
                    } else if (DBHandler.SDMD_Colour.equals(token)) {
                        jp.nextToken();
                        String col = jp.getText();
                        sizeNDesignClass.setColour(col);
                    } else if (DBHandler.SDMD_Size.equals(token)) {
                        jp.nextToken();
                        sizeNDesignClass.setSize(jp.getValueAsInt());
                    } else if (DBHandler.SDMD_Qty.equals(token)) {
                        jp.nextToken();
                        sizeNDesignClass.setQty(jp.getValueAsInt());
                    }
                }
                list.add(sizeNDesignClass);
            }
            new DBHandler(context).deleteTableFromToRangeSDMD(DBHandler.Table_SizeDesignMastDet, from, to);
            new DBHandler(context).addSizeDesignMastDet(list);
            Constant.showLog("" + count);
        } catch (Exception e) {
            updateSharedPref(context.getString(R.string.pref_autoSizeDetail),"N");
            writeLog("sdmd_" + e.getMessage());
            e.printStackTrace();
        }
    }

    private void updateSharedPref(String prefname, String value){
        writeLog(prefname+"_"+value);
        FirstActivity.pref = context.getSharedPreferences(FirstActivity.PREF_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = FirstActivity.pref.edit();
        String str = getDateTime()+"-"+value+"-"+getTime();
        Constant.showLog(prefname+"-"+str);
        if(!prefname.equals(context.getString(R.string.pref_lastSync))) {
            editor.putString(prefname, str);
        }else{
            editor.putString(prefname, getTime());
        }
        editor.apply();
    }

    public void getMaxAuto(final int type) {
        String url = Constant.ipaddress + "/GetMaxAuto?type="+type;
        Constant.showLog(url);
        writeLog("getMaxAuto_" + url);
        VolleyRequests requests = new VolleyRequests(context);
        requests.getMaxAuto(url, new ServerCallback() {
            @Override
            public void onSuccess(String result) {
                if(type==1){
                    maxProdId = Integer.valueOf(result);
                    loadProductMaster(0, 100);
                }/*else if(type==2){
                    maxCustId = Integer.valueOf(result);
                    loadCustomerMaster(0, 100);
                }else if(type==3){
                    maxBankBranchId = Integer.valueOf(result);
                    loadBankBranchMaster(0, 100);
                }*/
            }

            @Override
            public void onFailure(String result) {
                if(type==1){
                    maxProdId = new DBHandler(context).getMaxProdId();
                    loadProductMaster(0, 100);
                }/*else if(type==2){
                    maxCustId = new DBHandler(context).getCustMax();
                    loadCustomerMaster(0, 100);
                }else if(type==3){
                    maxBankBranchId = new DBHandler(context).getBankBranchMax();
                    loadBankBranchMaster(0, 100);
                }*/
            }
        });
    }

    private String getDateTime() {
        String str = "";
        try{
            str = new SimpleDateFormat("dd/MMM/yyyy", Locale.ENGLISH).format(new Date());
        }catch (Exception e){
            e.printStackTrace();
        }
        return str;
    }

    private String getTime() {
        String str = "";
        try{
            str = new SimpleDateFormat("dd/MMM/yyyy HH:mm", Locale.ENGLISH).format(new Date());
        }catch (Exception e){
            e.printStackTrace();
        }
        return str;
    }

    private void writeLog(String _data){
        new WriteLog().writeLog(context,"RequestResponseClass_"+_data);
    }

}
