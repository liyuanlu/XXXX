package com.mlly.xxalarm.model;

import android.content.Context;
import android.os.Handler;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.daobao.asus.dbbaseframe.mvp.model.BaseModel;
import com.google.gson.Gson;
import com.mlly.xxalarm.weather.FutureWeatherInfo;
import com.mlly.xxalarm.weather.GridWeatherInfo;
import com.mlly.xxalarm.weather.LifeSuggestion;
import com.mlly.xxalarm.weather.NowWeatherInfo;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by liyuanlu on 2018/11/17.
 */
public class WeatherFragmentModel extends BaseModel {

    public static final int LOCATE_SUCCESS = 1;

    public static final int LOCATE_FAILED = 2;

    public static final int GET_NOW_WEATHER_SUCCESS = 3;

    public static final int GET_FUTURE_WEATHER_SUCCESS = 4;

    public static final int NULL_POINTER = 5;

    public static final int GET_LIFE_SUGGESTION_SUCCESS = 6;

    public static final int REFRESH_FINISHED = 7;

    public static final int GET_GRID_WEATHER_SUCCESS = 8;

    //当前天气请求地址
    private static final String NOW_WEATHER_ADDRESS_HEAD = "https://api.seniverse.com/v3/weather/now.json?key=des12nko8oihfzte&location=";

    private static final String NOW_WEATHER_ADDRESS_TAIL = "&language=zh-Hans&unit=c";

    //未来天气请求地址
    private static final String FUTURE_WEATHER_ADDRESS_HEAD = "https://api.seniverse.com/v3/weather/daily.json?key=des12nko8oihfzte&location=";

    private static final String FUTURE_WEATHER_ADDRESS_TAIL = "&language=zh-Hans&unit=c&start=0&days=5";

    //生活建议请求地址
    private static final String LIFE_SUGGESTION_ADDRESS_HEAD = "https://api.seniverse.com/v3/life/suggestion.json?key=des12nko8oihfzte&location=";

    private static final String LIFE_SUGGESTION_ADDRESS_TAIL = "&language=zh-Hans";

    //网格天气实况
    private static final String GRID_NOW_WEATHER_ADDRESS = "https://api.seniverse.com/v3/pro/weather/grid/now.json?key=des12nko8oihfzte&location=";

    private Context context;

    private LocationClient mLocationClient;                 //百度定位对象

    private String mCityName;                               //城市完整名字

    private String mNowAddress;                             //当前位置

    private OkHttpClient mOKHttpClient;                     //网络请求对象

    private Gson mGson;                                     //数据解析对象

    private NowWeatherInfo mNowWeatherInfo;                 //当前天气对象

    private FutureWeatherInfo mFutureWeatherInfo;           //未来天气对象

    private LifeSuggestion mLifeSuggestion;                 //生活建议对象

    private GridWeatherInfo mGridWeather;                   //公里级网格天气对象

    private Double longitude = -1d;                               //当前位置经度值

    private Double latitude = -1d;                                //当前位置维度值

    private int mLocType = 0;                               //定位返回值

    public WeatherFragmentModel(Handler handler,Context context) {
        super(handler);
        this.context = context;
    }

    public GridWeatherInfo getGridWeather(){
        if (mGridWeather != null && mGridWeather.getResults() != null){
            return  mGridWeather;
        }else {
            sendEmptyMessage(NULL_POINTER);
            return null;
        }
    }

    /**
     * 获取生活建议对象
     * @return
     */
    public LifeSuggestion getLifeSuggestion(){
        if (mLifeSuggestion != null && mLifeSuggestion.getResults() != null){
            return mLifeSuggestion;
        }else {
            sendEmptyMessage(NULL_POINTER);
            return null;
        }
    }

    /**
     * 获取未来天气对象
     * @return
     */
    public FutureWeatherInfo getFutureWeatherInfo(){
        if (mFutureWeatherInfo != null && mFutureWeatherInfo.getResults() != null){
            return mFutureWeatherInfo;
        }else {
            sendEmptyMessage(NULL_POINTER);
            return null;
        }
    }

    /**
     * 获取当前天气对象
     * @return
     */
    public NowWeatherInfo getNowWeatherInfo(){
        if (mNowWeatherInfo != null && mNowWeatherInfo.getResults() != null){
            return mNowWeatherInfo;
        }else {
            sendEmptyMessage(NULL_POINTER);
            return null;
        }
    }

