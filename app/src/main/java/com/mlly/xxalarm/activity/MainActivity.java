package com.mlly.xxalarm.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.daobao.asus.dbbaseframe.mvp.view.BaseActivity;
import com.mlly.xxalarm.R;
import com.mlly.xxalarm.adapter.MViewPagerAdapter;
import com.mlly.xxalarm.fragment.AlarmFragment;
import com.mlly.xxalarm.fragment.MyFragment;
import com.mlly.xxalarm.fragment.WeatherFragment;
import com.mlly.xxalarm.presenter.MainPresenter;
import com.mlly.xxalarm.transformer.ZoomOutTransformer;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity<MainPresenter> {

    private TabLayout mTabLayout;                       //TabLayout对象

    private ViewPager mViewPaper;                       //ViewPaper对象

    private LinearLayout mLinearLayout;                 //根布局

    private List<String> mTitles;                       //Tab标题

    private List<Fragment> mFragments;                  //存放Fragment

    private WeatherFragment mWeatherFragment;           //天气Fragment

    private AlarmFragment mAlarmFragment;               //闹钟Fragment

    //将要申请的权限
    private String[] permissions = new String[]{
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    //拒绝的权限
    private List<String> deniedPermission = new ArrayList<>();

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
        mFragments = new ArrayList<>();
        mTitles = new ArrayList<>();
        mLinearLayout = (LinearLayout) findViewById(R.id.linear_layout);
        mLinearLayout.setBackgroundResource(R.mipmap.weather_background_day);
        mTabLayout.setTabTextColors(ColorStateList.valueOf(Color.WHITE));
        mWeatherFragment = new WeatherFragment();
        mAlarmFragment = new AlarmFragment();
        mFragments.add(mWeatherFragment);
        mFragments.add(mAlarmFragment);
        mTitles.add("天气");
        mTitles.add("闹钟");
        MViewPagerAdapter mViewPagerAdapter = new MViewPagerAdapter(getSupportFragmentManager(),
                mTitles,mFragments);
        mViewPaper.setAdapter(mViewPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPaper);
        mViewPaper.setPageTransformer(true,new ZoomOutTransformer());
        mViewPaper.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                switch (i){
                    case 0:mLinearLayout.setBackgroundResource(R.mipmap.weather_background_day);
                        break;
                    case 1:mLinearLayout.setBackgroundResource(R.color.grey);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    /**
     * 检查并申请权限
     */
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
        }
    }

    /**
     * 申请权限回调
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
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
                break;
        }
    }
}
