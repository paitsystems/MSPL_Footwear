package com.pait.cust;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.Gravity;

import com.pait.cust.adapters.GSTReportAdapter;
import com.pait.cust.constant.Constant;
import com.pait.cust.log.WriteLog;
import com.pait.cust.model.GSTDetailClass;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class GSTReportActivity extends AppCompatActivity implements View.OnClickListener {

    private Constant constant, constant1;
    private Toast toast;
    private ListView lv_gstdetail;
    private TextView tv_fdate, tv_tdate, tv_totQty, tv_totAssValue, tv_totCGSTAmnt, tv_totSGSTAmnt,
            tv_totIGSTAmnt, tv_totRoundOff, tv_totNetSale;
    private Calendar cal = Calendar.getInstance();
    private static final int fdt = 1, tdt = 2;
    private String fromdate = "", todate = "", all = "";
    private float totQty = 0, totAssValue = 0, totCGSTAmnt = 0, totSGSTAmnt = 0, totIGSTAmnt = 0,
            totRoundOff = 0, totNetSale = 0;
    private DecimalFormat flt_price;
    private AppCompatButton btn_show;
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
    private SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MMM/yyyy", Locale.ENGLISH);
    private int day, month, year;
    private List<GSTDetailClass> gstDetList;
    private String exportFileName = "", partyName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(Constant.liveTestFlag==1) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }

        setContentView(R.layout.activity_gst_report);

        init();

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_show:
                totQty = 0;
                totAssValue = 0;
                totCGSTAmnt = 0;
                totSGSTAmnt = 0;
                totIGSTAmnt = 0;
                totRoundOff = 0;
                totNetSale = 0;
                gstDetList.clear();
                getGSTReport();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        //showDia(0);
        new Constant(GSTReportActivity.this).doFinish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.gstreportactivity_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //showDia(0);
                new Constant(GSTReportActivity.this).doFinish();
                break;
            case R.id.gstReport:
                if(!gstDetList.isEmpty()) {
                    showDia(1);
                }else{
                    toast.setText("Please Import Data First");
                    toast.show();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getGSTReport(){
        fromdate = tv_fdate.getText().toString();
        todate = tv_tdate.getText().toString();
        int custId = FirstActivity.pref.getInt(getString(R.string.pref_retailCustId), 0);
        String data = custId+"|"+fromdate+"|"+todate+"|"+"B";
        writeLog("getGSTReport_"+data);
        new GetGSDetail().execute(data);
    }

    private class GetGSDetail extends AsyncTask<String, Void, String> {

        private ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(GSTReportActivity.this);
            pd.setMessage("Please Wait...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... url) {
            String value = "";
            DefaultHttpClient httpClient = null;
            HttpPost request = new HttpPost(Constant.ipaddress + "/GetGSTDetail");
            request.setHeader("Accept", "application/json");
            request.setHeader("Content-type", "application/json");
            try {
                JSONStringer vehicle = new JSONStringer().object().key("rData").object().key("details").value(url[0]).endObject().endObject();
                Constant.showLog(vehicle.toString());
                writeLog("GetGSTDetail_"+vehicle.toString());
                StringEntity entity = new StringEntity(vehicle.toString());
                request.setEntity(entity);
                HttpParams httpParams = new BasicHttpParams();
                HttpConnectionParams.setConnectionTimeout(httpParams,Constant.TIMEOUT_CON);
                HttpConnectionParams.setSoTimeout(httpParams, Constant.TIMEOUT_SO);
                httpClient = new DefaultHttpClient(httpParams);
                HttpResponse response = httpClient.execute(request);
                Constant.showLog("Saving : " + response.getStatusLine().getStatusCode());
                value = new BasicResponseHandler().handleResponse(response);
            } catch (Exception e) {
                e.printStackTrace();
                writeLog("GetGSTDetail_result_" + e.getMessage());
            }
            finally {
                try{
                    if(httpClient!=null) {
                        httpClient.getConnectionManager().shutdown();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    writeLog("GetGSTDetail_finally_"+e.getMessage());
                }
            }
            return value;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                Constant.showLog(result);
                String str = new JSONObject(result).getString("GetGSTDetailResult");
                JSONArray jsonArray = new JSONArray(str);
                str = str.replace("\\", "");
                str = str.replace("\"", "");
                str = str.replace("''", "");
                Constant.showLog(str);
                if(jsonArray.length()>=1) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        GSTDetailClass gstDet = new GSTDetailClass();
                        gstDet.setInvNo(jsonArray.getJSONObject(i).getString("InvNo"));
                        gstDet.setInvDate(jsonArray.getJSONObject(i).getString("InvDate"));
                        partyName = jsonArray.getJSONObject(i).getString("ShopName");
                        gstDet.setShopName(partyName);
                        gstDet.setGSTNo(jsonArray.getJSONObject(i).getString("GSTNo"));
                        gstDet.setHSNCode(jsonArray.getJSONObject(i).getString("HSNCode"));
                        gstDet.setTax(jsonArray.getJSONObject(i).getString("Tax"));
                        gstDet.setQty(roundDecimals(jsonArray.getJSONObject(i).getString("Qty")));
                        gstDet.setUOM(jsonArray.getJSONObject(i).getString("UOM"));
                        gstDet.setAssesible_value(roundDecimals(jsonArray.getJSONObject(i).getString("Assesible_value")));
                        gstDet.setCGSTAmt(roundDecimals(jsonArray.getJSONObject(i).getString("CGSTAmt")));
                        gstDet.setSGSTAmt(roundDecimals(jsonArray.getJSONObject(i).getString("SGSTAmt")));
                        gstDet.setIGSTAmt(roundDecimals(jsonArray.getJSONObject(i).getString("IGSTAmt")));
                        gstDet.setRoundOff(roundDecimals(jsonArray.getJSONObject(i).getString("RoundOff")));
                        gstDet.setNetSale(roundDecimals(jsonArray.getJSONObject(i).getString("NetSale")));
                        gstDet.setType(jsonArray.getJSONObject(i).getString("Type"));
                        gstDet.setInvoiceNo(jsonArray.getJSONObject(i).getString("InvoiceNo"));
                        gstDet.setInvoiceDate(jsonArray.getJSONObject(i).getString("InvoiceDate"));
                        gstDetList.add(gstDet);
                        if (jsonArray.getJSONObject(i).getString("Type").equals("Invoice")) {
                            totQty = totQty + Float.parseFloat(jsonArray.getJSONObject(i).getString("Qty"));
                            totAssValue = totAssValue + Float.parseFloat(jsonArray.getJSONObject(i).getString("Assesible_value"));
                            totCGSTAmnt = totCGSTAmnt + Float.parseFloat(jsonArray.getJSONObject(i).getString("CGSTAmt"));
                            totSGSTAmnt = totSGSTAmnt + Float.parseFloat(jsonArray.getJSONObject(i).getString("SGSTAmt"));
                            totIGSTAmnt = totIGSTAmnt + Float.parseFloat(jsonArray.getJSONObject(i).getString("IGSTAmt"));
                            totRoundOff = totRoundOff + Float.parseFloat(jsonArray.getJSONObject(i).getString("RoundOff"));
                            totNetSale = totNetSale + Float.parseFloat(jsonArray.getJSONObject(i).getString("NetSale"));
                        } else {
                            totQty = totQty - Float.parseFloat(jsonArray.getJSONObject(i).getString("Qty"));
                            totAssValue = totAssValue - Float.parseFloat(jsonArray.getJSONObject(i).getString("Assesible_value"));
                            totCGSTAmnt = totCGSTAmnt - Float.parseFloat(jsonArray.getJSONObject(i).getString("CGSTAmt"));
                            totSGSTAmnt = totSGSTAmnt - Float.parseFloat(jsonArray.getJSONObject(i).getString("SGSTAmt"));
                            totIGSTAmnt = totIGSTAmnt - Float.parseFloat(jsonArray.getJSONObject(i).getString("IGSTAmt"));
                            totRoundOff = totRoundOff - Float.parseFloat(jsonArray.getJSONObject(i).getString("RoundOff"));
                            totNetSale = totNetSale - Float.parseFloat(jsonArray.getJSONObject(i).getString("NetSale"));
                        }
                    }
                }else{
                    toast.setText("No Record Available");
                    toast.show();
                }
                pd.dismiss();
                GSTReportAdapter adapter = new GSTReportAdapter(gstDetList,getApplicationContext());
                lv_gstdetail.setAdapter(adapter);
                tv_totQty.setText(roundDecimals(String.valueOf(totQty)));
                tv_totAssValue.setText(roundDecimals(String.valueOf(totAssValue)));
                tv_totCGSTAmnt.setText(roundDecimals(String.valueOf(totCGSTAmnt)));
                tv_totSGSTAmnt.setText(roundDecimals(String.valueOf(totSGSTAmnt)));
                tv_totIGSTAmnt.setText(roundDecimals(String.valueOf(totIGSTAmnt)));
                tv_totRoundOff.setText(roundDecimals(String.valueOf(totRoundOff)));
                tv_totNetSale.setText(roundDecimals(String.valueOf(totNetSale)));
            } catch (Exception e) {
                writeLog("GetGSTDetail_" + e.getMessage());
                e.printStackTrace();
                pd.dismiss();
            }
        }
    }

    private void init() {
        FirstActivity.pref = getSharedPreferences(FirstActivity.PREF_NAME,MODE_PRIVATE);
        btn_show = (AppCompatButton) findViewById(R.id.btn_show);
        tv_fdate = (TextView) findViewById(R.id.tv_fdate);
        tv_tdate = (TextView) findViewById(R.id.tv_tdate);
        tv_totQty = (TextView) findViewById(R.id.tv_totQty);
        tv_totAssValue = (TextView) findViewById(R.id.tv_totAssValue);
        tv_totCGSTAmnt = (TextView) findViewById(R.id.tv_totCGSTAmnt);
        tv_totSGSTAmnt = (TextView) findViewById(R.id.tv_totSGSTAmnt);
        tv_totIGSTAmnt = (TextView) findViewById(R.id.tv_totIGSTAmnt);
        tv_totRoundOff = (TextView) findViewById(R.id.tv_totRoundOff);
        tv_totNetSale = (TextView) findViewById(R.id.tv_totNetSale);
        lv_gstdetail = (ListView) findViewById(R.id.lv_gstdetail);
        constant = new Constant(GSTReportActivity.this);
        constant1 = new Constant(getApplicationContext());
        toast = Toast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        gstDetList = new ArrayList<>();

        flt_price = new DecimalFormat();
        flt_price.setMaximumFractionDigits(2);

        day = cal.get(Calendar.DAY_OF_MONTH);
        month = cal.get(Calendar.MONTH);
        year = cal.get(Calendar.YEAR);

        btn_show.setOnClickListener(this);

        tv_fdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(fdt);
            }
        });

        tv_tdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(tdt);
            }
        });

        todate = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH).format(cal.getTime());
        fromdate = "01/"+(month + 1)+"/"+year;
        try {
            tv_fdate.setText(sdf1.format(sdf.parse(fromdate)));
            tv_tdate.setText(sdf1.format(sdf.parse(todate)));
        } catch (Exception e) {
            e.printStackTrace();
            writeLog("init_"+e.getMessage());
        }
    }

    private void showDia(int a) {
        AlertDialog.Builder builder = new AlertDialog.Builder(GSTReportActivity.this);
        builder.setCancelable(false);
        if (a == 0) {
            builder.setMessage("Do You Want To Exit App?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    new Constant(GSTReportActivity.this).doFinish();
                    dialog.dismiss();
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        } else if (a == 1) {
            builder.setMessage("Do You Want To Report On Mail?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    exportToExcel();
                    dialog.dismiss();
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        }
        builder.create().show();
    }

    private void writeLog(String _data) {
        new WriteLog().writeLog(getApplicationContext(), "GSTReportActivity_" + _data);
    }

    private String roundDecimals(String d) {
        DecimalFormat twoDForm = new DecimalFormat("#.##");
        return twoDForm.format(Double.parseDouble(d));
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case fdt:
                return new DatePickerDialog(this, fdate, year, month, day);

            case tdt:
                return new DatePickerDialog(this, tdate, year, month, day);

        }
        return null;
    }

    DatePickerDialog.OnDateSetListener fdate = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
            try {
                Date date = sdf.parse(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                tv_fdate.setText(sdf1.format(date));
                Constant.showLog("tv_fdate:" + sdf1.format(date));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    DatePickerDialog.OnDateSetListener tdate = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
            try {
                Date date = sdf.parse(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                tv_tdate.setText(sdf1.format(date));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    private void exportToExcel(){
        long datetime = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("dd_MMM_yyyy_hh_mm_ss", Locale.ENGLISH);
        Date resultdate = new Date(datetime);
        String cur_date = sdf.format(resultdate);
        exportFileName = "GSTReport_"+cur_date + ".xls";
        writeLog("exportToExcel_"+exportFileName);
        new writeFile().execute(exportFileName);
    }

    private class writeFile extends AsyncTask<String, Void, String> {

        private ProgressDialog pd1;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd1 = new ProgressDialog(GSTReportActivity.this);
            pd1.setMessage("Exporting Data Please Wait...");
            pd1.setCancelable(false);
            pd1.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String status = "";
            try {
                File directory = Constant.checkFolder(Constant.folder_name+"/"+Constant.gstFolderName);
                if (!directory.exists()) {
                    if (directory.mkdirs()) {
                        Constant.showLog("TAG");
                    }
                }
                File outputFile = new File(directory, strings[0]);
                Workbook wb = new HSSFWorkbook();
                CellStyle cs = wb.createCellStyle();
                Font font = wb.createFont();
                font.setBoldweight(Font.BOLDWEIGHT_BOLD);
                cs.setFont(font);
                writeLog("writeFile_exportGSTReport_called");
                exportGSTReport(wb,cs);
                FileOutputStream os = new FileOutputStream(outputFile);
                wb.write(os);
                os.close();
                writeLog("writeFile_Success");
            } catch (Exception e) {
                status = null;
                e.printStackTrace();
                writeLog("writeFile_"+e.getMessage());
            }
            return status;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd1.dismiss();
            if (result != null) {
                shareFile();
                Constant.showLog("Report Exported Successfully");
                toast.setText("Report Exported Successfully");
                toast.show();
            } else {
                writeLog("Error_While_Exporting_Data");
                toast.setText("Error While Exporting Data. Please Try Again");
                toast.show();
            }
        }
    }

    private void shareFile() {
        try {
            Intent intentShareFile = new Intent(Intent.ACTION_SEND);
            String myFilePath = Constant.checkFolder(Constant.folder_name).getAbsolutePath() + File.separator +
                    Constant.gstFolderName + File.separator + exportFileName;
            Constant.showLog("file path:" + myFilePath);

            File f = new File(myFilePath);
            Uri photoURI = FileProvider.getUriForFile(getApplicationContext(), getApplicationContext().getPackageName()
                    + ".provider", f);
            intentShareFile.setType("application/vnd.ms-excel");
            intentShareFile.putExtra(Intent.EXTRA_STREAM, photoURI);
            intentShareFile.putExtra(Intent.EXTRA_SUBJECT, partyName + " - " + fromdate + " - " + todate + " GST Report");
            startActivity(Intent.createChooser(intentShareFile, "Share File"));
        }catch (Exception e){
            e.printStackTrace();
            writeLog("shareFile_"+e.getMessage());
            toast.setText("Something Went Wrong");
            toast.show();
        }
    }

    private void exportGSTReport(Workbook wb, CellStyle cs){

        Constant.showLog("exportGSTReport");
        Sheet sheet = wb.createSheet("GST REPORT");

        String arr[] = new String[]{"InvDate","InvNo","HSNCode","Tax%","Qty","Assesible Value",
                "CGSTAmnt","SGSTAmnt","IGSTAmnt","RoundOff","NetSale","Type","Invoice No","Invoice Date"};

        String compName = "";
        int hocode = FirstActivity.pref.getInt(getString(R.string.pref_hocode), 0);
        if (hocode == 1) {
            compName = "MADHUR SALES PRIVATE LIMITED - PUNE DIVISION";
        } else if (hocode == 12) {
            compName = "MADHUR SALES PRIVATE LIMITED - KARAD DIVISION";
        } else if (hocode == 13) {
            compName = "MADHUR SALES PRIVATE LIMITED - AHMEDNAGAR DIVISION";
        }

        CellStyle style = wb.createCellStyle();
        Font font = wb.createFont();
        font.setColor(HSSFColor.BLUE.index);
        style.setFont(font);

        Row rowHeader = sheet.createRow(0);
        Cell cellHeader = rowHeader.createCell(1);
        cellHeader.setCellValue(compName);
        cellHeader.setCellStyle(style);

        sheet.addMergedRegion(new CellRangeAddress(
                0, //first row (0-based)
                0, //last row  (0-based)
                1, //first column (0-based)
                6  //last column  (0-based)
        ));

        cellHeader = rowHeader.createCell(8);
        cellHeader.setCellValue("GSTNO");
        cellHeader.setCellStyle(style);

        sheet.addMergedRegion(new CellRangeAddress(
                0, //first row (0-based)
                0, //last row  (0-based)
                8, //first column (0-based)
                8  //last column  (0-based)
        ));

        cellHeader = rowHeader.createCell(9);
        cellHeader.setCellValue("27AAJCM4570B1ZK");
        cellHeader.setCellStyle(style);

        sheet.addMergedRegion(new CellRangeAddress(
                0, //first row (0-based)
                0, //last row  (0-based)
                9, //first column (0-based)
                10  //last column  (0-based)
        ));

        Row row = sheet.createRow(1);
        for(int i=0;i<arr.length;i++){
            Cell c = row.createCell(i);
            c.setCellValue(arr[i]);
            c.setCellStyle(cs);
        }

        int count = 1;

        for(int i=0;i<=gstDetList.size();i++){
            count++;
            Row row1 = sheet.createRow(count);
            if(i<gstDetList.size()) {
                GSTDetailClass gstDet = gstDetList.get(i);
                DataFormat format = wb.createDataFormat();
                for (int j = 0; j < arr.length; j++) {
                    Cell cell = row1.createCell(j);
                    if (j == 0) {
                        cell.setCellValue(gstDet.getInvDate());
                    } else if (j == 1) {
                        cell.setCellValue(gstDet.getInvNo());
                    } else if (j == 2) {
                        cell.setCellValue(gstDet.getHSNCode());
                    } else if (j == 3) {
                        cell.setCellType(Cell.CELL_TYPE_NUMERIC);
                        cell.setCellValue(gstDet.getTax());
                    } else if (j == 4) {
                        cell.setCellValue(Double.parseDouble(gstDet.getQty()));
                    } else if (j == 5) {
                        cell.setCellValue(Double.parseDouble(gstDet.getAssesible_value()));
                    } else if (j == 6) {
                        cell.setCellValue(Double.parseDouble(gstDet.getCGSTAmt()));
                    } else if (j == 7) {
                        cell.setCellValue(Double.parseDouble(gstDet.getSGSTAmt()));
                    } else if (j == 8) {
                        cell.setCellValue(Double.parseDouble(gstDet.getIGSTAmt()));
                    } else if (j == 9) {
                        cell.setCellValue(Double.parseDouble(gstDet.getRoundOff()));
                    } else if (j == 10) {
                        cell.setCellValue(Double.parseDouble(gstDet.getNetSale()));
                    } else if (j == 11) {
                        cell.setCellValue(gstDet.getType());
                    } else if (j == 12) {
                        cell.setCellValue(gstDet.getInvoiceNo());
                    } else if (j == 13) {
                        cell.setCellValue(gstDet.getInvoiceDate());
                    }
                }
            }else {
                for (int j = 0; j < arr.length; j++) {
                    Cell cell = row1.createCell(j);
                    if (j == 4) {
                        cell.setCellType(Cell.CELL_TYPE_NUMERIC);
                        cell.setCellValue(Double.parseDouble(roundDecimals(String.valueOf(totQty))));
                    } else if (j == 5) {
                        cell.setCellType(Cell.CELL_TYPE_NUMERIC);
                        cell.setCellValue(Double.parseDouble(roundDecimals(String.valueOf(totAssValue))));
                    } else if (j == 6) {
                        cell.setCellType(Cell.CELL_TYPE_NUMERIC);
                        cell.setCellValue(Double.parseDouble(roundDecimals(String.valueOf(totCGSTAmnt))));
                    } else if (j == 7) {
                        cell.setCellType(Cell.CELL_TYPE_NUMERIC);
                        cell.setCellValue(Double.parseDouble(roundDecimals(String.valueOf(totSGSTAmnt))));
                    } else if (j == 8) {
                        cell.setCellType(Cell.CELL_TYPE_NUMERIC);
                        cell.setCellValue(Double.parseDouble(roundDecimals(String.valueOf(totIGSTAmnt))));
                    } else if (j == 9) {
                        cell.setCellType(Cell.CELL_TYPE_NUMERIC);
                        cell.setCellValue(Double.parseDouble(roundDecimals(String.valueOf(totRoundOff))));
                    } else if (j == 10) {
                        cell.setCellType(Cell.CELL_TYPE_NUMERIC);
                        cell.setCellValue(Double.parseDouble(roundDecimals(String.valueOf(totNetSale))));
                    }
                }
            }
        }
        Constant.showLog(""+count);
        writeLog("writeFile_exportGSTReport_called");
    }

}
