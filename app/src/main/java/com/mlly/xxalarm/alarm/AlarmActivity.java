package com.mlly.xxalarm.alarm;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.PopupMenu;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.daobao.asus.dbbaseframe.mvp.view.BaseActivity;
import com.mlly.xxalarm.R;
import com.mlly.xxalarm.alarm.SpUtil;
import com.mlly.xxalarm.alarm.AlarmPresenter;
import com.mlly.xxalarm.alarm.AlarmInfo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AlarmActivity extends BaseActivity<AlarmPresenter> {

    private RecyclerView mRecyclerView;                //显示闹钟item的view

    private FloatingActionButton mFloatingButton;   //悬浮按钮

    private List<AlarmInfo> mAlarmList;         //闹钟信息List

    private MAdapter mAlarmAdapter;                 //适配器

    private AlarmManager mAlarmManager;             //闹钟管理器对象


    @Override
    public AlarmPresenter binPresenter() {
        return new AlarmPresenter(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_alarm);
        setFlag();
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setFlag() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
    }


    private void init() {
        mRecyclerView = (RecyclerView)findViewById(R.id.alarm_list);
        mFloatingButton = (FloatingActionButton)findViewById(R.id.add_alarm);
        //从SharedPreferences中获取存放的对象
        mAlarmList = SpUtil.getList(getApplicationContext(),"alarmlist");
        if (mAlarmList == null){
            mAlarmList = new ArrayList<>();
        }
        mAlarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        mFloatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setTime();
            }
        });
        LinearLayoutManager manager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(manager);
        mAlarmAdapter = new MAdapter(mAlarmList,this);
        mRecyclerView.setAdapter(mAlarmAdapter);
        //设置RecycleView中Item的点击事件
        mAlarmAdapter.setRecyclerViewOnItemClickListener(new RecyclerViewOnItemClickListener() {
            @Override
            public void onItemClickListener(View view, int position) {
                Toast.makeText(AlarmActivity.this,"你点击了Item",Toast.LENGTH_SHORT).show();
            }
        });
        //设置RecycleView中Item的长按事件
        mAlarmAdapter.setRecyclerViewOnItemLongClickListener(new RecyclerViewOnItemLongClickListener() {
            @Override
            public boolean onItemLongClickListener(View view, int position) {
                PopupMenu popupMenu = new PopupMenu(AlarmActivity.this,view);
                popupMenu.getMenuInflater().inflate(R.menu.menu,popupMenu.getMenu());
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()){
                            case R.id.delete_item:
                                mAlarmManager.cancel(mAlarmList.get(position).getPendingIntent());
                                mAlarmList.remove(position);
                                mAlarmAdapter.notifyDataSetChanged();
                                break;
                            default:break;
                        }
                        return true;
                    }
                });
                return true;
            }
        });
    }


    /**
     * RecyclerView适配器
     */
    class MAdapter extends RecyclerView.Adapter<AlarmActivity.MAdapter.BaseHolder> implements View.OnClickListener,
            View.OnLongClickListener{

        private List<AlarmInfo> alarmInfos;

        private Context context;

        private MAdapter.BaseHolder mBaseHolder;

        private RecyclerViewOnItemClickListener onItemClickListener;

        private RecyclerViewOnItemLongClickListener onItemLongClickListener;

        public MAdapter(List<AlarmInfo> alarmInfos, Context context) {
            this.alarmInfos = alarmInfos;
            this.context = context;
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }

        @NonNull
        @Override
        public MAdapter.BaseHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View root = LayoutInflater.from(context).inflate(R.layout.item_alarm,viewGroup,false);
            root.setOnClickListener(this);
            root.setOnLongClickListener(this);
            mBaseHolder = new MAdapter.BaseHolder(root);
            return mBaseHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull MAdapter.BaseHolder baseHolder, int i) {
            baseHolder.setData(alarmInfos.get(i));
            baseHolder.root.setTag(i);
        }

        @Override
        public int getItemCount() {
            if (alarmInfos == null){
                return 0;
            }else {
                return alarmInfos.size();
            }
        }

        @Override
        public void onClick(View view) {
            if (onItemClickListener != null){
                onItemClickListener.onItemClickListener(view,(Integer)view.getTag());
            }
        }

        @Override
        public boolean onLongClick(View view) {
            return onItemLongClickListener != null &&
                    onItemLongClickListener.onItemLongClickListener(view,(Integer)view.getTag());
        }

        private void setRecyclerViewOnItemClickListener(RecyclerViewOnItemClickListener recyclerViewOnItemClickListener){
            this.onItemClickListener = recyclerViewOnItemClickListener;
        }

        private void setRecyclerViewOnItemLongClickListener(RecyclerViewOnItemLongClickListener recyclerViewOnItemLongClickListener){
            this.onItemLongClickListener = recyclerViewOnItemLongClickListener;
        }

        class BaseHolder extends RecyclerView.ViewHolder{

            private TextView alarmTime;

            private TextView alarmDelay;

            private Switch openAlarm;

            private View root;

            public BaseHolder(@NonNull View itemView) {
                super(itemView);
                alarmTime = (TextView) itemView.findViewById(R.id.alarm_time);
                alarmDelay = (TextView) itemView.findViewById(R.id.alarm_delay);
                openAlarm = (Switch)itemView.findViewById(R.id.open_alarm);
                openAlarm.setChecked(true);
                this.root = itemView;
                openAlarm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        AlarmInfo alarmInfo = mAlarmList.get((Integer) itemView.getTag());
                        if (b){
                            mAlarmManager.set(AlarmManager.RTC_WAKEUP,
                                    alarmInfo.getTimeInMillis(),alarmInfo.getPendingIntent());
                        }else {
                            mAlarmManager.cancel(alarmInfo.getPendingIntent());
                        }
                    }
                });
            }

            /**
             * 显示时间信息
             * @param alarmInfo
             */
            public void setData(AlarmInfo alarmInfo){
                alarmTime.setText(alarmInfo.getAlarmTime());
                alarmDelay.setText(alarmInfo.getAlarmDelay());
            }
        }
    }

    /**
     * 点击事件接口
     */
    public interface RecyclerViewOnItemClickListener{
        void onItemClickListener(View view,int position);
    }

    /**
     * 长按事件接口
     */
    public interface RecyclerViewOnItemLongClickListener{
        boolean onItemLongClickListener(View view,int position);
    }


    /**
     * 设置闹钟
     */
    public void setTime(){
        Calendar calendar = Calendar.getInstance();
        TimePickerDialog timePickerDialog =new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                Calendar c = Calendar.getInstance();
                c.set(Calendar.HOUR_OF_DAY,hourOfDay);
                c.set(Calendar.MINUTE,minute);
                Intent intent = new Intent(AlarmActivity.this,AlarmRingService.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                if (Build.VERSION.SDK_INT >= 26){
                    startForegroundService(intent);
                }else {
                    startService(intent);
                }
                Intent intent1 = new Intent("com.mlly.alarm.alarmring");
                intent1.putExtra("position",mAlarmList.size());
                PendingIntent pendingIntent = PendingIntent.getBroadcast(AlarmActivity.this,100,
                        intent1,0);
                AlarmInfo alarmInfo = new AlarmInfo(hourOfDay+":"+minute,"5小时",pendingIntent);
                alarmInfo.setTimeInMillis(c.getTimeInMillis());
                mAlarmList.add(alarmInfo);
                mAlarmAdapter.notifyDataSetChanged();
                mAlarmManager.set(AlarmManager.RTC_WAKEUP,c.getTimeInMillis(),pendingIntent);
            }
        },calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),true);
        timePickerDialog.show();
    }

    /**
     * 在Fragment结束时用SharedPreferences储存当前创建的AlarmInfo对象
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        SpUtil.clear(getApplicationContext());
        SpUtil.putList(getApplicationContext(),"alarmlist",mAlarmList);
    }
}
