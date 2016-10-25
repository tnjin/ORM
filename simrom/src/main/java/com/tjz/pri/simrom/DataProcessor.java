package com.tjz.pri.simrom;

import android.content.Context;
import android.database.Cursor;


/**
 * Created by Lenovo on 2016/10/17.
 */
public class DataProcessor {
    private Context context;
    private SQLBuilder sqlBuilder;
    private BaseSQLHelper sqlHelper;
private static DataProcessor instance;

    public static void init(Context context){
        instance = new DataProcessor(context.getApplicationContext());
    }

    public static DataProcessor getInstance(){
        return instance;
    }


    private DataProcessor(Context context) {
        this.context = context;
        sqlHelper = new BaseSQLHelper(context,1);
        sqlBuilder = new SQLBuilder();
    }

    public void createDb(Class clazz) {
        String sql = sqlBuilder.buildCreationSQL(clazz);
        sqlHelper.getWritableDatabase().execSQL(sql);
    }

    public void saveToDb(BaseEntity entity){
        if(entity == null){
            return;
        }
        Cursor cursor = null;
        boolean isExist = false;
        try {
            cursor = queryFromDb(entity);
           isExist =  (cursor != null && cursor.moveToFirst());
        }finally {
            if(cursor != null){
                cursor.close();
            }
        }
            if (isExist) {//数据库中有这个数据，需要更新
                updateToDb(entity);
            } else {//需要插入
                insertToDb(entity);
            }
    }


    public void insertToDb(BaseEntity entity){
        String sql = sqlBuilder.buildInsertSQL(entity);
        sqlHelper.getWritableDatabase().execSQL(sql);
    }

    public Cursor queryFromDb(BaseEntity entity){
        String sql = sqlBuilder.buildQuerySQL(entity);
        return sqlHelper.getWritableDatabase().rawQuery(sql,null);
    }

    public void updateToDb(BaseEntity entity){
        String sql = sqlBuilder.buildUpdateSQL(entity);
        sqlHelper.getWritableDatabase().execSQL(sql);
    }




}
