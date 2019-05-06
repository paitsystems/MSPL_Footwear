package com.pait.dispatch_app;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.pait.dispatch_app.constant.Constant;
import com.pait.dispatch_app.db.DBHandler;
import com.pait.dispatch_app.log.WriteLog;
import com.pait.dispatch_app.parse.UserClass;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CustomerLoginActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tv_custname, tv_custaddress, tv_custmobile, tv_custemail;
    private ImageView img_cust;
    private EditText ed1, ed2, ed3, ed4, ed5, ed6, ed7, ed8, ed9, ed10, ed11, ed12;
    private Button btn_save, btn_order, btn_report;
    private Toast toast;
    private UserClass userClass;
    private CardView lay_setpin, lay_enterpin;
    private LinearLayout lay_spinner;
    private int setEnterPINFlag = -1;
    private String PIN = null;
    private DBHandler db;
    private EditText[] editTexts;
    private Spinner sp_dpCenter;
    private List<String> dpList;
    private HashMap<String,Integer> dpMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(Constant.liveTestFlag==1) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }

        setContentView(R.layout.activity_customer_login);

        userClass = (UserClass) getIntent().getExtras().get("cust");

        init();

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        setData();


        Glide.with(getApplicationContext()).load(Constant.custimgUrl+userClass.getImagePath())
                .thumbnail(0.5f)
                .crossFade()
                .placeholder(R.drawable.user32)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(img_cust);

        sp_dpCenter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String br = (String) adapterView.getItemAtPosition(i);
                int id = dpMap.get(br);
                if(id !=0) {
                    userClass.setDpId(id);
                    Constant.showLog(br + " " + id);
                } else {
                    toast.setText("Select Dispatch Center First");
                    toast.show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    @Override
    public void onClick(View view) {
        int id = dpMap.get(dpList.get(sp_dpCenter.getSelectedItemPosition()));
        switch (view.getId()) {
            case R.id.btn_save:
                if (setEnterPINFlag == 0) {
                    setNewPIN();
                } else if (setEnterPINFlag == 1) {
                    verifyPin();
                }
                break;
            case R.id.btn_order:
                if(id !=0) {
                    startNewActivity();
                } else {
                    toast.setText("Select Dispatch Center First");
                    toast.show();
                }
                break;
            case R.id.btn_report:
                if(id !=0) {
                    startNewActivity();
                } else {
                    toast.setText("Select Dispatch Center First");
                    toast.show();
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        new Constant(CustomerLoginActivity.this).doFinish();
        toast.cancel();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                new Constant(CustomerLoginActivity.this).doFinish();
                toast.cancel();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setData() {
        tv_custname.setText(userClass.getName());
        tv_custaddress.setText(userClass.getAddress());
        tv_custmobile.setText(userClass.getMobile());
        tv_custemail.setText(userClass.getEmail());

        String custid = String.valueOf(userClass.getCustID());
        PIN = db.getCustPIN(custid);

        if (PIN.equals("-1")) {
            setEnterPINFlag = 0;
            btn_save.setText("SAVE");
            lay_setpin.setVisibility(View.VISIBLE);
            lay_enterpin.setVisibility(View.GONE);
            lay_spinner.setVisibility(View.GONE);
        } else {
            setEnterPINFlag = 1;
            btn_save.setText("LOGIN");
            lay_setpin.setVisibility(View.GONE);
            lay_enterpin.setVisibility(View.VISIBLE);
            lay_spinner.setVisibility(View.VISIBLE);
        }
        setDPCenter();
    }

    private void verifyPin() {
        if (ed9.getText().toString().length() == 1 && ed10.getText().toString().length() == 1 &&
                ed11.getText().toString().length() == 1 && ed12.getText().toString().length() == 1) {
            String pin1 = ed9.getText().toString() + ed10.getText().toString() +
                    ed11.getText().toString() + ed12.getText().toString();
            Constant.showLog(pin1);
            if (pin1.equals(PIN)) {
                //showDia(2);
                startNewActivity();
                /*List<String> _list = db.checkPINUnsetID();
                if(_list.size()!=0){
                    showDia(1);
                }else{
                    showDia(2);
                }*/
            } else {
                showDia(4);
            }
        }
    }

    private void setNewPIN() {
        if (ed1.getText().toString().length() == 1 && ed2.getText().toString().length() == 1 &&
                ed3.getText().toString().length() == 1 && ed4.getText().toString().length() == 1) {
            String pin1 = ed1.getText().toString() + ed2.getText().toString() +
                    ed3.getText().toString() + ed4.getText().toString();
            Constant.showLog(pin1);
            if (ed5.getText().toString().length() == 1 && ed6.getText().toString().length() == 1 &&
                    ed7.getText().toString().length() == 1 && ed8.getText().toString().length() == 1) {
                String pin2 = ed5.getText().toString() + ed6.getText().toString() +
                        ed7.getText().toString() + ed8.getText().toString();
                Constant.showLog(pin2);
                if (pin1.equals(pin2)) {
                    savePin(pin1);
                } else {
                    showDia(5);
                }
            } else {
                toast.setText(R.string.pleasereenterpin);
                toast.show();
            }
        } else {
            toast.setText(R.string.pleaseenterpin);
            toast.show();
        }
    }

    private void savePin(String _pin) {
        writeLog("savePin_"+_pin);
        db.updatePIN(String.valueOf(userClass.getCustID()), _pin);
        showDia(3);
    }

    private void clearFields(int a) {
        if (a == 1) {
            ed9.setText(null);
            ed10.setText(null);
            ed11.setText(null);
            ed12.setText(null);
            ed9.requestFocus();
        } else if (a == 2) {
            ed1.setText(null);
            ed2.setText(null);
            ed3.setText(null);
            ed4.setText(null);
            ed5.setText(null);
            ed6.setText(null);
            ed7.setText(null);
            ed8.setText(null);
            ed1.requestFocus();
        }
    }

    private void startNewActivity() {
        if(FirstActivity.pref.contains(getString(R.string.pref_dpId))){
            int prevId = FirstActivity.pref.getInt(getString(R.string.pref_dpId),0);
            if(prevId!=userClass.getDpId()){
                db.deleteTable(DBHandler.Table_DispatchMaster);
            }
        }
        String pin = userClass.getCustID() + "-" + PIN;
        SharedPreferences.Editor editor = FirstActivity.pref.edit();
        editor.putString(getString(R.string.pref_savedpin), pin);
        editor.putInt(getString(R.string.pref_retailCustId), userClass.getCustID());
        editor.putInt(getString(R.string.pref_branchid),userClass.getBranchId());
        editor.putInt(getString(R.string.pref_cityid),userClass.getCityId());
        editor.putInt(getString(R.string.pref_hocode),userClass.getHOCode());
        editor.putString(getString(R.string.pref_mobno),userClass.getMobile());
        editor.putInt(getString(R.string.pref_dpId),userClass.getDpId());
        editor.apply();
        finish();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("cust",userClass);
        startActivity(intent);
        overridePendingTransition(R.anim.enter, R.anim.exit);
    }

    private void setDPCenter(){
        FirstActivity.pref = getSharedPreferences(FirstActivity.PREF_NAME,MODE_PRIVATE);
        int hoCode = FirstActivity.pref.getInt(getString(R.string.pref_hocode),0);
        dpList.add("Select Dispatch Center");
        dpMap.put("Select Dispatch Center",0);
        Cursor res = db.getDPCenter(hoCode);
        if(res.moveToFirst()){
            do{
                dpList.add(res.getString(res.getColumnIndex(DBHandler.Company_DisplayCmp)));
                dpMap.put(res.getString(res.getColumnIndex(DBHandler.Company_DisplayCmp)),
                        res.getInt(res.getColumnIndex(DBHandler.Company_Id)));
            }while (res.moveToNext());
        }
        res.close();
        sp_dpCenter.setAdapter(new ArrayAdapter<>(getApplicationContext(), R.layout.sizegroup_spinner_row, dpList));

    }

    private void init() {
        FirstActivity.pref = getSharedPreferences(FirstActivity.PREF_NAME,MODE_PRIVATE);
        tv_custname = findViewById(R.id.tv_custname);
        tv_custaddress = findViewById(R.id.tv_custaddress);
        tv_custmobile = findViewById(R.id.tv_custmobile);
        tv_custemail = findViewById(R.id.tv_custemail);
        img_cust = findViewById(R.id.img_cust);
        ed1 = findViewById(R.id.ed1);
        ed2 = findViewById(R.id.ed2);
        ed3 = findViewById(R.id.ed3);
        ed4 = findViewById(R.id.ed4);
        ed5 = findViewById(R.id.ed5);
        ed6 = findViewById(R.id.ed6);
        ed7 = findViewById(R.id.ed7);
        ed8 = findViewById(R.id.ed8);
        ed9 = findViewById(R.id.ed9);
        ed10 = findViewById(R.id.ed10);
        ed11 = findViewById(R.id.ed11);
        ed12 = findViewById(R.id.ed12);
        lay_setpin = findViewById(R.id.lay_setpin);
        lay_enterpin = findViewById(R.id.lay_enterpin);
        lay_spinner = findViewById(R.id.lay_spinner);
        btn_save = findViewById(R.id.btn_save);
        btn_report = findViewById(R.id.btn_report);
        sp_dpCenter = findViewById(R.id.sp_dpcenter);
        sp_dpCenter = findViewById(R.id.sp_dpcenter);
        dpList = new ArrayList<>();
        dpMap = new HashMap<>();
        db = new DBHandler(getApplicationContext());
        toast = Toast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);

        btn_save.setOnClickListener(this);
        btn_order.setOnClickListener(this);
        btn_report.setOnClickListener(this);

        String custid = String.valueOf(userClass.getCustID());
        String PIN = db.getCustPIN(custid);

        if (PIN.equals("-1")) {
            editTexts = new EditText[]{ed1, ed2, ed3, ed4, ed5, ed6, ed7, ed8};
        } else {
            editTexts = new EditText[]{ed9, ed10, ed11, ed12};
        }

        ed1.addTextChangedListener(new PinTextWatcher(0));
        ed2.addTextChangedListener(new PinTextWatcher(1));
        ed3.addTextChangedListener(new PinTextWatcher(2));
        ed4.addTextChangedListener(new PinTextWatcher(3));

        ed5.addTextChangedListener(new PinTextWatcher(4));
        ed6.addTextChangedListener(new PinTextWatcher(5));
        ed7.addTextChangedListener(new PinTextWatcher(6));
        ed8.addTextChangedListener(new PinTextWatcher(7));

        ed9.addTextChangedListener(new PinTextWatcher(0));
        ed10.addTextChangedListener(new PinTextWatcher(1));
        ed11.addTextChangedListener(new PinTextWatcher(2));
        ed12.addTextChangedListener(new PinTextWatcher(3));

        ed1.setOnKeyListener(new PinOnKeyListener(0));
        ed2.setOnKeyListener(new PinOnKeyListener(1));
        ed3.setOnKeyListener(new PinOnKeyListener(2));
        ed4.setOnKeyListener(new PinOnKeyListener(3));

        ed5.setOnKeyListener(new PinOnKeyListener(4));
        ed6.setOnKeyListener(new PinOnKeyListener(5));
        ed7.setOnKeyListener(new PinOnKeyListener(6));
        ed8.setOnKeyListener(new PinOnKeyListener(7));

        ed9.setOnKeyListener(new PinOnKeyListener(0));
        ed10.setOnKeyListener(new PinOnKeyListener(1));
        ed11.setOnKeyListener(new PinOnKeyListener(2));
        ed12.setOnKeyListener(new PinOnKeyListener(3));
    }

    private void showDia(int a) {
        AlertDialog.Builder builder = new AlertDialog.Builder(CustomerLoginActivity.this);
        builder.setCancelable(false);
        if (a == 0) {
            builder.setMessage("Do You Want To Exit App?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    new Constant(CustomerLoginActivity.this).doFinish();
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
            builder.setMessage("Do You Want Set PIN To Other Details?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    new Constant(CustomerLoginActivity.this).doFinish();
                    dialog.dismiss();
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startNewActivity();
                    dialog.dismiss();
                }
            });
        } else if (a == 2) {
            builder.setMessage("Login Successfull");
            builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startNewActivity();
                    dialog.dismiss();
                }
            });
        } else if (a == 3) {
            builder.setMessage("PIN Set Successfully");
            builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    List<String> _list = db.checkPINUnsetID();
                    if (_list.size() != 0) {
                        showDia(1);
                    } else {
                        //showDia(6);
                        setData();
                    }
                }
            });
        } else if (a == 4) {
            builder.setMessage("Invalid PIN");
            builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    clearFields(1);
                    dialog.dismiss();
                }
            });
        } else if (a == 5) {
            builder.setMessage("PIN Not Matched");
            builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    clearFields(2);
                    dialog.dismiss();
                }
            });
        } else if (a == 6) {
            builder.setMessage("Do you want to continue with this login?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startNewActivity();
                    dialog.dismiss();
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    new Constant(CustomerLoginActivity.this).doFinish();
                    dialog.dismiss();
                }
            });
        }
        builder.create().show();
    }

    public class PinTextWatcher implements TextWatcher {

        private int currentIndex;
        private boolean isFirst = false, isLast = false;
        private String newTypedString = "";

        PinTextWatcher(int currentIndex) {
            this.currentIndex = currentIndex;

            if (currentIndex == 0)
                this.isFirst = true;
            else if (currentIndex == editTexts.length - 1)
                this.isLast = true;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            newTypedString = s.subSequence(start, start + count).toString().trim();
        }

        @Override
        public void afterTextChanged(Editable s) {

            String text = newTypedString;

            /* Detect paste event and set first char */
            if (text.length() > 1)
                text = String.valueOf(text.charAt(0));

            editTexts[currentIndex].removeTextChangedListener(this);
            editTexts[currentIndex].setText(text);
            editTexts[currentIndex].setSelection(text.length());
            editTexts[currentIndex].addTextChangedListener(this);

            if (text.length() == 1)
                moveToNext();
            else if (text.length() == 0)
                moveToPrevious();
        }

        private void moveToNext() {
            if (!isLast)
                editTexts[currentIndex + 1].requestFocus();

            if (isAllEditTextsFilled() && isLast) { // isLast is optional
                editTexts[currentIndex].clearFocus();
                hideKeyboard();
            }
        }

        private void moveToPrevious() {
            if (!isFirst)
                editTexts[currentIndex - 1].requestFocus();
        }

        private boolean isAllEditTextsFilled() {
            for (EditText editText : editTexts)
                if (editText.getText().toString().trim().length() == 0)
                    return false;
            return true;
        }

        private void hideKeyboard() {
            if (getCurrentFocus() != null) {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
        }
    }

    public class PinOnKeyListener implements View.OnKeyListener {

        private int currentIndex;

        PinOnKeyListener(int currentIndex) {
            this.currentIndex = currentIndex;
        }

        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_DEL && event.getAction() == KeyEvent.ACTION_DOWN) {
                if (editTexts[currentIndex].getText().toString().isEmpty() && currentIndex != 0)
                    editTexts[currentIndex - 1].requestFocus();
            }
            return false;
        }

    }

    private void writeLog(String _data){
        new WriteLog().writeLog(getApplicationContext(),"CustomerLoginActivity_"+_data);
    }
}
