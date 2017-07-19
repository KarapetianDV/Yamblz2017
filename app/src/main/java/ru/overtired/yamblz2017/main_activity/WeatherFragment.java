package ru.overtired.yamblz2017.main_activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.sql.SQLDataException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import ru.overtired.yamblz2017.R;
import ru.overtired.yamblz2017.service.WeatherRequestJob;
import ru.overtired.yamblz2017.data.ResponseProcesser;
import ru.overtired.yamblz2017.data.Weather;
import ru.overtired.yamblz2017.data.WeatherFetcher;
import ru.overtired.yamblz2017.data.database.Dao;
import ru.overtired.yamblz2017.service.WeatherService;

/**
 * Created by overtired on 09.07.17.
 */

public class WeatherFragment extends Fragment implements  WeatherView{
    public static final String TAG = "WeatherFragment";

    WeatherPresenter presenter;

    private Unbinder unbinder;

    @BindView(R.id.card_time)
    TextView cardTime;
    @BindView(R.id.card_image)
    ImageView cardImage;
    @BindView(R.id.card_temp)
    TextView cardTemp;
    @BindView(R.id.card_feelslike_temp)
    TextView cardFeelsTemp;
    @BindView(R.id.card_humidity)
    TextView cardHumidity;
    @BindView(R.id.card_windspeed)
    TextView cardWindSpeed;
    @BindView(R.id.card_weather)
    TextView cardWeather;

    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout refreshLayout;

    private BroadcastReceiver updateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            presenter.onUpdateWeather();
        }
    };

    public static WeatherFragment newInstance() {
        return new WeatherFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new WeatherPresenterImpl(this,
                new WeatherModelImpl(getActivity().getApplicationContext()));
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.onResume();//???

        IntentFilter filter = new IntentFilter(WeatherService.ACTION_UPDATE_WEATHER);
        getActivity().registerReceiver(updateReceiver, filter);
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(updateReceiver);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_activity_weather, container, false);
        unbinder = ButterKnife.bind(this, view);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.onRefreshWeather();
            }
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void setWeather(Weather weather) {
        Picasso.with(getActivity().getApplicationContext())
                .load(weather.imageUrl).into(cardImage);
        cardTemp.setText(Double.toString(weather.tempCelsius)+"°C ");
        cardFeelsTemp.setText(Double.toString(weather.feelsLikeCelsius)+"°C ");
        SimpleDateFormat format = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
        cardTime.setText(format.format(weather.date));
        cardHumidity.setText(weather.humidity);
        cardWindSpeed.setText(weather.windSpeedKph+" "+getString(R.string.kph));
        cardWeather.setText(weather.weather);
    }

    @Override
    public void hideProgress() {
        refreshLayout.setRefreshing(false);
    }
}
