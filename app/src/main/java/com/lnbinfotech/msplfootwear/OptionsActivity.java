package com.lnbinfotech.msplfootwear;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.lnbinfotech.msplfootwear.constant.Constant;
import com.lnbinfotech.msplfootwear.db.DBHandler;

import java.util.Locale;

public class OptionsActivity extends AppCompatActivity implements View.OnClickListener {

    private CardView card_give_order, card_account, card_track_order, card_profile, card_scheme, card_whats_new, card_feedback;
    public static float custDisc = 0;
    private Menu mMenu;
    private TextView actionbar_noti_tv;
    private DBHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);

        init();
        //test_t();

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }

        card_give_order.setOnClickListener(this);
        card_account.setOnClickListener(this);
        card_track_order.setOnClickListener(this);
        card_profile.setOnClickListener(this);
        card_scheme.setOnClickListener(this);
        card_whats_new.setOnClickListener(this);
        card_feedback.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mMenu!=null){
            onCreateOptionsMenu(mMenu);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.card_give_order:
                //showDia(1);
                //int custId = FirstActivity.pref.getInt(getString(R.string.pref_retailCustId),0);
                //custDisc = new DBHandler(getApplicationContext()).getCustDiscount(custId);
                startActivity(new Intent(getApplicationContext(), CutsizeSetwiseOrderActivity.class));
                overridePendingTransition(R.anim.enter, R.anim.exit);
                break;
            case R.id.card_account:
                startActivity(new Intent(getApplicationContext(), CustomerAccountActivity.class));
                overridePendingTransition(R.anim.enter, R.anim.exit);
                break;
            case R.id.card_track_order:
                startActivity(new Intent(getApplicationContext(), TrackOrderMasterActivity.class));
                overridePendingTransition(R.anim.enter, R.anim.exit);
                break;
            case R.id.card_profile:
                startActivity(new Intent(getApplicationContext(), ProfileViewActivity.class));
                overridePendingTransition(R.anim.enter, R.anim.exit);
                break;
            case R.id.card_scheme:
                break;
            case R.id.card_whatsnew:
                break;
            case R.id.card_feedback:
                startActivity(new Intent(getApplicationContext(), FeedbackActivity.class));
                overridePendingTransition(R.anim.enter, R.anim.exit);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        showDia(0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /*getMenuInflater().inflate(R.menu.mainactivity_menu, menu);
        return true;*/
        mMenu = menu;
        menu.clear();
       // mMenu = menu;
        getMenuInflater().inflate(R.menu.mainactivity_menu, menu);
        menu.clear();

        getMenuInflater().inflate(R.menu.mainactivity_menu, menu);

        final MenuItem item = menu.findItem(R.id.cart);
        MenuItemCompat.setActionView(item, R.layout.actionbaar_badge_layout);
        View view = MenuItemCompat.getActionView(item);
        TextView actionbar_noti_tv = (TextView)view.findViewById(R.id.actionbar_noti_tv);
        actionbar_noti_tv.setText("12");

        int count = new DBHandler(getApplicationContext()).getCartCount();
        Constant.showLog("cart_count:"+count);
        actionbar_noti_tv.setText(String.valueOf(count));

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onOptionsItemSelected(item);
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                showDia(0);
                break;
            case R.id.refresh:
                startActivity(new Intent(getApplicationContext(), DataRefreshActivity.class));
                overridePendingTransition(R.anim.enter, R.anim.exit);
                break;
            case R.id.report_error:
                showDia(6);
                break;
            case R.id.cart:
                Intent intent = new Intent(getApplicationContext(), ViewCustomerOrderActiviy.class);
                intent.putExtra("from","option");
                startActivity(intent);
                overridePendingTransition(R.anim.enter, R.anim.exit);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void init() {
        db = new DBHandler(this);
        card_give_order = (CardView) findViewById(R.id.card_give_order);
        card_account = (CardView) findViewById(R.id.card_account);
        card_track_order = (CardView) findViewById(R.id.card_track_order);
        card_profile = (CardView) findViewById(R.id.card_profile);
        card_scheme = (CardView) findViewById(R.id.card_scheme);
        card_whats_new = (CardView) findViewById(R.id.card_whatsnew);
        card_feedback = (CardView) findViewById(R.id.card_feedback);
    }

    private void showDia(int a) {
        AlertDialog.Builder builder = new AlertDialog.Builder(OptionsActivity.this);
        builder.setCancelable(false);
        if (a == 0) {
            builder.setMessage("Do You Want To Exit App?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    new Constant(OptionsActivity.this).doFinish();
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
        if (a == 1) {
            builder.setTitle("Take Order");
            builder.setMessage("How do you want to take order?");
            builder.setPositiveButton("Imagewise", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startActivity(new Intent(getApplicationContext(), ImagewiseSetwiseOrderActivity.class));
                    overridePendingTransition(R.anim.enter, R.anim.exit);
                    dialog.dismiss();
                }
            });
            builder.setNegativeButton("Cutsize", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    int custId = FirstActivity.pref.getInt(getString(R.string.pref_retailCustId),0);
                    custDisc = new DBHandler(getApplicationContext()).getCustDiscount(custId);
                    startActivity(new Intent(getApplicationContext(), CutsizeSetwiseOrderActivity.class));
                    overridePendingTransition(R.anim.enter, R.anim.exit);
                    dialog.dismiss();
                }
            });
            builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        }
        builder.create().show();
    }

    private void test_t(){
        Configuration configuration = getBaseContext().getResources().getConfiguration();
        String lang = "mr";
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        configuration.locale = locale;
        Constant.showLog("locale:"+locale);

    }

}
