package com.lnbinfotech.msplfootwearex;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.lnbinfotech.msplfootwearex.adapters.CustomerOrderUnpackGridAdpater;
import com.lnbinfotech.msplfootwearex.adapters.ImageWiseAddToCartRecyclerAdapter;
import com.lnbinfotech.msplfootwearex.adapters.SizeGroupRecyclerAdapter;
import com.lnbinfotech.msplfootwearex.adapters.SizeGroupWiseQtyAdapter;
import com.lnbinfotech.msplfootwearex.constant.Constant;
import com.lnbinfotech.msplfootwearex.db.DBHandler;
import com.lnbinfotech.msplfootwearex.interfaces.RecyclerViewToActivityInterface;
import com.lnbinfotech.msplfootwearex.interfaces.ServerCallback;
import com.lnbinfotech.msplfootwearex.interfaces.ServerCallbackList;
import com.lnbinfotech.msplfootwearex.log.WriteLog;
import com.lnbinfotech.msplfootwearex.model.CheckAvailStockClass;
import com.lnbinfotech.msplfootwearex.model.CustomerOrderClass;
import com.lnbinfotech.msplfootwearex.model.GentsCategoryClass;
import com.lnbinfotech.msplfootwearex.model.ImagewiseAddToCartClass;
import com.lnbinfotech.msplfootwearex.volleyrequests.VolleyRequests;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class ImageWiseAddToCartActivity extends AppCompatActivity implements View.OnClickListener,
        RecyclerViewToActivityInterface {

    private Toast toast;
    private TextView tv_prodname, tv_wsp, tv_mrp, tv_hsncode, tv_gstper,tv_gstprint,tv_marginup,tv_margindown,
            tv_totqty, tv_totamnt, tv_totset, tv_totnetamt, actionbar_noti_tv;
    private Button btn_addToCart, btn_looseOrder, btn_newCat, btn_checkstock;
    private RecyclerView rv_images, rv_size, rv_sizegroup;
    private DBHandler db;
    private GentsCategoryClass gentClass;
    private Spinner sp_sizeGroup;
    private List<String> sizeGroup_list;
    private String selSizeGroup = "", prodIdStr = "", cat9 = "",cat2 = "";
    private List<String> size_list;
    private int selQtyLocal = 0, compPackQty = -1;
    private int selProdId = 0;
    private List<String> unpackSizeList;
    public static HashMap<Integer, Integer> map;
    private List<String> colour_list;
    private Constant constant;
    private List<CheckAvailStockClass> stockList;
    private GridView gridView;
    private LinearLayout lay_comp_pack;
    private AutoCompleteTextView auto_set;
    private List<String> colorListUpdated;
    private Menu mMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(Constant.liveTestFlag==1) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }

        setContentView(R.layout.activity_imagewise_addtocart);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }

        init();

        try {
            gentClass = (GentsCategoryClass) getIntent().getExtras().getSerializable("data");
            cat9 = getIntent().getExtras().getString("cat9");
            cat2 = getIntent().getExtras().getString("cat2");
            String str = cat9+" -> "+cat2;
            getSupportActionBar().setTitle(str);
            assert gentClass != null;
            AddToCartActivity.selProdId = gentClass.getProdId();
            selProdId = gentClass.getProdId();
            tv_prodname.setText(gentClass.getProductName());
            tv_mrp.setText(gentClass.getMrp());
            tv_wsp.setText(gentClass.getWsp());
            tv_hsncode.setText(gentClass.getHsnCode());
            tv_marginup.setText(gentClass.getMarkup());
            tv_margindown.setText(gentClass.getMarkdown());
            tv_gstprint.setText(gentClass.getGstPer());
            tv_gstper.setText(gentClass.getGstGroupName());
            prodIdStr = gentClass.getProductId();
        }catch (Exception e){
            e.printStackTrace();
            writeLog("onCreate()_"+e.getMessage());
        }

        /*sp_sizeGroup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selSizeGroup = (String) adapterView.getItemAtPosition(i);
                Constant.showLog(selSizeGroup);
                lay_comp_pack.setVisibility(View.GONE);
                gridView.setVisibility(View.VISIBLE);

                setSizeGroupWiseImage(selSizeGroup);
                setSizeData(selSizeGroup);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                lay_comp_pack.setVisibility(View.GONE);
                gridView.setVisibility(View.VISIBLE);
            }
        });*/

        auto_set.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                final int action = motionEvent.getAction();
                if (action == MotionEvent.ACTION_DOWN) {
                    auto_set.showDropDown();
                    auto_set.setThreshold(0);
                }
                return false;
            }
        });

        RecyclerView.LayoutManager mLayoutManager1 =
                new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        rv_images.setLayoutManager(mLayoutManager1);
        rv_images.setItemAnimator(new DefaultItemAnimator());

        RecyclerView.LayoutManager mLayoutManager3 =
                new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        rv_sizegroup.setLayoutManager(mLayoutManager3);
        rv_sizegroup.setItemAnimator(new DefaultItemAnimator());

        RecyclerView.LayoutManager mLayoutManager2 =
                new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        rv_size.setLayoutManager(mLayoutManager2);
        rv_size.setItemAnimator(new DefaultItemAnimator());

        if(mMenu!=null){
            onCreateOptionsMenu(mMenu);
        }
        setData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //constant = new Constant(ImageWiseAddToCartActivity.this);
        totalCalculations();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        mMenu = menu;
        getMenuInflater().inflate(R.menu.addtocard_menu, menu);
        menu.clear();

        getMenuInflater().inflate(R.menu.addtocard_menu, menu);

        final MenuItem item = menu.findItem(R.id.cart);
        MenuItemCompat.setActionView(item, R.layout.actionbaar_badge_layout);
        View view = MenuItemCompat.getActionView(item);
        actionbar_noti_tv = (TextView)view.findViewById(R.id.actionbar_noti_tv);
        actionbar_noti_tv.setText("0");

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
                new Constant(ImageWiseAddToCartActivity.this).doFinish();
                break;
            case R.id.cart:
                startViewCustOrderActivity();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        map.clear();
        AddToCartActivity.activityToFrom = 0;
        new Constant(ImageWiseAddToCartActivity.this).doFinish();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_checkstock:
                convertPackToLoose(stockList);
                break;
            case R.id.btn_addtocart:
                if(!unpackSizeList.isEmpty()&& !colorListUpdated.isEmpty()){
                    loadCustDiscLimit();
                }else{
                    toast.setText("Please Select Atleast One Colour");
                    toast.show();
                }
                break;
            case R.id.btn_looseorder:
                AddToCartActivity.activityToFrom = 3;
                AddToCartActivity.updateCustOrder = new CustomerOrderClass();
                AddToCartActivity.updateCustOrder.setOrderType("U");
                AddToCartActivity.updateCustOrder.setProductid(selProdId);
                AddToCartActivity.selProd = tv_prodname.getText().toString();
                Intent intent = new Intent(getApplicationContext(), AddToCartActivity.class);
                intent.putExtra("cat9",cat9);
                intent.putExtra("cat2",cat2);
                intent.putExtra("from", "prodsearch");
                finish();
                startActivity(intent);
                overridePendingTransition(R.anim.enter,R.anim.exit);
                break;
            case R.id.btn_new_cat:
                new Constant(ImageWiseAddToCartActivity.this).doFinish();
                break;
        }
    }

    @Override
    public void onItemClick(String size) {
        Constant.showLog(size);
        if(size.equals("A")){
            if (selQtyLocal != compPackQty) {
                gridView.setVisibility(View.VISIBLE);
                lay_comp_pack.setVisibility(View.GONE);
                checkLooseStock();
            } else {
                gridView.setVisibility(View.GONE);
                lay_comp_pack.setVisibility(View.VISIBLE);
                setAdapter();
            }
        }else {
            if (!size.equalsIgnoreCase("NA")) {
                selQtyLocal = Integer.parseInt(size);
                if (selQtyLocal != compPackQty) {
                    gridView.setVisibility(View.VISIBLE);
                    lay_comp_pack.setVisibility(View.GONE);
                    convertPackToLoose(stockList);
                } else {
                    gridView.setVisibility(View.GONE);
                    lay_comp_pack.setVisibility(View.VISIBLE);
                    setAdapter();
                }
            } else {
                toast.setText("No Colour Available");
                toast.show();
            }
        }
    }

    @Override
    public void onImageClick(ImagewiseAddToCartClass prod) {
        Intent intent = new Intent(getApplicationContext(),CategoryWiseImageActivity.class);
        intent.putExtra("data", prod);
        startActivity(intent);
        overridePendingTransition(R.anim.enter,R.anim.exit);
    }

    @Override
    public void onSizeGroupClick(String sizeGroup) {
        selSizeGroup = sizeGroup;
        setSizeGroupWiseImage(sizeGroup);
        setSizeData(sizeGroup);
    }

    private void init(){
        FirstActivity.pref = getSharedPreferences(FirstActivity.PREF_NAME,MODE_PRIVATE);
        toast = Toast.makeText(getApplicationContext(),"File Not Found",Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER,0,0);
        constant = new Constant(ImageWiseAddToCartActivity.this);
        db = new DBHandler(getApplicationContext());
        tv_prodname = (TextView) findViewById(R.id.tv_prodname);
        tv_wsp = (TextView) findViewById(R.id.tv_wsp);
        tv_mrp = (TextView) findViewById(R.id.tv_mrp);
        tv_hsncode = (TextView) findViewById(R.id.tv_hsncode);
        tv_gstper = (TextView) findViewById(R.id.tv_gstper);
        tv_gstprint = (TextView) findViewById(R.id.tv_gstprint);
        tv_marginup = (TextView) findViewById(R.id.tv_marginup);
        tv_totqty = (TextView) findViewById(R.id.tv_tqty);
        tv_totamnt = (TextView) findViewById(R.id.tv_tamnt);
        tv_totset = (TextView) findViewById(R.id.tv_tset);
        tv_totnetamt = (TextView) findViewById(R.id.tv_namnt);
        tv_margindown = (TextView) findViewById(R.id.tv_margindown);
        FirstActivity.pref = getSharedPreferences(FirstActivity.PREF_NAME,MODE_PRIVATE);
        btn_addToCart = (Button) findViewById(R.id.btn_addtocart);
        btn_looseOrder = (Button) findViewById(R.id.btn_looseorder);
        btn_newCat = (Button) findViewById(R.id.btn_new_cat);
        btn_checkstock = (Button) findViewById(R.id.btn_checkstock);
        rv_images = (RecyclerView) findViewById(R.id.rv_images);
        rv_size = (RecyclerView) findViewById(R.id.rv_size);
        rv_sizegroup = (RecyclerView) findViewById(R.id.rv_sizegroup);
        sp_sizeGroup = (Spinner) findViewById(R.id.sp_sizegroup);
        gridView = (GridView) findViewById(R.id.gridView);
        sizeGroup_list = new ArrayList<>();
        size_list = new ArrayList<>();
        map = new HashMap<>();
        colour_list = new ArrayList<>();
        stockList = new ArrayList<>();
        unpackSizeList = new ArrayList<>();
        colorListUpdated = new ArrayList<>();
        lay_comp_pack = (LinearLayout) findViewById(R.id.lay_comp_pack);
        auto_set = (AutoCompleteTextView)findViewById(R.id.auto_comppack_set);

        AddToCartActivity.unClickablePositionsList = new ArrayList<>();
        AddToCartActivity.allSizeChangeList = new ArrayList<>();

        btn_addToCart.setOnClickListener(this);
        btn_looseOrder.setOnClickListener(this);
        btn_newCat.setOnClickListener(this);
        btn_checkstock.setOnClickListener(this);
        AddToCartActivity.selQty = 0;
    }

    private void setData(){
        sizeGroup_list.clear();
        colour_list.clear();
        AddToCartActivity.unClickablePositionsList.clear();
        AddToCartActivity.allSizeChangeList.clear();
        map.clear();

        int parentSizeGroup = -1, _flag = 1;
        Cursor res2 = db.getDistinctSizeGroup("M");
        if (res2.moveToFirst()) {
            do {
                String _sizeGroup = res2.getString(res2.getColumnIndex(DBHandler.ARSD_SizeGroup));
                if(_flag==1){
                    parentSizeGroup++;
                }
                if(_sizeGroup.equals(gentClass.getCat3())){
                   _flag = 0;
                    selSizeGroup = gentClass.getCat3();
                }
                sizeGroup_list.add(_sizeGroup);
            } while (res2.moveToNext());
        } else {
            sizeGroup_list.add("NA");
        }
        res2.close();
        SizeGroupRecyclerAdapter adapter = new SizeGroupRecyclerAdapter(sizeGroup_list, getApplicationContext(), "U", parentSizeGroup);
        adapter.setOnClickListener1(this);
        rv_sizegroup.setAdapter(adapter);
        setSizeGroupWiseImage(gentClass.getCat3());
        setSizeData(gentClass.getCat3());

    }

    private void setSizeGroupWiseImage(String selSizeGroup){
        colour_list.clear();
        AddToCartActivity.unClickablePositionsList.clear();
        AddToCartActivity.allSizeChangeList.clear();
        map.clear();
        int pos = 0;
        Cursor res1 = db.getDistinctColour(selSizeGroup,"M",1);
        if (res1.moveToFirst()) {
            do {
                String colourHashcode = res1.getString(res1.getColumnIndex(DBHandler.ARSD_Colour)) +
                        "-" +
                        res1.getString(res1.getColumnIndex(DBHandler.ARSD_HashCode));
                colour_list.add(colourHashcode);
                map.put(pos,1);
                pos ++;
            } while (res1.moveToNext());
        } else {
            colour_list.add("NA");
        }
        res1.close();

        rv_images.setAdapter(null);
        List<ImagewiseAddToCartClass> prodList = new ArrayList<>();
        Cursor res = db.getDistinctColourImageWise(gentClass.getProdId());
        if(res.moveToFirst()){
            do{
                ImagewiseAddToCartClass image = new ImagewiseAddToCartClass();
                //image.setImageName(res.getString(res.getColumnIndex(DBHandler.ARSD_ImageName)));
                String imageName = gentClass.getProductId()+"_"+cat2+"_"
                        +res.getString(res.getColumnIndex(DBHandler.ARSD_Colour))+"_P1"+","+
                        gentClass.getProductId()+"_"+cat2+"_"
                        +res.getString(res.getColumnIndex(DBHandler.ARSD_Colour))+"_P2"+","+
                        gentClass.getProductId()+"_"+cat2+"_"
                        +res.getString(res.getColumnIndex(DBHandler.ARSD_Colour))+"_P3"+","+
                        gentClass.getProductId()+"_"+cat2+"_"
                        +res.getString(res.getColumnIndex(DBHandler.ARSD_Colour))+"_P4"+",NA";
                imageName = imageName.replace(" ", "%20");
                imageName = imageName.replace("+", "_Pls");
                image.setImageName(imageName);
                image.setColour(res.getString(res.getColumnIndex(DBHandler.ARSD_Colour)));
                image.setHashCode(res.getString(res.getColumnIndex(DBHandler.ARSD_HashCode)));
                prodList.add(image);
            }while (res.moveToNext());
        }
        res.close();
        ImageWiseAddToCartRecyclerAdapter adapter = new ImageWiseAddToCartRecyclerAdapter(getApplicationContext(), prodList);
        adapter.setOnClickListener1(this);
        rv_images.setAdapter(adapter);
    }

    private void setSizeData(String sizegroup) {
        size_list.clear();
        rv_size.setAdapter(null);

        Cursor res1 = db.getDistinctSizes("M", sizegroup);
        if (res1.moveToFirst()) {
            do {
                String qty = res1.getString(res1.getColumnIndex(DBHandler.ARSD_Total));
                size_list.add(qty);
                compPackQty = Integer.parseInt(qty);
            } while (res1.moveToNext());
        } else {
            size_list.add("NA");
        }
        res1.close();
        if (size_list.size() != 0) {
            SizeGroupWiseQtyAdapter adapter = new SizeGroupWiseQtyAdapter(size_list, getApplicationContext(), "U", 0);
            adapter.setOnClickListener1(this);
            AddToCartActivity.selQty = 0;
            rv_size.setAdapter(adapter);
        } else {
            toast.setText("No Qty Available");
            toast.show();
        }

        checkLooseStock();
    }

    private void setAdapter() {
        auto_set.setAdapter(null);
        String[] arr = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.custom_spinner, arr);
        auto_set.setAdapter(adapter);
        auto_set.setSingleLine();
        auto_set.setText(arr[0]);
        auto_set.setSelection(auto_set.getText().length());
    }

    private void checkLooseStock() {
        try {
            String branchid, prodid, sizegroup, color = "", hashcode = "", enterqty, rate, type = "";
            branchid = String.valueOf(getDispatchCenterId());
            prodid = String.valueOf(selProdId);
            sizegroup = "0";

            int flag = 0;
            if (selQtyLocal != compPackQty) {
                type = "M";
            } else {
                type = "C";
                //sizegroup = sizeGroup_list.get(sp_sizeGroup.getSelectedItemPosition());
                sizegroup = selSizeGroup;
            }
            if (validateOrder()) {
                flag = 1;
                Set<Integer> set = map.keySet();
                for (Integer pos : set) {
                    int isSelected = map.get(pos);
                    if (isSelected == 1) {
                        String colorStr = colour_list.get(pos);
                        String colorArr[] = colorStr.split("\\-");
                        color = color + "'" + colorArr[0] + "',";
                        hashcode = hashcode + "'" + colorArr[1] + "',";
                    }
                }
            } else {
                flag = 0;
                showToast("Please Select Item");
            }

            if (flag != 0) {
                if (!color.equals("")) {
                    color = color.substring(0, color.length() - 1);

                    if (!hashcode.equals("")) {
                        hashcode = hashcode.substring(0, hashcode.length() - 1);
                    }
                    enterqty = "0";
                    rate = tv_mrp.getText().toString();
                    String data = branchid + "^" + prodid + "^" + sizegroup + "^" + color + "^" + hashcode + "^" + enterqty + "^" + rate + "^" + type;
                    Constant.showLog(data);
                    data = URLEncoder.encode(data, "UTF-8");
                    checkLooseStock(data);
                }
            } else {
                showToast("Please Select Order Type");
            }
        } catch (Exception e) {
            e.printStackTrace();
            writeLog("checkLooseStock_"+e.getMessage());
        }
    }

    private void checkLooseStock(String data) {
        String url = Constant.ipaddress+"/CheckLooseStock?data="+data;
        Constant.showLog(url);
        writeLog("checkLooseStock_" + url);
        constant = new Constant(ImageWiseAddToCartActivity.this);
        constant.showPD();
        stockList.clear();
        VolleyRequests requests = new VolleyRequests(ImageWiseAddToCartActivity.this);
        requests.getAvailStock(url, new ServerCallbackList() {
            @Override
            public void onSuccess(Object result) {
                constant.showPD();
                stockList = (List<CheckAvailStockClass>) result;
                if(!stockList.isEmpty()) {
                    convertPackToLoose(stockList);
                }else{
                    toast.setText("No Record Available");
                    toast.show();
                }
            }
            @Override
            public void onFailure(Object result) {
                toast.setText("No Record Available");
                toast.show();
                constant.showPD();
                colorListUpdated.clear();
                unpackSizeList.clear();
                gridView.setAdapter(null);
            }
        });
    }

    private int getDispatchCenterId(){
        int hocode = FirstActivity.pref.getInt(getString(R.string.pref_hocode), 0);
        String prodCol = "";
        if (hocode == 1) {
            prodCol = DBHandler.PM_HKHO;
        } else if (hocode == 12) {
            prodCol = DBHandler.PM_HKRD;
        } else if (hocode == 13) {
            prodCol = DBHandler.PM_HANR;
        }
        return db.getDispatchCenter(prodCol);
    }

    private void convertPackToLoose(List<CheckAvailStockClass> stockList){
        try {
            int unClickCount = -1;
            colorListUpdated.clear();
            unpackSizeList.clear();
            gridView.setAdapter(null);
            int part = Integer.parseInt(size_list.get(AddToCartActivity.selQty));
            String designNo = db.getDesignNo(selSizeGroup, part);
            List<String> sizeQtyList = db.getSizeDetails(selSizeGroup, designNo, part);

            List<String> qtyList = new ArrayList<>();
            List<String> sizeList = new ArrayList<>();
            HashMap<String, String> sizeQtyMap = new HashMap<>();
            HashMap<String, List<String>> sizeColorMap = new HashMap<>();
            HashMap<String, List<String>> sizeQtyHashMap = new HashMap<>();
            List<String> sizeGroupList = new ArrayList<>();
            List<String> sizeGroupListAvail = new ArrayList<>();

            String size = "";
            for (int i = 0; i < sizeQtyList.size(); i++) {
                String sizeQty = sizeQtyList.get(i);
                String sizeQtyArr[] = sizeQty.split("\\^");
                if (i == 0) {
                    size = sizeQtyArr[0];
                }
                sizeGroupListAvail.add(sizeQtyArr[0]);
                qtyList.add(sizeQtyArr[1]);
                sizeQtyMap.put(sizeQtyArr[0], sizeQtyArr[1]);
            }

            List<String> colorList = new ArrayList<>();

            Set<Integer> set = map.keySet();
            for (Integer pos : set) {
                int isSelected = map.get(pos);
                if (isSelected == 1) {
                    String colStr = colour_list.get(pos);
                    String colArr[] = colStr.split("\\-");
                    String hashCode = colArr[1];
                    String col = db.getDistinctColour(size, hashCode);
                    if (!col.equals("")) {
                        colorList.add(col);
                    }
                }
            }

            sizeColorMap.clear();
            sizeList.clear();
            for (int i = 0; i < sizeQtyList.size(); i++) {
                String sizeQty = sizeQtyList.get(i);
                String sizeQtyArr[] = sizeQty.split("\\^");
                size = sizeQtyArr[0];
                if(!sizeList.contains(size)){
                    sizeList.add(size);
                }
                for(String col : colorList){
                    if (sizeColorMap.isEmpty()) {
                        List<String> list = new ArrayList<>();
                        list.add(col);
                        sizeColorMap.put(size, list);
                    } else if (sizeColorMap.containsKey(size)) {
                        List<String> list = sizeColorMap.get(size);
                        list.add(col);
                        sizeColorMap.put(size, list);
                    } else {
                        List<String> list = new ArrayList<>();
                        list.add(col);
                        sizeColorMap.put(size, list);
                    }
                }
            }


            qtyList.clear();
            for (int i = 0; i < stockList.size(); i++) {
                CheckAvailStockClass stock = stockList.get(i);
                String colCode = stock.getColor() + "-" + stock.getHashcode();
                for (String col : colorList) {
                    if (col.equalsIgnoreCase(colCode)) {
                        String availSize = stock.getSizegroup();
                        if (sizeGroupListAvail.contains(availSize)) {
                            if(colorListUpdated.isEmpty() || !colorListUpdated.contains(col)) {
                                colorListUpdated.add(col);
                            }
                            sizeGroupList.add(availSize);
                            if (stock.getStat().equals("N")) {
                                if (sizeQtyHashMap.isEmpty()) {
                                    List<String> list = new ArrayList<>();
                                    list.add(sizeQtyMap.get(availSize)+"^" + stock.getColor() +"^N" +  "-" + stock.getHashcode());
                                    //list.add("N" + "^" + "Red" + "-" + "#FF0000");
                                    //list.add(sizeQtyMap.get(availSize) + "^" + "Red" + "-" + "#FF0000");
                                    sizeQtyHashMap.put(availSize, list);
                                } else if (sizeQtyHashMap.containsKey(availSize)) {
                                    List<String> list = sizeQtyHashMap.get(availSize);
                                    list.add(sizeQtyMap.get(availSize)+"^" + stock.getColor() +"^N" + "-" + stock.getHashcode());
                                    //list.add("N" + "^" + "Red" + "-" + "#FF0000");
                                    //list.add(sizeQtyMap.get(availSize) + "^" + "Red" + "-" + "#FF0000");
                                    sizeQtyHashMap.put(availSize, list);
                                } else {
                                    List<String> list = new ArrayList<>();
                                    list.add(sizeQtyMap.get(availSize)+"^" + stock.getColor() +"^N" + "-" + stock.getHashcode());
                                    //list.add("N" + "^" + "Red" + "-" + "#FF0000");
                                    //list.add(sizeQtyMap.get(availSize) + "^" + "Red" + "-" + "#FF0000");
                                    sizeQtyHashMap.put(availSize, list);
                                }
                                qtyList.add("0");
                            } else {
                                if (sizeQtyHashMap.isEmpty()) {
                                    List<String> list = new ArrayList<>();
                                    list.add(sizeQtyMap.get(availSize) + "^" + stock.getColor() +"^Y" + "-" + stock.getHashcode());
                                    //list.add("Y" + "^" + "Green" + "-" + "#00A933");
                                    //list.add(sizeQtyMap.get(availSize) + "^" + "Green" + "-" + "#00A933");
                                    sizeQtyHashMap.put(availSize, list);
                                } else if (sizeQtyHashMap.containsKey(availSize)) {
                                    List<String> list = sizeQtyHashMap.get(availSize);
                                    list.add(sizeQtyMap.get(availSize) + "^" + stock.getColor() +"^Y" + "-" + stock.getHashcode());
                                    //list.add("Y" + "^" + "Green" + "-" + "#00A933");
                                    //list.add(sizeQtyMap.get(availSize) + "^" + "Green" + "-" + "#00A933");
                                    sizeQtyHashMap.put(availSize, list);
                                } else {
                                    List<String> list = new ArrayList<>();
                                    list.add(sizeQtyMap.get(availSize) + "^" + stock.getColor() +"^Y" + "-" + stock.getHashcode());
                                    //list.add("Y" + "^" + "Green" + "-" + "#00A933");
                                    //list.add(sizeQtyMap.get(availSize) + "^" + "Green" + "-" + "#00A933");
                                    sizeQtyHashMap.put(availSize, list);
                                }
                                qtyList.add(sizeQtyMap.get(availSize));
                            }
                        }
                    }
                }
            }

            int gridColumns = colorListUpdated.size() + 1;
            gridView.setNumColumns(gridColumns);

            unpackSizeList.clear();
            unpackSizeList.add("+2");
            for (int i = 0; i < colorListUpdated.size(); i++) {
                unClickCount++;
                AddToCartActivity.allSizeChangeList.add(unClickCount);
                unpackSizeList.add(colorListUpdated.get(i));
            }

            Set<String> keySet = sizeQtyHashMap.keySet();
            /*List<String> sortedList = new ArrayList(keySet);
            Collections.sort(sortedList);*/
            for (String size1 : keySet) {
                unpackSizeList.add(size1);
                List<String> list = sizeQtyHashMap.get(size1);
                int colorListSize = colorListUpdated.size();
                int dataset = 0;
                String color = "";
                if (list.size() < colorListSize) {
                    dataset = 1;
                }
                List<String> _colList = new ArrayList<>();
                List<String> _addedColList = new ArrayList<>();
                for (int j = 0; j < list.size(); j++) {
                    String scolor = list.get(j);
                    String scolArr[] = scolor.split("\\^");
                    String col = scolArr[1];
                    _colList.add(col);
                }
                for (String _col : colorListUpdated) {
                    String[] _colArr = _col.split("\\-");
                    if (!_colList.contains(_colArr[0])) {
                        String put = 0 + "^" + _colArr[0] + "^N-" + _colArr[1];
                        unpackSizeList.add(put);
                    }else{
                        for (int j = 0; j < list.size(); j++) {
                            String scolor = list.get(j);
                            String scolArr[] = scolor.split("\\^");
                            color = scolArr[1];
                            if(!_addedColList.contains(color)) {
                                if (_colList.contains(color)) {
                                    unpackSizeList.add(list.get(j));
                                    _addedColList.add(color);
                                    break;
                                }
                            }
                        }
                    }
                }
                /*if (dataset == 1) {
                    int a = colorListSize - list.size();
                    for (int b = 0; b < a; b++) {
                        unpackSizeList.add("0^" + color+"^N-#000000");
                    }
                }*/
                /*for (int j = 0; j < list.size(); j++) {
                    String scolor = list.get(j);
                    String scolArr[] = scolor.split("\\^");
                    color = scolArr[1];
                    unpackSizeList.add(list.get(j));
                }
                if (dataset == 1) {
                    int a = colorListSize - list.size();
                    for (int b = 0; b < a; b++) {
                        unpackSizeList.add("0^" + color+"^N-#000000");
                    }
                }*/
            }
            gridView.setAdapter(new CustomerOrderUnpackGridAdpater(getApplicationContext(), unpackSizeList, 1));
        }catch (Exception e){
            e.printStackTrace();
            writeLog("convertPackToLoose_"+e.getMessage());
        }
    }

    private void loadCustDiscLimit() {
        int custId = FirstActivity.pref.getInt(getString(R.string.pref_selcustid), 0);
        int hocode = FirstActivity.pref.getInt(getString(R.string.pref_hocode), 0);
        String prodCol = "";
        if (hocode == 1) {
            prodCol = DBHandler.PM_HKHO;
        } else if (hocode == 12) {
            prodCol = DBHandler.PM_HKRD;
        } else if (hocode == 13) {
            prodCol = DBHandler.PM_HANR;
        }
        int branchId = db.getDispatchCenter(prodCol);
        String url = Constant.ipaddress + "/GetCustDiscLimit?custId="+custId+"&banchId="+branchId;
        Constant.showLog(url);
        writeLog("GetCustDiscLimit_" + url);
        constant = new Constant(ImageWiseAddToCartActivity.this);
        constant.showPD();
        VolleyRequests requests = new VolleyRequests(ImageWiseAddToCartActivity.this);
        requests.getCustDiscLimit(url, new ServerCallback() {
            @Override
            public void onSuccess(String result) {
                constant.showPD();
                OptionsActivity.custDisc = Float.parseFloat(result);
                if (selQtyLocal != compPackQty) {
                    InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    mgr.hideSoftInputFromWindow(tv_gstper.getWindowToken(), 0);
                    addToCardAfterConversion();
                } else {
                    String str = auto_set.getText().toString();
                    if (!str.equals("")) {
                        selQtyLocal = Integer.parseInt(auto_set.getText().toString());
                        if (selQtyLocal != 0) {
                            InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            mgr.hideSoftInputFromWindow(tv_gstper.getWindowToken(), 0);
                            addToCardCompPack();
                        } else {
                            showToast("Please Enter Non Zero Value");
                        }
                    } else {
                        showToast("Please Enter Proper Value");
                    }
                }
            }
            @Override
            public void onFailure(String result) {
                constant.showPD();
                toast.setText("Please Try Again...");
                toast.show();
            }
        });
    }

    private void addToCardAfterConversion(){
        try {
            int counter = 0, isDataInserted = 0;
            HashMap<String, List<String>> output = new HashMap<>();
            for (int i = 0; i < unpackSizeList.size(); i++) {
                List<String> list = new ArrayList<>();
                for (int j = 0; j <= colorListUpdated.size(); j++) {
                    if (j == 0) {
                        output.put(unpackSizeList.get(counter), list);
                    } else {
                        String qtyCol = unpackSizeList.get(counter);
                        list.add(qtyCol);
                    }
                    counter++;
                }
                i = counter;
            }

            if (!output.isEmpty()) {
                String rate = tv_wsp.getText().toString();
                String mrp = tv_mrp.getText().toString();
                float floatRate = Float.parseFloat(rate);

                String gstgroupName = tv_gstper.getText().toString();
                float gstPer = 0, cgstPer = 0, sgstPer = 0, cessPer = 0, cgstShare = 0, sgstShare = 0;
                float amount = 0, amountAfterDisc = 0, _discAmnt = 0, gstAmt = 0, cgstAmt = 0, sgstAmt = 0, cessAmt = 0, igstAmt = 0, totalAmt;
                String gstStatus = "";
                Cursor res = db.getGSTDetails(gstgroupName);
                if (res.moveToFirst()) {
                    gstStatus = res.getString(res.getColumnIndex(DBHandler.GST_Status));
                    gstPer = res.getFloat(res.getColumnIndex(DBHandler.GST_GSTPer));
                    cgstPer = res.getFloat(res.getColumnIndex(DBHandler.GST_CGSTPer));
                    sgstPer = res.getFloat(res.getColumnIndex(DBHandler.GST_SGSTPer));
                    cessPer = res.getFloat(res.getColumnIndex(DBHandler.GST_CESSPer));
                    cgstShare = res.getFloat(res.getColumnIndex(DBHandler.GST_CGSTSHARE));
                    sgstShare = res.getFloat(res.getColumnIndex(DBHandler.GST_SGSTSHARE));
                }
                res.close();

                if (gstStatus.equalsIgnoreCase("A")) {
                    int hocode = FirstActivity.pref.getInt(getString(R.string.pref_hocode), 0);
                    String prodCol = "";
                    if (hocode == 1) {
                        prodCol = DBHandler.PM_HKHO;
                    } else if (hocode == 12) {
                        prodCol = DBHandler.PM_HKRD;
                    } else if (hocode == 13) {
                        prodCol = DBHandler.PM_HANR;
                    }
                    int branchId = db.getDispatchCenter(prodCol);

                    Set<String> set = output.keySet();
                    for (String sizeGroup : set) {
                        if (!sizeGroup.equals("+2") && !sizeGroup.equals("+1")) {
                            List<String> list = output.get(sizeGroup);
                            for (int i = 0; i < list.size(); i++) {
                                String qtyColourStr = list.get(i);
                                String qtyColour[] = qtyColourStr.split("\\^");
                                int looseQty = Integer.parseInt(qtyColour[0]);
                                String colour = qtyColour[1];
                                String isAvailHashCode = qtyColour[2];
                                String colourhashCode[] = isAvailHashCode.split("\\-");
                                String isAvail = colourhashCode[0];
                                String hashCode = colourhashCode[1];
                                if (looseQty != 0) {
                                    if (isAvail.equals("Y")) {
                                        if (isDataInserted == 0) {
                                            isDataInserted = 1;
                                        }
                                        amount = floatRate * looseQty;
                                        if (OptionsActivity.custDisc != 0) {
                                            _discAmnt = (amount * OptionsActivity.custDisc) / 100;
                                            _discAmnt = round(_discAmnt, 2);
                                            amountAfterDisc = amount - _discAmnt;
                                        } else {
                                            amountAfterDisc = amount;
                                        }

                                        amount = round(amount, 2);
                                        amountAfterDisc = round(amountAfterDisc, 2);

                                        gstAmt = (amountAfterDisc * gstPer) / 100;
                                        gstAmt = round(gstAmt, 2);
                                        totalAmt = amountAfterDisc + gstAmt;
                                        totalAmt = round(totalAmt, 2);

                                        cgstAmt = (amountAfterDisc * cgstPer) / 100;
                                        cgstAmt = round(cgstAmt, 2);
                                        sgstAmt = (amountAfterDisc * sgstPer) / 100;
                                        sgstAmt = round(sgstAmt, 2);
                                        cessAmt = (amountAfterDisc * cessPer) / 100;
                                        cessAmt = round(cessAmt, 2);

                                        CustomerOrderClass custOrder = new CustomerOrderClass();
                                        int auto = db.getCustOrderMax();
                                        custOrder.setAuto(auto);
                                        custOrder.setBranchId(branchId);
                                        custOrder.setProductid(selProdId);
                                        custOrder.setSizeGroup(sizeGroup);
                                        custOrder.setRequiredSize(sizeGroup);
                                        custOrder.setPerPackQty(1);
                                        custOrder.setColor(colour);
                                        custOrder.setHashCode(hashCode);
                                        custOrder.setRate(rate);
                                        custOrder.setMrp(mrp);
                                        custOrder.setQty(looseQty);
                                        custOrder.setLooseQty(looseQty);
                                        custOrder.setActLooseQty(looseQty);
                                        custOrder.setAmount(String.valueOf(amount));
                                        custOrder.setLoosePackTyp("Unpack");
                                        custOrder.setPendingLooseQty(looseQty);
                                        custOrder.setTotalamt(String.valueOf(totalAmt));
                                        custOrder.setNetamnt(String.valueOf(totalAmt));
                                        custOrder.setAmtAfterDisc(String.valueOf(amountAfterDisc));
                                        custOrder.setGstper(String.valueOf(gstPer));
                                        custOrder.setGstAmt(String.valueOf(gstAmt));
                                        custOrder.setCgstamt(String.valueOf(cgstAmt));
                                        custOrder.setSgstamt(String.valueOf(sgstAmt));
                                        custOrder.setIgstamt(String.valueOf(igstAmt));
                                        custOrder.setCgstper(String.valueOf(cgstPer));
                                        custOrder.setSgstper(String.valueOf(sgstPer));
                                        custOrder.setCessper(String.valueOf(cessPer));
                                        custOrder.setCessamt(String.valueOf(cessAmt));
                                        custOrder.setDiscamnt(String.valueOf(_discAmnt));
                                        custOrder.setDiscPer(String.valueOf(OptionsActivity.custDisc));
                                        custOrder.setOrderType("U");
                                        custOrder.setAvailQty(0);
                                        custOrder.setProdId(prodIdStr);
                                        db.addCustomerOrder(custOrder);

                                        Constant.showLog("HOCODE-" + hocode + "-ProdCol-" + prodCol + "-Auto-" + auto + "-BranchId-" + branchId +
                                                "-SelProdId-" + selProdId + "-SizeGroup-" + sizeGroup + "-SelColor-" + colour +
                                                "-RequiredSize-" + sizeGroup + "-PerPackQty-1" +
                                                "-SelQty-" + looseQty + "-MRP-" + mrp + "-Amount-" + amount + "-TotalAmt-" + totalAmt + "-GSTPer-" + gstPer +
                                                "-CGSTAMt-" + cgstAmt + "-SGSTAMt-" + sgstAmt + "-IGSTAMt-" + igstAmt + "-CGSTPer-" + cgstPer + "-SGSTPer-" + sgstPer +
                                                "-CESSPer-" + cessPer + "-CESSAmt-" + cessAmt + "-DiscPer-" + OptionsActivity.custDisc + "-DiscAmnt-" + _discAmnt);
                                    }
                                }
                            }
                        }

                    }
                    if (isDataInserted == 1) {
                        totalCalculations();
                        showDia(1);
                    } else {
                        showToast("Please Enter Qty");
                    }
                } else {
                    showToast("Please Refresh GSTMaster");
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            writeLog("addToCardAfterConversion_"+e.getMessage());
        }
    }

    private void addToCardCompPack() {
        try {
            String rate = tv_wsp.getText().toString();
            String mrp = tv_mrp.getText().toString();
            float floatRate = Float.parseFloat(rate);

            String gstgroupName = tv_gstper.getText().toString();
            float gstPer = 0, cgstPer = 0, sgstPer = 0, cessPer = 0, cgstShare = 0, sgstShare = 0;
            float amount = 0, amountAfterDisc = 0, _discAmnt = 0, gstAmt = 0, cgstAmt = 0, sgstAmt = 0, cessAmt = 0, igstAmt = 0, totalAmt;
            String gstStatus = "";
            Cursor res = db.getGSTDetails(gstgroupName);
            if (res.moveToFirst()) {
                gstStatus = res.getString(res.getColumnIndex(DBHandler.GST_Status));
                gstPer = res.getFloat(res.getColumnIndex(DBHandler.GST_GSTPer));
                cgstPer = res.getFloat(res.getColumnIndex(DBHandler.GST_CGSTPer));
                sgstPer = res.getFloat(res.getColumnIndex(DBHandler.GST_SGSTPer));
                cessPer = res.getFloat(res.getColumnIndex(DBHandler.GST_CESSPer));
                cgstShare = res.getFloat(res.getColumnIndex(DBHandler.GST_CGSTSHARE));
                sgstShare = res.getFloat(res.getColumnIndex(DBHandler.GST_SGSTSHARE));
            }
            res.close();

            if (gstStatus.equalsIgnoreCase("A")) {
                int looseQty = selQtyLocal * compPackQty;
                amount = floatRate * looseQty;

                if (OptionsActivity.custDisc != 0) {
                    _discAmnt = (amount * OptionsActivity.custDisc) / 100;
                    _discAmnt = round(_discAmnt, 2);
                    amountAfterDisc = amount - _discAmnt;
                } else {
                    amountAfterDisc = amount;
                }

                amount = round(amount, 2);
                amountAfterDisc = round(amountAfterDisc, 2);

                gstAmt = (amountAfterDisc * gstPer) / 100;
                gstAmt = round(gstAmt, 2);

                totalAmt = amountAfterDisc + gstAmt;
                totalAmt = round(totalAmt, 2);

                cgstAmt = (amountAfterDisc * cgstPer) / 100;
                cgstAmt = round(cgstAmt, 2);

                sgstAmt = (amountAfterDisc * sgstPer) / 100;
                sgstAmt = round(sgstAmt, 2);

                cessAmt = (amountAfterDisc * cessPer) / 100;
                cessAmt = round(cessAmt, 2);

                int hocode = FirstActivity.pref.getInt(getString(R.string.pref_hocode), 0);
                String prodCol = "";
                if (hocode == 1) {
                    prodCol = DBHandler.PM_HKHO;
                } else if (hocode == 12) {
                    prodCol = DBHandler.PM_HKRD;
                } else if (hocode == 13) {
                    prodCol = DBHandler.PM_HANR;
                }
                int branchId = db.getDispatchCenter(prodCol);
                //String sizeGroup = sizeGroup_list.get(sp_sizeGroup.getSelectedItemPosition());
                String sizeGroup = selSizeGroup;

                Set<Integer> set = map.keySet();
                for (Integer pos : set) {
                    int isSelected = map.get(pos);
                    if (isSelected == 1) {
                        String colo = colour_list.get(pos);
                        String[] colour = colo.split("\\-");
                        String requiredSize, total;
                        String requiredSizeNTotal = db.getRequiredSize(sizeGroup, colour[0]);
                        if (!requiredSizeNTotal.equals("")) {
                            String[] arr = requiredSizeNTotal.split("\\^");
                            if (arr.length > 1) {
                                requiredSize = arr[0];
                                total = arr[1];

                                CustomerOrderClass custOrder = new CustomerOrderClass();
                                int auto = db.getCustOrderMax();
                                custOrder.setAuto(auto);
                                custOrder.setBranchId(branchId);
                                custOrder.setProductid(selProdId);
                                custOrder.setSizeGroup(sizeGroup);
                                custOrder.setRequiredSize(requiredSize);
                                custOrder.setPerPackQty(Integer.parseInt(total));
                                custOrder.setColor(colour[0]);
                                custOrder.setHashCode(colour[1]);
                                custOrder.setRate(rate);
                                custOrder.setMrp(mrp);
                                custOrder.setQty(selQtyLocal);
                                custOrder.setLooseQty(looseQty);
                                custOrder.setActLooseQty(looseQty);
                                custOrder.setAmount(String.valueOf(amount));
                                custOrder.setLoosePackTyp("Pack");
                                custOrder.setPendingLooseQty(looseQty);
                                custOrder.setTotalamt(String.valueOf(totalAmt));
                                custOrder.setNetamnt(String.valueOf(totalAmt));
                                custOrder.setAmtAfterDisc(String.valueOf(amountAfterDisc));
                                custOrder.setGstper(String.valueOf(gstPer));
                                custOrder.setGstAmt(String.valueOf(gstAmt));
                                custOrder.setCgstamt(String.valueOf(cgstAmt));
                                custOrder.setSgstamt(String.valueOf(sgstAmt));
                                custOrder.setIgstamt(String.valueOf(igstAmt));
                                custOrder.setCgstper(String.valueOf(cgstPer));
                                custOrder.setSgstper(String.valueOf(sgstPer));
                                custOrder.setCessper(String.valueOf(cessPer));
                                custOrder.setCessamt(String.valueOf(cessAmt));
                                custOrder.setDiscamnt(String.valueOf(_discAmnt));
                                custOrder.setDiscPer(String.valueOf(OptionsActivity.custDisc));
                                custOrder.setOrderType("C");
                                custOrder.setAvailQty(0);
                                custOrder.setProdId(prodIdStr);
                                db.addCustomerOrder(custOrder);

                                Constant.showLog("HOCODE-" + hocode + "-ProdCol-" + prodCol + "-Auto-" + auto + "-BranchId-" + branchId +
                                        "-SelProdId-" + selProdId + "-SizeGroup-" + sizeGroup + "-SelColor-" + colour[0] +
                                        "-RequiredSize-" + requiredSize + "-PerPackQty-" + total +
                                        "-SelQty-" + selQtyLocal + "-MRP-" + mrp + "-Amount-" + amount + "-TotalAmt-" + totalAmt + "-GSTPer-" + gstPer +
                                        "-CGSTAMt-" + cgstAmt + "-SGSTAMt-" + sgstAmt + "-IGSTAMt-" + igstAmt + "-CGSTPer-" + cgstPer + "-SGSTPer-" + sgstPer +
                                        "-CESSPer-" + cessPer + "-CESSAmt-" + cessAmt + "-DiscPer-" + OptionsActivity.custDisc + "-DiscAmnt-" + _discAmnt);
                            } else {
                                showToast("Something Went Wrong");
                            }
                        }
                    }
                }
                totalCalculations();
                showDia(1);
            } else {
                showToast("Please Update GST Master");
            }
        } catch (Exception e) {
            e.printStackTrace();
            writeLog("addToCardCompPack_" + e.getMessage());
        }
    }

    private void totalCalculations() {

        String totalSet = "0", totalQty = "0", totalAmnt = "0", totalNetAmnt = "0";

        Cursor res = db.getCustOrderTotals();
        if (res.moveToFirst()) {
            totalQty = res.getString(res.getColumnIndex(DBHandler.CO_LooseQty));
            totalSet = res.getString(res.getColumnIndex(DBHandler.CO_Auto));
            totalAmnt = res.getString(res.getColumnIndex(DBHandler.CO_Amount));
            totalNetAmnt = res.getString(res.getColumnIndex(DBHandler.CO_NetAmt));
        }
        res.close();

        if (totalQty == null) {
            totalQty = "0";
        }
        if (totalSet == null) {
            totalSet = "0";
        }
        if (totalAmnt == null) {
            totalAmnt = "0";
        }
        if (totalNetAmnt == null) {
            totalNetAmnt = "0";
        }

        tv_totqty.setText(totalQty);
        tv_totset.setText(totalSet);
        tv_totamnt.setText(totalAmnt);
        tv_totnetamt.setText(totalNetAmnt);

        if(actionbar_noti_tv!=null) {
            actionbar_noti_tv.setText(totalSet);
        }
    }

    private void startViewCustOrderActivity(){
        SharedPreferences.Editor editor = FirstActivity.pref.edit();
        editor.putString("totalNetAmnt",tv_totnetamt.getText().toString());
        editor.apply();
        AddToCartActivity.activityToFrom = 4;
        Intent intent = new Intent(getApplicationContext(), ViewCustomerOrderActiviy.class);
        intent.putExtra("from","addtocard");
        intent.putExtra("cat9",cat9);
        intent.putExtra("cat2",cat2);
        startActivity(intent);
        overridePendingTransition(R.anim.enter,R.anim.exit);
    }

    private float round(float d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd.floatValue();
    }

    private boolean validateOrder() {
        boolean stat = true;
        return stat;
    }

    private void showDia(int a) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ImageWiseAddToCartActivity.this);
        builder.setCancelable(false);
        if (a == 1) {
            builder.setTitle("Added To Cart");
            builder.setMessage("Order Added Successfully");
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        }
        builder.create().show();
    }

    private void showToast(String msg){
        toast.setText(msg);
        toast.show();
    }

    private void writeLog(String _data) {
        new WriteLog().writeLog(getApplicationContext(), "ImageWiseAddToCartActivity_" + _data);
    }

}
