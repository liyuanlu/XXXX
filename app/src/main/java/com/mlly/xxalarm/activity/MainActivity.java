package com.mlly.xxalarm.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.daobao.asus.dbbaseframe.mvp.presenter.BasePresenter;
import com.daobao.asus.dbbaseframe.mvp.view.BaseActivity;
import com.mlly.xxalarm.R;


public class MainActivity extends BaseActivity {

    @Override
    public BasePresenter binPresenter() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
