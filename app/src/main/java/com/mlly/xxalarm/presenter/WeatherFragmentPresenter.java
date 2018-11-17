package com.mlly.xxalarm.presenter;

import android.os.Handler;
import android.os.Message;

import com.daobao.asus.dbbaseframe.mvp.presenter.BasePresenter;
import com.mlly.xxalarm.fragment.WeatherFragment;
import com.mlly.xxalarm.model.MainModel;
import com.mlly.xxalarm.model.WeatherFragmentModel;

/**
 * Created by liyuanlu on 2018/11/17.
 */
public class WeatherFragmentPresenter extends BasePresenter<WeatherFragmentModel,WeatherFragment> {

    public WeatherFragmentPresenter(WeatherFragment view) {
        super(view);
    }

    @Override
    public WeatherFragmentModel binModel(Handler handler) {
        return new WeatherFragmentModel(handler,mView.getContext());
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

    @Override
    public void modelResponse(Message msg) {
        switch (msg.what){
            case WeatherFragmentModel.LOCATE_SUCCESS:
                requestWeatherData();
                mView.getNowAddress(mModel.getNowAddress());
                break;
            case WeatherFragmentModel.LOCATE_FAILED:
                mView.showMessage("定位失败:"+mModel.getLocType());
                mView.stopRefresh();
                break;
            case WeatherFragmentModel.GET_NOW_WEATHER_SUCCESS:
                mView.getNowWeatherData(mModel.getNowWeatherInfo());
                break;
            case WeatherFragmentModel.GET_FUTURE_WEATHER_SUCCESS:
                mView.getFutureWeatherData(mModel.getFutureWeatherInfo());
                break;
            case WeatherFragmentModel.GET_GRID_WEATHER_SUCCESS:
                mView.getGridWeather(mModel.getGridWeather());
                break;
            case WeatherFragmentModel.GET_LIFE_SUGGESTION_SUCCESS:
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
            case WeatherFragmentModel.NULL_POINTER:
                mView.showMessage("空指针异常");
                break;
            case WeatherFragmentModel.REFRESH_FINISHED:
                mView.stopRefresh();
            default:break;
        }
    }

}
