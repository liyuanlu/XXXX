package com.mlly.xxalarm.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daobao.asus.dbbaseframe.mvp.view.BaseActivity;
import com.mlly.xxalarm.R;
import com.mlly.xxalarm.presenter.MainPresenter;
import com.mlly.xxalarm.weather.FutureWeatherInfo;
import com.mlly.xxalarm.weather.NowWeatherInfo;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends BaseActivity<MainPresenter> {

    private TabLayout mTabLayout;                       //TabLayout对象

    private ViewPager mViewPaper;                       //ViewPaper对象

    private LinearLayout mLinearLayout;                 //根布局

    private ArrayList<View> mViews;                     //存放显示在ViewPaper上的对象

    //Tab标题
    private String[] mTitles = new String[]{"闹钟", "天气"};

    //将要申请的权限
    private String[] permissions = new String[]{
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    //拒绝的权限
    private List<String> deniedPermission = new ArrayList<>();

    private SwipeRefreshLayout mSwipeRefreshLayout;

    private TextView mNowTemperature;
    private ImageView mNowIcon;
    private TextView mNowText;
    private TextView mTodayText;
    private TextView mTodayLow;
    private TextView mTodayHigh;
    private ImageView mTodayIcon;
    private TextView mTomorrowText;
    private TextView mTomorrowLow;
    private TextView mTomorrowHigh;
    private ImageView mTomorrowIcon;
    private TextView mAfterTomorrowText;
    private TextView mAfterTomorrowLow;
    private TextView mAfterTomorrowHigh;
    private ImageView mAfterTomorrowIcon;
    private LinearLayout mLinearLayoutWeather;

    //图标Id数组
    private static int[] mIcons;

    /**
     * 初始化图标数组
     */
    static {
         mIcons = new int[]{
                R.drawable.w_0,R.drawable.w_1,R.drawable.w_2,R.drawable.w_3,R.drawable.w_4,
                R.drawable.w_5,R.drawable.w_6,R.drawable.w_7,R.drawable.w_8,R.drawable.w_9,
                R.drawable.w_10,R.drawable.w_11,R.drawable.w_12,R.drawable.w_13,R.drawable.w_14,
                R.drawable.w_15,R.drawable.w_16,R.drawable.w_17,R.drawable.w_18,R.drawable.w_19,
                R.drawable.w_20,R.drawable.w_21,R.drawable.w_22,R.drawable.w_23,R.drawable.w_24,
                R.drawable.w_25,R.drawable.w_26,R.drawable.w_27,R.drawable.w_28,R.drawable.w_29,
                R.drawable.w_30,R.drawable.w_31,R.drawable.w_32,R.drawable.w_33,R.drawable.w_34,
                R.drawable.w_35,R.drawable.w_36,R.drawable.w_37,R.drawable.w_38,R.drawable.w_99
        };
    }

    @Override
    public MainPresenter binPresenter() {
        return new MainPresenter(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFlag();
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);
        initPermission();
    }

    /**
     * 占用状态栏
     */
    @TargetApi(21)
    private void setFlag() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
    }

    /**
     * 初始化控件
     */
    private void init() {
        mTabLayout = (TabLayout) findViewById(R.id.tab_layout);
        mViewPaper = (ViewPager) findViewById(R.id.view_paper);
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            //切换Tab时更换背景色
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (mTabLayout.getSelectedTabPosition()) {
                    case 0:
                        mLinearLayout.setBackgroundColor(Color.BLACK);
                        break;
                    case 1:
                        mLinearLayout.setBackgroundColor(Color.GRAY);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        mLinearLayout = (LinearLayout) findViewById(R.id.linear_layout);
        mTabLayout.setTabTextColors(ColorStateList.valueOf(Color.WHITE));
        View alarm = getLayoutInflater().inflate(R.layout.layout_alarm, null);
        View weather = getLayoutInflater().inflate(R.layout.layout_weather, null);
        initView(weather);
        mViews = new ArrayList<>();
        mViews.add(alarm);
        mViews.add(weather);
        mTabLayout.setupWithViewPager(mViewPaper);
        //添加适配器
        mViewPaper.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return mViews.size();
            }

            @Override
            public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
                return view == o;
            }

            @NonNull
            @Override
            public Object instantiateItem(@NonNull ViewGroup container, int position) {
                View view = mViews.get(position);
                container.addView(view);
                return view;
            }

            @Override
            public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
                container.removeView((View) object);
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                return mTitles[position];
            }
        });
    }

    private void initPermission() {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) !=
                    PackageManager.PERMISSION_GRANTED) {
                deniedPermission.add(permission);
            }
        }
        if (deniedPermission.size() != 0) {
            ActivityCompat.requestPermissions(this, deniedPermission.toArray(
                    new String[deniedPermission.size()]), 1);
        }else {
            init();
            mPresenter.startLocate();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                for (int value : grantResults) {
                    if (value != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, "请授予权限", Toast.LENGTH_LONG).show();
                        finish();
                        return;
                    }
                }
                init();
                mPresenter.startLocate();
                break;
        }
    }

    private void initView(View view) {
        mNowTemperature = (TextView) view.findViewById(R.id.now_temperature);
        mNowIcon = (ImageView) view.findViewById(R.id.now_icon);
        mNowText = (TextView) view.findViewById(R.id.now_text);
        mTodayText = (TextView) view.findViewById(R.id.today_text);
        mTodayLow = (TextView) view.findViewById(R.id.today_low);
        mTodayHigh = (TextView) view.findViewById(R.id.today_high);
        mTodayIcon = (ImageView) view.findViewById(R.id.today_icon);
        mTomorrowText = (TextView) view.findViewById(R.id.tomorrow_text);
        mTomorrowLow = (TextView) view.findViewById(R.id.tomorrow_low);
        mTomorrowHigh = (TextView) view.findViewById(R.id.tomorrow_high);
        mTomorrowIcon = (ImageView) view.findViewById(R.id.tomorrow_icon);
        mAfterTomorrowText = (TextView) view.findViewById(R.id.after_tomorrow_text);
        mAfterTomorrowLow = (TextView) view.findViewById(R.id.after_tomorrow_low);
        mAfterTomorrowHigh = (TextView) view.findViewById(R.id.after_tomorrow_high);
        mAfterTomorrowIcon = (ImageView) view.findViewById(R.id.after_tomorrow_icon);
        mLinearLayoutWeather = (LinearLayout)view.findViewById(R.id.linear_layout_weather);
        mSwipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.startLocate();
            }
        });
    }

    /**
     * 获取到天气消息后停止刷新
     */
    public void stopRefresh(){
        if (mSwipeRefreshLayout.isRefreshing() && mSwipeRefreshLayout != null){
            mSwipeRefreshLayout.setRefreshing(false);
            Snackbar.make(mLinearLayoutWeather,"天气更新成功",Snackbar.LENGTH_SHORT).show();
        }
    }

    /**
     * 获取当前天气对象
     * @param nowWeatherInfo
     */
    public void getNowWeatherData(NowWeatherInfo nowWeatherInfo){
        if (nowWeatherInfo != null){
            NowWeatherInfo.ResultsBean.NowBean nowBean = nowWeatherInfo.getResults().get(0).getNow();
            mNowIcon.setImageResource(mIcons[Integer.parseInt(nowBean.getCode())]);
            mNowTemperature.setText(nowBean.getTemperature());
            mNowText.setText(nowBean.getText());
        }else {
            showMessage("获取当前天气失败");
        }
    }

    /**
     * 获取未来天气对象
     * @param futureWeatherInfo
     */
    public void getFutureWeatherData(FutureWeatherInfo futureWeatherInfo){
        if (futureWeatherInfo != null){
            List<FutureWeatherInfo.ResultsBean.DailyBean> dailyBeans =
                    futureWeatherInfo.getResults().get(0).getDaily();
            switch (dailyBeans.size()){
                case 3:
                    mAfterTomorrowIcon.setImageResource(mIcons[Integer.parseInt(dailyBeans.get(2).getCode_day())]);
                    mAfterTomorrowHigh.setText(dailyBeans.get(2).getHigh());
                    mAfterTomorrowLow.setText(dailyBeans.get(2).getLow());
                    mAfterTomorrowText.setText(dailyBeans.get(2).getText_day());
                case 2:
                    mTomorrowIcon.setImageResource(mIcons[Integer.parseInt(dailyBeans.get(1).getCode_day())]);
                    mTomorrowHigh.setText(dailyBeans.get(1).getHigh());
                    mTomorrowLow.setText(dailyBeans.get(1).getLow());
                    mTomorrowText.setText(dailyBeans.get(1).getText_day());
                case 1:
                    mTodayIcon.setImageResource(mIcons[Integer.parseInt(dailyBeans.get(0).getCode_day())]);
                    mTodayHigh.setText(dailyBeans.get(0).getHigh());
                    mTodayLow.setText(dailyBeans.get(0).getLow());
                    mTodayText.setText(dailyBeans.get(0).getText_day());
                    break;
                default:showMessage("获取未来天气失败");
                    break;
            }
        }else {
            showMessage("获取未来天气失败");
        }
    }
}
