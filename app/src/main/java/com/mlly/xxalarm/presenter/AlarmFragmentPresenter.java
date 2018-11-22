package com.mlly.xxalarm.presenter;

import android.os.Handler;
import android.os.Message;

import com.daobao.asus.dbbaseframe.mvp.presenter.BasePresenter;
import com.mlly.xxalarm.fragment.AlarmFragment;
import com.mlly.xxalarm.model.AlarmFragmentModel;

/**
 * Created by liyuanlu on 2018/11/22.
 */
public class AlarmFragmentPresenter extends BasePresenter<AlarmFragmentModel,AlarmFragment> {

    public AlarmFragmentPresenter(AlarmFragment view) {
        super(view);
    }

    @Override
    public AlarmFragmentModel binModel(Handler handler) {
        return new AlarmFragmentModel(handler);
    }

    @Override
    public void modelResponse(Message msg) {

    }
}
