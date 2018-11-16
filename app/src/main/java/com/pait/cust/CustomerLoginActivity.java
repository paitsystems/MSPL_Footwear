package com.pait.cust;

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
import com.pait.cust.constant.Constant;
import com.pait.cust.db.DBHandler;
import com.pait.cust.log.WriteLog;
import com.pait.cust.model.UserClass;

import java.util.List;

public class CustomerLoginActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tv_custname, tv_custaddress, tv_custmobile, tv_custemail;
    private ImageView img_cust;
    private EditText ed1, ed2, ed3, ed4, ed5, ed6, ed7, ed8, ed9, ed10, ed11, ed12;
    private Button btn_save;
    private Toast toast;
    private UserClass userClass;
    private CardView lay_setpin, lay_enterpin;
    private int setEnterPINFlag = -1;
    private String PIN = null;
    private DBHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(Constant.liveTestFlag==1) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }

        setContentView(R.layout.activity_customer_login);

        init();

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        userClass = (UserClass) getIntent().getExtras().get("cust");
        setData();

        btn_save.setOnClickListener(this);

        ed1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (ed1.getText().toString().length() == 1) {
                    ed2.requestFocus();
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
                if (ed2.getText().toString().length() == 1) {
                    ed3.requestFocus();
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
                if (ed3.getText().toString().length() == 1) {
                    ed4.requestFocus();
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
                if (ed4.getText().toString().length() == 1) {
                    ed5.requestFocus();
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
                if (ed5.getText().toString().length() == 1) {
                    ed6.requestFocus();
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
                if (ed6.getText().toString().length() == 1) {
                    ed7.requestFocus();
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
                if (ed7.getText().toString().length() == 1) {
                    ed8.requestFocus();
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
                if (ed8.getText().toString().length() == 1) {
                    ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(ed6.getWindowToken(), 0);
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
                if (ed9.getText().toString().length() == 1) {
                    ed10.requestFocus();
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
                if (ed10.getText().toString().length() == 1) {
                    ed11.requestFocus();
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
                if (ed11.getText().toString().length() == 1) {
                    ed12.requestFocus();
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
                if (ed12.getText().toString().length() == 1) {
                    ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(ed6.getWindowToken(), 0);
                }
            }
        });

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
                if (setEnterPINFlag == 0) {
                    setNewPIN();
                } else if (setEnterPINFlag == 1) {
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
        tv_custname.setText(userClass.getPartyName());
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
        String pin = userClass.getCustID() + "-" + PIN;
        SharedPreferences.Editor editor = FirstActivity.pref.edit();
        editor.putString(getString(R.string.pref_savedpin), pin);
        editor.putInt(getString(R.string.pref_retailCustId), userClass.getCustID());
        editor.putInt(getString(R.string.pref_branchid),userClass.getBranchId());
        editor.putInt(getString(R.string.pref_cityid),userClass.getCityId());
        editor.putInt(getString(R.string.pref_hocode),userClass.getHOCode());
        editor.putString(getString(R.string.pref_mobno),userClass.getMobile());
        editor.apply();
        finish();
        Intent intent = new Intent(getApplicationContext(), OptionsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        overridePendingTransition(R.anim.enter, R.anim.exit);
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
        toast.setGravity(Gravity.CENTER, 0, 0);
        FirstActivity.pref = getSharedPreferences(FirstActivity.PREF_NAME,MODE_PRIVATE);
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

    private void writeLog(String _data){
        new WriteLog().writeLog(getApplicationContext(),"CustomerLoginActivity_"+_data);
    }
}
