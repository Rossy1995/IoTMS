package com.example.ross.iotms;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBAdapter
{
    private static final String CONTROLLER_TABLE_NAME = "Controller";
    public static final String C_COL_1 = "Controller_ID";
    public static final String C_COL_2 = "Username";
    public static final String C_COL_3 = "Password";

    SQLiteDatabase mDB;
    Context mContext;
    DBConnection mDBConnection;

    public DBAdapter(Context context)
    {
        this.mContext = context;
    }

    public DBAdapter open() throws SQLException
    {
        mDBConnection = new DBConnection(mContext);
        mDB = mDBConnection.getWritableDatabase();
        return this;
    }

    public long register(String user, String pass)
    {
        ContentValues values = new ContentValues();
        values.put(C_COL_2, user);
        values.put(C_COL_3, pass);
        return mDB.insert(CONTROLLER_TABLE_NAME, null, values);
    }

    public boolean Login(String username, String password) throws SQLException
    {
        Cursor mCursor = mDB.rawQuery("SELECT * FROM " + CONTROLLER_TABLE_NAME + " WHERE username=? AND password=?", new String[]{username, password});
        if (mCursor != null)
        {
            if(mCursor.getCount() > 0)
            {
                return true;
            }
        }
        return false;
    }

}
