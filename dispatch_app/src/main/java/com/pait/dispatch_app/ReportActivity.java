package com.pait.dispatch_app;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.pait.dispatch_app.adapters.LedgerReportAdapter;
import com.pait.dispatch_app.constant.Constant;
import com.pait.dispatch_app.db.DBHandler;
import com.pait.dispatch_app.interfaces.RetrofitApiInterface;
import com.pait.dispatch_app.log.WriteLog;
import com.pait.dispatch_app.model.DispatchMasterClass;
import com.pait.dispatch_app.parse.UserClass;
import com.pait.dispatch_app.utility.RetrofitApiBuilder;

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
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReportActivity extends AppCompatActivity implements View.OnClickListener{

    private Toast toast;
    private ListView lv_ledger;
    private TextView tv_fdate, tv_tdate, tot_clb, tot_ob, tot_credit, tot_debit, tv_outstanding;
    private CheckBox cb_all;
    private SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
    private int day, month, year, hoCode, dpID, empId, custCode = 0, totQty = 0, flag = 0, designId = 0;
    private Calendar cal = Calendar.getInstance();
    private static final int fdt = 1, tdt = 2;
    private String fromdate = "", todate = "", all = "", exportFileName;
    private double total_op = 0, total_cl = 0, total_debit = 0, total_credit = 0;
    private DecimalFormat flt_price;
    private AppCompatButton btn_view_ledger,btn_view_out,btn_view_credit;
    private UserClass userClass;
    private List<DispatchMasterClass> exportList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(Constant.liveTestFlag==1) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }

        setContentView(R.layout.activity_report);

        init();
        userClass = (UserClass) getIntent().getExtras().get("cust");
        empId = userClass.getCustID();
        hoCode = userClass.getHOCode();
        dpID = userClass.getDpId();
        designId = FirstActivity.pref.getInt(getString(R.string.pref_design), 0);

        todate = sdf1.format(cal.getTime());
        Constant.showLog("todate_dafault-" + todate);
        tv_tdate.setText(todate);
        int day = 1;
        cal.set(year, month, day);
        Constant.showLog("First Day of month: " + cal.getTime());
        fromdate = sdf1.format(cal.getTime());
        Constant.showLog("fromdate_dafault-" + fromdate);
        tv_fdate.setText(fromdate);

        btn_view_ledger.setOnClickListener(this);
        btn_view_out.setOnClickListener(this);
        btn_view_credit.setOnClickListener(this);

        tv_fdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(fdt);
                cb_all.setChecked(false);
            }
        });

        tv_tdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(tdt);
                cb_all.setChecked(false);
            }
        });

        cb_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //visibility();
                // tv_fdate.setEnabled(false);
                // tv_tdate.setEnabled(false);
            }
        });

        getDispatchMaster(2);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case 0:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        showDia(4);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.reportactivity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.report:
                showDia(3);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getDispatchMaster(int type) {
        final Constant constant = new Constant(ReportActivity.this);
        constant.showPD();
        try {
            int maxAuto = new DBHandler(getApplicationContext()).getMaxAuto();
            //Auto + "|"+ CustId + "|"+ HOCode + "|"+ dispatchId + "|"+ empId + "|"+ type
            String url = maxAuto + "|" + 0 + "|" + hoCode + "|" + dpID + "|" + empId + "|" + type + "|" + designId;
            writeLog("getDispatchMaster_" + url);
            final JSONObject jsonBody = new JSONObject();
            jsonBody.put("details", url);
            RequestBody body = RequestBody.create(okhttp3.MediaType.
                    parse("application/json; charset=utf-8"), (jsonBody).toString());
            Constant.showLog(jsonBody.toString());

            Call<List<DispatchMasterClass>> call = new RetrofitApiBuilder().getApiBuilder().
                    create(RetrofitApiInterface.class).
                    getDispatchMaster(body);
            call.enqueue(new Callback<List<DispatchMasterClass>>() {
                @Override
                public void onResponse(Call<List<DispatchMasterClass>> call, Response<List<DispatchMasterClass>> response) {
                    Constant.showLog("onResponse");
                    List<DispatchMasterClass> list = response.body();
                    if (list != null) {
                        total_debit = 0;
                        for(DispatchMasterClass dp : list){
                            total_debit = total_debit + Integer.parseInt(dp.getTotalQty());
                        }
                        tot_debit.setText(String.valueOf(total_debit));
                        LedgerReportAdapter adapter = new LedgerReportAdapter(list, getApplicationContext());
                        lv_ledger.setAdapter(adapter);
                        Constant.showLog(list.size() + "_getDispatchMaster");
                        exportList = response.body();
                    } else {
                        Constant.showLog("onResponse_list_null");
                        writeLog("getDispatchMaster_onResponse_list_null");
                    }
                    constant.showPD();
                }

                @Override
                public void onFailure(Call<List<DispatchMasterClass>> call, Throwable t) {
                    Constant.showLog("onFailure");
                    if (!call.isCanceled()) {
                        call.cancel();
                    }
                    t.printStackTrace();
                    writeLog("getDispatchMaster_onFailure_" + t.getMessage());
                    constant.showPD();
                    showDia(2);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            writeLog("getDispatchMaster_" + e.getMessage());
            constant.showPD();
            showDia(2);
        }
    }

    private void init() {
        FirstActivity.pref = getSharedPreferences(FirstActivity.PREF_NAME,MODE_PRIVATE);
        btn_view_ledger = findViewById(R.id.btn_view_ledger);
        btn_view_out = findViewById(R.id.btn_view_out);
        btn_view_credit = findViewById(R.id.btn_view_credit);
        tv_outstanding = findViewById(R.id.tv_outstanding);
        tv_fdate = findViewById(R.id.tv_fdate);
        tv_tdate = findViewById(R.id.tv_tdate);
        lv_ledger = findViewById(R.id.lv_ledger);
        tot_clb = findViewById(R.id.tot_clb);
        tot_ob = findViewById(R.id.tot_ob);
        tot_credit = findViewById(R.id.tot_credit);
        tot_debit = findViewById(R.id.tot_debit);
        cb_all = findViewById(R.id.cb_all);
        toast = Toast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        flt_price = new DecimalFormat();
        flt_price.setMaximumFractionDigits(2);
        day = cal.get(Calendar.DAY_OF_MONTH);
        month = cal.get(Calendar.MONTH);
        year = cal.get(Calendar.YEAR);
        cb_all.setChecked(false);
    }

    private void showDia(int id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if (id == 0) {
            builder.setMessage("Do you want to exit?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    finish();
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
        } else if (id == 1) {
            builder.setMessage("Data Loaded Successfully");
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        } else if (id == 2) {
            builder.setMessage("Error While Loading Data?");
            builder.setPositiveButton("Try again", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        } else if (id == 3) {
            builder.setMessage("Do You Want To Send Report?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    exportToExcel();
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        }
        if (id == 4) {
            builder.setMessage("Do You Want To Go Back ?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    new Constant(ReportActivity.this).doFinish();
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

    private void exportToExcel() {
        int hoCode = FirstActivity.pref.getInt(getString(R.string.pref_hocode), 0);
        int dpId = FirstActivity.pref.getInt(getString(R.string.pref_dpId), 0);
        String init = new DBHandler(getApplicationContext()).getDPINIT(hoCode,dpId);
        long datetime = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("dd_MMM_yyyy_hh_mm_ss", Locale.ENGLISH);
        Date resultdate = new Date(datetime);
        String cur_date = sdf.format(resultdate);
        exportFileName = init+"_"+total_debit+"_" + cur_date + ".xls";
        writeLog("exportToExcel_" + exportFileName);
        new writeFile().execute(exportFileName);
    }

    private class writeFile extends AsyncTask<String, Void, String> {

        private ProgressDialog pd1;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd1 = new ProgressDialog(ReportActivity.this);
            pd1.setMessage("Exporting Data Please Wait...");
            pd1.setCancelable(false);
            pd1.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String status = "";
            try {
                File directory = Constant.checkFolder(Constant.folder_name + "/" + Constant.gstFolderName);
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
                writeLog("writeFile_exportReport_called");
                exportDispatchReport(wb, cs);
                FileOutputStream os = new FileOutputStream(outputFile);
                wb.write(os);
                os.close();
                writeLog("writeFile_Success");
            } catch (Exception e) {
                status = null;
                e.printStackTrace();
                writeLog("writeFile_" + e.getMessage());
            }
            return status;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd1.dismiss();
            if (result != null) {
                shareFile();
                flag = 0;
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
            //intentShareFile.putExtra(Intent.EXTRA_SUBJECT, partyName + " - " + fromdate + " - " + todate + " GST Report");
            intentShareFile.putExtra(Intent.EXTRA_SUBJECT, " Order Dispatch Allotment Report");
            startActivity(Intent.createChooser(intentShareFile, "Share File"));
        } catch (Exception e) {
            e.printStackTrace();
            writeLog("shareFile_" + e.getMessage());
            toast.setText("Something Went Wrong");
            toast.show();
        }
    }

    private void exportDispatchReport(Workbook wb, CellStyle cs) {

        Constant.showLog("exportGSTReport");
        Sheet sheet = wb.createSheet("Order Dispatch Allotment Report");

        String arr[] = new String[]{"PartyName", "Order No", "Dispatch Date", "Order Qty", "Transporter", "Dispatch By",};

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

        /*sheet.addMergedRegion(new CellRangeAddress(
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
        cellHeader.setCellStyle(style);*/

        sheet.addMergedRegion(new CellRangeAddress(
                0, //first row (0-based)
                0, //last row  (0-based)
                9, //first column (0-based)
                10  //last column  (0-based)
        ));

        Row row = sheet.createRow(1);
        for (int i = 0; i < arr.length; i++) {
            Cell c = row.createCell(i);
            c.setCellValue(arr[i]);
            c.setCellStyle(cs);
        }

        int count = 1;

        for (int i = 0; i <= exportList.size(); i++) {
            count++;
            Row row1 = sheet.createRow(count);
            if (i < exportList.size()) {
                DispatchMasterClass gstDet = exportList.get(i);
                DataFormat format = wb.createDataFormat();
                for (int j = 0; j < arr.length; j++) {
                    Cell cell = row1.createCell(j);
                    if (j == 0) {
                        cell.setCellValue(gstDet.getPartyName());
                    } else if (j == 1) {
                        cell.setCellValue(gstDet.getPONO());
                    } else if (j == 2) {
                        cell.setCellValue(gstDet.getDispatchDate());
                    } else if (j == 3) {
                        cell.setCellValue(Double.parseDouble(gstDet.getTotalQty()));
                        totQty = totQty + Integer.parseInt(gstDet.getTotalQty());
                    } else if (j == 4) {
                        cell.setCellValue(gstDet.getTransporter());
                    } else {
                        cell.setCellValue(gstDet.getEmp_Name());
                    }
                }
            } else {
                for (int j = 0; j < arr.length; j++) {
                    Cell cell = row1.createCell(j);
                    if (j == 3) {
                        cell.setCellType(Cell.CELL_TYPE_NUMERIC);
                        cell.setCellValue(Double.parseDouble(String.valueOf(totQty)));
                    }
                }
            }
        }
        Constant.showLog("" + count);
        writeLog("writeFile_exportGSTReport_called");
    }

    private void writeLog(String _data) {
        new WriteLog().writeLog(getApplicationContext(), "ReportActivity_" + _data);
    }
}
