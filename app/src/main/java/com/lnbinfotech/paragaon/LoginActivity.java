package com.lnbinfotech.paragaon;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.lnbinfotech.paragaon.connectivity.ConnectivityTest;
import com.lnbinfotech.paragaon.constant.Constant;
import com.lnbinfotech.paragaon.log.WriteLog;
import com.lnbinfotech.paragaon.parse.ParseJSON;
import com.lnbinfotech.paragaon.post.Post;

import java.net.URLEncoder;

// Created by lnb on 8/11/2016.

public class LoginActivity extends AppCompatActivity {

    private EditText mEmailView, mPasswordView;
    CheckBox cb_remember;
    ProgressDialog pd;
    InputMethodManager input;
    Drawable drawable;
    Toast toast;
    Button mEmailSignInButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if(getSupportActionBar()!=null){
            getSupportActionBar().show();
        }
        mEmailView = (EditText) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);
        cb_remember = (CheckBox) findViewById(R.id.remember);
        input = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        drawable = mEmailView.getBackground();

        toast = Toast.makeText(getApplicationContext(),"",Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER,0,0);

        mEmailView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                showSoftKeyboard(mEmailView);
                mEmailView.setBackgroundDrawable(drawable);
                mEmailView.setError(null);
                return false;
            }
        });

        mPasswordView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                showSoftKeyboard(mPasswordView);
                mPasswordView.setBackgroundDrawable(drawable);
                mPasswordView.setError(null);
                return false;
            }
        });

        mPasswordView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                mPasswordView.setBackgroundDrawable(drawable);
                mPasswordView.setError(null);
            }
        });

        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_UNSPECIFIED) {
                    input.hideSoftInputFromWindow(mPasswordView.getWindowToken(), 0);
                    handled = true;
                    attemptLogin();
                }
                return handled;
            }
        });

        mEmailSignInButton = (Button) findViewById(R.id.login);
        mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                input.hideSoftInputFromWindow(mEmailSignInButton.getWindowToken(), 0);
                attemptLogin();
            }
        });
    }


    public void showSoftKeyboard(View view) {
        if (view.requestFocus()) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    public void attemptLogin() {
        mEmailView.setError(null);
        mPasswordView.setError(null);

        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();
        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            mPasswordView.setBackgroundResource(R.drawable.apptheme_textfield_activated_holo_light);
            cancel = true;
        }

        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            mEmailView.setBackgroundResource(R.drawable.apptheme_textfield_activated_holo_light);
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            if (!FirstActivity.pref.contains(getString(R.string.pref_logged))) {
                if (ConnectivityTest.getNetStat(LoginActivity.this)) {
                    try {
                        String user = URLEncoder.encode(mEmailView.getText().toString(), "UTF-8");
                        String pass = URLEncoder.encode(mPasswordView.getText().toString(), "UTF-8");
                        String url = Constant.ipaddress + "/getEmpValid?UserName=" + user + "&Password=" +pass;
                        writeLog("LoginActivity_attemptLogin_"+url);
                        Constant.showLog(url);
                        new UserLoginTask().execute(url);
                    }catch (Exception e){
                        e.printStackTrace();
                        writeLog("LoginActivity_attemptLogin_"+e.getMessage());
                        toast.setText("Something Went Wrong");
                        toast.show();
                    }
                } else {
                    toast.setText("Network Connection Error");
                    toast.show();
                }
            } else if (FirstActivity.pref.contains(getString(R.string.pref_logged))) {
                if (FirstActivity.pref.getBoolean(getString(R.string.pref_logged), false)) {
                    startMainActivity();
                } else {
                    if (FirstActivity.pref.getString(getString(R.string.pref_username), "").equals(mEmailView.getText().toString()) && FirstActivity.pref.getString(getString(R.string.pref_password), "").equals(mPasswordView.getText().toString())) {
                        startMainActivity();
                    } else {
                        toast.setText("Invalid Username/Password");
                        toast.show();
                    }
                }
            }
        }
    }

    public class UserLoginTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            pd = new ProgressDialog(LoginActivity.this);
            pd.setCancelable(false);
            pd.setMessage("Please Wait...");
            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {
            return Post.POST(params[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null && !result.equals("")) {
                try {
                    result = result.replace("\\", "");
                    result = result.replace("''", "");
                    result = result.substring(1, result.length() - 1);
                    if(new ParseJSON(result,getApplicationContext()).parseUserData() == 1){
                        SharedPreferences.Editor editor = FirstActivity.pref.edit();
                        String uname = mEmailView.getText().toString();
                        editor.putString(getString(R.string.pref_username), uname);
                        if(uname.equals("lnb")) {
                            editor.putString(getString(R.string.pref_emptype), "E");
                        }else{
                            editor.putString(getString(R.string.pref_emptype), "C");
                        }
                        editor.putString(getString(R.string.pref_password), mPasswordView.getText().toString());
                        if(cb_remember.isChecked()){
                            editor.putBoolean(getString(R.string.pref_logged), true);
                        }else{
                            editor.putBoolean(getString(R.string.pref_logged), false);
                        }
                        editor.apply();
                        startMainActivity();
                        pd.dismiss();
                    }else {
                        toast.setText("Invalid Username/Password");
                        toast.show();
                        pd.dismiss();
                    }
                } catch (Exception e) {
                    writeLog("LoginActivity_UserLoginTask_"+e.getMessage());
                    e.printStackTrace();
                    toast.setText("Something Went Wrong");
                    toast.show();
                    pd.dismiss();
                }
            } else {
                writeLog("LoginActivity_UserLoginTask_Network_Connection_Error");
                toast.setText("Network Connection Error");
                toast.show();
                pd.dismiss();
            }
        }
    }

    void startMainActivity(){
        SharedPreferences.Editor editor = FirstActivity.pref.edit();
        if(cb_remember.isChecked()){
            editor.putBoolean(getString(R.string.pref_logged), true);
        }else{
            editor.putBoolean(getString(R.string.pref_logged), false);
        }
        editor.apply();
        startActivity(new Intent(getApplicationContext(),MainActivity.class));
        finish();
        overridePendingTransition(R.anim.enter,R.anim.exit);
    }


    private void writeLog(String _data){
        new WriteLog().writeLog(getApplicationContext(),_data);
    }
}

    /*void doThing(){
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        String uname = FirstActivity.pref.getString("username","");
        String pass = FirstActivity.pref.getString("password","");

        if (email.equals(uname) && password.equals(pass)) {
            SharedPreferences.Editor editor = FirstActivity.pref.edit();
            if (remember.isChecked()) {
                editor.putBoolean("logged", true);
            } else {
                editor.putBoolean("logged", false);
            }
            editor.apply();
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            Runtime.getRuntime().gc();
            Runtime.getRuntime().freeMemory();
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.enter,R.anim.exit);
        }else{
            toast.setText("Invalid Username/Password...");
            toast.show();
        }
    }*/

    /*void doThing(String email, String password){
        if (email.equals("admin") && password.equals("admin")) {
            SharedPreferences.Editor editor = FirstActivity.pref.edit();
            editor.putString("username", email);
            editor.putString("password", password);
            if (remember.isChecked()) {
                editor.putBoolean("logged", true);
            } else {
                editor.putBoolean("logged", false);
            }
            editor.apply();
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            Runtime.getRuntime().gc();
            Runtime.getRuntime().freeMemory();
            startActivity(intent);
            finish();
        }else{
            toast.setText("Invalid Username/Password...");
            toast.show();
        }
    }*/

