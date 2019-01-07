package com.mlly.xxalarm.presenter;

import android.os.Handler;
import android.os.Message;
import com.daobao.asus.dbbaseframe.mvp.presenter.BasePresenter;
import com.mlly.xxalarm.activity.WeatherActivity;
import com.mlly.xxalarm.model.MainModel;

/**
 * Created by liyuanlu on 2018/11/10.
 */
public class MainPresenter extends BasePresenter<MainModel,WeatherActivity> {

    public MainPresenter(WeatherActivity view) {
        super(view);
    }

    @Override
    public MainModel binModel(Handler handler) {
        return new MainModel(handler,mView.getApplicationContext());
    }

    /**
     * 处理M层传回的消息
     * @param msg
     */
    @Override
    public void modelResponse(Message msg) {

    }
}
