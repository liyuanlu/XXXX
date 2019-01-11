package com.mlly.xxalarm.setting;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.daobao.asus.dbbaseframe.mvpTest.TestContract;
import com.mlly.xxalarm.R;

public class SettingActivity extends AppCompatActivity{

    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSPTheme();
        setContentView(R.layout.activity_setting);
        button = (Button)findViewById(R.id.change_theme);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveTheme();
            }
        });
    }

    private void getSPTheme() {
        SharedPreferences sp = getSharedPreferences("save_theme",Context.MODE_PRIVATE);
        int theme = sp.getInt("bule",0);
        Log.d("theme", "" + theme);
        switch (theme){
            case 1:setTheme(R.style.BlueTheme);
            break;
            default:break;
        }
        setResult(100);
    }

    private void saveTheme() {
        SharedPreferences sp = getApplicationContext().getSharedPreferences("save_theme",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("bule",1);
        editor.apply();
        Log.d("theme", "saveTheme: success");
        recreate();
    }
}
