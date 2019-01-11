package com.mlly.xxalarm.main;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.mlly.xxalarm.alarm.AlarmActivity;
import com.mlly.xxalarm.R;
import com.mlly.xxalarm.note.NoteActivity;
import com.mlly.xxalarm.setting.SettingActivity;
import com.mlly.xxalarm.weather.WeatherActivity;

import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity{

    private static final int SPAN_COUNT = 2;

    private ArrayList<Info> mInfos = new ArrayList<>();

    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSPTheme();
        setContentView(R.layout.activity_main);
        addInfo();
        initView();
    }

    private void getSPTheme() {
        SharedPreferences sp = getSharedPreferences("save_theme",Context.MODE_PRIVATE);
        int theme = sp.getInt("color",0);
        Log.d("theme", "" + theme);
        switch (theme){
            case 0:setTheme(R.style.AppTheme);
                break;
            case 1:setTheme(R.style.BlueTheme);
                break;
            case 2:setTheme(R.style.PinkTheme);
                break;
            case 3:setTheme(R.style.GreenTheme);
                break;
            case 4:setTheme(R.style.PurpleTheme);
                break;
            default:setTheme(R.style.AppTheme);
                break;
        }
    }

    private void addInfo() {
        mInfos.add(new Info(R.drawable.weather,"天气"));
        mInfos.add(new Info(R.drawable.note,"便签"));
        mInfos.add(new Info(R.drawable.alarm,"闹钟"));
        mInfos.add(new Info(R.drawable.setting,"设置"));
    }

    private void initView() {
        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.main_recyclerview);
        MainAdapter mMainAdapter = new MainAdapter(mInfos, this);
        mMainAdapter.setListener(new MainAdapter.RecyclerViewOnItemClickListener() {
            @Override
            public void OnItemClickListener(View view, int position) {
                switch (position){
                    case 0:skipToWeatherActivity();break;
                    case 1:skipToNoteActivity();break;
                    case 2:skipToAlarmActivity();break;
                    case 3:skipToSettingActivity();break;
                    default:break;
                }
            }
        });
        mRecyclerView.setLayoutManager(
                new StaggeredGridLayoutManager(SPAN_COUNT,StaggeredGridLayoutManager.VERTICAL));
        mRecyclerView.setAdapter(mMainAdapter);
        mToolbar = (Toolbar)findViewById(R.id.main_toolbar);
        setSupportActionBar(mToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
    }

    private void skipToAlarmActivity() {
        startActivity(new Intent(MainActivity.this,AlarmActivity.class));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 666){
            recreate();
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

    /**
     * 跳转到设置活动
     */
    private void skipToSettingActivity() {
        startActivityForResult(new Intent(MainActivity.this,SettingActivity.class),666);
    }
}
