package com.mlly.xxalarm.weather;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.daobao.asus.dbbaseframe.mvp.presenter.BasePresenter;

import static android.content.ContentValues.TAG;

/**
 * Created by liyuanlu on 2018/11/10.
 */
public class WeatherPresenter extends BasePresenter<WeatherModel,WeatherActivity> {

    public WeatherPresenter(WeatherActivity view) {
        super(view);
    }

    @Override
    public WeatherModel binModel(Handler handler) {
        return new WeatherModel(handler,mView.getApplicationContext());
    }

    /**
     * 处理M层传回的消息
     * @param msg
     */
    @Override
    public void modelResponse(Message msg) {
        switch (msg.what){
            case WeatherModel.LOCATE_SUCCESS:
                requestWeatherData();
                setCity();
                mView.getNowAddress(mModel.getNowAddress());
                break;
            case WeatherModel.LOCATE_FAILED:
                mView.showMessage("定位失败:"+mModel.getLocType());
                mView.stopRefresh();
                break;
            case WeatherModel.GET_NOW_WEATHER_SUCCESS:
                mView.getNowWeatherData(mModel.getNowWeatherInfo());
                break;
            case WeatherModel.GET_FUTURE_WEATHER_SUCCESS:
                mView.getFutureWeatherData(mModel.getFutureWeatherInfo());
                break;
            case WeatherModel.GET_GRID_WEATHER_SUCCESS:
                mView.getGridWeather(mModel.getGridWeather());
                break;
            case WeatherModel.GET_LIFE_SUGGESTION_SUCCESS:
                mView.getLifeSuggestion(mModel.getLifeSuggestion());
                if (mModel.getLocType() == 161){
                    if (mModel.getFutureWeatherInfo() == null && mModel.getNowWeatherInfo() == null
                            && mModel.getLifeSuggestion() == null){
                        mView.refreshFailed();
                    }else {
                        mView.refreshSuccess();
                    }
                }
                break;
            case WeatherModel.NULL_POINTER:
                mView.showMessage("空指针异常");
                break;
            case WeatherModel.REFRESH_FINISHED:
                mView.stopRefresh();break;
            case 123456:
                mView.setCityName(msg.getData().getString("cityName"));
                break;
            default:break;
        }
    }
    /**
     * 开始定位
     */
    public void startLocate(){
        mModel.startLocate();
    }

    /**
     * 停止定位
     */
    public void stopLoacte(){
        mModel.stopLocate();
    }

    /**
     * 请求天气
     */
    private void requestWeatherData(){
        mModel.requestWeatherData();
    }

    private void setCity(){
        mView.setCityName(mModel.getCityName());
    }

}
