package ru.overtired.yamblz2017.main_activity;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.sql.SQLDataException;
import java.text.ParseException;

import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import ru.overtired.yamblz2017.data.ResponseProcesser;
import ru.overtired.yamblz2017.data.Weather;
import ru.overtired.yamblz2017.data.WeatherFetcher;
import ru.overtired.yamblz2017.data.database.Dao;

/**
 * Created by overtired on 18.07.17.
 */

public class WeatherModelImpl implements WeatherModel {
    private WeatherPresenter presenter;

    private Context context;
    AsyncFetcher fetcher;

    public WeatherModelImpl(Context context) {
        this.context = context.getApplicationContext();
    }

    @Override
    public void setPresenter(WeatherPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public WeatherPresenter getPresenter() {
        return null;
    }

    @Override
    public void addWeather(Weather weather) {
        Dao.get(context).addWeather(weather);
    }

    @Override
    public Weather getLastWeather() {
        return Dao.get(context).getLastWeather();
    }

    @Override
    public void loadNewWeather() {
        if (fetcher != null) {
            fetcher.cancel(true);
        }
        fetcher = new AsyncFetcher();
        fetcher.execute();//TODO: params
    }

    class AsyncFetcher extends AsyncTask<String, Void, Weather> {
        @Override
        protected Weather doInBackground(String... params) {

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://api.wunderground.com/api/")
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .build();

            WeatherFetcher fetcher = retrofit.create(WeatherFetcher.class);

            try {
                String response = fetcher.getWheather("7e79982c03e614a8", "RU", "Moscow")
                        .execute()
                        .body();

                Weather weather;
                weather = ResponseProcesser.getWheatherFromJsonResponse(response);
                addWeather(weather);
                Log.d("WHEATHER_RESPONSE", response);

                return weather;
            } catch (Exception e) {
                Log.d("WHEATHER_RESPONSE", "Exception:(");
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Weather weather) {
            if (weather == null) {
                presenter.onWeatherLoadingError();
            } else {
                presenter.onWeatherLoaded();
            }
        }
    }
}
