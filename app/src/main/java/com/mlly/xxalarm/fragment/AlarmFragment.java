package com.mlly.xxalarm.fragment;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.PopupMenu;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import com.daobao.asus.dbbaseframe.mvp.view.BaseFragment;
import com.mlly.xxalarm.R;
import com.mlly.xxalarm.activity.MainActivity;
import com.mlly.xxalarm.presenter.AlarmFragmentPresenter;
import com.mlly.xxalarm.receiver.AlarmRingReceiver;
import com.mlly.xxalarm.service.AlarmRingService;
import com.mlly.xxalarm.weather.AlarmInfo;

import java.util.ArrayList;
import java.util.Calendar;

import static android.content.ContentValues.TAG;

/**
 * Created by liyuanlu on 2018/11/22.
 */
public class AlarmFragment extends BaseFragment<AlarmFragmentPresenter> {

    private View view;                              //该Fragment对象

    private RecyclerView mRecyclerView;                //显示闹钟item的view

    private FloatingActionButton mFloatingButton;   //悬浮按钮

    private ArrayList<AlarmInfo> mAlarmList;         //闹钟信息List

    private MAdapter mAlarmAdapter;                 //适配器

    private AlarmManager mAlarmManager;             //闹钟管理器对象

    private PopupMenu mPopupMenu;

    private int position;

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
        mRecyclerView = (RecyclerView)view.findViewById(R.id.alarm_list);
        mFloatingButton = (FloatingActionButton)view.findViewById(R.id.add_alarm);
        mAlarmList=new ArrayList<>();
        mPopupMenu = new PopupMenu(getActivity(),view.findViewById(R.id.alarm_list));
        mAlarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        mPopupMenu.getMenuInflater().inflate(R.menu.menu,mPopupMenu.getMenu());
        mPopupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                String title = menuItem.getTitle().toString();
                if (title.equals("删除")){
                    mAlarmList.remove(position);
                    mAlarmAdapter.notifyDataSetChanged();
                    return true;
                }
                return false;
            }
        });
        //FloatingActionButton点击事件
        mFloatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                openAlarm.setChecked(true);
                openAlarm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if (b){

                        }else {

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


    /**
     * 设置闹钟
     */
    public void setTime(){
        Calendar calendar = Calendar.getInstance();
        TimePickerDialog timePickerDialog =new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                mAlarmList.add(new AlarmInfo(hourOfDay+":"+minute,"5小时"));
                mAlarmAdapter.notifyDataSetChanged();
                Calendar c = Calendar.getInstance();
                c.set(Calendar.HOUR_OF_DAY,hourOfDay);
                c.set(Calendar.MINUTE,minute);
                Intent intent = new Intent(getActivity(),AlarmRingService.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                if (Build.VERSION.SDK_INT >= 26){
                    getActivity().startForegroundService(intent);
                }else {
                    getActivity().startService(intent);
                }
                Intent intent1 = new Intent();
                PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(),100,
                        new Intent("com.mlly.alarm.alarmring"),0);
                mAlarmManager.set(AlarmManager.RTC_WAKEUP,c.getTimeInMillis(),pendingIntent);
            }
        },calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),true);
        timePickerDialog.show();
    }
}
