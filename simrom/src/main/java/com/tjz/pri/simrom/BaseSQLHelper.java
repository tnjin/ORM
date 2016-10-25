package com.tjz.pri.simrom;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Lenovo on 2016/10/25.
 */
public class BaseSQLHelper extends SQLiteOpenHelper {


    private static final String databaseName = "simrom";


    public BaseSQLHelper(Context context , int version) {
        super(context, databaseName, null, version);
    }





    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


}
