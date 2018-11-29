package com.mlly.xxalarm.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.mlly.xxalarm.activity.AlarmRingActivity;
import com.mlly.xxalarm.service.AlarmRingService;
import com.mlly.xxalarm.service.GuardService;

/**
 * Created by liyuanlu on 2018/11/25.
 */
public class AlarmRingReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals("com.mlly.alarm.alarmring")){
            Intent intent1 = new Intent(context,AlarmRingActivity.class);
            intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent1);
        }else if (action.equals("com.mlly.alarm.connectalarm")){
            Intent intent1 = new Intent(context,AlarmRingService.class);
            intent1.setAction("com.mlly.alarm.connectalarm");
            context.startService(intent1);
        }else if (action.equals("com.mlly.alarm.connectguard")){
            Intent intent1 = new Intent(context,GuardService.class);
            intent1.setAction("com.mlly.alarm.connectguard");
            context.startService(intent1);
        }
    }
}
