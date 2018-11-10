package com.mlly.xxalarm.presenter;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.daobao.asus.dbbaseframe.mvp.presenter.BasePresenter;
import com.mlly.xxalarm.activity.MainActivity;
import com.mlly.xxalarm.model.MainModel;

import static android.content.ContentValues.TAG;

/**
 * Created by liyuanlu on 2018/11/10.
 */
public class MainPresenter extends BasePresenter<MainModel,MainActivity> {

    public MainPresenter(MainActivity view) {
        super(view);
    }

    @Override
    public MainModel binModel(Handler handler) {
        return new MainModel(handler,mView.getApplicationContext());
    }

    public void startLocate(){
        mModel.startLocate();
    }

    private void requestWeatherData(){
        mModel.requestWeatherData();
    }

    /**
     * 处理M层传回的消息
     * @param msg
     */
    @Override
    public void modelResponse(Message msg) {
        switch (msg.what){
            case MainModel.LOCATE_SUCCESS:
                requestWeatherData();
                break;
            case MainModel.LOCATE_FAILED:
                mView.showMessage("定位失败");
                break;
            case MainModel.GET_NOW_WEATHER_SUCCESS:
                mView.getNowWeatherData(mModel.getNowWeatherInfo());
                    break;
            case MainModel.GET_FUTURE_WEATHER_SUCCESS:
                mView.getFutureWeatherData(mModel.getFutureWeatherInfo());
                break;
            case MainModel.NULL_POINTER:
                mView.showMessage("空指针异常");
                Log.d(TAG, "modelResponse: 空指针异常");
                    break;
                default:break;
        }
    }
}
