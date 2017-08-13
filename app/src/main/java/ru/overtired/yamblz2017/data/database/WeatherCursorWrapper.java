package ru.overtired.yamblz2017.data.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import ru.overtired.yamblz2017.data.Weather;

/**
 * Created by overtired on 15.07.17.
 */

public class WeatherCursorWrapper extends CursorWrapper {
    public WeatherCursorWrapper(Cursor cursor){
        super(cursor);
    }

    public Weather getWeather(){
        Weather weather = new Weather();

        weather.city = getString(getColumnIndex(DatabaseScheme.WeatherTable.Cols.CITY));
        weather.weather = getString(getColumnIndex(DatabaseScheme.WeatherTable.Cols.WEATHER));

        weather.tempCelsius = String.valueOf(getDouble(getColumnIndex(DatabaseScheme.WeatherTable.Cols.TEMP_C)));
        weather.tempFarengate = String.valueOf(getDouble(getColumnIndex(DatabaseScheme.WeatherTable.Cols.TEMP_F)));

        weather.feelsLikeCelsius= String.valueOf(getDouble(getColumnIndex(DatabaseScheme.WeatherTable.Cols.FEELS_C)));
        weather.feelsLikeFarengate= String.valueOf(getDouble(getColumnIndex(DatabaseScheme.WeatherTable.Cols.TEMP_F)));

        weather.humidity = getString(getColumnIndex(DatabaseScheme.WeatherTable.Cols.HUMIDITY));

        weather.windSpeedKph = String.valueOf(getDouble(getColumnIndex(DatabaseScheme.WeatherTable.Cols.WIND_KPH)));
        weather.windSpeedMph = String.valueOf(getDouble(getColumnIndex(DatabaseScheme.WeatherTable.Cols.WIND_MPH)));

        weather.dewPointCelsius = String.valueOf(getDouble(getColumnIndex(DatabaseScheme.WeatherTable.Cols.DEW_POINT_C)));
        weather.dewPointFarengate= String.valueOf(getDouble(getColumnIndex(DatabaseScheme.WeatherTable.Cols.DEW_POINT_F)));

        weather.imageUrl = getString(getColumnIndex(DatabaseScheme.WeatherTable.Cols.ICON_URL));

        weather.date = getString(getColumnIndex(DatabaseScheme.WeatherTable.Cols.DATE));

        weather.lang = getString(getColumnIndex(DatabaseScheme.WeatherTable.Cols.LANG));

        return weather;
    }
}
