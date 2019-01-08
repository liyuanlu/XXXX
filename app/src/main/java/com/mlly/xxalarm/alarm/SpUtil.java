package com.mlly.xxalarm.alarm;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mlly.xxalarm.alarm.AlarmInfo;
import java.util.ArrayList;
import java.util.List;

public class SpUtil {

    private static SharedPreferences sp;

    /**
     * 获取SharedPreferences对象
     * @param context 上下文对象
     * @return SharedPreferences对象
     */
    private static SharedPreferences getSp(Context context){
        if (sp == null){
            sp = context.getSharedPreferences("AlarmList",Context.MODE_PRIVATE);
        }
        return sp;
    }

    /**
     * 存放List<AlarmInfo>对象
     * @param context 上下文对象
     * @param key 键
     * @param list 需要存放的List<AlarmInfo>对象
     */
    public static void putList(Context context, String key, List<?> list){
        Gson gson = new Gson();
        String string = gson.toJson(list);
        SharedPreferences sharedPreferences = getSp(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (string != null){
            editor.putString(key,string);
        }
        editor.apply();
    }

    /**
     * 获取List<AlarmInfo>对象
     * @param context 上下文对象
     * @param key 键
     * @return 存放的List<AlarmInfo>对象
     */
    public static List<AlarmInfo> getList(Context context,String key){
        Gson gson = new Gson();
        SharedPreferences sharedPreferences = getSp(context);
        String string = null;
        if (sharedPreferences != null){
            string = sharedPreferences.getString(key,"");
        }
        if (string.equals("")){
            return null;
        }
        List<AlarmInfo> alarmInfos = new ArrayList<>();
        alarmInfos = gson.fromJson(string, new TypeToken<List<AlarmInfo>>() {}.getType());
        return alarmInfos;
    }

    /**
     * 清除SharedPreferences中的内容
     * @param context 上下文对象
     */
    public static void clear(Context context){
        SharedPreferences sharedPreferences = getSp(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

}