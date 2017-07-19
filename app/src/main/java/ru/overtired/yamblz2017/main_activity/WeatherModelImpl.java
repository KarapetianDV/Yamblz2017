package ru.overtired.yamblz2017.main_activity;

import android.content.Context;

import java.sql.SQLDataException;

import ru.overtired.yamblz2017.data.Weather;
import ru.overtired.yamblz2017.data.database.Dao;

/**
 * Created by overtired on 18.07.17.
 */

public class WeatherModelImpl implements WeatherModel {
    private Context context;

    public WeatherModelImpl(Context context) {
        this.context = context.getApplicationContext();
    }

    @Override
    public void addWeather(Weather weather) {
        Dao.get(context).addWeather(weather);
    }

    @Override
    public Weather getLastWeather() {
        return Dao.get(context).getLastWeather();
    }
}
