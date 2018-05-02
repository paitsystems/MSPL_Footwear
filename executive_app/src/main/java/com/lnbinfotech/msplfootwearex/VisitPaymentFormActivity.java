package com.lnbinfotech.msplfootwearex;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.lnbinfotech.msplfootwearex.adapters.InvoiceDetailForPaymentAdapter;
import com.lnbinfotech.msplfootwearex.connectivity.ConnectivityTest;
import com.lnbinfotech.msplfootwearex.constant.Constant;
import com.lnbinfotech.msplfootwearex.interfaces.PaymentTotalInterface;
import com.lnbinfotech.msplfootwearex.interfaces.ServerCallbackList;
import com.lnbinfotech.msplfootwearex.log.WriteLog;
import com.lnbinfotech.msplfootwearex.model.ChequeDetailsClass;
import com.lnbinfotech.msplfootwearex.model.OuststandingReportClass;
import com.lnbinfotech.msplfootwearex.model.VisitPaymentFormClass;
import com.lnbinfotech.msplfootwearex.volleyrequests.VolleyRequests;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.util.ArrayList;
import java.util.List;

public class VisitPaymentFormActivity extends AppCompatActivity implements View.OnClickListener, PaymentTotalInterface {

    private EditText ed_amount, ed_cus_name;
    private RadioButton rdo_cash, rdo_cheque, rdo_other;
    private LinearLayout add_cash_lay, add_cheque_lay, add_other_lay, list_lay, cash_lay;
    private String auto_type;
    private AppCompatButton btn_save, btn_showBill;
    public static VisitPaymentFormClass visit;
    private int total_amt = 0;
    private Toast toast;
    private String custName = "", custId = "0";
    private ListView lv_out;
    private Constant constant;
    public static int total = 0, totBal = 0, totPaid = 0, totAlloc = 0, isChequeDataSaved = 0, isCurrencyDataSaved = 0;
    private TextView tv_temant, tv_tbal, tv_paid, tv_talloc;
    private List<OuststandingReportClass> invlist, orgList;
    public static List<ChequeDetailsClass> ls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(Constant.liveTestFlag==1) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }

        setContentView(R.layout.activity_visit_payment_form);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.payment);
        }
        try {
            custId = getIntent().getExtras().getString("cust_id");
            custName = getIntent().getExtras().getString("child_selected");
        } catch (Exception e) {
            e.printStackTrace();
            writeLog("onCreate_" + e.getMessage());
        }
        init();
        ed_cus_name.setText(custName);
        ed_cus_name.setEnabled(false);
        get_auto_cuslist();

        if(ConnectivityTest.getNetStat(getApplicationContext())){
            totBal = 0;
            showOutstandingReport();
        }else{
            toast.setText("You Are Offline");
            toast.show();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_save:
                if (rdo_cash.isChecked()) {
                    savePaymentCash();
                } else if (rdo_cheque.isChecked()) {
                    savePaymentCheque();
                }else if (rdo_other.isChecked()) {
                    savePaymentOther();
                }
                break;
            case R.id.btn_showbill:
                ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(btn_showBill.getWindowToken(), 0);
                /*if (rdo_cash.isChecked()) {
                    if (!ed_amount.getText().toString().equals("")) {
                        total = Integer.parseInt(ed_amount.getText().toString());
                        ed_amount.setError(null);
                        showOutstandingReport();
                    } else {
                        ed_amount.requestFocus();
                        ed_amount.setError("Required");
                        toast.setText("Please Enter Amount");
                        toast.show();
                    }
                } else {
                    if (ed_amount.getText().toString().equals("")) {
                        toast.setText("Please Enter Amount");
                        toast.show();
                    } else {
                        showOutstandingReport();
                    }
                }*/
                totBal = 0;
                showOutstandingReport();
                break;
            case R.id.add_cheque_lay:
                setAmount();
                //cheque_button_validation();
                startCheqActivity();
                break;
            case R.id.add_other_lay:
                startOtherActivity();
                //finish();
                break;
            case R.id.add_cash_lay:
                startCashActivity();
                //finish();
                break;
            case R.id.ed_cus_name:
                Intent i = new Intent(VisitPaymentFormActivity.this, SelectAutoItemActivity.class);
                auto_type = "cus";
                i.putExtra("Auto_type", auto_type);
                startActivity(i);
                overridePendingTransition(R.anim.enter, R.anim.exit);
                writeLog("goes to SelectAutoItemActivity");
                break;
            case R.id.rdo_cash:
                if (totBal == 0) {
                    setrdoCash();
                } else {
                    //showPopup(2);
                    startCashActivity();
                }
                break;
            case R.id.rdo_cheque:
                if (totBal == 0) {
                    setrdoCheque();
                } else {
                    //showPopup(3);
                    startCheqActivity();
                }
                break;
            case R.id.rdo_other:
                if (totBal == 0) {
                    setrdoOther();
                } else {
                    //showPopup(4);
                    startOtherActivity();
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        //showPopup(1);
        new Constant(VisitPaymentFormActivity.this).doFinish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                new Constant(VisitPaymentFormActivity.this).doFinish();
                //showPopup(1);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isChequeDataSaved == 1) {
            isChequeDataSaved = 0;
            //lv_out.setAdapter(null);
            cash_lay.setVisibility(View.VISIBLE);
            ed_amount.setText(String.valueOf(total));
            tv_temant.setText(String.valueOf(total));
            tv_paid.setText(String.valueOf(total));
            //add_cheque_lay.setVisibility(View.VISIBLE);
            //add_other_lay.setVisibility(View.GONE);
            //add_cash_lay.setVisibility(View.GONE);
        }else if (isChequeDataSaved == 2) {
            isChequeDataSaved = 0;
            //lv_out.setAdapter(null);
            cash_lay.setVisibility(View.VISIBLE);
            ed_amount.setText(String.valueOf(total));
            tv_temant.setText(String.valueOf(total));
            tv_paid.setText(String.valueOf(total));
            //add_cheque_lay.setVisibility(View.GONE);
            //add_other_lay.setVisibility(View.VISIBLE);
            //add_cash_lay.setVisibility(View.GONE);
        }
        if(isCurrencyDataSaved==1){
            isCurrencyDataSaved = 0;
            //lv_out.setAdapter(null);
            cash_lay.setVisibility(View.VISIBLE);
            ed_amount.setText(String.valueOf(total));
            tv_temant.setText(String.valueOf(total));
            tv_paid.setText(String.valueOf(total));
            //add_cheque_lay.setVisibility(View.GONE);
            //add_other_lay.setVisibility(View.GONE);
            //add_cash_lay.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void totalsCalculation(Object obj) {
        String str = (String) obj;
        if (!str.equals("0")) {
            tv_temant.setText(String.valueOf(total));
            tv_tbal.setText(String.valueOf(totBal));
            tv_paid.setText(String.valueOf(totPaid));
            totAlloc = total - totPaid;
            tv_talloc.setText(String.valueOf(totAlloc));
        } else {
            toast.setText("Can Not Select Invoice");
            toast.show();
        }
    }

    private void init() {
        toast = Toast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        constant = new Constant(VisitPaymentFormActivity.this);
        ed_amount = (EditText) findViewById(R.id.ed_amount);
        ed_cus_name = (EditText) findViewById(R.id.ed_cus_name);
        btn_save = (AppCompatButton) findViewById(R.id.btn_save);
        btn_showBill = (AppCompatButton) findViewById(R.id.btn_showbill);
        rdo_cash = (RadioButton) findViewById(R.id.rdo_cash);
        rdo_cheque = (RadioButton) findViewById(R.id.rdo_cheque);
        rdo_other = (RadioButton) findViewById(R.id.rdo_other);
        add_cheque_lay = (LinearLayout) findViewById(R.id.add_cheque_lay);
        cash_lay = (LinearLayout) findViewById(R.id.cash_lay);
        list_lay = (LinearLayout) findViewById(R.id.list_lay);
        add_other_lay = (LinearLayout) findViewById(R.id.add_other_lay);
        add_cash_lay = (LinearLayout) findViewById(R.id.add_cash_lay);
        lv_out = (ListView) findViewById(R.id.lv_out);
        ed_cus_name.setOnClickListener(this);
        rdo_cash.setOnClickListener(this);
        rdo_cheque.setOnClickListener(this);
        rdo_other.setOnClickListener(this);
        add_cheque_lay.setOnClickListener(this);
        add_other_lay.setOnClickListener(this);
        add_cash_lay.setOnClickListener(this);
        btn_save.setOnClickListener(this);
        btn_showBill.setOnClickListener(this);
        tv_temant = (TextView) findViewById(R.id.tv_teamnt);
        tv_tbal = (TextView) findViewById(R.id.tv_tbal);
        tv_paid = (TextView) findViewById(R.id.tv_tpaid);
        tv_talloc = (TextView) findViewById(R.id.tv_talloc);
        visit = new VisitPaymentFormClass();
        invlist = new ArrayList<>();
        orgList = new ArrayList<>();
        ls = new ArrayList<>();
        FirstActivity.pref = getSharedPreferences(FirstActivity.PREF_NAME,MODE_PRIVATE);
        //cheque = new ChequeDetailsGetterSetter();
        //setAmount();
    }

    private void showPopup(int id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        if (id == 0) {
            builder.setMessage("Do you want to delete cheque amount?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    //ls.clear();
                    //lv_show_chq_detail.setAdapter(null);
                    list_lay.setVisibility(View.GONE);
                    add_cheque_lay.setVisibility(View.VISIBLE);
                    //ChequeDetailsActivity.chequeDetails.setChq_det_amt(null);
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
        } else if (id == 1) {
            builder.setMessage("Do you want to clear cheque details");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    setDefault();
                    new Constant(VisitPaymentFormActivity.this).doFinish();
                    // finish();
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    rdo_cheque.setChecked(true);
                    rdo_cash.setChecked(false);
                    rdo_other.setChecked(false);
                    dialogInterface.dismiss();
                }
            });
        } else if (id == 2) {
            builder.setMessage("Do You Want To Discard Changes?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    setrdoCash();
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    rdo_cash.setChecked(false);
                }
            });
        } else if (id == 3) {
            builder.setMessage("Do You Want To Discard Changes?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    setrdoCheque();
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    rdo_cheque.setChecked(false);
                }
            });
        } else if (id == 4) {
            builder.setMessage("Do You Want To Discard Changes?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    setrdoOther();
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    rdo_other.setChecked(false);
                }
            });
        } else if (id == 5) {
            builder.setMessage("No Records Available");
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });

        } else if (id == 6) {
            builder.setMessage("Payment Details Saved Successfully");
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    setDefault();
                    dialog.dismiss();

                }
            });
        } else if (id == 7) {
            builder.setMessage("Error While Saving Payment Details");
            builder.setPositiveButton("Try Again", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if(rdo_cash.isChecked()){
                        savePaymentCash();
                    }else if(rdo_cheque.isChecked()){
                        savePaymentCheque();
                    }else if(rdo_other.isChecked()){
                        savePaymentOther();
                    }
                    dialog.dismiss();
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        }
        builder.create().show();
    }

    private void get_auto_cuslist() {
        Constant.showLog("cus: " + visit.getCustomer_name());
    }

    private void setAmount() {
        String amt = ed_amount.getText().toString();
        visit.setAmount(amt);
        Constant.showLog("amount: " + visit.getAmount());
    }

    private void cheque_button_validation() {
        if (ed_amount.getText().toString().equals("")) {
            Toast toast = Toast.makeText(getApplicationContext(), "Please enter amount", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        } else {
            Intent intent = new Intent(VisitPaymentFormActivity.this, ChequeDetailsActivity.class);
            startActivity(intent);
            writeLog("goes to ChequeDetailsActivity");
            //finish();
        }
    }

    private void cheque_amount_missmatch() {
        String amt = String.valueOf(VisitPaymentFormActivity.total);
        total_amt = total_amt + Integer.parseInt(amt);
        Constant.showLog("total_amt:" + total_amt);
        String total = String.valueOf(total_amt);
        String totaled_amount = visit.getAmount();
        Constant.showLog("totaled_amount:" + totaled_amount);
        if (!total.equals(totaled_amount)) {
            toast.setText("Cheque amount missmatch,Please enter total amount");
            toast.show();
        } else {
            toast.setText("Data saved successfully");
            toast.show();
        }
    }

    private void validation() {
        if (ed_amount.getText().toString().equals("")) {
            Toast toast = Toast.makeText(getApplicationContext(), "Please enter amount", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        } else {
            cheque_amount_missmatch();
        }
    }

    private void showOutstandingReport() {
        if (ConnectivityTest.getNetStat(VisitPaymentFormActivity.this)) {
            try {
                constant = new Constant(VisitPaymentFormActivity.this);
                lv_out.setAdapter(null);
                String url = Constant.ipaddress + "/GetInvoiceDet?custid=" + custId;
                Constant.showLog(url);
                writeLog("showOutstandingReport" + url);
                constant.showPD();
                VolleyRequests requests = new VolleyRequests(VisitPaymentFormActivity.this);
                requests.loadOuststndReport(url, new ServerCallbackList() {
                    @Override
                    public void onSuccess(Object result) {
                        constant.showPD();
                        //totBal = 0;
                        totPaid = 0;
                        totAlloc = 0;
                        totalsCalculation("");
                        invlist.clear();
                        orgList.clear();
                        invlist = (List<OuststandingReportClass>) result;
                        orgList = invlist;
                        if (invlist.size() != 0) {
                            InvoiceDetailForPaymentAdapter adapter = new InvoiceDetailForPaymentAdapter(invlist, getApplicationContext());
                            adapter.initInterface(VisitPaymentFormActivity.this);
                            lv_out.setAdapter(adapter);
                            tv_tbal.setText(String.valueOf(totBal));
                        } else {
                            toast.setText("No Record Available");
                            toast.show();
                        }
                    }

                    @Override
                    public void onFailure(Object result) {
                        constant.showPD();
                        showPopup(5);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                constant.showPD();
                showPopup(2);
                writeLog("showOutstandingReport" + e.getMessage());
            }
        } else {
            toast.setText("You Are Offline");
            toast.show();
        }
    }

    private void discardChanges() {
        ed_amount.setText(null);
        total = 0;
        totBal = 0;
        totPaid = 0;
        totAlloc = 0;
        totalsCalculation("");
        //lv_out.setAdapter(null);
        invlist.clear();
        orgList.clear();
        //VisitPaymentFormActivity.ls.clear();
        //lv_out.setAdapter(new InvoiceDetailForPaymentAdapter(orgList, getApplicationContext()));
    }

    private void setrdoCash() {
        discardChanges();
        isChequeDataSaved = 0;
        add_cash_lay.setVisibility(View.VISIBLE);
        add_cheque_lay.setVisibility(View.GONE);
        add_other_lay.setVisibility(View.GONE);
        rdo_cash.setChecked(true);
        rdo_cheque.setChecked(false);
        rdo_other.setChecked(false);
        cash_lay.setVisibility(View.VISIBLE);
        add_cheque_lay.setVisibility(View.GONE);
    }

    private void setrdoCheque() {
        discardChanges();
        rdo_cash.setChecked(false);
        rdo_other.setChecked(false);
        rdo_cheque.setChecked(true);
        cash_lay.setVisibility(View.GONE);
        add_cash_lay.setVisibility(View.GONE);
        add_other_lay.setVisibility(View.GONE);
        add_cheque_lay.setVisibility(View.VISIBLE);
    }

    private void setrdoOther() {
        discardChanges();
        isChequeDataSaved = 0;
        add_cash_lay.setVisibility(View.GONE);
        add_cheque_lay.setVisibility(View.GONE);
        add_other_lay.setVisibility(View.VISIBLE);
        rdo_cash.setChecked(false);
        rdo_cheque.setChecked(false);
        rdo_other.setChecked(true);
    }

    private void savePaymentCash() {
        int branch = FirstActivity.pref.getInt(getString(R.string.pref_hocode), 0);
        int createdby = FirstActivity.pref.getInt(getString(R.string.pref_retailCustId), 0);

        int siteid = 0, totalPaidAmt = total, cashAmt = total, cardAmt = 0, cardNo = 0, chequeAmt = 0,
                totalRecpt = cashAmt, totalDisc = 0, totalMDRNote = 0, totalMCRNote = 0, totalGoodsRet = 0,
                totalAdvance = 0, receiptType = 0, bankid = 0, refNo = 0, otherTotal = 0;

        String status = "A", accNo = "NA", chqMode = "NA", CBankid = "NA", CBranch = "NA", chequeClear = "Y",
                chequeMode = "NA", ref = "NA", modeType = "NA", taxInvNo = "", det = "", chequDate = "24/Jan/2018",
                remark = "NA", chequeNo = "0", chqImg = "NA", custCurrDet = "NA", custRetDet = "NA", otherBank = "NA",
                otherBranch = "NA", otherPaymentMode = "NA", otherRemark = "NA", otherImg = "NA";

        for (OuststandingReportClass out : invlist) {
            if (out.isChecked()) {
                //billAmt-PreviuosPaid-Balance-ManualDr-ManualCr-GoodsRet-Advance-Disc-TotBal-Rec-Outstnd-ManualDlrlds-
                // ManualDrAmt-ManualCrlds-ManualCrAmt-GoodsRetIds-GoodsRetAmts-AdvancePayIds-AdvancePayAmnts-Type-ToBranchIds
                taxInvNo = taxInvNo + out.getDcno() + ",";
                det = det + out.getNetAmt() + "^" + out.getRecAmnt() + "^" + out.getTotal() + "^0^0^0^0^0^" + out.getTotal() + "^" + out.getPaidAmnt() + "^" + out.getOutAmnt() + "^0^0^0^0^0^0^0^0^0^0^" + out.getDcno() + ",";
            }
        }
        try {
            //taxInvNo = taxInvNo.substring(0, taxInvNo.length() - 1);
            //det = det.substring(0, det.length() - 1);

            String data = branch + "|" + custId + "|" + siteid + "|" + totalPaidAmt + "|" + cashAmt + "|" + cardAmt + "|" + chequeAmt + "|" + cardNo + "|" + chequeNo + "|" +
                    chequDate + "|" + totalRecpt + "|" + totalDisc + "|" + totalMDRNote + "|" + totalMCRNote + "|" + totalGoodsRet + "|" + totalAdvance + "|" +
                    createdby + "|" + remark + "|" + receiptType + "|" + bankid + "|" + accNo + "|" + taxInvNo + "|" + chqMode + "|" + CBankid + "|" + CBranch + "|" +
                    chequeClear + "|" + chequeMode + "|" + refNo + "|" + ref + "|" + modeType + "|" + chqImg + "|" +
                    custCurrDet + "|" + custRetDet + "|" + otherBank + "|" + otherBranch + "|" + otherPaymentMode + "|" +
                    otherRemark + "|" + otherImg + "|" + otherTotal + "|" + status + "|" + det;

            new SavePayment(1).execute(data);
            JSONStringer vehicle = new JSONStringer().object().key("rData").object().key("details").value(data).endObject().endObject();
            Constant.showLog(vehicle.toString());
        } catch (Exception e) {
            e.printStackTrace();
            writeLog("savePaymentCash_" + e.getMessage());
        }
    }

    private void savePaymentCheque() {
        if(ls.size()!=0) {
            for(ChequeDetailsClass chqClass : ls) {
                int branch = FirstActivity.pref.getInt(getString(R.string.pref_hocode), 0);
                int createdby = FirstActivity.pref.getInt(getString(R.string.pref_retailCustId), 0);

                int siteid = 0, totalPaidAmt = Integer.parseInt(chqClass.getChq_det_amt()), cashAmt = 0, cardAmt = 0, cardNo = 0, chequeAmt = Integer.parseInt(chqClass.getChq_det_amt()),
                        totalRecpt = Integer.parseInt(chqClass.getChq_det_amt()), totalDisc = 0, totalMDRNote = 0, totalMCRNote = 0, totalGoodsRet = 0,
                        totalAdvance = 0, receiptType = 0, bankid = 0, refNo = 0, otherTotal = 0;

                String status = "A", accNo = "NA", chqMode = "Direct", chequeNo = chqClass.getChq_det_number(),
                        CBankid = chqClass.getCustBankName(), CBranch = chqClass.getCustBankBranch(), chequeClear = "N",
                        chequeMode = "NA", ref = "NA", modeType = "NA", taxInvNo = "", det = "", chequDate = chqClass.getChq_det_date(),
                        remark = "NA", chqImg = chqClass.getChq_det_image(), custCurrDet = "NA", custRetDet = "NA", otherBank = "NA",
                        otherBranch = "NA", otherPaymentMode = "NA", otherRemark = "NA", otherImg = "NA";

                for (OuststandingReportClass out : invlist) {
                    if (out.isChecked()) {
                        //billAmt-PreviuosPaid-Balance-ManualDr-ManualCr-GoodsRet-Advance-Disc-TotBal-Rec-Outstnd-ManualDlrlds-
                        // ManualDrAmt-ManualCrlds-ManualCrAmt-GoodsRetIds-GoodsRetAmts-AdvancePayIds-AdvancePayAmnts-Type-ToBranchIds
                        taxInvNo = taxInvNo + out.getDcno() + ",";
                        det = det + out.getNetAmt() + "^" + out.getRecAmnt() + "^" + out.getTotal() + "^0^0^0^0^0^" + out.getTotal() + "^" + out.getPaidAmnt() + "^" + out.getOutAmnt() + "^0^0^0^0^0^0^0^0^0^0^" + out.getDcno() + ",";
                    }
                }

                try {
                    //taxInvNo = taxInvNo.substring(0, taxInvNo.length() - 1);
                    //det = det.substring(0, det.length() - 1);

                    String data = branch + "|" + custId + "|" + siteid + "|" + totalPaidAmt + "|" + cashAmt + "|" + cardAmt + "|" + chequeAmt + "|" + cardNo + "|" + chequeNo + "|" +
                            chequDate + "|" + totalRecpt + "|" + totalDisc + "|" + totalMDRNote + "|" + totalMCRNote + "|" + totalGoodsRet + "|" + totalAdvance + "|" +
                            createdby + "|" + remark + "|" + receiptType + "|" + bankid + "|" + accNo + "|" + taxInvNo + "|" + chqMode + "|" + CBankid + "|" + CBranch + "|" +
                            chequeClear + "|" + chequeMode + "|" + refNo + "|" + ref + "|" + modeType + "|" + chqImg + "|" +
                            custCurrDet + "|" + custRetDet + "|" + otherBank + "|" + otherBranch + "|" + otherPaymentMode + "|" +
                            otherRemark + "|" + otherImg + "|" + otherTotal + "|" + status + "|" + det;

                    new SavePayment(1).execute(data);

                    JSONStringer vehicle = new JSONStringer().object().key("rData").object().key("details").value(data).endObject().endObject();
                    Constant.showLog(vehicle.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                    writeLog("savePaymentCheque_" + e.getMessage());
                }
            }
        }else{
            toast.setText("Please Enter Values");
            toast.show();
        }
    }

    private void savePaymentOther() {
        if (ls.size() != 0) {
            for (ChequeDetailsClass chqClass : ls) {
                int branch = FirstActivity.pref.getInt(getString(R.string.pref_hocode), 0);
                int createdby = FirstActivity.pref.getInt(getString(R.string.pref_retailCustId), 0);

                int siteid = 0, totalPaidAmt = total, cashAmt = 0, cardAmt = 0, cardNo = 0, chequeAmt = 0,
                        totalRecpt = 0, totalDisc = 0, totalMDRNote = 0, totalMCRNote = 0, totalGoodsRet = 0,
                        totalAdvance = 0, receiptType = 0, bankid = 0, refNo = 0,
                        otherTotal = Integer.parseInt(chqClass.getChq_det_amt());

                String status = "A", accNo = "NA", chqMode = "Direct", chequeNo = "NA",
                        CBankid = "NA", CBranch = "NA", chequeClear = "N",
                        chequeMode = "NA", ref = "NA", modeType = "NA", taxInvNo = "", det = "", chequDate = "10/Feb/2018",
                        remark = "NA", chqImg = "NA", custCurrDet = "NA", custRetDet = "NA", otherBank = chqClass.getCustBankName(),
                        otherBranch = chqClass.getCustBankBranch(), otherPaymentMode = chqClass.getChq_det_number(),
                        otherRemark = chqClass.getChq_det_ref(), otherImg = chqClass.getChq_det_image();

                for (OuststandingReportClass out : invlist) {
                    if (out.isChecked()) {
                        //billAmt-PreviuosPaid-Balance-ManualDr-ManualCr-GoodsRet-Advance-Disc-TotBal-Rec-Outstnd-ManualDlrlds-
                        // ManualDrAmt-ManualCrlds-ManualCrAmt-GoodsRetIds-GoodsRetAmts-AdvancePayIds-AdvancePayAmnts-Type-ToBranchIds
                        taxInvNo = taxInvNo + out.getDcno() + ",";
                        det = det + out.getNetAmt() + "^" + out.getRecAmnt() + "^" + out.getTotal() + "^0^0^0^0^0^" + out.getTotal() + "^" + out.getPaidAmnt() + "^" + out.getOutAmnt() + "^0^0^0^0^0^0^0^0^0^0^" + out.getDcno() + ",";
                    }
                }

                try {
                    //taxInvNo = taxInvNo.substring(0, taxInvNo.length() - 1);
                    //det = det.substring(0, det.length() - 1);
                    String data = branch + "|" + custId + "|" + siteid + "|" + totalPaidAmt + "|" + cashAmt + "|" + cardAmt + "|" + chequeAmt + "|" + cardNo + "|" + chequeNo + "|" +
                            chequDate + "|" + totalRecpt + "|" + totalDisc + "|" + totalMDRNote + "|" + totalMCRNote + "|" + totalGoodsRet + "|" + totalAdvance + "|" +
                            createdby + "|" + remark + "|" + receiptType + "|" + bankid + "|" + accNo + "|" + taxInvNo + "|" + chqMode + "|" + CBankid + "|" + CBranch + "|" +
                            chequeClear + "|" + chequeMode + "|" + refNo + "|" + ref + "|" + modeType + "|" + chqImg + "|" +
                            custCurrDet + "|" + custRetDet + "|" + otherBank + "|" + otherBranch + "|" + otherPaymentMode + "|" +
                            otherRemark + "|" + otherImg + "|" + otherTotal + "|" + status + "|" + det;

                    new SavePayment(1).execute(data);

                    JSONStringer vehicle = new JSONStringer().object().key("rData").object().key("details").value(data).endObject().endObject();
                    Constant.showLog(vehicle.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                    writeLog("savePaymentOther_" + e.getMessage());
                }
            }
        } else {
            toast.setText("Please Enter Values");
            toast.show();
        }
    }

    private class SavePayment extends AsyncTask<String, Void, String> {

        private ProgressDialog pd;

        public SavePayment(int _branchId) {
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(VisitPaymentFormActivity.this);
            pd.setMessage("Please Wait...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... url) {
            String value = "";
            HttpPost request = new HttpPost(Constant.ipaddress + "/SavePayment");
            request.setHeader("Accept", "application/json");
            request.setHeader("Content-type", "application/json");
            try {

                JSONStringer vehicle = new JSONStringer().object().key("rData").object().key("details").value(url[0]).endObject().endObject();

                StringEntity entity = new StringEntity(vehicle.toString());
                request.setEntity(entity);

                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpResponse response = httpClient.execute(request);
                Constant.showLog("Saving : " + response.getStatusLine().getStatusCode());
                value = new BasicResponseHandler().handleResponse(response);
            } catch (Exception e) {
                e.printStackTrace();
                writeLog("savePaymentAsyncTask_result_" + e.getMessage());
            }
            return value;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                Constant.showLog(result);
                //String str = new JSONObject(result).getString("SaveCustOrderMasterResult");
                String str = new JSONObject(result).getString("SavePaymentResult");
                str = str.replace("\"", "");
                Constant.showLog(str);
                pd.dismiss();
                writeLog("savePaymentAsyncTask_result_" + str + "_" + result);
                String[] retAutoBranchId = str.split("\\-");
                if (retAutoBranchId.length > 1) {
                    if (!retAutoBranchId[0].equals("0") && !retAutoBranchId[0].equals("+2") && !retAutoBranchId[0].equals("+3")) {
                        showPopup(6);
                    } else {
                        showPopup(7);
                    }
                } else {
                    showPopup(7);
                }
            } catch (Exception e) {
                writeLog("savePaymentAsyncTask_" + e.getMessage());
                e.printStackTrace();
                showPopup(7);
                pd.dismiss();
            }
        }
    }

    private void setDefault() {
        if (ls != null) {
            //ls.clear();
        }
        invlist.clear();
        orgList.clear();
        //lv_out.setAdapter(null);
        if (rdo_cash.isChecked()) {
            add_cash_lay.setVisibility(View.VISIBLE);
        } else {
            add_cash_lay.setVisibility(View.GONE);
        }
        ed_amount.setText(null);
        tv_temant.setText("0");
        tv_tbal.setText("0");
        tv_paid.setText("0");
        tv_talloc.setText("0");
        total = 0;
        totBal = 0;
        totPaid = 0;
        totAlloc = 0;
        isChequeDataSaved = 0;
        new Constant(VisitPaymentFormActivity.this).doFinish();
    }

    private void startCheqActivity(){
        rdo_cheque.setChecked(true);
        rdo_cash.setChecked(false);
        rdo_other.setChecked(false);
        Intent intent3 = new Intent(VisitPaymentFormActivity.this, ChequeDetailsActivityChanged.class);
        startActivity(intent3);
        overridePendingTransition(R.anim.enter, R.anim.exit);
    }

    private void startCashActivity(){
        rdo_cheque.setChecked(false);
        rdo_cash.setChecked(true);
        rdo_other.setChecked(false);
        Intent intent2 = new Intent(VisitPaymentFormActivity.this, CurrencyDetailsActivityChanged.class);
        startActivity(intent2);
        overridePendingTransition(R.anim.enter, R.anim.exit);
        writeLog("goes to ChequeDetailsActivity");
    }

    private void startOtherActivity(){
        rdo_cheque.setChecked(false);
        rdo_cash.setChecked(false);
        rdo_other.setChecked(true);
        Intent intent1 = new Intent(VisitPaymentFormActivity.this, OtherDetailsActivity.class);
        startActivity(intent1);
        overridePendingTransition(R.anim.enter, R.anim.exit);
        writeLog("goes to ChequeDetailsActivity");
    }

    private void writeLog(String _data) {
        new WriteLog().writeLog(getApplicationContext(), "VisitPaymentFormActivity_" + _data);
    }

}
