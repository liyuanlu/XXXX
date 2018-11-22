package com.mlly.xxalarm.fragment;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.daobao.asus.dbbaseframe.mvp.view.BaseFragment;
import com.mlly.xxalarm.R;
import com.mlly.xxalarm.activity.MainActivity;
import com.mlly.xxalarm.presenter.AlarmFragmentPresenter;
import com.mlly.xxalarm.weather.AlarmInfo;

import java.util.ArrayList;
import java.util.Calendar;

import static android.content.Context.ALARM_SERVICE;

/**
 * Created by liyuanlu on 2018/11/22.
 */
public class AlarmFragment extends BaseFragment<AlarmFragmentPresenter> {

    private View view;                              //该Fragment对象

    private RecyclerView mRecyclerView;                //显示闹钟item的view

    private FloatingActionButton mFloatingButton;   //悬浮按钮

    private ArrayList<AlarmInfo> mAlarmList;         //闹钟信息List

    private MAdapter mAlarmAdapter;                 //适配器

    private AlarmManager mAlarm;                     //闹钟对象

    private int mHour,mMinute;                      //声明响铃时间

    private Calendar mCalendar;                       //日历

    private Activity activity;

    private Context context;

    private PendingIntent sender;                  //传递意图

    @Override
    protected AlarmFragmentPresenter binPresenter() {
        return new AlarmFragmentPresenter(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_alarm,container,false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    private void init() {
        activity=getActivity();
        context=getContext();
        mRecyclerView = (RecyclerView)view.findViewById(R.id.alarm_list);
        mFloatingButton = (FloatingActionButton)view.findViewById(R.id.add_alarm);
        mAlarmList=new ArrayList<>();
        mAlarm= (AlarmManager)activity.getSystemService(ALARM_SERVICE);
        //设置声明时间格式
        mCalendar = Calendar.getInstance();
        mHour=mCalendar.get(Calendar.HOUR_OF_DAY);
        mMinute=mCalendar.get(Calendar.MINUTE);
        //FloatingActionButton点击事件
        mFloatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /**
                 * FloatingActionButton点击事件
                 */
                setTime();
            }
        });
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(manager);
        mAlarmAdapter = new MAdapter(mAlarmList,getActivity());
        mRecyclerView.setAdapter(mAlarmAdapter);
    }


    /**
     * RecyclerView适配器
     */
    class MAdapter extends RecyclerView.Adapter<MAdapter.BaseHolder>{

        private ArrayList<AlarmInfo> alarmInfos;

        private Context context;

        private BaseHolder mBaseHolder;

        public MAdapter(ArrayList<AlarmInfo> alarmInfos, Context context) {
            this.alarmInfos = alarmInfos;
            this.context = context;
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }

        @NonNull
        @Override
        public BaseHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            mBaseHolder = new BaseHolder(LayoutInflater.from(context).inflate(R.layout.item_alarm,
                    viewGroup,false));
            return mBaseHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull BaseHolder baseHolder, int i) {
            baseHolder.setData(alarmInfos.get(i));
        }

        @Override
        public int getItemCount() {
            if (alarmInfos == null){
                return 0;
            }else {
                return alarmInfos.size();
            }
        }

        class BaseHolder extends RecyclerView.ViewHolder{

            private TextView alarmTime;

            private TextView alarmDelay;

            private Switch openAlarm;

            public BaseHolder(@NonNull View itemView) {
                super(itemView);
                alarmTime = (TextView) itemView.findViewById(R.id.alarm_time);
                alarmDelay = (TextView) itemView.findViewById(R.id.alarm_delay);
                openAlarm = (Switch)itemView.findViewById(R.id.open_alarm);

                /**
                 * 设置开关事件
                 */
                openAlarm.setChecked(true);
                openAlarm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        //设定pendingIntent接受自定义闹铃广播
                        sender = PendingIntent.getBroadcast(activity,0,new Intent("repeatAlarm"),0);
                        if(isChecked){
                            //分别获取闹铃响铃时间以及闹铃间隔时间（用冒号分割存储于数组中）
                            String[] triggerTime=alarmTime.getText().toString().split(":");
                            //通过Calendar分别获取对应的小时和分钟（需要将String转换为int）
                            Calendar calendar1 = Calendar.getInstance();
                            calendar1.set(Calendar.HOUR_OF_DAY,Integer.parseInt(triggerTime[0]));
                            calendar1.set(Calendar.MINUTE,Integer.parseInt(triggerTime[1]));
                            //得到点击触发的毫秒值（即闹钟提醒时间）
                            long triggerAtMillis= calendar1.getTimeInMillis();
                            //判断如果当前系统时间大于设置的闹铃时间，则在第二天开始启用该闹铃
                            if(System.currentTimeMillis()>triggerAtMillis){
                                triggerAtMillis=triggerAtMillis+24*60*60*1000;
                            }
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                AlarmManager.AlarmClockInfo alarmClockInfo = new AlarmManager.AlarmClockInfo(triggerAtMillis, null);
                                mAlarm.setAlarmClock(alarmClockInfo,sender);
                                //alarmManagerSet.setWindow(AlarmManager.RTC_WAKEUP,triggerAtMillis,0,pendingIntentSet);
                                Log.d("banben","24+");
                            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {//android 6.0以上7.0一下的手机
                                mAlarm.setExact(AlarmManager.RTC_WAKEUP,triggerAtMillis,sender);
                                Log.d("banben","23+");
                            } else {
                                mAlarm.set(AlarmManager.RTC_WAKEUP, triggerAtMillis,sender);
                                Log.d("banben","其他");
                            }
                            Toast.makeText(activity,"设置成功", Toast.LENGTH_LONG).show();
                        }
                        else {
                            mAlarm.cancel(sender);
                        }
                    }
                });
            }

            /**
             * 显示时间信息
             * @param alarmInfo
             */
            public void setData(AlarmInfo alarmInfo){
                alarmTime.setText(alarmInfo.alarmTime);
                alarmDelay.setText(alarmInfo.alarmDelay);
            }
        }
    }


    //使用timePickerDialog设置闹铃响铃时间
    public void setTime(){
        TimePickerDialog timePickerDialog =new TimePickerDialog(activity, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                mHour=hourOfDay;
                mMinute=minute;
                mAlarmList.add(new AlarmInfo(mHour+":"+mMinute,"5小时"));
                mAlarmAdapter.notifyDataSetChanged();

                //TODO 计算时间
            }
        },mHour,mMinute,true);
        timePickerDialog.show();
    }
}
