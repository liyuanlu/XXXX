package com.mlly.xxalarm.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.mlly.xxalarm.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        Button mSkipWeather = (Button) findViewById(R.id.skip_weather);
        Button mSkipNote = (Button) findViewById(R.id.skip_note);
        mSkipWeather.setOnClickListener(this);
        mSkipNote.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.skip_weather:
                skipToWeatherActivity();
                break;
            case R.id.skip_note:
                skipToNoteActivity();
                break;
        }
    }

    /**
     * 跳转到便签活动
     */
    private void skipToNoteActivity() {
        startActivity(new Intent(MainActivity.this,NoteActivity.class));
    }

    /**
     * 跳转到天气活动
     */
    private void skipToWeatherActivity() {
        startActivity(new Intent(MainActivity.this,WeatherActivity.class));
    }
}
