package com.pait.cust;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;

import com.pait.cust.constant.Constant;
import com.pait.cust.fragments.IGentsCategoryFragment1;
import com.pait.cust.log.WriteLog;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.util.ArrayList;
import java.util.List;

public class MainImagewiseSetwiseOrderActivity extends AppCompatActivity {

    private NonScrollableVP pager;
    private TabLayout tabs;
    private String from = "";
    public static int allOrNew = 0, product_id = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(Constant.liveTestFlag==1) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }

        setContentView(R.layout.activity_main_imagewise_setwise_order);
        pager = (NonScrollableVP)findViewById(R.id.pagerMain);
        tabs = (TabLayout) findViewById(R.id.tabs);

        try {
            from = getIntent().getExtras().getString("from");
            assert from != null;
            if (from.equals("Option")) {
                allOrNew = 0;
                setUpPager();
            }else{
                getMaxProdId();
            }
        }catch (Exception e){
            setUpPager();
            e.printStackTrace();
            writeLog("onCreate_"+e.getMessage());
        }
    }

    @Override
    public void onBackPressed() {
        new Constant(MainImagewiseSetwiseOrderActivity.this).doFinish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.cutsize_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //showDia(0);
                new Constant(MainImagewiseSetwiseOrderActivity.this).doFinish();
                break;
            case R.id.prod_search:
                Intent intent = new Intent(getApplicationContext(), ProductSearchActivity.class);
                intent.putExtra("cat9", "0");
                intent.putExtra("cat2", "0");
                intent.putExtra("from", "imagewisesize");
                startActivity(intent);
                overridePendingTransition(R.anim.enter, R.anim.exit);
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    private void setUpPager() {
        final ViewPagerClass adapter = new ViewPagerClass(getSupportFragmentManager());
        String arr[] = new String[]{"GENTS", "LADIES-N-BOYS", "HAWAI-N-EVA", "SCHOOL SHOES"};
        for (String str : arr) {
            adapter.addFragment(new IGentsCategoryFragment1(), str);
        }
        pager.setAdapter(adapter);
        tabs.setupWithViewPager(pager);
    }

    private class ViewPagerClass extends FragmentStatePagerAdapter {

        List<Fragment> mFragments = new ArrayList<>();
        List<String> mFragmentsTitle = new ArrayList<>();

        public ViewPagerClass(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment f, String s) {
            mFragments.add(f);
            mFragmentsTitle.add(s);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentsTitle.get(position);
        }

        @Override
        public Fragment getItem(int position) {
            //return mFragments.get(position);
            return IGentsCategoryFragment1.newInstance(mFragmentsTitle.get(position));
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }
    }

    private void writeLog(String data){
        new WriteLog().writeLog(getApplicationContext(),"MainImagewiseSetwiseOrderActivity_"+data);
    }

    private void getMaxProdId(){
        String data = "C";
        new GetWhatsNewProdId().execute(data);
    }

    private class GetWhatsNewProdId extends AsyncTask<String, Void, String> {

        private ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(MainImagewiseSetwiseOrderActivity.this);
            pd.setMessage("Please Wait...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... url) {
            String value = "";
            DefaultHttpClient httpClient = null;
            HttpPost request = new HttpPost(Constant.ipaddress + "/GetWhatsNewProdId");
            request.setHeader("Accept", "application/json");
            request.setHeader("Content-type", "application/json");
            try {
                JSONStringer vehicle = new JSONStringer().object().key("rData").object().key("details").value(url[0]).endObject().endObject();
                Constant.showLog(vehicle.toString());
                writeLog("GetWhatsNewProdId_"+vehicle.toString());
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
                writeLog("GetWhatsNewProdId_result_" + e.getMessage());
            }
            finally {
                try{
                    if(httpClient!=null) {
                        httpClient.getConnectionManager().shutdown();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    writeLog("GetWhatsNewProdId_finally_"+e.getMessage());
                }
            }
            return value;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                Constant.showLog(result);
                String str = new JSONObject(result).getString("GetWhatsNewProdIdResult");
                str = str.replace("\"", "");
                Constant.showLog(str);
                pd.dismiss();
                writeLog("GetWhatsNewProdId_result_" + str + "_" + result);
                if (!str.equals("0")) {
                    allOrNew = 1;
                    product_id = Integer.parseInt(str);
                    writeLog("GetWhatsNewProdId_result_" + str);
                    setUpPager();
                }
            } catch (Exception e) {
                writeLog("GetWhatsNewProdId_" + e.getMessage());
                e.printStackTrace();
                pd.dismiss();
            }
        }
    }

}
