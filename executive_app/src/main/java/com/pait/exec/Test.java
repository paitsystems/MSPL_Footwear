package com.pait.exec;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.pait.exec.constant.Constant;

import java.util.ArrayList;
import java.util.List;

public class Test extends AppCompatActivity {

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(Constant.liveTestFlag==1) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        }

        setContentView(R.layout.activity_test);

        listView = (ListView) findViewById(R.id.listView2);
        List<String> list = new ArrayList<>();
        list.add("String 1");list.add("String 2");list.add("String 3");list.add("String 4");
        list.add("String 5");list.add("String 6");list.add("String 7");list.add("String 8");
        list.add("String 9");list.add("String 10");list.add("String 11");list.add("String 12");
        list.add("String 13");list.add("String 14");list.add("String 15");
        listView.setAdapter(new Class1(list));
    }

    private class Class1 extends BaseAdapter{

        List<String> list;

        private Class1(List<String> _list){
            this.list = _list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public String getItem(int i) {
            return list.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if(view == null) {
                LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.test1, null);
            }
            TextView tv1 = (TextView) view.findViewById(R.id.tv1);
            TextView tv2 = (TextView) view.findViewById(R.id.tv2);
            TextView tv3 = (TextView) view.findViewById(R.id.tv3);
            TextView tv4 = (TextView) view.findViewById(R.id.tv4);
            TextView tv5 = (TextView) view.findViewById(R.id.tv5);
            TextView tv6 = (TextView) view.findViewById(R.id.tv6);
            TextView tv7 = (TextView) view.findViewById(R.id.tv7);
            TextView tv8 = (TextView) view.findViewById(R.id.tv8);
            TextView tv9 = (TextView) view.findViewById(R.id.tv9);
            TextView tv10 = (TextView) view.findViewById(R.id.tv10);
            TextView tv11 = (TextView) view.findViewById(R.id.tv11);
            TextView tv12 = (TextView) view.findViewById(R.id.tv12);
            TextView tv13 = (TextView) view.findViewById(R.id.tv13);
            TextView tv14 = (TextView) view.findViewById(R.id.tv14);
            TextView tv15 = (TextView) view.findViewById(R.id.tv15);

            tv1.setText(""+getItem(i));tv2.setText(""+getItem(i));
            tv3.setText(""+getItem(i));tv4.setText(""+getItem(i));
            tv5.setText(""+getItem(i));tv6.setText(""+getItem(i));
            tv7.setText(""+getItem(i));tv8.setText(""+getItem(i));
            tv9.setText(""+getItem(i));tv10.setText(""+getItem(i));
            tv11.setText(""+getItem(i));tv12.setText(""+getItem(i));
            tv13.setText(""+getItem(i));tv14.setText(""+getItem(i));
            tv15.setText(""+getItem(i));

            return view;
        }
    }
}
