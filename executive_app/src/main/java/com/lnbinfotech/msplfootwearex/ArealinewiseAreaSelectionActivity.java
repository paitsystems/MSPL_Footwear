package com.lnbinfotech.msplfootwearex;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.view.Gravity;

import com.lnbinfotech.msplfootwearex.adapters.ArealineSelectionAdapter;
import com.lnbinfotech.msplfootwearex.connectivity.ConnectivityTest;
import com.lnbinfotech.msplfootwearex.constant.Constant;
import com.lnbinfotech.msplfootwearex.db.DBHandler;
import com.lnbinfotech.msplfootwearex.log.WriteLog;
import com.lnbinfotech.msplfootwearex.model.ArealineMasterClass;
import com.lnbinfotech.msplfootwearex.model.ProductMasterClass;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class ArealinewiseAreaSelectionActivity extends AppCompatActivity implements View.OnClickListener {

    private Constant constant, constant1;
    private Toast toast;
    private int allvisit = 0;
    private ListView listView;
    private List<ArealineMasterClass> areaLineList;
    private DBHandler db;
    private EditText ed_search;
    private ArealineSelectionAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(Constant.liveTestFlag==1) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }

        setContentView(R.layout.activity_arealinewiseareaselection);

        init();

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.visit);
        }

        ed_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String text = ed_search.getText().toString().toLowerCase(Locale.getDefault());
                if(adapter!=null) {
                    adapter.filter(text);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(listView.getWindowToken(), 0);
                ArealineMasterClass areaClass = (ArealineMasterClass) listView.getItemAtPosition(i);
                Intent intent = new Intent(getApplicationContext(), AreawiseCustomerSelectionActivity.class);
                intent.putExtra("arealine",areaClass);
                startActivity(intent);
                overridePendingTransition(R.anim.enter, R.anim.exit);
            }
        });

        getTodaysVisit();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case 0:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        //showDia(0);
        new Constant(ArealinewiseAreaSelectionActivity.this).doFinish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.arealinewiseareaselection_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //showDia(0);
                new Constant(ArealinewiseAreaSelectionActivity.this).doFinish();
                break;
            case R.id.allvisit:
                if(allvisit == 0){
                    item.setTitle("Schedule Area Line");
                    allvisit = 1;
                    getAllAreaLine();
                }else{
                    allvisit = 0;
                    item.setTitle("All Area Line");
                    getTodaysVisit();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void init() {
        constant = new Constant(ArealinewiseAreaSelectionActivity.this);
        constant1 = new Constant(getApplicationContext());
        listView = (ListView) findViewById(R.id.listView);
        toast = Toast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        areaLineList = new ArrayList<>();
        db = new DBHandler(getApplicationContext());
        ed_search = (EditText) findViewById(R.id.ed_search);
    }

    private void getAllAreaLine(){
        areaLineList.clear();
        Cursor res = db.getDistinctAreaLine();
        if(res.moveToFirst()){
            do {
                ArealineMasterClass area = new ArealineMasterClass();
                area.setArea(res.getString(res.getColumnIndex(DBHandler.AL_Area)));
                areaLineList.add(area);
            }while (res.moveToNext());
            setData();
        }
        res.close();
    }

    private void getTodaysVisit(){
        int saleExe = FirstActivity.pref.getInt(getString(R.string.pref_retailCustId), 0);
        //TODO: Remove when live
        //int saleExe = 152;
        String weekDay;
        SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.US);
        Calendar calendar = Calendar.getInstance();
        weekDay = dayFormat.format(calendar.getTime());
        String url = saleExe + "|" + weekDay;
        if(ConnectivityTest.getNetStat(getApplicationContext())) {
            new getTodaysVisit(0).execute(url);
        }else{
            toast.setText("You Are Offline");
            toast.show();
        }
    }

    private class getTodaysVisit extends AsyncTask<String, Void, String> {
        private int branchId = 0;
        private ProgressDialog pd;

        public getTodaysVisit(int _branchId) {
            this.branchId = _branchId;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(ArealinewiseAreaSelectionActivity.this);
            pd.setMessage("Please Wait...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... url) {
            String value = "";
            String con = Constant.ipaddress + "/GetTodaysVisitDet";
            Constant.showLog(con);
            HttpPost request = new HttpPost(con);
            request.setHeader("Accept", "application/json");
            request.setHeader("Content-type", "application/json");
            try {
                JSONStringer vehicle = new JSONStringer().object().key("rData").object().key("details").value(url[0]).endObject().endObject();
                Constant.showLog(vehicle.toString());
                StringEntity entity = new StringEntity(vehicle.toString());
                request.setEntity(entity);
                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpResponse response = httpClient.execute(request);
                Constant.showLog("Saving : " + response.getStatusLine().getStatusCode());
                value = new BasicResponseHandler().handleResponse(response);
            } catch (Exception e) {
                e.printStackTrace();
                writeLog("getTodaysVisit_result_" + e.getMessage());
            }
            return value;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                pd.dismiss();
                areaLineList.clear();
                Constant.showLog(result);
                JSONArray jsonArray = new JSONArray(new JSONObject(result).get("GetTodaysVisitDetResult").toString());
                if(jsonArray.length()>=1){
                    for(int i=0;i<jsonArray.length();i++) {
                        ArealineMasterClass area = new ArealineMasterClass();
                        area.setArea(jsonArray.getJSONObject(i).getString("CLArea"));
                        areaLineList.add(area);
                    }
                    setData();
                }else{
                    toast.setText("No Record Available");
                    toast.show();
                }
                Constant.showLog(""+jsonArray.length());
            } catch (Exception e) {
                writeLog("getTodaysVisit_" + e.getMessage());
                e.printStackTrace();
                showDia(2);
                pd.dismiss();
            }
        }
    }

    private void setData(){
        listView.setAdapter(null);
        adapter = new ArealineSelectionAdapter(getApplicationContext(),areaLineList);
        listView.setAdapter(adapter);
    }

    private void showDia(int a) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ArealinewiseAreaSelectionActivity.this);
        builder.setCancelable(false);
        if (a == 0) {
            builder.setMessage("Do You Want To Exit App?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    new Constant(ArealinewiseAreaSelectionActivity.this).doFinish();
                    dialog.dismiss();
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        }else if (a == 2) {
            builder.setMessage("Error While Loading Data");
            builder.setPositiveButton("Try Again", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    getTodaysVisit();
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

    private void writeLog(String _data) {
        new WriteLog().writeLog(getApplicationContext(), "ArealinewiseAreaSelectionActivity_" + _data);
    }
}
