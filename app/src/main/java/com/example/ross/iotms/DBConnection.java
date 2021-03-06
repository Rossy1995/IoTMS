package com.example.ross.iotms;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.Date;

import static android.content.ContentValues.TAG;
import static android.os.FileObserver.CREATE;

public class DBConnection extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "devices.db";
    public static final String CONTROLLER_TABLE_NAME = "Controller";
    public static final String DEVICES_TABLE_NAME = "Devices";
    public static final String READINGS_TABLE_NAME = "Readings";

    public static final String C_COL_1 = "Controller_ID";
    public static final String C_COL_2 = "Username";
    public static final String C_COL_3 = "Password";

    public static final String D_COL_1 = "_id";
    public static final String D_COL_2 = "DEVICE_NAME";
    public static final String D_COL_3 = "DEVICE_TYPE";
    public static final String D_COL_4 = "DEVICE_DESCRIPTION";


    public DBConnection(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
            String DATABASE_CREATE = "CREATE TABLE Controller (Controller_ID integer primary key autoincrement,Username text not null,Password text not null);";
            db.execSQL(DATABASE_CREATE);
            String DATABASE_CREATE_DEVICES = "CREATE TABLE " + DEVICES_TABLE_NAME + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, DEVICE_NAME TEXT,DEVICE_TYPE TEXT, DEVICE_DESCRIPTION TEXT);";
            db.execSQL(DATABASE_CREATE_DEVICES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + CONTROLLER_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DEVICES_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + READINGS_TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData (String name, String type, String description){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(D_COL_2, name);
        contentValues.put(D_COL_3, type);
        contentValues.put(D_COL_4, description);
        Long result  = db.insert(DEVICES_TABLE_NAME, null, contentValues); // inserts parsed values into TABLE_NAME
        if (result == -1){
            return false;
        }
        else {
            return  true; // if result is not minus -1 then the insert has worked
        }
    }

    /**
     * collects all data from db and sorts it into order of best score to worst
     * @return result returns the query after it has run
     */

    public Cursor getAllData (){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor result = db.rawQuery("select rowid _id, DEVICE_NAME, DEVICE_TYPE, DEVICE_DESCRIPTION from " + DEVICES_TABLE_NAME, null); // selects all from db sorting by score ASC
        return result;
    }
}