    /**
     * 请求天气数据
     * 解析天气数据
     */
    public void requestWeatherData(){
        mOKHttpClient = new OkHttpClient();
        mGson = new Gson();
        if (mCityName != null){
            Request nowWeatherRequest = new Request.Builder().
                    url(NOW_WEATHER_ADDRESS_HEAD + mCityName + NOW_WEATHER_ADDRESS_TAIL)
                    .build();
            Call nowWeatherCall = mOKHttpClient.newCall(nowWeatherRequest);
            //单开线程进行网络请求
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Response response = nowWeatherCall.execute();
                        String result = response.body().string();
                        if (result != null){
                            mNowWeatherInfo = mGson.fromJson(result,NowWeatherInfo.class);
                            if (mNowWeatherInfo != null){
                                sendEmptyMessage(GET_NOW_WEATHER_SUCCESS);
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
            Request futureWeatherRequest = new Request.Builder()
                    .url(FUTURE_WEATHER_ADDRESS_HEAD + mCityName + FUTURE_WEATHER_ADDRESS_TAIL)
                    .build();
            Call futureWeatherCall = mOKHttpClient.newCall(futureWeatherRequest);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Response response = futureWeatherCall.execute();
                        String result = response.body().string();
                        if (result != null){
                            mFutureWeatherInfo = mGson.fromJson(result,FutureWeatherInfo.class);
                            sendEmptyMessage(GET_FUTURE_WEATHER_SUCCESS);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
            Request lifeSuggestionRequest = new Request.Builder()
                    .url(LIFE_SUGGESTION_ADDRESS_HEAD + mCityName + LIFE_SUGGESTION_ADDRESS_TAIL)
                    .build();
            Call lifeSuggestionCall = mOKHttpClient.newCall(lifeSuggestionRequest);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Response response = lifeSuggestionCall.execute();
                        String result = response.body().string();
                        if (result != null){
                            mLifeSuggestion = mGson.fromJson(result,LifeSuggestion.class);
                            sendEmptyMessage(GET_LIFE_SUGGESTION_SUCCESS);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
        if (longitude != -1d && latitude != -1d){
            Request gridWeatherRequest = new Request.Builder()
                    .url(GRID_NOW_WEATHER_ADDRESS + latitude + ":" + longitude)
                    .build();
            Call gridWeatherCall = mOKHttpClient.newCall(gridWeatherRequest);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Response response = gridWeatherCall.execute();
                        if (response != null){
                            String result = response.body().string();
                            mGridWeather = mGson.fromJson(result,GridWeatherInfo.class);
                            sendEmptyMessage(GET_GRID_WEATHER_SUCCESS);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    /**
     * 开始定位
     */
    public void startLocate(){
        mLocationClient = new LocationClient(context);
        BaiduLocationListener listener = new BaiduLocationListener();
        mLocationClient.registerLocationListener(listener);
        LocationClientOption option = new LocationClientOption();
        option.setIsNeedAddress(true);
        mLocationClient.setLocOption(option);
        mLocationClient.start();
    }

    /**
     * 停止定位
     */
    public void stopLocate(){
        mLocationClient.stop();
    }

    /**
     * 获取定位返回值
     */
    public int getLocType(){
        return mLocType;
    }

    public String getNowAddress(){
        if (mNowAddress != null){
            return mNowAddress;
        }else {
            return "未知";
        }
    }

    /**
     * 获取当前完整城市名
     * @return
     */
    public String getCityWholeName(){
        return mCityName;
    }

    /**
     * 百度定位监听器类
     * 定位后回调
     */
    class BaiduLocationListener extends BDAbstractLocationListener {

        String cityName;                                //当前城市完整名字

        String province;                                //当前定位省份

        String city;                                    //当前定位城市

        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            mLocType = bdLocation.getLocType();
            if (mLocType == 161){
                province = bdLocation.getProvince();
                city = bdLocation.getCity();
                mNowAddress = bdLocation.getDistrict() + bdLocation.getStreet();
                longitude = bdLocation.getLongitude();
                latitude = bdLocation.getLatitude();
                getCityWholeName();
                mCityName = cityName;
                //向P层发送消息
                sendEmptyMessage(LOCATE_SUCCESS);
                sendEmptyMessage(REFRESH_FINISHED);
            }else{
                sendEmptyMessage(LOCATE_FAILED);
            }
        }

        /**
         * 得到完整城市名
         * 用于查询该城市天气
         */
        public void getCityWholeName(){
            if (province != null){
                if (province.startsWith("内蒙古")){
                    province = "内蒙古";
                }else if (province.endsWith("区")){
                    province = province.substring(0,2);
                }else if (province.endsWith("省")){
                    province = province.substring(0,province.length() - 1);
                }
            }
            if (city != null){
                if (city.endsWith("自治区")){
                    city = city.substring(0,2);
                }else if (city.endsWith("区")){
                    city = city.substring(0,city.length() - 1);
                }else if (city.endsWith("市")){
                    city = city.substring(0,city.length() - 1);
                }
            }
            cityName = province + city;
        }
    }
}
