package com.mlly.xxalarm.model;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.daobao.asus.dbbaseframe.mvp.model.BaseModel;
import com.google.gson.Gson;
import com.mlly.xxalarm.weather.FutureWeatherInfo;
import com.mlly.xxalarm.weather.LifeSuggestion;
import com.mlly.xxalarm.weather.NowWeatherInfo;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.content.ContentValues.TAG;

/**
 * Created by liyuanlu on 2018/11/10.
 */
public class MainModel extends BaseModel {

    private Context context;

    public MainModel(Handler handler,Context context) {
        super(handler);
        this.context = context;
    }
}
