package com.lnbinfotech.msplfootwear.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.lnbinfotech.msplfootwear.model.UserGetterSetterClass;

// Created by lnb on 8/11/2016.

public class DBHandler extends SQLiteOpenHelper {

    public static final String Database_Name = "TicketManager.db";
    public static final int Database_Version = 1;

    public static final String Table_Table   = "Tables";

    public DBHandler(Context context) {
        super(context, Database_Name, null, Database_Version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
    public void addProfileDetail(UserGetterSetterClass userClass){
        ContentValues cv = new ContentValues();
       // cv.put(,userClass.getMob_no());
       // cv.put(,userClass.getEmail_id());
         //cv.put(,userClass.getCc());
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(Table_Table,null,cv);
    }

}

