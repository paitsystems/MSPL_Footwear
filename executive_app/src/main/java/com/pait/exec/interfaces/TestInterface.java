package com.pait.exec.interfaces;

//Created by SNEHA on 12/4/2017.

import android.content.Context;

public interface TestInterface {

    void onResumeFragment(String data1, String data2, Context context);

    void onPauseFragment(String data1, String data2, Context context);

    void onAmountChange(int amnt);
}
