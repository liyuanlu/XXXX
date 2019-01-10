package com.mlly.xxalarm.setting;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.daobao.asus.dbbaseframe.mvpTest.TestContract;
import com.mlly.xxalarm.R;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.change:
                setTheme(R.style.BlueTheme);
                recreate();
                break;
            default:break;
        }
    }
}
