package com.mlly.xxalarm.alarm;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import com.mlly.xxalarm.R;
import com.mlly.xxalarm.alarm.AlarmRingReceiver;

/**
 * Created by liyuanlu on 2018/11/25.
 */
public class AlarmRingService extends Service {

    private AlarmRingReceiver mReceiver;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        IntentFilter filter = new IntentFilter("com.mlly.alarm.alarmring");
        filter.addAction("com.mlly.alarm.connectalarm");
        filter.addAction("com.mlly.alarm.connectguard");
        mReceiver = new AlarmRingReceiver();
        registerReceiver(mReceiver,filter);
        start();
    }

    /**
     * 创建前台服务
     */
    private void start() {
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        String channelId = "alarm_service";
        String channelName = "alarm_ring";
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(channelId,channelName,
                    NotificationManager.IMPORTANCE_NONE);
            channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            manager.createNotificationChannel(channel);
            Notification.Builder builder = new Notification.Builder(this,channelId);
            Notification notification = builder.setOngoing(true)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setPriority(Notification.PRIORITY_MIN)
                    .setCategory(Notification.CATEGORY_SERVICE)
                    .build();
            startForeground(101,notification);
        }else {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this,channelId);
            Notification notification = builder
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setPriority(Notification.PRIORITY_MIN)
                    .setCategory(Notification.CATEGORY_SERVICE)
                    .build();
            startForeground(100,notification);
        }
        startGuardService();
    }


    private void startGuardService() {
        Intent intent = new Intent(this,GuardService.class);
        startService(intent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, START_FLAG_RETRY, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Intent intent = new Intent("com.mlly.alarm.connectguard");
        sendBroadcast(intent);
        unregisterReceiver(mReceiver);
    }




}
