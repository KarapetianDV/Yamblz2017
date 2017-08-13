package ru.overtired.yamblz2017.main_activity;

import ru.overtired.yamblz2017.data.forecastApi.ForecastApi;

/**
 * Created by overtired on 18.07.17.
 */

public interface WeatherPresenter {
    void onRefreshWeather();
    void onUpdateWeather();
    void onResume();
    void onDestroy();
    void setView(WeatherView view);
    void onWeatherLoaded();
    void onWeatherLoadingError();
    void loadForecast();
    void onForecastLoaded(ForecastApi forecastApi);
    void onForecastLoadingError(Throwable t);
    void onClickInRecyclerView(int position);
}
