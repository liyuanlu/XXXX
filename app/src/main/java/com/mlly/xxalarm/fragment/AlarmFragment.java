package com.mlly.xxalarm.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import com.daobao.asus.dbbaseframe.mvp.view.BaseFragment;
import com.mlly.xxalarm.R;
import com.mlly.xxalarm.presenter.AlarmFragmentPresenter;
import com.mlly.xxalarm.weather.AlarmInfo;

import java.util.ArrayList;

/**
 * Created by liyuanlu on 2018/11/22.
 */
public class AlarmFragment extends BaseFragment<AlarmFragmentPresenter> {

    private View view;                              //该Fragment对象

    private RecyclerView mRecyclerView;                //显示闹钟item的view

    private FloatingActionButton mFloatingButton;   //悬浮按钮

    private ArrayList<AlarmInfo> mAlarmList;         //闹钟信息List

    private MAdapter mAlarmAdapter;                 //

    @Override
    protected AlarmFragmentPresenter binPresenter() {
        return new AlarmFragmentPresenter(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_alarm,container,false);
        init();
        return view;
    }

    private void init() {
        mRecyclerView = (RecyclerView)view.findViewById(R.id.alarm_list);
        mFloatingButton = (FloatingActionButton)view.findViewById(R.id.add_alarm);
        //FloatingActionButton点击事件
        mFloatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /**
                 * FloatingActionButton点击事件
                 */
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

}
