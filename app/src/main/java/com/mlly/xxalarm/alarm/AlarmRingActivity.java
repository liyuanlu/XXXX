package com.mlly.xxalarm.alarm;

import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.mlly.xxalarm.R;
import com.mlly.xxalarm.alarm.SpUtil;
import com.mlly.xxalarm.alarm.AlarmInfo;

import java.util.List;

public class AlarmRingActivity extends AppCompatActivity {

    private List<AlarmInfo> mAlarmList;

    private Button mStopAlarm;

    private MediaPlayer mMediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_ring);
        init();
    }

    private void init() {
        mStopAlarm = (Button)findViewById(R.id.stop_alarm);
        mMediaPlayer = MediaPlayer.create(getApplicationContext(),R.raw.ring);
        mMediaPlayer.start();
        mStopAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mMediaPlayer != null){
                    mMediaPlayer.stop();
                    mMediaPlayer.release();
                    mMediaPlayer = null;
                    removeAlarm();
                    finish();
                }
            }
        });
    }

    private void removeAlarm(){
        int position = getIntent().getIntExtra("position",-1);
        if (position != -1){
            mAlarmList = SpUtil.getList(getApplicationContext(),"alarmlist");
            if (mAlarmList != null){
                mAlarmList.remove(position);
                SpUtil.clear(getApplicationContext());
                SpUtil.putList(getApplicationContext(),"alarmlist",mAlarmList);
            }
        }
    }
}