package com.example.ross.iotms;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBConnection extends SQLiteOpenHelper
{
    public static final String DATABASE_NAME = "devices.db";
    public static final String CONTROLLER_TABLE_NAME = "Controller";
    public static final String DEVICES_TABLE_NAME = "Devices";
    public static final String READINGS_TABLE_NAME = "Readings";

    public static final String C_COL_1 = "Controller_ID";
    public static final String C_COL_2 = "Username";
    public static final String C_COL_3 = "Password";

    public static final String D_COL_1 = "Device_ID";
    public static final String D_COL_2 = "Device_Name";
    public static final String D_COL_3 = "Device_Type";
    public static final String D_COL_4 = "Device_Description";

    public static final String R_COL_1 = "Reading_ID";
    public static final String R_COL_2 = "Device_ID";
    public static final String R_COL_3 = "Reading_DateTime";
    public static final String R_COL_4 = "Energy_Consumption";
    public static final String R_COL_5 = "Status";
    public static final String R_COL_6 = "Reading";

    public DBConnection(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + CONTROLLER_TABLE_NAME + " (CONTROLLER_ID INTEGER PRIMARY KEY AUTOINCREMENT,USERNAME TEXT,PASSWORD TEXT)");
        db.execSQL("create table " + DEVICES_TABLE_NAME + " (DEVICE_ID INTEGER PRIMARY KEY AUTOINCREMENT,DEVICE_NAME TEXT,DEVICE_TYPE TEXT, DEVICE_DESCRIPTION TEXT)");
        db.execSQL("create table " + READINGS_TABLE_NAME + " (READING_ID INTEGER PRIMARY KEY AUTOINCREMENT,DEVICE_ID INTEGER,READING_DATETIME DATE, ENERGY_CONSUMPTION DOUBLE, STATUS TEXT, READING DOUBLE)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + CONTROLLER_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DEVICES_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + READINGS_TABLE_NAME);
        onCreate(db);
    }

    public boolean addControllerData(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(C_COL_2, username);
        contentValues.put(C_COL_3, password);
        Long result = db.insert(CONTROLLER_TABLE_NAME, null, contentValues);
        if (result == -1) {
            return false;
        } else {
            return true;
        }

    }

    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.rawQuery("select * from " + DEVICES_TABLE_NAME, null);
        return result;
    }
}
