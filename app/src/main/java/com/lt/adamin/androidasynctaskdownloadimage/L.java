package com.lt.adamin.androidasynctaskdownloadimage;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

/**
 * 调试类
 * Created by Adam on 2014/11/30.
 */
public class L {
    public static void m(String message){
        Log.d("LiTao",message);
    }
    public static void s(Context context,String message){
        Toast.makeText(context,message,Toast.LENGTH_LONG).show();
    }
}
