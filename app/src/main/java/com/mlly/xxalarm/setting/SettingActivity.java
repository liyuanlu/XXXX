package com.mlly.xxalarm.setting;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.mlly.xxalarm.R;

public class SettingActivity extends AppCompatActivity {

    private ImageView mColorBlue;
    private ImageView mColorPink;
    private ImageView mColorGreen;
    private ImageView mColorPurple;
    private ImageView mColorDefault;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSPTheme();
        setContentView(R.layout.activity_setting);
        initView();
    }

    private void getSPTheme() {
        SharedPreferences sp = getSharedPreferences("save_theme", Context.MODE_PRIVATE);
        int theme = sp.getInt("bule", 0);
        Log.d("theme", "" + theme);
        switch (theme) {
            case 1:
                setTheme(R.style.BlueTheme);
                break;
            default:
                break;
        }
        setResult(100);
    }

    private void saveTheme(int color) {
        SharedPreferences sp = getApplicationContext().getSharedPreferences("save_theme", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("color", color);
        editor.apply();
        recreate();
    }

    private void initView() {
        mColorBlue = (ImageView) findViewById(R.id.color_blue);
        mColorBlue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveTheme(1);
            }
        });
        mColorPink = (ImageView) findViewById(R.id.color_pink);
        mColorPink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveTheme(2);
            }
        });
        mColorGreen = (ImageView) findViewById(R.id.color_green);
        mColorGreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveTheme(3);
            }
        });
        mColorPurple = (ImageView) findViewById(R.id.color_purple);
        mColorPurple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveTheme(4);
            }
        });
        mColorDefault = (ImageView) findViewById(R.id.color_default);
        mColorDefault.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveTheme(0);
            }
        });
    }
}
