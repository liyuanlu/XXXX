package com.mlly.xxalarm.weather;

import android.app.AlarmManager;
import android.app.PendingIntent;

import java.io.Serializable;

/**
 * Created by liyuanlu on 2018/11/22.
 */
public class AlarmInfo implements Serializable {

    private String alarmTime;                //闹钟响铃时间

    private String alarmDelay;               //闹钟距离响铃时间

    private PendingIntent pendingIntent;

    private long timeInMillis;

    public AlarmInfo(String alarmTime, String alarmDelay,PendingIntent pendingIntent) {
        this.alarmTime = alarmTime;
        this.alarmDelay = alarmDelay;
        this.pendingIntent = pendingIntent;
    }

    public long getTimeInMillis() {
        return timeInMillis;
    }

    public void setTimeInMillis(long timeInMillis) {
        this.timeInMillis = timeInMillis;
    }

    public String getAlarmDelay() {
        return alarmDelay;
    }

    public String getAlarmTime() {
        return alarmTime;
    }

    public PendingIntent getPendingIntent() {
        return pendingIntent;
    }
}
