package com.mlly.xxalarm.fragment;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cleveroad.pulltorefresh.firework.FireworkyPullToRefreshLayout;
import com.daobao.asus.dbbaseframe.mvp.view.BaseFragment;
import com.mlly.xxalarm.R;
import com.mlly.xxalarm.activity.MainActivity;
import com.mlly.xxalarm.presenter.WeatherFragmentPresenter;
import com.mlly.xxalarm.weather.FutureWeatherInfo;
import com.mlly.xxalarm.weather.GridWeatherInfo;
import com.mlly.xxalarm.weather.LifeSuggestion;
import com.mlly.xxalarm.weather.NowWeatherInfo;

import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by liyuanlu on 2018/11/15.
 */
public class WeatherFragment extends BaseFragment<WeatherFragmentPresenter> {

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
    private ImageView mCarWashingIcon;
    private TextView mCarWashing;
    private ImageView mDressingIcon;
    private TextView mDressing;
    private ImageView mFluIcon;
    private TextView mFlu;
    private ImageView mSportIcon;
    private TextView mSport;
    private ImageView mTravelIcon;
    private TextView mTravel;
    private ImageView mUvIcon;
    private TextView mUV;
    private TextView mNowAddress;
    private TextView mWindDirection;
    private TextView mWindDerectionDegree;
    private TextView mWindScale;
    private TextView mWindSpeed;
    private TextView mTemperature;
    private TextView mPressure;
    private FireworkyPullToRefreshLayout mSwipeRefreshLayout;

    private boolean firstLoad = false;

    private View view;

    private Snackbar snackbar;

    private static int[] mIcons;                    //天气状态图标Id数组

    /**
     * 天气图标初始化
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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_weather,container,false);
        Log.d(TAG, "onCreateView: ");
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        init();
        super.onActivityCreated(savedInstanceState);
    }

    /**
     * 初始化控件
     */
    private void init() {
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
        mCarWashingIcon = (ImageView) view.findViewById(R.id.car_washing_icon);
        mCarWashing = (TextView) view.findViewById(R.id.car_washing);
        mDressingIcon = (ImageView) view.findViewById(R.id.dressing_icon);
        mDressing = (TextView) view.findViewById(R.id.dressing);
        mFluIcon = (ImageView) view.findViewById(R.id.flu_icon);
        mFlu = (TextView) view.findViewById(R.id.flu);
        mSportIcon = (ImageView) view.findViewById(R.id.sport_icon);
        mSport = (TextView) view.findViewById(R.id.sport);
        mTravelIcon = (ImageView) view.findViewById(R.id.travel_icon);
        mTravel = (TextView) view.findViewById(R.id.travel);
        mUvIcon = (ImageView) view.findViewById(R.id.uv_icon);
        mUV = (TextView) view.findViewById(R.id.uv);
        mNowAddress = (TextView)view.findViewById(R.id.now_address);
        mWindDirection = (TextView)view.findViewById(R.id.wind_direction);
        mWindDerectionDegree = (TextView)view.findViewById(R.id.wind_direction_degree);
        mWindScale = (TextView)view.findViewById(R.id.wind_scale);
        mWindSpeed = (TextView)view.findViewById(R.id.wind_speed);
        mTemperature = (TextView)view.findViewById(R.id.temperature);
        mPressure = (TextView)view.findViewById(R.id.pressure);
        mSwipeRefreshLayout = (FireworkyPullToRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        //设置监听器
        mSwipeRefreshLayout.setOnRefreshListener(new FireworkyPullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                startLoacte();
            }
        });
        mPresenter.startLocate();
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
            mNowIcon.setImageResource(mIcons[Integer.parseInt(nowBean.getCode())]);
            mNowTemperature.setText(nowBean.getTemperature());
            mNowText.setText(nowBean.getText());
        }else {
            showMessage("获取当前天气失败");
        }
    }

    /**
     * 天气更新成功提示
     */
    public void refreshSuccess(){
        showMessage("天气更新成功");
    }

    public void refreshFailed(){
        showMessage("天气更新失败");
    }

    /**
     * 使用Snackbar提示消息
     * @param message
     */
    public void showMessage(String message) {
        if (snackbar == null){
            snackbar = Snackbar.make(view,message,Snackbar.LENGTH_SHORT);
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
            mUV.setText(suggestionBean.getUv().getBrief());
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
            mWindDerectionDegree.setText(nowGridBean.getWind_direction_degree());
            mWindDirection.setText(nowGridBean.getWind_direction());
        }
    }

    public void stopRefresh(){
        if (mSwipeRefreshLayout != null && mSwipeRefreshLayout.isRefreshing()){
            getActivity().runOnUiThread(new Runnable() {
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

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected WeatherFragmentPresenter binPresenter() {
        return new WeatherFragmentPresenter(this);
    }
}
