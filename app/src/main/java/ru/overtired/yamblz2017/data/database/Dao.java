package ru.overtired.yamblz2017.data.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import ru.overtired.yamblz2017.App;
import ru.overtired.yamblz2017.data.Weather;

/**
 * Created by overtired on 15.07.17.
 */

public class Dao {
    private static final String TAG = Dao.class.getSimpleName();
    private static final int FORECAST_ITEMS = 10;
    private Context context;

    @NonNull
    public static Dao get(@NonNull Context context) {

        App app = (App) context.getApplicationContext();

        if (app.getDao() == null) {
            Dao dao = new Dao(context.getApplicationContext());
            app.setDao(dao);
        }

        return app.getDao();
    }


    private Dao(@NonNull Context context) {
        this.context = context;
    }

    public synchronized void addWeather(@NonNull Weather weather) {
        SQLiteDatabase database = new DatabaseHelper(context).getWritableDatabase();

        ContentValues contentValues = getWeatherContentValues(weather);

        new DatabaseTask().execute(database, contentValues);
    }

    @Nullable
    public synchronized Weather getLastWeather(String lang) {
        SQLiteDatabase database = new DatabaseHelper(context).getReadableDatabase();

        Cursor cursor = database.query(DatabaseScheme.WeatherTable.NAME,
                null,
                DatabaseScheme.WeatherTable.Cols.LANG+"=?",
                new String[]{lang},
                null,
                null,
                DatabaseScheme.WeatherTable.Cols.DATE
        );
        Log.d(TAG, "getLastWeather: " + DatabaseUtils.dumpCursorToString(cursor));
        WeatherCursorWrapper cursorWrapper = new WeatherCursorWrapper(cursor);

        if (cursorWrapper.getCount() == 0) {
            cursorWrapper.close();
            return null;
        } else {
            cursorWrapper.moveToLast();
            Weather weather = cursorWrapper.getWeather();
            return weather;
        }
    }

    @NonNull
    private ContentValues getWeatherContentValues(@NonNull Weather weather) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(DatabaseScheme.WeatherTable.Cols.CITY, weather.city);
        contentValues.put(DatabaseScheme.WeatherTable.Cols.TEMP_C, weather.tempCelsius);
        contentValues.put(DatabaseScheme.WeatherTable.Cols.TEMP_F, weather.tempFarengate);
        contentValues.put(DatabaseScheme.WeatherTable.Cols.FEELS_C, weather.feelsLikeCelsius);
        contentValues.put(DatabaseScheme.WeatherTable.Cols.FEELS_F, weather.feelsLikeFarengate);
        contentValues.put(DatabaseScheme.WeatherTable.Cols.HUMIDITY, weather.humidity);
        contentValues.put(DatabaseScheme.WeatherTable.Cols.WIND_KPH, weather.windSpeedKph);
        contentValues.put(DatabaseScheme.WeatherTable.Cols.WIND_MPH, weather.windSpeedMph);
        contentValues.put(DatabaseScheme.WeatherTable.Cols.DEW_POINT_C, weather.dewPointCelsius);
        contentValues.put(DatabaseScheme.WeatherTable.Cols.DEW_POINT_F, weather.dewPointFarengate);
        contentValues.put(DatabaseScheme.WeatherTable.Cols.ICON_URL, weather.imageUrl);
        contentValues.put(DatabaseScheme.WeatherTable.Cols.WEATHER, weather.weather);

        contentValues.put(DatabaseScheme.WeatherTable.Cols.DATE, weather.date);
        contentValues.put(DatabaseScheme.WeatherTable.Cols.LANG,weather.lang);

        return contentValues;
    }

    private class DatabaseTask extends AsyncTask<Object, Void, Void> {

        @Override
        protected Void doInBackground(Object... params) {
            SQLiteDatabase database = (SQLiteDatabase) params[0];
            ContentValues contentValues = (ContentValues) params[1];
            Cursor cursor = database.query(
                    DatabaseScheme.WeatherTable.NAME,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null
            );

            boolean isEmpty = cursor.getCount() < FORECAST_ITEMS;

            Log.d(TAG, "doInBackground: " + cursor.getCount());
            Log.d(TAG, "doInBackground: " + contentValues.size());

            Log.d(TAG, "doInBackground: " + isEmpty);

            if(isEmpty) {
                database.insert(
                        DatabaseScheme.WeatherTable.NAME,
                        null,
                        contentValues
                );
            } else {
                database.update(
                        DatabaseScheme.WeatherTable.NAME,
                        contentValues,
                        "date = ?",
                        new String[]{contentValues.getAsString(DatabaseScheme.WeatherTable.Cols.DATE)}
                );
            }

            return null;
        }
    }
}
