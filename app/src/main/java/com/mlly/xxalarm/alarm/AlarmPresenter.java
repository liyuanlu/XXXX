package com.mlly.xxalarm.alarm;

import android.os.Handler;
import android.os.Message;

import com.daobao.asus.dbbaseframe.mvp.presenter.BasePresenter;
import com.mlly.xxalarm.alarm.AlarmActivity;
import com.mlly.xxalarm.alarm.AlarmModel;

/**
 * Created by liyuanlu on 2018/11/22.
 */
public class AlarmPresenter extends BasePresenter<AlarmModel,AlarmActivity> {

    public AlarmPresenter(AlarmActivity view) {
        super(view);
    }

    @Override
    public AlarmModel binModel(Handler handler) {
        return new AlarmModel(handler);
    }

    @Override
    public void modelResponse(Message msg) {

    }
}