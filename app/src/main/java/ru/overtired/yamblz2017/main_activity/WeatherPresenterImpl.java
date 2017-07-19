package ru.overtired.yamblz2017.main_activity;

import ru.overtired.yamblz2017.data.Weather;

/**
 * Created by overtired on 18.07.17.
 */

public class WeatherPresenterImpl implements WeatherPresenter {
    private WeatherView view;
    private WeatherModel model;

    private AsyncFetcher fetcher;

    public WeatherPresenterImpl(WeatherView view, WeatherModel model){
        this.view = view;
        this.model = model;
    }

    @Override
    public void onRefreshWeather() {
        if(fetcher!=null){
            fetcher.cancel(true);
        }
        fetcher = new AsyncFetcher(new Runnable() {
            @Override
            public void run() {
                onUpdateWeather();
            }
        });
        fetcher.execute();//TODO: params
    }

    @Override
    public void onUpdateWeather() {
        Weather weather = model.getLastWeather();
        if(weather == null){
            onRefreshWeather();
        }else {
            view.setWeather(weather);
        }
    }

    @Override
    public void onResume() {
        onUpdateWeather();
    }
}
