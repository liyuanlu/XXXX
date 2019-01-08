package com.mlly.xxalarm.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.mlly.xxalarm.Note.NoteActivity;
import com.mlly.xxalarm.R;
import com.mlly.xxalarm.weather.WeatherActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity{

    private static final int SPAN_COUNT = 2;

    private ArrayList<Info> mInfos = new ArrayList<>();

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
                    default:break;
                }
            }
        });
        mRecyclerView.setLayoutManager(
                new StaggeredGridLayoutManager(SPAN_COUNT,StaggeredGridLayoutManager.VERTICAL));
        mRecyclerView.setAdapter(mMainAdapter);
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
