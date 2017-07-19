package ru.overtired.yamblz2017.main_activity;

/**
 * Created by overtired on 18.07.17.
 */

public interface WeatherPresenter {
    void onRefreshWeather();
    void onUpdateWeather();
    void onResume();

}
