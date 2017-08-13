package ru.overtired.yamblz2017.main_activity;

import ru.overtired.yamblz2017.data.forecastApi.ForecastDay;

public interface MainView {
    void addDetailFragment(ForecastDay forecastDay);
}
