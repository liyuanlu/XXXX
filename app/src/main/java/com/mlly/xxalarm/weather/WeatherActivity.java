package com.mlly.xxalarm.weather;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cleveroad.pulltorefresh.firework.FireworkyPullToRefreshLayout;
import com.daobao.asus.dbbaseframe.mvp.view.BaseActivity;
import com.mlly.xxalarm.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class WeatherActivity extends BaseActivity<WeatherPresenter> {

    //将要申请的权限
    private String[] permissions = new String[]{
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    //拒绝的权限
    private List<String> deniedPermission = new ArrayList<>();
    private TextView mNowTemperature;
    private ImageView mNowIcon;
    private TextView mNowAddress;
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
    private TextView mCarWashing;
    private TextView mDressing;
    private TextView mFlu;
    private TextView mSport;
    private TextView mTravel;
    private TextView mUv;
    private TextView mWindDirection;
    private TextView mWindDirectionDegree;
    private TextView mWindScale;
    private TextView mWindSpeed;
    private TextView mTemperature;
    private TextView mPressure;
    private TextView mCityName;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private Snackbar snackbar;
    private String mInputCity;
    private static int[] mIcons;                    //天气状态图标Id数组
    /**
     * 天气图标初始化
     */
    static {
        mIcons = new int[]{
                R.drawable.w_0, R.drawable.w_1, R.drawable.w_2, R.drawable.w_3, R.drawable.w_4,
                R.drawable.w_5, R.drawable.w_6, R.drawable.w_7, R.drawable.w_8, R.drawable.w_9,
                R.drawable.w_10, R.drawable.w_11, R.drawable.w_12, R.drawable.w_13, R.drawable.w_14,
                R.drawable.w_15, R.drawable.w_16, R.drawable.w_17, R.drawable.w_18, R.drawable.w_19,
                R.drawable.w_20, R.drawable.w_21, R.drawable.w_22, R.drawable.w_23, R.drawable.w_24,
                R.drawable.w_25, R.drawable.w_26, R.drawable.w_27, R.drawable.w_28, R.drawable.w_29,
                R.drawable.w_30, R.drawable.w_31, R.drawable.w_32, R.drawable.w_33, R.drawable.w_34,
                R.drawable.w_35, R.drawable.w_36, R.drawable.w_37, R.drawable.w_38, R.drawable.w_99
        };
    }

    @Override
    public WeatherPresenter binPresenter() {
        return new WeatherPresenter(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFlag();
        getSPTheme();
        setContentView(R.layout.activity_weather);
        initView();
        initPermission();
    }

    public void setLocatedCityName(String cityName){
        mCityName.setText(cityName);
        mInputCity = cityName;
    }

    private void getSPTheme() {
        SharedPreferences sp = getSharedPreferences("save_theme",Context.MODE_PRIVATE);
        int theme = sp.getInt("color",0);
        Log.d("theme", "" + theme);
        switch (theme){
            case 0:setTheme(R.style.AppTheme);
                break;
            case 1:setTheme(R.style.BlueTheme);
                break;
            case 2:setTheme(R.style.PinkTheme);
                break;
            case 3:setTheme(R.style.GreenTheme);
                break;
            case 4:setTheme(R.style.PurpleTheme);
                break;
            default:setTheme(R.style.AppTheme);
                break;
        }
    }

    /**
     * 占用状态栏
     */

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setFlag() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
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
        } else {
            startLoacte();
        }
    }

    /**
     * 申请权限回调
     *
     * @param requestCode  权限请求码
     * @param permissions  需要请求的权限
     * @param grantResults 权限请求结果
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
                startLoacte();
                break;
        }
    }

    private void initView() {
        mNowTemperature = (TextView) findViewById(R.id.now_temperature);
        mNowIcon = (ImageView) findViewById(R.id.now_icon);
        mNowAddress = (TextView) findViewById(R.id.now_address);
        mNowText = (TextView) findViewById(R.id.now_text);
        mTodayText = (TextView) findViewById(R.id.today_text);
        mTodayLow = (TextView) findViewById(R.id.today_low);
        mTodayHigh = (TextView) findViewById(R.id.today_high);
        mTodayIcon = (ImageView) findViewById(R.id.today_icon);
        mTomorrowText = (TextView) findViewById(R.id.tomorrow_text);
        mTomorrowLow = (TextView) findViewById(R.id.tomorrow_low);
        mTomorrowHigh = (TextView) findViewById(R.id.tomorrow_high);
        mTomorrowIcon = (ImageView) findViewById(R.id.tomorrow_icon);
        mAfterTomorrowText = (TextView) findViewById(R.id.after_tomorrow_text);
        mAfterTomorrowLow = (TextView) findViewById(R.id.after_tomorrow_low);
        mAfterTomorrowHigh = (TextView) findViewById(R.id.after_tomorrow_high);
        mAfterTomorrowIcon = (ImageView) findViewById(R.id.after_tomorrow_icon);
        mCarWashing = (TextView) findViewById(R.id.car_washing);
        mDressing = (TextView) findViewById(R.id.dressing);
        mFlu = (TextView) findViewById(R.id.flu);
        mSport = (TextView) findViewById(R.id.sport);
        mTravel = (TextView) findViewById(R.id.travel);
        mUv = (TextView) findViewById(R.id.uv);
        mWindDirection = (TextView) findViewById(R.id.wind_direction);
        mWindDirectionDegree = (TextView) findViewById(R.id.wind_direction_degree);
        mWindScale = (TextView) findViewById(R.id.wind_scale);
        mWindSpeed = (TextView) findViewById(R.id.wind_speed);
        mTemperature = (TextView) findViewById(R.id.temperature);
        mPressure = (TextView) findViewById(R.id.pressure);
        mCityName = (TextView)findViewById(R.id.city_name);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        //设置监听器
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                startLoacte();
            }
        });
        AppBarLayout mAppbarLayout = (AppBarLayout) findViewById(R.id.appbar_layout);
        mAppbarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
                if (i >= 0){
                    mSwipeRefreshLayout.setEnabled(true);
                }else {
                    mSwipeRefreshLayout.setEnabled(false);
                }
            }
        });
        Toolbar mToolbar = (Toolbar) findViewById(R.id.weather_toolbar);
        setSupportActionBar(mToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mPresenter.startLocate();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.weather_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.search_city_weather:
                searchCityWeather();
                break;
                default:break;
        }
        return true;
    }

    private void searchCityWeather() {
        final EditText text = new EditText(this);
        new AlertDialog.Builder(this).setTitle("请输入城市名")
                .setIcon(R.drawable.search)
                .setView(text)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mPresenter.requestCityWeatherData(text.getText().toString());
                        mInputCity = text.getText().toString();
                    }
                }).setNegativeButton("取消",null).show();
    }

    public void getNowAddress(String address){
        mNowAddress.setText(address);
    }

    /**
     * 获取当前天气对象
     * @param nowWeatherInfo 当前天气对象
     */
    public void getNowWeatherData(NowWeatherInfo nowWeatherInfo){
        if (nowWeatherInfo != null){
            NowWeatherInfo.ResultsBean.NowBean nowBean = nowWeatherInfo.getResults().get(0).getNow();
            int weatherCode = Integer.parseInt(nowBean.getCode());
            mNowIcon.setImageResource(mIcons[weatherCode]);
            setWeatherBackground(weatherCode);
            mNowTemperature.setText(nowBean.getTemperature());
            mNowText.setText(nowBean.getText());
        }else {
            showMessage("获取当前天气失败");
        }
    }

    /**
     * 更换天气背景
     */
    public void setWeatherBackground(int code){
        CollapsingToolbarLayout layout = (CollapsingToolbarLayout)findViewById(R.id.collapsing_layout);
        CoordinatorLayout coordinatorLayout = (CoordinatorLayout)findViewById(R.id.coordinator_layout);
        if (code >= 0 && code <= 3){
            layout.setBackgroundResource(R.drawable.sunny);
            coordinatorLayout.setBackgroundColor(Color.parseColor("#FCCE74"));
        }else if (code >= 4 && code <= 9){
            layout.setBackgroundResource(R.drawable.windy);
            coordinatorLayout.setBackgroundColor(Color.parseColor("#4468B6"));
        }else if (code >= 10 && code <= 19){
            layout.setBackgroundResource(R.drawable.rainy);
            coordinatorLayout.setBackgroundColor(Color.parseColor("#909EE9"));
        }else if (code >= 20 && code <= 25){
            layout.setBackgroundResource(R.drawable.snowy);
            coordinatorLayout.setBackgroundColor(Color.parseColor("#5DB0FF"));
        }else {
            layout.setBackgroundColor(Color.YELLOW);
            coordinatorLayout.setBackgroundColor(Color.CYAN);
        }
    }

    /**
     * 设置指定城市名字
     */
    public void setCityName(){
        mCityName.setText(mInputCity);
    }

    /**
     * 天气更新成功提示
     */
    public void refreshSuccess(){
        showMessage("天气更新成功");
        setCityName();
    }

    public void refreshFailed(){
        showMessage("天气更新失败");
    }

    /**
     * 使用Snackbar提示消息
     * @param message 将要提示的消息
     */
    public void showMessage(String message) {
        if (snackbar == null){
            snackbar = Snackbar.make(mSwipeRefreshLayout,message,Snackbar.LENGTH_SHORT);
            snackbar.show();
        }else {
            snackbar.setText(message).show();
        }
    }

    /**
     * 获取未来天气数据
     * @param futureWeatherInfo 未来天气数据对象
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

    /**
     * 获取生活建议数据
     * @param lifeSuggestion 生活建议对象
     */
    public void getLifeSuggestion(LifeSuggestion lifeSuggestion){
        if (lifeSuggestion != null){
            LifeSuggestion.ResultsBean.SuggestionBean suggestionBean = lifeSuggestion.getResults()
                    .get(0).getSuggestion();
            mCarWashing.setText(suggestionBean.getCar_washing().getBrief());
            mDressing.setText(suggestionBean.getDressing().getBrief());
            mFlu.setText(suggestionBean.getFlu().getBrief());
            mSport.setText(suggestionBean.getSport().getBrief());
            mTravel.setText(suggestionBean.getTravel().getBrief());
            mUv.setText(suggestionBean.getUv().getBrief());
        }
    }

    public void getGridWeather(GridWeatherInfo gridWeatherInfo){
        if (gridWeatherInfo != null){
            GridWeatherInfo.ResultsBean.NowGridBean nowGridBean = gridWeatherInfo.getResults().get(0)
                    .getNow_grid();
            mPressure.setText(nowGridBean.getPressure());
            mTemperature.setText(nowGridBean.getTemperature());
            mWindSpeed.setText(nowGridBean.getWind_speed());
            mWindScale.setText(nowGridBean.getWind_scale());
            mWindDirectionDegree.setText(nowGridBean.getWind_direction_degree());
            mWindDirection.setText(nowGridBean.getWind_direction());
        }
    }

    public void stopRefresh(){
        if (mSwipeRefreshLayout != null && mSwipeRefreshLayout.isRefreshing()){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            });
        }
    }

    public void startLoacte(){
        mPresenter.startLocate();
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
            }
        });
    }
}
