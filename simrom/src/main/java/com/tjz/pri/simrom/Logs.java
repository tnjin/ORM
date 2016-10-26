package com.tjz.pri.simrom;

import android.util.Log;

/**
 * Created by Lenovo on 2016/10/26.
 */
public class Logs {

    private static boolean log_enabled = true;
    private static boolean log_to_file_enabled = false;
    private static final String TAG = "SIM_LOG";
    public static void  i(String msg){
        if(log_enabled){
            Log.e(TAG,msg);
        }
        if(log_to_file_enabled){
            write2File(msg);
        }
    }

    private static void write2File(String msg) {
    }


}
