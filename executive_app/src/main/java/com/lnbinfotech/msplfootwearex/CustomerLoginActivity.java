package com.lnbinfotech.msplfootwearex;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.lnbinfotech.msplfootwearex.constant.Constant;
import com.lnbinfotech.msplfootwearex.db.DBHandler;
import com.lnbinfotech.msplfootwearex.log.WriteLog;
import com.lnbinfotech.msplfootwearex.model.CustomerDetailClass;
import com.lnbinfotech.msplfootwearex.model.UserClass;

import java.util.List;

public class CustomerLoginActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tv_custname, tv_custaddress, tv_custmobile, tv_custemail;
    private ImageView img_cust;
    private EditText ed1,ed2,ed3,ed4,ed5,ed6,ed7,ed8,ed9,ed10,ed11,ed12;
    private Button btn_save;
    private Toast toast;
    private UserClass userClass;
    private CardView lay_setpin, lay_enterpin;
    private int setEnterPINFlag = -1;
    private String PIN = null;
    private DBHandler db;
    private EditText[] editTexts;

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

        btn_save.setOnClickListener(this);

        /*ed1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(ed1.getText().toString().length()==1){
                    ed2.requestFocus();
                } else {
                    ed1.requestFocus();
                }
            }
        });

        ed2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(ed2.getText().toString().length()==1){
                    ed3.requestFocus();
                } else {
                    ed1.requestFocus();
                }
            }
        });

        ed3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(ed3.getText().toString().length()==1){
                    ed4.requestFocus();
                } else {
                    ed2.requestFocus();
                }
            }
        });

        ed4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(ed4.getText().toString().length()==1){
                    ed5.requestFocus();
                } else {
                    ed3.requestFocus();
                }
            }
        });

        ed5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(ed5.getText().toString().length()==1){
                    ed6.requestFocus();
                } else {
                    ed4.requestFocus();
                }
            }
        });

        ed6.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(ed6.getText().toString().length()==1){
                    ed7.requestFocus();
                } else {
                    ed5.requestFocus();
                }
            }
        });

        ed7.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(ed7.getText().toString().length()==1){
                    ed8.requestFocus();
                } else {
                    ed6.requestFocus();
                }
            }
        });

        ed8.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(ed8.getText().toString().length()==1){
                    ((InputMethodManager)getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(ed6.getWindowToken(),0);
                } else {
                    ed7.requestFocus();
                }
            }
        });

        ed9.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(ed9.getText().toString().length()==1){
                    ed10.requestFocus();
                } else {
                    ed8.requestFocus();
                }
            }
        });

        ed10.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(ed10.getText().toString().length()==1){
                    ed11.requestFocus();
                } else {
                    ed9.requestFocus();
                }
            }
        });

        ed11.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(ed11.getText().toString().length()==1){
                    ed12.requestFocus();
                } else {
                    ed10.requestFocus();
                }
            }
        });

        ed12.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(ed12.getText().toString().length()==1){
                    ((InputMethodManager)getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(ed6.getWindowToken(),0);
                } else {
                    ed11.requestFocus();
                }
            }
        });*/

        Glide.with(getApplicationContext()).load(Constant.custimgUrl+userClass.getImagePath())
                .thumbnail(0.5f)
                .crossFade()
                .placeholder(R.drawable.user32)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(img_cust);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_save:
                if(setEnterPINFlag == 0){
                    setNewPIN();
                }else if(setEnterPINFlag == 1){
                    verifyPin();
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
        } else {
            setEnterPINFlag = 1;
            btn_save.setText("LOGIN");
            lay_setpin.setVisibility(View.GONE);
            lay_enterpin.setVisibility(View.VISIBLE);
        }
    }

    private void verifyPin(){
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
            }else{
                showDia(4);
            }
        }
    }

    private void setNewPIN(){
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

    private void clearFields(int a){
        if(a==1){
            ed9.setText(null);ed10.setText(null);ed11.setText(null);ed12.setText(null);
            ed9.requestFocus();
        }else if(a==2){
            ed1.setText(null);ed2.setText(null);ed3.setText(null);ed4.setText(null);
            ed5.setText(null);ed6.setText(null);ed7.setText(null);ed8.setText(null);
            ed1.requestFocus();
        }
    }

    private void startNewActivity(){
        String pin = userClass.getCustID()+"-"+PIN;
        SharedPreferences.Editor editor = FirstActivity.pref.edit();
        editor.putString(getString(R.string.pref_savedpin),pin);
        editor.putInt(getString(R.string.pref_retailCustId),userClass.getCustID());
        editor.putInt(getString(R.string.pref_branchid),userClass.getBranchId());
        editor.putInt(getString(R.string.pref_cityid),userClass.getCityId());
        editor.putInt(getString(R.string.pref_hocode),userClass.getHOCode());
        editor.putString(getString(R.string.pref_mobno),userClass.getMobile());
        editor.putString(getString(R.string.pref_name),userClass.getName());
        editor.apply();
        finish();
        Intent intent = new Intent(getApplicationContext(),OptionsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        overridePendingTransition(R.anim.enter,R.anim.exit);
    }

    private void init() {
        tv_custname = (TextView) findViewById(R.id.tv_custname);
        tv_custaddress = (TextView) findViewById(R.id.tv_custaddress);
        tv_custmobile = (TextView) findViewById(R.id.tv_custmobile);
        tv_custemail = (TextView) findViewById(R.id.tv_custemail);
        img_cust = (ImageView) findViewById(R.id.img_cust);
        ed1 = (EditText) findViewById(R.id.ed1);
        ed2 = (EditText) findViewById(R.id.ed2);
        ed3 = (EditText) findViewById(R.id.ed3);
        ed4 = (EditText) findViewById(R.id.ed4);
        ed5 = (EditText) findViewById(R.id.ed5);
        ed6 = (EditText) findViewById(R.id.ed6);
        ed7 = (EditText) findViewById(R.id.ed7);
        ed8 = (EditText) findViewById(R.id.ed8);
        ed9 = (EditText) findViewById(R.id.ed9);
        ed10 = (EditText) findViewById(R.id.ed10);
        ed11 = (EditText) findViewById(R.id.ed11);
        ed12 = (EditText) findViewById(R.id.ed12);
        lay_setpin = (CardView) findViewById(R.id.lay_setpin);
        lay_enterpin = (CardView) findViewById(R.id.lay_enterpin);
        btn_save = (Button) findViewById(R.id.btn_save);
        db = new DBHandler(getApplicationContext());
        toast = Toast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG);
        FirstActivity.pref = getSharedPreferences(FirstActivity.PREF_NAME, MODE_PRIVATE);
        toast.setGravity(Gravity.CENTER, 0, 0);

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
                        showDia(6);
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
