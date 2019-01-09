package com.mlly.xxalarm.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.mlly.xxalarm.alarm.AlarmActivity;
import com.mlly.xxalarm.R;
import com.mlly.xxalarm.note.NoteActivity;
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
        setContentView(R.layout.activity_main);
        addInfo();
        initView();
    }

    private void addInfo() {
        mInfos.add(new Info(R.drawable.weather,"天气"));
        mInfos.add(new Info(R.drawable.note,"便签"));
        mInfos.add(new Info(R.drawable.alarm,"闹钟"));
        mInfos.add(new Info(R.drawable.setting,"设置"));
        mInfos.add(new Info(R.drawable.weather,"天气"));
        mInfos.add(new Info(R.drawable.note,"便签"));
        mInfos.add(new Info(R.drawable.alarm,"闹钟"));
        mInfos.add(new Info(R.drawable.setting,"设置"));
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