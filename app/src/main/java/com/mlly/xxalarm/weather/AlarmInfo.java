package com.mlly.xxalarm.weather;

/**
 * Created by liyuanlu on 2018/11/22.
 */
public class AlarmInfo {

    public String alarmTime;                //闹钟响铃时间

    public String alarmDelay;               //闹钟距离响铃时间

    public AlarmInfo(String alarmTime, String alarmDelay) {
        this.alarmTime = alarmTime;
        this.alarmDelay = alarmDelay;
    }
}